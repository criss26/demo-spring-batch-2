package com.example.batch.demo.spring.batch.row.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.example.batch.demo.spring.batch.entity.Transaction;


public class TransactionRowMapper implements RowMapper<Transaction> {
	
	@Override
	public Transaction mapRow(ResultSet resultSet, int i) throws SQLException {
		return new Transaction(resultSet.getLong("id"),
							   resultSet.getString("description"));
	}
	
}