package framework.model;

import framework.interfaces.IAccount;
import framework.interfaces.ICustomer;
import framework.view.IView;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer implements ICustomer {
    private String name;
    private String email;
    private List<IAccount> accounts;
    private Address address;

    List<IView> observers;

    protected Customer(String name, String email, String street, String city, String state, String zip) {
        Address address= new Address(street, city, state, zip);
        this.name = name;
        this.email = email;
        this.address = address;
        this.accounts= new ArrayList<>();
        this.observers = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;

    }

    public List<IAccount> getAccounts() {
        return accounts;
    }

    public void addAccount(Account accounts) {
        this.accounts.add(accounts);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    @Override
    public void notifyViews() {
        for(IView observer: this.observers) observer.update();
    }

    @Override
    public void attachView(IView view) {
        this.observers.add(view);
    }

    @Override
    public void detachView(IView view) {
        this.observers.remove(view);
    }
    
    public  abstract void sendEmail(String message);
}
