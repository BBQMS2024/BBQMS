import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form, Dropdown, DropdownButton } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { fetchData } from '../../fetching/Fetch.js';
import { useLocation } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import { SERVER_URL } from '../../constants.js';

const ManageStationScreen = () => {
    const location = useLocation();
    const [showServiceModal, setShowServiceModal] = useState(false);
    const [showDisplayModal, setShowDisplayModal] = useState(false);
    const [deleteConfirmation, setDeleteConfirmation] = useState(false);
    const [stations, setStations] = useState([]);
    const [selectedStation, setSelectedStation] = useState(null);
<<<<<<< HEAD
=======
    const [selectedBranch, setSelectedBranch] = useState(null);
    const [branches, setBranches] = useState([]);
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
    const [selectedServices, setSelectedServices] = useState([]);
    const [availableServices, setAvailableServices] = useState([]);
    const [availableDisplays, setAvailableDisplays] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const { tenantCode } = useParams();

    const url = `${ SERVER_URL }/api/v1/`;

    useEffect(() => {
<<<<<<< HEAD
        async function fetchStations() {
            try {
                const response = await fetchData(`${ url }stations/${ tenantCode }`, 'GET');
=======
        async function fetchStationsForBranch() {
            try {
                const response = await fetchData(`${ url }stations/${ tenantCode }/${ selectedBranch.id }`, 'GET');
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
                if (response.success) {
                    setStations(response.data);
                } else {
                    console.error('Error fetching stations:', response.error);
                }
            } catch (error) {
                console.error('Error fetching stations:', error);
            }
        }

<<<<<<< HEAD
        async function fetchAvailableDisplays() {
            try {
                const response = await fetchData(`${ url }displays/unassigned/${ tenantCode }`, 'GET');
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
        fetchAvailableDisplays();
    }, [location]);

    async function fetchAvailableServices(station) {
        try {
            const response = await fetchData(`${ url }stations/${ tenantCode }/${station.id}/services?assigned=false`, 'GET');
=======
        if (selectedBranch) {
            fetchStationsForBranch();
            fetchAvailableDisplays(selectedBranch);
        } else {
            setStations([]);
        }
    }, [selectedBranch]);

    useEffect(() => {
        async function fetchBranches() {
            try {
                const response = await fetchData(`${ url }branches/${ tenantCode }`, 'GET');
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
    }, [location]);

    async function fetchAvailableDisplays(branch) {
        try {
            const response = await fetchData(`${ url }displays/unassigned/${ tenantCode }/${ branch.id }`, 'GET');
            if (response.success) {
                setAvailableDisplays(response.data);
            } else {
                console.error('Error fetching available displays:', response.error);
            }
        } catch (error) {
            console.error('Error fetching available displays:', error);
        }
    }

    async function fetchAvailableServices(station) {
        try {
            const response = await fetchData(`${ url }stations/${ tenantCode }/${ station.id }/services?assigned=false`, 'GET');
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
            if (response.success) {
                setAvailableServices(response.data);
            } else {
                console.error('Error fetching available services:', response.error);
            }
        } catch (error) {
            console.error('Error fetching available services:', error);
        }
    }

    useEffect(() => {
        setSelectedServices(selectedStation ? selectedStation.services : []);
        if (selectedStation) {
            fetchAvailableServices(selectedStation);
        }
    }, [selectedStation]);

    const confirmDeleteStation = () => {
    };

    const saveServiceToStation = async (service) => {
        try {
            const response = await fetchData(`${ url }stations/${ tenantCode }/${ selectedStation.id }/services/${ service.id }`, 'PUT');
            if (response.success) {
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
            const response = await fetchData(`${ url }stations/${ tenantCode }/${ selectedStation.id }/displays/${ display.id }`, 'PUT');
            if (response.success) {
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
                setAvailableDisplays(prevDisplays => prevDisplays.filter(d => d.id !== display.id));
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
            const response = await fetchData(`${ url }stations/${ tenantCode }/${ selectedStation.id }/services/${ serviceId }`, 'DELETE');
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
            const response = await fetchData(`${ url }stations/${ tenantCode }/${ selectedStation.id }/displays/${ selectedStation.display.id }`, 'DELETE');
            if (response.success) {
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
<<<<<<< HEAD
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
                { stations.map((station, index) => (
                    <tr key={ index }>
                        <td>{ station.id }</td>
                        <td>{ station.name }</td>
                        <td>
                            { station.services && station.services.length > 0 ? (
                                station.services.map((service, serviceIndex) => (
                                    <span key={ serviceIndex } style={ { marginRight: '5px' } }>
                                            { serviceIndex > 0 && ', ' }
                                        { service.name }
                                        </span>
                                ))
                            ) : (
                                <span>No services</span>
                            ) }
                        </td>
                        <td>
                            <Button
                                variant="primary"
                                style={ { backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' } }
                                onClick={ () => {
                                    setShowServiceModal(true);
                                    setSelectedStation(station);
                                } }
                            >
                                Edit
                            </Button>
                        </td>
                        <td>
                            { station.display ? (
                                <span>{ station.display.name }</span>
                            ) : (
                                <span>No display</span>
                            ) }
                        </td>
                        <td>
                            <Button
                                variant="primary"
                                style={ { backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' } }
                                onClick={ () => {
                                    setShowDisplayModal(true);
                                    setSelectedStation(station);
                                } }
                            >
                                Edit
                            </Button>
                        </td>
                    </tr>
                )) }
                </tbody>
            </Table>

=======
            <DropdownButton title={ selectedBranch ? selectedBranch.name : 'Select Branch' }
                            variant="btn btn-outline-secondary">
                { branches.map((branch, index) => (
                    <Dropdown.Item key={ index }
                                   onClick={ () => setSelectedBranch(branch) }>{ branch.name }</Dropdown.Item>
                )) }
            </DropdownButton>
            <div style={ { marginTop: '20px' } }>
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
                    { stations.map((station, index) => (
                        <tr key={ index }>
                            <td>{ station.id }</td>
                            <td>{ station.name }</td>
                            <td>
                                { station.services && station.services.length > 0 ? (
                                    station.services.map((service, serviceIndex) => (
                                        <span key={ serviceIndex } style={ { marginRight: '5px' } }>
                                            { serviceIndex > 0 && ', ' }
                                            { service.name }
                                        </span>
                                    ))
                                ) : (
                                    <span>No services</span>
                                ) }
                            </td>
                            <td>
                                <Button
                                    variant="primary"
                                    style={ { backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' } }
                                    onClick={ () => {
                                        setShowServiceModal(true);
                                        setSelectedStation(station);
                                    } }
                                >
                                    Edit
                                </Button>
                            </td>
                            <td>
                                { station.display ? (
                                    <span>{ station.display.name }</span>
                                ) : (
                                    <span>No display</span>
                                ) }
                            </td>
                            <td>
                                <Button
                                    variant="primary"
                                    style={ { backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' } }
                                    onClick={ () => {
                                        setShowDisplayModal(true);
                                        setSelectedStation(station);
                                    } }
                                >
                                    Edit
                                </Button>
                            </td>
                        </tr>
                    )) }
                    </tbody>
                </Table>
            </div>
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
            <Modal show={ showServiceModal } onHide={ handleCloseModal }>
                <Modal.Header closeButton style={ { backgroundColor: '#334257', color: 'white' } }>
                    <Modal.Title>Manage Services for Station</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p><strong>Teller Station:</strong> { selectedStation && selectedStation.name }</p>
                    <div>
                        <div style={ { marginBottom: '10px' } }>
                            <strong>Selected Services:</strong>
                            { selectedServices.length > 0 ? (
                                selectedServices.map((service, index) => (
                                    <span key={ index } className="badge bg-secondary m-1"
                                          style={ { display: 'inline-flex', alignItems: 'center' } }>
                        { service.name }
                                        <Button variant="danger" size="sm" style={ {
                                            marginLeft: '5px',
                                            padding: '2px 5px',
                                            backgroundColor: '#dc3545',
                                            borderColor: '#dc3545'
                                        } } onClick={ () => removeSelectedService(service.id) }>X</Button>
                                </span>
                                ))
                            ) : (
                                <span> No service selected</span>
                            ) }
                        </div>
                        <Form.Group controlId="formGroupService" className="mb-3">
<<<<<<< HEAD
                            <DropdownButton title={'Select Service'} variant="btn btn-outline-secondary">
                                {availableServices.map((service, index) => {
                                    const isAssigned = selectedServices.some(selectedService => selectedService.id === service.id);
                                    if (!isAssigned) {
                                        return (
                                            <Dropdown.Item key={index} onClick={() => addService(service)}>{service.name}</Dropdown.Item>
=======
                            <DropdownButton title={ 'Select Service' } variant="btn btn-outline-secondary">
                                { availableServices.map((service, index) => {
                                    const isAssigned = selectedServices.some(selectedService => selectedService.id === service.id);
                                    if (!isAssigned) {
                                        return (
                                            <Dropdown.Item key={ index }
                                                           onClick={ () => addService(service) }>{ service.name }</Dropdown.Item>
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
                                        );
                                    } else {
                                        return null;
                                    }
<<<<<<< HEAD
                                })}
=======
                                }) }
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
                            </DropdownButton>
                        </Form.Group>

                        { errorMessage && <p className="text-danger">{ errorMessage }</p> }
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={ handleCloseModal }>Close</Button>
                </Modal.Footer>
            </Modal>

            <Modal show={ showDisplayModal } onHide={ handleCloseModal }>
                <Modal.Header closeButton style={ { backgroundColor: '#334257', color: 'white' } }>
                    <Modal.Title>Manage Display for Station</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p><strong>Teller Station:</strong> { selectedStation && selectedStation.name }</p>
                    <div>
                        <div style={ { marginBottom: '10px' } }>
                            <strong>Selected Display:</strong>
                            { selectedStation && selectedStation.display ? (
                                <span className="badge bg-secondary m-1" style={ {
                                    display: 'inline-flex',
                                    alignItems: 'center'
                                } }> { selectedStation.display.name }
                                    <Button variant="danger" size="sm" style={ {
                                        marginLeft: '5px',
                                        padding: '2px 5px',
                                        backgroundColor: '#dc3545',
                                        borderColor: '#dc3545'
                                    } } onClick={ removeDisplayFromStation }>X</Button>
                                </span>
                            ) : (
                                <span> No display selected</span>
                            ) }
                        </div>
                        <Form>
                            <Form.Group controlId="formGroupDisplay" className="mb-3">
                                <DropdownButton
                                    title={ 'Select Display' }
                                    variant="btn btn-outline-secondary"
                                >
                                    { availableDisplays.map((display, index) => (
                                        <Dropdown.Item key={ index }
                                                       onClick={ () => addDisplayToStation(display) }>{ display.name }</Dropdown.Item>
                                    )) }
                                </DropdownButton>


                            </Form.Group>
                        </Form>
                        { errorMessage && <p className="text-danger">{ errorMessage }</p> }
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={ handleCloseModal }>Close</Button>
                </Modal.Footer>
            </Modal>

            <Modal show={ deleteConfirmation } onHide={ () => setDeleteConfirmation(false) }>
                <Modal.Header closeButton style={ { backgroundColor: '#334257', color: 'white' } }>
                    <Modal.Title>Confirmation</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Are you sure you want to delete this station?
                </Modal.Body>
                <Modal.Footer style={ { backgroundColor: '#334257', color: 'white' } }>
                    <Button variant="secondary" onClick={ () => setDeleteConfirmation(false) }>Cancel</Button>
                    <Button variant="danger" onClick={ confirmDeleteStation }>Delete</Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default ManageStationScreen;
