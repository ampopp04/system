/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.ui.base.component.definition.controller;

import com.system.ui.base.component.definition.UiComponentDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * The <class>UiComponentDefinitionResourceAssembler</class> converts
 * a UiComponentDefinition into a json resource without
 * the standard hateoas self referencing links to make this a simple json response
 * to be handled by the UI.
 *
 * @author Andrew
 */
@Component
public class UiComponentDefinitionResourceAssembler extends ResourceAssemblerSupport<UiComponentDefinition, UiComponentDefinitionResource> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @Autowired
    RepositoryEntityLinks repositoryEntityLinks;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                        //////////
    /////////////////////////////////////////////////////////////////////

    public UiComponentDefinitionResourceAssembler() {
        super(UiComponentDefinitionController.class, UiComponentDefinitionResource.class);
    }

    @Override
    public UiComponentDefinitionResource toResource(UiComponentDefinition uiComponentDefinition) {
        //TODO Remove unused objects
        Link uiComponentLink = repositoryEntityLinks.linkToSingleResource(UiComponentDefinition.class, uiComponentDefinition.getId());
        Link selfLink = new Link(uiComponentLink.getHref(), Link.REL_SELF);
        return new UiComponentDefinitionResource(uiComponentDefinition, Arrays.asList());
    }
}