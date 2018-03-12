/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package db.migration;

import com.job.tracker.system.entity.expression.EntityExpression;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignment;
import com.job.tracker.system.entity.expression.operation.EntityExpressionOperation;
import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationType;
import com.job.tracker.system.entity.expression.type.EntityExpressionType;
import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.job.tracker.system.entity.expression.type.EntityExpressionType.*;
import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V15__initial_schema</class> defines the initial schema for
 * a system expressions.
 *
 * @author Andrew
 */
public class V15__expression_initial_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<EntityExpressionType> entityExpressionTypeRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return asList(
                EntityExpressionOperationType.class, EntityExpressionOperation.class, EntityExpressionType.class, EntityExpression.class, EntityExpressionAssignment.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getEntityExpressionTypeRepository().saveAll(getEntityExpressionTypeData());
    }

    public List<EntityExpressionType> getEntityExpressionTypeData() {
        return asList(
                EntityExpressionType.newInstance(NUMBER_EXPRESSION_TYPE_NAME, "An expression used to generate a unique identifier number for a specific entity."),
                EntityExpressionType.newInstance(FOLDER_EXPRESSION_TYPE_NAME, "An expression used to generate an underlying folder path for a given entity."),
                EntityExpressionType.newInstance(OBJECT_EXPRESSION_TYPE_NAME, "An expression used to generate an object for a given entity.")
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<EntityExpressionType> getEntityExpressionTypeRepository() {
        return entityExpressionTypeRepository;
    }

    public void setEntityExpressionTypeRepository(NamedEntityRepository<EntityExpressionType> entityExpressionTypeRepository) {
        this.entityExpressionTypeRepository = entityExpressionTypeRepository;
    }
}