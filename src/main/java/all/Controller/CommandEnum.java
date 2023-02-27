package all.Controller;

import all.domain.Product.Product;
import all.domain.Rating.Rating;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import static all.Controller.CommandHandler.amazon;
import static all.Controller.CommandHandler.mapper;
import all.Controller.Command.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    addCommodity {
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
        public void execute(String json) throws Exception{
            Rating rating = mapper.readValue(json, Rating.class);
            amazon.rateCommodity(rating);
        }
    },
    getCommodityById {
        @Override
        public void execute(String json) throws Exception{
            Integer id = mapper.readValue(json, Integer.class);
            amazon.getCommodityById(id);
        }
    },
    getCommoditiesByCategory {
        @Override
        public void execute(String json) throws Exception{
            String category = mapper.readValue(json, String.class);
            amazon.getCommoditiesByCategory(category);
        }
    },
    addToBuyList {
        @Override
        public void execute(String json) throws Exception {
            ArrayList<JsonNode> args = extractArgs(json,
                    new ArrayList<>(Arrays.asList("username", "commodityId")));
            amazon.addToBuyList(args.get(0).textValue(), Integer.parseInt(String.valueOf(args.get(1))));
        }
    },
    removeFromBuyList {
        @Override
        public void execute(String json) throws Exception {
            ArrayList<JsonNode> args = extractArgs(json,
                    new ArrayList<>(Arrays.asList("username", "commodityId")));
            amazon.removeFromBuyList(args.get(0).textValue(), Integer.parseInt(String.valueOf(args.get(1))));
        }
    },
    getBuyList {
        @Override
        public void execute(String json) throws Exception {
            ArrayList<JsonNode> args = extractArgs(json,
                    new ArrayList<>(List.of("username")));
            amazon.getUserBuyList(args.get(0).textValue());
        }
    }
}
