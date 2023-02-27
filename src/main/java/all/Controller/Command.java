package all.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;

interface Command {
    void execute(String json) throws JsonProcessingException;
}