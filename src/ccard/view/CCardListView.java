package ccard.view;

import ccard.model.BronzeCCard;
import ccard.model.CreditCard;
import ccard.model.SilverCCard;
import framework.interfaces.ICustomer;
import framework.model.Account;
import framework.model.Address;
import framework.model.IModel;
import framework.view.ListView;

import java.time.format.DateTimeFormatter;

public class CCardListView extends ListView {
	private static final long serialVersionUID = -4938094676546203232L;

	public CCardListView() {
		super();
		this.addColumn("Name");
		this.addColumn("CC Number");
		this.addColumn("Exp. date");
		this.addColumn("Type");
		this.addColumn("Balance");
		
		this.setIndexOfAccountNumberColumn(1);
		this.setIndexOfAmountColumn(4);
	}
	
	@Override
	public void addAnItem(IModel model) {
		if(!(model instanceof CreditCard)) return;
		CreditCard item = (CreditCard)model;
		String typeOfCCard = "Gold";
		if(item instanceof SilverCCard) typeOfCCard = "Silver";
		else if(item instanceof BronzeCCard) typeOfCCard = "Bronze";

		this.addAnItem(item.getCustomer().getName(), item.getAccountNumber(),
				item.getExpirationDate().format(DateTimeFormatter.ISO_DATE),
				typeOfCCard, item.getBalance());
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
