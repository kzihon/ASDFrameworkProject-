package framework.model;

import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;

public class DefaultAccountFactory implements AccountFactory {
	protected DefaultAccountFactory() { }

	@Override
	public IAccount createAccount(ICustomer customer, String accountNumber) {
		return new DefaultAccount(accountNumber, customer);
	}

}
