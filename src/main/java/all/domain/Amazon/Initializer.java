package all.domain.Amazon;

import all.domain.Comment.Comment;
import all.domain.Product.Product;
import all.domain.Supplier.Supplier;
import all.domain.User.User;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Initializer {


    HashMap<String, Object> classMap = new HashMap<String,Object>(){{put("commodities", Product.class);
                                                                     put("providers", Supplier.class);
                                                                     put("users", User.class);
                                                                     put("comments", Comment .class);}};
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

    private List<Product> getCommoditiesFromAPI(final String URL) throws Exception{
        String ProductsJsonString = getRequest(baseUrl + URL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Product> products = gson.fromJson(ProductsJsonString, new TypeToken<List<Product>>() {}.getType());
        return products;
    }

    private List<Supplier> getProvidersFromAPI(final String URL) throws Exception{
        String ProvidersJsonString = getRequest(baseUrl + URL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Supplier> providers = gson.fromJson(ProvidersJsonString, new TypeToken<List<Supplier>>() {}.getType());
        return providers;
    }

    private List<User> getUsersFromAPI(final String URL) throws Exception{
        String UserJsonString = getRequest(baseUrl + URL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<User> users = gson.fromJson(UserJsonString, new TypeToken<List<User>>() {}.getType());
        return users;
    }

    private List<Comment> getCommentsFromAPI(final String URL) throws Exception{
        String CommentsJsonString = getRequest(baseUrl + URL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Comment> comments = gson.fromJson(CommentsJsonString, new TypeToken<List<Comment>>() {}.getType());
        return comments;
    }

}
