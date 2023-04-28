package com.baloot.IE.Controller.Product;
import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Product.ProductRepository;
import com.baloot.IE.domain.Session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    public List<Product> all(@RequestParam(name = "category", required = false) String category,
                             @RequestParam(name = "price_range", required = false) String priceRange,
                             @RequestParam(name = "name", required = false) String name,
                             @RequestParam(name = "id", required = false) String id,
                             @RequestParam(name = "sort", required = false) String sort_param) {
        List<Product> products = productRepository.filterProducts(category, priceRange, name, id);
        if(sort_param != null)
            products = productRepository.sortProducts(products, sort_param);
        return products;
    }

    @GetMapping("/{id}")
    public Product one(@PathVariable int id){
        Product p = productRepository.findProductsById(id);
        productRepository.setSuggestedProducts(p);
        return p;
    }

    @PostMapping("/{id}/ratings")
    public void rate(@PathVariable int id, @RequestParam("rate") String rate) throws Exception {
      productRepository.updateRating(session.getActiveUser().getUsername(), rate, id);
    }

    @GetMapping("/{id}/comments")
    public ArrayList<Comment> allComments(HttpServletRequest request, HttpServletResponse response,
                                          @PathVariable int id) {
        return productRepository.getProductComments(id);
    }


    @PostMapping("/{id}/comments")
    public void addComment(HttpServletRequest request, HttpServletResponse response,
                       @PathVariable int id,
                       @RequestParam("comment") String comment) {
        productRepository.addComment(session.getActiveUser().getEmail(), id, comment);
    }

    @PostMapping("/{id}/comments/{commentId}")
    public void voteComment(HttpServletRequest request, HttpServletResponse response,
                        @PathVariable int id,
                        @PathVariable int commentId,
                        @RequestParam("vote") String vote) {
        productRepository.voteComment(session.getActiveUser().getEmail(), id, commentId, Integer.parseInt(vote));
    }
}
