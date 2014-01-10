package com.quartz.example;

import com.quartz.example.jobs.RetryJob;
import com.quartz.example.jobs.SimpleJob;
import com.quartz.example.jobs.StopOnExceptionJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class App
{
    public static void main( String[] args ) throws SchedulerException {
        // Jobs
        JobDetail simpleJob = createSimpleJob();
        JobDetail stopOnExceptionJob = createStopOnExceptionJob();
        JobDetail retryJob = createRetryJob();

        // Trigger
        Trigger trigger = createTrigger();

        // Creating the scheduler
        SchedulerFactory schFactory = new StdSchedulerFactory();

        Scheduler scheduler = schFactory.getScheduler();

        // Starting the jobs
        scheduler.start();

        //scheduler.scheduleJob(simpleJob, trigger);
        //scheduler.scheduleJob(stopOnExceptionJob, trigger);
        scheduler.scheduleJob(retryJob, trigger);
    }

    private static SimpleTrigger createTrigger() {
        return newTrigger()
            .withIdentity("exampleTrigger", "exampleTriggerGroup")
            .withSchedule(
                    simpleSchedule()
                            .withIntervalInSeconds(1)
                            .repeatForever()
            )
            .build();
    }

    private static JobDetail createRetryJob() {
        JobDetail jobDetail = newJob(RetryJob.class)
                .build();

        jobDetail.getJobDataMap().put(RetryJob.RETRIES_FIELD, RetryJob.RETRIES_AMOUNT);

        return jobDetail;
    }

    private static JobDetail createStopOnExceptionJob() {
        return newJob(StopOnExceptionJob.class)
                .build();
    }

    private static JobDetail createSimpleJob() {
        return newJob(SimpleJob.class)
                .withIdentity("example simpleJob", "example group")
                .build();
    }

}
