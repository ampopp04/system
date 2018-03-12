/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.generator.executor.report;

import com.haulmont.yarg.exception.DataLoadingException;
import com.haulmont.yarg.loaders.impl.GroovyDataLoader;
import com.haulmont.yarg.structure.BandData;
import com.haulmont.yarg.structure.ReportQuery;
import com.haulmont.yarg.util.groovy.Scripting;

import java.util.List;
import java.util.Map;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;

public class PropertyDataLoader extends GroovyDataLoader {

    public PropertyDataLoader(Scripting scripting) {
        super(scripting);
    }

    @Override
    public List<Map<String, Object>> loadData(ReportQuery reportQuery, BandData parentBand, Map<String, Object> params) {

        try {
            return asList(reportQuery.getAdditionalParams());
        } catch (Throwable e) {
            throw new DataLoadingException(String.format("An error occurred while loading data for data set [%s]", reportQuery.getName()), e);
        }
    }

}
