import React, { useState, useContext } from "react";
import { Link, useLocation } from "react-router-dom";
import { UserContext } from "../App";

function Navbar() {
  const UserInfo = useContext(UserContext);

  const location = useLocation();
  return (
      <nav className="navbar" fixed="top" expand="md">
        <div className="col-md-3 col-12">
          <Link to="/">
            <img src="image/logo.png" className="logo-img-baloot" alt="logo" />
          </Link>
          <div className="header-title-baloot">Baloot</div>
        </div>
        <div className="col-md-6 col-12"></div>
        {!UserInfo.loggedIn ? (
          <>
            <Link className="col-md-1 col-5" to="/Register">
              <button className="register-btn btn">Register</button>
            </Link>
            <Link className="col-md-1 col-5" to="/Login">
              <button className="login-btn btn">Login</button>
            </Link>
          </>
        ) : (
          <>
            <Link className="col-md-1 col-5" to="/Profile">
              <div className="profile-text">#{UserInfo.username}</div>
            </Link>
            <Link className="col-md-1 col-5" to="/Profile">
              <button className="d-flex justify-content-between btn cart-btn">
                <div className="d-inline-flex" style={{ marginRight: "20px" }}>
                  cart
                </div>
                <div className="d-inline-flex">{UserInfo.cardCount}</div>
              </button>
            </Link>
          </>
        )}
      </nav>
  );
}

export default Navbar;
