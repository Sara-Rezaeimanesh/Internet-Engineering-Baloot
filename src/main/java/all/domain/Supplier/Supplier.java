package all.domain.Supplier;

import java.util.ArrayList;

public class Supplier {

    private int id;
    private String name;
    private String registeryDate;
    private ArrayList<Integer> products;

    public int getId() {
        return id;
    }

    public Supplier(int id, String name, String registeryDate) {
        this.id = id;
        this.name = name;
        this.registeryDate = registeryDate;
    }
}
