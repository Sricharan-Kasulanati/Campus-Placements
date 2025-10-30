import { useAuth } from '../auth/AuthProvider';
export default function Home() {
  const { user, logout } = useAuth();
  return (
    <div className="max-w-xl mx-auto mt-14">
      <h1 className="text-2xl font-semibold mb-2">Welcome{user ? `, ${user.fullName}` : ''}!</h1>
      <p className="text-gray-600 mb-4">Role: {user?.role ?? 'Guest'}</p>
      {user && <button className="border rounded px-3 py-1" onClick={logout}>Log out</button>}
    </div>
  );
}
