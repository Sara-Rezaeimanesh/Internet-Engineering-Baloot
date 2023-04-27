package com.baloot.IE.Controller.Product;
import com.baloot.IE.domain.Amazon.Amazon;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Product.ProductRepository;
import com.baloot.IE.domain.Session.Session;
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

    private final ProductRepository productRepository;
    private final Session session;
    @Autowired
    public ProductController() throws Exception {
        this.productRepository = ProductRepository.getInstance();
        session = Session.getInstance();
    }

    @GetMapping("")
    public List<Product> all(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(name = "category", required = false) String category,
                                  @RequestParam(name = "price_range", required = false) String priceRange,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "id", required = false) String id,
                                  @RequestParam(name = "sort", required = false) String sort_param) throws Exception {
        if(session.isAnybodyLoggedIn()) {
            List<Product> products = productRepository.filterProducts(category, priceRange, name, id);
            if(sort_param != null)
                products = productRepository.sortProducts(products, sort_param);
            return products;
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new Exception("Please login first!");
        }
    }

    @GetMapping("/{id}")
    public Product one(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) throws Exception {
        if(session.isAnybodyLoggedIn()) {
            Product p = productRepository.findProductsById(id);
            productRepository.saveChosenProduct(p);
            return p;
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new Exception("Please login first!");
        }
    }

    @PostMapping("/{id}/ratings")
    public void rate(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable int id,
                          @RequestParam("rate") String rate) throws Exception {
      if(!session.isAnybodyLoggedIn()) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          throw new Exception("Please login first!");
      }
      productRepository.updateRating(session.getActiveUser().getUsername(), rate, id);
//      if(Objects.equals(action, "comment"))
//          amazon.addComment(arg);
//      if(Objects.equals(action, "add"))
//          amazon.addToBuyList();
//      if(Objects.equals(action, "like"))
//          amazon.rateComment(arg, "1");
//      if(Objects.equals(action, "dislike"))
//          amazon.rateComment(arg, "-1");
    }
    @PostMapping("/{id}/comments")
    public void comment(HttpServletRequest request, HttpServletResponse response,
                       @PathVariable int id,
                       @RequestParam("comment") String comment) throws Exception {
        if(!session.isAnybodyLoggedIn()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new Exception("Please login first!");
        }
        productRepository.addComment(session.getActiveUser().getUsername(), id, comment);
//      if(Objects.equals(action, "comment"))
//
//      if(Objects.equals(action, "add"))
//          amazon.addToBuyList();
//      if(Objects.equals(action, "like"))
//          amazon.rateComment(arg, "1");
//      if(Objects.equals(action, "dislike"))
//          amazon.rateComment(arg, "-1");
    }
}
