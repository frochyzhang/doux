package cn.lezoo.doux.batch.scaffold.dal.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TblBatchJobExecutionParamsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblBatchJobExecutionParamsExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andJobExecutionIdIsNull() {
            addCriterion("JOB_EXECUTION_ID is null");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdIsNotNull() {
            addCriterion("JOB_EXECUTION_ID is not null");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdEqualTo(Long value) {
            addCriterion("JOB_EXECUTION_ID =", value, "jobExecutionId");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdNotEqualTo(Long value) {
            addCriterion("JOB_EXECUTION_ID <>", value, "jobExecutionId");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdGreaterThan(Long value) {
            addCriterion("JOB_EXECUTION_ID >", value, "jobExecutionId");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdGreaterThanOrEqualTo(Long value) {
            addCriterion("JOB_EXECUTION_ID >=", value, "jobExecutionId");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdLessThan(Long value) {
            addCriterion("JOB_EXECUTION_ID <", value, "jobExecutionId");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdLessThanOrEqualTo(Long value) {
            addCriterion("JOB_EXECUTION_ID <=", value, "jobExecutionId");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdIn(List<Long> values) {
            addCriterion("JOB_EXECUTION_ID in", values, "jobExecutionId");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdNotIn(List<Long> values) {
            addCriterion("JOB_EXECUTION_ID not in", values, "jobExecutionId");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdBetween(Long value1, Long value2) {
            addCriterion("JOB_EXECUTION_ID between", value1, value2, "jobExecutionId");
            return (Criteria) this;
        }

        public Criteria andJobExecutionIdNotBetween(Long value1, Long value2) {
            addCriterion("JOB_EXECUTION_ID not between", value1, value2, "jobExecutionId");
            return (Criteria) this;
        }

        public Criteria andTypeCdIsNull() {
            addCriterion("TYPE_CD is null");
            return (Criteria) this;
        }

        public Criteria andTypeCdIsNotNull() {
            addCriterion("TYPE_CD is not null");
            return (Criteria) this;
        }

        public Criteria andTypeCdEqualTo(String value) {
            addCriterion("TYPE_CD =", value, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdNotEqualTo(String value) {
            addCriterion("TYPE_CD <>", value, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdGreaterThan(String value) {
            addCriterion("TYPE_CD >", value, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdGreaterThanOrEqualTo(String value) {
            addCriterion("TYPE_CD >=", value, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdLessThan(String value) {
            addCriterion("TYPE_CD <", value, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdLessThanOrEqualTo(String value) {
            addCriterion("TYPE_CD <=", value, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdLike(String value) {
            addCriterion("TYPE_CD like", value, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdNotLike(String value) {
            addCriterion("TYPE_CD not like", value, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdIn(List<String> values) {
            addCriterion("TYPE_CD in", values, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdNotIn(List<String> values) {
            addCriterion("TYPE_CD not in", values, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdBetween(String value1, String value2) {
            addCriterion("TYPE_CD between", value1, value2, "typeCd");
            return (Criteria) this;
        }

        public Criteria andTypeCdNotBetween(String value1, String value2) {
            addCriterion("TYPE_CD not between", value1, value2, "typeCd");
            return (Criteria) this;
        }

        public Criteria andKeyNameIsNull() {
            addCriterion("KEY_NAME is null");
            return (Criteria) this;
        }

        public Criteria andKeyNameIsNotNull() {
            addCriterion("KEY_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andKeyNameEqualTo(String value) {
            addCriterion("KEY_NAME =", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameNotEqualTo(String value) {
            addCriterion("KEY_NAME <>", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameGreaterThan(String value) {
            addCriterion("KEY_NAME >", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameGreaterThanOrEqualTo(String value) {
            addCriterion("KEY_NAME >=", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameLessThan(String value) {
            addCriterion("KEY_NAME <", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameLessThanOrEqualTo(String value) {
            addCriterion("KEY_NAME <=", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameLike(String value) {
            addCriterion("KEY_NAME like", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameNotLike(String value) {
            addCriterion("KEY_NAME not like", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameIn(List<String> values) {
            addCriterion("KEY_NAME in", values, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameNotIn(List<String> values) {
            addCriterion("KEY_NAME not in", values, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameBetween(String value1, String value2) {
            addCriterion("KEY_NAME between", value1, value2, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameNotBetween(String value1, String value2) {
            addCriterion("KEY_NAME not between", value1, value2, "keyName");
            return (Criteria) this;
        }

        public Criteria andStringValIsNull() {
            addCriterion("STRING_VAL is null");
            return (Criteria) this;
        }

        public Criteria andStringValIsNotNull() {
            addCriterion("STRING_VAL is not null");
            return (Criteria) this;
        }

        public Criteria andStringValEqualTo(String value) {
            addCriterion("STRING_VAL =", value, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValNotEqualTo(String value) {
            addCriterion("STRING_VAL <>", value, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValGreaterThan(String value) {
            addCriterion("STRING_VAL >", value, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValGreaterThanOrEqualTo(String value) {
            addCriterion("STRING_VAL >=", value, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValLessThan(String value) {
            addCriterion("STRING_VAL <", value, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValLessThanOrEqualTo(String value) {
            addCriterion("STRING_VAL <=", value, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValLike(String value) {
            addCriterion("STRING_VAL like", value, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValNotLike(String value) {
            addCriterion("STRING_VAL not like", value, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValIn(List<String> values) {
            addCriterion("STRING_VAL in", values, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValNotIn(List<String> values) {
            addCriterion("STRING_VAL not in", values, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValBetween(String value1, String value2) {
            addCriterion("STRING_VAL between", value1, value2, "stringVal");
            return (Criteria) this;
        }

        public Criteria andStringValNotBetween(String value1, String value2) {
            addCriterion("STRING_VAL not between", value1, value2, "stringVal");
            return (Criteria) this;
        }

        public Criteria andDateValIsNull() {
            addCriterion("DATE_VAL is null");
            return (Criteria) this;
        }

        public Criteria andDateValIsNotNull() {
            addCriterion("DATE_VAL is not null");
            return (Criteria) this;
        }

        public Criteria andDateValEqualTo(LocalDateTime value) {
            addCriterion("DATE_VAL =", value, "dateVal");
            return (Criteria) this;
        }

        public Criteria andDateValNotEqualTo(LocalDateTime value) {
            addCriterion("DATE_VAL <>", value, "dateVal");
            return (Criteria) this;
        }

        public Criteria andDateValGreaterThan(LocalDateTime value) {
            addCriterion("DATE_VAL >", value, "dateVal");
            return (Criteria) this;
        }

        public Criteria andDateValGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("DATE_VAL >=", value, "dateVal");
            return (Criteria) this;
        }

        public Criteria andDateValLessThan(LocalDateTime value) {
            addCriterion("DATE_VAL <", value, "dateVal");
            return (Criteria) this;
        }

        public Criteria andDateValLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("DATE_VAL <=", value, "dateVal");
            return (Criteria) this;
        }

        public Criteria andDateValIn(List<LocalDateTime> values) {
            addCriterion("DATE_VAL in", values, "dateVal");
            return (Criteria) this;
        }

        public Criteria andDateValNotIn(List<LocalDateTime> values) {
            addCriterion("DATE_VAL not in", values, "dateVal");
            return (Criteria) this;
        }

        public Criteria andDateValBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("DATE_VAL between", value1, value2, "dateVal");
            return (Criteria) this;
        }

        public Criteria andDateValNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("DATE_VAL not between", value1, value2, "dateVal");
            return (Criteria) this;
        }

        public Criteria andLongValIsNull() {
            addCriterion("LONG_VAL is null");
            return (Criteria) this;
        }

        public Criteria andLongValIsNotNull() {
            addCriterion("LONG_VAL is not null");
            return (Criteria) this;
        }

        public Criteria andLongValEqualTo(Long value) {
            addCriterion("LONG_VAL =", value, "longVal");
            return (Criteria) this;
        }

        public Criteria andLongValNotEqualTo(Long value) {
            addCriterion("LONG_VAL <>", value, "longVal");
            return (Criteria) this;
        }

        public Criteria andLongValGreaterThan(Long value) {
            addCriterion("LONG_VAL >", value, "longVal");
            return (Criteria) this;
        }

        public Criteria andLongValGreaterThanOrEqualTo(Long value) {
            addCriterion("LONG_VAL >=", value, "longVal");
            return (Criteria) this;
        }

        public Criteria andLongValLessThan(Long value) {
            addCriterion("LONG_VAL <", value, "longVal");
            return (Criteria) this;
        }

        public Criteria andLongValLessThanOrEqualTo(Long value) {
            addCriterion("LONG_VAL <=", value, "longVal");
            return (Criteria) this;
        }

        public Criteria andLongValIn(List<Long> values) {
            addCriterion("LONG_VAL in", values, "longVal");
            return (Criteria) this;
        }

        public Criteria andLongValNotIn(List<Long> values) {
            addCriterion("LONG_VAL not in", values, "longVal");
            return (Criteria) this;
        }

        public Criteria andLongValBetween(Long value1, Long value2) {
            addCriterion("LONG_VAL between", value1, value2, "longVal");
            return (Criteria) this;
        }

        public Criteria andLongValNotBetween(Long value1, Long value2) {
            addCriterion("LONG_VAL not between", value1, value2, "longVal");
            return (Criteria) this;
        }

        public Criteria andDoubleValIsNull() {
            addCriterion("DOUBLE_VAL is null");
            return (Criteria) this;
        }

        public Criteria andDoubleValIsNotNull() {
            addCriterion("DOUBLE_VAL is not null");
            return (Criteria) this;
        }

        public Criteria andDoubleValEqualTo(Double value) {
            addCriterion("DOUBLE_VAL =", value, "doubleVal");
            return (Criteria) this;
        }

        public Criteria andDoubleValNotEqualTo(Double value) {
            addCriterion("DOUBLE_VAL <>", value, "doubleVal");
            return (Criteria) this;
        }

        public Criteria andDoubleValGreaterThan(Double value) {
            addCriterion("DOUBLE_VAL >", value, "doubleVal");
            return (Criteria) this;
        }

        public Criteria andDoubleValGreaterThanOrEqualTo(Double value) {
            addCriterion("DOUBLE_VAL >=", value, "doubleVal");
            return (Criteria) this;
        }

        public Criteria andDoubleValLessThan(Double value) {
            addCriterion("DOUBLE_VAL <", value, "doubleVal");
            return (Criteria) this;
        }

        public Criteria andDoubleValLessThanOrEqualTo(Double value) {
            addCriterion("DOUBLE_VAL <=", value, "doubleVal");
            return (Criteria) this;
        }

        public Criteria andDoubleValIn(List<Double> values) {
            addCriterion("DOUBLE_VAL in", values, "doubleVal");
            return (Criteria) this;
        }

        public Criteria andDoubleValNotIn(List<Double> values) {
            addCriterion("DOUBLE_VAL not in", values, "doubleVal");
            return (Criteria) this;
        }

        public Criteria andDoubleValBetween(Double value1, Double value2) {
            addCriterion("DOUBLE_VAL between", value1, value2, "doubleVal");
            return (Criteria) this;
        }

        public Criteria andDoubleValNotBetween(Double value1, Double value2) {
            addCriterion("DOUBLE_VAL not between", value1, value2, "doubleVal");
            return (Criteria) this;
        }

        public Criteria andIdentifyingIsNull() {
            addCriterion("IDENTIFYING is null");
            return (Criteria) this;
        }

        public Criteria andIdentifyingIsNotNull() {
            addCriterion("IDENTIFYING is not null");
            return (Criteria) this;
        }

        public Criteria andIdentifyingEqualTo(String value) {
            addCriterion("IDENTIFYING =", value, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingNotEqualTo(String value) {
            addCriterion("IDENTIFYING <>", value, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingGreaterThan(String value) {
            addCriterion("IDENTIFYING >", value, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingGreaterThanOrEqualTo(String value) {
            addCriterion("IDENTIFYING >=", value, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingLessThan(String value) {
            addCriterion("IDENTIFYING <", value, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingLessThanOrEqualTo(String value) {
            addCriterion("IDENTIFYING <=", value, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingLike(String value) {
            addCriterion("IDENTIFYING like", value, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingNotLike(String value) {
            addCriterion("IDENTIFYING not like", value, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingIn(List<String> values) {
            addCriterion("IDENTIFYING in", values, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingNotIn(List<String> values) {
            addCriterion("IDENTIFYING not in", values, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingBetween(String value1, String value2) {
            addCriterion("IDENTIFYING between", value1, value2, "identifying");
            return (Criteria) this;
        }

        public Criteria andIdentifyingNotBetween(String value1, String value2) {
            addCriterion("IDENTIFYING not between", value1, value2, "identifying");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
