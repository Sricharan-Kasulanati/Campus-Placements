import '../styles/site.css';
export default function Footer() {
  return (
    <footer className="footer">
      <div className="container footer-inner">
        <div>© {new Date().getFullYear()} Campus Placements</div>
        <div></div>
      </div>
    </footer>
  );
}
