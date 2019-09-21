package com.example.commons.job;

import com.example.commons.job.component.JobComponent;
import com.example.commons.job.component.ZKComponent;
import com.example.commons.job.exception.QuartzJobException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wanghongmeng on 2016/6/1.
 */
public abstract class DistributedJob extends JobComponent {
    private static final String ZK_NAMESPACE = "/cluster/job/";
    private static final String DATE_FORMAT = "yyyyMMddHHmmss";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final String instanceId = context.getFireInstanceId();
        JobKey jobKey = context.getJobDetail().getKey();
        final String scheduledFireTime = DateFormatUtils.format(context.getScheduledFireTime(), DATE_FORMAT);

        final String masterPath =
                ZK_NAMESPACE + jobKey.getGroup() + "/" + jobKey.getName() + "/executing/" + scheduledFireTime;
        final String executedPath =
                ZK_NAMESPACE + jobKey.getGroup() + "/" + jobKey.getName() + "/executed/" + scheduledFireTime;

        final Map<String, Object> jobDataMap = context.getMergedJobDataMap();
        final ZKComponent.ZKClient zkClient = (ZKComponent.ZKClient) jobDataMap.get("zkClient");

        final ZKComponent.ZKMaster zkMaster = new ZKComponent.ZKMaster(zkClient);
        final CountDownLatch gate = new CountDownLatch(1);
        zkMaster.competeMaster(masterPath, () -> {
            try {
                if (!zkClient.exist(executedPath)) {
                    executeJob(jobDataMap);
                    zkClient.add(executedPath);

                    //执行完成后删除锁
                    if (zkClient.exist(masterPath)) {
                        zkClient.delete(masterPath);
                    }
                } else {
                    String host = zkClient.get(executedPath);
                    log.info("job instanceId:{}, fireTime:{} has executed by {}", instanceId, scheduledFireTime, host);
                }
            } finally {
                zkMaster.close();
                gate.countDown();
            }
        });
        try {
            gate.await();
        } catch (InterruptedException e) {
            throw new QuartzJobException(ExceptionUtils.getStackTrace(e));
        }
    }
}
