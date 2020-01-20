package com.automation.session;

import com.automation.domain.TransactionType;

public class TransactionTypeSession {
	
	private static TransactionTypeSession instance;
	private TransactionType transactionType;
	
	public static TransactionTypeSession getInstance() {
		
		if(instance == null) {
			instance = new TransactionTypeSession();
		}
		return instance;
		
	}
	
	public TransactionType getTransactionType() {
		return this.transactionType;
	}
	
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	
}
