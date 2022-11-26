package bank;

import bank.controller.*;
import bank.dao.CheckingAccountDAO;
import bank.dao.SavingsAccountDAO;
import bank.model.SimpleAccountFactory;
import bank.view.AccountListView;
import framework.Finco;
import framework.model.Account;
import framework.model.IModel;
import framework.resources.ConfigReader;
import framework.view.*;

import javax.swing.*;
import java.util.*;

public class Bank extends Finco {
    static private Bank instance;
    static private Map<String, String> config = new HashMap<String, String>();

    static public Bank getInstance() {
        if(instance != null) return instance;

        config = ConfigReader.readFincoSettings(CONFIG_FILE); System.out.println(CONFIG_FILE);
        if(config == null) {
            System.out.println("Something goes wrong with the settings file. Please check on it!");
            return null;
        }
        instance = new Bank(config.get("name"), config.get("email"), 0, config.get("street"),
                config.get("city"), config.get("state"), config.get("zip"), config.get("phonenumber"),
                config.get("legalname"), config.get("establishedyear"), config.get("type"));
        return instance;
    }

    static final private String CONFIG_FILE = System.getProperty("user.dir")
            + "/src/bank/resources/config.xml";

    protected Bank(String name, String email, int numberOfEmployees, String street, String city, String state, String zip, String phoneNumber, String legalName, String establishedYear, String type) {
        super(name, email, numberOfEmployees, street, city, state, zip, phoneNumber, legalName, establishedYear, type);
    }

    static public void main(String[] arg) {
        Bank.getInstance().run();
    }

    @Override public void run() {
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

            List<IModel> accountList = new ArrayList<>();
            accountList.addAll(new SavingsAccountDAO().getAll());
            accountList.addAll(new CheckingAccountDAO().getAll());

            Comparator<IModel> comparator = (x, y) -> {
                Account a = (Account) x;
                Account b = (Account) y;
                return a.getAccountNumber().compareTo(b.getAccountNumber());
            };
            accountList.sort(comparator);
            for (IModel item: accountList) {
                accountData.addAnItem(item);
            }

            index.setListView(accountData);

            String addPersonalAccountTitle = "Add personal account";
            ArrayList<RadioButtonTypeAccount> listPersonalAcountTypeOptions = new ArrayList<>();
            listPersonalAcountTypeOptions.add(new RadioButtonTypeAccount("Checkings"));
            listPersonalAcountTypeOptions.add(new RadioButtonTypeAccount("Savings"));

            CreatePersonalAccountModal modalCreatePersonalAccount =
                    new CreatePersonalAccountModal(index, addPersonalAccountTitle, listPersonalAcountTypeOptions);
            modalCreatePersonalAccount.setController(new PersonalAccountController());
            CreationalAction actionCreatePersonalAccount =
                    new CreationalAction(addPersonalAccountTitle, modalCreatePersonalAccount);
            index.addCreationalAction(actionCreatePersonalAccount);

		    String addCompanyAccountTitle = "Add company account";
		    ArrayList<RadioButtonTypeAccount> listCompanyAccountTypeOptions = new ArrayList<>();
		    listCompanyAccountTypeOptions.add(new RadioButtonTypeAccount("Checkings"));
		    listCompanyAccountTypeOptions.add(new RadioButtonTypeAccount("Savings"));

		    CreateCompanyAccountModal modalCreateCompanyAccount =
		    		new CreateCompanyAccountModal(index, "Add company account", listCompanyAccountTypeOptions);
		    modalCreateCompanyAccount.setController(new CompanyAccountController());
		    CreationalAction actionCreateCompanyAccount =
		    		new CreationalAction(addCompanyAccountTitle, modalCreateCompanyAccount);
		    index.addCreationalAction(actionCreateCompanyAccount);

            String depositTitle = "Deposit";
            FinancialOperationModalView depositModal =
                    new FinancialOperationModalView(index, depositTitle, "Acc. No.");
            depositModal.setController(new DepositController());
            FinancialAction depositAction =
                    new FinancialAction(index, depositTitle, depositModal);
            index.addFinancialAction(depositAction);

            String withdrawTitle = "Withdraw";
            FinancialOperationModalView withdrawModal =
                    new FinancialOperationModalView(index, withdrawTitle, "Acc. No.");
            withdrawModal.setController(new WithdrawController());
            FinancialAction withdrawAction =
                    new FinancialAction(index, withdrawTitle, withdrawModal);
            index.addFinancialAction(withdrawAction);

            String interestActionWarning = "You are about to add interest to all accounts.\n";
            interestActionWarning += "Press OK to validate or just cancel if not sure.";
            InterestAction interestAction =
                    new InterestAction(index, "Add interest", interestActionWarning);
            interestAction.setController(new InterestController());
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
