package com.quartz.example;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class App
{
    public static void main( String[] args ) throws SchedulerException {


        JobDetail job = newJob(ExampleJob.class)
                            .withIdentity("example job", "example group")
                            .build();

        Trigger trigger = newTrigger()
                                .withIdentity("exampleTrigger", "exampleTriggerGroup")
                                .startNow()
                                .withSchedule(
                                        simpleSchedule()
                                                .withIntervalInMilliseconds(1000)
                                                .repeatForever()
                                )
                                .build();

        SchedulerFactory schFactory = new StdSchedulerFactory();

        Scheduler sch = schFactory.getScheduler();

        sch.start();

        sch.scheduleJob(job, trigger);
    }

    public class ExampleJob implements Job {

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            System.out.println(String.format("Executing current job: {0)", jobExecutionContext));
        }
    }

}
