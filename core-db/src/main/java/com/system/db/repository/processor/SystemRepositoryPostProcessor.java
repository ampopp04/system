package com.system.db.repository.processor;

import com.system.db.entity.base.BaseEntity;
import com.system.db.entity.named.NamedEntity;
import com.system.db.repository.base.entity.SystemRepositoryImpl;
import com.system.db.repository.base.named.NamedEntityRepositoryImpl;
import com.system.inversion.util.InversionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.system.db.util.entity.EntityUtils.getRepositoryName;
import static com.system.util.collection.CollectionUtils.iterate;

/**
 * The <interface>SystemRepositoryPostProcessor</interface> injects
 * new instances of {@link com.system.db.repository.base.entity.SystemRepository}
 * and {@link com.system.db.repository.base.named.NamedEntityRepository}
 * into the application context for each database {@link com.system.db.entity.Entity}
 * based on whether it is a {@link NamedEntity} or {@link com.system.db.entity.base.BaseEntity}
 * <p>
 * If the base repository retrieval methods are not sufficient these repositories can be extended
 * in which case no auto-injection will take place as it is assumed these entity specific repositories will take
 * precedent.
 *
 * @author Andrew
 * @see com.system.db.repository.base.entity.SystemRepository
 * @see com.system.db.repository.base.named.NamedEntityRepository
 * @see NamedEntity
 * @see com.system.db.entity.base.BaseEntity
 */
@Component
public class SystemRepositoryPostProcessor implements BeanPostProcessor {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The context used for bean definition creation
     */
    @Autowired
    private ApplicationContext context;

    /**
     * Ensure we only process this execution once on startup
     */
    private AtomicBoolean processed = new AtomicBoolean(false);

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Implementation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Not used
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * Process the {@link EntityManager}  bean and iterate over each of it's meta models entities to ensure
     * they have a repository defined within the context for use.
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof EntityManager && !processed.getAndSet(true)) {
            processEntityManager((EntityManager) bean);
        }
        return bean;
    }

    /**
     * Process each entity within the entity manager
     *
     * @param em
     */
    private void processEntityManager(EntityManager em) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) this.context.getAutowireCapableBeanFactory();
        iterate(em.getMetamodel().getEntities(), (entityType) -> processEntityType(beanFactory, entityType));
    }

    /**
     * Process a specific entity type, this would be any database {@link com.system.db.entity.Entity} that extends
     * {@link NamedEntity} or {@link BaseEntity}
     * <p>
     * These will only be processed if they currently do not have an applicable repository defined in the context for use.
     *
     * @param beanFactory
     * @param entityType
     */
    private void processEntityType(DefaultListableBeanFactory beanFactory, EntityType<?> entityType) {
        String repositoryBeanName = getRepositoryName(entityType.getName());

        if (!this.context.containsBeanDefinition(repositoryBeanName)) {
            registerBeanDefinition(beanFactory, repositoryBeanName, entityType);
        }
    }

    /**
     * Register a repository that is applicable for the given entity.
     * <p>
     * This will either be a named or base repository implementation based on the type of entity.
     *
     * @param beanFactory
     * @param beanName
     * @param entityType
     */
    private void registerBeanDefinition(DefaultListableBeanFactory beanFactory, String beanName, EntityType<?> entityType) {
        Class entityClass = (ClassUtils.isAssignable(entityType.getJavaType(), NamedEntity.class)) ? NamedEntityRepositoryImpl.class : SystemRepositoryImpl.class;

        MutablePropertyValues properties = new MutablePropertyValues();
        properties.addPropertyValue("entityType", entityType);

        InversionUtils.registerBeanDefinition(beanFactory, beanName, entityClass, properties);
    }
}