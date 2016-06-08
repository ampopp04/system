package com.system.db.repository.search.criteria;

/**
 * The <class>SearchCriteria</class> defines a search criteria
 * object
 *
 * @author Andrew
 * @see com.system.db.repository.specification.EntitySpecification
 */
public class SearchCriteria {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private String key;
    private String operation;
    private Object value;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    //////////////////////////////////////////////////////////////////////

    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Getter/Setters                                                  //////////
    //////////////////////////////////////////////////////////////////////

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
