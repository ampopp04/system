package com.system.ui.base.component.definition.controller;

import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.ui.base.component.definition.UiComponentDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RepositorySearchesResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The <class>UiComponentDefinitionController</class> defines  a controller
 * for returning back basic json for UiComponentDefinitions
 *
 * @author Andrew
 */
@RepositoryRestController
@RequestMapping("uiComponentDefinitions/search")
public class UiComponentDefinitionController implements ResourceProcessor<RepositorySearchesResource> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<UiComponentDefinition> uiComponentDefinitionRepository;

    @Autowired
    UiComponentDefinitionResourceAssembler uiComponentDefinitionResourceAssembler;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                        //////////
    /////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "findByNameAll", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<UiComponentDefinitionResource> findByNameAll(@RequestParam String name) {
        return ResponseEntity.ok(this.uiComponentDefinitionResourceAssembler.toResource(uiComponentDefinitionRepository.findByName(name)));
    }

    @Override
    public RepositorySearchesResource process(RepositorySearchesResource repositorySearchesResource) {
        final String search = repositorySearchesResource.getId().getHref();
        final Link customLink = new Link(search + "/findByNameAll{?name}").withRel("findByNameAll");
        repositorySearchesResource.add(customLink);
        return repositorySearchesResource;
    }
}