import React, { useState } from "react";
import "./App.css";
import Navbar from "./components/Navbar";
import Carousel from "./components/Carousel";
import CardActions from "./components/CardActions";
import Footer from "./components/footer";

// Main App Component
const App = () => {
  // Data for components
  const navItems = [
    "Accounts & Deposits",
    "About Us",
    "Cards",
    "Invest",
    "Loans",
    "Help",
    "Services",
  ];

  const carouselSlides = [
    {
      badge: "Premium Rewards Card",
      title: "Get welcome voucher worth INR 15,000",
      description:
        "Apply for our Premium Rewards Credit Card and unlock exclusive benefits including airport lounge access, reward points, and cashback on all purchases.",
      primaryButton: "Apply Now",
      secondaryButton: "Manage cookies or not",
      image:
        "https://images.pexels.com/photos/4386378/pexels-photo-4386378.jpeg?auto=compress&w=300&h=170&fit=crop",
      imageAlt: "Rewards Card",
    },
    {
      badge: "Travel Benefits",
      title: "Complimentary Airport Lounge Access",
      description:
        "Enjoy free lounge access at airports worldwide when you use your Premium Rewards Credit Card. Relax and travel in style!",
      primaryButton: "Explore Benefits",
      secondaryButton: "Terms & Conditions",
      image:
        "https://images.pexels.com/photos/1059383/pexels-photo-1059383.jpeg?auto=compress&w=300&h=170&fit=crop",
      imageAlt: "Lounge Access",
    },
  ];

  const quickActions = [
    {
      icon: "fa-solid fa-mobile-screen-button",
      title: "SC Mobile Key",
      description: "Secure digital authentication for seamless banking",
      buttonText: "Get Mobile Key",
      iconClass: "quick-mobile",
    },
    {
      icon: "fa-solid fa-clipboard-list",
      title: "Apply Now",
      description: "Quick and easy credit card application process",
      buttonText: "Start Application",
      iconClass: "quick-clipboard",
    },
    {
      icon: "fa-solid fa-question",
      title: "How to Apply",
      description: "Step-by-step guide to credit card applications",
      buttonText: "View Guide",
      iconClass: "quick-question",
    },
    {
      icon: "fa-solid fa-credit-card",
      title: "Types of Credit Cards",
      description: "Explore our complete range of credit cards",
      buttonText: "Compare Cards",
      iconClass: "quick-card",
    },
    {
      icon: "fa-solid fa-trophy",
      title: "Rewards & Benefits",
      description: "Discover exclusive privileges and rewards",
      buttonText: "Learn More",
      iconClass: "quick-trophy",
    },
    {
      icon: "fa-solid fa-phone-volume",
      title: "Customer Support",
      description: "24/7 assistance for all your banking needs",
      buttonText: "Contact Us",
      iconClass: "quick-phone",
    },
  ];

  const socialLinks = [
    { url: "#", icon: "fab fa-facebook-f" },
    { url: "#", icon: "fab fa-linkedin-in" },
    { url: "#", icon: "fab fa-instagram" },
  ];

  const footerSections = [
    {
      title: "About",
      links: [
        "About Us",
        "Our History",
        "Leadership Team",
        "Corporate Governance",
        "Awards & Recognition",
      ],
    },
    {
      title: "Bank With Us",
      links: [
        "Personal Banking",
        "Business Banking",
        "Corporate Banking",
        "Priority Banking",
        "Wealth Management",
      ],
    },
    {
      title: "Support",
      links: [
        "ATMs and Branches",
        "FAQs",
        "Forms and Downloads",
        "Security Tips",
        "Contact Us",
      ],
    },
    {
      title: "Insights",
      links: [
        "Market Insights",
        "Global Research",
        "News & Media",
        "Careers",
        "Sustainability",
      ],
    },
  ];

  // Event handlers
  const handleLogin = () => {
    alert("Login functionality would be implemented here");
  };

  return (
    <>
      {/* Include external stylesheets */}
      <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
        rel="stylesheet"
      />
      <link
        rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
      />

      <div className="App">
        <Navbar
          brandName="Standard Chartered"
          navItems={navItems}
          onLogin={handleLogin}
        />
        <Carousel slides={carouselSlides} />
        <CardActions
          title="Quick Actions"
          subtitle="Everything you need to manage your credit cards and banking services in one place"
          actions={quickActions}
        />
        <Footer
          socialLinks={socialLinks}
          footerSections={footerSections}
          companyInfo="© 2025 Standard Chartered Bank. All rights reserved."
        />
      </div>

      {/* Bootstrap JS */}
      <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </>
  );
};

export default App;
