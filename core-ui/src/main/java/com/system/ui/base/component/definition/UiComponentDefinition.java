package com.system.ui.base.component.definition;

import com.system.ui.base.UiBase;
import com.system.ui.base.component.UiComponent;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Set;

import static com.system.util.collection.CollectionUtils.add;
import static com.system.util.collection.CollectionUtils.asSet;
import static javax.persistence.FetchType.EAGER;

/**
 * The <class>UiComponentDefinition</class> defines a
 * a definition component for the UI.
 * <p>
 * This would be a usual UI definition that is defined within the database
 * for the sake of flexibility.
 *
 * @author Andrew
 */
public class UiComponentDefinition extends UiBase<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * Defines which class this definition extends
     */
    private String extend;

    /**
     * Defines an easy to reference xtype for this definition to be used elsewhere
     */
    private String xtype;

    /**
     * Defines a controller that can accept events from this definition.
     */
    private String controller;

    /**
     * A set of required classes that are used within this definition.
     */
    @ElementCollection(fetch = EAGER)
    private Set<String> requires;

    /**
     * This UiComponent defines the underlying structure of this definition.
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ui_component_id")
    private UiComponent uiComponent;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                        //////////
    /////////////////////////////////////////////////////////////////////

    public void addRequire(String require) {
        add(getRequires(), require);
    }

    public Set<String> getRequires() {
        if (requires == null) {
            setRequires(asSet());
        }
        return requires;
    }

    public void setRequires(Set<String> requires) {
        this.requires = requires;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getXtype() {
        return xtype;
    }

    public void setXtype(String xtype) {
        this.xtype = xtype;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public UiComponent getUiComponent() {
        return uiComponent;
    }

    public void setUiComponent(UiComponent uiComponent) {
        this.uiComponent = uiComponent;
    }

}