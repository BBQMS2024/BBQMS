import React, { useState, useEffect } from 'react';
import { Button, Table } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useInterval } from '../../hooks/hooks.jsx';
import { fetchData } from '../../fetching/Fetch.js'
import { SERVER_URL } from '../../constants.js';
import { Link, useParams } from 'react-router-dom';

const styles = {
    stationRow: {
        backgroundColor: "#ADD8E6",
    }
};

export default function ShowQueuesForTellerPage() {
    const url = `${ SERVER_URL }/api/v1/`;
    const [ tickets, setTickets ] = useState([]);
    const [ usedUndo, setUsedUndo ] = useState(false)
    const [ timedOut, setTimedOut ] = useState(false)

    const { stationId} = useParams()
    
    useEffect(() => {
        fetchTicketsForTellerStation();
    }, [ stationId ]);

    useInterval(fetchTicketsForTellerStation, 10000)

    function fetchTicketsForTellerStation() {
        fetchData(`${ url }stations/${ stationId }/tickets`, 'GET')
            .then(response => response.data)
            .then(setTickets)
            .catch(console.error)
    }

    function advanceQueue() {
        fetchData(`${ url }teller/advance-queue/${ stationId }`, 'POST')
            .then(fetchTicketsForTellerStation)
            .then(() => setUsedUndo(false))
            .then(timeOutTeller)
            .catch(console.error)
    }

    function undoQueue() {
        fetchData(`${ url }teller/undo-queue/${ stationId }`, 'POST')
            .then(fetchTicketsForTellerStation)
            .then(() => setUsedUndo(true))
            .catch(console.error)
    }

    function timeOutTeller() {
        setTimedOut(true)
        setTimeout(() => setTimedOut(false), 5000)
        // Can't advance more often than every 5 seconds.
    }

    const hasAssignedTicket = tickets.some(ticket => ticket.station?.id == stationId)

    return (
        <div className="text-center mt-5">
            <h1>Queues</h1>
            <div className="d-flex gap-2 justify-content-center">
                <Button
                    variant="primary"
                    style={ { backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' } }
                    onClick={ advanceQueue }
                    disabled={ timedOut }
                >
                    Advance Queue
                </Button>
                <Button variant="primary"
                        style={ { backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' } }
                        onClick={ undoQueue }
                        disabled={ usedUndo || !hasAssignedTicket }>
                    Undo
                </Button>
                <Button variant="primary"
                        style={ { backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' } }
                        onClick={ fetchTicketsForTellerStation }>
                    Refresh
                </Button>
                <Link to={`/display/${stationId}`} target="_blank" rel="noopener noreferrer">
                    <Button
                        variant="secondary"
                    >
                        Current Ticket
                    </Button>
                </Link>
            </div>
            <div style={ { marginTop: '20px' } }>
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>Ticket number</th>
                            <th>Service</th>
                            <th>Creation at</th>
                        </tr>
                    </thead>
                    <tbody>
                    {tickets.map((ticket, index) => {
                            const createdAt = new Date(ticket.createdAt);
                            return (
                                <tr key={index}>
                                    <td style={ticket.station?.id == stationId ? styles.stationRow : {}}>{ticket.number}</td>
                                    <td>{ticket.service.name}</td>
                                    <td>{createdAt.toLocaleString()}</td>
                                </tr>
                            );
                        })}
                    </tbody>
                </Table>
            </div>
        </div>
    )
}
