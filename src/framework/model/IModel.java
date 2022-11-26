package framework.model;

import framework.view.IView;

public interface IModel {
    public void attachView(IView view);
    public void detachView(IView view);
    public void notifyViews();
    public void save();
    public void delete();
}
