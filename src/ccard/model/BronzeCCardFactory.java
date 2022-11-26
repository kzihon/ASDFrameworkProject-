package ccard.model;

import ccard.interfaces.CCardFactory;
import ccard.interfaces.ICreditCard;
import framework.model.Account;

import java.time.LocalDate;

public class BronzeCCardFactory implements CCardFactory {
    @Override
    public ICreditCard createCCard(CCAccount account, String ccard_number, LocalDate expirationDate) {
        return new BronzeCCard(account, ccard_number, expirationDate);
    }
}
