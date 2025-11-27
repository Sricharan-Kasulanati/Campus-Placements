import type { ButtonHTMLAttributes, PropsWithChildren } from 'react';
export default function Button({ children, ...props }: PropsWithChildren<ButtonHTMLAttributes<HTMLButtonElement>>) {
  return <button className="w-full rounded-lg px-4 py-2 font-medium border hover:shadow disabled:opacity-50" {...props}>{children}</button>;
}
