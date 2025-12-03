import "../styles/site.css";

export default function Footer() {
  const year = new Date().getFullYear();

  return (
    <footer className="footer">
      <div className="container footer-inner">
        <div className="footer-column footer-contact">

          <div className="footer-contact-item">
            <div className="footer-icon-circle">📍</div>
            <div>
              <div className="footer-contact-title">ISU - School of IT</div>
              <div className="footer-contact-subtitle">Normal, IL, USA</div>
            </div>
          </div>

          <div className="footer-contact-item">
            <div className="footer-icon-circle">📞</div>
            <div className="footer-contact-subtitle">+1 (555) 123-4567</div>
          </div>

          <div className="footer-contact-item">
            <div className="footer-icon-circle">✉️</div>
            <a
              href="mailto:support@campusplacements.com"
              className="footer-contact-link"
            >
              support@campusplacements.com
            </a>
          </div>

          <div className="footer-copy">
            © {year} Campus Placements. All rights reserved.
          </div>
        </div>
        <div className="footer-column footer-about">
          <h4 className="footer-heading">About the platform</h4>
          <p className="footer-text">
            Campus Placements helps students discover companies, practice
            role-based exams, and track their progress with real-time analytics.
          </p>

          <p className="footer-text">
            Admins can manage companies, exams, and student performance from a
            single dashboard.
          </p>

          <div className="footer-social">
            <a
              href="#"
              aria-label="Facebook"
              className="footer-social-icon"
            >
              f
            </a>
            <a
              href="#"
              aria-label="Twitter"
              className="footer-social-icon"
            >
              t
            </a>
            <a
              href="#"
              aria-label="LinkedIn"
              className="footer-social-icon"
            >
              in
            </a>
            <a
              href="#"
              aria-label="GitHub"
              className="footer-social-icon"
            >
              {`</>`}
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
}
