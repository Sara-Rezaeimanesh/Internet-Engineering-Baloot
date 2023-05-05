import React, { useState, useContext } from "react";
import { UserContext } from "../App";
import axios from "axios";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom"

function Register() {
  const [form, setForm] = useState({
    username: "",
    birthDate: null,
    password: null,
    email: null,
    address: null,
    credit: 0,
    cardCount : 0,
  });

  const UserInfo = useContext(UserContext);
  const navigate = useNavigate()

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log(form);

    const params = {
      username: form.username,
      password: form.password,
      email: form.email,
      birthDate: form.birthDate,
      address: form.address,
    };

    axios
      .post("//localhost:8080/signup", params)
      .then((response) => {
        console.log(response.data);
        UserInfo.SetAllInfo(form);
        navigate("/")
      })
      .catch((error) => {
        Swal.fire({
          icon: "error",
          title: "Something went wrong",
          text: "Please repeat procedure!",
        });
      });
  };

  return (
    <div>
      <div className="container d-flex justify-content-center align-items-center register-container">
        <form className="d-flex flex-column justfy-context-center">
          <img
            src="image/logo.png"
            className="d-block mx-auto my-3"
            width="100px"
          />
          <h2 className="h3 font-weight-normal mb-4 text-center signup-title">
            Sign up to Baloot!
          </h2>
          <div className="row mb-3 form-item">
            <div className="col col-12 col-md-6">
              <div className="form-outline">
                <input
                  type="text"
                  id="form6Example1"
                  className="form-control register-input"
                  placeholder="username"
                  value={form.username}
                  onChange={(e) =>
                    setForm({ ...form, username: e.target.value })
                  }
                  required
                />
              </div>
            </div>
            <div className="col col-12 col-md-6">
              <div className="form-outline pass-input">
                <input
                  type="text"
                  id="form6Example2"
                  className="form-control register-input"
                  placeholder="password"
                  value={form.password}
                  onChange={(e) =>
                    setForm({ ...form, password: e.target.value })
                  }
                  required
                />
              </div>
            </div>
          </div>
          <div className="form-outline mb-3 form-item">
            <input
              type="email"
              id="form6Example5"
              className="form-control register-input"
              placeholder="email"
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
              required
            />
          </div>
          <div className="form-outline mb-3 form-item">
            <input
              id="startDate"
              className="form-control register-input"
              type="date"
              placeholder="Birth date"
              value={form.birthDate}
              onChange={(e) => setForm({ ...form, birthDate: e.target.value })}
              onfocus="(this.type='date')"
            />
          </div>
          <div className="form-outline mb-1 form-item">
            <textarea
              className="form-control"
              id="form6Example7"
              rows="4"
              placeholder="address"
              value={form.address}
              onChange={(e) => setForm({ ...form, address: e.target.value })}
            ></textarea>
            <label className="form-label" for="form6Example7"></label>
          </div>

          <button
            type="submit"
            className="btn btn-primary register-btn btn-block mb-4 form-item"
            onClick={handleSubmit}
          >
            Sign up
          </button>
          <div className="d-flex justify-content-center mb-3">
            <p>
              Already have an accouunt? <a href="#">Login</a>
            </p>
          </div>
        </form>
      </div>
      <footer>
        <span class="bold footer-text container-fluid">2023 @UT</span>
      </footer>
    </div>
  );
}

export default Register;
