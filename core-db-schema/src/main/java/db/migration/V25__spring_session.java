/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package db.migration;

import com.system.db.migration.sql.SqlQueryMigration;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V26__spring_session</class> defines the spring session
 * table creation schema override
 *
 * @author Andrew
 */
public class V25__spring_session extends SqlQueryMigration {

    @Autowired(required = false)
    private Server h2TcpServer;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        QUERY                                                          //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected List<String> sqlQuery() {
        /**
         * Create the SPRING_SESSION table only if we are working with a database that is not embedded H2
         */
        if (isMySqlDatabaseDriver()) {
            return asList(
                    "CREATE TABLE SPRING_SESSION (\n" +
                            "\tPRIMARY_ID CHAR(36) NOT NULL,\n" +
                            "\tSESSION_ID CHAR(36) NOT NULL,\n" +
                            "\tCREATION_TIME BIGINT NOT NULL,\n" +
                            "\tLAST_ACCESS_TIME BIGINT NOT NULL,\n" +
                            "\tMAX_INACTIVE_INTERVAL INT NOT NULL,\n" +
                            "\tEXPIRY_TIME BIGINT NOT NULL,\n" +
                            "\tPRINCIPAL_NAME VARCHAR(100),\n" +
                            "\tCONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)\n" +
                            ") ENGINE=InnoDB;",
                    "CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);",
                    "CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);",
                    "CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);",
                    "CREATE TABLE SPRING_SESSION_ATTRIBUTES (\n" +
                            "\tSESSION_PRIMARY_ID CHAR(36) NOT NULL,\n" +
                            "\tATTRIBUTE_NAME VARCHAR(200) NOT NULL,\n" +
                            "\tATTRIBUTE_BYTES BLOB NOT NULL,\n" +
                            "\tCONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),\n" +
                            "\tCONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE\n" +
                            ") ENGINE=InnoDB;",
                    "CREATE INDEX SPRING_SESSION_ATTRIBUTES_IX1 ON SPRING_SESSION_ATTRIBUTES (SESSION_PRIMARY_ID);"
            );

        }
        return asList();
    }

    private boolean isMySqlDatabaseDriver() {
        return h2TcpServer == null;
    }

}