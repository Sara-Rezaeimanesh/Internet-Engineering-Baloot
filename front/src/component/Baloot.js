import React, { useEffect, useState, useContext} from "react";
import { Link } from "react-router-dom";
import { Pagination } from "react-bootstrap";
import axios from "axios";
import { AddToCart } from "./utility/addToCart";
import { UserContext } from "../App";
import Swal from "sweetalert2";

function Baloot() {
  const [products, setProducts] = useState();
  const [filterByRating, setfilterByRating] = useState(true);

  const [searchType, setSearchType] = useState(0);
  const [searchWord, setSearchWord] = useState("");
  const [page, setPage] = useState(0);
  const UserInfo = useContext(UserContext);

  let items = [];
  for (let number = 1; products && number <= parseInt(products.resultsSize / 12) + 1; number++) {
    items.push(<Pagination.Item key={number} onClick={() => setPage(number-1)}>{number}</Pagination.Item>);
  }

  const [switch1, setSwitch] = useState(false);

  useEffect(() => {
    let url = "//localhost:8080/products?";
    if (Number(searchType) === 0) url += "name=" + searchWord;
    else if (Number(searchType) === 1) url += "category=" + searchWord;
    else if (Number(searchType) === 2) url += "provider=" + searchWord;
    url += "&" + (filterByRating ? "sort=rate"  : "sort=price");
    url += "&" + ("apply=" + String(1));
    url += "&" + ("page=" + String(page));
    url += (switch1) ? ("&" + ("available=1")) : "";

    console.log(url);
    axios.get(url).then((response) => {
      console.log(response.data);
      setProducts(response.data);
    });
  }, [page, switch1]);

  function changeProductOrder(e, productOrder) {
    setfilterByRating(productOrder);
    searchByFilter(e, 1, productOrder);
  }

  function searchByFilter(e, apply = 1, productOrder = true, available = 0) {
    e.preventDefault();
    console.log("Im here " + searchType + " ! " + searchWord + "-" + available);
    let url = "//localhost:8080/products?";
    if (Number(searchType) === 0) url += "name=" + searchWord;
    else if (Number(searchType) === 1) url += "category=" + searchWord;
    else if (Number(searchType) === 2) url += "provider=" + searchWord;
    url += "&" + (productOrder ? "sort=rate"  : "sort=price");
    url += "&" + ("apply=" + String(apply));
    url += "&" + ("page=" + String(page));
    url += (available) ? ("&" + ("available=1")) : "";
    console.log(url);
    axios.get(url).then((response) => {
      console.log(response.data);
      setProducts(response.data);
    }).catch((error) => {
      Swal.fire({
        icon: "error",
        title: error.response.data.message.split(":")[1],
        text: "Please repeat procedure!",
      });
    });
  }

  function addToCart(productId){
    AddToCart(UserInfo.username, productId);
    UserInfo.setCardCount(UserInfo.cardCount + 1)
  }


  return (
    products && (
      <div className="main">
        <div className="container">
          <div class="filter-padding">
            <div className="row filter">
              <div className="col-md-8">
                <div className="row">
                  <div className="filter-text d-flex">
                    <p>Available commodities</p>
                    <div className="form-check form-switch">
                      <input
                        className="form-check-input"
                        type="checkbox"
                        role="switch"
                        value={switch1}
                        onChange={(e) => setSwitch(e.target.checked)}
                      />
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-md-4">
                <div className="row d-flex justify-content-between">
                  <div className="col-md-4 filter-text">Sort by</div>
                  <button
                    className={
                      "col-md-4 btn " +
                      (filterByRating ? "filter-btn" : "unfilter-btn")
                    }
                    onClick={(e) => changeProductOrder(e, true)}
                  >
                    rating
                  </button>
                  <button
                    className={
                      "col-md-4 btn " +
                      (!filterByRating ? "filter-btn" : "unfilter-btn")
                    }
                    onClick={(e) => changeProductOrder(e, false)}
                  >
                    price
                  </button>
                </div>
              </div>
            </div>
          </div>
          <form className="col-md-6 col-12 form-search search-group">
            <div className="row input-group d-flex justify-content-center">
              <div className="col-md-3 col-6 search-select">
                <select
                  className="form-select"
                  aria-label="example"
                  onChange={(e) => setSearchType(e.target.value)}
                  value={searchType}
                >
                  <option value="0">name</option>
                  <option value="1">category</option>
                  <option value="2">provider</option>
                </select>
              </div>
              <div className="col-md-6 col-6 search-text">
                <input
                  className="form-control custom-search-input"
                  type="search"
                  placeholder="search your product ..."
                  aria-label="Search"
                  value={searchWord}
                  onChange={(e) => setSearchWord(e.target.value)}
                />
                <button
                  className="btn btn-primary custom-search-botton"
                  type="submit"
                  onClick={(e) => searchByFilter(e)}
                >
                  <img
                    src="image/Vector.png"
                    alt="search"
                    className="img-search-btn"
                  />
                </button>
              </div>
            </div>
          </form>
          <div className="album py-2 bg-light">
            <div className="container">
              <div className="row">
                {products.products.map((product) => {
                  return (
                    <div className="col-md-3">
                      <div className="card mb-4 box-shadow">
                        <div className="card-body">
                          <div>{product.name}</div>
                          <small className="text-danger">
                            {product.inStock} left in stock
                          </small>
                          <Link to={"/Products/" + String(product.id)}>
                            <img
                              className="card-img-top"
                              src={product.image}
                              alt="card"
                              height={"160"}
                            />
                          </Link>
                          <div className="d-flex justify-content-between align-items-center">
                            <div className="col-md-6 text-muted cart-price">
                              {product.price} $
                            </div>
                            <button
                              type="button"
                              className="col-md-6 btn btn-sm btn-outline-secondary add-to-cart-btn"
                              onClick={() => addToCart(product.id)}
                            >
                              add to cart
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
          <div className="pgination-class">
            <Pagination>{items}</Pagination>
          </div>
        </div>
        <footer>
          <span className="bold footer-text container-fluid">2023 @UT</span>
        </footer>
      </div>
    )
  );
}

export default Baloot;
