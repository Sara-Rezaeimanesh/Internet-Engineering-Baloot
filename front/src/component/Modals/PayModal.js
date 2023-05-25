import React, {useContext, useState} from "react";
import { Row, Col, Button, Modal, Form } from "react-bootstrap";
import axios from "axios";
import { UserContext } from "../../App";
import Swal from "sweetalert2";

function PayModal({ total, setTotal, buyList, show, closeModal, buy, setBuy}) {
  const [discount, setDiscount] = useState("");
  const UserInfo = useContext(UserContext);

  function applyDiscount(){
    console.log(discount)
    let url = "//localhost:8080/users/" + String(UserInfo.username) + "/discounts";
    axios
    .post(url,  {"discount-id": discount})
    .then((response) => {
      console.log(response);
      console.log(response.data);
      setTotal(Number(response.data));
      setBuy(1-buy);
    }).catch((error) => {
      Swal.fire({
        icon: "error",
        title: error.response.data.message.split(":")[1],
        text: "Please repeat procedure!",
      });
    });  
  }

  function buyProduct(){//POST /users/{id}/discounts
    let url = "//localhost:8080/users/"+ String(UserInfo.username) + "/cart/buy";
    console.log(url)
    axios.post(url)
    .then((response) => {
      console.log(response);
      console.log(response.data);
      UserInfo.setCredit(Number(UserInfo.credit)-Number(total));
      UserInfo.setCardCount(0);
      setBuy(1-buy);
      closeModal();
    })
    .catch((error) => {
      console.log(error);
      // Swal.fire({
      //   icon: "error",
      //   title: error.response.data.message.split(":")[1],
      //   text: "Please repeat procedure!",
      // });
    });
  }

  const listItems = buyList && buyList.map((myList) => {
    return (
      <li>
        <div class="d-flex justify-content-between">
          <div style={{fontSize: "18px"}}>{myList.product.name} × {myList.quantity}</div>
          <div style={{fontSize: "18px", color:"#A97B47"}}>{myList.product.price}$</div>
        </div>
      </li>
    );
  });

  return (
    <Modal show={show} onHide={closeModal}>
      <Modal.Header closeButton>
        <Modal.Title>Your cart</Modal.Title>
      </Modal.Header>

      <Modal.Body style={{ alignItems: "center" }}>
        <div className="pay-madal-content">
          <Row>
            <ul className="pay-list-item"> {listItems} </ul>
          </Row>
          <Row className="mt-3">
            <Col xs={7}>
              <Form.Control type="text" className="pay-modal-input" value={discount} onChange={(e) => setDiscount(e.target.value)}/>
            </Col>
            <Col xs={5}>
              <Button className="add-credit-modal-btn submit-modal-btn" onClick = {() => applyDiscount()}>submit</Button>
            </Col>
          </Row>
          <Row className="mt-4">
            <Col className="d-flex justify-content-between">
              <div style={{fontSize:"18px", fontWeight: "600"}}>Total</div>
              <div style={{color:"red", fontSize:"18px", fontWeight: "600"}}>{total}$</div>
            </Col>
          </Row>
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Col xs={{ span: 3 }}>
          <Button className="close-btn" onClick={closeModal}>
            Close
          </Button>
        </Col>
        <Col xs={{ span: 3 }}>
          <Button className="btn-light btn-lg add-credit-modal-btn" onClick={() => buyProduct()}>
            Buy!
          </Button>{" "}
        </Col>
      </Modal.Footer>
    </Modal>
  );
}

export default PayModal;
