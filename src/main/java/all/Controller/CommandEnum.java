package all.Controller;

import all.domain.Product.Product;
import all.domain.Rating.Rating;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import static all.Controller.CommandHandler.amazon;
import static all.Controller.CommandHandler.mapper;
import all.Controller.Command.*;

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
        public void execute(String json) throws Exception{
            Rating rating = mapper.readValue(json, Rating.class);
            amazon.rateCommodity(rating);
        }
    }
}
