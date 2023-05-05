package com.baloot.IE.Controller.Product;

import com.baloot.IE.domain.Product.Product;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchResult {
    private final List<Product> products;
    private final int resultsSize;

    public SearchResult(List<Product> products_, int size) {
        products = products_;
        resultsSize = size;
    }
}
