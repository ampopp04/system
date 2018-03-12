/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.bean.variable.definition.modifier.type;

import com.system.bean.modifier.type.SystemBeanModifierType;
import com.system.bean.variable.definition.SystemBeanVariableDefinition;
import com.system.db.entity.base.BaseEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemBeanVariableDefinitionModifierType</class> defines a
 * many to many relationship between
 * {@link SystemBeanVariableDefinition}
 * {@link SystemBeanVariableDefinitionModifierType}
 * with an order on how these modifier types are applied
 *
 * @author Andrew
 */
public class SystemBeanVariableDefinitionModifierType extends BaseEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_bean_variable_definition_id")
    private SystemBeanVariableDefinition systemBeanVariableDefinition;

    @ManyToOne
    @JoinColumn(name = "system_bean_modifier_type_id")
    private SystemBeanModifierType systemBeanModifierType;

    private Integer modifierOrder;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanVariableDefinitionModifierType() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanVariableDefinition getSystemBeanVariableDefinition() {
        return systemBeanVariableDefinition;
    }

    public void setSystemBeanVariableDefinition(SystemBeanVariableDefinition systemBeanVariableDefinition) {
        this.systemBeanVariableDefinition = systemBeanVariableDefinition;
    }

    public SystemBeanModifierType getSystemBeanModifierType() {
        return systemBeanModifierType;
    }

    public void setSystemBeanModifierType(SystemBeanModifierType systemBeanModifierType) {
        this.systemBeanModifierType = systemBeanModifierType;
    }

    public Integer getModifierOrder() {
        return modifierOrder;
    }

    public void setModifierOrder(Integer modifierOrder) {
        this.modifierOrder = modifierOrder;
    }
}