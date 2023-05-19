import React, { useEffect, useState, useContext } from "react";
import { Link, useParams } from "react-router-dom";
import axios from "axios";
import { AddToCart } from "./utility/addToCart";
import { UserContext } from "../App";

function Provider() {
  const { id } = useParams();
  const [provider, setProvider] = useState("");
  const UserInfo = useContext(UserContext);

  useEffect(() => {
    let url = "//localhost:8080/suppliers/" + String(id);
    axios
      .get(url)
      .then((response) => {
        console.log(response.data.products);
        setProvider(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [id]);

  function addToCart(productId){
    AddToCart(UserInfo.username, productId);
    UserInfo.setCardCount(UserInfo.cardCount + 1)
  }
  
  return (
    provider && <div className="container">
      <div className="provider-img-div">
        <img src= {provider.image} className="img-fluid provider-img" alt="provider"/>
      </div>
      <div className="row vh-40 suggestions">
        <p className="text-title-big">All provided commodities</p>
      </div>
      <div className="row">
        {provider.products.map((product) => {
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
                      src= {product.image}
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
      <footer>
          <span class="bold footer-text container-fluid">2023 @UT</span>
        </footer>
    </div>
  );
}

export default Provider;
