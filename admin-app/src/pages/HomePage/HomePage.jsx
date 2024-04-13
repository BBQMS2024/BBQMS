import 'bootstrap/dist/css/bootstrap.min.css';

import './HomePage.css'
import { SERVER_URL } from '../../constants.js';
export default function HomePage() {

    function downloadPdf() {
        fetch(SERVER_URL + "/api/v1/tickets/12/print")
            .then(res => res.blob())
            .then(blob => {
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'Ticket.pdf';
                a.click();
            })
    }

    return (
        <main className="background-hp">
            <h1 className="h1-hp">Dashboard</h1>
            <button onClick={ downloadPdf }>DOWNLOAD</button>
        </main>
    )
}
