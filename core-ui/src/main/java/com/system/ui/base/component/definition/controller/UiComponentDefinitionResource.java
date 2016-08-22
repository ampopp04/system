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