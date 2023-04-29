package com.baloot.IE.Controller.Product;
import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    @Autowired
    public ProductController() throws Exception {
        this.productRepository = ProductRepository.getInstance();
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
    public void rate(@PathVariable int id, @RequestBody Map<String, String> body) throws Exception {
      productRepository.updateRating(body.get("username"), body.get("rate"), id);
    }

    @GetMapping("/{id}/comments")
    public ArrayList<Comment> allComments(@PathVariable int id) {
        return productRepository.getProductComments(id);
    }

    @PostMapping("/{id}/comments")
    public void addComment(@PathVariable int id,
                           @RequestBody Map<String, String> body) {
        productRepository.addComment(body.get("username"), id, body.get("comment"));
    }

    @PutMapping("/{id}/comments/{commentId}/vote")
    public void voteComment(@PathVariable int id, @PathVariable int commentId,
                            @RequestBody Map<String, String> body) {
        productRepository.voteComment(body.get("username"), id,
                                    commentId, Integer.parseInt(body.get("vote")));
    }

    @GetMapping("/{id}/suggestions")
    public ArrayList<Product> getSuggestions(@PathVariable int id) {
        return productRepository.findProductsById(id).getSuggestedProducts();
    }
}
