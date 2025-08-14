// Quick Actions Component

const QuickActions = ({ actions, title, subtitle }) => {
  return (
    <div className="container mt-5 mb-5">
      <h3 className="mb-4 fw-semibold">{title}</h3>
      <p className="mb-4 text-muted" style={{ marginTop: "-14px" }}>
        {subtitle}
      </p>
      <div className="row row-cols-1 row-cols-md-3 g-4 quick-actions">
        {actions.map((action, index) => (
          <QuickActionCard
            key={index}
            icon={action.icon}
            title={action.title}
            description={action.description}
            buttonText={action.buttonText}
            iconClass={action.iconClass}
          />
        ))}
      </div>
    </div>
  );
};

export default QuickActions;
