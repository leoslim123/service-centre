package ru.servicecenter.dao;

import ru.servicecenter.model.Part;
import java.util.List;

public interface PartDao {

    void create(Part part);

    Part getById(int id);

    List<Part> getAll();

    void update(Part part);

    void delete(int id);

    List<Part> searchByName(String name);
}
