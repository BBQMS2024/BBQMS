import { GoogleOAuthProvider } from "@react-oauth/google";
import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import ManageServices from "./pages/ManageServices/ManageServices";
import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <GoogleOAuthProvider clientId="776973117081-smp0drfulvkjk8s55ifr2i7k3uklpr04.apps.googleusercontent.com">
            <BrowserRouter>
                <ManageServices />
            </BrowserRouter>
        </GoogleOAuthProvider>
    </React.StrictMode>
);
