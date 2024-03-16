import { useState } from "react";
import ExampleComponent from "../../components/ExampleComponent/ExampleComponent";
import ExampleButton from "../../components/ExampleButton/ExampleButton";

import './Home.css'

export default function Home() {
    const [message, setMessage] = useState('');

    async function getMessageFromBackend() {
        const response = await fetch('http://localhost:8080/api/v1/teller');
        const body = await response.json();

        setMessage('Got from backend: ' + body.text);
    }

    return (
        <main>
            <ExampleComponent />
            <div className="center">
                <ExampleButton text={ "Click to get message from backend" } onClick={ getMessageFromBackend }/>
                <p>{ message }</p>
            </div>
            
        </main>
    )
}