package com.system.db.repository.search.criteria.property;

import com.jayway.jsonpath.InvalidPathException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

public enum SearchCriteriaPropertyOperators {
    AND("&&") {
        @Override
        public Predicate processCompoundLogicalOperator(CriteriaBuilder builder, Predicate... restrictions) {
            return builder.and(restrictions);
        }
    },
    NOT("!") {
        @Override
        public Predicate processCompoundLogicalOperator(CriteriaBuilder builder, Predicate... restrictions) {
            return builder.notEqual(restrictions[0], restrictions[1]);
        }
    },
    OR("||") {
        @Override
        public Predicate processCompoundLogicalOperator(CriteriaBuilder builder, Predicate... restrictions) {
            return builder.or(restrictions);
        }
    };

    private final String operatorString;

    public abstract Predicate processCompoundLogicalOperator(CriteriaBuilder builder, Predicate... restrictions);

    SearchCriteriaPropertyOperators(String operatorString) {
        this.operatorString = operatorString;
    }

    public String getOperatorString() {
        return operatorString;
    }

    @Override
    public String toString() {
        return operatorString;
    }

    public static SearchCriteriaPropertyOperators fromString(String operatorString) {
        if (AND.operatorString.equals(operatorString)) return AND;
        else if (NOT.operatorString.equals(operatorString)) return NOT;
        else if (OR.operatorString.equals(operatorString)) return OR;
        else throw new InvalidPathException("Failed to parse operator " + operatorString);
    }

}
