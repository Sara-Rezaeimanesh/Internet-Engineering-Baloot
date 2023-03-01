package Controller;

import all.Controller.CommandHandler;
import all.domain.Amazon.Amazon;
import all.domain.Product.Product;
import all.domain.Rating.Rating;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@RunWith(MockitoJUnitRunner.class)
class CommandHandlerTest {

    private Amazon amazon;

    private User user1;
    private User user2;
    private Supplier supplier;
    private Product product;
    private final String PRODUCT_DOES_NOT_EXIT_ERROR = "Product does not exist!";
    private final String USER_DOES_NOT_EXIST_ERROR = "User does not exist!";

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ObjectWriter ow;

    private final int PRODUCT_ID = 1;

    public CommandHandlerTest() throws Exception {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @BeforeEach
    void setUp() throws Exception {
        this.product = new Product(PRODUCT_ID, "ice cream", 1, 20000, new ArrayList<>(){{add("snack");}}, 10, 50);//(Product) Mockito.mock(Product.class);
//        this.user =  (User) Mockito.mock(User.class);
        this.supplier = (Supplier) Mockito.mock(Supplier.class);
        this.user1 = new User("user1", "#123", "a@gmail.com", "12/5/2022", "hi", 500);
        this.user2 = new User("user2", "#123", "a@gmail.com", "12/5/2022", "hi", 100);
        System.setOut(new PrintStream(outputStreamCaptor));
        amazon = new Amazon();
    }

    @Test
    void addSupplierDoesNotReturnError() throws Exception {
        Supplier p = new Supplier(1, "product", "date");
        amazon.addSupplier(p);
    }

    @Test
    void getCommodityByIdGivesCommodityJson() throws Exception {
        amazon.addSupplier(supplier);
        Mockito.when(this.supplier.getId()).thenReturn(1);
        amazon.addProduct(product);
        amazon.getCommodityById(PRODUCT_ID);
        Assertions.assertEquals("\"data\": {" + ow.writeValueAsString(product) + "}"
                , outputStreamCaptor.toString().trim());
    }

    @Test
    void getCommodityByIdDoesNotReturnSupplierNotExistError()  {
        try{
            amazon.getCommodityById(PRODUCT_ID);
        }
        catch (Exception e){
            Assertions.assertEquals(e.getMessage(), PRODUCT_DOES_NOT_EXIT_ERROR);
        }
    }

    @Test
    void rateCommodityReturnUserNotFoundError() throws Exception {
        try{
            Rating rating = new Rating("user3", PRODUCT_ID,7);
            amazon.rateCommodity(rating);
        }
        catch (Exception e){
            Assertions.assertEquals(e.getMessage(), USER_DOES_NOT_EXIST_ERROR);
        }

    }

    @Test
    void rateCommodityReturnProductNotFoundError() throws Exception {
        try{
            this.amazon.addUser(user1);
            Rating rating = new Rating("user1", PRODUCT_ID,7);
            amazon.rateCommodity(rating);
        }
        catch (Exception e){
            Assertions.assertEquals(e.getMessage(), PRODUCT_DOES_NOT_EXIT_ERROR);
        }
    }

    private void prepareForTestingRateCommodity() throws Exception {
        amazon.addSupplier(supplier);
        Mockito.when(this.supplier.getId()).thenReturn(1);
        amazon.addProduct(product);
        this.amazon.addUser(user1);
    }

    @Test
    void rateCommodityReturnNoError() throws Exception {
        prepareForTestingRateCommodity();
        Rating rating = new Rating("user1", PRODUCT_ID,7);
        amazon.rateCommodity(rating);
        amazon.getCommodityById(PRODUCT_ID);
        Assertions.assertEquals("Product(id=1, name=ice cream, providerId=1, price=20000, categories=[snack], inStock=50, rating=7.0)"
                , outputStreamCaptor.toString().trim());
    }

    @Test
    void rateCommodityTwoUsersReturnNoError() throws Exception {
        prepareForTestingRateCommodity();
        this.amazon.addUser(user2);
        Rating rating1 = new Rating("user1", PRODUCT_ID,7);
        Rating rating2 = new Rating("user2", PRODUCT_ID,8);
        amazon.rateCommodity(rating1);
        amazon.rateCommodity(rating2);
        amazon.getCommodityById(PRODUCT_ID);
        Assertions.assertEquals("Product(id=1, name=ice cream, providerId=1, price=20000, categories=[snack], inStock=50, rating=7.5)"
                , outputStreamCaptor.toString().trim());
    }

    @Test
    void rateCommodityTwoRateOneUsersReturnNoError() throws Exception {
        prepareForTestingRateCommodity();
        Rating rating1 = new Rating("user1", PRODUCT_ID,7);
        amazon.rateCommodity(rating1);
        Rating rating2 = new Rating("user1", PRODUCT_ID,8);
        amazon.rateCommodity(rating2);
        amazon.getCommodityById(PRODUCT_ID);
        Assertions.assertEquals("Product(id=1, name=ice cream, providerId=1, price=20000, categories=[snack], inStock=50, rating=8.0)"
                , outputStreamCaptor.toString().trim());
    }

    ArrayList<Product> addProductsWithSameCats(String cat) throws Exception {
        amazon.addSupplier(supplier);
        Mockito.when(this.supplier.getId()).thenReturn(1);
        ArrayList<Product> ps = new ArrayList<>(Arrays.asList(
                new Product(1, "piaz", 1, 3000,
                        new ArrayList<String>(List.of(cat)), 10, 12),
                new Product(2, "piaz", 1, 3000,
                        new ArrayList<String>(List.of(cat)), 10, 12)));

        amazon.addProduct(ps.get(0));
        amazon.addProduct(ps.get(1));
        return ps;
    }

    @Test
    void getCOmmoditiesByCategoryReturnsProductsWithCategry() throws Exception {
        ArrayList<Product> ps = addProductsWithSameCats("sabzi");

        amazon.getCommoditiesByCategory("sabzi");
        Assertions.assertEquals("\"data\": {\"commoditiesListByCategory\": "+ ow.writeValueAsString(ps) + "}"
                                    , outputStreamCaptor.toString().trim());
    }

    @Test
    void getCommoditiesByCategoryReturnsEmptyList() throws Exception {
        ArrayList<Product> ps = addProductsWithSameCats("sabzi");

        amazon.getCommoditiesByCategory("mashin");
        Assertions.assertEquals("\"data\": {\"commoditiesListByCategory\": [ ]}"
                , outputStreamCaptor.toString().trim());
    }

    @Test
    void addProductToBuyListThrowsUserNotFoundExcpetion() throws Exception {

    }

    @Test
    void addProductToBuyListThrowsProductNotFoundExcpetion() throws Exception {

    }

    @Test
    void addProductToBuyListThrowsProductAlreadyAddedExcpetion() throws Exception {

    }

    @Test
    void addProductToBuyListThrowsNotInStockExcpetion() throws Exception {

    }

    @AfterEach
    void tearDown() {
        this.product = null;
        this.user1 = null;
        this.user2 = null;
        this.supplier = null;
        this.amazon = null;
    }
}