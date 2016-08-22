package com.system.ui.base.component.config;

import com.system.ui.base.component.config.attribute.UiComponentConfigAttributeProjection;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * The <class>UiComponentConfig</class> defines a
 * projection for retrieving all fields within the UiComponentConfig
 *
 * @author Andrew
 */
@Projection(name = "ui-component-config-all", types = UiComponentConfig.class)
public interface UiComponentConfigProjection {

    public List<UiComponentConfigAttributeProjection> getUiComponentConfigAttributeList();
}
