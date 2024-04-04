import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form, Dropdown, DropdownButton } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { fetchData } from '../../fetching/Fetch.js';
import { useLocation } from 'react-router-dom';

const ManageStationScreen = () => {
    const location = useLocation();
    const [showServiceModal, setShowServiceModal] = useState(false);
    const [showDisplayModal, setShowDisplayModal] = useState(false);
    const [deleteConfirmation, setDeleteConfirmation] = useState(false);
    const [stations, setStations] = useState([]);
    const [selectedStation, setSelectedStation] = useState(null);
    const [selectedServices, setSelectedServices] = useState([]);
    const [availableServices, setAvailableServices] = useState([]);
    const [availableDisplays, setAvailableDisplays] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        async function fetchStations() {
            try {
                const tenantCode = location.pathname.split('/')[1];
                const response = await fetchData(`http://localhost:8080/api/v1/stations/${tenantCode}`, 'GET');
                if (response.success) {
                    setStations(response.data);
                } else {
                    console.error('Error fetching stations:', response.error);
                }
            } catch (error) {
                console.error('Error fetching stations:', error);
            }
        }

        async function fetchAvailableServices() {
            try {
                const tenantCode = location.pathname.split('/')[1];
                const response = await fetchData(`http://localhost:8080/api/v1/tenants/${tenantCode}/services`, 'GET');
                if (response.success) {
                    setAvailableServices(response.data);
                } else {
                    console.error('Error fetching available services:', response.error);
                }
            } catch (error) {
                console.error('Error fetching available services:', error);
            }
        }

        async function fetchAvailableDisplays() {
            try {
                const tenantCode = location.pathname.split('/')[1];
                const response = await fetchData(`http://localhost:8080/api/v1/displays/unassigned/${tenantCode}`, 'GET');
                if (response.success) {
                    setAvailableDisplays(response.data);
                } else {
                    console.error('Error fetching available displays:', response.error);
                }
            } catch (error) {
                console.error('Error fetching available displays:', error);
            }
        }

        fetchStations();
        fetchAvailableServices();
        fetchAvailableDisplays();
    }, [location]);

    useEffect(() => {
        setSelectedServices(selectedStation ? selectedStation.services : []);
    }, [selectedStation]);

    const confirmDeleteStation = () => {
    };

    const saveServiceToStation = async (service) => {
        try {
            const tenantCode = location.pathname.split('/')[1];
            const response = await fetchData(`http://localhost:8080/api/v1/stations/${tenantCode}/${selectedStation.id}/services/${service.id}`, 'PUT');
            if (response.success) {
                console.log('Service added to station successfully');

                const updatedStations = stations.map(station => {
                    if (station.id === selectedStation.id) {
                        return {
                            ...station,
                            services: [...station.services, service]
                        };
                    }
                    return station;
                });
                setStations(updatedStations);
            } else {
                console.error('Error adding service to station:', response.error);
            }
        } catch (error) {
            console.error('Error adding service to station:', error);
        }
    };

    const addDisplayToStation = async (display) => {
        try {
            const tenantCode = location.pathname.split('/')[1];
            const response = await fetchData(`http://localhost:8080/api/v1/stations/${tenantCode}/${selectedStation.id}/displays/${display.id}`, 'PUT');
            if (response.success) {
                console.log('Display added to station successfully');

                const updatedStations = stations.map(station => {
                    if (station.id === selectedStation.id) {
                        if (station.display) {
                            setAvailableDisplays(prevDisplays => [...prevDisplays, station.display]);
                        }
                        return {
                            ...station,
                            display: display
                        };
                    }
                    return station;
                });
                setStations(updatedStations);
                setAvailableDisplays(prevDisplays => prevDisplays.filter(d => d.id !== display.id)); // Remove the added display from availableDisplays
                setSelectedStation(updatedStations.find(station => station.id === selectedStation.id));
                setShowDisplayModal(true);
            } else {
                console.error('Error adding display to station:', response.error);
            }
        } catch (error) {
            console.error('Error adding display to station:', error);
        }
    };

    const removeServiceFromStation = async (serviceId) => {
        try {
            const tenantCode = location.pathname.split('/')[1];
            const response = await fetchData(`http://localhost:8080/api/v1/stations/${tenantCode}/${selectedStation.id}/services/${serviceId}`, 'DELETE');
            if (response.success) {
                const updatedStations = stations.map(station => {
                    if (station.id === selectedStation.id) {
                        return {
                            ...station,
                            services: station.services.filter(service => service.id !== serviceId)
                        };
                    }
                    return station;
                });
                setStations(updatedStations);
            } else {
                console.error('Error removing service from station:', response.error);
            }
        } catch (error) {
            console.error('Error removing service from station:', error);
        }
    };

    const removeSelectedService = (serviceId) => {
        removeServiceFromStation(serviceId);
        const updatedServices = selectedServices.filter(service => service.id !== serviceId);
        setSelectedServices(updatedServices);
    };

    const removeDisplayFromStation = async () => {
        try {
            const tenantCode = location.pathname.split('/')[1];
            const response = await fetchData(`http://localhost:8080/api/v1/stations/${tenantCode}/${selectedStation.id}/displays/${selectedStation.display.id}`, 'DELETE');
            if (response.success) {
                console.log('Display removed from station successfully');

                const updatedStations = stations.map(station => {
                    if (station.id === selectedStation.id) {
                        return {
                            ...station,
                            display: null
                        };
                    }
                    return station;
                });
                setStations(updatedStations);
                setAvailableDisplays(prevDisplays => [...prevDisplays, selectedStation.display]);
                setShowDisplayModal(false);
            } else {
                console.error('Error removing display from station:', response.error);
            }
        } catch (error) {
            console.error('Error removing display from station:', error);
        }
    };

    const addService = (service) => {
        saveServiceToStation(service);
        setSelectedServices([...selectedServices, service]);
    };

    const handleCloseModal = () => {
        setSelectedServices(selectedStation ? selectedStation.services : []);
        setShowServiceModal(false);
        setShowDisplayModal(false);
    };

    return (
        <div className="text-center mt-5">
            <h2>Teller Stations</h2>
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Station ID</th>
                    <th>Station Name</th>
                    <th>Services</th>
                    <th>Service Action</th>
                    <th>Displays</th>
                    <th>Display Action</th>
                </tr>
                </thead>
                <tbody>
                {stations.map((station, index) => (
                    <tr key={index}>
                        <td>{station.id}</td>
                        <td>{station.name}</td>
                        <td>
                            {station.services && station.services.length > 0 ? (
                                station.services.map((service, serviceIndex) => (
                                    <span key={serviceIndex} style={{ marginRight: '5px' }}>
                                            {serviceIndex > 0 && ', '}
                                        {service.name}
                                        </span>
                                ))
                            ) : (
                                <span>No services</span>
                            )}
                        </td>
                        <td>
                            <Button
                                variant="primary"
                                style={{ backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' }}
                                onClick={() => {
                                    setShowServiceModal(true);
                                    setSelectedStation(station);
                                }}
                            >
                                Edit
                            </Button>
                        </td>
                        <td>
                            {station.display ? (
                                <span>{station.display.name}</span>
                            ) : (
                                <span>No display</span>
                            )}
                        </td>
                        <td>
                            <Button
                                variant="primary"
                                style={{ backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' }}
                                onClick={() => {
                                    setShowDisplayModal(true);
                                    setSelectedStation(station);
                                }}
                            >
                                Edit
                            </Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <Modal show={showServiceModal} onHide={handleCloseModal}>
                <Modal.Header closeButton style={{ backgroundColor: '#334257', color: 'white' }}>
                    <Modal.Title>Manage Services for Station</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p><strong>Teller Station:</strong> {selectedStation && selectedStation.name}</p>
                    <div>
                        <div style={{ marginBottom: '10px' }}>
                            <strong>Selected Services:</strong>
                            {selectedServices.length > 0 ? (
                                selectedServices.map((service, index) => (
                                    <span key={index} className="badge bg-secondary m-1" style={{ display: 'inline-flex', alignItems: 'center' }}>
                        {service.name}
                                        <Button variant="danger" size="sm" style={{ marginLeft: '5px', padding: '2px 5px', backgroundColor: '#dc3545', borderColor: '#dc3545' }} onClick={() => removeSelectedService(service.id)}>X</Button>
                                </span>
                                ))
                            ) : (
                                <span> No service selected</span>
                            )}
                        </div>
                        <Form.Group controlId="formGroupService" className="mb-3">
                            <DropdownButton
                                title={'Select Service'}
                                variant="btn btn-outline-secondary"
                            >
                                {availableServices.map((service, index) => {
                                    const isAssigned = selectedServices.some(selectedService => selectedService.id === service.id);
                                    if (!isAssigned) {
                                        return (
                                            <Dropdown.Item key={index} onClick={() => addService(service)}>{service.name}</Dropdown.Item>
                                        );
                                    } else {
                                        return null;
                                    }
                                })}
                            </DropdownButton>
                        </Form.Group>

                        {errorMessage && <p className="text-danger">{errorMessage}</p>}
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>Close</Button>
                </Modal.Footer>
            </Modal>

            <Modal show={showDisplayModal} onHide={handleCloseModal}>
                <Modal.Header closeButton style={{ backgroundColor: '#334257', color: 'white' }}>
                    <Modal.Title>Manage Display for Station</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p><strong>Teller Station:</strong> {selectedStation && selectedStation.name}</p>
                    <div>
                        <div style={{ marginBottom: '10px' }}>
                            <strong>Selected Display:</strong>
                            {selectedStation && selectedStation.display ? (
                                <span className="badge bg-secondary m-1" style={{ display: 'inline-flex', alignItems: 'center' }}> {selectedStation.display.name}
                                    <Button variant="danger" size="sm" style={{ marginLeft: '5px', padding: '2px 5px', backgroundColor: '#dc3545', borderColor: '#dc3545' }} onClick={removeDisplayFromStation}>X</Button>
                                </span>
                            ) : (
                                <span> No display selected</span>
                            )}

                        </div>
                        <Form>
                            <Form.Group controlId="formGroupDisplay" className="mb-3">
                                <DropdownButton
                                    title={'Select Display'}
                                    variant="btn btn-outline-secondary"
                                >
                                    {availableDisplays.map((display, index) => (
                                        <Dropdown.Item key={index} onClick={() => addDisplayToStation(display)}>{display.name}</Dropdown.Item>
                                    ))}
                                </DropdownButton>


                            </Form.Group>
                        </Form>
                        {errorMessage && <p className="text-danger">{errorMessage}</p>}
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>Close</Button>
                </Modal.Footer>
            </Modal>

            <Modal show={deleteConfirmation} onHide={() => setDeleteConfirmation(false)}>
                <Modal.Header closeButton style={{ backgroundColor: '#334257', color: 'white' }}>
                    <Modal.Title>Confirmation</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Are you sure you want to delete this station?
                </Modal.Body>
                <Modal.Footer style={{ backgroundColor: '#334257', color: 'white' }}>
                    <Button variant="secondary" onClick={() => setDeleteConfirmation(false)}>Cancel</Button>
                    <Button variant="danger" onClick={confirmDeleteStation}>Delete</Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default ManageStationScreen;
