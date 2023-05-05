import React, { useState, useContext, useEffect } from "react";
import AddCreditModals from "./Modals/addCreditModals";
import PayModal from "./Modals/PayModal";
import Form from "react-bootstrap/Form";
import { UserContext } from "../App";
import axios from "axios";
import { Link } from "react-router-dom";

function Profile() {
  const [showAddCreditModal, setCreditModal] = useState(false);
  const closeCreditModal = () => setCreditModal(false);

  const [showPayModal, setPayModal] = useState(false);
  const closePayModal = () => setPayModal(false);

  const [buyList, setBuyList] = useState();
  const [purchaseList, setPurchaseList] = useState();
  const [total, setTotal] = useState();
  const UserInfo = useContext(UserContext);

  const [refresh, setRefresh] = useState(0);

  useEffect(() => {
    let url = "//localhost:8080/users/" + String(UserInfo.username) + "/cart";
    axios
      .get(url)
      .then((response) => {
        console.log(response);
        console.log(response.data);
        setBuyList(response.data.buyList);
        setPurchaseList(response.data.purchaseList);
        setTotal(response.data.total);
        UserInfo.setCardCount(response.data.no_items);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [refresh]);

  function addProduct(num, productId) {
    console.log("hi");
    let url = "//localhost:8080/users/" + String(UserInfo.username) + "/cart/";
    if (num === 1)
      axios
        .post(url, { "product-id": productId })
        .then((response) => {
          console.log(response);
          console.log(response.data);
        })
        .catch((error) => {
          console.log(error);
        });
    else if (num === -1) {
      url += productId;
      axios
        .delete(url)
        .then((response) => {
          console.log(response);
          console.log(response.data);
        })
        .catch((error) => {
          console.log(error);
        });
    }
    setRefresh(1 - refresh);
  }

  const [Amount, setAmount] = useState("");

  return (
    <div className="profile bg-light">
      <div className="container">
        <div className="row profile">
          <div className="col-md-6">
            <div className="row">
              <p className="profile-text">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  fill="currentColor"
                  className="bi bi-person-fill"
                  viewBox="0 0 16 16"
                >
                  <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3Zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z" />
                </svg>
                {"  "}
                {UserInfo.username}
              </p>
            </div>
            <div className="row">
              <p className="profile-text">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  fill="currentColor"
                  className="bi bi-envelope-fill"
                  viewBox="0 0 16 16"
                >
                  <path d="M.05 3.555A2 2 0 0 1 2 2h12a2 2 0 0 1 1.95 1.555L8 8.414.05 3.555ZM0 4.697v7.104l5.803-3.558L0 4.697ZM6.761 8.83l-6.57 4.027A2 2 0 0 0 2 14h12a2 2 0 0 0 1.808-1.144l-6.57-4.027L8 9.586l-1.239-.757Zm3.436-.586L16 11.801V4.697l-5.803 3.546Z" />
                </svg>
                {"  "}
                {UserInfo.email}
              </p>
            </div>
            <div className="row">
              <p className="profile-text">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  fill="currentColor"
                  className="bi bi-calendar"
                  viewBox="0 0 16 16"
                >
                  <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5zM1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4H1z" />
                </svg>
                {"  "}
                {UserInfo.birthDate}
              </p>
            </div>
            <div className="row">
              <p className="profile-text">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  fill="currentColor"
                  className="bi bi-geo-alt-fill"
                  viewBox="0 0 16 16"
                >
                  <path d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10zm0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6z" />
                </svg>
                {"  "}
                {UserInfo.address}
              </p>
            </div>
            <div className="row">
              <button
                className="btn logout-btn add-credit-modal-btn"
                onClick={() => UserInfo.setLoggedIn(false)}
              >
                {" "}
                Logout{" "}
              </button>
            </div>
          </div>
          <div className="col-md-6">
            <div className="row">
              <div className="profile-credit-number">{UserInfo.credit}</div>
            </div>
            <div className="row amount-box">
              <Form.Control
                type="text"
                placeholder="$Amount"
                className="no-outline"
                value={Amount}
                onChange={(event) => setAmount(event.target.value)}
              />
              <AddCreditModals
                show={showAddCreditModal}
                closeModal={closeCreditModal}
                amount={Amount}
                setAmount={setAmount}
              />
            </div>
            <div className="row">
              <button
                className="btn add-credit-btn"
                onClick={() => setCreditModal(true)}
              >
                Add More Credit
              </button>
            </div>
          </div>
        </div>
        <div className="cart-row">
          <div className="row">
            <p className="cart-text">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="25"
                height="25"
                fill="currentColor"
                className="bi bi-cart"
                viewBox="0 0 16 16"
              >
                <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM3.102 4l1.313 7h8.17l1.313-7H3.102zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z" />
              </svg>
              Cart
            </p>
          </div>
          <div className="row">
            <table className="table table-borderless">
              <thead>
                <tr>
                  <th>Image</th>
                  <th>Name</th>
                  <th>Categories</th>
                  <th>Price</th>
                  <th>Provider ID</th>
                  <th>Rating</th>
                  <th>In Stock</th>
                  <th>In Cart</th>
                </tr>
              </thead>
              <tbody>
                {buyList &&
                  buyList.map((buyListItem) => {
                    return (
                      <tr className="buy-list-table-color">
                        <td className="t-img">
                          <Link to={"/Products/" + buyListItem.product.id}>
                            <img
                              src={buyListItem.product.image}
                              alt="profile"
                              width="80"
                              height="80"
                            />
                          </Link>
                        </td>
                        <td>{buyListItem.product.name}</td>
                        <td>
                          {buyListItem.product.category &&
                            buyListItem.product.category.map((c) => c)}
                        </td>
                        <td>${buyListItem.product.price}</td>
                        <td>{buyListItem.product.providerId}</td>
                        <td className="text-warning">
                          {buyListItem.product.rating}
                        </td>
                        <td className="text-danger">
                          {buyListItem.product.inStock}
                        </td>
                        <td className="inc-dec-td">
                          <div className="inc-dec-btn input-group d-flex justify-content-between">
                            <input
                              type="button"
                              value="-"
                              className="button-minus"
                              onClick={() =>
                                addProduct(-1, buyListItem.product.id)
                              }
                            />
                            <input
                              type="text"
                              placeholder={buyListItem.quantity}
                              name="quantity"
                              className="quantity-input text-center w-25"
                            />
                            <input
                              type="button"
                              value="+"
                              className="button-plus"
                              onClick={() =>
                                addProduct(1, buyListItem.product.id)
                              }
                            />
                          </div>
                        </td>
                      </tr>
                    );
                  })}
              </tbody>
            </table>
          </div>
          <div className="row">
            <PayModal
              total={total}
              setTotal={setTotal}
              buyList={buyList}
              show={showPayModal}
              closeModal={closePayModal}
              buy={refresh}
              setBuy={setRefresh}
            />
            <div className="text-center">
              <button
                className="btn add-credit-btn w-50"
                onClick={() => setPayModal(true)}
              >
                Pay now!
              </button>
            </div>
          </div>
          <div className="row">
            <p className="cart-text">
              <i className="fa fa-history"></i>
              History
            </p>
          </div>
          <div className="row">
            <div className="row">
              <table className="table table-borderless">
                <thead>
                  <tr className="buy-list-table-color">
                    <th className="t-img">Image</th>
                    <th>Name</th>
                    <th>Categories</th>
                    <th>Price</th>
                    <th>Provider ID</th>
                    <th>Rating</th>
                    <th>In Stock</th>
                    <th>Quantity</th>
                  </tr>
                </thead>
                {/* {console.log("pu " + JSON.stringify(purchaseList))}
                {console.log("bu " + JSON.stringify(buyList))} */}
                <tbody>
                  {purchaseList &&
                    purchaseList.map((purchaseListItem) => {
                      return (
                        <tr className="buy-list-table-color">
                          <td className="t-img">
                            <Link to={"/Products/" + purchaseListItem.product.id}>
                              <img
                                src={purchaseListItem.product.image}
                                width="100"
                                height="100"
                              />
                            </Link>
                          </td>
                          <td>{purchaseListItem.product.name}</td>
                          <td>
                            {purchaseListItem.product.category &&
                              purchaseListItem.product.category.map((c) => c)}
                          </td>
                          <td>${purchaseListItem.product.price}</td>
                          <td>{purchaseListItem.product.providerId}</td>
                          <td className="text-warning">
                            {purchaseListItem.product.rating}
                          </td>
                          <td className="text-danger">
                            {purchaseListItem.product.inStock}
                          </td>
                          <td>{purchaseListItem.quantity}</td>
                        </tr>
                      );
                    })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <footer>
        <span class="bold footer-text container-fluid">2023 @UT</span>
      </footer>
    </div>
  );
}

export default Profile;
