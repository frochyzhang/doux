package com.allinfinance.dev.ccs.dal.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblUserOptLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblUserOptLogExample() {
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

        public Criteria andOperIdIsNull() {
            addCriterion("OPER_ID is null");
            return (Criteria) this;
        }

        public Criteria andOperIdIsNotNull() {
            addCriterion("OPER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andOperIdEqualTo(Integer value) {
            addCriterion("OPER_ID =", value, "operId");
            return (Criteria) this;
        }

        public Criteria andOperIdNotEqualTo(Integer value) {
            addCriterion("OPER_ID <>", value, "operId");
            return (Criteria) this;
        }

        public Criteria andOperIdGreaterThan(Integer value) {
            addCriterion("OPER_ID >", value, "operId");
            return (Criteria) this;
        }

        public Criteria andOperIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("OPER_ID >=", value, "operId");
            return (Criteria) this;
        }

        public Criteria andOperIdLessThan(Integer value) {
            addCriterion("OPER_ID <", value, "operId");
            return (Criteria) this;
        }

        public Criteria andOperIdLessThanOrEqualTo(Integer value) {
            addCriterion("OPER_ID <=", value, "operId");
            return (Criteria) this;
        }

        public Criteria andOperIdIn(List<Integer> values) {
            addCriterion("OPER_ID in", values, "operId");
            return (Criteria) this;
        }

        public Criteria andOperIdNotIn(List<Integer> values) {
            addCriterion("OPER_ID not in", values, "operId");
            return (Criteria) this;
        }

        public Criteria andOperIdBetween(Integer value1, Integer value2) {
            addCriterion("OPER_ID between", value1, value2, "operId");
            return (Criteria) this;
        }

        public Criteria andOperIdNotBetween(Integer value1, Integer value2) {
            addCriterion("OPER_ID not between", value1, value2, "operId");
            return (Criteria) this;
        }

        public Criteria andOperModuleIsNull() {
            addCriterion("OPER_MODULE is null");
            return (Criteria) this;
        }

        public Criteria andOperModuleIsNotNull() {
            addCriterion("OPER_MODULE is not null");
            return (Criteria) this;
        }

        public Criteria andOperModuleEqualTo(String value) {
            addCriterion("OPER_MODULE =", value, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleNotEqualTo(String value) {
            addCriterion("OPER_MODULE <>", value, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleGreaterThan(String value) {
            addCriterion("OPER_MODULE >", value, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_MODULE >=", value, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleLessThan(String value) {
            addCriterion("OPER_MODULE <", value, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleLessThanOrEqualTo(String value) {
            addCriterion("OPER_MODULE <=", value, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleLike(String value) {
            addCriterion("OPER_MODULE like", value, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleNotLike(String value) {
            addCriterion("OPER_MODULE not like", value, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleIn(List<String> values) {
            addCriterion("OPER_MODULE in", values, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleNotIn(List<String> values) {
            addCriterion("OPER_MODULE not in", values, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleBetween(String value1, String value2) {
            addCriterion("OPER_MODULE between", value1, value2, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperModuleNotBetween(String value1, String value2) {
            addCriterion("OPER_MODULE not between", value1, value2, "operModule");
            return (Criteria) this;
        }

        public Criteria andOperTypeIsNull() {
            addCriterion("OPER_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andOperTypeIsNotNull() {
            addCriterion("OPER_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andOperTypeEqualTo(String value) {
            addCriterion("OPER_TYPE =", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeNotEqualTo(String value) {
            addCriterion("OPER_TYPE <>", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeGreaterThan(String value) {
            addCriterion("OPER_TYPE >", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_TYPE >=", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeLessThan(String value) {
            addCriterion("OPER_TYPE <", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeLessThanOrEqualTo(String value) {
            addCriterion("OPER_TYPE <=", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeLike(String value) {
            addCriterion("OPER_TYPE like", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeNotLike(String value) {
            addCriterion("OPER_TYPE not like", value, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeIn(List<String> values) {
            addCriterion("OPER_TYPE in", values, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeNotIn(List<String> values) {
            addCriterion("OPER_TYPE not in", values, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeBetween(String value1, String value2) {
            addCriterion("OPER_TYPE between", value1, value2, "operType");
            return (Criteria) this;
        }

        public Criteria andOperTypeNotBetween(String value1, String value2) {
            addCriterion("OPER_TYPE not between", value1, value2, "operType");
            return (Criteria) this;
        }

        public Criteria andOperDescIsNull() {
            addCriterion("OPER_DESC is null");
            return (Criteria) this;
        }

        public Criteria andOperDescIsNotNull() {
            addCriterion("OPER_DESC is not null");
            return (Criteria) this;
        }

        public Criteria andOperDescEqualTo(String value) {
            addCriterion("OPER_DESC =", value, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescNotEqualTo(String value) {
            addCriterion("OPER_DESC <>", value, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescGreaterThan(String value) {
            addCriterion("OPER_DESC >", value, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_DESC >=", value, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescLessThan(String value) {
            addCriterion("OPER_DESC <", value, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescLessThanOrEqualTo(String value) {
            addCriterion("OPER_DESC <=", value, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescLike(String value) {
            addCriterion("OPER_DESC like", value, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescNotLike(String value) {
            addCriterion("OPER_DESC not like", value, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescIn(List<String> values) {
            addCriterion("OPER_DESC in", values, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescNotIn(List<String> values) {
            addCriterion("OPER_DESC not in", values, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescBetween(String value1, String value2) {
            addCriterion("OPER_DESC between", value1, value2, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperDescNotBetween(String value1, String value2) {
            addCriterion("OPER_DESC not between", value1, value2, "operDesc");
            return (Criteria) this;
        }

        public Criteria andOperRequParamIsNull() {
            addCriterion("OPER_REQU_PARAM is null");
            return (Criteria) this;
        }

        public Criteria andOperRequParamIsNotNull() {
            addCriterion("OPER_REQU_PARAM is not null");
            return (Criteria) this;
        }

        public Criteria andOperRequParamEqualTo(String value) {
            addCriterion("OPER_REQU_PARAM =", value, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamNotEqualTo(String value) {
            addCriterion("OPER_REQU_PARAM <>", value, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamGreaterThan(String value) {
            addCriterion("OPER_REQU_PARAM >", value, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_REQU_PARAM >=", value, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamLessThan(String value) {
            addCriterion("OPER_REQU_PARAM <", value, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamLessThanOrEqualTo(String value) {
            addCriterion("OPER_REQU_PARAM <=", value, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamLike(String value) {
            addCriterion("OPER_REQU_PARAM like", value, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamNotLike(String value) {
            addCriterion("OPER_REQU_PARAM not like", value, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamIn(List<String> values) {
            addCriterion("OPER_REQU_PARAM in", values, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamNotIn(List<String> values) {
            addCriterion("OPER_REQU_PARAM not in", values, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamBetween(String value1, String value2) {
            addCriterion("OPER_REQU_PARAM between", value1, value2, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRequParamNotBetween(String value1, String value2) {
            addCriterion("OPER_REQU_PARAM not between", value1, value2, "operRequParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamIsNull() {
            addCriterion("OPER_RESP_PARAM is null");
            return (Criteria) this;
        }

        public Criteria andOperRespParamIsNotNull() {
            addCriterion("OPER_RESP_PARAM is not null");
            return (Criteria) this;
        }

        public Criteria andOperRespParamEqualTo(String value) {
            addCriterion("OPER_RESP_PARAM =", value, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamNotEqualTo(String value) {
            addCriterion("OPER_RESP_PARAM <>", value, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamGreaterThan(String value) {
            addCriterion("OPER_RESP_PARAM >", value, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_RESP_PARAM >=", value, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamLessThan(String value) {
            addCriterion("OPER_RESP_PARAM <", value, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamLessThanOrEqualTo(String value) {
            addCriterion("OPER_RESP_PARAM <=", value, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamLike(String value) {
            addCriterion("OPER_RESP_PARAM like", value, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamNotLike(String value) {
            addCriterion("OPER_RESP_PARAM not like", value, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamIn(List<String> values) {
            addCriterion("OPER_RESP_PARAM in", values, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamNotIn(List<String> values) {
            addCriterion("OPER_RESP_PARAM not in", values, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamBetween(String value1, String value2) {
            addCriterion("OPER_RESP_PARAM between", value1, value2, "operRespParam");
            return (Criteria) this;
        }

        public Criteria andOperRespParamNotBetween(String value1, String value2) {
            addCriterion("OPER_RESP_PARAM not between", value1, value2, "operRespParam");
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
            addCriterion("OPER_CREATE_TIME =", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeNotEqualTo(Date value) {
            addCriterion("OPER_CREATE_TIME <>", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeGreaterThan(Date value) {
            addCriterion("OPER_CREATE_TIME >", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("OPER_CREATE_TIME >=", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeLessThan(Date value) {
            addCriterion("OPER_CREATE_TIME <", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("OPER_CREATE_TIME <=", value, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeIn(List<Date> values) {
            addCriterion("OPER_CREATE_TIME in", values, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeNotIn(List<Date> values) {
            addCriterion("OPER_CREATE_TIME not in", values, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeBetween(Date value1, Date value2) {
            addCriterion("OPER_CREATE_TIME between", value1, value2, "operCreateTime");
            return (Criteria) this;
        }

        public Criteria andOperCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("OPER_CREATE_TIME not between", value1, value2, "operCreateTime");
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