import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import AuthProvider from "./auth/AuthProvider";
import NavBar from "./components/NavBar";
import Footer from "./components/Footer";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Profile from "./pages/Profile";
import ProtectedRoute from "./auth/ProtectedRoute";
import Landing from "pages/Landing";
import AdminLanding from "pages/AdminLanding";
import AdminStudentPage from "pages/AdminStudentPage";
import CompanyPrepPage from "pages/CompanyPrepPage";
import StudentExamPage from "pages/StudentExamPage";
import StudentAnalyticsPage from "pages/StudentAnalyticsPage";
import AdminAnalyticsPage from "pages/AdminAnalyticsPage";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <NavBar />

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />

          <Route
            path="/landing"
            element={
              <ProtectedRoute role="STUDENT">
                <Landing />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin"
            element={
              <ProtectedRoute role="ADMIN">
                <AdminLanding />
              </ProtectedRoute>
            }
          />

          <Route
            path="/profile"
            element={
              <ProtectedRoute>
                <Profile />
              </ProtectedRoute>
            }
          />

          <Route
            path="/admin/students"
            element={
              <ProtectedRoute role="ADMIN">
                <AdminStudentPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/companies/:id/prep"
            element={
              <ProtectedRoute role="STUDENT">
                <CompanyPrepPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/companies/:companyId/exams"
            element={<StudentExamPage />}
          />
          <Route
            path="/analytics"
            element={
              <ProtectedRoute>
                <StudentAnalyticsPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/admin/analytics"
            element={
              <ProtectedRoute role="ADMIN">
                <AdminAnalyticsPage />
              </ProtectedRoute>
            }
          />

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
        <Footer />
      </AuthProvider>
    </BrowserRouter>
  );
}
