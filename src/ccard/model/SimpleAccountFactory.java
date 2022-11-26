package ccard.model;

import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;
import framework.model.AccountFactory;

public class SimpleAccountFactory implements AccountFactory {
    @Override
    public IAccount createAccount(ICustomer customer, String accountNumber) {
        return new CCAccount(accountNumber, customer);
    }
}
