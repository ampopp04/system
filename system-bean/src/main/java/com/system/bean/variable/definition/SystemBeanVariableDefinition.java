package com.system.bean.variable.definition;

import com.system.bean.type.SystemBeanType;
import com.system.bean.variable.definition.modifier.type.SystemBeanVariableDefinitionModifierType;
import com.system.db.entity.named.NamedEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;

/**
 * The <class>SystemBeanVariableDefinition</class> defines a variables structure.
 * <p>
 * /---Modifiers---/--SystemBeanType--/--name--/
 * private static final                 String                   myString
 *
 * @author Andrew
 */
public class SystemBeanVariableDefinition extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_bean_type_id")
    private SystemBeanType systemBeanType;

    @OneToMany(mappedBy = "systemBeanVariableDefinition")
    private Collection<SystemBeanVariableDefinitionModifierType> SystemBeanVariableDefinitionModifierTypes;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanVariableDefinition() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanType getSystemBeanType() {
        return systemBeanType;
    }

    public void setSystemBeanType(SystemBeanType systemBeanType) {
        this.systemBeanType = systemBeanType;
    }

    public Collection<SystemBeanVariableDefinitionModifierType> getSystemBeanVariableDefinitionModifierTypes() {
        return SystemBeanVariableDefinitionModifierTypes;
    }

    public void setSystemBeanVariableDefinitionModifierTypes(Collection<SystemBeanVariableDefinitionModifierType> systemBeanVariableDefinitionModifierTypes) {
        SystemBeanVariableDefinitionModifierTypes = systemBeanVariableDefinitionModifierTypes;
    }
}