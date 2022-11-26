package bank.rules;

import bank.model.Company;
import bank.model.Person;
import framework.interfaces.IAccount;
import framework.interfaces.IRule;

public class HighDepositAmountForPersonalAccountRule implements IRule {

	@Override
	public boolean isApplicable(IAccount account, double amount) {
		return (account.getCustomer() instanceof Person) && amount > 400;
	}

	@Override
	public void apply(IAccount account, double amount) {
		account.getCustomer().sendEmail("A deposit of " + amount + " have been made in your account. Account number: " + account.getAccountNumber());
	}

}
