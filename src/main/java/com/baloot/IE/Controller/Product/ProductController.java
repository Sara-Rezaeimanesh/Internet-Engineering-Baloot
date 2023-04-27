package com.baloot.IE.Controller.Product;
import com.baloot.IE.domain.Amazon.Amazon;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Product.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductRepository productRepository;
    @Autowired
    public ProductController() throws Exception {
        this.productRepository = ProductRepository.getInstance();
    }

    @GetMapping("")
    public List<Product> all(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(name = "category", required = false) String category,
                                  @RequestParam(name = "price_range", required = false) String priceRange,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "id", required = false) String id,
                                  @RequestParam(name = "sort", required = false) String sort_param) throws Exception {
        Amazon amazon = Amazon.getInstance();
        if(amazon.isAnybodyLoggedIn()) {
            List<Product> products;
            productRepository.filterProducts(category, category, name, id);
            products = productRepository.sortProducts(sort_param);
            return products;
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new Exception("Please login first!");
        }
    }

    @GetMapping("/{id}")
    public Product one(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws Exception {
        Amazon amazon = Amazon.getInstance();
        if(amazon.isAnybodyLoggedIn())
            return productRepository.findProductsById(id);
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new Exception("Please login first!");
        }
    }
    @PostMapping("/{id}")
    public void action(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable int id,
                          @RequestParam("action") String action,
                          @RequestParam("arg") String arg) throws Exception {
        Amazon amazon = Amazon.getInstance();
      if(!amazon.isAnybodyLoggedIn()) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          throw new Exception("Please login first!");
      }
      if(Objects.equals(action, "rate"))
          amazon.addRating(arg);
      if(Objects.equals(action, "comment"))
          amazon.addComment(arg);
      if(Objects.equals(action, "add"))
          amazon.addToBuyList();
      if(Objects.equals(action, "like"))
          amazon.rateComment(arg, "1");
      if(Objects.equals(action, "dislike"))
          amazon.rateComment(arg, "-1");
    }
}
