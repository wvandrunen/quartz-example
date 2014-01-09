package com.quartz.example;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class StopOnExceptionJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            int i = 5 / 0;
        } catch(Exception ex) {
            System.out.println(String.format("Exception catched %s - this job shouldn't start again!", ex));

            JobExecutionException jobExecutionException = new JobExecutionException(ex);

            jobExecutionException.setUnscheduleAllTriggers(true);

            throw jobExecutionException;
        }
    }
}
