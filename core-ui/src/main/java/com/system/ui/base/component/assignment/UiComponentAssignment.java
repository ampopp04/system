package com.system.ui.base.component.assignment;

import com.system.db.schema.table.SchemaTable;
import com.system.ui.base.UiBase;
import com.system.ui.base.component.UiComponent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>UiComponentAssignment</class>
 * allows assigning a specific UiComponent to a specific
 * entity within a given SchemaTable
 *
 * @author Andrew
 */
public class UiComponentAssignment extends UiBase<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @ManyToOne(optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(name = "ui_component_id")
    private UiComponent uiComponent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "schema_table_id")
    private SchemaTable schemaTable;

    @Column(nullable = false)
    private Integer fkFieldId;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                        //////////
    /////////////////////////////////////////////////////////////////////

    public UiComponent getUiComponent() {
        return uiComponent;
    }

    public void setUiComponent(UiComponent uiComponent) {
        this.uiComponent = uiComponent;
    }

    public SchemaTable getSchemaTable() {
        return schemaTable;
    }

    public void setSchemaTable(SchemaTable schemaTable) {
        this.schemaTable = schemaTable;
    }

    public Integer getFkFieldId() {
        return fkFieldId;
    }

    public void setFkFieldId(Integer fkFieldId) {
        this.fkFieldId = fkFieldId;
    }
}
