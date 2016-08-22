package com.system.ws.projection.resolver;

import com.system.ws.projection.DynamicEntityProjector;
import org.springframework.core.MethodParameter;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.projection.ProjectionDefinitions;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.config.PersistentEntityResourceAssemblerArgumentResolver;
import org.springframework.data.rest.webmvc.support.PersistentEntityProjector;
import org.springframework.hateoas.EntityLinks;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * The <class>DynamicEntityResourceArgumentResolver</class> defines
 * a resolver that handles requests with dynamically resolved fields.  If an incoming request
 * has the dynamicProjection field set this resolver will resolve/create a projection that will resolve that data
 * to be returned
 *
 * @author Andrew
 */
public class DynamicEntityResourceArgumentResolver extends PersistentEntityResourceAssemblerArgumentResolver {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    private final PersistentEntities entities;
    private final EntityLinks entityLinks;
    private final ProjectionDefinitions projectionDefinitions;
    private final ProjectionFactory projectionFactory;
    private final ResourceMappings mappings;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Methods                                                       //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * Creates a new {@link PersistentEntityResourceAssemblerArgumentResolver} for the given {@link Repositories},
     * {@link EntityLinks}, {@link ProjectionDefinitions} and {@link ProjectionFactory}.
     *
     * @param entities              must not be {@literal null}.
     * @param entityLinks           must not be {@literal null}.
     * @param projectionDefinitions must not be {@literal null}.
     * @param projectionFactory     must not be {@literal null}.
     * @param mappings
     */
    public DynamicEntityResourceArgumentResolver(PersistentEntities entities, EntityLinks entityLinks, ProjectionDefinitions projectionDefinitions, ProjectionFactory projectionFactory, ResourceMappings mappings) {
        super(entities, entityLinks, projectionDefinitions, projectionFactory, mappings);

        this.entities = entities;
        this.entityLinks = entityLinks;
        this.projectionDefinitions = projectionDefinitions;
        this.projectionFactory = projectionFactory;
        this.mappings = mappings;
    }

    /**
     * Resolve the arguments via a dynamic entity projector
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String projectionParameter = webRequest.getParameter(projectionDefinitions.getParameterName());
        PersistentEntityProjector projector = new DynamicEntityProjector(projectionDefinitions, projectionFactory,
                projectionParameter, mappings);

        return new PersistentEntityResourceAssembler(entities, entityLinks, projector, mappings);
    }
}
