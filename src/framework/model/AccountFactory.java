package framework.model;

import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;

public interface AccountFactory {
    public IAccount createAccount(ICustomer customer, String accountNumber);
}
