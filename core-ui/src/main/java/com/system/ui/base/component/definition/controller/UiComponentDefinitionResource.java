/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.ui.base.component.definition.controller;

import com.system.ui.base.component.definition.UiComponentDefinition;
import org.springframework.hateoas.Link;

/**
 * The <class>UiComponentDefinitionResource</class> defines  a
 * hateoas UiComponentDefinition resource
 *
 * @author Andrew
 */
public class UiComponentDefinitionResource extends org.springframework.hateoas.Resource<UiComponentDefinition> {

    public UiComponentDefinitionResource(UiComponentDefinition content, Iterable<Link> links) {
        super(content, links);
    }
}