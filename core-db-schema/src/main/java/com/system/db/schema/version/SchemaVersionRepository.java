package com.system.db.schema.version;


import com.system.db.repository.base.identity.EntityIdentityRepository;

/**
 * The <class>SchemaVersionRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 */
public interface SchemaVersionRepository extends EntityIdentityRepository<SchemaVersion, String> {
}