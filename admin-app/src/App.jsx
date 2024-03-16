import { useEffect } from "react";
import { useState } from "react"


export default function App() {
    const [message, setMessage] = useState('');

    async function getMessageFromBackend() {
        const response = await fetch('http://localhost:8080/api/v1/admin');
        const body = await response.json();

        setMessage('Got from backend: ' + body.text);
    }

    return (
        <>
            <p>
                Hello world. Please set up my structure similar to Teller-app
            </p>
            <button onClick={ getMessageFromBackend }> Click me </button>
            <p>{ message }</p>
        </>
    )
}


