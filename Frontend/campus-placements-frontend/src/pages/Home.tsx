import '../styles/home.css';
import heroImg from '../images/home-1.jpg';
import step1Img from '../images/home-step-1.webp';
import step2Img from '../images/home-step-2.jpg';
import step3Img from '../images/home-step-3.avif';


export default function Home() {
  return (
    <main className="home-page">
      <section className="home-hero">
        <div className="home-hero-inner">
          <div className="home-hero-text">
            <h1 className="home-title">Campus Placements</h1>
            <p className="home-subtitle">
              Connect students with top companies, explore opportunities, and streamline your placement process.
            </p>
          </div>

          <div className="home-hero-image">
            <img
              src={heroImg}
              alt="Campus placements illustration"
            />
          </div>
        </div>
      </section>
      <section className="home-steps">
        <div className="home-steps-inner">
          <h2 className="steps-title">How it works</h2>

          <div className="steps-grid">
            <article className="step-card">
              <div className="step-image">
                <img
                  src={step1Img}
                  alt="Step 1"
                />
              </div>
              <h3 className="step-heading">Step 1</h3>
              <p className="step-text">
                Students sign up, complete their profiles, and explore available companies.
              </p>
            </article>
            <article className="step-card">
              <div className="step-image">
                <img
                  src={step2Img}
                  alt="Step 2"
                />
              </div>
              <h3 className="step-heading">Step 2</h3>
              <p className="step-text">
                Practice questions, topics and download practice papers.
              </p>
            </article>
            <article className="step-card">
              <div className="step-image">
                <img
                  src={step3Img}
                  alt="Step 3"
                />
              </div>
              <h3 className="step-heading">Step 3</h3>
              <p className="step-text">
                Take practice quizzes to evaluate and see analytics.
              </p>
            </article>
          </div>
        </div>
      </section>
    </main>
  );
}
