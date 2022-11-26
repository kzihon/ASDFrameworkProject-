package bank.model;

import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;
import framework.model.AccountFactory;

public class SavingsAccountFactory implements AccountFactory {
    @Override
    public IAccount createAccount(ICustomer customer, String accountNumber) {
        return new SavingsAccount(accountNumber, customer);
    }
}
