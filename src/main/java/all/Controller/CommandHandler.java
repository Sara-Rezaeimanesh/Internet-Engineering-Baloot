package all.Controller;

import all.domain.Amazon.Amazon;
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

    static Amazon amazon;
    public static ObjectMapper mapper;

    public CommandHandler(Amazon amazon) {
        CommandHandler.amazon = amazon;
        mapper = new ObjectMapper();
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
