package ccard.interfaces;

import ccard.model.CCAccount;
import framework.interfaces.IAccount;

import java.time.LocalDate;

public interface ICreditCard extends IAccount {
    int getId();
    String getCcNumber();
    void setId(int id);
    double getLastMonthBalance();
    void setLastMonthBalance(double value);
    LocalDate getExpirationDate();
    CCAccount getCCAccount();
}
