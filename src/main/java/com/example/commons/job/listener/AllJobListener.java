package com.example.commons.job.listener;

import com.example.commons.job.exception.QuartzJobException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;

import java.util.Date;

/**
 * Created by wanghongmeng on 2016/6/2.
 */
@Slf4j
public class AllJobListener extends AllListener implements JobListener {
    private static final int SLEEP_MILLI_SECONDS = 10000;

    @Override
    public String getName() {
        return "allJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        String jobRunTime = DateFormatUtils.format(new Date(), DATE_FORMAT);
        log.info("start job:'{}', jobRunTime:'{}'", jobKey, jobRunTime);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        //do nothing
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobKey jobKey = context.getJobDetail().getKey();
        String jobCompleteTime = DateFormatUtils.format(new Date(), DATE_FORMAT);
        if (null != jobException) {
            log.info("job:'{}' failed and will refire, exception:'{}'", jobKey,
                    ExceptionUtils.getStackFrames(jobException));
            jobException.setRefireImmediately(true);
            try {
                Thread.sleep(SLEEP_MILLI_SECONDS);
            } catch (InterruptedException e) {
                throw new QuartzJobException(ExceptionUtils.getStackTrace(e));
            }
        } else {
            log.info("job:'{}' executed successfully, jobCompleteTime:'{}'", jobKey, jobCompleteTime);
        }
    }
}
