/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.ws.entity.upload.util;

import com.system.db.entity.base.BaseEntity;
import org.springframework.data.repository.support.Repositories;

import static com.system.ws.entity.upload.util.EntityPropertyUtils.injectPropertyValues;
import static com.system.ws.entity.upload.util.FileParsingUtils.parseFileLine;

/**
 * The <class>FileUtils</class> defines
 * utility methods for manipulating
 * files
 *
 * @author Andrew
 */
public class EntityUtils {

    public static <T extends BaseEntity> T parseEntity(Class<T> entityClass, String[] filePropertyHeaders, String
            customerLine, String contentType, Repositories repositories) throws IllegalAccessException, InstantiationException {
        String[] fileLinePropertyValues = parseFileLine(filePropertyHeaders, customerLine, contentType);

        return (T) injectPropertyValues(entityClass.newInstance(), filePropertyHeaders, fileLinePropertyValues, repositories);
    }

}
