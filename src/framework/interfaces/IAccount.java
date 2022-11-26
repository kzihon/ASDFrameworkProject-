package framework.interfaces;

import framework.model.IModel;

import java.util.List;

public interface IAccount extends IModel {
	double getBalance();
	void makeWithdrawal(double amount, IEntry accountOperation);
	void makeDeposit(double amount, IEntry accountOperation);
	ICustomer getCustomer();
	void addInterest();
	String getAccountNumber();
	double getInterestRate();
	List<IEntry> getOperations();
}
