package com.allinfinance.dev.ccs.dal.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblUserExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblUserExample() {
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

        public Criteria andUserIdIsNull() {
            addCriterion("USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("USER_ID =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("USER_ID <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("USER_ID >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("USER_ID >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("USER_ID <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("USER_ID <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("USER_ID like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("USER_ID not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("USER_ID in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("USER_ID not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("USER_ID between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("USER_ID not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("USER_NAME is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("USER_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("USER_NAME =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("USER_NAME <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("USER_NAME >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("USER_NAME >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("USER_NAME <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("USER_NAME <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("USER_NAME like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("USER_NAME not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("USER_NAME in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("USER_NAME not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("USER_NAME between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("USER_NAME not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserPassIsNull() {
            addCriterion("USER_PASS is null");
            return (Criteria) this;
        }

        public Criteria andUserPassIsNotNull() {
            addCriterion("USER_PASS is not null");
            return (Criteria) this;
        }

        public Criteria andUserPassEqualTo(String value) {
            addCriterion("USER_PASS =", value, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassNotEqualTo(String value) {
            addCriterion("USER_PASS <>", value, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassGreaterThan(String value) {
            addCriterion("USER_PASS >", value, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassGreaterThanOrEqualTo(String value) {
            addCriterion("USER_PASS >=", value, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassLessThan(String value) {
            addCriterion("USER_PASS <", value, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassLessThanOrEqualTo(String value) {
            addCriterion("USER_PASS <=", value, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassLike(String value) {
            addCriterion("USER_PASS like", value, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassNotLike(String value) {
            addCriterion("USER_PASS not like", value, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassIn(List<String> values) {
            addCriterion("USER_PASS in", values, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassNotIn(List<String> values) {
            addCriterion("USER_PASS not in", values, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassBetween(String value1, String value2) {
            addCriterion("USER_PASS between", value1, value2, "userPass");
            return (Criteria) this;
        }

        public Criteria andUserPassNotBetween(String value1, String value2) {
            addCriterion("USER_PASS not between", value1, value2, "userPass");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNull() {
            addCriterion("ROLE_ID is null");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNotNull() {
            addCriterion("ROLE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andRoleIdEqualTo(String value) {
            addCriterion("ROLE_ID =", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotEqualTo(String value) {
            addCriterion("ROLE_ID <>", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThan(String value) {
            addCriterion("ROLE_ID >", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThanOrEqualTo(String value) {
            addCriterion("ROLE_ID >=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThan(String value) {
            addCriterion("ROLE_ID <", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThanOrEqualTo(String value) {
            addCriterion("ROLE_ID <=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLike(String value) {
            addCriterion("ROLE_ID like", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotLike(String value) {
            addCriterion("ROLE_ID not like", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIn(List<String> values) {
            addCriterion("ROLE_ID in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotIn(List<String> values) {
            addCriterion("ROLE_ID not in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdBetween(String value1, String value2) {
            addCriterion("ROLE_ID between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotBetween(String value1, String value2) {
            addCriterion("ROLE_ID not between", value1, value2, "roleId");
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

        public Criteria andInitPassIsNull() {
            addCriterion("INIT_PASS is null");
            return (Criteria) this;
        }

        public Criteria andInitPassIsNotNull() {
            addCriterion("INIT_PASS is not null");
            return (Criteria) this;
        }

        public Criteria andInitPassEqualTo(String value) {
            addCriterion("INIT_PASS =", value, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassNotEqualTo(String value) {
            addCriterion("INIT_PASS <>", value, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassGreaterThan(String value) {
            addCriterion("INIT_PASS >", value, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassGreaterThanOrEqualTo(String value) {
            addCriterion("INIT_PASS >=", value, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassLessThan(String value) {
            addCriterion("INIT_PASS <", value, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassLessThanOrEqualTo(String value) {
            addCriterion("INIT_PASS <=", value, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassLike(String value) {
            addCriterion("INIT_PASS like", value, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassNotLike(String value) {
            addCriterion("INIT_PASS not like", value, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassIn(List<String> values) {
            addCriterion("INIT_PASS in", values, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassNotIn(List<String> values) {
            addCriterion("INIT_PASS not in", values, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassBetween(String value1, String value2) {
            addCriterion("INIT_PASS between", value1, value2, "initPass");
            return (Criteria) this;
        }

        public Criteria andInitPassNotBetween(String value1, String value2) {
            addCriterion("INIT_PASS not between", value1, value2, "initPass");
            return (Criteria) this;
        }

        public Criteria andErrorNumIsNull() {
            addCriterion("ERROR_NUM is null");
            return (Criteria) this;
        }

        public Criteria andErrorNumIsNotNull() {
            addCriterion("ERROR_NUM is not null");
            return (Criteria) this;
        }

        public Criteria andErrorNumEqualTo(String value) {
            addCriterion("ERROR_NUM =", value, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumNotEqualTo(String value) {
            addCriterion("ERROR_NUM <>", value, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumGreaterThan(String value) {
            addCriterion("ERROR_NUM >", value, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumGreaterThanOrEqualTo(String value) {
            addCriterion("ERROR_NUM >=", value, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumLessThan(String value) {
            addCriterion("ERROR_NUM <", value, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumLessThanOrEqualTo(String value) {
            addCriterion("ERROR_NUM <=", value, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumLike(String value) {
            addCriterion("ERROR_NUM like", value, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumNotLike(String value) {
            addCriterion("ERROR_NUM not like", value, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumIn(List<String> values) {
            addCriterion("ERROR_NUM in", values, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumNotIn(List<String> values) {
            addCriterion("ERROR_NUM not in", values, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumBetween(String value1, String value2) {
            addCriterion("ERROR_NUM between", value1, value2, "errorNum");
            return (Criteria) this;
        }

        public Criteria andErrorNumNotBetween(String value1, String value2) {
            addCriterion("ERROR_NUM not between", value1, value2, "errorNum");
            return (Criteria) this;
        }

        public Criteria andPassStatusIsNull() {
            addCriterion("PASS_STATUS is null");
            return (Criteria) this;
        }

        public Criteria andPassStatusIsNotNull() {
            addCriterion("PASS_STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andPassStatusEqualTo(String value) {
            addCriterion("PASS_STATUS =", value, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusNotEqualTo(String value) {
            addCriterion("PASS_STATUS <>", value, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusGreaterThan(String value) {
            addCriterion("PASS_STATUS >", value, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusGreaterThanOrEqualTo(String value) {
            addCriterion("PASS_STATUS >=", value, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusLessThan(String value) {
            addCriterion("PASS_STATUS <", value, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusLessThanOrEqualTo(String value) {
            addCriterion("PASS_STATUS <=", value, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusLike(String value) {
            addCriterion("PASS_STATUS like", value, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusNotLike(String value) {
            addCriterion("PASS_STATUS not like", value, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusIn(List<String> values) {
            addCriterion("PASS_STATUS in", values, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusNotIn(List<String> values) {
            addCriterion("PASS_STATUS not in", values, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusBetween(String value1, String value2) {
            addCriterion("PASS_STATUS between", value1, value2, "passStatus");
            return (Criteria) this;
        }

        public Criteria andPassStatusNotBetween(String value1, String value2) {
            addCriterion("PASS_STATUS not between", value1, value2, "passStatus");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeIsNull() {
            addCriterion("LAST_PASS_UPDATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeIsNotNull() {
            addCriterion("LAST_PASS_UPDATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeEqualTo(Date value) {
            addCriterion("LAST_PASS_UPDATE_TIME =", value, "lastPassUpdateTime");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeNotEqualTo(Date value) {
            addCriterion("LAST_PASS_UPDATE_TIME <>", value, "lastPassUpdateTime");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeGreaterThan(Date value) {
            addCriterion("LAST_PASS_UPDATE_TIME >", value, "lastPassUpdateTime");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LAST_PASS_UPDATE_TIME >=", value, "lastPassUpdateTime");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeLessThan(Date value) {
            addCriterion("LAST_PASS_UPDATE_TIME <", value, "lastPassUpdateTime");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("LAST_PASS_UPDATE_TIME <=", value, "lastPassUpdateTime");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeIn(List<Date> values) {
            addCriterion("LAST_PASS_UPDATE_TIME in", values, "lastPassUpdateTime");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeNotIn(List<Date> values) {
            addCriterion("LAST_PASS_UPDATE_TIME not in", values, "lastPassUpdateTime");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("LAST_PASS_UPDATE_TIME between", value1, value2, "lastPassUpdateTime");
            return (Criteria) this;
        }

        public Criteria andLastPassUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("LAST_PASS_UPDATE_TIME not between", value1, value2, "lastPassUpdateTime");
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

        public Criteria andReservedField4IsNull() {
            addCriterion("RESERVED_FIELD4 is null");
            return (Criteria) this;
        }

        public Criteria andReservedField4IsNotNull() {
            addCriterion("RESERVED_FIELD4 is not null");
            return (Criteria) this;
        }

        public Criteria andReservedField4EqualTo(String value) {
            addCriterion("RESERVED_FIELD4 =", value, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4NotEqualTo(String value) {
            addCriterion("RESERVED_FIELD4 <>", value, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4GreaterThan(String value) {
            addCriterion("RESERVED_FIELD4 >", value, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4GreaterThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD4 >=", value, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4LessThan(String value) {
            addCriterion("RESERVED_FIELD4 <", value, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4LessThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD4 <=", value, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4Like(String value) {
            addCriterion("RESERVED_FIELD4 like", value, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4NotLike(String value) {
            addCriterion("RESERVED_FIELD4 not like", value, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4In(List<String> values) {
            addCriterion("RESERVED_FIELD4 in", values, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4NotIn(List<String> values) {
            addCriterion("RESERVED_FIELD4 not in", values, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4Between(String value1, String value2) {
            addCriterion("RESERVED_FIELD4 between", value1, value2, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField4NotBetween(String value1, String value2) {
            addCriterion("RESERVED_FIELD4 not between", value1, value2, "reservedField4");
            return (Criteria) this;
        }

        public Criteria andReservedField5IsNull() {
            addCriterion("RESERVED_FIELD5 is null");
            return (Criteria) this;
        }

        public Criteria andReservedField5IsNotNull() {
            addCriterion("RESERVED_FIELD5 is not null");
            return (Criteria) this;
        }

        public Criteria andReservedField5EqualTo(String value) {
            addCriterion("RESERVED_FIELD5 =", value, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5NotEqualTo(String value) {
            addCriterion("RESERVED_FIELD5 <>", value, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5GreaterThan(String value) {
            addCriterion("RESERVED_FIELD5 >", value, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5GreaterThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD5 >=", value, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5LessThan(String value) {
            addCriterion("RESERVED_FIELD5 <", value, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5LessThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD5 <=", value, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5Like(String value) {
            addCriterion("RESERVED_FIELD5 like", value, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5NotLike(String value) {
            addCriterion("RESERVED_FIELD5 not like", value, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5In(List<String> values) {
            addCriterion("RESERVED_FIELD5 in", values, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5NotIn(List<String> values) {
            addCriterion("RESERVED_FIELD5 not in", values, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5Between(String value1, String value2) {
            addCriterion("RESERVED_FIELD5 between", value1, value2, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField5NotBetween(String value1, String value2) {
            addCriterion("RESERVED_FIELD5 not between", value1, value2, "reservedField5");
            return (Criteria) this;
        }

        public Criteria andReservedField6IsNull() {
            addCriterion("RESERVED_FIELD6 is null");
            return (Criteria) this;
        }

        public Criteria andReservedField6IsNotNull() {
            addCriterion("RESERVED_FIELD6 is not null");
            return (Criteria) this;
        }

        public Criteria andReservedField6EqualTo(String value) {
            addCriterion("RESERVED_FIELD6 =", value, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6NotEqualTo(String value) {
            addCriterion("RESERVED_FIELD6 <>", value, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6GreaterThan(String value) {
            addCriterion("RESERVED_FIELD6 >", value, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6GreaterThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD6 >=", value, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6LessThan(String value) {
            addCriterion("RESERVED_FIELD6 <", value, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6LessThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD6 <=", value, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6Like(String value) {
            addCriterion("RESERVED_FIELD6 like", value, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6NotLike(String value) {
            addCriterion("RESERVED_FIELD6 not like", value, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6In(List<String> values) {
            addCriterion("RESERVED_FIELD6 in", values, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6NotIn(List<String> values) {
            addCriterion("RESERVED_FIELD6 not in", values, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6Between(String value1, String value2) {
            addCriterion("RESERVED_FIELD6 between", value1, value2, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField6NotBetween(String value1, String value2) {
            addCriterion("RESERVED_FIELD6 not between", value1, value2, "reservedField6");
            return (Criteria) this;
        }

        public Criteria andReservedField7IsNull() {
            addCriterion("RESERVED_FIELD7 is null");
            return (Criteria) this;
        }

        public Criteria andReservedField7IsNotNull() {
            addCriterion("RESERVED_FIELD7 is not null");
            return (Criteria) this;
        }

        public Criteria andReservedField7EqualTo(String value) {
            addCriterion("RESERVED_FIELD7 =", value, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7NotEqualTo(String value) {
            addCriterion("RESERVED_FIELD7 <>", value, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7GreaterThan(String value) {
            addCriterion("RESERVED_FIELD7 >", value, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7GreaterThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD7 >=", value, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7LessThan(String value) {
            addCriterion("RESERVED_FIELD7 <", value, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7LessThanOrEqualTo(String value) {
            addCriterion("RESERVED_FIELD7 <=", value, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7Like(String value) {
            addCriterion("RESERVED_FIELD7 like", value, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7NotLike(String value) {
            addCriterion("RESERVED_FIELD7 not like", value, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7In(List<String> values) {
            addCriterion("RESERVED_FIELD7 in", values, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7NotIn(List<String> values) {
            addCriterion("RESERVED_FIELD7 not in", values, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7Between(String value1, String value2) {
            addCriterion("RESERVED_FIELD7 between", value1, value2, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andReservedField7NotBetween(String value1, String value2) {
            addCriterion("RESERVED_FIELD7 not between", value1, value2, "reservedField7");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNull() {
            addCriterion("LAST_LOGIN_TIME is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNotNull() {
            addCriterion("LAST_LOGIN_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeEqualTo(Date value) {
            addCriterion("LAST_LOGIN_TIME =", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotEqualTo(Date value) {
            addCriterion("LAST_LOGIN_TIME <>", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThan(Date value) {
            addCriterion("LAST_LOGIN_TIME >", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LAST_LOGIN_TIME >=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThan(Date value) {
            addCriterion("LAST_LOGIN_TIME <", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThanOrEqualTo(Date value) {
            addCriterion("LAST_LOGIN_TIME <=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIn(List<Date> values) {
            addCriterion("LAST_LOGIN_TIME in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotIn(List<Date> values) {
            addCriterion("LAST_LOGIN_TIME not in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeBetween(Date value1, Date value2) {
            addCriterion("LAST_LOGIN_TIME between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotBetween(Date value1, Date value2) {
            addCriterion("LAST_LOGIN_TIME not between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andNotExpiredIsNull() {
            addCriterion("NOT_EXPIRED is null");
            return (Criteria) this;
        }

        public Criteria andNotExpiredIsNotNull() {
            addCriterion("NOT_EXPIRED is not null");
            return (Criteria) this;
        }

        public Criteria andNotExpiredEqualTo(String value) {
            addCriterion("NOT_EXPIRED =", value, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredNotEqualTo(String value) {
            addCriterion("NOT_EXPIRED <>", value, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredGreaterThan(String value) {
            addCriterion("NOT_EXPIRED >", value, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredGreaterThanOrEqualTo(String value) {
            addCriterion("NOT_EXPIRED >=", value, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredLessThan(String value) {
            addCriterion("NOT_EXPIRED <", value, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredLessThanOrEqualTo(String value) {
            addCriterion("NOT_EXPIRED <=", value, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredLike(String value) {
            addCriterion("NOT_EXPIRED like", value, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredNotLike(String value) {
            addCriterion("NOT_EXPIRED not like", value, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredIn(List<String> values) {
            addCriterion("NOT_EXPIRED in", values, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredNotIn(List<String> values) {
            addCriterion("NOT_EXPIRED not in", values, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredBetween(String value1, String value2) {
            addCriterion("NOT_EXPIRED between", value1, value2, "notExpired");
            return (Criteria) this;
        }

        public Criteria andNotExpiredNotBetween(String value1, String value2) {
            addCriterion("NOT_EXPIRED not between", value1, value2, "notExpired");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedIsNull() {
            addCriterion("ACCOUNT_NOT_LOCKED is null");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedIsNotNull() {
            addCriterion("ACCOUNT_NOT_LOCKED is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedEqualTo(String value) {
            addCriterion("ACCOUNT_NOT_LOCKED =", value, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedNotEqualTo(String value) {
            addCriterion("ACCOUNT_NOT_LOCKED <>", value, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedGreaterThan(String value) {
            addCriterion("ACCOUNT_NOT_LOCKED >", value, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedGreaterThanOrEqualTo(String value) {
            addCriterion("ACCOUNT_NOT_LOCKED >=", value, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedLessThan(String value) {
            addCriterion("ACCOUNT_NOT_LOCKED <", value, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedLessThanOrEqualTo(String value) {
            addCriterion("ACCOUNT_NOT_LOCKED <=", value, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedLike(String value) {
            addCriterion("ACCOUNT_NOT_LOCKED like", value, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedNotLike(String value) {
            addCriterion("ACCOUNT_NOT_LOCKED not like", value, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedIn(List<String> values) {
            addCriterion("ACCOUNT_NOT_LOCKED in", values, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedNotIn(List<String> values) {
            addCriterion("ACCOUNT_NOT_LOCKED not in", values, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedBetween(String value1, String value2) {
            addCriterion("ACCOUNT_NOT_LOCKED between", value1, value2, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andAccountNotLockedNotBetween(String value1, String value2) {
            addCriterion("ACCOUNT_NOT_LOCKED not between", value1, value2, "accountNotLocked");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredIsNull() {
            addCriterion("CREDENTIALS_NOT_EXPIRED is null");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredIsNotNull() {
            addCriterion("CREDENTIALS_NOT_EXPIRED is not null");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredEqualTo(String value) {
            addCriterion("CREDENTIALS_NOT_EXPIRED =", value, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredNotEqualTo(String value) {
            addCriterion("CREDENTIALS_NOT_EXPIRED <>", value, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredGreaterThan(String value) {
            addCriterion("CREDENTIALS_NOT_EXPIRED >", value, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredGreaterThanOrEqualTo(String value) {
            addCriterion("CREDENTIALS_NOT_EXPIRED >=", value, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredLessThan(String value) {
            addCriterion("CREDENTIALS_NOT_EXPIRED <", value, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredLessThanOrEqualTo(String value) {
            addCriterion("CREDENTIALS_NOT_EXPIRED <=", value, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredLike(String value) {
            addCriterion("CREDENTIALS_NOT_EXPIRED like", value, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredNotLike(String value) {
            addCriterion("CREDENTIALS_NOT_EXPIRED not like", value, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredIn(List<String> values) {
            addCriterion("CREDENTIALS_NOT_EXPIRED in", values, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredNotIn(List<String> values) {
            addCriterion("CREDENTIALS_NOT_EXPIRED not in", values, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredBetween(String value1, String value2) {
            addCriterion("CREDENTIALS_NOT_EXPIRED between", value1, value2, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andCredentialsNotExpiredNotBetween(String value1, String value2) {
            addCriterion("CREDENTIALS_NOT_EXPIRED not between", value1, value2, "credentialsNotExpired");
            return (Criteria) this;
        }

        public Criteria andMobileNoIsNull() {
            addCriterion("MOBILE_NO is null");
            return (Criteria) this;
        }

        public Criteria andMobileNoIsNotNull() {
            addCriterion("MOBILE_NO is not null");
            return (Criteria) this;
        }

        public Criteria andMobileNoEqualTo(String value) {
            addCriterion("MOBILE_NO =", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotEqualTo(String value) {
            addCriterion("MOBILE_NO <>", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoGreaterThan(String value) {
            addCriterion("MOBILE_NO >", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoGreaterThanOrEqualTo(String value) {
            addCriterion("MOBILE_NO >=", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoLessThan(String value) {
            addCriterion("MOBILE_NO <", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoLessThanOrEqualTo(String value) {
            addCriterion("MOBILE_NO <=", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoLike(String value) {
            addCriterion("MOBILE_NO like", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotLike(String value) {
            addCriterion("MOBILE_NO not like", value, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoIn(List<String> values) {
            addCriterion("MOBILE_NO in", values, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotIn(List<String> values) {
            addCriterion("MOBILE_NO not in", values, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoBetween(String value1, String value2) {
            addCriterion("MOBILE_NO between", value1, value2, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andMobileNoNotBetween(String value1, String value2) {
            addCriterion("MOBILE_NO not between", value1, value2, "mobileNo");
            return (Criteria) this;
        }

        public Criteria andOptFlagIsNull() {
            addCriterion("opt_flag is null");
            return (Criteria) this;
        }

        public Criteria andOptFlagIsNotNull() {
            addCriterion("opt_flag is not null");
            return (Criteria) this;
        }

        public Criteria andOptFlagEqualTo(String value) {
            addCriterion("opt_flag =", value, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagNotEqualTo(String value) {
            addCriterion("opt_flag <>", value, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagGreaterThan(String value) {
            addCriterion("opt_flag >", value, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagGreaterThanOrEqualTo(String value) {
            addCriterion("opt_flag >=", value, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagLessThan(String value) {
            addCriterion("opt_flag <", value, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagLessThanOrEqualTo(String value) {
            addCriterion("opt_flag <=", value, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagLike(String value) {
            addCriterion("opt_flag like", value, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagNotLike(String value) {
            addCriterion("opt_flag not like", value, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagIn(List<String> values) {
            addCriterion("opt_flag in", values, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagNotIn(List<String> values) {
            addCriterion("opt_flag not in", values, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagBetween(String value1, String value2) {
            addCriterion("opt_flag between", value1, value2, "optFlag");
            return (Criteria) this;
        }

        public Criteria andOptFlagNotBetween(String value1, String value2) {
            addCriterion("opt_flag not between", value1, value2, "optFlag");
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