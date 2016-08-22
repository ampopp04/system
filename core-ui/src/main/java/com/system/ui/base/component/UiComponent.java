package com.system.ui.base.component;

import com.system.ui.base.UiBase;
import com.system.ui.base.component.config.UiComponentConfig;
import com.system.ui.base.component.config.attribute.UiComponentConfigAttribute;
import com.system.ui.base.component.type.UiComponentType;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;


/**
 * The <class>UiBase</class> defines
 * the base UI entity
 *
 * @author Andrew
 */
public class UiComponent extends UiBase<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "ui_component_type_id")
    private UiComponentType uiComponentType;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ui_component_config_id")
    private UiComponentConfig uiComponentConfig;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                         //////////
    /////////////////////////////////////////////////////////////////////

    public UiComponentConfig getUiComponentConfig() {
        if (uiComponentConfig == null) {
            setUiComponentConfig(new UiComponentConfig());
        }

        return uiComponentConfig;
    }

    public void setUiComponentConfig(UiComponentConfig uiComponentConfig) {
        this.uiComponentConfig = uiComponentConfig;
    }

    public List<UiComponentConfigAttribute> getUiComponentConfigAttributeList() {
        return getUiComponentConfig().getUiComponentConfigAttributeList();
    }

    public UiComponentType getUiComponentType() {
        return uiComponentType;
    }

    public void setUiComponentType(UiComponentType uiComponentType) {
        this.uiComponentType = uiComponentType;
    }
}
