package com.system.ws.projection;

import com.system.ws.projection.util.DynamicProjectionUtils;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.projection.ProjectionDefinitions;
import org.springframework.data.rest.webmvc.support.PersistentEntityProjector;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * The <class>DynamicEntityProjector</class> defines the dynamic entity projector class.
 * This class allows the construction and retrieval of dynamic projections.
 * <p>
 * Traditionally a projection would be any interface marked by the annotation Projection
 * but this is not flexible enough.  Instead this class will use byte code manipulation to
 * construct projection interfaces on the fly that describe the underlying entity.
 * <p>
 * This is useful for when the UI requests full sets of entities and their sub-relationships on the fly.
 * Instead of having to know what projections to request ahead of time we can construct these dynamically
 * based on what fields are requested by an external source.
 *
 * @author Andrew
 */
public class DynamicEntityProjector extends PersistentEntityProjector {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    private final ProjectionDefinitions projectionDefinitions;
    private final ProjectionFactory factory;
    private final String projection;
    private final ResourceMappings mappings;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                            Methods                                                     //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Creates a new {@link PersistentEntityProjector} using the given {@link ProjectionDefinitions},
     * {@link ProjectionFactory} and projection name.
     *
     * @param projectionDefinitions must not be {@literal null}.
     * @param factory               must not be {@literal null}.
     * @param projection            can be empty.
     * @param mappings
     */
    public DynamicEntityProjector(ProjectionDefinitions projectionDefinitions, ProjectionFactory factory, String projection, ResourceMappings mappings) {
        super(projectionDefinitions, factory, projection, mappings);

        Assert.notNull(projectionDefinitions, "ProjectionDefinitions must not be null!");
        Assert.notNull(factory, "ProjectionFactory must not be null!");

        this.projectionDefinitions = projectionDefinitions;
        this.factory = factory;
        this.projection = projection;
        this.mappings = mappings;
    }


    /**
     * Creates a projection for a given source entity
     *
     * @param source
     * @return
     */
    @Override
    public Object project(Object source) {

        Assert.notNull(source, "Projection source must not be null!");

        if (!StringUtils.hasText(projection)) {
            return source;
        }

        Class<?> projectionType = DynamicProjectionUtils.getProjection(source, projection, projectionDefinitions);

        return projectionType == null ? source : factory.createProjection(projectionType, source);
    }

    @Override
    public Object projectExcerpt(Object source) {
        return project(source);
    }
}