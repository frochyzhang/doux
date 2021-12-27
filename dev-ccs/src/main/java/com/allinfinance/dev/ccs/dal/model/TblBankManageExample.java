package com.allinfinance.dev.ccs.dal.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblBankManageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblBankManageExample() {
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

        public Criteria andBankIdIsNull() {
            addCriterion("BANK_ID is null");
            return (Criteria) this;
        }

        public Criteria andBankIdIsNotNull() {
            addCriterion("BANK_ID is not null");
            return (Criteria) this;
        }

        public Criteria andBankIdEqualTo(String value) {
            addCriterion("BANK_ID =", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotEqualTo(String value) {
            addCriterion("BANK_ID <>", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdGreaterThan(String value) {
            addCriterion("BANK_ID >", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdGreaterThanOrEqualTo(String value) {
            addCriterion("BANK_ID >=", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdLessThan(String value) {
            addCriterion("BANK_ID <", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdLessThanOrEqualTo(String value) {
            addCriterion("BANK_ID <=", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdLike(String value) {
            addCriterion("BANK_ID like", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotLike(String value) {
            addCriterion("BANK_ID not like", value, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdIn(List<String> values) {
            addCriterion("BANK_ID in", values, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotIn(List<String> values) {
            addCriterion("BANK_ID not in", values, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdBetween(String value1, String value2) {
            addCriterion("BANK_ID between", value1, value2, "bankId");
            return (Criteria) this;
        }

        public Criteria andBankIdNotBetween(String value1, String value2) {
            addCriterion("BANK_ID not between", value1, value2, "bankId");
            return (Criteria) this;
        }

        public Criteria andOrgIsNull() {
            addCriterion("ORG is null");
            return (Criteria) this;
        }

        public Criteria andOrgIsNotNull() {
            addCriterion("ORG is not null");
            return (Criteria) this;
        }

        public Criteria andOrgEqualTo(String value) {
            addCriterion("ORG =", value, "org");
            return (Criteria) this;
        }

        public Criteria andOrgNotEqualTo(String value) {
            addCriterion("ORG <>", value, "org");
            return (Criteria) this;
        }

        public Criteria andOrgGreaterThan(String value) {
            addCriterion("ORG >", value, "org");
            return (Criteria) this;
        }

        public Criteria andOrgGreaterThanOrEqualTo(String value) {
            addCriterion("ORG >=", value, "org");
            return (Criteria) this;
        }

        public Criteria andOrgLessThan(String value) {
            addCriterion("ORG <", value, "org");
            return (Criteria) this;
        }

        public Criteria andOrgLessThanOrEqualTo(String value) {
            addCriterion("ORG <=", value, "org");
            return (Criteria) this;
        }

        public Criteria andOrgLike(String value) {
            addCriterion("ORG like", value, "org");
            return (Criteria) this;
        }

        public Criteria andOrgNotLike(String value) {
            addCriterion("ORG not like", value, "org");
            return (Criteria) this;
        }

        public Criteria andOrgIn(List<String> values) {
            addCriterion("ORG in", values, "org");
            return (Criteria) this;
        }

        public Criteria andOrgNotIn(List<String> values) {
            addCriterion("ORG not in", values, "org");
            return (Criteria) this;
        }

        public Criteria andOrgBetween(String value1, String value2) {
            addCriterion("ORG between", value1, value2, "org");
            return (Criteria) this;
        }

        public Criteria andOrgNotBetween(String value1, String value2) {
            addCriterion("ORG not between", value1, value2, "org");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNull() {
            addCriterion("BANK_NAME is null");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNotNull() {
            addCriterion("BANK_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andBankNameEqualTo(String value) {
            addCriterion("BANK_NAME =", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotEqualTo(String value) {
            addCriterion("BANK_NAME <>", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThan(String value) {
            addCriterion("BANK_NAME >", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThanOrEqualTo(String value) {
            addCriterion("BANK_NAME >=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThan(String value) {
            addCriterion("BANK_NAME <", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThanOrEqualTo(String value) {
            addCriterion("BANK_NAME <=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLike(String value) {
            addCriterion("BANK_NAME like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotLike(String value) {
            addCriterion("BANK_NAME not like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameIn(List<String> values) {
            addCriterion("BANK_NAME in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotIn(List<String> values) {
            addCriterion("BANK_NAME not in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameBetween(String value1, String value2) {
            addCriterion("BANK_NAME between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotBetween(String value1, String value2) {
            addCriterion("BANK_NAME not between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andIsAvailableIsNull() {
            addCriterion("IS_AVAILABLE is null");
            return (Criteria) this;
        }

        public Criteria andIsAvailableIsNotNull() {
            addCriterion("IS_AVAILABLE is not null");
            return (Criteria) this;
        }

        public Criteria andIsAvailableEqualTo(String value) {
            addCriterion("IS_AVAILABLE =", value, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableNotEqualTo(String value) {
            addCriterion("IS_AVAILABLE <>", value, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableGreaterThan(String value) {
            addCriterion("IS_AVAILABLE >", value, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableGreaterThanOrEqualTo(String value) {
            addCriterion("IS_AVAILABLE >=", value, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableLessThan(String value) {
            addCriterion("IS_AVAILABLE <", value, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableLessThanOrEqualTo(String value) {
            addCriterion("IS_AVAILABLE <=", value, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableLike(String value) {
            addCriterion("IS_AVAILABLE like", value, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableNotLike(String value) {
            addCriterion("IS_AVAILABLE not like", value, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableIn(List<String> values) {
            addCriterion("IS_AVAILABLE in", values, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableNotIn(List<String> values) {
            addCriterion("IS_AVAILABLE not in", values, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableBetween(String value1, String value2) {
            addCriterion("IS_AVAILABLE between", value1, value2, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andIsAvailableNotBetween(String value1, String value2) {
            addCriterion("IS_AVAILABLE not between", value1, value2, "isAvailable");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("CREATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("CREATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("CREATE_TIME =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("CREATE_TIME <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CREATE_TIME >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CREATE_TIME >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CREATE_TIME <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CREATE_TIME <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CREATE_TIME in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CREATE_TIME not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CREATE_TIME between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CREATE_TIME not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNull() {
            addCriterion("CREATE_BY is null");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNotNull() {
            addCriterion("CREATE_BY is not null");
            return (Criteria) this;
        }

        public Criteria andCreateByEqualTo(String value) {
            addCriterion("CREATE_BY =", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotEqualTo(String value) {
            addCriterion("CREATE_BY <>", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThan(String value) {
            addCriterion("CREATE_BY >", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThanOrEqualTo(String value) {
            addCriterion("CREATE_BY >=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThan(String value) {
            addCriterion("CREATE_BY <", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThanOrEqualTo(String value) {
            addCriterion("CREATE_BY <=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLike(String value) {
            addCriterion("CREATE_BY like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotLike(String value) {
            addCriterion("CREATE_BY not like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByIn(List<String> values) {
            addCriterion("CREATE_BY in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotIn(List<String> values) {
            addCriterion("CREATE_BY not in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByBetween(String value1, String value2) {
            addCriterion("CREATE_BY between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotBetween(String value1, String value2) {
            addCriterion("CREATE_BY not between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("UPDATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("UPDATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UPDATE_TIME =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UPDATE_TIME <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UPDATE_TIME >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UPDATE_TIME >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UPDATE_TIME <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UPDATE_TIME <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UPDATE_TIME in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UPDATE_TIME not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UPDATE_TIME between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UPDATE_TIME not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateByIsNull() {
            addCriterion("UPDATE_BY is null");
            return (Criteria) this;
        }

        public Criteria andUpdateByIsNotNull() {
            addCriterion("UPDATE_BY is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateByEqualTo(String value) {
            addCriterion("UPDATE_BY =", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotEqualTo(String value) {
            addCriterion("UPDATE_BY <>", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThan(String value) {
            addCriterion("UPDATE_BY >", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
            addCriterion("UPDATE_BY >=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThan(String value) {
            addCriterion("UPDATE_BY <", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThanOrEqualTo(String value) {
            addCriterion("UPDATE_BY <=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLike(String value) {
            addCriterion("UPDATE_BY like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotLike(String value) {
            addCriterion("UPDATE_BY not like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByIn(List<String> values) {
            addCriterion("UPDATE_BY in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotIn(List<String> values) {
            addCriterion("UPDATE_BY not in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByBetween(String value1, String value2) {
            addCriterion("UPDATE_BY between", value1, value2, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotBetween(String value1, String value2) {
            addCriterion("UPDATE_BY not between", value1, value2, "updateBy");
            return (Criteria) this;
        }

        public Criteria andReservedField1IsNull() {
            addCriterion("RESERVED_FIELD1 is null");
            return (Criteria) this;
        }

        public Criteria andReservedField1IsNotNull() {
            addCriterion("RESERVED_FIELD1 is not null");
            return (Criteria) this;
        }

        public Criteria andReservedField1EqualTo(String value) {
            addCriterion("RESERVED_FIELD1 =", value, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1NotEqualTo(String value) {
            addCriterion("RESERVED_FIELD1 <>", value, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1GreaterThan(String value) {
            addCriterion("RESERVED_FIELD1 >", value, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1GreaterThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD1 >=", value, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1LessThan(String value) {
            addCriterion("RESERVED_FIELD1 <", value, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1LessThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD1 <=", value, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1Like(String value) {
            addCriterion("RESERVED_FIELD1 like", value, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1NotLike(String value) {
            addCriterion("RESERVED_FIELD1 not like", value, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1In(List<String> values) {
            addCriterion("RESERVED_FIELD1 in", values, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1NotIn(List<String> values) {
            addCriterion("RESERVED_FIELD1 not in", values, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1Between(String value1, String value2) {
            addCriterion("RESERVED_FIELD1 between", value1, value2, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField1NotBetween(String value1, String value2) {
            addCriterion("RESERVED_FIELD1 not between", value1, value2, "reservedField1");
            return (Criteria) this;
        }

        public Criteria andReservedField2IsNull() {
            addCriterion("RESERVED_FIELD2 is null");
            return (Criteria) this;
        }

        public Criteria andReservedField2IsNotNull() {
            addCriterion("RESERVED_FIELD2 is not null");
            return (Criteria) this;
        }

        public Criteria andReservedField2EqualTo(String value) {
            addCriterion("RESERVED_FIELD2 =", value, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2NotEqualTo(String value) {
            addCriterion("RESERVED_FIELD2 <>", value, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2GreaterThan(String value) {
            addCriterion("RESERVED_FIELD2 >", value, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2GreaterThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD2 >=", value, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2LessThan(String value) {
            addCriterion("RESERVED_FIELD2 <", value, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2LessThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD2 <=", value, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2Like(String value) {
            addCriterion("RESERVED_FIELD2 like", value, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2NotLike(String value) {
            addCriterion("RESERVED_FIELD2 not like", value, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2In(List<String> values) {
            addCriterion("RESERVED_FIELD2 in", values, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2NotIn(List<String> values) {
            addCriterion("RESERVED_FIELD2 not in", values, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2Between(String value1, String value2) {
            addCriterion("RESERVED_FIELD2 between", value1, value2, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField2NotBetween(String value1, String value2) {
            addCriterion("RESERVED_FIELD2 not between", value1, value2, "reservedField2");
            return (Criteria) this;
        }

        public Criteria andReservedField3IsNull() {
            addCriterion("RESERVED_FIELD3 is null");
            return (Criteria) this;
        }

        public Criteria andReservedField3IsNotNull() {
            addCriterion("RESERVED_FIELD3 is not null");
            return (Criteria) this;
        }

        public Criteria andReservedField3EqualTo(String value) {
            addCriterion("RESERVED_FIELD3 =", value, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3NotEqualTo(String value) {
            addCriterion("RESERVED_FIELD3 <>", value, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3GreaterThan(String value) {
            addCriterion("RESERVED_FIELD3 >", value, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3GreaterThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD3 >=", value, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3LessThan(String value) {
            addCriterion("RESERVED_FIELD3 <", value, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3LessThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD3 <=", value, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3Like(String value) {
            addCriterion("RESERVED_FIELD3 like", value, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3NotLike(String value) {
            addCriterion("RESERVED_FIELD3 not like", value, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3In(List<String> values) {
            addCriterion("RESERVED_FIELD3 in", values, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3NotIn(List<String> values) {
            addCriterion("RESERVED_FIELD3 not in", values, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3Between(String value1, String value2) {
            addCriterion("RESERVED_FIELD3 between", value1, value2, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andReservedField3NotBetween(String value1, String value2) {
            addCriterion("RESERVED_FIELD3 not between", value1, value2, "reservedField3");
            return (Criteria) this;
        }

        public Criteria andBankNameEnIsNull() {
            addCriterion("BANK_NAME_EN is null");
            return (Criteria) this;
        }

        public Criteria andBankNameEnIsNotNull() {
            addCriterion("BANK_NAME_EN is not null");
            return (Criteria) this;
        }

        public Criteria andBankNameEnEqualTo(String value) {
            addCriterion("BANK_NAME_EN =", value, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnNotEqualTo(String value) {
            addCriterion("BANK_NAME_EN <>", value, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnGreaterThan(String value) {
            addCriterion("BANK_NAME_EN >", value, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnGreaterThanOrEqualTo(String value) {
            addCriterion("BANK_NAME_EN >=", value, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnLessThan(String value) {
            addCriterion("BANK_NAME_EN <", value, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnLessThanOrEqualTo(String value) {
            addCriterion("BANK_NAME_EN <=", value, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnLike(String value) {
            addCriterion("BANK_NAME_EN like", value, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnNotLike(String value) {
            addCriterion("BANK_NAME_EN not like", value, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnIn(List<String> values) {
            addCriterion("BANK_NAME_EN in", values, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnNotIn(List<String> values) {
            addCriterion("BANK_NAME_EN not in", values, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnBetween(String value1, String value2) {
            addCriterion("BANK_NAME_EN between", value1, value2, "bankNameEn");
            return (Criteria) this;
        }

        public Criteria andBankNameEnNotBetween(String value1, String value2) {
            addCriterion("BANK_NAME_EN not between", value1, value2, "bankNameEn");
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