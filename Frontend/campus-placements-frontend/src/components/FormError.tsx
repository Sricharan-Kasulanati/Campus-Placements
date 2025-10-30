export default function FormError({ message }: { message?: string }) {
  if (!message) return null;
  return <div className="bg-red-50 text-red-700 border border-red-200 rounded-md px-3 py-2 mb-3 text-sm">{message}</div>;
}
