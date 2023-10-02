package com.springbatch.parser.listener;

import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerListener implements JobExecutionListener {

	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Customer Job Started At "+ jobExecution.getStartTime().format(dateTimeFormatter));
		System.out.println("Customer Job Status At Start"+ jobExecution.getStatus());
	}

	public void afterJob(JobExecution jobExecution) {
		System.out.println("Customer Job Stopped At " + jobExecution.getEndTime().format(dateTimeFormatter));
		System.out.println("Customer Job Status At End"+ jobExecution.getStatus());
	}
}
