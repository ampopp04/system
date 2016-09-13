package com.system.ui.base.component.config.attribute;

import com.system.db.entity.base.BaseEntity;
import com.system.ui.base.component.UiComponent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>UiComponentConfigAttribute</class> defines a
 * key value attribute pair.  If the value is null then the value will be
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
    private String key;

    /**
     * Either the UiComponent for a nested value or the provided value.
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ui_component_id")
    private UiComponent uiComponent;

    /**
     * The string value to use instead of a UiComponent if specified.
     */
    @Column(length = 65000)
    private String value;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}