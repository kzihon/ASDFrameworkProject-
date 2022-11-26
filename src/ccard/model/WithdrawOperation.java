package ccard.model;

import framework.model.Entry;

import java.time.LocalDate;

public class WithdrawOperation extends Entry {
    public WithdrawOperation(double amount, LocalDate date) {
        super(amount, date);
    }

    @Override
    public void save() {

    }

    @Override
    public void delete() {

    }
}
