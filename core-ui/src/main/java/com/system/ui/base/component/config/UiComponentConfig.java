package com.system.ui.base.component.config;

import com.system.db.entity.base.BaseEntity;
import com.system.ui.base.component.config.attribute.UiComponentConfigAttribute;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

import static com.system.util.collection.CollectionUtils.add;
import static com.system.util.collection.CollectionUtils.newList;

/**
 * The <class>UiComponentConfig</class> defines a list of UiComponentConfigAttributes
 *
 * @author Andrew
 */
public class UiComponentConfig extends BaseEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(joinColumns = @JoinColumn(name = "ui_component_config_id", referencedColumnName = ID_TYPE_NAME_LOWERCASE), inverseJoinColumns = @JoinColumn(name = "ui_component_config_attribute_id", referencedColumnName = ID_TYPE_NAME_LOWERCASE))
    List<UiComponentConfigAttribute> uiComponentConfigAttributeList;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                        //////////
    /////////////////////////////////////////////////////////////////////

    public void addUiComponentConfigAttribute(UiComponentConfigAttribute uiComponentConfigAttribute) {
        add(getUiComponentConfigAttributeList(), uiComponentConfigAttribute);
    }

    public List<UiComponentConfigAttribute> getUiComponentConfigAttributeList() {
        if (uiComponentConfigAttributeList == null) {
            setUiComponentConfigAttributeList(newList());
        }
        return uiComponentConfigAttributeList;
    }

    public void setUiComponentConfigAttributeList(List<UiComponentConfigAttribute> uiComponentConfigAttributeList) {
        this.uiComponentConfigAttributeList = uiComponentConfigAttributeList;
    }
}
