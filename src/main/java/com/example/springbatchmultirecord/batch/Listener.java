package com.example.springbatchmultirecord.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class Listener extends JobExecutionListenerSupport {

  Logger logger = LoggerFactory.getLogger(Listener.class);

  @Override
  public void beforeJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.STARTED) {
      logger.info("Batch process started!");
    }
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      logger.info("Batch process completed!");
    }
  }
}
