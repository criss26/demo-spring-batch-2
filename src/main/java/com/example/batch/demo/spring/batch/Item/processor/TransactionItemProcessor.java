package com.example.batch.demo.spring.batch.Item.processor;

import org.springframework.batch.item.ItemProcessor;
import com.example.batch.demo.spring.batch.entity.Transaction;

public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction> {

	@Override
	public Transaction process(Transaction item) throws Exception {
		
		System.out.println("process "+item.toString());
		
		final Long id = item.getId();
		final String description = item.getDescription().toUpperCase();

		final Transaction transformedTransaction = new Transaction(id, description);

		System.out.println("Converting (" + item + ") into (" + transformedTransaction + ")");

		return transformedTransaction;
		
	}

}
