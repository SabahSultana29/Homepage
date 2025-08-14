
const CardActions = ({ icon, title, description, buttonText, iconClass }) => {
  return (
    <div className="col">
      <div className="card h-100 p-3 text-center">
        <div className={`quick-icon-bg ${iconClass}`}>
          <i className={icon}></i>
        </div>
        <h5 className="card-title">{title}</h5>
        <p className="card-text">{description}</p>
        <a href="#" className="btn btn-outline-primary btn-sm">
          {buttonText}
        </a>
      </div>
    </div>
  );
};
export default CardActions;
