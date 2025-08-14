// Navbar Component
const Navbar = ({ brandName, navItems, onLogin }) => {
  return (
    <nav className="navbar navbar-expand-lg sc-navbar">
      <div className="container-fluid">
        <a className="navbar-brand" href="#">
          <img
            src="https://via.placeholder.com/32x32/22988c/ffffff?text=SC"
            alt="Logo"
          />
          {brandName}
        </a>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarContent"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div
          className="collapse navbar-collapse justify-content-center"
          id="navbarContent"
        >
          <ul className="navbar-nav mx-auto mb-2 mb-lg-0">
            {navItems.map((item, index) => (
              <li key={index} className="nav-item">
                <a className="nav-link" href="#">
                  {item}
                </a>
              </li>
            ))}
          </ul>
          <button onClick={onLogin} className="btn btn-login">
            Login
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
