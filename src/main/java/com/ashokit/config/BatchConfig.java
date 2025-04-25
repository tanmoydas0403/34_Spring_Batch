package com.ashokit.config;

// Import necessary classes
import com.ashokit.batch.ExcelItemReader;
import com.ashokit.entity.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

// Marks this class as a Spring configuration class
@Configuration
public class BatchConfig {

    // Define a Spring Batch Job bean that will handle the customer import process
    @Bean
    public Job importCustomerJob(JobRepository jobRepository, Step step) {
        // Create a new job named "importCustomerJob" using JobBuilder
        return new JobBuilder("importCustomerJob", jobRepository)
                .start(step)    // Define the first (and only) step of the job
                .build();       // Build the job configuration
    }

    // Define a Step bean that contains the actual batch processing logic
    @Bean
    public Step step(JobRepository jobRepository,
                    PlatformTransactionManager transactionManager,
                    JpaItemWriter<Customer> writer) {
        return new StepBuilder("step", jobRepository)
                // Configure chunk-oriented processing with chunk size of 10
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(excelItemReader(null))  // Set the reader (null will be replaced at runtime)
                .processor(processor())         // Set the processor
                .writer(writer)                 // Set the writer
                .build();                       // Build the step configuration
    }

    // Define the Excel reader bean that will read data from the Excel file
    @Bean
    @StepScope // Enables late binding of job parameters
    public ExcelItemReader excelItemReader(
            // Inject the filePath parameter from job parameters at runtime
            @Value("#{jobParameters['filePath']}") String filePath) {
        return new ExcelItemReader(filePath);
    }

    // Define the processor bean that can transform or filter items
    @Bean
    public ItemProcessor<Customer, Customer> processor() {
        // Currently just passes through items without modification
        // You can add validation, transformation, or filtering logic here
        return item -> {
            // Add any processing logic here
            return item;
        };
    }

    // Define the writer bean that will save processed items to the database
    @Bean
    public JpaItemWriter<Customer> writer(EntityManagerFactory entityManagerFactory) {
        // Create a JPA writer that will persist Customer entities
        return new JpaItemWriterBuilder<Customer>()
                .entityManagerFactory(entityManagerFactory)  // Set the entity manager factory
                .build();                                   // Build the writer configuration
    }
}