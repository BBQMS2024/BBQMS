import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form, Dropdown, DropdownButton } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { fetchData } from '../../fetching/Fetch.js';
import { useParams } from 'react-router-dom';
import { SERVER_URL } from '../../constants.js';

const ManageGroupsScreen = () => {
    const [showModal, setShowModal] = useState(false);
    const [deleteConfirmation, setDeleteConfirmation] = useState(false);
    const [groups, setGroups] = useState([]);
    const [groupName, setGroupName] = useState('');
    const [selectedBranches, setSelectedBranches] = useState([]);
    const [selectedServices, setSelectedServices] = useState([]);
    const [availableBranches, setAvailableBranches] = useState([]);
    const [availableServices, setAvailableServices] = useState([]);
    const [selectedGroupIndex, setSelectedGroupIndex] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');
    const { tenantCode } = useParams();

    const url = `${SERVER_URL}/api/v1/`;

    useEffect(() => {
        async function fetchGroups() {
            try {
                const response = await fetchData(`${url}groups/${tenantCode}`, 'GET');
                if (response.success) {
                    setGroups(response.data);
                } else {
                    console.error('Error fetching groups:', response.error);
                }
            } catch (error) {
                console.error('Error fetching groups:', error);
            }
        }

        async function fetchBranchesAndServices() {
            try {
                const branchResponse = await fetchData(`${url}branches/${tenantCode}`, 'GET');
                const serviceResponse = await fetchData(`${url}tenants/${tenantCode}/services`, 'GET');

                if (branchResponse.success) {
                    setAvailableBranches(branchResponse.data);
                } else {
                    console.error('Error fetching branches:', branchResponse.error);
                }

                if (serviceResponse.success) {
                    setAvailableServices(serviceResponse.data);
                } else {
                    console.error('Error fetching services:', serviceResponse.error);
                }
            } catch (error) {
                console.error('Error fetching branches and services:', error);
            }
        }

        fetchGroups();
        fetchBranchesAndServices();
    }, [SERVER_URL, url]);

    useEffect(() => {
        if (!showModal) {
            // Reset modal state when it closes
            setGroupName('');
            setSelectedBranches([]);
            setSelectedServices([]);
            setSelectedGroupIndex(null);
            setErrorMessage('');
        }
    }, [showModal]);

    const handleAddGroup = async () => {
        if (groupName.trim() === '' || selectedBranches.length === 0 || selectedServices.length === 0) {
            setErrorMessage('Please fill out all fields.');
            return;
        }

        try {
            const groupData = {
                name: groupName,
                branchIds: selectedBranches.map(branch => branch.id),
                serviceIds: selectedServices.map(service => service.id)
            };
            const response = await fetchData(`${url}groups/${tenantCode}`, 'POST', groupData);
            if (response.success) {
                const newGroup = response.data;
                setGroups([...groups, newGroup]);
                setShowModal(false);
            } else {
                console.error('Error adding group:', response.error);
            }
        } catch (error) {
            console.error('Error adding group:', error);
        }
    };

    const handleEditGroup = async () => {
        if (groupName.trim() === '' || selectedBranches.length === 0 || selectedServices.length === 0) {
            setErrorMessage('Please fill out all fields.');
            return;
        }

        try {
            const tenantCode = location.pathname.split('/')[1];
            const groupToUpdate = groups[selectedGroupIndex];
            const updatedGroup = {
                ...groupToUpdate,
                name: groupName,
                branches: selectedBranches,
                services: selectedServices
            };
            const response = await fetchData(`${url}groups/${tenantCode}/${groupToUpdate.id}`, 'PUT', updatedGroup);
            if (response.success) {
                const updatedGroups = [...groups];
                updatedGroups[selectedGroupIndex] = updatedGroup;
                setGroups(updatedGroups);
                setShowModal(false);
            } else {
                console.error('Error updating group:', response.error);
            }
        } catch (error) {
            console.error('Error updating group:', error);
        }
    };

    const handleDeleteGroup = async (index) => {
        setSelectedGroupIndex(index);
        setDeleteConfirmation(true);
    };

    const confirmDeleteGroup = async () => {
        try {
            const groupToDelete = groups[selectedGroupIndex];
            const response = await fetchData(`${url}groups/${tenantCode}/${groupToDelete.id}`, 'DELETE');
            if (response.success) {
                const updatedGroups = groups.filter((_, i) => i !== selectedGroupIndex);
                setGroups(updatedGroups);
            } else {
                console.error('Error deleting group:', response.error);
            }
        } catch (error) {
            console.error('Error deleting group:', error);
        }
        setSelectedGroupIndex(null);
        setDeleteConfirmation(false);
    };


    const handleEditClick = (index) => {
        setSelectedGroupIndex(index);
        setGroupName(groups[index].name);
        setSelectedBranches(groups[index].branches);
        setSelectedServices(groups[index].services);
        setShowModal(true);
    };

    const handleBranchSelection = async (branchId) => {
        const selectedBranch = availableBranches.find(branch => branch.id === parseInt(branchId));
        if (selectedBranch && !selectedBranches.some(branch => branch.id === selectedBranch.id)) {
            setSelectedBranches([...selectedBranches, selectedBranch]);
            try {
                const groupToUpdate = groups[selectedGroupIndex];
                const response = await fetchData(`${url}groups/${tenantCode}/${groupToUpdate.id}/branches/${branchId}`, 'PUT');
                if (!response.success) {
                    console.error('Error adding branch to group:', response.error);
                }
            } catch (error) {
                console.error('Error adding branch to group:', error);
            }
        }
    };

    const handleRemoveBranch = async (branch) => {
        const updatedBranches = selectedBranches.filter(selectedBranch => selectedBranch.id !== branch.id);
        setSelectedBranches(updatedBranches);
        try {
            const groupToUpdate = groups[selectedGroupIndex];
            const response = await fetchData(`${url}groups/${tenantCode}/${groupToUpdate.id}/branches/${branch.id}`, 'DELETE');
            if (!response.success) {
                console.error('Error removing branch from group:', response.error);
            }
        } catch (error) {
            console.error('Error removing branch from group:', error);
        }
    };

    const handleServiceSelection = async (serviceId) => {
        const selectedService = availableServices.find(service => service.id === parseInt(serviceId));
        if (selectedService && !selectedServices.some(service => service.id === selectedService.id)) {
            setSelectedServices([...selectedServices, selectedService]);
            try {
                const groupToUpdate = groups[selectedGroupIndex];
                const response = await fetchData(`${url}groups/${tenantCode}/${groupToUpdate.id}/services/${serviceId}`, 'PUT');
                if (!response.success) {
                    console.error('Error adding service to group:', response.error);
                }
            } catch (error) {
                console.error('Error adding service to group:', error);
            }
        }
    };

    const handleRemoveService = async (service) => {
        const updatedServices = selectedServices.filter(selectedService => selectedService.id !== service.id);
        setSelectedServices(updatedServices);
        try {
            const tenantCode = location.pathname.split('/')[1];
            const groupToUpdate = groups[selectedGroupIndex];
            const response = await fetchData(`${url}groups/${tenantCode}/${groupToUpdate.id}/services/${service.id}`, 'DELETE');
            if (!response.success) {
                console.error('Error removing service from group:', response.error);
            }
        } catch (error) {
            console.error('Error removing service from group:', error);
        }
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