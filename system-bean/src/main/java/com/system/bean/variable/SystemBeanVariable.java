package com.system.bean.variable;

import com.system.bean.SystemBean;
import com.system.bean.type.SystemBeanType;
import com.system.bean.variable.definition.SystemBeanVariableDefinition;
import com.system.db.entity.base.BaseEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemBeanVariable</class> defines an instance
 * of a {@link SystemBeanVariableDefinition}
 *
 * @author Andrew
 */
public class SystemBeanVariable extends BaseEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_bean_variable_definition_id")
    private SystemBeanVariableDefinition systemBeanVariableDefinition;

    @ManyToOne
    @JoinColumn(name = "system_bean_id")
    private SystemBean systemBean;

    /**
     * This is a string representation of the value. Depending on the data-type backing the
     * variable definition this value will be interpreted differently.
     * <p>
     * It could be a string, an integer, a date, etc, or it could be linked to a
     * {@link SystemBeanType} in which case this value would represent the primary key ID field
     * on that bean. Which would be dynamically resolved via a database query.
     */
    private String value;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanVariable() {
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

    public SystemBean getSystemBean() {
        return systemBean;
    }

    public void setSystemBean(SystemBean systemBean) {
        this.systemBean = systemBean;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}