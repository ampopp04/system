package com.system.db.repository.base.entity;

import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.specification.ByIdsSpecification;
import com.system.db.util.query.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.system.db.util.query.QueryUtils.*;
import static com.system.util.collection.CollectionUtils.iterate;
import static org.springframework.data.jpa.repository.query.QueryUtils.*;

/**
 * The <class>SystemRepositoryImpl</class> defines the implementation
 * of the {@link SystemRepository}
 *
 * @author Andrew
 * @see com.system.db.repository.base.named.NamedEntityRepository
 */
@NoRepositoryBean
public class SystemRepositoryImpl<T extends BaseEntity> implements SystemRepository<T> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * The entity manager that manages our entities
     */
    @Autowired
    protected EntityManager em;

    /**
     * The type of entity this repository executes queries for
     */
    protected EntityType entityType;

    /**
     * Used to provide lock mode for query execution against this entity type
     */
    protected CrudMethodMetadata metadata;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    /////////////////////////////////////////////////////////////////////

    public SystemRepositoryImpl() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                               Find One  Methods                                            //////////
    /////////////////////////////////////////////////////////////////////

    public T findOne(Specification<T> spec) {
        try {
            return getQuery(spec, (Sort) null).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public T getOne(T id) {
        return em.getReference(getDomainClass(), id);
    }

    public T findOne(T id) {
        return em.find(getDomainClass(), id);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Count Methods                                                  //////////
    /////////////////////////////////////////////////////////////////////

    public long count(Specification<T> spec) {
        return executeCountQuery(getCountQuery(spec, getEm(), getDomainClass()));
    }

    public long count() {
        return em.createQuery(getCountQueryString(getEntityTypeName()), Long.class).getSingleResult();
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Exists Methods                                               //////////
    /////////////////////////////////////////////////////////////////////

    public boolean exists(T id) {
        return findOne(id) != null;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Flush Methods                                              //////////
    /////////////////////////////////////////////////////////////////////

    @Transactional
    public void flush() {
        getEm().flush();
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Find Methods                                                     //////////
    /////////////////////////////////////////////////////////////////////

    public List<T> findAll() {
        return getQuery(null, (Sort) null).getResultList();
    }

    public List<T> findAll(Sort sort) {
        return getQuery(null, sort).getResultList();
    }

    public Page<T> findAll(Pageable pageable) {
        return (null == pageable) ? new PageImpl<>(findAll()) : findAll(null, pageable);
    }

    public List<T> findAll(Iterable<T> ids) {
        if (ids == null || !ids.iterator().hasNext()) {
            return Collections.emptyList();
        }

        if (getEntityInformation().hasCompositeId()) {
            List<T> results = new ArrayList<>();
            iterate(ids, (id) -> results.add(findOne(id)));
            return results;
        }

        ByIdsSpecification<T> specification = new ByIdsSpecification<>(getEntityInformation());
        return getQuery(specification, (Sort) null).setParameter(specification.getParameter(), ids).getResultList();
    }

    public List<T> findAll(Specification<T> spec) {
        return getQuery(spec, (Sort) null).getResultList();
    }

    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        TypedQuery<T> query = getQuery(spec, pageable);
        return pageable == null ? new PageImpl<>(query.getResultList()) : readPage(query, pageable, spec, getEm(), getDomainClass());
    }

    public List<T> findAll(Specification<T> spec, Sort sort) {
        return getQuery(spec, sort).getResultList();
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Delete Methods                                                   //////////
    /////////////////////////////////////////////////////////////////////

    @Transactional
    public void delete(T entity) {
        if (entity != null) {
            em.remove(em.contains(entity) ? entity : em.merge(entity));
        }
    }

    @Transactional
    public void delete(Iterable<? extends T> entities) {
        iterate(entities, (entity) -> delete(entity));
    }

    @Transactional
    public void deleteAll() {
        delete(findAll());
    }

    @Transactional
    public void deleteInBatch(Iterable<T> entities) {
        applyAndBind(getQueryString(DELETE_ALL_QUERY_STRING, getEntityTypeName()), entities, em).executeUpdate();
    }

    @Transactional
    public void deleteAllInBatch() {
        em.createQuery(getDeleteAllQueryString(getEntityTypeName())).executeUpdate();
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Save Methods                                                     //////////
    /////////////////////////////////////////////////////////////////////

    @Transactional
    public <S extends T> S save(S entity) {
        if (entity == null) {
            return entity;
        } else if (entity.isNew()) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Transactional
    public <S extends T> List<S> save(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        iterate(entities, (entity) -> result.add(save(entity)));
        return result;
    }

    @Transactional
    public <S extends T> S saveAndFlush(S entity) {
        S result = save(entity);
        flush();
        return result;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                               Advanced Getter                                                //////////
    /////////////////////////////////////////////////////////////////////

    protected Class<T> getDomainClass() {
        return this.entityType.getBindableJavaType();
    }

    public JpaEntityInformation getEntityInformation() {
        return JpaEntityInformationSupport.getEntityInformation(getDomainClass(), em);
    }

    protected TypedQuery<T> getQuery(Specification<T> spec, Pageable pageable) {
        Sort sort = pageable == null ? null : pageable.getSort();
        return getQuery(spec, sort);
    }

    protected TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {
        return QueryUtils.getQuery(spec, sort, getMetadata(), getEm(), getDomainClass());
    }

    public String getEntityTypeName() {
        return getEntityType().getName();
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Getter/Setters                                                  //////////
    /////////////////////////////////////////////////////////////////////

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public CrudMethodMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CrudMethodMetadata metadata) {
        this.metadata = metadata;
    }
}
