package db.migration;


import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.export.task.history.SystemExportTaskHistory;
import com.system.util.collection.CollectionUtils;

import java.util.List;

/**
 * The <class>V10__history_schema</class> defines the schema
 * for tracking runs of task executions. The history maintains a link
 * between a specific task execution, it's status at the end of execution,
 * how it generated it's content, and the content it actually generated.
 * <p>
 * This also allows recording details or error messages about the execution process.
 *
 * @author Andrew
 */
public class V10__history_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return CollectionUtils.asList(
                SystemExportTaskHistory.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}