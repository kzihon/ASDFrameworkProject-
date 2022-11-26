package ccard.model;

import ccard.dao.EntryDAO;
import ccard.interfaces.ICreditCard;
import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;
import framework.interfaces.IEntry;
import framework.model.Account;
import framework.view.IView;

import java.time.LocalDate;
import java.util.List;

abstract public class CreditCard implements ICreditCard, IAccount {
    private int id;
    private CCAccount account;

    final public String getCcNumber() {
        return ccNumber;
    }

    private String ccNumber;

    @Override
    final public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    final public double getLastMonthBalance() {
        return lastMonthBalance;
    }

    @Override
    final public void setLastMonthBalance(double value) {
        this.lastMonthBalance = value;
    }

    private LocalDate expirationDate;
    private double lastMonthBalance;

    protected CreditCard(CCAccount account, String creditCardNumber, LocalDate expirationDate) {
        this.account = account;
        this.ccNumber = creditCardNumber;
        this.lastMonthBalance = 0;
        this.expirationDate = expirationDate;
    }

    @Override
    final public int getId() { return id; }

    @Override
    final public void setId(int id) { this.id = id; }

    @Override
    final public double getBalance() {
        return this.account.getBalance();
    }

    @Override
    final public void makeWithdrawal(double amount, IEntry accountOperation) {
        this.account.makeWithdrawal(amount, accountOperation);
        new EntryDAO().persist(this.getId(), accountOperation);
    }

    @Override
    final public void makeDeposit(double amount, IEntry accountOperation){
        this.account.makeDeposit(amount, accountOperation);
        new EntryDAO().persist(this.getId(), accountOperation);
    }

    @Override
    final public ICustomer getCustomer() {
        return this.account.getCustomer();
    }

    @Override
    final public void addInterest() {
        this.account.addInterest();
    }

    @Override
    final public String getAccountNumber() {
        return this.account.getAccountNumber();
    }

    @Override
    final public double getInterestRate() {
        return this.account.getInterestRate();
    }

    @Override
    final public CCAccount getCCAccount() {
        return this.account;
    }

    @Override
    public List<IEntry> getOperations() {
        return this.account.getOperations();
    }

    @Override
    final public void attachView(IView view) {
        this.account.attachView(view);
    }

    @Override
    final public void detachView(IView view) {
        this.account.detachView(view);
    }

    @Override
    final public void notifyViews() {
        this.account.notifyViews();
    }
}
