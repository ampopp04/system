/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.ws.config.domain.object.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.mapping.*;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.mapping.model.ConvertingPropertyAccessor;
import org.springframework.data.rest.webmvc.json.DomainObjectReader;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Optional;

public class EntityAssociationDomainObjectReader extends DomainObjectReader {

    private final PersistentEntities entities;
    private final Associations associationLinks;

    public EntityAssociationDomainObjectReader(PersistentEntities entities, Associations associationLinks) {
        super(entities, associationLinks);

        this.entities = entities;
        this.associationLinks = associationLinks;
    }

    /**
     * Reads the given source node onto the given target object and applies PUT semantics, i.e. explicitly
     *
     * @param source must not be {@literal null}.
     * @param target must not be {@literal null}.
     * @param mapper
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T readPut(final ObjectNode source, T target, final ObjectMapper mapper) {
        try {
            T obj = super.readPut(source, target, mapper);
            Object intermediate = mapper.readerFor(target.getClass()).readValue(source);
            return mergeForPutEntityOverride((T) intermediate, obj, mapper);
        } catch (Exception o_O) {
            throw new HttpMessageNotReadableException("Could not read payload!", o_O);
        }
    }

    <T> T mergeForPutEntityOverride(T source, T target, final ObjectMapper mapper) {

        Class<? extends Object> type = target.getClass();

        return entities.getPersistentEntity(type).map(
                (PersistentEntity<?, ? extends PersistentProperty<?>> it) -> {

                    it.doWithAssociations(
                            (SimpleAssociationHandler) association -> (
                                    (SimplePropertyHandler) property -> {

                                        PersistentPropertyAccessor targetAccessor = new ConvertingPropertyAccessor(it.getPropertyAccessor(target), new DefaultConversionService());

                                        Optional<?> result = Optional.ofNullable(it.getPropertyAccessor(source).getProperty(property));

                                        targetAccessor.setProperty(property, result.orElse(null));

                                    }
                            ).doWithPersistentProperty(association.getInverse())
                    );

                    return target;

                }
        ).orElse(source);
    }


}
