import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form, Dropdown, DropdownButton } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

const ManageGroupsScreen = () => {
    const [showModal, setShowModal] = useState(false);
    const [deleteConfirmation, setDeleteConfirmation] = useState(false);
    const [groups, setGroups] = useState([]);
    const [groupName, setGroupName] = useState('');
    const [groupDescription, setGroupDescription] = useState('');
    const [selectedBranches, setSelectedBranches] = useState([]);
    const [selectedServices, setSelectedServices] = useState([]);
    const [availableBranches, setAvailableBranches] = useState([
        { id: 1, name: 'Branch 1' },
        { id: 2, name: 'Branch 2' },
        { id: 3, name: 'Branch 3' },
        { id: 4, name: 'Branch 4' },
        { id: 5, name: 'Branch 5' }
    ]);
    const [availableServices, setAvailableServices] = useState([
        { id: 1, name: 'Service 1' },
        { id: 2, name: 'Service 2' },
        { id: 3, name: 'Service 3' },
        { id: 4, name: 'Service 4' }
    ]);
    const [selectedGroupIndex, setSelectedGroupIndex] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        // Dummy data to simulate API call
        const dummyData = [
            { id: 1, name: 'Group 1', description: 'Description 1', branches: [{ id: 1, name: 'Branch 1' }, { id: 2, name: 'Branch 2' }], services: [{ id: 1, name: 'Service 1' }, { id: 2, name: 'Service 2' }] },
            { id: 2, name: 'Group 2', description: 'Description 2', branches: [{ id: 3, name: 'Branch 3' }, { id: 4, name: 'Branch 4' }], services: [{ id: 3, name: 'Service 3' }, { id: 4, name: 'Service 4' }] }
        ];
        setGroups(dummyData);
    }, []);

    useEffect(() => {
        if (!showModal) {
            // Reset modal state when it closes
            setGroupName('');
            setGroupDescription('');
            setSelectedBranches([]);
            setSelectedServices([]);
            setSelectedGroupIndex(null);
            setErrorMessage('');
        }
    }, [showModal]);

    const handleAddGroup = () => {
        if (groupName.trim() === '' || groupDescription.trim() === '' || selectedBranches.length === 0 || selectedServices.length === 0) {
            setErrorMessage('Please fill out all fields.');
            return;
        }

        const newGroup = { id: groups.length + 1, name: groupName, description: groupDescription, branches: selectedBranches, services: selectedServices };
        setGroups([...groups, newGroup]);
        setShowModal(false);
    };

    const handleEditGroup = () => {
        if (groupName.trim() === '' || groupDescription.trim() === '' || selectedBranches.length === 0 || selectedServices.length === 0) {
            setErrorMessage('Please fill out all fields.');
            return;
        }

        const updatedGroups = [...groups];
        updatedGroups[selectedGroupIndex] = { ...updatedGroups[selectedGroupIndex], name: groupName, description: groupDescription, branches: selectedBranches, services: selectedServices };
        setGroups(updatedGroups);
        setShowModal(false);
    };

    const handleDeleteGroup = (index) => {
        setDeleteConfirmation(true);
        setSelectedGroupIndex(index);
    };

    const confirmDeleteGroup = () => {
        const updatedGroups = [...groups];
        updatedGroups.splice(selectedGroupIndex, 1);
        setGroups(updatedGroups);
        setDeleteConfirmation(false);
    };

    const handleEditClick = (index) => {
        setSelectedGroupIndex(index);
        setGroupName(groups[index].name);
        setGroupDescription(groups[index].description);
        setSelectedBranches(groups[index].branches);
        setSelectedServices(groups[index].services);
        setShowModal(true);
    };

    const handleBranchSelection = (branchId) => {
        const selectedBranch = availableBranches.find(branch => branch.id === parseInt(branchId));
        if (selectedBranch && !selectedBranches.some(branch => branch.id === selectedBranch.id)) {
            setSelectedBranches([...selectedBranches, selectedBranch]);
        }
    };

    const handleRemoveBranch = (branch) => {
        const updatedBranches = selectedBranches.filter(selectedBranch => selectedBranch.id !== branch.id);
        setSelectedBranches(updatedBranches);
    };

    const handleServiceSelection = (serviceId) => {
        const selectedService = availableServices.find(service => service.id === parseInt(serviceId));
        if (selectedService && !selectedServices.some(service => service.id === selectedService.id)) {
            setSelectedServices([...selectedServices, selectedService]);
        }
    };

    const handleRemoveService = (service) => {
        const updatedServices = selectedServices.filter(selectedService => selectedService.id !== service.id);
        setSelectedServices(updatedServices);
    };

    return (
        <div className="text-center mt-5">
            <h2>Groups of Branches</h2>
            <Button variant="primary" style={{ backgroundColor: '#548CA8', borderColor: '#548CA8' }} className="mb-3" onClick={() => { setShowModal(true); }}>Add Group</Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Branches</th>
                    <th>Services</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {groups.map((group, index) => (
                    <tr key={index}>
                        <td>{group.id}</td>
                        <td>{group.name}</td>
                        <td>{group.description}</td>
                        <td>{group.branches.map(branch => branch.name).join(', ')}</td>
                        <td>{group.services.map(service => service.name).join(', ')}</td>
                        <td>
                            <Button variant="info" style={{ backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' }} onClick={() => handleEditClick(index)}>Edit</Button>{' '}
                            <Button variant="danger" onClick={() => handleDeleteGroup(index)}>Delete</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <Modal show={showModal} onHide={() => { setShowModal(false); }}>
                <Modal.Header closeButton style={{ backgroundColor: '#334257', color: 'white' }}>
                    <Modal.Title>{selectedGroupIndex !== null ? 'EDIT GROUP' : 'ADD GROUP'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="formGroupName" className="mb-3">
                            <Form.Label>Name</Form.Label>
                            <Form.Control type="text" placeholder="Enter group name" value={groupName} onChange={(e) => setGroupName(e.target.value)} />
                        </Form.Group>
                        <Form.Group controlId="formGroupDescription" className="mb-3">
                            <Form.Label>Description</Form.Label>
                            <Form.Control type="text" placeholder="Enter group description" value={groupDescription} onChange={(e) => setGroupDescription(e.target.value)} />
                        </Form.Group>
                        <Form.Group controlId="formGroupBranches" className="mb-3">
                            {selectedBranches.map((branch, index) => (
                                <span key={index} className="badge bg-secondary m-1" style={{ display: 'inline-flex', alignItems: 'center' }}>
                                    {branch.name}{' '}
                                    <Button variant="danger" size="sm" style={{ marginLeft: '5px', padding: '2px 5px', backgroundColor: '#dc3545', borderColor: '#dc3545' }} onClick={() => handleRemoveBranch(branch)}>X</Button>
                                </span>
                            ))}
                            <DropdownButton
                                title="Select Branches"
                                onSelect={(eventKey) => handleBranchSelection(eventKey)}
                                variant="btn btn-outline-secondary"
                            >
                                {availableBranches.map((branch, index) => (
                                    <Dropdown.Item key={index} eventKey={branch.id}>{branch.name}</Dropdown.Item>
                                ))}
                            </DropdownButton>
                        </Form.Group>
                        <Form.Group controlId="formGroupServices" className="mb-3">
                            {selectedServices.map((service, index) => (
                                <span key={index} className="badge bg-secondary m-1" style={{ display: 'inline-flex', alignItems: 'center' }}>
                                    {service.name}{' '}
                                    <Button variant="danger" size="sm" style={{ marginLeft: '5px', padding: '2px 5px', backgroundColor: '#dc3545', borderColor: '#dc3545' }} onClick={() => handleRemoveService(service)}>X</Button>
                                </span>
                            ))}
                            <DropdownButton
                                title="Select Services"
                                onSelect={(eventKey) => handleServiceSelection(eventKey)}
                                variant="btn btn-outline-secondary"
                            >
                                {availableServices.map((service, index) => (
                                    <Dropdown.Item key={index} eventKey={service.id}>{service.name}</Dropdown.Item>
                                ))}
                            </DropdownButton>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => { setShowModal(false); }}>Close</Button>
                    {selectedGroupIndex !== null ?
                        <Button variant="primary" style={{ backgroundColor: '#548CA8', borderColor: '#548CA8' }} onClick={handleEditGroup}>Save Changes</Button> :
                        <Button variant="primary" style={{ backgroundColor: '#548CA8', borderColor: '#548CA8' }} onClick={handleAddGroup}>Add Group</Button>
                    }
                </Modal.Footer>
            </Modal>

            <Modal show={deleteConfirmation} onHide={() => setDeleteConfirmation(false)}>
                <Modal.Header closeButton style={{ backgroundColor: '#334257', color: 'white' }}>
                    <Modal.Title>CONFIRMATION</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>Are you sure you want to delete this group?</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setDeleteConfirmation(false)}>Cancel</Button>
                    <Button variant="danger" onClick={confirmDeleteGroup}>Delete</Button>
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

export default ManageGroupsScreen;
