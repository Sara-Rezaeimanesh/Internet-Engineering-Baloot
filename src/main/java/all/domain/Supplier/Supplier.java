package all.domain.Supplier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


public class Supplier {

    private int id;
    private String name;
    private String registryDate;
    private ArrayList<Integer> products;

    @JsonCreator
    public Supplier(@JsonProperty("id")int id,
                    @JsonProperty("name")String name,
                    @JsonProperty("registryDate")String registryDate) {
        this.id = id;
        this.name = name;
        this.registryDate = registryDate;
    }
}
