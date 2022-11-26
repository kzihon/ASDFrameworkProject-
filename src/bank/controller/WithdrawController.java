package bank.controller;

import bank.dao.CheckingAccountDAO;
import bank.dao.SavingsAccountDAO;
import bank.model.DepositOperation;
import bank.model.WithdrawOperation;
import bank.rules.*;
import framework.controller.IController;
import framework.interfaces.IAccount;
import framework.rules.RuleEvaluator;
import framework.view.FinancialOperationModalView;
import framework.view.IView;

import javax.swing.*;
import java.time.LocalDate;

public class WithdrawController implements IController {
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
            WithdrawOperation operation = new WithdrawOperation(amountValue, LocalDate.now());

            NegativeWithdrawResultForPersonalAccountRule mainRule = new NegativeWithdrawResultForPersonalAccountRule();
            if(mainRule.isApplicable(account, amountValue)) {
                mainRule.apply(account, amountValue);
                JOptionPane.showMessageDialog(null, "This operation cannot be performed cause of low balance!", "Warning: low balance found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            RuleEvaluator ruleEvaluator = new RuleEvaluator();
            ruleEvaluator.addRule(new HighWithdrawAmountForPersonalAccountRule());
            ruleEvaluator.addRule(new SendEmailToCompanyForWithdrawRule());
            ruleEvaluator.evaluate(account, amountValue);

            account.makeWithdrawal(amountValue, operation);
            account.save();

            JOptionPane.showMessageDialog(null, "A withdraw of " + amountValue + " have successfuly been done from " + account.getCustomer().getName() + "'s account!", "Information: Success", JOptionPane.INFORMATION_MESSAGE);
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
