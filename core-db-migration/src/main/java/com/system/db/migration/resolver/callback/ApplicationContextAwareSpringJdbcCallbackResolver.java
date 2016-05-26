package com.system.db.migration.resolver.callback;


import com.system.util.collection.CollectionUtils;
import org.flywaydb.core.api.callback.FlywayCallback;
import org.springframework.context.ApplicationContext;

/**
 * The <class>ApplicationContextAwareSpringJdbcCallbackResolver</class> resolves
 * all Flyway callbacks for flyway callback registration
 *
 * @author Andrew
 */
public class ApplicationContextAwareSpringJdbcCallbackResolver {

    /**
     * The context used to scan for our migration callbacks
     */
    private final ApplicationContext applicationContext;

    public ApplicationContextAwareSpringJdbcCallbackResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public FlywayCallback[] resolveCallbacks() {
        return CollectionUtils.asArray(CollectionUtils.asList(applicationContext.getBeansOfType(FlywayCallback.class)));
    }
}