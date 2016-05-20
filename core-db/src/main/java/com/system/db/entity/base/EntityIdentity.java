package com.system.db.entity.base;


import java.io.Serializable;

public interface EntityIdentity extends Serializable {

    public Serializable getId();


    public boolean isNew();

}