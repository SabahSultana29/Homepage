import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import {
  Facebook,
  Instagram,
  Twitter,
  Linkedin,
  Youtube,
} from "react-bootstrap-icons";
// import logo from "../assets/logo.png"; // Adjust the path as necessary

export default function Footer() {
  return (
    <footer
      style={{ backgroundColor: "#001b56", color: "#fff", padding: "40px 0" }}
    >
      <Container>
        <Row className="align-items-start">
          {/* Logo and left links */}
          <Col md={4} className="mb-4">
            <img
              src={logo}
              alt="Standard Chartered"
              style={{ width: "180px", marginBottom: "20px" }}
            />
            <ul
              className="list-unstyled mb-0"
              style={{ fontSize: "14px", lineHeight: "2" }}
            >
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Accessibility
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Cookie policy
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Terms of use
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Privacy policy
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Modern slavery statement
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Regulatory disclosures
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Straight2Bank onboarding portal
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Our Code of Conduct and Ethics
                </a>
              </li>
            </ul>
          </Col>

          {/* Right link column */}
          <Col
            md={4}
            className="mb-4"
            style={{ fontSize: "14px", lineHeight: "2" }}
          >
            <ul className="list-unstyled mb-0">
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Online security
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Fighting financial crime
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Our suppliers
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  FAQs
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Our locations
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Contact us
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Sitemap
                </a>
              </li>
              <li>
                <a href="#" className="text-white text-decoration-none">
                  Manage cookies
                </a>
              </li>
            </ul>
          </Col>

          {/* Social and copyright */}
          <Col
            md={4}
            className="d-flex flex-column align-items-center justify-content-center text-center"
          >
            <div className="mb-3">
              <a href="#" className="text-white me-3">
                <Facebook size={20} />
              </a>
              <a href="#" className="text-white me-3">
                <Instagram size={20} />
              </a>
              <a href="#" className="text-white me-3">
                <Twitter size={20} />
              </a>
              <a href="#" className="text-white me-3">
                <Linkedin size={20} />
              </a>
              <a href="#" className="text-white">
                <Youtube size={20} />
              </a>
            </div>
            <div style={{ fontSize: "13px", color: "#ccc" }}>
              &copy; Standard Chartered {new Date().getFullYear()}.<br />
              All Rights Reserved.
            </div>
          </Col>
        </Row>
      </Container>
    </footer>
  );
}
