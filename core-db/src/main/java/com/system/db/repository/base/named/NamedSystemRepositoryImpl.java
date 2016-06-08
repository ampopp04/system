package com.system.db.repository.base.named;

import com.system.db.entity.named.NamedEntity;
import com.system.db.repository.base.entity.SystemRepositoryImpl;
import com.system.db.repository.search.criteria.SearchCriteria;
import com.system.db.repository.specification.EntitySpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The <interface>NamedSystemRepositoryImpl</interface> defines implementation
 * for the {@link NamedEntityRepository} interface which is an
 * extension of the {@link SystemRepositoryImpl}
 *
 * @author Andrew
 * @SystemRepositoryImpl
 * @see NamedEntityRepository
 */
@NoRepositoryBean
public class NamedSystemRepositoryImpl<T extends NamedEntity> extends SystemRepositoryImpl<T> implements NamedEntityRepository<T> {

    public NamedSystemRepositoryImpl() {
    }

    public T findByName(String name) {
        Specification<T> spec = new EntitySpecification<>(new SearchCriteria("name", ":", name));
        return getQuery(spec, null).getSingleResult();
    }
}
