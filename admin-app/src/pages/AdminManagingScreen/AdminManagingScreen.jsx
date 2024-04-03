import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form } from 'react-bootstrap';
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

const AdminManageScreen = () => {
    const [showModal, setShowModal] = useState(false);
    const [admins, setAdmins] = useState([]);
    const [adminEmail, setAdminEmail] = useState('');
    const [adminPassword, setAdminPassword] = useState('');
    const [selectedAdminIndex, setSelectedAdminIndex] = useState(null);
    const [token, setToken] = useState('');

    useEffect(() => {
        const storedToken = localStorage.getItem('token');
        if (storedToken) {
            setToken(storedToken);
        }
    }, []);

    useEffect(() => {
        if (token) {
            fetchAdmins();
        }
    }, [token]);

    const fetchAdmins = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/v1/admin/DFLT', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            });
            if (response.ok) {
                const data = await response.json();
                setAdmins(data);
            } else {
                console.error('Unsuccessful API call');
            }
        } catch (error) {
            console.error('Error while making API call:', error);
        }
    };

    const handleAddAdmin = async () => {
        const requestBody = {
            email: adminEmail,
            password: adminPassword
        };

        try {
            const response = await fetch(`http://localhost:8080/api/v1/admin/DFLT/user`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                },
                body: JSON.stringify(requestBody)
            });

            if (response.ok) {
                const data = await response.json();
                setAdmins([...admins, data]);
                setShowModal(false);
                setAdminEmail('');
                setAdminPassword('');
            } else {
                console.error('Unsuccessful API call');
            }
        } catch (error) {
            console.error('Error while making API call:', error);
        }
    };

    const handleEditAdmin = async () => {
        try {
            const updatedAdmin = {
                email: adminEmail
            };
            const response = await fetch(`http://localhost:8080/api/v1/admin/DFLT/user/${admins[selectedAdminIndex].id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                },
                body: JSON.stringify(updatedAdmin),
            });
            if (response.ok) {
                const updatedAdmins = [...admins];
                updatedAdmins[selectedAdminIndex].email = adminEmail;
                setAdmins(updatedAdmins);
                setShowModal(false);
                setAdminEmail('');
                setAdminPassword('');
            } else {
                console.error('Unsuccessful API call');
            }
        } catch (error) {
            console.error('Error while making API call:', error);
        }
    };

    const handleDeleteAdmin = async (userId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/admin/DFLT/user/${userId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            });
            if (response.ok) {
                const updatedAdmins = admins.filter(admin => admin.id !== userId);
                setAdmins(updatedAdmins);
            } else {
                console.error('Unsuccessful API call');
            }
        } catch (error) {
            console.error('Error while making API call:', error);
        }
    };

    const handleEditClick = (index) => {
        const admin = admins[index];
        setSelectedAdminIndex(index);
        setAdminEmail(admin.email);
        setShowModal(true);
    };

    return (
        <div className="text-center mt-5">
            <h2>Manage Administrators</h2>
            <Button variant="primary" style={styles.primaryButton} className="mb-3" onClick={() => { setShowModal(true); setSelectedAdminIndex(null); }}>Add Admin</Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Email</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {admins.map((admin, index) => (
                    <tr key={index}>
                        <td>{admin.id}</td>
                        <td>{admin.email}</td>
                        <td>
                            <Button variant="info" style={styles.infoButton} onClick={() => handleEditClick(index)}>Edit</Button>{' '}
                            <Button variant="danger" onClick={() => handleDeleteAdmin(admin.id)}>Delete</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <Modal show={showModal} onHide={() => { setShowModal(false); setSelectedAdminIndex(null); }}>
                <Modal.Header closeButton style={styles.modalHeader}>
                    <Modal.Title>{selectedAdminIndex !== null ? 'EDIT ADMIN' : 'ADD ADMIN'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="formAdminEmail" className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="email" placeholder="Enter admin email" value={adminEmail} onChange={(e) => setAdminEmail(e.target.value)} />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => { setShowModal(false); setSelectedAdminIndex(null); }}>Close</Button>
                    {selectedAdminIndex !== null ?
                        <Button variant="primary" style={styles.primaryButton} onClick={handleEditAdmin}>Save Changes</Button> :
                        <Button variant="primary" style={styles.primaryButton} onClick={handleAddAdmin}>Add Admin</Button>
                    }
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default AdminManageScreen;
