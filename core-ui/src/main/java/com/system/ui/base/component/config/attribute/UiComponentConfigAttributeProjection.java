package com.system.ui.base.component.config.attribute;

import com.system.ui.base.component.UiComponentProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * The <class>UiComponentConfigAttribute</class> defines a
 * projection for retrieving all fields within the UiComponentConfigAttribute
 *
 * @author Andrew
 */
@Projection(name = "ui-component-config-attribute-all", types = UiComponentConfigAttribute.class)
public interface UiComponentConfigAttributeProjection {

    public UiComponentProjection getUiComponent();

    public String getKey();

    public String getValue();
}
