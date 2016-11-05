package com.system.db.schema.datatype;


import com.system.db.entity.named.NamedEntity;
import com.system.db.schema.table.column.SchemaTableColumn;

/**
 * The <class>SchemaDataType</class> defines data types
 * that map Java types to ANSI types
 *
 * @author Andrew
 * @see SchemaTableColumn
 * <p>
 * <p>
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
 * Calendar	                 java.util.Calendar                                      	        TIMESTAMP
 */
public class SchemaDataType extends NamedEntity<Short> {
    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////
    /**
     * Denotes whether this is a number data type or otherwise
     */
    private boolean number;

    /**
     * The sql type.
     * <p>
     * Ex. VARCHAR
     */
    private String sqlType;

    /**
     * The java type.
     * <p>
     * Ex. java.lang.String
     */
    private String javaType;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaDataType() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public boolean isNumber() {
        return number;
    }

    public void setNumber(boolean number) {
        this.number = number;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}