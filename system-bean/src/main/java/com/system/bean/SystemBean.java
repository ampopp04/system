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
    private SystemBeanType systemBeanType;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBean() {
    }

    public static SystemBean newInstance(String name, String description, SystemBeanType systemBeanType) {
        SystemBean entity = new SystemBean();
        entity.setName(name);
        entity.setDescription(description);
        entity.setSystemBeanType(systemBeanType);
        return entity;
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

}