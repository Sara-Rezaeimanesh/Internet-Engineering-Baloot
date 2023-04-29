package com.baloot.IE.domain.Supplier;

import com.baloot.IE.domain.Initializer.Initializer;
import java.util.ArrayList;

public class SupplierRepository {
    private final ArrayList<Supplier> suppliers;
    private static SupplierRepository instance;

    public SupplierRepository() throws Exception {
        Initializer initializer = new Initializer();
        suppliers = initializer.getProvidersFromAPI("providers");
        suppliers.forEach(Supplier::initialize);
    }

    public static SupplierRepository getInstance() throws Exception {
        if(instance == null)
            instance = new SupplierRepository();
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
}
