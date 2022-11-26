package framework.dao;

import framework.model.IModel;

import java.util.List;

public interface DAO {
    public void persist(IModel model);
    public void update(IModel model);
    public IModel getById(int id);
    public List<IModel> getAll();
    public void delete(IModel model);
}
