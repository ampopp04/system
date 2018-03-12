/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.ui.base.component.definition;

import com.system.ui.base.component.UiComponentProjection;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * The <class>UiComponentDefinitionProjection</class> defines  a
 * projection for all fields within the UiComponentDefinition object.
 *
 * @author Andrew
 */
@Projection(name = "ui-component-definition-all", types = UiComponentDefinition.class)
public interface UiComponentDefinitionProjection {

    public String getName();

    public String getDescription();

    public List<String> getRequires();

    public String getExtend();

    public String getXtype();

    public String getController();

    public UiComponentProjection getUiComponent();

    public Integer getId();
}
