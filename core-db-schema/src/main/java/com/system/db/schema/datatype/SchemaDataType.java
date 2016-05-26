package com.system.db.schema.datatype;


import com.system.db.entity.named.NamedEntity;
import com.system.db.schema.table.column.SchemaTableColumn;

import javax.persistence.Entity;

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
@Entity
public class SchemaDataType extends NamedEntity<Short> {
    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////
    /**
     * Denotes whether this is a numeric data type or otherwise
     */
    private boolean numeric;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaDataType() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public boolean isNumeric() {
        return numeric;
    }

    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }
}