package framework.interfaces;

import java.time.LocalDate;

public interface IPerson extends ICustomer {
    LocalDate getBirthdate();

    void setBirthdate(LocalDate birthdate);
}
