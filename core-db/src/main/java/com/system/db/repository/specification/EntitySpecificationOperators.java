/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.repository.specification;

import com.system.db.repository.search.criteria.SearchCriteria;
import com.system.util.compare.CompareUtils;
import org.hibernate.query.criteria.internal.path.PluralAttributePath;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.PluralAttribute;
import java.util.Set;

import static com.system.util.collection.CollectionUtils.iterable;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * The <class>EntitySpecificationOperators</class> defines the
 * various operators that can be performed in a search criteria against
 * a database specification query.
 * <p>
 * <p>
 * Supported Operators
 * <
 * <=
 * =
 * >=
 * >
 * !=
 * in
 * notin
 * like
 * <p>
 *
 * @author Andrew
 * @see EntitySpecification
 */
public enum EntitySpecificationOperators {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    LESS_THAN("<") {
        @Override
        Predicate toPredicate(Root root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.lessThan(getExpressionPath(root, criteria.getProperty()), criteria.getValue().toString());
        }
    },
    LESS_THAN_EQUAL("<=") {
        @Override
        Predicate toPredicate(Root root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.lessThanOrEqualTo(getExpressionPath(root, criteria.getProperty()), criteria.getValue().toString());
        }
    },

    GREATER_THAN(">") {
        @Override
        Predicate toPredicate(Root root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.greaterThan(getExpressionPath(root, criteria.getProperty()), criteria.getValue().toString());
        }
    },

    GREATER_THAN_EQUAL(">=") {
        @Override
        Predicate toPredicate(Root root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.greaterThanOrEqualTo(getExpressionPath(root, criteria.getProperty()), criteria.getValue().toString());
        }
    },

    EQUAL("=") {
        @Override
        Predicate toPredicate(Root root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.equal(getExpressionPath(root, criteria.getProperty()), criteria.getValue().toString());
        }
    },
    NOT_EQUAL("!=") {
        @Override
        Predicate toPredicate(Root root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.notEqual(getExpressionPath(root, criteria.getProperty()), criteria.getValue().toString());
        }
    },

    IN("in") {
        @Override
        Predicate toPredicate(Root root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.in(getExpressionPath(root, criteria.getValue().toString()));
        }
    },
    NOTIN("notin") {
        @Override
        Predicate toPredicate(Root root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.not(IN.toPredicate(root, criteria, builder));
        }
    },

    LIKE("like") {
        @Override
        Predicate toPredicate(Root root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.like(builder.upper(getExpressionPath(root, criteria.getProperty())), "%" + criteria.getValue().toString().toUpperCase() + "%");
        }
    };

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////

    abstract Predicate toPredicate(Root root, SearchCriteria criteria, CriteriaBuilder builder);

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////

    private final String operator;

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////

    EntitySpecificationOperators(final String operator) {
        this.operator = operator;
    }

    private static Expression<String> getExpressionPath(Root root, String path) {
        if (isEmpty(path)) {
            return null;
        }
        return doGetExpressionPath(root, path);
    }

    private static Expression<String> doGetExpressionPath(Root root, String path) {
        String[] paths = path.split("\\.");
        Path<String> criteriaPath = null;

        for (String pathValue : iterable(paths)) {
            if (criteriaPath == null) {

                try {
                    criteriaPath = root.<String>get(pathValue);
                } catch (Exception e) {
                }

            } else {
                if (criteriaPath instanceof PluralAttributePath) {
                    final PluralAttribute pluralAttribute = ((PluralAttributePath) criteriaPath).getAttribute();
                    if (pluralAttribute.isCollection()) {
                        Join collectionJoin = ((Set<Join>) root.getJoins())
                                .stream()
                                .filter(join ->
                                        join.getAttribute().isCollection() && CompareUtils.equals(join.getJavaType(), pluralAttribute.getElementType().getJavaType()))
                                .findFirst()
                                .get();

                        criteriaPath = collectionJoin.get(pathValue);
                    }
                } else {
                    criteriaPath = criteriaPath.get(pathValue);
                }
            }
        }

        return criteriaPath != null ? criteriaPath.as(String.class) : null;
    }

    public static EntitySpecificationOperators fromName(String name) {
        for (EntitySpecificationOperators specificationOperator : EntitySpecificationOperators.values()) {
            if (specificationOperator.operator.equalsIgnoreCase(name)) {
                return specificationOperator;
            }
        }
        //If null or not found return like comparison
        return LIKE;
    }

    @Override
    public String toString() {
        return operator;
    }


}