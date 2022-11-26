package ccard.model;

import framework.model.Entry;

import java.time.LocalDate;

public class InterestOperation extends Entry {
    public InterestOperation(double amount, LocalDate date) {
        super(amount, date);
    }

    @Override
    public void save() {

    }

    @Override
    public void delete() {

    }
}
