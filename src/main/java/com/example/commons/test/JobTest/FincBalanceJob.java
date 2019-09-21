package com.example.commons.test.JobTest;

import com.example.commons.job.DistributedJob;
import com.example.commons.service.CommonsService;
import com.example.commons.test.HireComLink;
import com.example.commons.test.HireComLinkService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class FincBalanceJob extends DistributedJob {

    private HireComLinkService hireComLinkService;

    public FincBalanceJob() {
        hireComLinkService = CommonsService.getBean(HireComLinkService.class);
    }

    @Override
    public void executeJob(Map<String, Object> map) {

        log.info("定时任务 00:00 执行开始==============================》》》");
        try {
            HireComLink hireComLinkById = hireComLinkService.getHireComLinkById(1L);
            log.info("hireComLinkById:{}", hireComLinkById);
            log.info("定时任务 00:00 执行结束==============================》》》");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("定时任务 00:00 执行失败==============================》》》");
        }
    }
}