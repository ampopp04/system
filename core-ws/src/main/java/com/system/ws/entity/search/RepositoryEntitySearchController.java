/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.ws.entity.search;

import com.system.db.repository.base.entity.BaseEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.core.mapping.SearchResourceMappings;
import org.springframework.data.rest.webmvc.*;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.rest.webmvc.ControllerUtils.EMPTY_RESOURCE_LIST;

/**
 * The <class>RepositoryEntitySearchController</class> defines
 * an alternative implementation to {@link org.springframework.data.rest.webmvc.RepositoryEntityController}
 * which is the main entry point controller to expose
 * webservices access to all of the repository implementations dynamically.
 * <p>
 * Unfortunately, their implementation only exposes specific back-end database operations based
 * on a somewhat restrictive dynamic convention.
 * <p>
 * This controller allows exposing methods within {@link BaseEntityRepository} and {@link BaseEntityRepositoryImpl}
 * <p>
 * It does this generically but under the subheading path of /{repository}/read. We can't set the path to be
 * /{repository} because that would class with {@link org.springframework.data.rest.webmvc.RepositoryEntityController }
 * and at this time we are avoiding having to completely replace their implementation.
 *
 * @author Andrew
 */
@RepositoryRestController
public class RepositoryEntitySearchController {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private static final String BASE_MAPPING = "/{repository}/read";

    private static final EmbeddedWrappers WRAPPERS = new EmbeddedWrappers(false);

    private final RepositoryEntityLinks entityLinks;
    private final RepositoryRestConfiguration config;
    private final PagedResourcesAssembler<Object> pagedResourcesAssembler;
    private final Repositories repositories;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    public RepositoryEntitySearchController(Repositories repositories, RepositoryRestConfiguration config,
                                            RepositoryEntityLinks entityLinks, PagedResourcesAssembler<Object> assembler,
                                            HttpHeadersPreparer headersPreparer) {

        this.repositories = repositories;
        this.pagedResourcesAssembler = assembler;
        this.entityLinks = entityLinks;
        this.config = config;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                      //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * <code>GET /{repository}</code> - Returns the collection resource (paged or unpaged).
     *
     * @param resourceInformation
     * @param pageable
     * @param sort
     * @param assembler
     * @return
     * @throws ResourceNotFoundException
     * @throws HttpRequestMethodNotSupportedException
     */
    @ResponseBody
    @RequestMapping(value = BASE_MAPPING, method = RequestMethod.GET)
    public Resources<?> getCollectionResource(@QuerydslPredicate RootResourceInformation resourceInformation,
                                              DefaultedPageable pageable, Sort sort, String filter, Integer searchDepth, String[] extraSearchParams, PersistentEntityResourceAssembler assembler)
            throws ResourceNotFoundException, HttpRequestMethodNotSupportedException {

        BaseEntityRepository systemRepository = (BaseEntityRepository) repositories.getRepositoryFor(resourceInformation.getDomainType()).get();

        Iterable<?> results = systemRepository.findAll(filter, searchDepth, extraSearchParams, pageable.getPageable());

        ResourceMetadata metadata = resourceInformation.getResourceMetadata();
        Optional<Link> baseLink = Optional.of(entityLinks.linkToPagedResource(resourceInformation.getDomainType(),
                pageable.isDefault() ? null : pageable.getPageable()));

        Resources<?> result = toResources(results, assembler, metadata.getDomainType(), baseLink);
        result.add(getCollectionResourceLinks(resourceInformation, pageable));
        return result;
    }


    protected Resources<?> toResources(Iterable<?> source, PersistentEntityResourceAssembler assembler,
                                       Class<?> domainType, Optional<Link> baseLink) {

        if (source instanceof Page) {
            Page<Object> page = (Page<Object>) source;
            return entitiesToResources(page, assembler, domainType, baseLink);
        } else if (source instanceof Iterable) {
            return entitiesToResources((Iterable<Object>) source, assembler, domainType);
        } else {
            return new Resources(EMPTY_RESOURCE_LIST);
        }
    }


    protected Link getDefaultSelfLink() {
        return new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
    }

    protected Resources<?> entitiesToResources(Page<Object> page, PersistentEntityResourceAssembler assembler,
                                               Class<?> domainType, Optional<Link> baseLink) {

        if (page.getContent().isEmpty()) {
            return baseLink.<PagedResources<?>>map(it -> pagedResourcesAssembler.toEmptyResource(page, domainType, it))//
                    .orElseGet(() -> pagedResourcesAssembler.toEmptyResource(page, domainType));
        }

        return baseLink.map(it -> pagedResourcesAssembler.toResource(page, assembler, it))//
                .orElseGet(() -> pagedResourcesAssembler.toResource(page, assembler));
    }

    protected Resources<?> entitiesToResources(Iterable<Object> entities, PersistentEntityResourceAssembler assembler,
                                               Class<?> domainType) {

        if (!entities.iterator().hasNext()) {

            List<Object> content = Arrays.<Object>asList(WRAPPERS.emptyCollectionOf(domainType));
            return new Resources<Object>(content, getDefaultSelfLink());
        }

        List<Resource<Object>> resources = new ArrayList<Resource<Object>>();

        for (Object obj : entities) {
            resources.add(obj == null ? null : assembler.toResource(obj));
        }

        return new Resources<Resource<Object>>(resources, getDefaultSelfLink());
    }

    private List<Link> getCollectionResourceLinks(RootResourceInformation resourceInformation,
                                                  DefaultedPageable pageable) {

        ResourceMetadata metadata = resourceInformation.getResourceMetadata();
        SearchResourceMappings searchMappings = metadata.getSearchResourceMappings();

        List<Link> links = new ArrayList<Link>();
        links.add(new Link(ProfileController.getPath(this.config, metadata), ProfileResourceProcessor.PROFILE_REL));

        if (searchMappings.isExported()) {
            links.add(entityLinks.linkFor(metadata.getDomainType()).slash(searchMappings.getPath())
                    .withRel(searchMappings.getRel()));
        }

        return links;
    }
}