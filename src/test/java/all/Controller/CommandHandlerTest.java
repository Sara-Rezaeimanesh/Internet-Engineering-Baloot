package all.Controller;

import all.Controller.CommandHandler;
import all.domain.Amazon.Amazon;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
    public void searchCommodityByCategoryReturnsAllCategoryCommodities() throws Exception {
        HttpResponse<String> response = Unirest.get("http://localhost:8080/commodities/search/Vegetables").asString();
        Assertions.assertEquals(8, response.getBody().toString().split("Vegetables", -1).length-1);
    }


    @Test
    public void searchCommodityByPriceIntervalReturnsAllPriceIntervalCommodities() throws Exception {
        HttpResponse<String> response = Unirest.get("http://localhost:8080/commodities/search/9000/14500").asString();
        Assertions.assertEquals(3, response.getBody().toString().split("<tr>", -1).length-2);
    }

    @Test
    public void searchCommodityByPriceIntervalReturnsNoPriceIntervalCommodities() throws Exception {
        HttpResponse<String> response = Unirest.get("http://localhost:8080/commodities/search/0/1").asString();
        Assertions.assertEquals(0, response.getBody().toString().split("<tr>", -1).length-2);
    }

    @Test
    public void userBuyListReturnsTwoCommodities() throws Exception {
        amazon.addToBuyList("amir", 1);
        amazon.addToBuyList("amir", 2);
        HttpResponse<String> response = Unirest.get("http://localhost:8080/users/amir").asString();
        Assertions.assertEquals(1, response.getBody().toString().split("Onion", -1).length-1);
        Assertions.assertEquals(1, response.getBody().toString().split("Potato", -1).length-1);
    }

    @Test
    public void userBuyListReturnsNoCommodities() throws Exception {
        HttpResponse<String> response = Unirest.get("http://localhost:8080/users/amir").asString();
        Assertions.assertEquals(0, response.getBody().toString().split("removeFromBuyList", -1).length-1);
    }

    @Test
    public void rateCommodityUserUpdateRateChangesCommodityRate() throws Exception {
        HttpResponse<String> response1 = Unirest.get("http://localhost:8080/rateCommodity/amir/1/7").asString();
        HttpResponse<String> response2 = Unirest.get("http://localhost:8080/rateCommodity/amir/1/4").asString();
        System.out.println(response2.getBody().toString());
        Assertions.assertEquals(1, response2.getBody().toString().split("Rating: 4.0", -1).length-1);
    }

    @Test
    public void rateCommodityUserAddRateChangesCommodityRate() throws Exception {
        HttpResponse<String> response1 = Unirest.get("http://localhost:8080/rateCommodity/amir/1/7").asString();
        HttpResponse<String> response2 = Unirest.get("http://localhost:8080/rateCommodity/hamid/1/4").asString();
        Assertions.assertEquals(1, response2.getBody().toString().split("Rating: 5.5", -1).length-1);
    }

    @Test
    public void rateCommodityReturns404ForUserNotFound() throws Exception {
        HttpResponse<String> response1 = Unirest.get("http://localhost:8080/rateCommodity/narges/1/7").asString();
        Assertions.assertEquals(1, response1.getBody().toString().split("<h1>404<br>Page Not Found!</h1>", -1).length-1);
    }

    @Test
    public void rateCommodityReturns404ForProductNotFound() throws Exception {
        HttpResponse<String> response1 = Unirest.get("http://localhost:8080/rateCommodity/amir/50/7").asString();
        Assertions.assertEquals(1, response1.getBody().toString().split("<h1>404<br>Page Not Found!</h1>", -1).length-1);
    }

    @Test
    public void rateCommodityReturns403ForRateIsNotInValidRange() throws Exception {
        HttpResponse<String> response1 = Unirest.get("http://localhost:8080/rateCommodity/amir/50/12").asString();
        System.out.println(response1.getBody().toString());
        Assertions.assertEquals(1, response1.getBody().toString().split("<h1>403<br>This function is Forbidden!</h1>", -1).length-1);
    }

    @AfterEach
    public void teardown() {
        amazon = null;
        cm = null;
    }
}