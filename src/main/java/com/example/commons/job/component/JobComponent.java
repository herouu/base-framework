package com.example.commons.job.component;

import com.google.common.base.Strings;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by wanghongmeng on 2016/5/31.
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public abstract class JobComponent implements Job {
    protected static final Logger log = LoggerFactory.getLogger(JobComponent.class);
    private String jobName;
    private String jobGroupName;
    private String triggerName;
    private String triggerGroupName;
    private String cronExpression;
    private Set<String> zkNodes;
    private Map<String, Object> componentMap = new HashMap<>();

    public String getJobName() {
        if (Strings.isNullOrEmpty(jobName)) {
            return String.valueOf(Objects.hash(this));
        }
        return jobName;
    }

    public JobComponent setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getJobGroupName() {
        if (Strings.isNullOrEmpty(jobGroupName)) {
            return String.valueOf(Objects.hash(this));
        }
        return jobGroupName;
    }

    public JobComponent setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
        return this;
    }

    public String getTriggerName() {
        if (Strings.isNullOrEmpty(triggerName)) {
            return String.valueOf(Objects.hash(this));
        }
        return triggerName;
    }

    public JobComponent setTriggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    public String getTriggerGroupName() {
        if (Strings.isNullOrEmpty(triggerGroupName)) {
            return String.valueOf(Objects.hash(this));
        }
        return triggerGroupName;
    }

    public JobComponent setTriggerGroupName(String triggerGroupName) {
        this.triggerGroupName = triggerGroupName;
        return this;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public JobComponent setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public Set<String> getZkNodes() {
        return zkNodes;
    }

    public JobComponent setZkNodes(Set<String> zkNodes) {
        this.zkNodes = zkNodes;
        return this;
    }

    public Map<String, Object> getComponentMap() {
        return componentMap;
    }

    public JobComponent addComponent(String key, Object component) {
        if (Strings.isNullOrEmpty(key) || null == component) {
            throw new IllegalArgumentException();
        }

        componentMap.put(key, component);
        return this;
    }

    public abstract void executeJob(Map<String, Object> componentMap);
}
