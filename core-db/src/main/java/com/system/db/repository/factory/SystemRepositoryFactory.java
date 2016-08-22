package com.system.db.repository.factory;

import com.system.db.repository.factory.metadata.SystemRepositoryMetadata;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AnnotationRepositoryMetadata;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;

/**
 * The <class>SystemRepositoryFactory</class> defines
 * our repository factory for creating any system repository.
 *
 * @author Andrew
 */
public class SystemRepositoryFactory extends JpaRepositoryFactory {

    /**
     * Creates a new {@link JpaRepositoryFactory}.
     *
     * @param entityManager must not be {@literal null}
     */
    public SystemRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * Returns the {@link RepositoryMetadata} for the given repository interface.
     *
     * @param repositoryInterface will never be {@literal null}.
     * @return
     */
    @Override
    protected RepositoryMetadata getRepositoryMetadata(Class<?> repositoryInterface) {
        Assert.notNull(repositoryInterface, "Repository interface must not be null!");

        return Repository.class.isAssignableFrom(repositoryInterface) ? new SystemRepositoryMetadata(repositoryInterface)
                : new AnnotationRepositoryMetadata(repositoryInterface);
    }
}