package bank.rules;

import bank.model.Company;
import framework.interfaces.IAccount;
import framework.interfaces.IRule;

public class SendEmailToCompanyForDepositRule implements IRule {

	@Override
	public boolean isApplicable(IAccount account, double amount) {
		return account.getCustomer() instanceof Company;
	}

	@Override
	public void apply(IAccount account, double amount) {
		account.getCustomer().sendEmail("A deposit of " + amount + " has been made in your account. Account number: " + account.getAccountNumber());
	}

}
