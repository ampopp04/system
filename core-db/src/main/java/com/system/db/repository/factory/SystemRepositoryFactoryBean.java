package com.system.db.repository.factory;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;

/**
 * The <class>SystemRepositoryFactoryBean</class> defines
 * our repository factory bean for creating our repository factory.
 *
 * @author Andrew
 */
public class SystemRepositoryFactoryBean extends JpaRepositoryFactoryBean {

    /**
     * Returns a {@link RepositoryFactorySupport}.
     *
     * @param entityManager
     * @return
     */
    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new SystemRepositoryFactory(entityManager);
    }
}