package com.example.batch.demo.spring.batch.entity;

public class Transaction {
	
	private Long id;
	private String description;
	
	public Transaction(Long id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", description=" + description + "]";
	}

}
