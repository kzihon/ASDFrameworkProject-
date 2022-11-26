package ccard.model;

import ccard.dao.SilverCCardDAO;
import framework.interfaces.ICustomer;
import framework.model.Account;

import java.time.LocalDate;

public class SilverCCard extends CreditCard {
    protected SilverCCard(CCAccount account, String ccard_number, LocalDate expirationDate) {
        super(account, ccard_number, expirationDate);
    }

    @Override
    public void save() {
        SilverCCardDAO dao = new SilverCCardDAO();
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
        SilverCCardDAO dao = new SilverCCardDAO();
        dao.delete(this);
        this.notifyViews();
    }
}
