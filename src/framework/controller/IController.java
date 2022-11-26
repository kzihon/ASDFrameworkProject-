package framework.controller;

import framework.view.IView;

public interface IController {
    public void add(IView viewSender);
    public void update(IView viewSender);
    public void get(IView viewSender);
    public void delete(IView viewSender);
}
