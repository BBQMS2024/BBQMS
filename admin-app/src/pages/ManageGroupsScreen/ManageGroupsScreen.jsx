import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form, Dropdown, DropdownButton } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { fetchData } from '../../fetching/Fetch.js';
import { useParams } from 'react-router-dom';
import { SERVER_URL } from '../../constants.js';

export default function ManageGroupsScreen() {
    const [showModal, setShowModal] = useState(false);
    const [deleteConfirmation, setDeleteConfirmation] = useState(false);
    const [groups, setGroups] = useState([]);
    const [groupName, setGroupName] = useState('');
    const [selectedBranches, setSelectedBranches] = useState([]);
    const [selectedServices, setSelectedServices] = useState([]);
    const [availableBranches, setAvailableBranches] = useState([]);
    const [availableServices, setAvailableServices] = useState([]);
    const [selectedGroup, setSelectedGroup] = useState();
    const [errorMessage, setErrorMessage] = useState('');

    const { tenantCode } = useParams();

    const url = `${ SERVER_URL }/api/v1/`;

    useEffect(() => {
        fetchGroups();
    }, []);

    useEffect(() => {
        if (!showModal) {
            // Reset modal state when it closes
            setGroupName('')
            setSelectedGroup(undefined)
            setSelectedBranches([])
            setSelectedServices([])
            setAvailableBranches([])
            setAvailableServices([])
            setErrorMessage('')
        }
    }, [showModal]);

    const isValid = groupName.trim() !== '' && selectedBranches.length !== 0 && selectedServices.length !== 0

    function fetchAvailableBranches(group) {
        fetchData(`${ url }groups/${ tenantCode }/${ group.id }/assignable/branch`)
            .then(response => response.data)
            .then(setAvailableBranches)
            .catch(console.error)
    }

    function fetchAvailableServices(group) {
        fetchData(`${ url }groups/${ tenantCode }/${ group.id }/assignable/service`)
            .then(response => response.data)
            .then(setAvailableServices)
            .catch(console.error)
    }

    function fetchGroups() {
        fetchData(`${ url }groups/${ tenantCode }`, 'GET')
            .then(response => response.data)
            .then(setGroups)
    }

    function handleAddGroup() {
        if (!isValid) {
            setErrorMessage('Please fill out all fields.');
            return;
        }

        const groupData = {
            name: groupName,
            branchIds: selectedBranches.map(branch => branch.id),
            serviceIds: selectedServices.map(service => service.id)
        };

        fetchData(`${ url }groups/${ tenantCode }`, 'POST', groupData)
            .then(fetchGroups)
            .catch(console.error)

        setShowModal(false)
    }

    function handleEditGroup() {
        if (!isValid) {
            setErrorMessage('Please fill out all fields.');
            return
        }

        const updatedGroup = {
            ...selectedGroup,
            name: groupName,
            branches: selectedBranches,
            services: selectedServices
        };

        fetchData(`${ url }groups/${ tenantCode }/${ selectedGroup?.id }`, 'PUT', updatedGroup)
            .then(fetchGroups)
            .catch(console.error)

        setShowModal(false)
    }

    function handleDeleteGroup(group) {
        setSelectedGroup(group)
        setDeleteConfirmation(true);
    }

    function confirmDeleteGroup() {
        fetchData(`${ url }groups/${ tenantCode }/${ selectedGroup?.id }`, 'DELETE')
            .then(fetchGroups)
            .catch(console.error)

        setDeleteConfirmation(false)
        setSelectedGroup(undefined)
    }

    function handleEditClick(group) {
        setSelectedGroup(group)
        setGroupName(group.name)

        setSelectedBranches(group.branches)
        setSelectedServices(group.services)

        fetchAvailableBranches(group)
        fetchAvailableServices(group)

        setShowModal(true)
    }

    function handleBranchSelection(branchId) {
        const branchToAdd = availableBranches.find(branch => branch.id == branchId)
        setSelectedBranches([ ...selectedBranches, branchToAdd ])

        if (selectedGroup) {
            fetchData(`${ url }groups/${ tenantCode }/${ selectedGroup.id }/branches/${ branchId }`, 'PUT')
                .then(() => fetchAvailableBranches(selectedGroup))
                .then(fetchGroups)
                .catch(console.error)
        } else {
            const updatedAvailable = availableBranches.filter(branch => branch.id != branchId)
            setAvailableBranches(updatedAvailable)
        }
    }

    function handleRemoveBranch(branch) {
        const updatedBranches = selectedBranches.filter(selectedBranch => selectedBranch.id !== branch.id);
        setSelectedBranches(updatedBranches);

        if (selectedGroup) {
            fetchData(`${ url }groups/${ tenantCode }/${ selectedGroup?.id }/branches/${ branch.id }`, 'DELETE')
                .then(() => fetchAvailableBranches(selectedGroup))
                .then(fetchGroups)
                .catch(console.error)
        } else {
            setAvailableBranches([ ...availableBranches, branch ])
        }
    }

    function handleServiceSelection(serviceId) {
        const serviceToAdd = availableServices.find(service => service.id == serviceId)
        setSelectedServices([ ...selectedServices, serviceToAdd ])

        if (selectedGroup) {
            fetchData(`${ url }groups/${ tenantCode }/${ selectedGroup.id }/services/${ serviceId }`, 'PUT')
                .then(() => fetchAvailableServices(selectedGroup))
                .then(fetchGroups)
                .catch(console.error)
        } else {
            const updatedAvailable = availableServices.filter(service => service.id != serviceId)
            setAvailableServices(updatedAvailable)
        }
    }

    function handleRemoveService(service) {
        const updatedServices = selectedServices.filter(selectedService => selectedService.id !== service.id);
        setSelectedServices(updatedServices);

        if (selectedGroup) {
            fetchData(`${ url }groups/${ tenantCode }/${ selectedGroup?.id }/services/${ service.id }`, 'DELETE')
                .then(() => fetchAvailableServices(selectedGroup))
                .then(fetchGroups)
                .catch(console.error)
        } else {
            setAvailableServices([ ...availableServices, service ])
        }
    }

    function onStartAddGroup() {
        setSelectedBranches([])
        setSelectedServices([])

        fetchData(`${ url }branches/${ tenantCode }`, 'GET')
            .then(response => response.data)
            .then(setAvailableBranches)
            .catch(console.error)

        fetchData(`${ url }tenants/${ tenantCode }/services`)
            .then(response => response.data)
            .then(setAvailableServices)
            .catch(console.error)

        setShowModal(true)
    }

    return (
        <div className="text-center">
            <h2>Groups of Branches</h2>
            <Button variant="primary" style={ { backgroundColor: '#548CA8', borderColor: '#548CA8' } } className="mb-3"
                    onClick={ onStartAddGroup }>Add Group</Button>

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
                { groups.map((group, index) => (
                    <tr key={ index }>
                        <td>{ group.id }</td>
                        <td>{ group.name }</td>
                        <td>{ group.branches.map(branch => branch.name).join(', ') }</td>
                        <td>{ group.services.map(service => service.name).join(', ') }</td>
                        <td className="d-flex justify-content-center gap-2">
                            <Button variant="info"
                                    style={ { backgroundColor: '#548CA8', color: 'white', borderColor: '#548CA8' } }
                                    onClick={ () => handleEditClick(group) }>
                                Edit
                            </Button>

                            <Button variant="danger"
                                    onClick={ () => handleDeleteGroup(group) }>
                                Delete
                            </Button>
                        </td>
                    </tr>
                )) }
                </tbody>
            </Table>

            <Modal show={ showModal } onHide={ () => {
                setShowModal(false);
            } }>
                <Modal.Header closeButton style={ { backgroundColor: '#334257', color: 'white' } }>
                    <Modal.Title>{ selectedGroup ? 'EDIT GROUP' : 'ADD GROUP' }</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="formGroupName" className="mb-3">
                            <Form.Label>Name</Form.Label>
                            <Form.Control type="text"
                                          placeholder="Enter group name"
                                          value={ groupName }
                                          onChange={ (e) => setGroupName(e.target.value) } />
                        </Form.Group>
                        <Form.Group controlId="formGroupBranches" className="mb-3">
                            { selectedBranches.map((branch, index) => (
                                <span key={ index } className="badge bg-secondary m-1"
                                      style={ { display: 'inline-flex', alignItems: 'center' } }>
                                    { branch.name }{ ' ' }
                                    <Button variant="danger" size="sm" style={ {
                                        marginLeft: '5px',
                                        padding: '2px 5px',
                                        backgroundColor: '#dc3545',
                                        borderColor: '#dc3545'
                                    } } onClick={ () => handleRemoveBranch(branch) }>X</Button>
                                </span>
                            )) }
                            <DropdownButton
                                title="Select Branches"
                                onSelect={ (eventKey) => handleBranchSelection(eventKey) }
                                variant="btn btn-outline-secondary"
                            >
                                { availableBranches.map((branch, index) => (
                                    <Dropdown.Item key={ index } eventKey={ branch.id }>{ branch.name }</Dropdown.Item>
                                )) }
                            </DropdownButton>
                        </Form.Group>
                        <Form.Group controlId="formGroupServices" className="mb-3">
                            { selectedServices.map((service, index) => (
                                <span key={ index } className="badge bg-secondary m-1"
                                      style={ { display: 'inline-flex', alignItems: 'center' } }>
                                    { service.name }{ ' ' }
                                    <Button variant="danger" size="sm" style={ {
                                        marginLeft: '5px',
                                        padding: '2px 5px',
                                        backgroundColor: '#dc3545',
                                        borderColor: '#dc3545'
                                    } } onClick={ () => handleRemoveService(service) }>X</Button>
                                </span>
                            )) }
                            <DropdownButton
                                title="Select Services"
                                onSelect={ (eventKey) => handleServiceSelection(eventKey) }
                                variant="btn btn-outline-secondary"
                            >
                                { availableServices.map((service, index) => (
                                    <Dropdown.Item key={ index }
                                                   eventKey={ service.id }>{ service.name }</Dropdown.Item>
                                )) }
                            </DropdownButton>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    { selectedGroup ?
                        <Button variant="primary" style={ { backgroundColor: '#548CA8', borderColor: '#548CA8' } }
                                onClick={ handleEditGroup }>Save Changes</Button> :
                        <Button variant="primary" style={ { backgroundColor: '#548CA8', borderColor: '#548CA8' } }
                                onClick={ handleAddGroup }>Add Group</Button>
                    }
                </Modal.Footer>
            </Modal>

            <Modal show={ deleteConfirmation } onHide={ () => setDeleteConfirmation(false) }>
                <Modal.Header closeButton style={ { backgroundColor: '#334257', color: 'white' } }>
                    <Modal.Title>CONFIRMATION</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>Are you sure you want to delete this group?</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={ () => setDeleteConfirmation(false) }>Cancel</Button>
                    <Button variant="danger" onClick={ confirmDeleteGroup }>Delete</Button>
                </Modal.Footer>
            </Modal>

            <Modal show={ errorMessage !== '' } onHide={ () => setErrorMessage('') } backdrop="static"
                   keyboard={ false }>
                <Modal.Header closeButton style={ { backgroundColor: '#dc3545', color: 'white' } }>
                    <Modal.Title>ERROR</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>{ errorMessage }</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={ () => setErrorMessage('') }>Close</Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};
