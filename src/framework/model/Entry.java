package framework.model;

import framework.interfaces.IEntry;
import framework.view.IView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Entry implements IEntry {
	final private List<IView> observers;
    private double amount;
    private LocalDate date;

    public Entry(double amount, LocalDate date) {
        this.amount = amount;
        this.date = date;
        this.observers = new ArrayList<>();
    }
    
    public double getAmount() {
		return amount;
	}

	public LocalDate getDate() {
		return date;
	}
    
    @Override final public void notifyViews() {
		for(IView observer: this.observers) observer.update();
	}

	@Override final public void attachView(IView view) {
		this.observers.add(view);		
	}

	@Override final public void detachView(IView view) {
		this.observers.remove(view);
	}
}
