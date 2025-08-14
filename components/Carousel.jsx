// Carousel Component
const Carousel = ({ slides }) => {
  const [activeSlide, setActiveSlide] = useState(0);

  const nextSlide = () => {
    setActiveSlide((prev) => (prev + 1) % slides.length);
  };

  const prevSlide = () => {
    setActiveSlide((prev) => (prev - 1 + slides.length) % slides.length);
  };

  const goToSlide = (index) => {
    setActiveSlide(index);
  };

  return (
    <div className="container">
      <div className="carousel slide carousel-bg">
        <div className="carousel-inner">
          {slides.map((slide, index) => (
            <div
              key={index}
              className={`carousel-item ${
                index === activeSlide ? "active" : ""
              }`}
            >
              <div className="row align-items-center">
                <div className="col-md-7 col-12">
                  <span className="badge bg-light text-primary mb-3 px-3 py-2">
                    {slide.badge}
                  </span>
                  <h2>{slide.title}</h2>
                  <p>{slide.description}</p>
                  <div className="mt-4 mb-5">
                    <button className="btn btn-light text-primary fw-bold me-2 px-4 py-2">
                      {slide.primaryButton}
                    </button>
                    <button
                      className="btn btn-outline-light fw-bold"
                      style={{ opacity: 0.6 }}
                    >
                      {slide.secondaryButton}
                    </button>
                  </div>
                </div>
                <div className="col-md-5 col-12 text-center">
                  <img
                    className="sc-hero-img"
                    src={slide.image}
                    alt={slide.imageAlt}
                  />
                </div>
              </div>
            </div>
          ))}
        </div>
        <button
          className="carousel-control-prev"
          type="button"
          onClick={prevSlide}
        >
          <span
            className="carousel-control-prev-icon"
            aria-hidden="true"
          ></span>
          <span className="visually-hidden">Previous</span>
        </button>
        <button
          className="carousel-control-next"
          type="button"
          onClick={nextSlide}
        >
          <span
            className="carousel-control-next-icon"
            aria-hidden="true"
          ></span>
          <span className="visually-hidden">Next</span>
        </button>
        <div className="carousel-indicators">
          {slides.map((_, index) => (
            <button
              key={index}
              type="button"
              onClick={() => goToSlide(index)}
              className={index === activeSlide ? "active" : ""}
              aria-current={index === activeSlide ? "true" : "false"}
              aria-label={`Slide ${index + 1}`}
            ></button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Carousel;
