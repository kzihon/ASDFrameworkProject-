package ccard.view;

import framework.view.CreationalOperationModalView;
import framework.view.MainFrame;
import framework.view.RadioButtonTypeAccount;

import javax.swing.*;
import java.util.List;

public class CreateCCAccountModal extends CreationalOperationModalView {
	private static final long serialVersionUID = -3625552525711195308L;

	private JLabel JLabelName = new JLabel();
	private JLabel JLabelStreet = new JLabel();
	private JLabel JLabelCity = new JLabel();
	private JLabel JLabelState = new JLabel();
	private JLabel JLabelZip = new JLabel();
	private JLabel JLabelEmail = new JLabel();
	private JLabel JLabelCCardNumber = new JLabel();

	private JTextField JTextField_NAME = new JTextField();
	private JTextField JTextField_CITY = new JTextField();
	private JTextField JTextField_STATE = new JTextField();
	private JTextField JTextField_STREET = new JTextField();
	private JTextField JTextField_ZIP = new JTextField();
	private JTextField JTextField_EMAIL = new JTextField();
	private JTextField JTextField_ACCOUNTNUMBER = new JTextField();

	protected JLabel JLabelExpDate = new JLabel();
	protected JTextField JTextField_EXPIRATIONDATE = new JTextField();

	public CreateCCAccountModal(MainFrame parent, String operationTitle, List<RadioButtonTypeAccount> listOfRadiosTypeAccount) {
		super(parent, operationTitle);
		
		for(RadioButtonTypeAccount item: listOfRadiosTypeAccount) {
			this.addRadioTypeAccount(item);
		}
		
		JLabelCCardNumber.setText("CC Number");
		getContentPane().add(JLabelCCardNumber);
		JLabelCCardNumber.setForeground(java.awt.Color.black);
		JLabelCCardNumber.setBounds(12,115,48,24);

		getContentPane().add(JTextField_ACCOUNTNUMBER);
		JTextField_ACCOUNTNUMBER.setBounds(84,115,156,20);
		
		JLabelName.setText("Name");
		getContentPane().add(JLabelName);
		JLabelName.setForeground(java.awt.Color.black);
		JLabelName.setBounds(12,152,48,24);
		
		getContentPane().add(JTextField_NAME);		
		JTextField_NAME.setBounds(84,152,156,20);
		
		JLabelStreet.setText("Street");
		getContentPane().add(JLabelStreet);
		JLabelStreet.setForeground(java.awt.Color.black);
		JLabelStreet.setBounds(12,176,48,24);

		getContentPane().add(JTextField_STREET);
		JTextField_STREET.setBounds(84,176,156,20);
		
		JLabelCity.setText("City");
		getContentPane().add(JLabelCity);
		JLabelCity.setForeground(java.awt.Color.black);
		JLabelCity.setBounds(12,200,48,24);
		
		getContentPane().add(JTextField_CITY);		
		JTextField_CITY.setBounds(84,200,156,20);
		
		JLabelState.setText("State");
		getContentPane().add(JLabelState);
		JLabelState.setForeground(java.awt.Color.black);
		JLabelState.setBounds(12,224,48,24);

		getContentPane().add(JTextField_STATE);
		JTextField_STATE.setBounds(84,224,156,20);
		
		JLabelZip.setText("Zip");
		getContentPane().add(JLabelZip);
		JLabelZip.setForeground(java.awt.Color.black);
		JLabelZip.setBounds(12,248,48,24);

		getContentPane().add(JTextField_ZIP);
		JTextField_ZIP.setBounds(84,248,156,20);
		
		JLabelExpDate.setText("Exp. date");
		getContentPane().add(JLabelExpDate);
		JLabelExpDate.setForeground(java.awt.Color.black);
		JLabelExpDate.setBounds(12,272,96,24);

		getContentPane().add(JTextField_EXPIRATIONDATE);
		JTextField_EXPIRATIONDATE.setBounds(84,272,156,20);
		
		JLabelEmail.setText("Email");
		getContentPane().add(JLabelEmail);
		JLabelEmail.setForeground(java.awt.Color.black);
		JLabelEmail.setBounds(12,296,48,24);
		
		getContentPane().add(JTextField_EMAIL);
		JTextField_EMAIL.setBounds(84,296,156,20);
	}
	
	public String getAccountNumber() {
		return JTextField_ACCOUNTNUMBER.getText().trim();
	}
	
	public String getName() {
		return JTextField_NAME.getText().trim();
	}
	
	public String getStreet() {
		return JTextField_STREET.getText().trim();
	}
	
	public String getCity() {
		return JTextField_CITY.getText().trim();
	}
	
	public String getState() {
		return JTextField_STATE.getText().trim();
	}
	
	public String getZip() {
		return JTextField_ZIP.getText().trim();
	}
	
	public String getEmail() {
		return JTextField_EMAIL.getText().trim();
	}

	public String getExpirationDate() {
		return JTextField_EXPIRATIONDATE.getText().trim();
	}
	
	@Override
	final protected void refresh() {
		JTextField_NAME.setText("");
		JTextField_CITY.setText("");
		JTextField_STATE.setText("");
		JTextField_STREET.setText("");
		JTextField_ZIP.setText("");
		JTextField_EMAIL.setText("");
		JTextField_ACCOUNTNUMBER.setText("");
		JTextField_EXPIRATIONDATE.setText("");
	}
}
