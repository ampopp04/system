/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.task.executor.controller;

import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.base.entity.BaseEntityRepository;
import com.system.export.task.assignment.SystemExportTaskAssignment;
import com.system.export.task.executor.SystemExportTaskExecutor;
import com.system.export.task.history.SystemExportTaskHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.HttpHeadersPreparer;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The <class>RepositoryEntitySearchController</class> defines
 *
 * @author Andrew
 */
@RepositoryRestController
public class SystemExportTaskExecutorController {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private static final String BASE_MAPPING = "/{repository}/taskExecutor";

    private static final EmbeddedWrappers WRAPPERS = new EmbeddedWrappers(false);

    private final RepositoryEntityLinks entityLinks;
    private final RepositoryRestConfiguration config;
    private final PagedResourcesAssembler<Object> pagedResourcesAssembler;
    private final Repositories repositories;

    @Autowired
    private SystemExportTaskExecutor systemExportTaskExecutor;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    public SystemExportTaskExecutorController(Repositories repositories, RepositoryRestConfiguration config,
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

    @RequestMapping(value = BASE_MAPPING, method = RequestMethod.GET)
    public ResponseEntity<Resource<?>> getItemResource(@QuerydslPredicate RootResourceInformation resourceInformation, final PersistentEntityResourceAssembler assembler, SystemExportTaskAssignment systemExportTaskAssignment, Integer fkFieldId) {

        BaseEntityRepository systemRepository = (BaseEntityRepository) repositories.getRepositoryFor(resourceInformation.getDomainType()).get();
        BaseEntity entityObject = (BaseEntity) systemRepository.getOne(fkFieldId);

        SystemExportTaskHistory taskHistory = systemExportTaskExecutor.executeTask(systemExportTaskAssignment.getSystemExportTask(), entityObject);

        return ResponseEntity.ok(assembler.toFullResource(taskHistory));
    }

}