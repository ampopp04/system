package com.system.db.repository.specification.builder;

import com.system.db.repository.search.criteria.SearchCriteria;
import com.system.db.repository.specification.EntitySpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

/**
 * The <interface>EntitySpecification</interface> defines the
 * base database search predicate specification.
 *
 * @author Andrew
 * @see Specification
 */
public class EntitySpecificationBuilder<T> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    private final List<SearchCriteria> params;
    private Integer searchDepth;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Constructor                                                       //////////
    //////////////////////////////////////////////////////////////////////


    public EntitySpecificationBuilder(Integer searchDepth) {
        params = new ArrayList<>();
        this.searchDepth = searchDepth;
    }
    ///////////////////////////////////////////////////////////////////////
    ////////                                               Implementation                                                     //////////
    //////////////////////////////////////////////////////////////////////


    public EntitySpecificationBuilder with(String key, String operation, String value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<T>> specs = new ArrayList<>();

        for (SearchCriteria param : params) {
            specs.add(new EntitySpecification<>(param, searchDepth));
        }

        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}
