package framework.model;

import framework.interfaces.ICustomer;

public class DefaultAccount extends Account {

    protected DefaultAccount(String accountNumber, ICustomer customer) {
        super(accountNumber, customer);
    }

    @Override
    public void addInterest() {

    }

	@Override
	public void save() {
        this.notifyViews();
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
}
