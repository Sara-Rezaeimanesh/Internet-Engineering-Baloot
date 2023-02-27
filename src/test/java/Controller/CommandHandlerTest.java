package Controller;

import all.Controller.CommandHandler;
import all.domain.Amazon.Amazon;
import all.domain.Product.Product;
import all.domain.Supplier.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

class CommandHandlerTest {

    private CommandHandler ch;
    private Amazon amazon;

    public CommandHandlerTest() {

    }

    @BeforeEach
    void setUp() {
        amazon = new Amazon();
    }

    @Test
    void addSupplierDoesntReturnError() throws Exception {
        Supplier p = new Supplier(1, "product", "date");
        amazon.addSupplier(p);
    }


    @AfterEach
    void tearDown() {
    }
}