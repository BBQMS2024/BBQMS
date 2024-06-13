import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form, ListGroup } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { fetchData } from '../../fetching/Fetch.js';
import { useParams } from 'react-router-dom';
import { SERVER_URL } from '../../constants.js';

const styles = {
    primaryButton: {
        backgroundColor: '#548CA8',
        borderColor: '#548CA8',
    },
    infoButton: {
        backgroundColor: '#548CA8',
        color: 'white',
        borderColor: '#548CA8',
    },
    modalHeader: {
        backgroundColor: '#334257',
        color: 'white',
    },
};

const ManageBranchesScreen = () => {
    const [showModal, setShowModal] = useState(false);
    const [manageBranches, setManageBranches] = useState([]);
    const [branchName, setBranchName] = useState('');
    const [selectedBranchIndex, setSelectedBranchIndex] = useState(null);
    const [tellerStations, setTellerStations] = useState([]);
    const [newStationName, setNewStationName] = useState('');
    const [deleteConfirmation, setDeleteConfirmation] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const { tenantCode } = useParams();

    const url = `${SERVER_URL}/api/v1/branches/${tenantCode}`;

    useEffect(() => {
        const fetchBranches = async () => {
            try {
                const response = await fetchData(url, 'GET');
                if (!response.success) {
                    throw new Error('Network response was not ok');
                }
                setManageBranches(response.data);
            } catch (error) {
                console.error('Error:', error);
                setErrorMessage('Failed to fetch branches.');
            }
        };
        fetchBranches();
    }, [SERVER_URL, url]);

    useEffect(() => {
        if (!showModal) {
            setBranchName('');
            setTellerStations([]);
            setSelectedBranchIndex(null);
            setErrorMessage('');
        }
    }, [showModal]);

    const handleEditClick = (index) => {
        const branch = manageBranches[index];
        setSelectedBranchIndex(index);
        setBranchName(branch.name);
        setTellerStations(branch.tellerStations ? [...branch.tellerStations] : []);
        setShowModal(true);
    };

    const handleAddBranch = async () => {
        if (branchName.trim() === '' || tellerStations.length === 0) {
            setErrorMessage('Please fill out all fields.');
            return;
        }

        const newBranch = {
            name: branchName,
            tellerStations: tellerStations.map(station => station.name)
        };

        try {
            const response = await fetchData(url, 'POST', newBranch);
            if (!response.success) {
                throw new Error('Network response was not ok');
            }
            setManageBranches([...manageBranches, response.data]);
            setShowModal(false);
        } catch (error) {
            console.error('Error:', error);
            setErrorMessage('Failed to create branch.');
        }
    };

    const handleAddTellerStation = () => {
        if (newStationName.trim() === '') {
            return;
        }

        const newStation = { id: tellerStations.length + 1, name: newStationName };
        setTellerStations([...tellerStations, newStation]);
        setNewStationName('');
    };

    const handleEditTellerStation = async () => {
        if (newStationName.trim() === '' || selectedBranchIndex === null) {
            return;
        }

        const branchId = manageBranches[selectedBranchIndex].id;
        const newStation = { name: newStationName };

        try {
            const response = await fetchData(`${url}/${branchId}/stations`, 'POST', newStation);
            if (!response.success) {
                throw new Error('Network response was not ok');
            }
            const updatedStations = [...tellerStations, response.data];
            setTellerStations(updatedStations);
            setNewStationName('');
        } catch (error) {
            console.error('Error:', error);
            setErrorMessage('Failed to add teller station.');
        }
    };

    const handleRemoveTellerStation = (index) => {
        const updatedStations = [...tellerStations];
        updatedStations.splice(index, 1);
        setTellerStations(updatedStations);
    };

    const handleEditRemoveTellerStation = async (index) => {
        const stationToRemove = tellerStations[index];
        const branchId = manageBranches[selectedBranchIndex].id;
        const stationId = stationToRemove.id;

        try {
            const response = await fetchData(`${url}/${branchId}/stations/${stationId}`, 'DELETE');
            if (!response.success) {
                throw new Error('Network response was not ok');
            }

            const updatedStations = [...tellerStations];
            updatedStations.splice(index, 1);
            setTellerStations(updatedStations);
        } catch (error) {
            console.error('Error:', error);
            setErrorMessage('Failed to remove teller station.');
        }
    };

    const handleDeleteBranch = (index) => {
        setDeleteConfirmation(true);
        setSelectedBranchIndex(index);
    };

    const confirmDeleteBranch = async () => {
        const branchId = manageBranches[selectedBranchIndex].id;
        const urlToDelete = `${url}/${branchId}`;

        try {
            const response = await fetchData(urlToDelete, 'DELETE');
            if (!response.success) {
                throw new Error('Network response was not ok');
            }
            const updatedBranches = [...manageBranches];
            updatedBranches.splice(selectedBranchIndex, 1);
            setManageBranches(updatedBranches);
            setDeleteConfirmation(false);
        } catch (error) {
            console.error('Error:', error);
            setErrorMessage('Failed to delete branch.');
        }
    };

    const handleEditBranch = async () => {
        if (branchName.trim() === '' || tellerStations.length === 0 || selectedBranchIndex === null) {
            setErrorMessage('Please fill out all fields.');
            return;
        }

        const branchId = manageBranches[selectedBranchIndex].id;
        const updatedBranch = {
            name: branchName
        };

        try {
            const response = await fetchData(`${url}/${branchId}`, 'PUT', updatedBranch);
            if (!response.success) {
                throw new Error('Network response was not ok');
            }
            const updatedBranches = [...manageBranches];
            updatedBranches[selectedBranchIndex] = response.data;
            setManageBranches(updatedBranches);
            setShowModal(false);
        } catch (error) {
            console.error('Error:', error);
            setErrorMessage('Failed to update branch.');
        }
    };

    return (
        <div className="text-center">
            <h2>Manage Branches</h2>
            <Button variant="primary" style={styles.primaryButton} className="mb-3" onClick={() => { setShowModal(true); setSelectedBranchIndex(null); }}>Add Branch</Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Teller Stations</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {manageBranches.map((branch, index) => (
                    <tr key={index}>
                        <td>{branch.id}</td>
                        <td>{branch.name}</td>
                        <td>{branch.tellerStations ? branch.tellerStations.map(station => station.name).join(', ') : '-'}</td>
                        <td>
                            <Button variant="info" style={styles.infoButton} onClick={() => handleEditClick(index)}>Edit</Button>{' '}
                            <Button variant="danger" onClick={() => handleDeleteBranch(index)}>Delete</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <Modal show={showModal} onHide={() => { setShowModal(false); setSelectedBranchIndex(null); }}>
                <Modal.Header closeButton style={styles.modalHeader}>
                    <Modal.Title>{selectedBranchIndex !== null ? 'EDIT BRANCH' : 'ADD BRANCH'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="formBranchName" className="mb-3">
                            <Form.Label>Name</Form.Label>
                            <Form.Control type="text" placeholder="Enter branch name" value={branchName} onChange={(e) => setBranchName(e.target.value)} />
                        </Form.Group>
                        <Form.Group controlId="formNewStation" className="mb-3">
                            <Form.Label>New Teller Station</Form.Label>
                            <div className="d-flex align-items-center">
                                <Form.Control type="text" placeholder="Enter new station name" value={newStationName} onChange={(e) => setNewStationName(e.target.value)} />
                                <Button variant="primary" onClick={selectedBranchIndex !== null ? handleEditTellerStation : handleAddTellerStation} style={{ backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8', marginLeft: '10px' }}>Add</Button>
                            </div>
                        </Form.Group>
                        <Form.Group controlId="formTellerStations" className="mb-3">
                            <Form.Label>Teller Stations</Form.Label>
                            <ListGroup>
                                {tellerStations.map((station, index) => (
                                    <ListGroup.Item key={index}>
                                        {station.name}{' '}
                                        {selectedBranchIndex !== null ?
                                            <Button variant="danger" size="sm" onClick={() => handleEditRemoveTellerStation(index)}>X</Button> :
                                            <Button variant="danger" size="sm" onClick={() => handleRemoveTellerStation(index)}>X</Button>
                                        }
                                    </ListGroup.Item>
                                ))}
                            </ListGroup>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => { setShowModal(false); setSelectedBranchIndex(null); }}>Close</Button>
                    {selectedBranchIndex !== null ?
                        <Button variant="primary" style={styles.primaryButton} onClick={handleEditBranch}>Save Changes</Button> :
                        <Button variant="primary" style={styles.primaryButton} onClick={handleAddBranch}>Add Branch</Button>
                    }
                </Modal.Footer>
            </Modal>

            <Modal show={deleteConfirmation} onHide={() => setDeleteConfirmation(false)}>
                <Modal.Header closeButton style={{ backgroundColor: '#334257', color: 'white' }}>
                    <Modal.Title>CONFIRMATION</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>Are you sure you want to delete this branch?</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setDeleteConfirmation(false)}>Cancel</Button>
                    <Button variant="danger" onClick={confirmDeleteBranch}>Delete</Button>
                </Modal.Footer>
            </Modal>

            <Modal show={errorMessage !== ''} onHide={() => setErrorMessage('')} backdrop="static" keyboard={false}>
                <Modal.Header closeButton style={{ backgroundColor: '#dc3545', color: 'white' }}>
                    <Modal.Title>ERROR</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>{errorMessage}</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setErrorMessage('')}>Close</Button>
                </Modal.Footer>
            </Modal>

        </div>
    );
};

export default ManageBranchesScreen;
