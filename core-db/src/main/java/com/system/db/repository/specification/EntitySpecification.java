package com.system.db.repository.specification;

import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.search.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;

/**
 * The <interface>EntitySpecification</interface> defines the
 * base database search predicate specification.
 *
 * @author Andrew
 * @see Specification
 */
public class EntitySpecification<T extends BaseEntity> implements Specification<T> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    private SearchCriteria criteria;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Constructor                                                       //////////
    //////////////////////////////////////////////////////////////////////

    public EntitySpecification(SearchCriteria criteria) {
        this.criteria = criteria;
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
    public Predicate toPredicate
    (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

    public Class<T> getType() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }
}
