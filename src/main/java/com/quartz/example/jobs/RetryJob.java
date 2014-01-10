package com.quartz.example.jobs;

import org.quartz.*;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RetryJob implements Job {

    public static final String RETRIES_FIELD = "retries";
    public static final int RETRIES_AMOUNT = 3;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println(String.format("Executing Job: %s", jobExecutionContext.toString()));

        try {
            int i = 5 / 0;
        } catch(Exception ex) {
            JobExecutionException jobExecutionException = new JobExecutionException(ex);

            int retries = retrieveAndUpdateAmountOfRetriesLeft(jobExecutionContext);

            System.out.println(String.format("Retries left: %s", retries));

            if(retries >= 0) {

                System.out.println("Wait 2 seconds and the refire!");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {}

                jobExecutionException.setRefireImmediately(true);

                throw jobExecutionException;

            } else {
                System.out.println("No retries left. The iteration of the job can try again... resetting the retries");

                resetRetries(jobExecutionContext);
            }
        }
    }

    private void resetRetries(JobExecutionContext jobExecutionContext) {
        jobExecutionContext.getJobDetail().getJobDataMap().put(RETRIES_FIELD, RETRIES_AMOUNT);
    }

    private int retrieveAndUpdateAmountOfRetriesLeft(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int retries = (Integer) jobDataMap.get(RETRIES_FIELD);

        retries--;

        jobDataMap.put(RETRIES_FIELD, retries);

        return retries;
    }

}
