package bank.rules;

import bank.model.Person;
import framework.interfaces.IAccount;
import framework.interfaces.IRule;

public class HighWithdrawAmountForPersonalAccountRule implements IRule {

	@Override
	public boolean isApplicable(IAccount account, double amount) {
		return (account.getCustomer() instanceof Person) && amount > 400;
	}

	@Override
	public void apply(IAccount account, double amount) {
		account.getCustomer().sendEmail("A withdraw of " + amount + " have been made from your account. Account number: " + account.getAccountNumber());

	}

}
