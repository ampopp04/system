package com.system.bean.definition;

import com.system.bean.definition.type.SystemBeanDefinitionType;
import com.system.db.entity.named.NamedEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemBeanDefinition</class> defines a specific instance of a
 * {@link SystemBeanDefinitionType}. This class would represent a definition of a
 * java interface, annotation, abstract class, etc.
 *
 * @author Andrew
 */
public class SystemBeanDefinition extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_bean_definition_type_id")
    private SystemBeanDefinitionType systemBeanDefinitionType;

    /**
     * Represents the fully qualified package and class name
     */
    private String className;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanDefinition() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanDefinitionType getSystemBeanDefinitionType() {
        return systemBeanDefinitionType;
    }

    public void setSystemBeanDefinitionType(SystemBeanDefinitionType systemBeanDefinitionType) {
        this.systemBeanDefinitionType = systemBeanDefinitionType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}