package bank.rules;

import bank.model.Company;
import framework.interfaces.IAccount;
import framework.interfaces.IRule;

public class SendEmailToCompanyForWithdrawRule implements IRule {

	@Override
	public boolean isApplicable(IAccount account, double amount) {
		return account.getCustomer() instanceof Company;
	}

	@Override
	public void apply(IAccount account, double amount) {
		account.getCustomer().sendEmail("A withdraw of " + amount + " has been made from your account. Account number: " + account.getAccountNumber());
	}

}
