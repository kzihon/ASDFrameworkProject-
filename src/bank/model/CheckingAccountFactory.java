package bank.model;

import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;
import framework.model.AccountFactory;

public class CheckingAccountFactory implements AccountFactory {
    @Override
    public IAccount createAccount(ICustomer customer, String accountNumber) {
        return new CheckingAccount(accountNumber, customer);
    }
}
