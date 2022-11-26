package framework.model;

import java.time.LocalDate;

public class DefaultEntry extends Entry {

    public DefaultEntry(double amount, LocalDate date) {
        super(amount, date);
    }

	@Override
	public void save() {
		// TODO Auto-generated method stub
        this.notifyViews();
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

}
