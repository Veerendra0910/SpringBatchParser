package com.springbatch.parser.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

import com.springbatch.parser.entity.Person;
import com.springbatch.parser.listener.PersonListener;
import com.springbatch.parser.processor.PersonProcessor;
import com.springbatch.parser.repository.PersonRepository;
import com.thoughtworks.xstream.security.ExplicitTypePermission;

@Configuration
@EnableBatchProcessing
public class PersonBatchConfig {

	@Autowired
	private PersonRepository personRepository;

	@Value("${xml.file.path}")
	private String xmlFilePath;

	@Bean
	public Job personJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("personJob", jobRepository).listener(listener())
				.flow(personStep(jobRepository, transactionManager)).end().build();
	}

	@Bean
	public Step personStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("personStep", jobRepository).<Person, Person>chunk(10, transactionManager)
				.reader(personReader()).processor(personProcessor()).writer(personItemWriter()).build();

	}

	@Bean
	public PersonProcessor personProcessor() {
		return new PersonProcessor();
	}

	@Bean
	public PersonListener listener() {
		return new PersonListener();
	}

	@Bean
	public StaxEventItemReader<Person> personReader() {
		StaxEventItemReader<Person> reader = new StaxEventItemReader<Person>();
		reader.setResource(new ClassPathResource(xmlFilePath));
		reader.setFragmentRootElementName("person");

		Map<String, String> aliasesMap = new HashMap<String, String>();
		aliasesMap.put("person", "com.springbatch.parser.entity.Person");
		XStreamMarshaller marshaller = new XStreamMarshaller();
		ExplicitTypePermission typePermission = new ExplicitTypePermission(new Class[] { Person.class });
		marshaller.setTypePermissions(typePermission);
		marshaller.setAliases(aliasesMap);

		reader.setUnmarshaller(marshaller);
		return reader;
	}

	@Bean
	public RepositoryItemWriter<Person> personItemWriter() {
		RepositoryItemWriter<Person> repositoryItemWriter = new RepositoryItemWriter<>();
		repositoryItemWriter.setRepository(personRepository);
		repositoryItemWriter.setMethodName("save");
		return repositoryItemWriter;
	}

}
