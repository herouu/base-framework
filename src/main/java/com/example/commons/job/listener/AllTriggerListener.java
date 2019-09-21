package com.example.commons.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.TriggerListener;

import java.util.Date;

/**
 * Created by wanghongmeng on 2016/6/2.
 */
@Slf4j
public class AllTriggerListener extends AllListener implements TriggerListener {

    @Override
    public String getName() {
        return "allTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        TriggerKey triggerKey = trigger.getKey();
        String scheduledFireTime = DateFormatUtils.format(context.getScheduledFireTime(), DATE_FORMAT);
        String fireTime = DateFormatUtils.format(new Date(), DATE_FORMAT);
        log.info("trigger fired:'{}', scheduledFireTime:'{}', fireTime:'{}'", triggerKey, scheduledFireTime, fireTime);
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        TriggerKey triggerKey = trigger.getKey();
        String now = DateFormatUtils.format(new Date(), DATE_FORMAT);
        log.info("trigger misFired:'{}', timestamp:'{}'", triggerKey, now);
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        TriggerKey triggerKey = trigger.getKey();
        String endTime = DateFormatUtils.format(new Date(), DATE_FORMAT);
        log.info("trigger completed:'{}', endTime:'{}'", triggerKey, endTime);
    }
}
