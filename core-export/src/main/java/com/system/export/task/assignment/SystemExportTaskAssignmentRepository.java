package com.system.export.task.assignment;

import com.system.db.repository.base.named.NamedEntityRepository;

import java.util.List;

/**
 * The <class>SystemExportTaskAssignmentRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see NamedEntityRepository
 */
public interface SystemExportTaskAssignmentRepository extends NamedEntityRepository<SystemExportTaskAssignment> {

    /**
     * Find the SystemExportTaskAssignments for
     * a specific table
     */
    public List<SystemExportTaskAssignment> findBySchemaTableName(String schemaTableName);
}