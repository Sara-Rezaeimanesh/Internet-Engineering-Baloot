import { useState } from "react";

export const UserInfo = () => {
  const [username, setusername] = useState("");
  const [loggedIn, setLoggedIn] = useState(false);
  const [birthDate, setbirthDate] = useState("");
  const [email, setemail] = useState("");
  const [address, setAddress] = useState("");
  const [credit, setCredit] = useState("");
  const [cardCount, setCardCount] = useState("");
  
  const SetAllInfo = (user) => {
    console.log(user);
    setLoggedIn(true);
    setusername(user.username);
    setbirthDate(user.birthDate);
    setemail(user.email);
    setAddress(user.address);
    setCredit(user.credit);
    setCardCount(user.cardCount);
  };

  return {
    username,
    setusername, 
    SetAllInfo,
    loggedIn,
    setLoggedIn,
    setCredit,
    birthDate,
    email,
    address, 
    credit,
    setCardCount,
    cardCount
  };
};

export default UserInfo;
