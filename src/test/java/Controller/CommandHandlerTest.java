package Controller;

import all.Controller.CommandHandler;
import all.domain.Amazon.Amazon;
import all.domain.Product.Product;
import all.domain.Rating.Rating;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
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

@RunWith(MockitoJUnitRunner.class)
class CommandHandlerTest {

    private Amazon amazon;

    private User user;
    private Supplier supplier;
    private Product product;
    private final String PRODUCT_DOES_NOT_EXIT_ERROR = "Product does not exist!";

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private final int PRODUCT_ID = 1;

    public CommandHandlerTest() throws Exception {
        this.product = new Product(PRODUCT_ID, "ice cream", 1, 20000, new ArrayList<>(){{add("snack");}}, 10, 50);//(Product) Mockito.mock(Product.class);
        this.user =  (User) Mockito.mock(User.class);
        this.supplier = (Supplier) Mockito.mock(Supplier.class);
//         this.user = new User("user1", "#123", "a@gmail.com", "12/5/2022", "hi", "5");
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        amazon = new Amazon();
    }

    @Test
    void addSupplierDoesNotReturnError() throws Exception {
        Supplier p = new Supplier(1, "product", "date");
        amazon.addSupplier(p);
    }

    @Test
    void getCommodityByIdReturnNoError() throws Exception {
        amazon.addSupplier(supplier);
        Mockito.when(this.supplier.getId()).thenReturn(1);
        amazon.addProduct(product);
        amazon.getCommodityById(PRODUCT_ID);
        Assertions.assertEquals("Product(id=1, name=ice cream, providerId=1, price=20000, categories=[snack], inStock=50, rating=10.0)"
                , outputStreamCaptor.toString()
                .trim());
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
        Rating rating = new Rating("user2", PRODUCT_ID,7);
        amazon.rateCommodity(rating);
        amazon.getCommodityById(PRODUCT_ID);
    }

    @Test
    void rateCommodityReturnProductNotFoundError() throws Exception {
        Rating rating = new Rating("user2", PRODUCT_ID+1,7);
        amazon.rateCommodity(rating);
        amazon.getCommodityById(PRODUCT_ID);
    }

    @Test
    void rateCommodityReturnNoError() throws Exception {
        amazon.addSupplier(supplier);
        Mockito.when(this.supplier.getId()).thenReturn(1);
        amazon.addProduct(product);
        Mockito.when(this.user.userNameEquals("user1")).thenReturn(false);
        this.amazon.addUser(user);
        Rating rating = new Rating("user1", PRODUCT_ID,7);
        Mockito.when(this.user.userNameEquals("user1")).thenReturn(true);
        amazon.rateCommodity(rating);

        amazon.getCommodityById(PRODUCT_ID);
    }

    @AfterEach
    void tearDown() {
        amazon = null;
    }
}