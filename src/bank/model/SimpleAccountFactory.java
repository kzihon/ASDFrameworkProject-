package bank.model;

import framework.model.AccountFactory;

public class SimpleAccountFactory {
	static public AccountFactory getCheckingAccountFactory() {
		return new CheckingAccountFactory();
	}
	static public AccountFactory getSavingsAccountFactory() {
		return new SavingsAccountFactory();
	}
}
