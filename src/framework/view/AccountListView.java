package framework.view;

import framework.interfaces.ICustomer;
import framework.model.Account;
import framework.model.Address;
import framework.model.IModel;

public class AccountListView extends ListView {
	private static final long serialVersionUID = -4938094676546203232L;
	
	public AccountListView() {
		super();
		this.addColumn("Account Num");
		this.addColumn("Name");
		this.addColumn("City");
		this.addColumn("P/C");
		this.addColumn("Ch/Sv");
		this.addColumn("Balance");
		
		this.setIndexOfAccountNumberColumn(0);
		this.setIndexOfAmountColumn(5);
	}
	
	@Override
	public void addAnItem(IModel model) {
		if(!(model instanceof Account)) return;
		Account item = (Account)model;
		String typeOfAccount = "Ch";
		//if(item instanceof Savings) typeOfAccount = "S";
		
		ICustomer customer = item.getCustomer();
		String typeOfCustomer = "P";
		//if(customer instanceof Company) typeOfCustomer = "C";
			
		Address address = customer.getAddress();
		this.addAnItem(item.getAccountNumber(), customer.getName(),
				address.getCity(), typeOfCustomer,
				typeOfAccount, item.getBalance());
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
