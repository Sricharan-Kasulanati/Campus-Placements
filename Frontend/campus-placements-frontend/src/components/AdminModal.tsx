import { useState } from "react";
import { Company } from "types/company";

function AdminModal({
  initial,
  onClose,
  onSubmit,
}: {
  initial: Company | null;
  onClose: () => void;
  onSubmit: (v: any) => void;
}) {
  const [v, set] = useState({
    name: initial?.name ?? "",
    description: initial?.description ?? "",
    location: initial?.location ?? "",
    website: initial?.website ?? "",
    category: (initial as any)?.category ?? "",
  });

  return (
    <div className="practice-modal-backdrop">
      <div className="practice-modal">
        <h2>{initial ? "Update Company" : "Add Company"}</h2>
        Name
        <input
          className="input"
          placeholder="Name *"
          value={v.name}
          onChange={(e) => set({ ...v, name: e.target.value })}
        />
        Descrtiption
        <textarea
          className="input"
          placeholder="Description"
          value={v.description}
          onChange={(e) => set({ ...v, description: e.target.value })}
        />
        Location
        <input
          className="input"
          placeholder="Location"
          value={v.location}
          onChange={(e) => set({ ...v, location: e.target.value })}
        />
        Website
        <input
          className="input"
          placeholder="Website"
          value={v.website}
          onChange={(e) => set({ ...v, website: e.target.value })}
        />
        Catogery
        <input
          className="input"
          placeholder="Category"
          value={v.category}
          onChange={(e) => set({ ...v, category: e.target.value })}
        />
        <div className="modal-actions">
          <button className="pager-btn" onClick={onClose}>
            Cancel
          </button>
          <button className="pager-btn" onClick={() => onSubmit(v)}>
            {initial ? "Save" : "Create"}
          </button>
        </div>
      </div>
    </div>
  );
}

export default AdminModal;
