package ccard.model;

import ccard.dao.GoldCCardDAO;
import framework.interfaces.ICustomer;
import framework.model.Account;

import java.time.LocalDate;

public class GoldCCard extends CreditCard {
    protected GoldCCard(CCAccount account, String ccard_number, LocalDate expirationDate) {
        super(account, ccard_number, expirationDate);
    }

    @Override
    public void save() {
        GoldCCardDAO dao = new GoldCCardDAO();
        if(this.getId() == 0) {
            dao.persist(this);
            this.setId(dao.getLastId());
        } else {
            dao.update(this);
        }
        this.getCCAccount().save();
        this.notifyViews();
    }

    @Override
    public void delete() {
        GoldCCardDAO dao = new GoldCCardDAO();
        dao.delete(this);
        this.notifyViews();
    }
}
