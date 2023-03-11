package ir.proprog.enrollassist.controller.course;

import all.Controller.CommandHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CommandHandler.class)
class CommandHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {}

    @Test
    public void command() throws Exception {
        mockMvc.perform(get("/rateCommodity/user1/1/1"))
                .andExpect(jsonPath("$[*].courseTitle", containsInAnyOrder("AI", "DL", "NLP")))
                .andExpect(jsonPath("$[*].graduateLevel", containsInAnyOrder("Masters", "Masters", "Masters")))
                .andExpect(jsonPath("$[*].courseNumber.courseNumber", containsInAnyOrder("1234568", "1234567", "1234569")))
                .andExpect(jsonPath("$[*].courseCredits", containsInAnyOrder(4, 4, 4)))
                .andExpect(status().isOk());
    }

    @AfterEach
    public void teardown() {}


}