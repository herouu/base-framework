package com.example.commons.test.JobTest.config;

import com.example.commons.job.JobExecutor;
import com.example.commons.test.JobTest.FincBalanceJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;


/**
 * Created by forest on 2018/3/13
 */
@Configuration
public class JobConfiguration {


    @Value("#{'${nodes.zk}'.split(',')}")
    private Set<String> zkNodes;


    @Bean
    public JobExecutor calBanlanceJob() {
        FincBalanceJob fincBalanceJob = new FincBalanceJob();
        fincBalanceJob.setZkNodes(zkNodes);
        // 每分
        fincBalanceJob.setCronExpression("0 * * * * ?");
        fincBalanceJob.setJobGroupName("frameJobGroup");
        fincBalanceJob.setJobName("frameJobName");
        fincBalanceJob.setTriggerGroupName("frameTriggersGroup1");
        fincBalanceJob.setTriggerName("frameJobTriggers1");
        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(fincBalanceJob);
        return jobExecutor;
    }


}
