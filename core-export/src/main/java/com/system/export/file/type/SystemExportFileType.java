/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.file.type;

import com.haulmont.yarg.structure.ReportOutputType;
import com.system.db.entity.named.NamedEntity;

/**
 * The <class>SystemExportFileType</class> defines a simple
 * set of supported export file types.
 * <p>
 * Including
 * xls
 * doc
 * docx
 * xlsx
 * html
 * pdf
 * csv
 *
 * @author Andrew
 */
public class SystemExportFileType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    public static class SystemExportFileTypes extends ReportOutputType {

        public final static ReportOutputType odt = new ReportOutputType("odt");
        public final static ReportOutputType rtf = new ReportOutputType("rtf");

        static {
            values.put(odt.getId(), doc);
            values.put(rtf.getId(), doc);
        }

        public SystemExportFileTypes(String id) {
            super(id);
        }

    }

    /**
     * Defines the content type of this file type
     * <p>
     * Ex. text/csv
     */
    private String contentType;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportFileType() {
    }

    public static SystemExportFileType newInstance(String name, String description, String contentType) {
        SystemExportFileType systemExportFileType = new SystemExportFileType();
        systemExportFileType.setName(name);
        systemExportFileType.setDescription(description);
        systemExportFileType.setContentType(contentType);
        return systemExportFileType;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}