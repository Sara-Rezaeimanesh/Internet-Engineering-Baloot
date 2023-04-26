package all.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

interface Command {
    void execute(String json) throws Exception;

    default ArrayList<JsonNode> extractArgs(String json, ArrayList<String> argNames) throws JsonProcessingException {
        ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
        ArrayList<JsonNode> args = new ArrayList<>();
        for(String argName : argNames)
            if(node.has(argName))
                args.add(node.get(argName));

        return args;
    }
}