package all.domain.Supplier;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Supplier {

    private int id;
    private String name;
    private String registeryDate;
    private ArrayList<Integer> products;

    public int getId() {
        return id;
    }

    public Supplier(@JsonProperty("id") int id,
                    @JsonProperty("name") String name,
                    @JsonProperty("registryDate") String registeryDate) {
        this.id = id;
        this.name = name;
        this.registeryDate = registeryDate;
    }
}
