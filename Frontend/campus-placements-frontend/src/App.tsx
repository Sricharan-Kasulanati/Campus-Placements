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
              <ProtectedRoute>
                <AdminStudentPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/companies/:id/prep"
            element={
              <ProtectedRoute>
                <CompanyPrepPage />
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
