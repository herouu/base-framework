package com.example.commons.job;

import com.example.commons.job.component.JobComponent;
import com.example.commons.job.component.ZKComponent;
import com.example.commons.job.exception.QuartzJobException;
import com.example.commons.job.listener.AllListener;
import com.example.commons.job.listener.OnceJobListener;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wanghongmeng on 2016/5/31.
 */
public class JobExecutor {
    private static final Logger log = LoggerFactory.getLogger(AllListener.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static Properties properties;
    private static SchedulerFactory schedulerFactory;
    private static Scheduler scheduler;

    static {
        try {
            properties = new Properties();
            properties.put("org.quartz.threadPool.threadCount", "50");
            properties.put("org.quartz.plugin.triggHistory.class", "org.quartz.plugins.history.LoggingTriggerHistoryPlugin");
            properties.put("org.quartz.plugin.triggHistory.triggerFiredMessage", "Trigger {1}.{0} fired job {6}.{5} at{4, date, yyyy-MM-dd HH:mm:ss}");
            properties.put("org.quartz.plugin.triggHistory.triggerMisfiredMessage", "Trigger {1}.{0} misfired job {6}.{5}  at{4, date, yyyy-MM-dd HH:mm:ss}. Should have fired at: {3, date, yyyy-MM-dd HH:mm:ss}");
            properties.put("org.quartz.plugin.triggHistory.triggerCompleteMessage", "Trigger {1}.{0} completed firing job {6}.{5} at{4, date, yyyy-MM-dd HH:mm:ss}");
            properties.put("org.quartz.plugin.jobHistory.class", "org.quartz.plugins.history.LoggingJobHistoryPlugin");
            properties.put("org.quartz.plugin.jobHistory.jobToBeFiredMessage", "Job {1}.{0} fired (by trigger {4}.{3}) at{2, date, yyyy-MM-dd HH:mm:ss}");
            properties.put("org.quartz.plugin.jobHistory.jobSuccessMessage", "Job {1}.{0} execution complete at{2, date, yyyy-MM-dd HH:mm:ss}");
            properties.put("org.quartz.plugin.jobHistory.jobFailedMessage", "Job {1}.{0} execution failed at{2, date, yyyy-MM-dd HH:mm:ss}");
            properties.put("org.quartz.plugin.jobHistory.jobWasVetoedMessage", "Job {1}.{0} was vetoed. It was to be fired (by trigger {4}.{3}) at{2, date, yyyy-MM-dd HH:mm:ss}");
            properties.put("org.quartz.plugin.shutdownhook.class", "org.quartz.plugins.management.ShutdownHookPlugin");
            properties.put("org.quartz.plugin.shutdownhook.cleanShutdown", "true");

            schedulerFactory = new StdSchedulerFactory(properties);
            scheduler = schedulerFactory.getScheduler();
            //scheduler.getListenerManager().addJobListener(new AllJobListener(), EverythingMatcher.allJobs());
            //scheduler.getListenerManager().addTriggerListener(new AllTriggerListener(), EverythingMatcher.allTriggers());
        } catch (SchedulerException e) {
            throw new QuartzJobException(ExceptionUtils.getStackTrace(e));
        }
    }

    public JobExecutor execute(JobComponent job) {
        try {
            JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroupName());
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getTriggerGroupName());
            if (scheduler.checkExists(jobKey) || scheduler.checkExists(triggerKey)) {
                throw new QuartzJobException("任务已存在");
            }

            if (!CronExpression.isValidExpression(job.getCronExpression())) {
                throw new QuartzJobException("非法的cronExpression表达式:" + job.getCronExpression());
            }

            JobDataMap jobDataMap = new JobDataMap();
            for (Map.Entry<String, Object> entry : job.getComponentMap().entrySet()) {
                jobDataMap.put(entry.getKey(), entry.getValue());
            }
            if (null != job.getZkNodes()) {
                ZKComponent.ZKClient zkClient = new ZKComponent.ZKClient(job.getZkNodes());
                jobDataMap.put("zkClient", zkClient);
            }

            JobDetail jobDetail = JobBuilder.newJob(job.getClass()).requestRecovery(true)
                    .withIdentity(jobKey).usingJobData(jobDataMap).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression()).withMisfireHandlingInstructionFireAndProceed())
                    .startNow().build();

            scheduler.scheduleJob(jobDetail, trigger);

            return this;
        } catch (Exception e) {
            throw new QuartzJobException(ExceptionUtils.getStackTrace(e));
        }
    }

    @PostConstruct
    public void start() {
        try {
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new QuartzJobException(ExceptionUtils.getStackTrace(e));
        }
    }

    @PreDestroy
    public void shutdown() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown(true);
            }
        } catch (SchedulerException e) {
            throw new QuartzJobException(ExceptionUtils.getStackTrace(e));
        }
    }

    public void executeOnce(JobComponent job) {
        try {
            JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroupName());
            scheduler.getListenerManager().addJobListener(new OnceJobListener(), KeyMatcher.keyEquals(jobKey));
            execute(job);
        } catch (Exception e) {
            throw new QuartzJobException(ExceptionUtils.getStackTrace(e));
        }
    }

    public void delete(JobComponent job) {
        try {
            JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroupName());
            if (!scheduler.checkExists(jobKey)) {
                throw new QuartzJobException("任务不存在");
            }

            String deletedTime = DateFormatUtils.format(new Date(), DATE_FORMAT);
            scheduler.deleteJob(jobKey);
            log.info("job {} delete at {}", jobKey, deletedTime);
        } catch (SchedulerException e) {
            throw new QuartzJobException(ExceptionUtils.getStackTrace(e));
        }
    }
}
