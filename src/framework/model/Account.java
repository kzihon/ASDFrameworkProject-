package framework.model;

import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;
import framework.interfaces.IEntry;
import framework.view.IView;

import java.util.ArrayList;
import java.util.List;

public abstract class Account implements IAccount {
	final private List<IView> observers;
    private String accountNumber;
    private double balance;
    private double interestRate;
    private List<IEntry> accountOperations;
    private ICustomer customer;

    protected Account(String accountNumber, ICustomer customer) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance= 0;
        this.accountOperations= new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void makeWithdrawal(double amount, IEntry accountOperation) {
        this.reduceBalanceForWithdrawal(amount);
        this.accountOperations.add(accountOperation);
    }
    
    protected void reduceBalanceForWithdrawal(double amount){
        this.balance-=amount;
    }

    public void makeDeposit(double amount, IEntry accountOperation){
        this.increaseBalanceForDeposit(amount);
        this.accountOperations.add(accountOperation);
    }
    
    protected void increaseBalanceForDeposit(double amount){
        this.balance+=amount;
    }
    
    public abstract void addInterest();
    
    final public double getBalance(){
        return balance;
    }

    final public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public final List<IEntry> getOperations(){
        return this.accountOperations;
    }
    
    public final void addOperation(IEntry operation){
        accountOperations.add(operation);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public final double getInterestRate() {
        return interestRate;
    }

    public final void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public ICustomer getCustomer() {
        return customer;
    }
    
    @Override final public void notifyViews() {
		for(IView observer: this.observers) observer.update();
	}

	@Override final public void attachView(IView view) {
		this.observers.add(view);		
	}

	@Override final public void detachView(IView view) {
		this.observers.remove(view);
	}
}
