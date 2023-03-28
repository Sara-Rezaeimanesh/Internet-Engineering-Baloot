package all.Controller;

import all.Controller.CommandHandler;
import all.domain.Amazon.Amazon;
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
    Amazon amazon;
    CommandHandler cm;

    @BeforeEach
    public void setup()  {
        try{
            amazon = new Amazon();
            cm = new CommandHandler(amazon);
            cm.run(8080);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void searchCommodityByCategory() throws Exception {

    }

    @AfterEach
    public void teardown() {
        amazon = null;
        cm = null;
    }
}