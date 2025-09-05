// import React, { useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import './CreditCardOffer.css';
// import logo from '../assets/creditcard.png';
// import free_button from '../assets/free_button.png';
// import lightning from '../assets/lightning.png';
// import gift_box from '../assets/gift_box.png';
// import phone from '../assets/phone.png';

// const CreditCardOfferCustomer = () => {
//   const navigate = useNavigate();
//   const [notification, setNotification] = useState({
//     show: false,
//     message: '',
//     type: ''
//   });

//   const handleAccept = () => {
//     setNotification({
//       show: true,
//       message: 'Offer Accepted Successfully',
//       type: 'success-banner'
//     });
//   };

//   const handleDecline = () => {
//     setNotification({
//       show: true,
//       message: 'Offer Declined Successfully',
//       type: 'decline-banner'
//     });
//   };

//   return (
//     <div className="offer-container">
//       <div className="offer-card">
//         <div className="header">
//           <h1 className="congratulations">Congratulations!</h1>
//           <p className="subtitle">
//             User, here is your <span className="highlight">lifetime-free</span> Standard Chartered<br />
//             Credit Card offer
//           </p>
//         </div>

//         <div className="content">
//           <div className="card-section">
//             <div className="credit-card-image">
//             <img
//               src={logo}
//               alt="Standard Chartered Platinum Credit Card"
//               className="card-img"

//             />
//             </div>
//           </div>

//           <div className="details-section">
//             <div className="credit-limit">
//               <h2>Credit Limit</h2>
//               <div className="amount">₹1,50,000</div>
//               <p className="availability">Available immediately upon approval</p>
//             </div>

//             <div className="interest-rate">
//               <h3>Interest Rate </h3>
//               <div className="rates">
//                 <div className="rate-item">
//                   <span className="rate-label">Starting 2.49% p.m.</span>
//                   <span className="rate-value">2.75%</span>
//                   <span className="rate-desc">per month</span>
//                   <small>*Interest charged only on outstanding balance</small>
//                 </div>
//                 <div className="rate-item special">
//                   <span className="rate-label">Special Offer</span>
//                   <span className="rate-value">33.00%</span>
//                   <span className="rate-desc">per annum</span>
//                 </div>
//               </div>
//             </div>

//             <div className="features">
//               <div className="feature-item">
//                 <img src={free_button} className="feature-icon" />
//                 <span>Lifetime free</span>
//               </div>
//               <div className="feature-item">
//               <img src={lightning} className="feature-icon" />
//                 <span>Instant Approval</span>
//               </div>
//               <div className="feature-item">
//               <img src={gift_box} className="feature-icon" />
//                 <span>Reward Points</span>
//               </div>
//               <div className="feature-item">
//               <img src={phone} className="feature-icon" />
//                 <span>Contactless Pay</span>
//               </div>
//             </div>
//           </div>
//         </div>

//         {notification.show && notification.type === 'success-banner' && (
//           <>
//             <div className="success-banner">
//               <h3 className="success-banner-title">{notification.message}</h3>
//             </div>
//             <div className="success-banner-actions">
//               <button
//                 className="success-banner-button"
//                 onClick={() => navigate('/home')}
//               >
//                 Go Back to Home
//               </button>
//             </div>
//           </>
//         )}

//         {notification.show && notification.type === 'decline-banner' && (
//           <>
//             <div className="decline-banner">
//               <h3 className="decline-banner-title">{notification.message}</h3>
//             </div>
//             <div className="decline-banner-actions">
//               <button
//                 className="decline-banner-button"
//                 onClick={() => navigate('/home')}
//               >
//                 Go Back to Home
//               </button>
//             </div>
//           </>
//         )}

//         {!(notification.show && (notification.type === 'success-banner' || notification.type === 'decline-banner')) && (
//           <div className="actions">
//             <button
//               className="accept-btn"
//               onClick={handleAccept}
//             >
//               Accept
//             </button>
//             <button
//               className="decline-btn"
//               onClick={handleDecline}
//               disabled={notification.show}
//             >
//               Decline
//             </button>
//           </div>
//         )}
//       </div>
//     </div>
//   );
// };

// export default CreditCardOfferCustomer;

//updated react code

import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios"; //added axios import
import "./CreditCardOffer.css";
import logo from "../assets/creditcard.png";
import free_button from "../assets/free_button.png";
import lightning from "../assets/lightning.png";
import gift_box from "../assets/gift_box.png";
import phone from "../assets/phone.png";

