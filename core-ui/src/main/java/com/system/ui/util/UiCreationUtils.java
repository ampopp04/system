package com.system.ui.util;

import com.system.db.entity.base.BaseEntity;
import com.system.ui.base.component.UiComponent;
import com.system.ui.base.component.config.UiComponentConfig;
import com.system.ui.base.component.config.attribute.UiComponentConfigAttribute;
import com.system.ui.base.component.definition.UiComponentDefinition;
import com.system.ui.base.component.type.UiComponentType;
import com.system.util.collection.Pair;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.iterate;
import static com.system.util.string.StringUtils.arrayToString;

/**
 * The <class>UiCreationUtils</class> defines
 * ui creation related utility methods
 *
 * @author Andrew
 */
public class UiCreationUtils {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                     //////////
    /////////////////////////////////////////////////////////////////////

    public static final String TOOLBAR_COMPONENT_TYPE = "Toolbar";
    public static final String PANEL_COMPONENT_TYPE = "Panel";
    public static final String ITEMS_COMPONENT_TYPE = "Items";
    public static final String TOOLBAR_MENU_COMPONENT_TYPE = "Toolbar Menu";

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * Create a new shallow UiComponent
     *
     * @param name
     * @param description
     * @param componentType
     * @return
     */
    public static UiComponent createUiToolbar(String name, String description, UiComponentType componentType) {
        UiComponent uiToolbar = createUiComponent(name, description, componentType);
        uiToolbar.setName(name);
        uiToolbar.setDescription(description);
        return uiToolbar;
    }

    /**
     * Create Ui Component Config Attribute for an array of pairs
     *
     * @param pair
     * @return
     */
    public static UiComponentConfigAttribute createUiComponentConfigAttributeForPair(Pair<String, ?> pair) {
        UiComponentConfigAttribute pairComponentConfigAttribute;

        if (pair.getValue() instanceof String) {
            pairComponentConfigAttribute = createUiComponentConfigAttribute(null, pair.getKey(), (String) pair.getValue());
        } else if (pair.getValue() instanceof UiComponent) {
            pairComponentConfigAttribute = createUiComponentConfigAttribute((UiComponent) pair.getValue(), pair.getKey(), null);
        } else {
            pairComponentConfigAttribute = (UiComponentConfigAttribute) pair.getValue();
        }

        return pairComponentConfigAttribute;
    }

    /**
     * Create an items attribute.
     * This is an attribute whose value is provided by the uiComponent
     * and whose key is "items"
     *
     * @param uiComponent
     * @return
     */
    public static UiComponentConfigAttribute createItemsAttribute(UiComponent uiComponent) {
        return createUiComponentConfigAttribute(uiComponent, "items", null);
    }

    /**
     * Create an menu attribute.
     * This is an attribute whose value is provided by the uiComponent
     * and whose key is "menu"
     *
     * @param uiComponent
     * @return
     */
    public static UiComponentConfigAttribute createMenuAttribute(UiComponent uiComponent) {
        return createUiComponentConfigAttribute(uiComponent, "menu", null);
    }

    /**
     * Create a nested Ui Component Config Attribute by key and values
     *
     * @param name
     * @param description
     * @param uiComponentType
     * @param pairs
     * @return
     */
    public static UiComponentConfigAttribute createUiCompontConfigAttributeByKeyValues(String name, String description, UiComponentType uiComponentType, Pair<String, String>... pairs) {
        UiComponent component = createUiComponent(name, description, uiComponentType);
        UiComponentConfigAttribute attribute = createUiComponentConfigAttribute(component, null, null);

        UiComponentConfig componentConfig = createUiComponentConfig();
        component.setUiComponentConfig(componentConfig);

        iterate(iterable(pairs), (pair) ->
                componentConfig.addUiComponentConfigAttribute(createUiComponentConfigAttribute(null, pair.getKey(), pair.getValue())));

        return attribute;
    }

    /**
     * Create a basic Ui Component object
     *
     * @param name
     * @param description
     * @param uiComponentType
     * @return
     */
    public static UiComponent createUiComponent(String name, String description, UiComponentType uiComponentType) {
        UiComponent uiComponent = new UiComponent();
        uiComponent.setName(name);
        uiComponent.setDescription(description);
        if (uiComponentType != null) {
            uiComponent.setUiComponentType(uiComponentType);
        }
        return uiComponent;
    }

    /**
     * Create a basic Ui Component Config
     *
     * @return
     */
    public static UiComponentConfig createUiComponentConfig() {
        UiComponentConfig uiComponentConfig = new UiComponentConfig();
        return uiComponentConfig;
    }

    /**
     * Create a basic Ui Component Config Attribute
     *
     * @param uiComponent
     * @param key
     * @param value
     * @return
     */
    public static UiComponentConfigAttribute createUiComponentConfigAttribute(UiComponent uiComponent, String key, String value) {
        UiComponentConfigAttribute uiComponentConfigAttribute = new UiComponentConfigAttribute();
        uiComponentConfigAttribute.setUiComponent(uiComponent);
        uiComponentConfigAttribute.setKey(key);
        uiComponentConfigAttribute.setValue(value);
        return uiComponentConfigAttribute;
    }

    /**
     * Create a basic Ui Component Definition
     *
     * @param name
     * @param description
     * @param extend
     * @param typeName
     * @param controller
     * @param uiComponent
     * @param requires
     * @return
     */
    public static UiComponentDefinition createUiComponentDefinition(String name, String description, String extend, String typeName, String controller, UiComponent uiComponent, String... requires) {
        UiComponentDefinition uiComponentDefinition = new UiComponentDefinition();
        uiComponentDefinition.setName(name);
        uiComponentDefinition.setDescription(description);
        uiComponentDefinition.setExtend(extend);
        uiComponentDefinition.setXtype(typeName);
        uiComponentDefinition.setController(controller);
        uiComponentDefinition.setUiComponent(uiComponent);

        iterate(iterable(requires), (require) -> uiComponentDefinition.addRequire(require));

        return uiComponentDefinition;
    }


    /**
     * Creates the function for creating a Tab Grid System Window
     * that has a tab with a grid for each entity and the given title
     *
     * @param title
     * @param entityClasses
     * @return
     */
    public static String createSchemaTabGridSystemWindow(String title, Class<? extends BaseEntity>... entityClasses) {
        return "function () {System.util.system.WindowUtils.createSchemaTabGridSystemWindow('" + title + "'," + arrayToString(entityClasses, Class::getSimpleName) + ");}";
    }
}