package com.quartz.example;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class App
{
    public static void main( String[] args ) throws SchedulerException {

        JobDetail simpleJob = newJob(SimpleJob.class)
                            .withIdentity("example simpleJob", "example group")
                            .build();

        JobDetail stopOnExceptionJob = newJob(StopOnExceptionJob.class)
                            .build();

        JobDetail refireOnExceptionJob = newJob(RefireOnExceptionJob.class)
                            .build();

        Trigger trigger = newTrigger()
                                .withIdentity("exampleTrigger", "exampleTriggerGroup")
                                .withSchedule(
                                        simpleSchedule()
                                                .withIntervalInMilliseconds(1000)
                                                .repeatForever()
                                )
                                .build();

        SchedulerFactory schFactory = new StdSchedulerFactory();

        Scheduler sch = schFactory.getScheduler();

        sch.start();

        //sch.scheduleJob(simpleJob, trigger);
        //sch.scheduleJob(stopOnExceptionJob, trigger);

        sch.scheduleJob(refireOnExceptionJob, trigger);
    }

}
