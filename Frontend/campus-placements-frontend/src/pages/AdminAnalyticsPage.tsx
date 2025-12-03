import { useEffect, useMemo, useState } from "react";
import "../styles/adminAnalytics.css";
import {
  fetchAdminAnalyticsOverview,
} from "../api/adminAnalytics";

import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  CartesianGrid,
  ResponsiveContainer,
} from "recharts";
import { AdminAnalyticsOverview, AdminCompanyAnalytics } from "types/adminAnalytics";

const PRIMARY = "#2563eb";
const SECONDARY = "#22c55e";

export default function AdminAnalyticsPage() {
  const [overview, setOverview] = useState<AdminAnalyticsOverview | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        setError(null);
        const res = await fetchAdminAnalyticsOverview();
        setOverview(res);
      } catch (err) {
        console.error(err);
        setError("Failed to load admin analytics.");
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const companies: AdminCompanyAnalytics[] = overview?.companies ?? [];
  const hasData = companies.length > 0;

  const registrationsChartData = useMemo(
    () =>
      companies.map((c) => ({
        companyName: c.companyName,
        registeredStudents: c.registeredStudents,
      })),
    [companies]
  );

  const passRateChartData = useMemo(
    () =>
      companies.map((c) => ({
        companyName: c.companyName,
        passRate50Percent: Math.round(c.passRate50Percent),
        avgScorePercent: Math.round(c.avgScorePercent),
      })),
    [companies]
  );

  return (
    <div className="admin-analytics-page">
      <header className="admin-analytics-header">
        <h1>Student Analytics – Admin View</h1>
        <p className="admin-analytics-subtitle">
          Overview of student registrations and exam performance across all companies.
        </p>
      </header>

      {loading && <div className="admin-analytics-state">Loading…</div>}
      {error && <div className="admin-analytics-error">{error}</div>}

      {!loading && !error && overview && (
        <>
          <section className="admin-analytics-summary-grid">
            <div className="admin-analytics-card">
              <p className="admin-analytics-card-label">Students registered</p>
              <p className="admin-analytics-card-value">
                {overview.totalStudents}
              </p>
            </div>
            <div className="admin-analytics-card">
              <p className="admin-analytics-card-label">Companies in system</p>
              <p className="admin-analytics-card-value">
                {overview.totalCompanies}
              </p>
            </div>
            <div className="admin-analytics-card">
              <p className="admin-analytics-card-label">Quizzes created</p>
              <p className="admin-analytics-card-value">
                {overview.totalQuizzes}
              </p>
            </div>
            <div className="admin-analytics-card">
              <p className="admin-analytics-card-label">Total exam attempts</p>
              <p className="admin-analytics-card-value">
                {overview.totalAttempts}
              </p>
            </div>
          </section>

          {!hasData && (
            <div className="admin-analytics-state">
              No companies or exam attempts found yet.
            </div>
          )}

          {hasData && (
            <>
              <section className="admin-analytics-section">
                <div className="admin-analytics-section-head">
                  <h2>Company-level registrations</h2>
                  <p>
                    How many distinct students have registered for each company.
                  </p>
                </div>
                <div className="admin-analytics-chart-wrapper">
                  <ResponsiveContainer width="100%" height={260}>
                    <BarChart
                      data={registrationsChartData}
                      margin={{ top: 10, right: 20, left: 0, bottom: 40 }}
                    >
                      <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
                      <XAxis
                        dataKey="companyName"
                        tick={{ fill: "#6b7280", fontSize: 12 }}
                        axisLine={{ stroke: "#e5e7eb" }}
                        tickLine={{ stroke: "#e5e7eb" }}
                        height={40}
                      />
                      <YAxis
                        tick={{ fill: "#6b7280", fontSize: 12 }}
                        axisLine={{ stroke: "#e5e7eb" }}
                        tickLine={{ stroke: "#e5e7eb" }}
                      />
                      <Tooltip />
                      <Legend />
                      <Bar
                        dataKey="registeredStudents"
                        name="Registered students"
                        fill={PRIMARY}
                        radius={[4, 4, 0, 0]}
                      />
                    </BarChart>
                  </ResponsiveContainer>
                </div>
              </section>
              <section className="admin-analytics-section">
                <div className="admin-analytics-section-head">
                  <h2>Exam performance by company</h2>
                  <p>
                    Shows how often students score at least 50% and the average score
                    across all attempts for each company.
                  </p>
                </div>
                <div className="admin-analytics-chart-wrapper">
                  <ResponsiveContainer width="100%" height={260}>
                    <BarChart
                      data={passRateChartData}
                      margin={{ top: 10, right: 20, left: 0, bottom: 40 }}
                    >
                      <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
                      <XAxis
                        dataKey="companyName"
                        tick={{ fill: "#6b7280", fontSize: 12 }}
                        axisLine={{ stroke: "#e5e7eb" }}
                        tickLine={{ stroke: "#e5e7eb" }}
                        height={40}
                      />
                      <YAxis
                        domain={[0, 100]}
                        tickFormatter={(v) => `${v}%`}
                        tick={{ fill: "#6b7280", fontSize: 12 }}
                        axisLine={{ stroke: "#e5e7eb" }}
                        tickLine={{ stroke: "#e5e7eb" }}
                      />
                      <Tooltip formatter={(v: any) => `${v}%`} />
                      <Legend />
                      <Bar
                        dataKey="passRate50Percent"
                        name="Pass rate (≥ 50%)"
                        fill={PRIMARY}
                        radius={[4, 4, 0, 0]}
                      />
                      <Bar
                        dataKey="avgScorePercent"
                        name="Average score %"
                        fill={SECONDARY}
                        radius={[4, 4, 0, 0]}
                      />
                    </BarChart>
                  </ResponsiveContainer>
                </div>
              </section>
              <section className="admin-analytics-section">
                <div className="admin-analytics-section-head">
                  <h2>Company details</h2>
                </div>
                <div className="admin-analytics-table-wrapper">
                  <table className="admin-analytics-table">
                    <thead>
                      <tr>
                        <th>Company</th>
                        <th>Registered students</th>
                        <th>Total attempts</th>
                        <th>Avg. score</th>
                        <th>Pass rate ≥ 50%</th>
                      </tr>
                    </thead>
                    <tbody>
                      {companies.map((c) => (
                        <tr key={c.companyId}>
                          <td>{c.companyName}</td>
                          <td>{c.registeredStudents}</td>
                          <td>{c.totalAttempts}</td>
                          <td>{Math.round(c.avgScorePercent)}%</td>
                          <td>{Math.round(c.passRate50Percent)}%</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </section>
            </>
          )}
        </>
      )}
    </div>
  );
}
