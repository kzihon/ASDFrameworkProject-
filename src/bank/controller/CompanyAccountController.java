package bank.controller;

import bank.dao.CheckingAccountDAO;
import bank.dao.CompanyDAO;
import bank.dao.PersonDAO;
import bank.dao.SavingsAccountDAO;
import bank.model.Company;
import bank.model.CustomerFactory;
import bank.model.SimpleAccountFactory;
import framework.controller.IController;
import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;
import framework.model.AccountFactory;
import framework.model.IModel;
import framework.view.CreateCompanyAccountModal;
import framework.view.IView;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompanyAccountController implements IController {
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private IModel model;

	@Override
	public void add(IView viewSender) {
		try {
			CreateCompanyAccountModal sender = (CreateCompanyAccountModal)viewSender;

			String accountType = sender.getSelectedType();

			String accountNumber = sender.getAccountNumber();
			if(accountNumber.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Account number cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if(CheckingAccountDAO.getAccountByAccountNumber(accountNumber) != null) {
				JOptionPane.showMessageDialog(null, "This account number is already used by another checking account!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if(SavingsAccountDAO.getAccountByAccountNumber(accountNumber) != null) {
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

			ICustomer customer = (Company)CompanyDAO.getCompanyByEmail(email);

			if(oldCustomer) {
				if(customer == null) {
					JOptionPane.showMessageDialog(null, "We didn't find any company related to this email address. Please provide a name if a new customer!", "Warning: customer not found", JOptionPane.WARNING_MESSAGE);
					return;
				}
			} else {
				if( customer != null) {
					JOptionPane.showMessageDialog(null, "This email address is already used by another company. Please provide don't insert name if not a new customer!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if(PersonDAO.getPersonByEmail(email) != null) {
					JOptionPane.showMessageDialog(null, "This email address is already used by another customer!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
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

				String numberOfEmployees = sender.getNumberOfEmployees();
				int numberOfEmployeesValue = 0;
				if(numberOfEmployees.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Number of employees cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
					return;
				} else {
					try {
						numberOfEmployeesValue = Integer.parseInt(numberOfEmployees);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Wrong value provided for number of employees!", "Warning: wrong value found", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}

				CustomerFactory customerFactory = new CustomerFactory();
				customer = customerFactory.createCompany(customerName, numberOfEmployeesValue, street, city, state, zip, email);
			}

			AccountFactory factory = SimpleAccountFactory.getCheckingAccountFactory();
			if(accountType.toLowerCase().equals("savings")) {
				factory = SimpleAccountFactory.getSavingsAccountFactory();
			}

			IAccount account = factory.createAccount(customer, accountNumber);
			account.attachView(viewSender);
			sender.setModel(account);
			account.save();

			JOptionPane.showMessageDialog(null, "A " + accountType + " account was successfuly created for " + customerName + "!", "Information: Success", JOptionPane.INFORMATION_MESSAGE);
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
