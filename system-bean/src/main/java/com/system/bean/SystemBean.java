package com.system.bean;

import com.system.bean.type.SystemBeanType;
import com.system.db.entity.named.NamedEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemBean</class> defines an instance
 * of a {@link SystemBeanType}
 *
 * @author Andrew
 */
public class SystemBean extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_bean_type_id")
    private SystemBeanType SystemBeanType;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBean() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public com.system.bean.type.SystemBeanType getSystemBeanType() {
        return SystemBeanType;
    }

    public void setSystemBeanType(com.system.bean.type.SystemBeanType systemBeanType) {
        SystemBeanType = systemBeanType;
    }
}