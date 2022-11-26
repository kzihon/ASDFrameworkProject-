package ccard;

import ccard.controller.CCAccountController;
import ccard.controller.ChargeController;
import ccard.controller.DepositController;
import ccard.dao.BronzeCCardDAO;
import ccard.dao.GoldCCardDAO;
import ccard.dao.SilverCCardDAO;
import ccard.model.CCAccount;
import ccard.model.CreditCard;
import ccard.view.CCardListView;
import ccard.view.CreateCCAccountModal;
import framework.Finco;
import framework.model.Account;
import framework.model.IModel;
import framework.resources.ConfigReader;
import framework.view.*;

import javax.swing.*;
import java.util.*;

public class CCard extends Finco {
    static private CCard instance;
    static private Map<String, String> config = new HashMap<String, String>();

    static public CCard getInstance() {
        if(instance != null) return instance;

        config = ConfigReader.readFincoSettings(CONFIG_FILE);
        if(config == null) {
            System.out.println("Something goes wrong with the settings file. Please check on it!");
            return null;
        }
        instance = new CCard(config.get("name"), config.get("email"), 0, config.get("street"),
                config.get("city"), config.get("state"), config.get("zip"), config.get("phonenumber"),
                config.get("legalname"), config.get("establishedyear"), config.get("type"));
        return instance;
    }

    static final private String CONFIG_FILE = System.getProperty("user.dir")
            + "/src/ccard/resources/config.xml";
    protected CCard(String name, String email, int numberOfEmployees, String street, String city, String state, String zip, String phoneNumber, String legalName, String establishedYear, String type) {
        super(name, email, numberOfEmployees, street, city, state, zip, phoneNumber, legalName, establishedYear, type);
    }

    static public void main(String[] arg) {
        CCard.getInstance().run();
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
            CCardListView accountData = new CCardListView();

            List<IModel> accountList = new ArrayList<>();
            accountList.addAll(new SilverCCardDAO().getAll());
            accountList.addAll(new BronzeCCardDAO().getAll());
            accountList.addAll(new GoldCCardDAO().getAll());

            Comparator<IModel> comparator = (x, y) -> {
                CreditCard a = (CreditCard) x;
                CreditCard b = (CreditCard) y;
                return a.getAccountNumber().compareTo(b.getAccountNumber());
            };
            accountList.sort(comparator);
            for (IModel item: accountList) {
                accountData.addAnItem(item);
            }

            index.setListView(accountData);

            String addPersonalAccountTitle = "Add Credit-card account";
            ArrayList<RadioButtonTypeAccount> listPersonalAcountTypeOptions = new ArrayList<>();
            listPersonalAcountTypeOptions.add(new RadioButtonTypeAccount("Gold"));
            listPersonalAcountTypeOptions.add(new RadioButtonTypeAccount("Silver"));
            listPersonalAcountTypeOptions.add(new RadioButtonTypeAccount("Bronze"));

            CreateCCAccountModal modalCreatePersonalAccount =
                    new CreateCCAccountModal(index, addPersonalAccountTitle, listPersonalAcountTypeOptions);
            modalCreatePersonalAccount.setController(new CCAccountController());
            CreationalAction actionCreatePersonalAccount =
                    new CreationalAction(addPersonalAccountTitle, modalCreatePersonalAccount);
            index.addCreationalAction(actionCreatePersonalAccount);

            /*String addCompanyAccountTitle = "Add company account";
            ArrayList<RadioButtonTypeAccount> listCompanyAccountTypeOptions = new ArrayList<>();
            listCompanyAccountTypeOptions.add(new RadioButtonTypeAccount("Checkings"));
            listCompanyAccountTypeOptions.add(new RadioButtonTypeAccount("Savings"));

            CreateCompanyAccountModal modalCreateCompanyAccount =
                    new CreateCompanyAccountModal(index, "Add company account", listCompanyAccountTypeOptions);
            modalCreateCompanyAccount.setController(new CompanyAccountController());
            CreationalAction actionCreateCompanyAccount =
                    new CreationalAction(addCompanyAccountTitle, modalCreateCompanyAccount);
            index.addCreationalAction(actionCreateCompanyAccount);*/

            String depositTitle = "Deposit";
            FinancialOperationModalView depositModal =
                    new FinancialOperationModalView(index, depositTitle, "CC. No.");
            depositModal.setController(new DepositController());
            FinancialAction depositAction =
                    new FinancialAction(index, depositTitle, depositModal);
            index.addFinancialAction(depositAction);

            String withdrawTitle = "Charge";
            FinancialOperationModalView withdrawModal =
                    new FinancialOperationModalView(index, withdrawTitle, "CC. No.");
            withdrawModal.setController(new ChargeController());
            FinancialAction withdrawAction =
                    new FinancialAction(index, withdrawTitle, withdrawModal);
            index.addFinancialAction(withdrawAction);

            /*String interestActionWarning = "You are about to add interest to all accounts.\n";
            interestActionWarning += "Press OK to validate or just cancel if not sure.";
            InterestAction interestAction =
                    new InterestAction(index, "Add interest", interestActionWarning);
            interestAction.setController(new InterestController());
            index.addInterestAction(interestAction);*/

            index.setVisible(true);
        }
        catch (Throwable t) {
            t.printStackTrace();
            //Ensure the application exits with an error condition.
            System.exit(1);
        }
    }
}
