package com.system.db.repository.base.named;

import com.system.db.entity.named.NamedEntity;
import com.system.db.repository.base.entity.SystemRepositoryImpl;
import com.system.db.repository.search.criteria.SearchCriteria;
import com.system.db.repository.specification.EntitySpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The <interface>NamedEntityRepositoryImpl</interface> defines implementation
 * for the {@link NamedEntityRepository} interface which is an
 * extension of the {@link SystemRepositoryImpl}
 *
 * @author Andrew
 * @SystemRepositoryImpl
 * @see NamedEntityRepository
 */
@NoRepositoryBean
public class NamedEntityRepositoryImpl<T extends NamedEntity> extends SystemRepositoryImpl<T> implements NamedEntityRepository<T> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Constructor                                                    //////////
    /////////////////////////////////////////////////////////////////////

    public NamedEntityRepositoryImpl() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Implementation                                                  //////////
    /////////////////////////////////////////////////////////////////////

    public T findByName(String name) {
        return getQuery(new EntitySpecification<>(new SearchCriteria("name", ":", name)), (Sort) null).getSingleResult();
    }
}
