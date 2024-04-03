import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form, Dropdown, DropdownButton } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { fetchData } from '../../fetching/Fetch.js'; // Import fetchData function
import { useLocation } from 'react-router-dom';

const StationServiceScreen = () => {
    const location = useLocation();
    const [showModal, setShowModal] = useState(false);
    const [deleteConfirmation, setDeleteConfirmation] = useState(false);
    const [branches, setBranches] = useState([]);
    const [selectedBranch, setSelectedBranch] = useState(null);
    const [selectedStation, setSelectedStation] = useState(null);
    const [selectedServices, setSelectedServices] = useState([]);
    const [availableServices, setAvailableServices] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        async function fetchBranches() {
            try {
                const tenantCode = location.pathname.split('/')[1];
                const response = await fetchData(`http://localhost:8080/api/v1/branches/${tenantCode}`, 'GET');
                if (response.success) {
                    setBranches(response.data);
                } else {
                    console.error('Error fetching branches:', response.error);
                }
            } catch (error) {
                console.error('Error fetching branches:', error);
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

        fetchBranches();
        fetchAvailableServices();
    }, [location]);

    const confirmDeleteStation = () => {
        // Implement station deletion logic here
    };

    const saveServiceToStation = async () => {
        try {
            const tenantCode = location.pathname.split('/')[1];
            const promises = selectedServices.map(async (service) => {
                const response = await fetchData(`http://localhost:8080/api/v1/stations/${tenantCode}/${selectedStation.id}/services/${service.id}`, 'PUT');
                if (!response.success) {
                    console.error('Error adding service to station:', response.error);
                }
            });
            await Promise.all(promises);
            console.log('Services added to station successfully');
        } catch (error) {
            console.error('Error adding service to station:', error);
        }
    };

    const removeSelectedService = (index) => {
        const updatedServices = [...selectedServices];
        updatedServices.splice(index, 1);
        setSelectedServices(updatedServices);
    };

    const addService = (service) => {
        setSelectedServices([...selectedServices, service]);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setSelectedServices([]); // Reset selected services when modal is closed
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
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {branches.map((branch, index) => (
                    branch.tellerStations.map((station, stationIndex) => (
                        <tr key={`${index}-${stationIndex}`}>
                            <td>{station.id}</td>
                            <td>{station.name}</td>
                            <td>
                                {station.services && station.services.length > 0 ? (
                                    station.services.map((service, serviceIndex) => (
                                        <span key={serviceIndex} className="badge bg-secondary m-1" style={{ display: 'inline-flex', alignItems: 'center' }}>
                                                {service.name}
                                            </span>
                                    ))
                                ) : (
                                    <span>No services</span>
                                )}
                            </td>
                            <td>
                                <Button variant="primary" style={{ backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' }} onClick={() => { setShowModal(true); setSelectedBranch(branch); setSelectedStation(station); }}>Add</Button>
                            </td>
                        </tr>
                    ))
                ))}
                </tbody>
            </Table>

            <Modal show={showModal} onHide={handleCloseModal}>
                <Modal.Header closeButton style={{ backgroundColor: '#334257', color: 'white' }}>
                    <Modal.Title>Add Service to Station</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p><strong>Teller Station:</strong> {selectedStation && selectedStation.name}</p>
                    <div>
                    {selectedServices.map((service, index) => (
                            <span key={index} className="badge bg-secondary m-1" style={{ display: 'inline-flex', alignItems: 'center' }}>
                                {service.name}
                                <Button variant="danger" size="sm" style={{ marginLeft: '5px', padding: '2px 5px', backgroundColor: '#dc3545', borderColor: '#dc3545' }} onClick={() => removeSelectedService(index)}>X</Button>
                            </span>
                        ))}
                    </div>
                    <Form>
                        <Form.Group controlId="formGroupService" className="mb-3">
                            <DropdownButton
                                title={'Select Service'}
                                variant="btn btn-outline-secondary"
                            >
                                {availableServices.map((service, index) => (
                                    <Dropdown.Item key={index} onClick={() => addService(service)}>{service.name}</Dropdown.Item>
                                ))}
                            </DropdownButton>
                        </Form.Group>
                    </Form>
                    {errorMessage && <p className="text-danger">{errorMessage}</p>}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>Close</Button>
                    <Button variant="primary" style={{ backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' }} onClick={() => { handleCloseModal(); saveServiceToStation(); }}>Save</Button>
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

export default StationServiceScreen;
