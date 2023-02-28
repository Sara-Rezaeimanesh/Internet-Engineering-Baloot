package Controller;

import all.Controller.CommandHandler;
import all.domain.Amazon.Amazon;
import all.domain.Product.Product;
import all.domain.Rating.Rating;
import all.domain.Supplier.Supplier;
import all.domain.User.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
class CommandHandlerTest {

    private Amazon amazon;

    private User user;
    private Supplier supplier;
    private Product product;

    private final int PRODUCT_ID = 1;

    public CommandHandlerTest() throws Exception {
        this.product = new Product(PRODUCT_ID, "ice cream", 1, 20000, new ArrayList<>(){{add("snack");}}, 10, 50);//(Product) Mockito.mock(Product.class);
        this.user =  (User) Mockito.mock(User.class);
        this.supplier = (Supplier) Mockito.mock(Supplier.class);
//         this.user = new User("user1", "#123", "a@gmail.com", "12/5/2022", "hi", "5");
    }

    @BeforeEach
    void setUp() {
        amazon = new Amazon();
    }

    @Test
    void addSupplierDoesNotReturnError() throws Exception {
        Supplier p = new Supplier(1, "product", "date");
        amazon.addSupplier(p);
    }

    @Test
    void getCommodityByIdReturnNoError() throws Exception {
        amazon.addProduct(product);
        amazon.getCommodityById(PRODUCT_ID);

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