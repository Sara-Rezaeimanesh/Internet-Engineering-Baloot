package com.baloot.IE.domain.Initializer;

import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.Discount.Discount;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Supplier.Supplier;
import com.baloot.IE.domain.User.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.ArrayList;

public class Initializer {
    String baseUrl = "http://5.253.25.110:5000/api/";

    public static String getRequest(String url) throws Exception{
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();

            String result = "";
            if (entity != null)
                result = EntityUtils.toString(entity);
            return result;
        }
    }

    public ArrayList<Product> getCommoditiesFromAPI(final String URL) throws Exception{
        String ProductsJsonString = getRequest(baseUrl + URL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(ProductsJsonString, new TypeToken<ArrayList<Product>>() {}.getType());
    }

    public ArrayList<Supplier> getProvidersFromAPI(final String URL) throws Exception{
        String ProvidersJsonString = getRequest(baseUrl + URL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(ProvidersJsonString, new TypeToken<ArrayList<Supplier>>() {}.getType());
    }

    public ArrayList<User> getUsersFromAPI(final String URL) throws Exception{
        String UserJsonString = getRequest(baseUrl + URL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(UserJsonString, new TypeToken<ArrayList<User>>() {}.getType());
    }

    public ArrayList<Comment> getCommentsFromAPI(final String URL) throws Exception{
        String CommentsJsonString = getRequest(baseUrl + URL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(CommentsJsonString, new TypeToken<ArrayList<Comment>>() {}.getType());
    }

    public ArrayList<Discount> getDiscountsFromAPI(final String URL) throws Exception{
        String DiscountJsonString = getRequest(baseUrl + URL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(DiscountJsonString, new TypeToken<ArrayList<Discount>>() {}.getType());
    }

}
