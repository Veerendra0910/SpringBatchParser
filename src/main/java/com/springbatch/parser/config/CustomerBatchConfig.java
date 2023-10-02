package com.springbatch.parser.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.springbatch.parser.entity.Customer;
import com.springbatch.parser.listener.CustomerListener;
import com.springbatch.parser.processor.CustomerProcessor;
import com.springbatch.parser.repository.CustomerRepository;

@Configuration
@EnableBatchProcessing
public class CustomerBatchConfig {

	@Autowired
	private CustomerRepository customerRepository;

	@Value("${csv.file.path}")
	private String csvFilePath;

	@Bean
	public Step customerStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("customerStep", jobRepository).<Customer, Customer>chunk(10, platformTransactionManager)
				.reader(customerReader()).processor(customerProcessor()).writer(customerWriter()).build();
	}

	@Bean
	public Job customerJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new JobBuilder("customerJob", jobRepository).listener(customerCsvListener())
				.flow(customerStep(jobRepository, platformTransactionManager)).end().build();
	}

	@Bean
	public CustomerProcessor customerProcessor() {
		return new CustomerProcessor();
	}

	@Bean
	public CustomerListener customerCsvListener() {
		return new CustomerListener();
	}

	@Bean
	public FlatFileItemReader<Customer> customerReader() {
		FlatFileItemReader<Customer> customerItemReader = new FlatFileItemReader<>();
		customerItemReader.setResource(new ClassPathResource(csvFilePath));
		customerItemReader.setName("csvReader");
		customerItemReader.setLinesToSkip(1);
		customerItemReader.setLineMapper(lineMapper());
		return customerItemReader;
	}

	private LineMapper<Customer> lineMapper() {
		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(Boolean.FALSE);
		lineTokenizer.setNames(Customer.COLUMNCUSTOMER);

		BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Customer.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);

		return lineMapper;
	}

	@Bean
	public RepositoryItemWriter<Customer> customerWriter() {
		RepositoryItemWriter<Customer> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(customerRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}
}
