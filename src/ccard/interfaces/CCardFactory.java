package ccard.interfaces;

import ccard.interfaces.ICreditCard;
import ccard.model.CCAccount;
import framework.model.Account;

import java.time.LocalDate;

public interface CCardFactory {
    ICreditCard createCCard(CCAccount account, String ccard_number, LocalDate expirationDate);
}
