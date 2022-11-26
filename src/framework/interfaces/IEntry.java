package framework.interfaces;

import framework.model.IModel;

import java.time.LocalDate;

public interface IEntry extends IModel {
    double getAmount();

    LocalDate getDate();
}
