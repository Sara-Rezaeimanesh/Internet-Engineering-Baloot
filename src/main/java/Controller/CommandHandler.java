package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import domain.Amazon.Amazon;
import domain.User.User;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandHandler {
    private final int COMMAND_IDX = 0;
    private final int ARGS_IDX = 1;

    private static Amazon amazon;

    public CommandHandler() {
//        this.amazon = amazon;
    }

    interface Command {
        void execute(String json) throws JsonProcessingException;
    }

    enum CommandEnum implements Command {
        addUser {
            @Override
            public void execute(String json) throws JsonProcessingException {
                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.readValue(json, User.class);
                amazon.addUser(user);
            }
        },
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
