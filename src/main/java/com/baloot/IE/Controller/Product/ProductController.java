package com.baloot.IE.Controller.Product;
import com.baloot.IE.domain.Comment.Comment;
import com.baloot.IE.domain.Comment.CommentManager;
import com.baloot.IE.domain.Product.Product;
import com.baloot.IE.domain.Product.ProductManager;
import com.baloot.IE.domain.Supplier.SupplierManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/products")
@Order(3)
public class ProductController {

    private final int ppp;
    private final ProductManager productManager;
    private final SupplierManager supplierManager;
    private final CommentManager commentManager;
    private List<Product> results;
    @Autowired
    public ProductController() throws Exception {
        this.productManager = ProductManager.getInstance();
        this.supplierManager = SupplierManager.getInstance();
        this.commentManager = CommentManager.getInstance();
        results = new ArrayList<>();
        ppp = 12;
    }

    @GetMapping("")
    public SearchResult all(@RequestParam(name = "category", required = false) String category,
                             @RequestParam(name = "price_range", required = false) String priceRange,
                             @RequestParam(name = "name", required = false) String name,
                             @RequestParam(name = "id", required = false) String id,
                             @RequestParam(name = "sort", required = false) String sort_param,
                             @RequestParam(name = "provider", required = false) String provider_name,
                             @RequestParam(name = "available", required = false) String available,
                             @RequestParam(name = "apply") String apply,
                             @RequestParam(name = "page") int page) {

        int supplier_id = -1;
        if(provider_name != null)
            supplier_id = supplierManager.findSupplierByName(provider_name).getId();

        if(Integer.parseInt(apply) == 1) {
            results = productManager.filterProducts(category, priceRange, name, id, supplier_id, available);
            if(sort_param != null)
                results = productManager.sortProducts(results, sort_param);
        }
        int result_size = results.size();
        int end = Math.min(results.size(), (page + 1) * ppp);
        return new SearchResult(results.subList(page*ppp, end), result_size);
    }

    @GetMapping("/{id}")
    public Product one(@PathVariable int id){
        Product p = productManager.findProductsById(id);
        productManager.setSuggestedProducts(p);
        return p;
    }

    @PostMapping("/{id}/ratings")
    public void rate(@PathVariable int id, @RequestBody Map<String, String> body) throws Exception {
      productManager.updateRating(body.get("username"), body.get("rate"), id);
    }

    @GetMapping("/{id}/comments")
    public ArrayList<Comment> allComments(@PathVariable int id) {
        return productManager.getProductComments(id);
    }

    @PostMapping("/{id}/comments")
    public void addComment(@PathVariable int id,
                           @RequestBody Map<String, String> body) throws Exception {
        productManager.addComment(body.get("username"), id, body.get("comment"));
    }

    @PutMapping("/{id}/comments/{commentId}/vote")
    public void voteComment(@PathVariable int id, @PathVariable int commentId,
                            @RequestBody Map<String, String> body) throws SQLException {
        productManager.voteComment(body.get("username"), id,
                                    commentId, Integer.parseInt(body.get("vote")));
    }

    @GetMapping("/{id}/suggestions")
    public ArrayList<Product> getSuggestions(@PathVariable int id) {
        return productManager.findProductsById(id).getSuggestedProducts();
    }
}
