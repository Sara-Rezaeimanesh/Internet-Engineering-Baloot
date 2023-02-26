package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import domain.Amazon.Amazon;
import domain.Supplier.Supplier;
import domain.User.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandHandler {
    private final int COMMAND_IDX = 0;
    private final int ARGS_IDX = 1;

    private static Amazon amazon;
    public static ObjectMapper mapper;

    public CommandHandler() {
        amazon = new Amazon();
        mapper = new ObjectMapper();
    }

    interface Command {
        void execute(String json) throws JsonProcessingException;
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
        getCommoditiesList {
            @Override
            public void execute(String json) throws JsonProcessingException {
                amazon.listCommodities();
            }
        }
    }

    public void run() throws IOException {
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
