package com.system.ui.base.component.assignment;

import com.system.db.repository.base.named.NamedEntityRepository;

/**
 * The <class>UiComponentAssignmentRepository</class> defines a repository for
 * handling special methods to retrieve UiComponentAssignment objects
 *
 * @author Andrew
 */
public interface UiComponentAssignmentRepository extends NamedEntityRepository<UiComponentAssignment> {

    /**
     * Find UiComponentAssignment by schema table name and fk field id and ui component type.
     *
     * @param schemaTableName
     * @param fkFieldId
     * @param uiComponentUiComponentTypeName
     * @return
     */
    public UiComponentAssignment findBySchemaTableNameAndFkFieldIdAndUiComponentUiComponentTypeName(String schemaTableName, Integer fkFieldId, String uiComponentUiComponentTypeName);

    /**
     * Find UiComponentAssignment by schema table name and fk field id
     *
     * @param schemaTableName
     * @param fkFieldId
     * @return
     */
    public UiComponentAssignment findBySchemaTableNameAndFkFieldId(String schemaTableName, Integer fkFieldId);

}