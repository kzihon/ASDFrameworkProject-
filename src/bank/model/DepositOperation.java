package bank.model;

import framework.model.Entry;

import java.time.LocalDate;

public class DepositOperation extends Entry {

    public DepositOperation(double amount, LocalDate date) {
        super(amount, date);
    }

    @Override
    public void save() {

    }

    @Override
    public void delete() {

    }
}
