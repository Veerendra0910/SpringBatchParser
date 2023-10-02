package com.springbatch.parser.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.springbatch.parser.entity.Book;
import com.springbatch.parser.listener.BookListener;
import com.springbatch.parser.processor.BookProcessor;
import com.springbatch.parser.repository.BookRepository;

@Configuration
@EnableBatchProcessing
public class BookBatchConfig {

	@Autowired
	private BookRepository bookRepository;

	@Value("${json.file.path}")
	private String jsonFilePath;

	@Bean
	public Step bookStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("bookStep", jobRepository).<Book, Book>chunk(10, platformTransactionManager)
				.reader(jsonItemReader()).processor(bookProcessor()).writer(bookWriter()).build();
	}

	@Bean
	public Job bookJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new JobBuilder("bookJob", jobRepository).listener(bookJsonListener())
				.flow(bookStep(jobRepository, platformTransactionManager)).end().build();
	}

	@Bean
	public BookProcessor bookProcessor() {
		return new BookProcessor();
	}

	@Bean
	public BookListener bookJsonListener() {
		return new BookListener();
	}

	@Bean
	public JsonItemReader<Book> jsonItemReader() {
		return new JsonItemReaderBuilder<Book>().jsonObjectReader(new JacksonJsonObjectReader<>(Book.class))
				.resource(new ClassPathResource(jsonFilePath)).name("jsonReader").build();
	}

	@Bean
	public RepositoryItemWriter<Book> bookWriter() {
		RepositoryItemWriter<Book> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(bookRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}
}
