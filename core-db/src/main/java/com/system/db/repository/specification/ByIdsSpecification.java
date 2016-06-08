package com.system.db.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.Parameter;
import javax.persistence.criteria.*;


/**
 * The <class>ByIdsSpecification</class> gives access to the {@link Parameter} instance used to bind the
 * ids for {@link com.system.db.repository.base.entity.SystemRepositoryImpl#findAll(Iterable)}.
 * Workaround for OpenJPA not binding collections to in-clauses correctly when using by-name binding.
 *
 * @author Andrew
 */
public class ByIdsSpecification<T> implements Specification<T> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    private final JpaEntityInformation<T, ?> entityInformation;
    private ParameterExpression<Iterable> parameter;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    /////////////////////////////////////////////////////////////////////

    public ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
        this.entityInformation = entityInformation;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Implementation                                                  //////////
    /////////////////////////////////////////////////////////////////////

    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return root.get(entityInformation.getIdAttribute()).in(cb.parameter(Iterable.class));
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Getter/Setters                                                //////////
    /////////////////////////////////////////////////////////////////////

    public JpaEntityInformation<T, ?> getEntityInformation() {
        return entityInformation;
    }

    public ParameterExpression<Iterable> getParameter() {
        return parameter;
    }

    public void setParameter(ParameterExpression<Iterable> parameter) {
        this.parameter = parameter;
    }
}