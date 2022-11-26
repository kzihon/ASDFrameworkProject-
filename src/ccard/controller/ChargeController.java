package ccard.controller;

import ccard.dao.BronzeCCardDAO;
import ccard.dao.GoldCCardDAO;
import ccard.dao.SilverCCardDAO;
import ccard.interfaces.ICreditCard;
import ccard.model.DepositOperation;
import framework.controller.IController;
import framework.interfaces.IAccount;
import framework.rules.RuleEvaluator;
import framework.view.FinancialOperationModalView;
import framework.view.IView;

import javax.swing.*;
import java.time.LocalDate;

public class ChargeController implements IController {
    @Override
    public void add(IView viewSender) {
        try {
            FinancialOperationModalView sender = (FinancialOperationModalView) viewSender;

            String ccardNumber = sender.getAccountNumber();
            if(ccardNumber.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Credit-card number cannot be empty!", "Warning: empty value found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ICreditCard ccard = (ICreditCard) GoldCCardDAO.getCCardByNumber(ccardNumber);
            if(ccard == null) {
                ccard = (ICreditCard) SilverCCardDAO.getCCardByNumber(ccardNumber);
            }

            if(ccard == null) {
                ccard = (ICreditCard) BronzeCCardDAO.getCCardByNumber(ccardNumber);
            }

            if( ccard == null) {
                JOptionPane.showMessageDialog(null, "We didn't find any credit-card related to this credit-card number!", "Warning: duplicate value found", JOptionPane.WARNING_MESSAGE);
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

            ccard.attachView(viewSender);
            sender.setModel(ccard);
            DepositOperation operation = new DepositOperation(amountValue, LocalDate.now());

            /*RuleEvaluator ruleEvaluator = new RuleEvaluator();
            ruleEvaluator.addRule(new HighDepositAmountForPersonalAccountRule());
            ruleEvaluator.addRule(new SendEmailToCompanyForDepositRule());
            ruleEvaluator.evaluate(ccard, amountValue);*/

            ccard.makeDeposit(amountValue, operation);
            ccard.save();

            JOptionPane.showMessageDialog(null, "A charge of " + amountValue + " have successfuly been done for " + ccard.getCustomer().getName() + "'s credit-card!", "Information: Success", JOptionPane.INFORMATION_MESSAGE);
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
