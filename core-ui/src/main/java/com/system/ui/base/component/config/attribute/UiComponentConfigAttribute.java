/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.ui.base.component.config.attribute;

import com.system.db.entity.base.BaseEntity;
import com.system.ui.base.component.UiComponent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>UiComponentConfigAttribute</class> defines a
 * key attributeValue attribute pair.  If the attributeValue is null then the attributeValue will be
 * the UiComponent which allows for a rich set of attributes.
 *
 * @author Andrew
 */
public class UiComponentConfigAttribute extends BaseEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * The key defining the attribute for the UI.
     */
    private String attributeKey;

    /**
     * Either the UiComponent for a nested attributeValue or the provided attributeValue.
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ui_component_id")
    private UiComponent uiComponent;

    /**
     * The string attributeValue to use instead of a UiComponent if specified.
     */
    @Column(length = 65000, columnDefinition = "TEXT")
    private String attributeValue;

    /**
     * Defines the order this attribute is applied relative to any others
     */
    private Integer displayOrder;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                        //////////
    /////////////////////////////////////////////////////////////////////

    public UiComponent getUiComponent() {
        return uiComponent;
    }

    public void setUiComponent(UiComponent uiComponent) {
        this.uiComponent = uiComponent;
    }

    public String getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}