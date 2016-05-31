package db.migration;


import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.schema.datatype.SchemaDataType;
import com.system.db.schema.datatype.SchemaDataTypeRepository;
import com.system.db.schema.table.SchemaTable;
import com.system.db.schema.table.column.SchemaTableColumn;
import com.system.db.schema.table.column.relationship.SchemaTableColumnRelationship;
import com.system.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * The <class>V1__initial_schema</class> defines the initial
 * database migration schema creation.
 * <p>
 * This defines various tables that manage the database schema itself.
 * <p>
 * It then performs data insertion into these newly created tables.
 *
 * @author Andrew
 * @see SchemaDataType
 * @see SchemaTable
 * @see SchemaTableColumn
 * @see SchemaTableColumnRelationship
 */
public class V1__initial_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private SchemaDataTypeRepository schemaDataTypeRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return CollectionUtils.asList(SchemaDataType.class, SchemaTable.class, SchemaTableColumn.class, SchemaTableColumnRelationship.class);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getSchemaDataTypeRepository().save(CollectionUtils.iterable(getDataEntities()));
    }

    /**
     * Mapping Type                     Java Type                                           ANSI SQL Type
     * Integer	                        int or java.lang.Integer	                                INTEGER
     * Long	                            long or java.lang.Long	                                BIGINT
     * Short	                            short or java.lang.Short	                            SMALLINT
     * Float	                            float or java.lang.Float	                                FLOAT
     * Double	                        double or java.lang.Double	                        DOUBLE
     * Big Decimal	                java.math.BigDecimal	                                    NUMERIC
     * <p>
     * String	                        java.lang.String	                                                VARCHAR
     * <p>
     * Byte	                            byte or java.lang.Byte	                                TINYINT
     * <p>
     * Boolean	                    boolean or java.lang.Boolean	                    BIT
     * <p>
     * Date	                            java.util.Date or java.sql.Date	                DATE
     * Time	                            java.util.Date or java.sql.Time             	    TIME
     * Timestamp         	        java.util.Date or java.sql.Timestamp	    TIMESTAMP
     * Calendar	                    java.util.Calendar                                      	        TIMESTAMP
     */
    private List<SchemaDataType> getDataEntities() {
        List<SchemaDataType> entityList = new ArrayList<>();

        //Numbers
        entityList.add(newSchemaDataType("Integer", "Mapping between java.lang.Integer and INTEGER ANSI type", true));
        entityList.add(newSchemaDataType("Long", "Mapping between java.lang.Long and BIGINT ANSI type", true));
        entityList.add(newSchemaDataType("Short", "Mapping between java.lang.Short and SMALLINT ANSI type", true));
        entityList.add(newSchemaDataType("Float", "Mapping between java.lang.Float and FLOAT ANSI type", true));
        entityList.add(newSchemaDataType("Double", "Mapping between java.lang.Double and DOUBLE ANSI type", true));
        entityList.add(newSchemaDataType("BigDecimal", "Mapping between java.math.BigDecimal and NUMERIC ANSI type", true));

        //Dates
        entityList.add(newSchemaDataType("Date", "Mapping between java.sql.Date and DATE ANSI type", false));
        entityList.add(newSchemaDataType("Time", "Mapping between java.sql.Time and TIME ANSI type", false));
        entityList.add(newSchemaDataType("Timestamp", "Mapping between java.util.Date or java.sql.Timestamp and TIMESTAMP ANSI type", false));
        entityList.add(newSchemaDataType("Calendar", "Mapping between java.util.Calendar  and TIMESTAMP ANSI type", false));

        //Other
        entityList.add(newSchemaDataType("String", "Mapping between java.lang.String and VARCHAR ANSI type", false));
        entityList.add(newSchemaDataType("Byte", "Mapping between java.lang.Byte and TINYINT ANSI type", false));
        entityList.add(newSchemaDataType("Boolean", "Mapping between java.lang.Boolean and BIT ANSI type", false));

        return entityList;
    }

    private SchemaDataType newSchemaDataType(String name, String description, boolean numeric) {
        SchemaDataType dataType = new SchemaDataType();
        dataType.setName(name);
        dataType.setDescription(description);
        dataType.setNumeric(numeric);
        return dataType;
    }


    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////
    public SchemaDataTypeRepository getSchemaDataTypeRepository() {
        return schemaDataTypeRepository;
    }

    public void setSchemaDataTypeRepository(SchemaDataTypeRepository schemaDataTypeRepository) {
        this.schemaDataTypeRepository = schemaDataTypeRepository;
    }
}