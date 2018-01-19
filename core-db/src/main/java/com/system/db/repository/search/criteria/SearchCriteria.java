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

    private String property;
    private String operator;
    private String value;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    //////////////////////////////////////////////////////////////////////

    public SearchCriteria() {
        super();
    }

    public SearchCriteria(String property, String operator, String value) {
        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Getter/Setters                                                  //////////
    //////////////////////////////////////////////////////////////////////

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
