import logo from './logo.svg';
import './Startup.css';

function Startup() {
  return (
    <div className="Startup">
      <header className="Startup-header">
        <p>Test</p>
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          THIS IS STARTUP
        </p>
        <a
          className="Startup-link"
          href="./Startup.js"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default Startup;
