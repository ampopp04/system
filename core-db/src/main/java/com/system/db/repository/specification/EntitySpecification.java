/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.repository.specification;

import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.search.criteria.SearchCriteria;
import com.system.db.repository.search.criteria.property.SearchCriteriaPropertyOperators;
import com.system.logging.exception.util.ExceptionUtils;
import com.system.util.clazz.ClassUtils;
import com.system.util.compare.CompareUtils;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.iterate;
import static com.system.util.string.StringUtils.isNotEmpty;
import static javax.persistence.metamodel.Attribute.PersistentAttributeType.BASIC;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * The <interface>EntitySpecification</interface> defines the
 * base database search predicate specification.
 *
 * @author Andrew
 * @see Specification
 */
public class EntitySpecification<T> implements Specification<T> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////
    public static final String NULL_PROPERTY_STRING = "null-all-property";

    private SearchCriteria criteria;
    private Integer maxSearchDepth = 10;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Constructor                                                       //////////
    //////////////////////////////////////////////////////////////////////

    public EntitySpecification(SearchCriteria criteria, Integer maxSearchDepth) {
        this.criteria = criteria;
        this.maxSearchDepth = maxSearchDepth;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                               Implementation                                                     //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a {@link Predicate} for the given
     * {@link Root} and {@link CriteriaQuery}.
     *
     * @param root
     * @param query
     * @return a {@link Predicate}, must not be {@literal null}.
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        EntitySpecificationOperators operator = EntitySpecificationOperators.fromName(criteria.getOperator());

        if (operator != null) {

            if (NULL_PROPERTY_STRING.equals(criteria.getProperty())) {
                criteria.setProperty(null);
            }

            if (criteria.getProperty() != null) {
                return performCriteriaToPredicate(operator, root, builder);
            } else {
                return getNullCriteriaPropertyPredicate(operator, root, criteria, builder);
            }
        }

        return null;
    }

    /**
     * Split a criteria property on blank spaces and process compound predicates based on logical operators such as
     * NOT EQUAL, OR, AND
     * !, ||, &&
     */
    private Predicate performCriteriaToPredicate(EntitySpecificationOperators operator, Root<T> root, CriteriaBuilder builder) {
        Predicate resultPredicate = null;

        String[] propertyExpressionPaths = split(criteria.getProperty(), " ");

        boolean onOperator = false;
        SearchCriteriaPropertyOperators currentPropertyOperator = null;

        for (String propertyPath : iterable(propertyExpressionPaths)) {
            criteria.setProperty(propertyPath);

            if (resultPredicate == null) {
                resultPredicate = operator.toPredicate(root, criteria, builder);
                onOperator = true;
            } else {
                if (onOperator) {
                    currentPropertyOperator = SearchCriteriaPropertyOperators.fromString(propertyPath);
                    onOperator = false;
                } else {
                    resultPredicate = currentPropertyOperator.processCompoundLogicalOperator(builder, resultPredicate, operator.toPredicate(root, criteria, builder));
                    onOperator = true;
                }
            }

        }

        return resultPredicate;
    }

    private Predicate getNullCriteriaPropertyPredicate(EntitySpecificationOperators operator, Root root, SearchCriteria criteria, CriteriaBuilder builder) {
        List<SearchCriteria> completeTableColumnSearchCriteriaList = getCompleteEntityTablePathExpression(root, criteria, builder, root.getJavaType(), null, 0);

        List<Predicate> predicateList = new ArrayList<>();

        for (SearchCriteria currentSearchCriteria : iterable(completeTableColumnSearchCriteriaList)) {
            predicateList.add(operator.toPredicate(root, currentSearchCriteria, builder));
        }

        return builder.or(predicateList.toArray(new Predicate[0]));
    }

    private List<SearchCriteria> getCompleteEntityTablePathExpression(From root, SearchCriteria criteria, CriteriaBuilder builder, Class javaType, String attributePathPrefix, Integer searchDepth) {
        List<SearchCriteria> criteriaList = new ArrayList<>();

        if (searchDepth > maxSearchDepth) {
            return criteriaList;
        }

        try {
            Set<Attribute> attributeSet = ((CriteriaBuilderImpl) builder).getEntityManagerFactory().getMetamodel().entity(javaType).getAttributes();

            iterate(iterable(attributeSet), attribute -> {
                boolean basicAttribute = attribute.getPersistentAttributeType() == BASIC;

                if (basicAttribute) {

                    String currentAttributePath = getBasicEntityAttributePath(attribute);
                    currentAttributePath = isNotEmpty(attributePathPrefix) ? attributePathPrefix + "." + currentAttributePath : currentAttributePath;
                    criteriaList.add(new SearchCriteria(currentAttributePath, criteria.getOperator(), criteria.getValue()));

                } /*else if (attribute.isCollection()) {

                    CollectionAttribute collectionAttribute = (CollectionAttribute) attribute;

                    System.out.println("Attribute: " + collectionAttribute.getName() + " - " + collectionAttribute.getElementType().getJavaType() + " - " + attribute.isCollection() + " - " + attribute.getClass().getSimpleName());

                    criteriaList.addAll(getCompleteEntityTablePathExpression(root.join(collectionAttribute), criteria, builder, collectionAttribute.getElementType().getJavaType(), collectionAttribute.getName(), searchDepth + 1));

                } */ else if (ClassUtils.isAssignable(attribute.getJavaType(), BaseEntity.class) && !attribute.isCollection()) {

                    SingularAttribute entityAttribute = ((SingularAttribute) attribute);

                    if (joinAllowed(root, entityAttribute)) {
                        String currentAttributePath = getBasicEntityAttributePath(attribute);
                        currentAttributePath = isNotEmpty(attributePathPrefix) ? attributePathPrefix + "." + currentAttributePath : currentAttributePath;
                        criteriaList.addAll(getCompleteEntityTablePathExpression(root.join(entityAttribute, JoinType.LEFT), criteria, builder, entityAttribute.getJavaType(), currentAttributePath, searchDepth + 1));
                    }

                }
            });

        } catch (Exception e) {
            ExceptionUtils.throwSystemException("Advanced Search Failed", e);
        }

        return criteriaList;
    }

    private boolean joinAllowed(From parent, SingularAttribute entityAttribute) {
        From parentEntity = parent;

        do {

            Set<Join> parentJoinSet = (Set<Join>) parentEntity.getJoins();
            for (Join parentJoin : iterable(parentJoinSet)) {
                if (CompareUtils.equals(parentJoin.getJavaType(), entityAttribute.getJavaType())) {
                    return false;
                }
            }

            Path parentEntityPath = parentEntity.getParentPath();
            parentEntity = parentEntityPath != null && parentEntityPath instanceof From ? (From) parentEntityPath : null;

        } while (parentEntity != null);

        return true;
    }

    private String getBasicEntityAttributePath(Attribute basicAttribute) {
        return basicAttribute.getName();
    }


    public Class<T> getType() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }
}
