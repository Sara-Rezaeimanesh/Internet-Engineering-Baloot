package com.baloot.IE.domain.Supplier;

import com.baloot.IE.domain.Initializer.Initializer;
import java.util.ArrayList;

public class SupplierManager {
    private final ArrayList<Supplier> suppliers;
    private static SupplierManager instance;

    public SupplierManager() throws Exception {
        Initializer initializer = new Initializer();
        suppliers = initializer.getProvidersFromAPI("providers");
        suppliers.forEach(Supplier::initialize);
    }

    public static SupplierManager getInstance() throws Exception {
        if(instance == null)
            instance = new SupplierManager();
        return instance;
    }

    public ArrayList<Supplier> getAll() {
        return suppliers;
    }

    public Supplier findSupplierById(int id) {
        for (Supplier s : suppliers)
            if (id == s.getId())
                return s;

        throw new IllegalArgumentException("Supplier does not exist.");
    }

    public Supplier findSupplierByName(String name) {
        for (Supplier s : suppliers)
            if (name.equalsIgnoreCase(s.getName()))
                return s;

        throw new IllegalArgumentException("Supplier does not exist.");
    }
}
