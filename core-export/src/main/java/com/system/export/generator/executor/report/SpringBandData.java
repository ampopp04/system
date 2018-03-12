/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.generator.executor.report;

import com.haulmont.yarg.structure.BandData;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.time.LocalDate;

public class SpringBandData extends BandData {

    private Object dataObject;

    public SpringBandData(String name, BandData parentBand, Object dataObject) {
        super(name, parentBand);
        this.dataObject = dataObject;
    }

    @Override
    public Object getParameterValue(String name) {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(getExpression(name));
        StandardEvaluationContext context = new StandardEvaluationContext(dataObject);

        try {
            context.registerFunction("date", LocalDate.class.getDeclaredMethod("now"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return expression.getValue(context);
    }

    private String getExpression(String text) {
        return text.replace("date", "#date()");
    }
}
