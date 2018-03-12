/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.schema.datatype;


import com.system.util.collection.CollectionUtils;

import java.util.Set;

/**
 * The <class>SchemaDataTypes</class> defines data types
 * as an enum that map to SchemaDataType
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
 * Byte[]                          [B Array of Java Byte                                    BLOB
 * <p>
 * Boolean	                    boolean or java.lang.Boolean	                    BIT
 * <p>
 * Date	                            java.util.Date or java.sql.Date	                DATE
 * Time	                            java.util.Date or java.sql.Time             	    TIME
 * Timestamp         	        java.util.Date or java.sql.Timestamp	    TIMESTAMP
 * Calendar	                 java.util.Calendar                                      	        TIMESTAMP
 * <p>
 *
 * @author Andrew
 * @see SchemaDataType
 */
public enum SchemaDataTypes {
    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    NUMBER("Integer", "Long", "Short", "Float", "Double", "Big Decimal"),
    STRING("String", "Byte"),
    BOOLEAN("Boolean"),
    FILE("ByteArray"),
    DATE("LocalDate", "LocalTime", "LocalDateTime", "Time", "Timestamp", "Calendar");

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////

    private Set<String> dataTypeNameSet;

    SchemaDataTypes(String... dataTypeName) {
        this.dataTypeNameSet = CollectionUtils.asSet(dataTypeName);
    }

    public boolean isType(String dataTypeName) {
        return getDataTypeNameSet().contains(dataTypeName);
    }

    public Set<String> getDataTypeNameSet() {
        return dataTypeNameSet;
    }

    public void setDataTypeNameSet(Set<String> dataTypeNameSet) {
        this.dataTypeNameSet = dataTypeNameSet;
    }
}