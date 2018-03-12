/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.generator.executor.report;

import com.haulmont.yarg.formatters.ReportFormatter;
import com.haulmont.yarg.formatters.factory.DefaultFormatterFactory;
import com.haulmont.yarg.formatters.factory.FormatterFactoryInput;
import com.haulmont.yarg.structure.BandData;

public class SystemFormatterFactory extends DefaultFormatterFactory {

    private Object dataObject;

    public SystemFormatterFactory(Object dataObject) {
        this.dataObject = dataObject;
    }

    @Override
    public ReportFormatter createFormatter(FormatterFactoryInput factoryInput) {
        ReportFormatter parentFormatter = super.createFormatter(factoryInput);
        addSpringBandData(factoryInput.getRootBand());
        return parentFormatter;
    }

    private void addSpringBandData(BandData rootBandData) {
        SpringBandData springBandData = new SpringBandData("system", rootBandData, dataObject);
        rootBandData.addChild(springBandData);
    }
}
