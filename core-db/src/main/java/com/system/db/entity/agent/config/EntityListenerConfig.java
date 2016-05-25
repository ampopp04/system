package com.system.db.entity.agent.config;


import com.system.db.entity.agent.EntityAgent;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;


/**
 * The <class>EntityListenerConfig</class>  hooks into the spring boot life cycle.
 * <p>
 * It will be called immediately prior to beginning any spring life cycle logic.
 * <p>
 * This is useful for any code that needs to be executed before spring starts up.
 *
 * @author Andrew
 * @see EntityAgent
 */
@Component
public class EntityListenerConfig implements BeanFactoryPostProcessor, PriorityOrdered {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Processor Method                                        //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //Enable byte-code manipulation for Database Entities
        EntityAgent.install(ByteBuddyAgent.install());
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Getter/Setters                                              //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

}