const CreditCardOfferCustomer = () => {
  const navigate = useNavigate();
  const [notification, setNotification] = useState({
    show: false,
    message: "",
    type: "",
  });

  // Accept offer -> call backend
  const handleAccept = async () => {
    try {
      const requestData = {
        offerId: 1, // you can set dynamically later
        offerName: "Platinum Card",
        description: "Lifetime free card with benefits",
        annualFee: 0,
        customerName: "John Doe", // ✅ replace with actual logged-in customer
        email: "john.doe@example.com",
        phone: "9876543210",
        cardNumber: "1234-5678-9876-5432",
      };

      const response = await axios.post(
        "http://localhost:8080/api/offers/accept",
        requestData
      );

      setNotification({
        show: true,
        message: response.data.message || "Offer Accepted Successfully",
        type: "success-banner",
      });
    } catch (error) {
      console.error("Error accepting offer:", error);
      setNotification({
        show: true,
        message: "Error while accepting offer",
        type: "decline-banner",
      });
    }
  };

  // Decline offer -> call backend
  const handleDecline = async () => {
    try {
      const offerId = 1; // replace with dynamic offerId
      const response = await axios.post(
        `http://localhost:8080/api/offers/decline/${offerId}`
      );

      setNotification({
        show: true,
        message: response.data || "Offer Declined Successfully",
        type: "decline-banner",
      });
    } catch (error) {
      console.error("Error declining offer:", error);
      setNotification({
        show: true,
        message: "Error while declining offer",
        type: "decline-banner",
      });
    }
  };

  return (
    <div className="offer-container">
      <div className="offer-card">
        <div className="header">
          <h1 className="congratulations">Congratulations!</h1>
          <p className="subtitle">
            User, here is your <span className="highlight">lifetime-free</span>{" "}
            Standard Chartered
            <br />
            Credit Card offer
          </p>
        </div>

        <div className="content">
          <div className="card-section">
            <div className="credit-card-image">
              <img
                src={logo}
                alt="Standard Chartered Platinum Credit Card"
                className="card-img"
              />
            </div>
          </div>

          <div className="details-section">
            <div className="credit-limit">
              <h2>Credit Limit</h2>
              <div className="amount">₹1,50,000</div>
              <p className="availability">
                Available immediately upon approval
              </p>
            </div>

            <div className="interest-rate">
              <h3>Interest Rate </h3>
              <div className="rates">
                <div className="rate-item">
                  <span className="rate-label">Starting 2.49% p.m.</span>
                  <span className="rate-value">2.75%</span>
                  <span className="rate-desc">per month</span>
                  <small>*Interest charged only on outstanding balance</small>
                </div>
                <div className="rate-item special">
                  <span className="rate-label">Special Offer</span>
                  <span className="rate-value">33.00%</span>
                  <span className="rate-desc">per annum</span>
                </div>
              </div>
            </div>

            <div className="features">
              <div className="feature-item">
                <img src={free_button} className="feature-icon" />
                <span>Lifetime free</span>
              </div>
              <div className="feature-item">
                <img src={lightning} className="feature-icon" />
                <span>Instant Approval</span>
              </div>
              <div className="feature-item">
                <img src={gift_box} className="feature-icon" />
                <span>Reward Points</span>
              </div>
              <div className="feature-item">
                <img src={phone} className="feature-icon" />
                <span>Contactless Pay</span>
              </div>
            </div>
          </div>
        </div>

        {notification.show && notification.type === "success-banner" && (
          <>
            <div className="success-banner">
              <h3 className="success-banner-title">{notification.message}</h3>
            </div>
            <div className="success-banner-actions">
              <button
                className="success-banner-button"
                onClick={() => navigate("/home")}
              >
                Go Back to Home
              </button>
            </div>
          </>
        )}

        {notification.show && notification.type === "decline-banner" && (
          <>
            <div className="decline-banner">
              <h3 className="decline-banner-title">{notification.message}</h3>
            </div>
            <div className="decline-banner-actions">
              <button
                className="decline-banner-button"
                onClick={() => navigate("/home")}
              >
                Go Back to Home
              </button>
            </div>
          </>
        )}

        {!(
          notification.show &&
          (notification.type === "success-banner" ||
            notification.type === "decline-banner")
        ) && (
          <div className="actions">
            <button className="accept-btn" onClick={handleAccept}>
              Accept
            </button>
            <button
              className="decline-btn"
              onClick={handleDecline}
              disabled={notification.show}
            >
              Decline
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default CreditCardOfferCustomer;
