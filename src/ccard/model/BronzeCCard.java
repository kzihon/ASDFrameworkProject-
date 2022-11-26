package ccard.model;

import ccard.dao.BronzeCCardDAO;
import framework.model.Account;

import java.time.LocalDate;

public class BronzeCCard extends CreditCard {
    protected BronzeCCard(CCAccount account, String ccard_number, LocalDate expirationDate) {
        super(account, ccard_number, expirationDate);
    }

    @Override
    public void save() {
        BronzeCCardDAO dao = new BronzeCCardDAO();
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
        BronzeCCardDAO dao = new BronzeCCardDAO();
        dao.delete(this);
        this.notifyViews();
    }
}
