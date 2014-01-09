package com.quartz.example.jobs;

import org.quartz.*;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RetryJob implements Job {

    public static final String RETRIES_FIELD = "retries";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println(String.format("Executing Job: %s", jobExecutionContext.toString()));

        try {
            int i = 5 / 0;
        } catch(Exception ex) {
            JobExecutionException jobExecutionException = new JobExecutionException(ex);

            int retries = retrieveAndUpdateAmountOfRetriesLeft(jobExecutionContext);

            System.out.println(String.format("Retries left: %s", retries));

            if(retries <= 0) {
                System.out.println("Out of retries... stopping this job from getting triggered!");

                jobExecutionException.setUnscheduleAllTriggers(true);

                throw jobExecutionException;
            }
        }

    }

    private int retrieveAndUpdateAmountOfRetriesLeft(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int retries = (Integer) jobDataMap.get(RETRIES_FIELD);

        retries--;

        jobDataMap.put(RETRIES_FIELD, retries);

        return retries;
    }

}
