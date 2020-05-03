package com.example.batch.demo.spring.batch.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.batch.demo.spring.batch.Item.processor.TransactionItemProcessor;
import com.example.batch.demo.spring.batch.entity.Transaction;
import com.example.batch.demo.spring.batch.row.mapper.TransactionRowMapper;


@Configuration
public class JobConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;
	
	/*
	@Bean
	public JdbcCursorItemReader<Transaction> cursorItemReader() {
		JdbcCursorItemReader<Transaction> reader = new JdbcCursorItemReader<>();
		reader.setSql("select id, description from transaction");
		reader.setDataSource(this.dataSource);
		reader.setRowMapper(new TransactionRowMapper());
		return reader;
	}*/
	
	@Bean
	public JdbcPagingItemReader<Transaction> pagingItemReader() {
	
		System.out.println("pagingItemReader");
		JdbcPagingItemReader<Transaction> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(this.dataSource);
		reader.setFetchSize(2);
		reader.setRowMapper(new TransactionRowMapper());

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, description");
		queryProvider.setFromClause("from transaction");

		Map<String, Order> sortKeys = new HashMap<>(1);

		sortKeys.put("id", Order.ASCENDING);

		queryProvider.setSortKeys(sortKeys);

		reader.setQueryProvider(queryProvider);
	
		return reader;
	}

	
	@Bean
	public ItemWriter<Transaction> transactionItemWriter() {
		System.out.println("transactionItemWriter");
		return items -> {
			for (Transaction item : items) {
				System.out.println(item.toString());
			}
		};
	}
	

	@Bean
	public TransactionItemProcessor processor() {
		return new TransactionItemProcessor();
	}

	/*
	@Bean
	public JdbcBatchItemWriter<Transaction> transactionItemWriter(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Transaction>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql("INSERT INTO transactions_processed (id, description) VALUES (:id, :description)")
			.dataSource(dataSource)
			.build();
	}*/
	

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Transaction, Transaction>chunk(2)
				.reader(pagingItemReader())
				.processor(processor())
				.writer(transactionItemWriter())
				.build();
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("job")
				.start(step1())
				.build();
	}
	
}