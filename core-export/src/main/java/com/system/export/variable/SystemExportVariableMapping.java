/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.variable;

import com.system.db.entity.named.NamedEntity;
import com.system.db.schema.table.column.SchemaTableColumn;
import com.system.export.SystemExport;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemExportTemplateVariableMapping</class> defines mappings
 * between variable names within a template
 * and entity columns within the system. This is useful for templates
 * that use variable names that don't directly map to entity names in the system
 *
 * @author Andrew
 */
public class SystemExportVariableMapping extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private String variableName;

    @ManyToOne
    @JoinColumn(name = "schema_table_column_id")
    private SchemaTableColumn schemaTableColumn;

    @ManyToOne
    @JoinColumn(name = "system_export_id")
    private SystemExport systemExport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_system_export_variable_mapping_id")
    private SystemExportVariableMapping parentSystemExportVariableMapping;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportVariableMapping() {
    }

    public String getSystemExportVariableMappingExpanded() {
        String result = this.getVariableName();
        SystemExportVariableMapping parentSystemExportVariableMapping = this.getParentSystemExportVariableMapping();

        int depth = 0;

        while (parentSystemExportVariableMapping != null) {

            result = parentSystemExportVariableMapping.getVariableName() + "." + result;
            parentSystemExportVariableMapping = parentSystemExportVariableMapping.getParentSystemExportVariableMapping();

            ++depth;
            if (depth > 20) {
                break;
            }
        }

        return result;
    }
    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public SchemaTableColumn getSchemaTableColumn() {
        return schemaTableColumn;
    }

    public void setSchemaTableColumn(SchemaTableColumn schemaTableColumn) {
        this.schemaTableColumn = schemaTableColumn;
    }

    public SystemExport getSystemExport() {
        return systemExport;
    }

    public void setSystemExport(SystemExport systemExport) {
        this.systemExport = systemExport;
    }

    public SystemExportVariableMapping getParentSystemExportVariableMapping() {
        return parentSystemExportVariableMapping;
    }

    public void setParentSystemExportVariableMapping(SystemExportVariableMapping parentSystemExportVariableMapping) {
        this.parentSystemExportVariableMapping = parentSystemExportVariableMapping;
    }
}