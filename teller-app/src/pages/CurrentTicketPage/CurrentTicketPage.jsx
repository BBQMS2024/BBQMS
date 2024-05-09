import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { fetchData } from '../../fetching/Fetch.js';
import { SERVER_URL } from '../../constants.js';
import '../CurrentTicketPage/CurrentTicketPage.css';

export default function CurrentTicketPage() {
    const [currentTicket, setCurrentTicket] = useState('');
    const { stationId } = useParams();

    useEffect(() => {
        fetchCurrentTicket();
    }, [stationId]);

    const fetchCurrentTicket = async () => {
        try {
            const response = await fetchData(`${SERVER_URL}/api/v1/teller/current-ticket/${stationId}`, 'GET');
            if (response.success) {
                if (typeof response.data === 'object' && response.data.message) {
                    setCurrentTicket(response.data.message);
                } else {
                    setCurrentTicket(response.data.number);
                }
            } else {
                setCurrentTicket('There is no current ticket for this station');
                console.error('Error fetching current ticket:', response.error);
            }
        } catch (error) {
            setCurrentTicket('There is no current ticket for this station');
            console.log("Error fetching current ticket:", error);
        }
    };

    return (
        <div className="text-center mt-5">
            <h1 className="message">{currentTicket}</h1>
        </div>
    );
}
