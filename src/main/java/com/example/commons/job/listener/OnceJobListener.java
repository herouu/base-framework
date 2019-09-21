package com.example.commons.job.listener;

import com.example.commons.job.exception.QuartzJobException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;

import java.util.Date;

/**
 * Created by wanghongmeng on 2016/6/2.
 */
@Slf4j
public class OnceJobListener extends AllListener implements JobListener {

    @Override
    public String getName() {
        return "onceJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        // do nothing
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        // do nothing
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        try {
            JobKey jobKey = context.getJobDetail().getKey();
            String deletedTime = DateFormatUtils.format(new Date(), DATE_FORMAT);
            context.getScheduler().deleteJob(jobKey);
            log.info("job {} execution once complete, delete at {}", jobKey, deletedTime);
        } catch (SchedulerException e) {
            throw new QuartzJobException(ExceptionUtils.getStackTrace(e));
        }
    }
}
