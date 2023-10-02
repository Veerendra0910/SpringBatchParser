package com.springbatch.parser.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/springbatch")
public class ParserController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("customerJob")
	private Job customerJob;

	@Autowired
	@Qualifier("personJob")
	private Job personJob;

	@Autowired
	@Qualifier("bookJob")
	private Job bookJob;

	@GetMapping("xml/loadData")
	public String importXmlToDBJob() {
		String status = null;
		try {
			JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(personJob, jobParameters);
			status = new String("Xml File Loaded Successfully!!");
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
		return status;
	}

	@GetMapping("csv/loadData")
	public String importCsvToDBJob() {
		String status = null;
		try {
			JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(customerJob, jobParameters);
			status = new String("CSV File Loaded Successfully!!");
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
		return status;
	}

	@GetMapping("json/loadData")
	public String importJsonToDBJob() {
		String status = null;
		try {
			JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(bookJob, jobParameters);
			status = new String("JSON File Loaded Successfully!!");
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
		return status;
	}
}
