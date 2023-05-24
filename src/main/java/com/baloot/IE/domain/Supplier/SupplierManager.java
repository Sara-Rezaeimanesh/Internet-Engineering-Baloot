package com.baloot.IE.domain.Supplier;

import com.baloot.IE.domain.Initializer.Initializer;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.repository.Supplier.SupplierRepository;
import com.baloot.IE.repository.User.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierManager {
    private static SupplierManager instance;
    private final SupplierRepository repository = SupplierRepository.getInstance();

    public SupplierManager() throws Exception {
        Initializer initializer = new Initializer();
        ArrayList<Supplier> suppliers = initializer.getProvidersFromAPI("providers");
        suppliers.forEach(Supplier::initialize);
        for(Supplier s : suppliers)
            repository.insert(s);
    }

    public static SupplierManager getInstance() throws Exception {
        if(instance == null)
            instance = new SupplierManager();
        return instance;
    }

    public ArrayList<Supplier> getAll() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Supplier findSupplierById(int id) {
        Supplier s = null;
        try {
            s = repository.findByField(String.valueOf(id), "id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(s != null)
            return s;
        throw new IllegalArgumentException("Supplier does not exist.");
    }

    public Supplier findSupplierByName(String name) {
        Supplier s = null;
        try {
            s = repository.findByField(name, "name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(s != null)
            return s;

        throw new IllegalArgumentException("Supplier does not exist.");
    }
}
