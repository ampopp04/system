package com.system.bean.type;

import com.system.bean.definition.SystemBeanDefinition;
import com.system.db.entity.named.NamedEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemBeanType</class> defines a specific instance of a
 * {@link SystemBeanDefinition}. This class would represent a
 * an actual instance of an Interface or class.
 *
 * @author Andrew
 */
public class SystemBeanType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_bean_definition_id")
    private SystemBeanDefinition systemBeanDefinition;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanType() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanDefinition getSystemBeanDefinition() {
        return systemBeanDefinition;
    }

    public void setSystemBeanDefinition(SystemBeanDefinition systemBeanDefinition) {
        this.systemBeanDefinition = systemBeanDefinition;
    }
}