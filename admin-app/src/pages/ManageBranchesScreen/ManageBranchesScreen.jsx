import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form, ListGroup } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

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

    // Dummy podaci za testiranje, kasnije ce se prilagoditi
    const dummyData = [
        { id: 1, name: 'Branch 1', tellerStations: [{ id: 1, name: 'Station 1' }, { id: 2, name: 'Station 2' }] },
        { id: 2, name: 'Branch 2', tellerStations: [{ id: 3, name: 'Station 3' }, { id: 4, name: 'Station 4' }] }
    ];

    useEffect(() => {
        // Simulacija...
        setManageBranches(dummyData);
    }, []);

    const handleAddBranch = () => {
        const newBranch = {
            id: manageBranches.length + 1,
            name: branchName,
            tellerStations: tellerStations.map(station => ({ ...station }))
        };
        setManageBranches([...manageBranches, newBranch]);
        setShowModal(false);
        setBranchName('');
        setTellerStations([]);
    };

    const handleEditBranch = () => {
        const updatedBranches = [...manageBranches];
        updatedBranches[selectedBranchIndex] = {
            ...updatedBranches[selectedBranchIndex],
            name: branchName,
            tellerStations: tellerStations.map(station => ({ ...station }))
        };
        setManageBranches(updatedBranches);
        setShowModal(false);
        setBranchName('');
        setTellerStations([]);
        setSelectedBranchIndex(null);
    };

    const handleDeleteBranch = (index) => {
        const updatedBranches = [...manageBranches];
        updatedBranches.splice(index, 1);
        setManageBranches(updatedBranches);
    };

    const handleEditClick = (index) => {
        const branch = manageBranches[index];
        setSelectedBranchIndex(index);
        setBranchName(branch.name);
        setTellerStations(branch.tellerStations ? [...branch.tellerStations] : []);
        setShowModal(true);
    };

    const handleAddTellerStation = () => {
        const newStation = { id: tellerStations.length + 1, name: `Station ${tellerStations.length + 1}` };
        setTellerStations([...tellerStations, newStation]);
    };

    const handleRemoveTellerStation = (index) => {
        const updatedStations = [...tellerStations];
        updatedStations.splice(index, 1);
        setTellerStations(updatedStations);
    };

    return (
        <div className="text-center mt-5">
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
                        <Form.Group controlId="formTellerStations" className="mb-3">
                            <Form.Label>Teller Stations</Form.Label>
                            <ListGroup>
                                {tellerStations.map((station, index) => (
                                    <ListGroup.Item key={index}>
                                        {station.name}{' '}
                                        <Button variant="danger" size="sm" onClick={() => handleRemoveTellerStation(index)}>Remove</Button>
                                    </ListGroup.Item>
                                ))}
                            </ListGroup>
                            <Button variant="primary" size="sm" style={styles.primaryButton} onClick={handleAddTellerStation} className="mt-2">Add Teller Station</Button>
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
        </div>
    );
};

export default ManageBranchesScreen;
