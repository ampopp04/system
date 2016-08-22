package com.system.ui.base.component;

import com.system.ui.base.component.config.UiComponentConfigProjection;
import com.system.ui.base.component.type.UiComponentType;
import org.springframework.data.rest.core.config.Projection;

/**
 * The <class>UiComponentProjection</class> defines
 * a projection that includes all fields for the UiComponent object.
 *
 * @author Andrew
 */
@Projection(name = "ui-component-all", types = UiComponent.class)
public interface UiComponentProjection {

    public UiComponentConfigProjection getUiComponentConfig();

    public UiComponentType getUiComponentType();

    public String getName();

    public String getDescription();
}
