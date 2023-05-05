import React, { useContext, useState } from "react";
import { UserContext } from "../App";
import axios from "axios";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom"

function Login() {
  const [form, setForm] = useState({
    Username: "",
    Password: null,
  });

  const navigate = useNavigate()

  const UserInfo = useContext(UserContext);

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log(form.Username);
    console.log(form.Password);
    const params = { username: form.Username, password: form.Password };
    axios.post("//localhost:8080/login", params).then(
      (response) => {
        console.log(response);
        if (response.status === 200) {
          axios.get("//localhost:8080/users/" + String(form.Username)).then(
            (response) => {
              let info = response.data
              console.log(info)
              axios
              .get("//localhost:8080/users/" + String(form.Username) + "/cart")
              .then((response) => {
              console.log("//localhost:8080/users/" + String(form.Username) + "/cart")
              info['cardCount'] = 0; //response.data.no_items
                UserInfo.SetAllInfo(info);
                navigate("/")
              });
            },
            (error) => {
              Swal.fire({
                icon: error,
                title: "Something went wrong",
                text: "Please repeat procedure!",
              });
            }
          );
        }
      },
      (error) => {
        Swal.fire({
          icon: error,
          title: error.response.data.message.split(":")[1],
          text: "Please repeat procedure!",
        });
      }
    );
  };

  return (
    <div className="d-flex justfy-context-center align-items-center h-100 name">
      <form className="form-signin">
        <img
          src="image/logo.png"
          className="d-block mx-auto my-3"
          width="100px"
        />
        <h2 className="h3 font-weight-normal mb-3 text-center">Login</h2>

        <input
          className="form-control p-2"
          type="text"
          placeholder="Username"
          value={form.Username}
          onChange={(e) => setForm({ ...form, Username: e.target.value })}
        />
        <input
          className="form-control p-2 mb-3 mt-2"
          type="password"
          placeholder="Password"
          value={form.Password}
          onChange={(e) => setForm({ ...form, Password: e.target.value })}
        />
        <div className="checkbox text-center">
          <input
            type="checkbox"
            className="custom-control-input"
            id="rememberMe"
          />
          <label className="custom-control-label mb-3" for="remember">
            {" "}
            Remember me{" "}
          </label>
        </div>
        <div className="text-center">
          <button
            className="btn btn-lg btn-register"
            type="submit"
            onClick={handleSubmit}
          >
            Login
          </button>
        </div>
        <footer class="login-footer">
          <span class="bold footer-text container-fluid">2023 @UT</span>
        </footer>
      </form>
    </div>
  );
}

export default Login;
