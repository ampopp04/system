package com.system.db.entity;


import com.system.db.entity.base.EntityIdentity;

import java.io.Serializable;

public interface Entity<T extends Serializable> extends EntityIdentity {

    public boolean isNew();


    public T getId();


    public void setId(T id);
}
