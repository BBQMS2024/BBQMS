import React, { useState, useEffect } from 'react';
import { Table, Dropdown, DropdownButton } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { fetchData } from '../../fetching/Fetch.js';
import { useLocation } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import { SERVER_URL } from '../../constants.js';

const ViewBranchQueues = () => {
    const location = useLocation();
    const [selectedStation, setSelectedStation] = useState(null);
    const [selectedBranch, setSelectedBranch] = useState(null);
    const [branches, setBranches] = useState([]);
    const [services, setServices] = useState([]);
    const [queue, setQueue] = useState([]);
    const [selectedService, setSelectedService] = useState(null);
    const [serviceName, setServiceName] = useState('Select Service');
    const [originalQueue, setOriginalQueue] = useState([]);
    const { tenantCode } = useParams();

    const url = `${SERVER_URL}/api/v1/`;

    useEffect(() => {
        async function fetchQueuesForBranch() {
            try {
                const response = await fetchData(`${url}branches/${tenantCode}/${selectedBranch.id}/queue`, 'GET');

                if (response.success) {
                    setOriginalQueue(response.data);
                    setQueue(response.data);
                } else {
                    console.error('Error fetching queue:', response.error);
                }
            } catch (error) {
                console.error('Error fetching queue:', error);
            }
        }

        async function fetchServicesForBranch() {
            try {
                const response = await fetchData(`${url}branches/${tenantCode}/${selectedBranch.id}/services`, 'GET');
                if (response.success) {
                    setServices(response.data);
                } else {
                    console.error('Error fetching services:', response.error);
                }
            } catch (error) {
                console.error('Error fetching services:', error);
            }
        }

        if (selectedBranch) {
            fetchQueuesForBranch();
            fetchServicesForBranch();
        } else {
            setQueue([]);
            setServices([]);
        }
    }, [selectedBranch, tenantCode]);

    useEffect(() => {
        async function fetchBranches() {
            try {
                const response = await fetchData(`${url}branches/${tenantCode}`, 'GET');
                if (response.success) {
                    setBranches(response.data);
                } else {
                    console.error('Error fetching branches:', response.error);
                }
            } catch (error) {
                console.error('Error fetching branches:', error);
            }
        }

        fetchBranches();
    }, [location, tenantCode]);

    function setUpDropdowns(branch) {
        setSelectedBranch(branch);
        setServiceName('Select Service');
        setQueue(originalQueue);
    }

    function setDropdownService(service) {
        setSelectedService(service);
        setServiceName(service.name);
        updateQueue(service.id);
    }

    function updateQueue(serviceId) {
        if (serviceId) {
            const filteredQueue = originalQueue.map(queueItem => ({
                ...queueItem,
                tickets: Array.isArray(queueItem.tickets) ? queueItem.tickets.filter(ticket => ticket.service.id === serviceId) : []
            }));
            setQueue(filteredQueue);
        } else {
            setQueue(originalQueue);
        }
    }

    return (
        <div className="text-center mt-5">
            <h2>Queue</h2>
            <DropdownButton title={selectedBranch ? selectedBranch.name : 'Select Branch'} variant="btn btn-outline-secondary">
                {branches.map((branch, index) => (
                    <Dropdown.Item key={index} onClick={() => setUpDropdowns(branch)}>{branch.name}</Dropdown.Item>
                ))}
            </DropdownButton>

            <DropdownButton style={{ marginTop: '20px' }} title={serviceName} variant="btn btn-outline-secondary">
                {services.map((service, index) => (
                    <Dropdown.Item key={index} onClick={() => setDropdownService(service)}>{service.name}</Dropdown.Item>
                ))}
            </DropdownButton>

            <div style={{ marginTop: '20px' }}>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>Ticket Id</th>
                        <th>Ticket Number</th>
                        <th>Service</th>
                        <th>Created At</th>
                        <th>Elapsed Time</th>
                    </tr>
                    </thead>
                    <tbody>
                    {queue.map((queueItem, index) => (
                        queueItem.tickets.map((ticket, ticketIndex) => {
                            const createdAt = new Date(ticket.createdAt);
                            const currentTime = new Date();
                            const elapsedTime = Math.floor((currentTime - createdAt) / (1000 * 60));

                            return (
                                <tr key={`${index}-${ticketIndex}`}>
                                    <td>{ticket.id}</td>
                                    <td>{ticket.number}</td>
                                    <td>{ticket.service.name}</td>
                                    <td>{createdAt.toLocaleString()}</td>
                                    <td>{elapsedTime} minutes</td>
                                </tr>
                            );
                        })
                    ))}
                    </tbody>
                </Table>
            </div>
        </div>
    );
};

export default ViewBranchQueues;
