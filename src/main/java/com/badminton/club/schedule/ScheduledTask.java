package com.badminton.club.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.badminton.club.service.ScheduleService;

@Component
public class ScheduledTask {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);
	@Autowired
	private ScheduleService scheduleService;

    @Scheduled(cron = "0 2 0 * * ?")
    public void reportCurrentTimeCron() throws InterruptedException {
        this.scheduleService.updateActivityStatus();
        
        log.info("執行batch", new Date());
    }

}
