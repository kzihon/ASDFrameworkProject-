package framework;

import framework.controller.DefaultPersonalAccountController;
import framework.model.DefaultCompany;
import framework.resources.ConfigReader;
import framework.view.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Finco extends DefaultCompany {
	
	static private Finco instance;
	static private Map<String, String> config = new HashMap<String, String>();
	
	static public Finco getInstance() {
		if(instance != null) return instance;
		
		config = ConfigReader.readFincoSettings(CONFIG_FILE);
		if(config == null) {
			System.out.println("Something goes wrong with the settings file. Please check on it!");
			return null;
		}
		instance = new Finco(config.get("name"), config.get("email"), 0, config.get("street"),
				config.get("city"), config.get("state"), config.get("zip"), config.get("phonenumber"),
				config.get("legalname"), config.get("establishedyear"), config.get("type"));
		return instance;
	}

	static final private String CONFIG_FILE = System.getProperty("user.dir")
			+ "/src/framework/resources/config.xml";
	
	private String phoneNumber, legalName, establishedYear, type;
	
	protected Finco(String name, String email, int numberOfEmployees, String street, String city, String state,
			String zip, String phoneNumber, String legalName, String establishedYear, String type) {
		
		super(name, email, numberOfEmployees, street, city, state, zip);
		this.phoneNumber = phoneNumber;
		this.legalName = legalName;
		this.establishedYear = establishedYear;
		this.type = type;
	}
	
	
	static public void main(String[] arg) {
		Finco.getInstance().run();
	}
	
	public void run() {
		try {
		    // Add the following code if you want the Look and Feel
		    // to be set to the Look and Feel of the native system.
		    
		    try {
		        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    } 
		    catch (Exception e) { }
		    
			//Create a new instance of our application's frame, and make it visible.
		    MainFrame index = new MainFrame(this.getName());
		    AccountListView accountData = new AccountListView();
		    index.setListView(accountData);
		    
		    String addPersonalAccountTitle = "Add personal account";
		    ArrayList<RadioButtonTypeAccount> listPersonalAcountTypeOptions = new ArrayList<>();
		    listPersonalAcountTypeOptions.add(new RadioButtonTypeAccount("Checkings"));
		    //listPersonalAcountTypeOptions.add(new RadioButtonTypeAccount("Savings"));
		    CreatePersonalAccountModal modalCreatePersonalAccount = 
		    		new CreatePersonalAccountModal(index, addPersonalAccountTitle, listPersonalAcountTypeOptions);
		    modalCreatePersonalAccount.setController(new DefaultPersonalAccountController());
		    CreationalAction actionCreatePersonalAccount = 
		    		new CreationalAction(addPersonalAccountTitle, modalCreatePersonalAccount);
		    index.addCreationalAction(actionCreatePersonalAccount);
		    
		    /*String addCompanyAccountTitle = "Add company account";
		    ArrayList<RadioButtonTypeAccount> listCompanyAccountTypeOptions = new ArrayList<>();
		    listCompanyAccountTypeOptions.add(new RadioButtonTypeAccount("Checkings"));
		    listCompanyAccountTypeOptions.add(new RadioButtonTypeAccount("Savings"));
		    CreateCompanyAccountModal modalCreateCompanyAccount = 
		    		new CreateCompanyAccountModal(index, "Add company account", listCompanyAccountTypeOptions);
		    modalCreateCompanyAccount.setController(null); // To be added
		    CreationalAction actionCreateCompanyAccount = 
		    		new CreationalAction(addCompanyAccountTitle, modalCreateCompanyAccount);
		    index.addCreationalAction(actionCreateCompanyAccount);*/
		    
		    String depositTitle = "Deposit";
		    FinancialOperationModalView depositModal = 
		    		new FinancialOperationModalView(index, depositTitle, "Acc. No.");
		    depositModal.setController(null); // To be added
		    FinancialAction depositAction = 
		    		new FinancialAction(index, depositTitle, depositModal);
		    index.addFinancialAction(depositAction);
		    
		    String withdrawTitle = "Withdraw";
		    FinancialOperationModalView withdrawModal = 
		    		new FinancialOperationModalView(index, withdrawTitle, "Acc. No.");
		    withdrawModal.setController(null); // To be added
		    FinancialAction withdrawAction = 
		    		new FinancialAction(index, withdrawTitle, withdrawModal);
		    index.addFinancialAction(withdrawAction);
		    
		    String interestActionWarning = "You are about to add interest to all accounts.\n";
		    interestActionWarning += "Press OK to validate or just cancel if not sure.";
		    InterestAction interestAction = 
		    		new InterestAction(index, "Add interest", interestActionWarning);
		    interestAction.setController(null); // To be added
		    index.addInterestAction(interestAction);
		    
		    index.setVisible(true);
		} 
		catch (Throwable t) {
			t.printStackTrace();
			//Ensure the application exits with an error condition.
			System.exit(1);
		}
	}
}
