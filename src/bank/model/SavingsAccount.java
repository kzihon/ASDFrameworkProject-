package bank.model;

import bank.dao.EntryDAO;
import bank.dao.SavingsAccountDAO;
import framework.interfaces.ICustomer;
import framework.interfaces.IEntry;
import framework.model.Account;

public class SavingsAccount extends Account {
    private int id;
    protected SavingsAccount(String accountNumber, ICustomer customer) {
        super(accountNumber, customer);
        this.setInterestRate(5);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public void makeWithdrawal(double amount, IEntry accountOperation) {
        super.makeWithdrawal(amount, accountOperation);
        new EntryDAO().persist(this.getId(), accountOperation);
    }

    @Override
    public void makeDeposit(double amount, IEntry accountOperation){
        super.makeDeposit(amount, accountOperation);
        new EntryDAO().persist(this.getId(), accountOperation);
    }

    @Override
    public void addInterest() {
        double value = this.getBalance();
        value += (value * this.getInterestRate() / 100);
        this.setBalance(value);
    }

    @Override
    public void save() {
        SavingsAccountDAO dao = new SavingsAccountDAO();
        if(this.getId() == 0) {
            dao.persist(this);
            this.setId(dao.getLastId());
        } else {
            dao.update(this);
        }
        this.notifyViews();
    }

    @Override
    public void delete() {
        SavingsAccountDAO dao = new SavingsAccountDAO();
        dao.delete(this);
        this.notifyViews();
    }
}
