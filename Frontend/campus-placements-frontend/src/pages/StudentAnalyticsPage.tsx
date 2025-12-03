import { useEffect, useMemo, useState } from "react";
import "../styles/analytics.css";
import { fetchStudentAnalyticsOverview } from "../api/analytics";
import { fetchStudentAnalyticsOverviewForStudent } from "../api/adminAnalytics";

import {
  StudentAnalyticsOverview,
  CompanyAnalytics,
  QuizAnalytics,
} from "../types/analytics";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  CartesianGrid,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  PieLabelRenderProps,
} from "recharts";
import { useLocation } from "react-router-dom";

function useQuery() {
  const { search } = useLocation();
  return useMemo(() => new URLSearchParams(search), [search]);
}

type ViewLevel = "company" | "companyQuizzes" | "quizDistribution";
type ChartStyle = "bar" | "pie";

const PRIMARY = "#4f46e5";
const SECONDARY = "#22c55e";
const BUCKET_COLORS = ["#4f46e5", "#22c55e", "#f97316", "#06b6d4", "#a855f7"];

export default function StudentAnalyticsPage() {
  const [overview, setOverview] = useState<StudentAnalyticsOverview | null>(
    null
  );
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [viewLevel, setViewLevel] = useState<ViewLevel>("company");
  const [chartStyle, setChartStyle] = useState<ChartStyle>("bar");
  const [selectedCompanyId, setSelectedCompanyId] = useState<number | null>(
    null
  );
  const [selectedQuizId, setSelectedQuizId] = useState<number | null>(null);

  const query = useQuery();
  const studentIdFromQuery = query.get("studentId");
  const studentNameFromQuery = query.get("name");
  const studentId = studentIdFromQuery ? Number(studentIdFromQuery) : null;
  const isAdminViewingStudent = studentId != null && !Number.isNaN(studentId);

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        setError(null);

        let res: StudentAnalyticsOverview;
        if (studentId != null && !Number.isNaN(studentId)) {
          res = await fetchStudentAnalyticsOverviewForStudent(studentId);
        } else {
          res = await fetchStudentAnalyticsOverview();
        }

        setOverview(res);

        if (res.companies.length > 0) {
          const firstCompany = res.companies[0];
          setSelectedCompanyId(firstCompany.companyId);
          if (firstCompany.quizzes.length > 0) {
            setSelectedQuizId(firstCompany.quizzes[0].quizId);
          }
        }
      } catch (err) {
        console.error(err);
        setError("Failed to load analytics.");
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [studentId]);

  const companies = overview?.companies ?? [];

  const selectedCompany: CompanyAnalytics | null = useMemo(() => {
    if (!overview || selectedCompanyId == null) return null;
    return (
      overview.companies.find((c) => c.companyId === selectedCompanyId) ?? null
    );
  }, [overview, selectedCompanyId]);

  const selectedQuiz: QuizAnalytics | null = useMemo(() => {
    if (!selectedCompany || selectedQuizId == null) return null;
    return (
      selectedCompany.quizzes.find((q) => q.quizId === selectedQuizId) ?? null
    );
  }, [selectedCompany, selectedQuizId]);

  const totalCompaniesRegistered = companies.length;

  const totalJobRoles = useMemo(() => {
    const set = new Set<string>();
    companies.forEach((c) =>
      c.quizzes.forEach((q) => {
        const key = q.jobRole || `${c.companyId}-${q.quizTitle}`;
        set.add(key);
      })
    );
    return set.size;
  }, [companies]);

  const totalExamsParticipated = useMemo(() => {
    const quizIds = new Set<number>();
    companies.forEach((c) =>
      c.quizzes.forEach((q) => {
        if (q.myAttemptsCount > 0) quizIds.add(q.quizId);
      })
    );
    return quizIds.size;
  }, [companies]);

  const companyLevelChartData = useMemo(
    () =>
      companies.map((c) => ({
        companyName: c.companyName,
        myAverageScorePercent: Math.round(c.myAverageScorePercent),
        myBestScorePercent: Math.round(c.myBestScorePercent),
        myAttemptsCount: c.myAttemptsCount,
      })),
    [companies]
  );

  const companyQuizzesChartData = useMemo(() => {
    if (!selectedCompany) return [];
    return selectedCompany.quizzes.map((q) => ({
      quizId: q.quizId,
      quizTitle: q.quizTitle,
      myAverageScorePercent: Math.round(q.myAverageScorePercent),
      overallAverageScorePercent: Math.round(q.overallAverageScorePercent),
      myAttemptsCount: q.myAttemptsCount,
    }));
  }, [selectedCompany]);

  const quizDistributionChartData = useMemo(() => {
    if (!selectedQuiz) return [];
    return selectedQuiz.scoreDistribution.map((b, i) => ({
      ...b,
      color: BUCKET_COLORS[i % BUCKET_COLORS.length],
    }));
  }, [selectedQuiz]);

  const hasData = companies.length > 0;

  const shorten = (value: string) =>
    value.length > 22 ? value.slice(0, 22) + "…" : value;

  return (
    <div className="analytics-page">
      <header className="analytics-header">
        <h1>
          Student Analytics
          {isAdminViewingStudent && (
            <>
              {" – "}
              {studentNameFromQuery
                ? `${studentNameFromQuery} (ID: ${studentId})`
                : `Student #${studentId}`}
            </>
          )}
        </h1>
        {!isAdminViewingStudent && (
          <p className="analytics-subtitle">
            Track your progress across companies, job roles, and individual
            exams.
          </p>
        )}
      </header>

      {loading && <div className="analytics-state">Loading…</div>}
      {error && <div className="analytics-error">{error}</div>}

      {!loading && !error && !hasData && (
        <div className="analytics-state">
          No exam attempts yet. Take an exam to see analytics here.
        </div>
      )}

      {!loading && !error && hasData && (
        <>
          <section className="analytics-summary-grid">
            <div className="analytics-card">
              <p className="analytics-card-label">Companies registered</p>
              <p className="analytics-card-value">{totalCompaniesRegistered}</p>
            </div>
            <div className="analytics-card">
              <p className="analytics-card-label">Job roles</p>
              <p className="analytics-card-value">{totalJobRoles}</p>
            </div>
            <div className="analytics-card">
              <p className="analytics-card-label">Exams participated</p>
              <p className="analytics-card-value">{totalExamsParticipated}</p>
            </div>
          </section>
          <section className="analytics-view-controls">
            <div className="analytics-view-left">
              <label className="analytics-view-label">View level</label>
              <select
                className="analytics-select"
                value={viewLevel}
                onChange={(e) => setViewLevel(e.target.value as ViewLevel)}
              >
                <option value="company">Company level</option>
                <option value="companyQuizzes">
                  Within a company (quizzes)
                </option>
                <option value="quizDistribution">
                  Single quiz – score distribution
                </option>
              </select>

              <label className="analytics-view-label">Chart type</label>
              <select
                className="analytics-select analytics-select--small"
                value={chartStyle}
                onChange={(e) => setChartStyle(e.target.value as ChartStyle)}
              >
                <option value="bar">Bar</option>
                <option value="pie">Pie</option>
              </select>
            </div>

            {(viewLevel === "companyQuizzes" ||
              viewLevel === "quizDistribution") && (
              <div className="analytics-view-right">
                <label className="analytics-view-label">Company</label>
                <select
                  className="analytics-select"
                  value={selectedCompanyId ?? ""}
                  onChange={(e) => {
                    const id = Number(e.target.value);
                    setSelectedCompanyId(id);
                    const comp = companies.find((c) => c.companyId === id);
                    if (comp && comp.quizzes.length > 0) {
                      setSelectedQuizId(comp.quizzes[0].quizId);
                    } else {
                      setSelectedQuizId(null);
                    }
                  }}
                >
                  {companies.map((c) => (
                    <option key={c.companyId} value={c.companyId}>
                      {c.companyName}
                    </option>
                  ))}
                </select>

                {viewLevel === "quizDistribution" && selectedCompany && (
                  <>
                    <label className="analytics-view-label">Quiz</label>
                    <select
                      className="analytics-select"
                      value={selectedQuizId ?? ""}
                      onChange={(e) =>
                        setSelectedQuizId(Number(e.target.value))
                      }
                    >
                      {selectedCompany.quizzes.map((q) => (
                        <option key={q.quizId} value={q.quizId}>
                          {q.quizTitle}
                        </option>
                      ))}
                    </select>
                  </>
                )}
              </div>
            )}
          </section>
          <section className="analytics-section">
            {viewLevel === "company" && (
              <>
                <div className="analytics-section-head">
                  <h2>Performance by company</h2>
                  <p>
                    Compare your average and best scores, and how many attempts
                    you made at each company.
                  </p>
                </div>
                <div className="analytics-chart-wrapper">
                  {chartStyle === "bar" ? (
                    <ResponsiveContainer width="100%" height={260}>
                      <BarChart
                        data={companyLevelChartData}
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
                          domain={[0, 100]}
                          tickFormatter={(v) => `${v}%`}
                        />
                        <Tooltip formatter={(v: any) => `${v}%`} />
                        <Legend />
                        <Bar
                          dataKey="myAverageScorePercent"
                          name="My avg. score %"
                          fill={PRIMARY}
                          radius={[4, 4, 0, 0]}
                        />
                        <Bar
                          dataKey="myBestScorePercent"
                          name="My best score %"
                          fill={SECONDARY}
                          radius={[4, 4, 0, 0]}
                        />
                      </BarChart>
                    </ResponsiveContainer>
                  ) : (
                    <ResponsiveContainer width="100%" height={260}>
                      <PieChart>
                        <Pie
                          data={companyLevelChartData}
                          dataKey="myAverageScorePercent"
                          nameKey="companyName"
                          outerRadius={90}
                          label
                        >
                          {companyLevelChartData.map((entry, index) => (
                            <Cell
                              key={index}
                              fill={BUCKET_COLORS[index % BUCKET_COLORS.length]}
                            />
                          ))}
                        </Pie>
                        <Tooltip formatter={(v: any) => `${v}%`} />
                        <Legend />
                      </PieChart>
                    </ResponsiveContainer>
                  )}
                </div>
              </>
            )}
            {viewLevel === "companyQuizzes" && selectedCompany && (
              <>
                <div className="analytics-section-head">
                  <h2>Quizzes in {selectedCompany.companyName}</h2>
                  <p>
                    Your performance vs overall average on each quiz for this
                    company.
                  </p>
                </div>
                <div className="analytics-chart-wrapper">
                  {chartStyle === "bar" ? (
                    <ResponsiveContainer width="100%" height={260}>
                      <BarChart
                        data={companyQuizzesChartData}
                        margin={{ top: 10, right: 20, left: 0, bottom: 60 }}
                      >
                        <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
                        <XAxis
                          dataKey="quizTitle"
                          tickFormatter={shorten}
                          tick={{ fill: "#6b7280", fontSize: 12 }}
                          axisLine={{ stroke: "#e5e7eb" }}
                          tickLine={{ stroke: "#e5e7eb" }}
                          height={60}
                        />
                        <YAxis
                          domain={[0, 100]}
                          tickFormatter={(v) => `${v}%`}
                          tick={{ fill: "#6b7280", fontSize: 12 }}
                          axisLine={{ stroke: "#e5e7eb" }}
                          tickLine={{ stroke: "#e5e7eb" }}
                        />
                        <Tooltip
                          formatter={(v: any) => `${v}%`}
                          labelFormatter={(label) => `Quiz: ${label}`}
                        />
                        <Legend />
                        <Bar
                          dataKey="myAverageScorePercent"
                          name="My avg. score %"
                          fill={PRIMARY}
                          radius={[4, 4, 0, 0]}
                        />
                        <Bar
                          dataKey="overallAverageScorePercent"
                          name="Overall avg. score %"
                          fill={SECONDARY}
                          radius={[4, 4, 0, 0]}
                        />
                      </BarChart>
                    </ResponsiveContainer>
                  ) : (
                    <ResponsiveContainer width="100%" height={260}>
                      <PieChart>
                        <Pie
                          data={companyQuizzesChartData}
                          dataKey="myAverageScorePercent"
                          nameKey="quizTitle"
                          outerRadius={90}
                          label={(props: PieLabelRenderProps) =>
                            shorten(String(props.name))
                          }
                        >
                          {companyQuizzesChartData.map((entry, index) => (
                            <Cell
                              key={index}
                              fill={BUCKET_COLORS[index % BUCKET_COLORS.length]}
                            />
                          ))}
                        </Pie>
                        <Tooltip formatter={(v: any) => `${v}%`} />
                        <Legend />
                      </PieChart>
                    </ResponsiveContainer>
                  )}
                </div>
              </>
            )}
            {viewLevel === "quizDistribution" && selectedQuiz && (
              <>
                <div className="analytics-section-head">
                  <h2>Score distribution – {selectedQuiz.quizTitle}</h2>
                  <p>
                    See how your scores compare with everyone who attempted this
                    quiz.
                  </p>
                </div>
                <div className="analytics-chart-wrapper">
                  {chartStyle === "bar" ? (
                    <ResponsiveContainer width="100%" height={260}>
                      <BarChart
                        data={quizDistributionChartData}
                        margin={{ top: 10, right: 20, left: 0, bottom: 40 }}
                      >
                        <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
                        <XAxis
                          dataKey="label"
                          tick={{ fill: "#6b7280", fontSize: 12 }}
                          axisLine={{ stroke: "#e5e7eb" }}
                          tickLine={{ stroke: "#e5e7eb" }}
                        />
                        <YAxis
                          tick={{ fill: "#6b7280", fontSize: 12 }}
                          axisLine={{ stroke: "#e5e7eb" }}
                          tickLine={{ stroke: "#e5e7eb" }}
                        />
                        <Tooltip />
                        <Legend />
                        <Bar dataKey="count" name="Attempts">
                          {quizDistributionChartData.map((entry, index) => (
                            <Cell
                              key={index}
                              fill={entry.color || BUCKET_COLORS[index]}
                            />
                          ))}
                        </Bar>
                      </BarChart>
                    </ResponsiveContainer>
                  ) : (
                    <ResponsiveContainer width="100%" height={260}>
                      <PieChart>
                        <Pie
                          data={quizDistributionChartData}
                          dataKey="count"
                          nameKey="label"
                          outerRadius={90}
                          label
                        >
                          {quizDistributionChartData.map((entry, index) => (
                            <Cell
                              key={index}
                              fill={entry.color || BUCKET_COLORS[index]}
                            />
                          ))}
                        </Pie>
                        <Tooltip />
                        <Legend />
                      </PieChart>
                    </ResponsiveContainer>
                  )}
                </div>

                <div className="analytics-quiz-summary">
                  <p>
                    Your last score:{" "}
                    <strong>
                      {selectedQuiz.myLastScorePercent != null
                        ? `${Math.round(selectedQuiz.myLastScorePercent)}%`
                        : "Not attempted yet"}
                    </strong>
                    {" · "}Your best:{" "}
                    <strong>
                      {Math.round(selectedQuiz.myBestScorePercent)}%
                    </strong>
                    {" · "}Overall avg:{" "}
                    <strong>
                      {Math.round(selectedQuiz.overallAverageScorePercent)}%
                    </strong>
                  </p>
                </div>
              </>
            )}
          </section>
        </>
      )}
    </div>
  );
}
