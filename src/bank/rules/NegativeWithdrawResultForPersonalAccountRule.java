package bank.rules;

import bank.model.Person;
import framework.interfaces.IAccount;
import framework.interfaces.IRule;

public class NegativeWithdrawResultForPersonalAccountRule implements IRule {

	@Override
	public boolean isApplicable(IAccount account, double amount) {
		return (account.getCustomer() instanceof Person) && (account.getBalance() - amount < 0);
	}

	@Override
	public void apply(IAccount account, double amount) {
		account.getCustomer().sendEmail("A withdraw of " + amount + " cannot be made from your account as your balance is low. Account number: " + account.getAccountNumber());

	}

}
