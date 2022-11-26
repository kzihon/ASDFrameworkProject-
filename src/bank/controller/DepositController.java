package bank.controller;

import bank.dao.CheckingAccountDAO;
import bank.dao.EntryDAO;
import bank.dao.SavingsAccountDAO;
import bank.model.DepositOperation;
import bank.rules.HighDepositAmountForPersonalAccountRule;
import bank.rules.SendEmailToCompanyForDepositRule;
import framework.controller.IController;
import framework.interfaces.IAccount;
import framework.model.IModel;
import framework.rules.RuleEvaluator;
import framework.view.FinancialOperationModalView;
import framework.view.IView;

import javax.swing.*;
import java.time.LocalDate;

public class DepositController implements IController {
    @Override
    public void add(IView viewSender) {
        try {
            FinancialOperationModalView sender = (FinancialOperationModalView) viewSender;

            String accountNumber = sender.getAccountNumber();
            if(accountNumber.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Account number cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            IAccount account = (IAccount)CheckingAccountDAO.getAccountByAccountNumber(accountNumber);
            if(account == null) {
                account = (IAccount)SavingsAccountDAO.getAccountByAccountNumber(accountNumber);
            }

            if( account == null) {
                JOptionPane.showMessageDialog(null, "We didn't find any account related to this account number!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String amount = sender.getAmount();
            double amountValue = 0.0;
            if(amount.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Amount cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                try {
                    amountValue = Double.parseDouble(amount);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Wrong value provided for amount!", "Warning: wrong value found", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            account.attachView(viewSender);
            sender.setModel(account);
            DepositOperation operation = new DepositOperation(amountValue, LocalDate.now());

            RuleEvaluator ruleEvaluator = new RuleEvaluator();
            ruleEvaluator.addRule(new HighDepositAmountForPersonalAccountRule());
            ruleEvaluator.addRule(new SendEmailToCompanyForDepositRule());
            ruleEvaluator.evaluate(account, amountValue);

            account.makeDeposit(amountValue, operation);
            account.save();

            JOptionPane.showMessageDialog(null, "A deposit of " + amountValue + " have successfuly been done for " + account.getCustomer().getName() + "'s account!", "Information: Success", JOptionPane.INFORMATION_MESSAGE);
            sender.dispose();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Something goes wrong, please try again later!", "Warning: internal error", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void update(IView viewSender) {

    }

    @Override
    public void get(IView viewSender) {

    }

    @Override
    public void delete(IView viewSender) {

    }
}
