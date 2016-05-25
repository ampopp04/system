package com.system.db.migration.schema;

import com.system.db.repository.BaseEntityRepository;

/**
 * The <class>SchemaVersionRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 * @see BaseEntityRepository
 */
public interface SchemaVersionRepository extends BaseEntityRepository<SchemaVersion> {
}