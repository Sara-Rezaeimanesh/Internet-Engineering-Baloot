package all.Controller;

import all.domain.Amazon.Amazon;
import all.domain.Product.Product;
import all.domain.Rating.Rating;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandHandler {
    private final int COMMAND_IDX = 0;
    private final int ARGS_IDX = 1;

    private static Amazon amazon;
    public static ObjectMapper mapper;

    public CommandHandler(Amazon amazon) {
        CommandHandler.amazon = amazon;
        mapper = new ObjectMapper();
    }

    interface Command {
        void execute(String json) throws Exception;
    }

    enum CommandEnum implements Command {
        addUser {
            @Override
            public void execute(String json) throws JsonProcessingException {
                User user = mapper.readValue(json, User.class);
                amazon.addUser(user);
            }
        },
        addProvider {
            @Override
            public void execute(String json) throws JsonProcessingException {
                Supplier supplier = mapper.readValue(json, Supplier.class);
                amazon.addSupplier(supplier);
            }
        },
        addProduct {
            @Override
            public void execute(String json) throws Exception {
                Product product = mapper.readValue(json, Product.class);
                amazon.addProduct(product);
            }
        },
        getCommoditiesList {
            @Override
            public void execute(String json) throws JsonProcessingException {
                amazon.listCommodities();
            }
        },
        rateCommodity {
            @Override
            public void execute(String json) throws Exception {
                Rating rating = mapper.readValue(json, Rating.class);
                amazon.rateCommodity(rating);
            }
        }
    }

    public void run() throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        String line;
        while((line = reader.readLine()) != null) {
            String []lineSplit = line.split(" ", 2);
            String command = lineSplit[COMMAND_IDX];
            String args = lineSplit[ARGS_IDX];
            CommandEnum.valueOf(command).execute(args);
        }


    }
}
