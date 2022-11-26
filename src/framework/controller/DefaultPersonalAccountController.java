package framework.controller;

import framework.dao.DefaultAccountDAO;
import framework.dao.DefaultPersonDAO;
import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;
import framework.model.AccountFactory;
import framework.model.DefaultCustomerFactory;
import framework.model.DefaultSimpleAccountFactory;
import framework.view.CreatePersonalAccountModal;
import framework.view.IView;

import javax.swing.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultPersonalAccountController implements IController {
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public void add(IView viewSender) {
		try {
			CreatePersonalAccountModal sender = (CreatePersonalAccountModal)viewSender;
			
			String accountType = sender.getSelectedType();
			
			String accountNumber = sender.getAccountNumber();
			if(accountNumber.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Account number cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
				return;
			}			
			
			String customerName = sender.getName();
			if(customerName.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Customer name cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
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
			
			String birthdate = sender.getBirthDate();
			LocalDate birthdateValue = LocalDate.now();
			if(birthdate.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Birth date cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
				return;
			} else {
				try {
					birthdateValue = LocalDate.parse(birthdate);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Wrong value provided for birth date!", "Warning: wrong value found", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			
			String email = sender.getEmail();
			if(email.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Birth date cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
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
			
			if(DefaultPersonDAO.getByEmail(email) != null) {
				JOptionPane.showMessageDialog(null, "This email address is already used by another customer!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(DefaultAccountDAO.getAccountByAccountNumber(accountNumber) != null) {
				JOptionPane.showMessageDialog(null, "This account number is already used by another account!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			DefaultCustomerFactory customerFactory = new DefaultCustomerFactory();
			ICustomer customer = customerFactory.createPerson(customerName, birthdateValue, street, city, state, zip, email);
			
			AccountFactory factory = DefaultSimpleAccountFactory.getCheckingAccountFactory();
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
