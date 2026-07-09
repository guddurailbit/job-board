import React, { useState } from "react";
import "./LoginModal.css";

export default function LoginModal({ onClose, onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  function submit(e) {
    e.preventDefault();

    const user = {
      name: "User",
      email,
      token: "demo-token",
    };

    onLogin(user);

    onClose();
  }

  return (
    <div className="modal-overlay">
      <div className="login-box">
        <h2>Login</h2>

        <form onSubmit={submit}>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button className="login-submit">Login</button>
        </form>

        <button className="close-btn" onClick={onClose}>
          Cancel
        </button>
      </div>
    </div>
  );
}
