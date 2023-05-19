import React, { useEffect, useState, useContext } from "react";
import { Link, useParams } from "react-router-dom";
import { Button } from "react-bootstrap";
import axios from "axios";
import Swal from "sweetalert2";
import { UserContext } from "../App";
import { AddToCart } from "./utility/addToCart";

function Product() {
  const { id } = useParams();
  const [product, setProduct] = useState();
  const [suggestedProducts, setSuggestedProducts] = useState();
  const [comments, setComments] = useState();
  const [commentText, setCommentText] = useState();
  const [score, setScore] = useState(0);
  const [providerName, setProviderName] = useState("");
  const UserInfo = useContext(UserContext);

  function getProduct() {
    var providerId = 0;
    let url = "//localhost:8080/products/" + String(id);
    axios
      .get(url)
      .then((response) => {
        console.log(response);
        console.log(response.data);
        setProduct(response.data);
        providerId = response.data.providerId;
        url = "//localhost:8080/suppliers/" + String(providerId);
        axios
          .get(url)
          .then((response) => {
            console.log(response.data);
            setProviderName(response.data.name);
          })
          .catch((error) => {
            Swal.fire({
              icon: "error",
              title: "Something went wrong",
              text: "Please repeat procedure!",
            });
          });
      })
      .catch((error) => {
        console.log(error);
      });

    url = "//localhost:8080/products/" + String(id) + "/comments";
    axios
      .get(url)
      .then((response) => {
        console.log(response.data);
        setComments(response.data);
      })
      .catch((error) => {
        console.log(error);
      });

    url = "//localhost:8080/products/" + String(id) + "/suggestions";
    axios
      .get(url)
      .then((response) => {
        console.log(response.data);
        setSuggestedProducts(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
    console.log(providerId);
  }

  useEffect(() => {
    console.log(id);
    setProduct(null);
    setComments(null);
    getProduct();
  }, [id]);

  function rateProduct(rating) {
    console.log(rating);
    let url = "//localhost:8080/products/" + String(id) + "/ratings";
    axios
      .post(url, { username: UserInfo.username, rate: rating })
      .then((response) => {
        console.log(response.data);
        if (response.status === 200) getProduct();
      })
      .catch((error) => {
        Swal.fire({
          icon: "error",
          title: "Something went wrong",
          text: "Please repeat procedure!",
        });
      });
  }

  function voteComment(vote, voteId) {
    console.log(vote, voteId);
    let url =
      "//localhost:8080/products/" +
      String(id) +
      "/comments/" +
      String(voteId) +
      "/vote";
    axios
      .put(url, { username: UserInfo.username, vote: vote })
      .then((response) => {
        console.log(response);
        if (response.status === 200) getProduct();
      })
      .catch((error) => {
        Swal.fire({
          icon: "error",
          title: "Something went wrong",
          text: "Please repeat procedure!",
        });
      });
  }

  function postComment(vote) {
    console.log(vote);
    let url = "//localhost:8080/products/" + String(id) + "/comments";
    axios
      .post(url, { username: UserInfo.username, comment: vote })
      .then((response) => {
        console.log(response.data);
        setCommentText("");
        if (response.status === 200) getProduct();
      })
      .catch((error) => {
        Swal.fire({
          icon: "error",
          title: "Something went wrong",
          text: "Please repeat procedure!",
        });
      });
  }

  function addToCart(productId){
    AddToCart(UserInfo.username, productId);
    UserInfo.setCardCount(UserInfo.cardCount + 1)
  }

  return (
    product &&
    comments && (
      <div>
        <div class="container product-card">
          <div class="h-100">
            <div class="h-50 row product-desc">
              <div class="h-100 col-md-6 overflow-none">
                <img
                  class="img-fluid product-pic"
                  alt="product"
                  src= {product.image}
                />
              </div>
              <div class="col-md-6 d-flex h-100 product-details">
                <p class="product-name">{product.name}</p>
                <div class="row">
                  <small class="text-danger col-md-8 col-sm-8 text-small bold">
                    {product.inStock} left in stock
                  </small>
                  <div class="col-md-4 col-sm-4 d-flex justify-content-center stars-div">
                    <img alt="product" src="image/star.png" class="star-img" />
                    <p class="stars d-flex flex-row align-items-end">
                      {product.rating}
                      <p class="text-secondary star-num">{product.num_rates}</p>
                    </p>
                  </div>
                  <small class="text-small bold">
                    by:{" "}
                    <Link to={"/Provider/" + String(product.providerId)}>
                      {providerName}
                    </Link>
                  </small>
                </div>
                <div class="category">
                  <p class="cat-p text-small">Category(s)</p>
                  <ul class="cat-ul text-small-li">
                    {product.categories.map((c) => (
                      <li>{c}</li>
                    ))}
                  </ul>
                </div>
                <div class="row buy d-flex justfy-context-center align-items-center">
                  <p class="price bold col-xs-3 col-md-8">{product.price}$</p>
                  <div class="col-xs-7 col-md-4 pl-0 pr-0 buy-btn-div d-flex justify-content-end">
                    <button
                      type="button"
                      class="btn btn-outline-dark buy-btn"
                      onClick={() => addToCart(product.id)}
                    >
                      add to cart
                    </button>
                  </div>
                </div>
                <div class="row rate d-flex align-items-center col-md-12">
                  <div class="ten-stars col-9">
                    <p class="text-small-li bold ten-stars-text">rate now</p>
                    <div class="rate">
                      <input
                        type="radio"
                        id="star10"
                        name="rate"
                        value="10"
                        onChange={(e) => setScore(e.target.value)}
                      />
                      <label for="star10" title="text">
                        10 stars
                      </label>
                      <input
                        type="radio"
                        id="star9"
                        name="rate"
                        value="9"
                        onChange={(e) => setScore(e.target.value)}
                      />
                      <label for="star9" title="text">
                        9 stars
                      </label>
                      <input
                        type="radio"
                        id="star8"
                        name="rate"
                        value="8"
                        onChange={(e) => setScore(e.target.value)}
                      />
                      <label for="star8" title="text">
                        8 stars
                      </label>
                      <input
                        type="radio"
                        id="star7"
                        name="rate"
                        value="7"
                        onChange={(e) => setScore(e.target.value)}
                      />
                      <label for="star7" title="text">
                        7 stars
                      </label>
                      <input
                        type="radio"
                        id="star6"
                        name="rate"
                        value="6"
                        onChange={(e) => setScore(e.target.value)}
                      />
                      <label for="star6" title="text">
                        6 star
                      </label>
                      <input
                        type="radio"
                        id="star5"
                        name="rate"
                        value="5"
                        onChange={(e) => setScore(e.target.value)}
                      />
                      <label for="star5" title="text">
                        5 stars
                      </label>
                      <input
                        type="radio"
                        id="star4"
                        name="rate"
                        value="4"
                        onChange={(e) => setScore(e.target.value)}
                      />
                      <label for="star4" title="text">
                        4 stars
                      </label>
                      <input
                        type="radio"
                        id="star3"
                        name="rate"
                        value="3"
                        onChange={(e) => setScore(e.target.value)}
                      />
                      <label for="star3" title="text">
                        3 stars
                      </label>
                      <input
                        type="radio"
                        id="star2"
                        name="rate"
                        value="2"
                        onChange={(e) => setScore(e.target.value)}
                      />
                      <label for="star2" title="text">
                        2 stars
                      </label>
                      <input
                        type="radio"
                        id="star1"
                        name="rate"
                        value="1"
                        onChange={(e) => setScore(e.target.value)}
                      />
                      <label for="star1" title="text">
                        1 star
                      </label>
                    </div>
                  </div>
                  <button
                    type="button"
                    class="btn btn-dark col-3 rate-btn"
                    onClick={() => rateProduct(score)}
                  >
                    submit
                  </button>
                </div>
              </div>
            </div>
            <div class="row h-50 comments">
              <div class="col-md-12 mt-4">
                <p class="text-title">
                  Comments{" "}
                  <small class="text-secondary">({comments.length})</small>
                </p>
                {comments.map((comment) => {
                  return (
                    <div class="comment-item d-flex flex-column">
                      <p class="bold mb-1 comment-item-text">{comment.text}</p>
                      <div class="d-flex">
                        <p class="text-secondary text-very-small">
                          {comment.date}
                        </p>
                        <p class="random-dot text-secondary">.</p>
                        <p class="text-secondary text-small-li">
                          {comment.userEmail}
                        </p>
                      </div>
                      <div class="d-flex justify-content-end align-items-center">
                        <p class="helpful">Is this comment helpful?</p>
                        <div class="d-flex align-items-center thumbs">
                          <p class="text-very-small">{comment.likes}</p>
                          <Button
                            className="thumbs-btn"
                            onClick={() => voteComment(1, comment.id)}
                          >
                            <img
                              alt="product"
                              src="image/thumbs up.png"
                              class="thumbs-img"
                            />
                          </Button>
                          <p class="text-very-small">{comment.dislikes}</p>
                          <Button
                            className="thumbs-btn"
                            onClick={() => voteComment(-1, comment.id)}
                          >
                            <img
                              alt="product"
                              src="image/thumbs down.png"
                              class="thumbs-img"
                            />
                          </Button>
                        </div>
                      </div>
                    </div>
                  );
                })}
                <div class="comment-item">
                  <div class="your-comment mt-2 d-flex flex-column">
                    <p class="text-title comment-submit-title">
                      Submit your opinion
                    </p>
                    <div class="d-flex align-items-end mt-3 w-100 submit-comment">
                      <textarea
                        class="form-control comment"
                        rows="3"
                        value={commentText}
                        onChange={(e) => setCommentText(e.target.value)}
                      ></textarea>
                      <div class="col-md-1 comment-btn-div">
                        <button
                          type="button"
                          class="btn btn-dark w-100 rate-btn comment-btn"
                          onClick={() => postComment(commentText)}
                        >
                          post
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row vh-40 suggestions">
                <p class="text-title-big">You might also like...</p>
                {suggestedProducts &&
                  suggestedProducts.slice(0, 4).map((suggestionProsuct) => {
                    return (
                      <div className="col-md-3">
                        <div className="card mb-4 box-shadow">
                          <div className="card-body">
                            <div>{suggestionProsuct.name}</div>
                            <small className="text-danger">
                              {suggestionProsuct.inStock} left in stock
                            </small>
                            <Link
                              to={"/Products/" + String(suggestionProsuct.id)}
                            >
                              <img
                                className="card-img-top"
                                src= {suggestionProsuct.image}
                                alt="card"
                                height={"160"}
                              />
                            </Link>
                            <div className="d-flex justify-content-between align-items-center">
                              <div className="col-md-6 text-muted cart-price">
                                {suggestionProsuct.price} $
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
        <footer>
          <span class="bold footer-text container-fluid">2023 @UT</span>
        </footer>
      </div>
    )
  );
}

export default Product;
