/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.template.type;

import com.system.db.entity.named.NamedEntity;
import com.system.export.template.SystemExportTemplate;

/**
 * The <class>SystemExportTemplateType</class> defines different
 * types of {@link SystemExportTemplate}
 * <p>
 * Report, Query, Calculation, etc
 *
 * @author Andrew
 */
public class SystemExportTemplateType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTemplateType() {
    }

    public static SystemExportTemplateType newInstance(String name, String description) {
        SystemExportTemplateType systemExportTemplateType = new SystemExportTemplateType();
        systemExportTemplateType.setName(name);
        systemExportTemplateType.setDescription(description);
        return systemExportTemplateType;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}