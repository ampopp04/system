package com.system.db.migration;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemTableRepository extends CrudRepository<SystemTable, Long> {

}