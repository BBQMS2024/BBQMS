import React, { useState, useEffect } from 'react';
import { Button, Table } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { fetchData } from '../../fetching/Fetch.js'
import { SERVER_URL } from '../../constants.js';

const styles = {
    stationRow: {
        backgroundColor: "#ADD8E6",
    }
};

const ShowQueuesForTellerPage = () =>{
    const [tickets, setTickets] = useState([]);
    const [tellerStationId, setTellerStationId] = useState(localStorage.getItem('tellerId'));
    const url = `${SERVER_URL}/api/v1/`;
    
    useEffect(() => {
        fetchTicketsForTellerStation();
    }, [tellerStationId]);

    const fetchTicketsForTellerStation = async () => {
        try{
            const response = await fetchData(`${url}stations/${tellerStationId}/tickets`, 'GET');
    
            if(response.success){
                const sortedTickets = response.data.sort((a, b) => a.number - b.number);
                setTickets(sortedTickets);
            }else{
                console.error('Error fethcing tickets:', response.error);
            }
        }catch(error){
            console.log("Error fetching tickets for tellerStation:", error);
        }
    };

    const advanceQueue = async () =>{
        try{
            const response = await fetchData(`${url}teller/advance-queue/${tellerStationId}`, 'POST');
            if(response.success){
                await fetchTicketsForTellerStation();
            }else{
                console.error('Error advancing queue:', response.error);
            }
        }catch(error){
            console.log("Error advancing queue:", error);
        }
    }

    return (
        <div className="text-center mt-5">
            <h1>Queues</h1>
            <Button
                variant="primary"
                style={ { backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' } }
                onClick={advanceQueue}
            >
                Advance Queue
            </Button>
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
                                    <td style={ticket.station?.id == tellerStationId ? styles.stationRow : {}}>{ticket.number}</td>
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

export default ShowQueuesForTellerPage;