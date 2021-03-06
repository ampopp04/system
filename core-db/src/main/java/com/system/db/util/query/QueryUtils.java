/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.util.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.jpa.repository.query.QueryUtils.*;

/**
 * The <class>QueryUtils</class> defines
 * entity query related utility methods
 *
 * @author Andrew
 */
public class QueryUtils {

    /**
     * Placeholder for query counts
     */
    public static final String COUNT_QUERY_PLACEHOLDER = "x";

    /**
     * Query to delete all entities
     *
     * @return
     */
    public static String getDeleteAllQueryString(String entityName) {
        return getQueryString(DELETE_ALL_QUERY_STRING, entityName);
    }

    /**
     * Query to return count
     *
     * @return
     */
    public static String getCountQueryString(String entityName) {
        String countQuery = String.format(COUNT_QUERY_STRING, COUNT_QUERY_PLACEHOLDER, "%s");
        return getQueryString(countQuery, entityName);
    }

    /**
     * Reads the given {@link TypedQuery} into a {@link Page} applying the given {@link Pageable} and
     * {@link Specification}.
     *
     * @param query    must not be {@literal null}.
     * @param spec     can be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return
     */
    public static <T> Page<T> readPage(TypedQuery<T> query, Pageable pageable, Specification<T> spec, EntityManager em, Class<T> entityType) {
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        Long total = executeCountQuery(getCountQuery(spec, em, entityType));
        List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Creates a new count query for the given {@link Specification}.
     *
     * @param spec can be {@literal null}.
     * @return
     */
    public static <T> TypedQuery<Long> getCountQuery(Specification<T> spec, EntityManager em, Class<T> entityType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<T> root = applySpecificationToCriteria(spec, query, em, entityType);

        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }

        return em.createQuery(query);
    }

    /**
     * Executes a count query and transparently sums up all values returned.
     *
     * @param query must not be {@literal null}.
     * @return
     */
    public static Long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query);
        List<Long> totals = query.getResultList();
        Long total = 0L;

        for (Long element : totals) {
            total += element == null ? 0 : element;
        }
        return total;
    }


    /**
     * Creates a {@link TypedQuery} for the given {@link Specification} and {@link Sort}.
     *
     * @param spec can be {@literal null}.
     * @param sort can be {@literal null}.
     * @return
     */
    public static <T> TypedQuery<T> getQuery(Specification<T> spec, Sort sort, CrudMethodMetadata metadata, EntityManager em, Class<T> entityType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityType);

        Root<T> root = applySpecificationToCriteria(spec, query, em, entityType);
        query.select(root);

        if (sort != null) {
            query.orderBy(toOrders(sort, root, builder));
        }

        return applyRepositoryMethodMetadata(metadata, em.createQuery(query));
    }

    /**
     * Applies the given {@link Specification} to the given {@link CriteriaQuery}.
     *
     * @param spec  can be {@literal null}.
     * @param query must not be {@literal null}.
     * @return
     */
    private static <S, T> Root<T> applySpecificationToCriteria(Specification<T> spec, CriteriaQuery<S> query, EntityManager em, Class<T> entityType) {
        Assert.notNull(query);
        Root<T> root = query.from(entityType);

        if (spec == null) {
            return root;
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return root;
    }

    private static <T> TypedQuery<T> applyRepositoryMethodMetadata(CrudMethodMetadata metadata, TypedQuery<T> query) {
        if (metadata == null) {
            return query;
        }

        LockModeType type = metadata.getLockModeType();
        return type == null ? query : query.setLockMode(type);
    }
}
