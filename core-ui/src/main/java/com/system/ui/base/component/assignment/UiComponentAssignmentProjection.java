package com.system.ui.base.component.assignment;

import com.system.db.schema.table.SchemaTable;
import com.system.ui.base.component.UiComponentProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * The <class>UiComponentAssignmentProjection</class> defines a projection
 * for all the fields of a UiComponentAssignment
 *
 * @author Andrew
 */
@Projection(name = "ui-component-assignment-all", types = UiComponentAssignment.class)
public interface UiComponentAssignmentProjection {

    public UiComponentProjection getUiComponent();

    public SchemaTable getSchemaTable();

    public Integer getFkFieldId();

    public String getName();

    public String getDescription();

    public Integer getId();
}