import type { ChangeEventHandler } from 'react';
type Props = {
  label: string; name: string; value: string;
  onChange: ChangeEventHandler<HTMLInputElement>;
  type?: string; placeholder?: string; autoComplete?: string; required?: boolean;
};
export default function TextField({ label, name, value, onChange, type='text', placeholder, autoComplete, required }: Props) {
  return (
    <label className="block mb-3">
      <span className="block text-sm font-medium mb-1">{label}</span>
      <input className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring"
        {...{ name, value, onChange, type, placeholder, autoComplete, required }} />
    </label>
  );
}