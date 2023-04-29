package com.baloot.IE.domain.Amazon;

import com.baloot.IE.domain.Discount.Discount;
import com.baloot.IE.domain.Product.ProductRepository;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Supplier.Supplier;
import com.baloot.IE.domain.Supplier.SupplierRepository;
import com.baloot.IE.domain.User.User;
import com.baloot.IE.domain.User.UserRepository;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Amazon {
    private static Amazon instance;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private SupplierRepository supplierRepository;

    private ArrayList<Discount> discounts;

    private static String errorMsg;

    public static String getErrorMsg(){
        return errorMsg;
    }
    public static void setErrorMsg(String err){
        errorMsg = err;
    }
    public Amazon() throws Exception {

        productRepository = ProductRepository.getInstance();
        userRepository = UserRepository.getInstance();
        supplierRepository = SupplierRepository.getInstance();

    }

    public static Amazon getInstance() throws Exception {
        if(instance == null)
            instance = new Amazon();
        return instance;
    }


    public Discount findDiscountById(String did) {
        for(Discount d : discounts)
            if(d.discountCodeEquals(did))
                return d;
        return null;
    }

//    public void addToBuyList() throws Exception {
//        if(activeUser.hasBoughtProduct(chosenProduct.getId()))
//        {
//            errorMsg = PRODUCT_HAS_BOUGHT_ERROR;
//            throw new Exception(PRODUCT_HAS_BOUGHT_ERROR);
//        }
//        if(!chosenProduct.isInStock())
//        {
//            errorMsg = PRODUCT_IS_NOT_IN_STOCK;
//            throw new Exception(PRODUCT_IS_NOT_IN_STOCK);
//        }
//        activeUser.addProduct(chosenProduct);
//        chosenProduct.updateStock(-1);
//    }


//    public void applyDiscount(String discountCode) throws Exception {
//        Discount discount = findDiscountById(discountCode);
//        if(discount == null) {
//            errorMsg = "This discount does not exist";
//            throw new Exception();
//        }
//        if(discount.isValidToUse(activeUser.getUsername())) {
//            activeUser.applyDiscount(discount.getDiscount());
//            discount.addToUsed(activeUser.getUsername());
//        }
//        else{
//            errorMsg = "You have already use this discount code";
//            throw new Exception();
//        }
//
//    }
}
