/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.entity.transformer;


import com.system.db.entity.base.BaseEntity;
import com.system.manipulator.transformer.util.TransformerUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.persistence.Entity;


/**
 * The <class>EntityTransformer</class>  hooks into the spring boot life cycle.
 * <p>
 * It will be called immediately prior to beginning any spring lifecycle logic.
 * <p>
 * This is useful for any code that needs to be executed before spring starts up.
 * <p>
 * Any class extending BaseEntity will have the @Entity annotation injected into it at runtime
 *
 * @author Andrew
 */
public class EntityTransformer implements PriorityOrdered, SpringApplicationRunListener {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                               Constructor                                            //////////
    //////////////////////////////////////////////////////////////////////

    public EntityTransformer(SpringApplication application, String[] args) {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Processor Method                                        //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public void starting() {
    }


    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        //Any object extending BaseEntity is a JPA Entity,
        //therefore inject the @Entity annotation into it
        TransformerUtils.addAnnotationToClass(Entity.class, BaseEntity.class);
    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Getter/Setters                                              //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }
}