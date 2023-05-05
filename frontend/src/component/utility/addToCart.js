import axios from "axios";
import Swal from "sweetalert2";

export const AddToCart = (username, productId) => {
    let url = "//localhost:8080/users/" + username + "/cart";
    axios.post(url, { "product-id": productId }).then(
      (response) => {
        console.log(response.data);
      },
      (error) => {
        Swal.fire({
          icon: "error",
          title: "Something went wrong",
          text: "Please repeat procedure!",
        });
      }
    );
  };
  