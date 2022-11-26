package ccard.controller;

import ccard.dao.*;
import ccard.interfaces.CCardFactory;
import ccard.interfaces.ICreditCard;
import ccard.model.*;
import ccard.view.CreateCCAccountModal;
import framework.controller.IController;
import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;
import framework.model.AccountFactory;
import framework.model.IModel;
import framework.view.CreatePersonalAccountModal;
import framework.view.IView;

import javax.swing.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CCAccountController implements IController {
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private IModel model;

	@Override
	public void add(IView viewSender) {
		try {
			CreateCCAccountModal sender = (CreateCCAccountModal)viewSender;

			String accountType = sender.getSelectedType();

			String accountNumber = sender.getAccountNumber();
			if(accountNumber.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Credit-card number cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if(GoldCCardDAO.getCCardByNumber(accountNumber) != null) {
				JOptionPane.showMessageDialog(null, "This credit-card number is already used by another credit card!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if(SilverCCardDAO.getCCardByNumber(accountNumber) != null) {
				JOptionPane.showMessageDialog(null, "This account number is already used by another saving account!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if(BronzeCCardDAO.getCCardByNumber(accountNumber) != null) {
				JOptionPane.showMessageDialog(null, "This account number is already used by another saving account!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
				return;
			}

			boolean oldCustomer = true;
			String email = sender.getEmail();
			if(email.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Email address cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
				return;
			} else {
				email = email.toLowerCase();
				Pattern pattern = Pattern.compile(EMAIL_PATTERN);
				Matcher matcher = pattern.matcher(email);
				if(!matcher.matches()){
					JOptionPane.showMessageDialog(null, "Wrong value provided for email address!", "Warning: wrong value found", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

			String customerName = sender.getName();
			if(!customerName.isEmpty()) oldCustomer = false;

			ICustomer customer = (Person) PersonDAO.getPersonByEmail(email);
			CCAccount account = null;

			if(oldCustomer) {
				if(customer == null) {
					JOptionPane.showMessageDialog(null, "We didn't find any person related to this email address. Please provide a name if a new customer!", "Warning: customer not found", JOptionPane.WARNING_MESSAGE);
					return;
				}
				account = (CCAccount) CCAccountDAO.getAccountByPerson(customer);
			} else {
				if( customer != null) {
					JOptionPane.showMessageDialog(null, "This email address is already used by another customer. Please provide don't insert name if not a new customer!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
					return;
				}

				String street = sender.getStreet();
				if(street.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Street name cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
					return;
				}

				String city = sender.getCity();
				if(city.isEmpty()) {
					JOptionPane.showMessageDialog(null, "City name cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
					return;
				}

				String state = sender.getState();
				if(state.isEmpty()) {
					JOptionPane.showMessageDialog(null, "City name cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
					return;
				}

				String zip = sender.getZip();
				if(zip.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Zip code cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
					return;
				} else {
					try {
						Integer.parseInt(zip);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Wrong value provided for zip code!", "Warning: wrong value found", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}

				SimpleCustomerFactory customerFactory = new SimpleCustomerFactory();
				customer = customerFactory.createPerson(customerName, LocalDate.now(), street, city, state, zip, email);

				AccountFactory accountFactory = new SimpleAccountFactory();
				account = (CCAccount) accountFactory.createAccount(customer, accountNumber);
			}

			String expdate = sender.getExpirationDate();
			LocalDate expdateValue = LocalDate.now();
			if(expdate.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Expiration date cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
				return;
			} else {
				try {
					expdateValue = LocalDate.parse(expdate);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Wrong value provided for expiration date!", "Warning: wrong value found", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

			CCardFactory factory = new GoldCCardFactory();
			if(accountType.toLowerCase().equals("silver")) {
				factory = new SilverCCardFactory();
			} else if(accountType.toLowerCase().equals("bronze")) {
				factory = new BronzeCCardFactory();
			}
			ICreditCard ccard = factory.createCCard(account, accountNumber, expdateValue);

			ccard.attachView(viewSender);
			sender.setModel(ccard);
			ccard.save();

			JOptionPane.showMessageDialog(null, "A " + accountType + " credit card account was successfuly created for " + customerName + "!", "Information: Success", JOptionPane.INFORMATION_MESSAGE);
			sender.dispose();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "Something goes wrong, please try again later!", "Warning: internal error", JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void update(IView viewSender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void get(IView viewSender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(IView viewSender) {
		// TODO Auto-generated method stub
		
	}

}
