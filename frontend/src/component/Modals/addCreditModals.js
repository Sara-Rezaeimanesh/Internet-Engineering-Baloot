import React, { useContext } from "react";
import { Row, Col, Button, Modal } from "react-bootstrap";
import axios from "axios";
import Swal from "sweetalert2";
import { UserContext } from "../../App";

function AddCreditModals({ show, closeModal, amount, setAmount }) {

  const UserInfo = useContext(UserContext);

  function incCredit(amount) {///users/{id}/credit?amount=1000
    axios.put("//localhost:8080/users/" + UserInfo.username + "/credit", {amount: amount}).then(
      (response) => {
        UserInfo.setCredit(Number(amount) + Number(UserInfo.credit));
        setAmount("");
        closeModal();        
      })
      .catch((error) => {
        Swal.fire({
          icon: error,
          title: "Something went wrong",
          text: "Please repeat procedure!",
        });
      }
    );
  }

  return (
    <Modal show={show} onHide={closeModal}>
      <Modal.Header closeButton>
        <Modal.Title>Add credit</Modal.Title>
      </Modal.Header>

      <Modal.Body style={{ alignItems: "center" }}>
        <Row>
          <Col className="modal-text">
            Are you sure you want to add {amount}$ to your account?
          </Col>
        </Row>
      </Modal.Body>
      <Modal.Footer>
        <Col xs={{ span: 3 }}>
          <Button className="close-btn" onClick={closeModal}>
            Close
          </Button>
        </Col>
        <Col xs={{ span: 3 }}>
          <Button
            className="add-credit-modal-btn"
            onClick={() => incCredit(amount)}
          >
            Confirm!
          </Button>{" "}
        </Col>
      </Modal.Footer>
    </Modal>
  );
}

export default AddCreditModals;
