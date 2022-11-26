package ccard.controller;

import bank.dao.AccountTool;
import framework.controller.IController;
import framework.view.IView;

import javax.swing.*;

public class InterestController implements IController {
    @Override
    public void add(IView viewSender) {
        try {
            AccountTool.addInterest();

            JOptionPane.showMessageDialog(null, "You have successfuly added interest to all the accounts!", "Information: Success", JOptionPane.INFORMATION_MESSAGE);
            viewSender.update();
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
