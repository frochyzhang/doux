package com.allinfinance.dev.ccs.dal.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TblUserErrorLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblUserErrorLogExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andExcIdIsNull() {
            addCriterion("EXC_ID is null");
            return (Criteria) this;
        }

        public Criteria andExcIdIsNotNull() {
            addCriterion("EXC_ID is not null");
            return (Criteria) this;
        }

        public Criteria andExcIdEqualTo(Integer value) {
            addCriterion("EXC_ID =", value, "excId");
            return (Criteria) this;
        }

        public Criteria andExcIdNotEqualTo(Integer value) {
            addCriterion("EXC_ID <>", value, "excId");
            return (Criteria) this;
        }

        public Criteria andExcIdGreaterThan(Integer value) {
            addCriterion("EXC_ID >", value, "excId");
            return (Criteria) this;
        }

        public Criteria andExcIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("EXC_ID >=", value, "excId");
            return (Criteria) this;
        }

        public Criteria andExcIdLessThan(Integer value) {
            addCriterion("EXC_ID <", value, "excId");
            return (Criteria) this;
        }

        public Criteria andExcIdLessThanOrEqualTo(Integer value) {
            addCriterion("EXC_ID <=", value, "excId");
            return (Criteria) this;
        }

        public Criteria andExcIdIn(List<Integer> values) {
            addCriterion("EXC_ID in", values, "excId");
            return (Criteria) this;
        }

        public Criteria andExcIdNotIn(List<Integer> values) {
            addCriterion("EXC_ID not in", values, "excId");
            return (Criteria) this;
        }

        public Criteria andExcIdBetween(Integer value1, Integer value2) {
            addCriterion("EXC_ID between", value1, value2, "excId");
            return (Criteria) this;
        }

        public Criteria andExcIdNotBetween(Integer value1, Integer value2) {
            addCriterion("EXC_ID not between", value1, value2, "excId");
            return (Criteria) this;
        }

        public Criteria andExcRequParamIsNull() {
            addCriterion("EXC_REQU_PARAM is null");
            return (Criteria) this;
        }

        public Criteria andExcRequParamIsNotNull() {
            addCriterion("EXC_REQU_PARAM is not null");
            return (Criteria) this;
        }

        public Criteria andExcRequParamEqualTo(String value) {
            addCriterion("EXC_REQU_PARAM =", value, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamNotEqualTo(String value) {
            addCriterion("EXC_REQU_PARAM <>", value, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamGreaterThan(String value) {
            addCriterion("EXC_REQU_PARAM >", value, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamGreaterThanOrEqualTo(String value) {
            addCriterion("EXC_REQU_PARAM >=", value, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamLessThan(String value) {
            addCriterion("EXC_REQU_PARAM <", value, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamLessThanOrEqualTo(String value) {
            addCriterion("EXC_REQU_PARAM <=", value, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamLike(String value) {
            addCriterion("EXC_REQU_PARAM like", value, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamNotLike(String value) {
            addCriterion("EXC_REQU_PARAM not like", value, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamIn(List<String> values) {
            addCriterion("EXC_REQU_PARAM in", values, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamNotIn(List<String> values) {
            addCriterion("EXC_REQU_PARAM not in", values, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamBetween(String value1, String value2) {
            addCriterion("EXC_REQU_PARAM between", value1, value2, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcRequParamNotBetween(String value1, String value2) {
            addCriterion("EXC_REQU_PARAM not between", value1, value2, "excRequParam");
            return (Criteria) this;
        }

        public Criteria andExcNameIsNull() {
            addCriterion("EXC_NAME is null");
            return (Criteria) this;
        }

        public Criteria andExcNameIsNotNull() {
            addCriterion("EXC_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andExcNameEqualTo(String value) {
            addCriterion("EXC_NAME =", value, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameNotEqualTo(String value) {
            addCriterion("EXC_NAME <>", value, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameGreaterThan(String value) {
            addCriterion("EXC_NAME >", value, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameGreaterThanOrEqualTo(String value) {
            addCriterion("EXC_NAME >=", value, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameLessThan(String value) {
            addCriterion("EXC_NAME <", value, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameLessThanOrEqualTo(String value) {
            addCriterion("EXC_NAME <=", value, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameLike(String value) {
            addCriterion("EXC_NAME like", value, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameNotLike(String value) {
            addCriterion("EXC_NAME not like", value, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameIn(List<String> values) {
            addCriterion("EXC_NAME in", values, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameNotIn(List<String> values) {
            addCriterion("EXC_NAME not in", values, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameBetween(String value1, String value2) {
            addCriterion("EXC_NAME between", value1, value2, "excName");
            return (Criteria) this;
        }

        public Criteria andExcNameNotBetween(String value1, String value2) {
            addCriterion("EXC_NAME not between", value1, value2, "excName");
            return (Criteria) this;
        }

        public Criteria andExcMessageIsNull() {
            addCriterion("EXC_MESSAGE is null");
            return (Criteria) this;
        }

        public Criteria andExcMessageIsNotNull() {
            addCriterion("EXC_MESSAGE is not null");
            return (Criteria) this;
        }

        public Criteria andExcMessageEqualTo(String value) {
            addCriterion("EXC_MESSAGE =", value, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageNotEqualTo(String value) {
            addCriterion("EXC_MESSAGE <>", value, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageGreaterThan(String value) {
            addCriterion("EXC_MESSAGE >", value, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageGreaterThanOrEqualTo(String value) {
            addCriterion("EXC_MESSAGE >=", value, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageLessThan(String value) {
            addCriterion("EXC_MESSAGE <", value, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageLessThanOrEqualTo(String value) {
            addCriterion("EXC_MESSAGE <=", value, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageLike(String value) {
            addCriterion("EXC_MESSAGE like", value, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageNotLike(String value) {
            addCriterion("EXC_MESSAGE not like", value, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageIn(List<String> values) {
            addCriterion("EXC_MESSAGE in", values, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageNotIn(List<String> values) {
            addCriterion("EXC_MESSAGE not in", values, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageBetween(String value1, String value2) {
            addCriterion("EXC_MESSAGE between", value1, value2, "excMessage");
            return (Criteria) this;
        }

        public Criteria andExcMessageNotBetween(String value1, String value2) {
            addCriterion("EXC_MESSAGE not between", value1, value2, "excMessage");
            return (Criteria) this;
        }

        public Criteria andOperUserIdIsNull() {
            addCriterion("OPER_USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andOperUserIdIsNotNull() {
            addCriterion("OPER_USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andOperUserIdEqualTo(String value) {
            addCriterion("OPER_USER_ID =", value, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdNotEqualTo(String value) {
            addCriterion("OPER_USER_ID <>", value, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdGreaterThan(String value) {
            addCriterion("OPER_USER_ID >", value, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_USER_ID >=", value, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdLessThan(String value) {
            addCriterion("OPER_USER_ID <", value, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdLessThanOrEqualTo(String value) {
            addCriterion("OPER_USER_ID <=", value, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdLike(String value) {
            addCriterion("OPER_USER_ID like", value, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdNotLike(String value) {
            addCriterion("OPER_USER_ID not like", value, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdIn(List<String> values) {
            addCriterion("OPER_USER_ID in", values, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdNotIn(List<String> values) {
            addCriterion("OPER_USER_ID not in", values, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdBetween(String value1, String value2) {
            addCriterion("OPER_USER_ID between", value1, value2, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserIdNotBetween(String value1, String value2) {
            addCriterion("OPER_USER_ID not between", value1, value2, "operUserId");
            return (Criteria) this;
        }

        public Criteria andOperUserNameIsNull() {
            addCriterion("OPER_USER_NAME is null");
            return (Criteria) this;
        }

        public Criteria andOperUserNameIsNotNull() {
            addCriterion("OPER_USER_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andOperUserNameEqualTo(String value) {
            addCriterion("OPER_USER_NAME =", value, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameNotEqualTo(String value) {
            addCriterion("OPER_USER_NAME <>", value, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameGreaterThan(String value) {
            addCriterion("OPER_USER_NAME >", value, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_USER_NAME >=", value, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameLessThan(String value) {
            addCriterion("OPER_USER_NAME <", value, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameLessThanOrEqualTo(String value) {
            addCriterion("OPER_USER_NAME <=", value, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameLike(String value) {
            addCriterion("OPER_USER_NAME like", value, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameNotLike(String value) {
            addCriterion("OPER_USER_NAME not like", value, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameIn(List<String> values) {
            addCriterion("OPER_USER_NAME in", values, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameNotIn(List<String> values) {
            addCriterion("OPER_USER_NAME not in", values, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameBetween(String value1, String value2) {
            addCriterion("OPER_USER_NAME between", value1, value2, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperUserNameNotBetween(String value1, String value2) {
            addCriterion("OPER_USER_NAME not between", value1, value2, "operUserName");
            return (Criteria) this;
        }

        public Criteria andOperMethodIsNull() {
            addCriterion("OPER_METHOD is null");
            return (Criteria) this;
        }

        public Criteria andOperMethodIsNotNull() {
            addCriterion("OPER_METHOD is not null");
            return (Criteria) this;
        }

        public Criteria andOperMethodEqualTo(String value) {
            addCriterion("OPER_METHOD =", value, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodNotEqualTo(String value) {
            addCriterion("OPER_METHOD <>", value, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodGreaterThan(String value) {
            addCriterion("OPER_METHOD >", value, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_METHOD >=", value, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodLessThan(String value) {
            addCriterion("OPER_METHOD <", value, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodLessThanOrEqualTo(String value) {
            addCriterion("OPER_METHOD <=", value, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodLike(String value) {
            addCriterion("OPER_METHOD like", value, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodNotLike(String value) {
            addCriterion("OPER_METHOD not like", value, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodIn(List<String> values) {
            addCriterion("OPER_METHOD in", values, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodNotIn(List<String> values) {
            addCriterion("OPER_METHOD not in", values, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodBetween(String value1, String value2) {
            addCriterion("OPER_METHOD between", value1, value2, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperMethodNotBetween(String value1, String value2) {
            addCriterion("OPER_METHOD not between", value1, value2, "operMethod");
            return (Criteria) this;
        }

        public Criteria andOperUriIsNull() {
            addCriterion("OPER_URI is null");
            return (Criteria) this;
        }

        public Criteria andOperUriIsNotNull() {
            addCriterion("OPER_URI is not null");
            return (Criteria) this;
        }

        public Criteria andOperUriEqualTo(String value) {
            addCriterion("OPER_URI =", value, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriNotEqualTo(String value) {
            addCriterion("OPER_URI <>", value, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriGreaterThan(String value) {
            addCriterion("OPER_URI >", value, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_URI >=", value, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriLessThan(String value) {
            addCriterion("OPER_URI <", value, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriLessThanOrEqualTo(String value) {
            addCriterion("OPER_URI <=", value, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriLike(String value) {
            addCriterion("OPER_URI like", value, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriNotLike(String value) {
            addCriterion("OPER_URI not like", value, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriIn(List<String> values) {
            addCriterion("OPER_URI in", values, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriNotIn(List<String> values) {
            addCriterion("OPER_URI not in", values, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriBetween(String value1, String value2) {
            addCriterion("OPER_URI between", value1, value2, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperUriNotBetween(String value1, String value2) {
            addCriterion("OPER_URI not between", value1, value2, "operUri");
            return (Criteria) this;
        }

        public Criteria andOperIpIsNull() {
            addCriterion("OPER_IP is null");
            return (Criteria) this;
        }

        public Criteria andOperIpIsNotNull() {
            addCriterion("OPER_IP is not null");
            return (Criteria) this;
        }

        public Criteria andOperIpEqualTo(String value) {
            addCriterion("OPER_IP =", value, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpNotEqualTo(String value) {
            addCriterion("OPER_IP <>", value, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpGreaterThan(String value) {
            addCriterion("OPER_IP >", value, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_IP >=", value, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpLessThan(String value) {
            addCriterion("OPER_IP <", value, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpLessThanOrEqualTo(String value) {
            addCriterion("OPER_IP <=", value, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpLike(String value) {
            addCriterion("OPER_IP like", value, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpNotLike(String value) {
            addCriterion("OPER_IP not like", value, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpIn(List<String> values) {
            addCriterion("OPER_IP in", values, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpNotIn(List<String> values) {
            addCriterion("OPER_IP not in", values, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpBetween(String value1, String value2) {
            addCriterion("OPER_IP between", value1, value2, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperIpNotBetween(String value1, String value2) {
            addCriterion("OPER_IP not between", value1, value2, "operIp");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeIsNull() {
            addCriterion("OPER_CREATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeIsNotNull() {
            addCriterion("OPER_CREATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeEqualTo(Date value) {
            addCriterionForJDBCDate("OPER_CREATE_TIME =", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("OPER_CREATE_TIME <>", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("OPER_CREATE_TIME >", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("OPER_CREATE_TIME >=", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeLessThan(Date value) {
            addCriterionForJDBCDate("OPER_CREATE_TIME <", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("OPER_CREATE_TIME <=", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeIn(List<Date> values) {
            addCriterionForJDBCDate("OPER_CREATE_TIME in", values, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("OPER_CREATE_TIME not in", values, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("OPER_CREATE_TIME between", value1, value2, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("OPER_CREATE_TIME not between", value1, value2, "operCreateTime");
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