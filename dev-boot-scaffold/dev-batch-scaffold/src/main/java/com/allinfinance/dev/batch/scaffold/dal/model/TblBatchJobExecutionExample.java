package com.allinfinance.dev.batch.scaffold.dal.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TblBatchJobExecutionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblBatchJobExecutionExample() {
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

        public Criteria andVersionIsNull() {
            addCriterion("VERSION is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("VERSION is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Long value) {
            addCriterion("VERSION =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Long value) {
            addCriterion("VERSION <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Long value) {
            addCriterion("VERSION >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Long value) {
            addCriterion("VERSION >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Long value) {
            addCriterion("VERSION <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Long value) {
            addCriterion("VERSION <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Long> values) {
            addCriterion("VERSION in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Long> values) {
            addCriterion("VERSION not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Long value1, Long value2) {
            addCriterion("VERSION between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Long value1, Long value2) {
            addCriterion("VERSION not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdIsNull() {
            addCriterion("JOB_INSTANCE_ID is null");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdIsNotNull() {
            addCriterion("JOB_INSTANCE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdEqualTo(Long value) {
            addCriterion("JOB_INSTANCE_ID =", value, "jobInstanceId");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdNotEqualTo(Long value) {
            addCriterion("JOB_INSTANCE_ID <>", value, "jobInstanceId");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdGreaterThan(Long value) {
            addCriterion("JOB_INSTANCE_ID >", value, "jobInstanceId");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdGreaterThanOrEqualTo(Long value) {
            addCriterion("JOB_INSTANCE_ID >=", value, "jobInstanceId");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdLessThan(Long value) {
            addCriterion("JOB_INSTANCE_ID <", value, "jobInstanceId");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdLessThanOrEqualTo(Long value) {
            addCriterion("JOB_INSTANCE_ID <=", value, "jobInstanceId");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdIn(List<Long> values) {
            addCriterion("JOB_INSTANCE_ID in", values, "jobInstanceId");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdNotIn(List<Long> values) {
            addCriterion("JOB_INSTANCE_ID not in", values, "jobInstanceId");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdBetween(Long value1, Long value2) {
            addCriterion("JOB_INSTANCE_ID between", value1, value2, "jobInstanceId");
            return (Criteria) this;
        }

        public Criteria andJobInstanceIdNotBetween(Long value1, Long value2) {
            addCriterion("JOB_INSTANCE_ID not between", value1, value2, "jobInstanceId");
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

        public Criteria andCreateTimeEqualTo(LocalDateTime value) {
            addCriterion("CREATE_TIME =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
            addCriterion("CREATE_TIME <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
            addCriterion("CREATE_TIME >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("CREATE_TIME >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(LocalDateTime value) {
            addCriterion("CREATE_TIME <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("CREATE_TIME <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<LocalDateTime> values) {
            addCriterion("CREATE_TIME in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
            addCriterion("CREATE_TIME not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("CREATE_TIME between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("CREATE_TIME not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("START_TIME is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("START_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(LocalDateTime value) {
            addCriterion("START_TIME =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(LocalDateTime value) {
            addCriterion("START_TIME <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(LocalDateTime value) {
            addCriterion("START_TIME >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("START_TIME >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(LocalDateTime value) {
            addCriterion("START_TIME <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("START_TIME <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<LocalDateTime> values) {
            addCriterion("START_TIME in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<LocalDateTime> values) {
            addCriterion("START_TIME not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("START_TIME between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("START_TIME not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("END_TIME is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("END_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(LocalDateTime value) {
            addCriterion("END_TIME =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(LocalDateTime value) {
            addCriterion("END_TIME <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(LocalDateTime value) {
            addCriterion("END_TIME >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("END_TIME >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(LocalDateTime value) {
            addCriterion("END_TIME <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("END_TIME <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<LocalDateTime> values) {
            addCriterion("END_TIME in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<LocalDateTime> values) {
            addCriterion("END_TIME not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("END_TIME between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("END_TIME not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("STATUS is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("STATUS =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("STATUS <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("STATUS >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("STATUS >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("STATUS <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("STATUS <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("STATUS like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("STATUS not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("STATUS in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("STATUS not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("STATUS between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("STATUS not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andExitCodeIsNull() {
            addCriterion("EXIT_CODE is null");
            return (Criteria) this;
        }

        public Criteria andExitCodeIsNotNull() {
            addCriterion("EXIT_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andExitCodeEqualTo(String value) {
            addCriterion("EXIT_CODE =", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeNotEqualTo(String value) {
            addCriterion("EXIT_CODE <>", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeGreaterThan(String value) {
            addCriterion("EXIT_CODE >", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeGreaterThanOrEqualTo(String value) {
            addCriterion("EXIT_CODE >=", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeLessThan(String value) {
            addCriterion("EXIT_CODE <", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeLessThanOrEqualTo(String value) {
            addCriterion("EXIT_CODE <=", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeLike(String value) {
            addCriterion("EXIT_CODE like", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeNotLike(String value) {
            addCriterion("EXIT_CODE not like", value, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeIn(List<String> values) {
            addCriterion("EXIT_CODE in", values, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeNotIn(List<String> values) {
            addCriterion("EXIT_CODE not in", values, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeBetween(String value1, String value2) {
            addCriterion("EXIT_CODE between", value1, value2, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitCodeNotBetween(String value1, String value2) {
            addCriterion("EXIT_CODE not between", value1, value2, "exitCode");
            return (Criteria) this;
        }

        public Criteria andExitMessageIsNull() {
            addCriterion("EXIT_MESSAGE is null");
            return (Criteria) this;
        }

        public Criteria andExitMessageIsNotNull() {
            addCriterion("EXIT_MESSAGE is not null");
            return (Criteria) this;
        }

        public Criteria andExitMessageEqualTo(String value) {
            addCriterion("EXIT_MESSAGE =", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageNotEqualTo(String value) {
            addCriterion("EXIT_MESSAGE <>", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageGreaterThan(String value) {
            addCriterion("EXIT_MESSAGE >", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageGreaterThanOrEqualTo(String value) {
            addCriterion("EXIT_MESSAGE >=", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageLessThan(String value) {
            addCriterion("EXIT_MESSAGE <", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageLessThanOrEqualTo(String value) {
            addCriterion("EXIT_MESSAGE <=", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageLike(String value) {
            addCriterion("EXIT_MESSAGE like", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageNotLike(String value) {
            addCriterion("EXIT_MESSAGE not like", value, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageIn(List<String> values) {
            addCriterion("EXIT_MESSAGE in", values, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageNotIn(List<String> values) {
            addCriterion("EXIT_MESSAGE not in", values, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageBetween(String value1, String value2) {
            addCriterion("EXIT_MESSAGE between", value1, value2, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andExitMessageNotBetween(String value1, String value2) {
            addCriterion("EXIT_MESSAGE not between", value1, value2, "exitMessage");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedIsNull() {
            addCriterion("LAST_UPDATED is null");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedIsNotNull() {
            addCriterion("LAST_UPDATED is not null");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedEqualTo(LocalDateTime value) {
            addCriterion("LAST_UPDATED =", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedNotEqualTo(LocalDateTime value) {
            addCriterion("LAST_UPDATED <>", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedGreaterThan(LocalDateTime value) {
            addCriterion("LAST_UPDATED >", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("LAST_UPDATED >=", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedLessThan(LocalDateTime value) {
            addCriterion("LAST_UPDATED <", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("LAST_UPDATED <=", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedIn(List<LocalDateTime> values) {
            addCriterion("LAST_UPDATED in", values, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedNotIn(List<LocalDateTime> values) {
            addCriterion("LAST_UPDATED not in", values, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("LAST_UPDATED between", value1, value2, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("LAST_UPDATED not between", value1, value2, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationIsNull() {
            addCriterion("JOB_CONFIGURATION_LOCATION is null");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationIsNotNull() {
            addCriterion("JOB_CONFIGURATION_LOCATION is not null");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationEqualTo(String value) {
            addCriterion("JOB_CONFIGURATION_LOCATION =", value, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationNotEqualTo(String value) {
            addCriterion("JOB_CONFIGURATION_LOCATION <>", value, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationGreaterThan(String value) {
            addCriterion("JOB_CONFIGURATION_LOCATION >", value, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationGreaterThanOrEqualTo(String value) {
            addCriterion("JOB_CONFIGURATION_LOCATION >=", value, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationLessThan(String value) {
            addCriterion("JOB_CONFIGURATION_LOCATION <", value, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationLessThanOrEqualTo(String value) {
            addCriterion("JOB_CONFIGURATION_LOCATION <=", value, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationLike(String value) {
            addCriterion("JOB_CONFIGURATION_LOCATION like", value, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationNotLike(String value) {
            addCriterion("JOB_CONFIGURATION_LOCATION not like", value, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationIn(List<String> values) {
            addCriterion("JOB_CONFIGURATION_LOCATION in", values, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationNotIn(List<String> values) {
            addCriterion("JOB_CONFIGURATION_LOCATION not in", values, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationBetween(String value1, String value2) {
            addCriterion("JOB_CONFIGURATION_LOCATION between", value1, value2, "jobConfigurationLocation");
            return (Criteria) this;
        }

        public Criteria andJobConfigurationLocationNotBetween(String value1, String value2) {
            addCriterion("JOB_CONFIGURATION_LOCATION not between", value1, value2, "jobConfigurationLocation");
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
