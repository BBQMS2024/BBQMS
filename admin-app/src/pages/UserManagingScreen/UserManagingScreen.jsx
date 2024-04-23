import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { SERVER_URL } from '../../constants.js';
import { UserContext } from '../../context/UserContext.jsx';
import { useNavigate, useParams } from "react-router-dom";

const styles = {
    primaryButton: {
        backgroundColor: "var(--light-blue)",
        borderColor: "var(--light-blue)",
    },
    infoButton: {
        backgroundColor: "var(--light-blue)",
        color: 'white',
        borderColor: "var(--light-blue)",
    },
    modalHeader: {
        backgroundColor: "var(--blue)",
        color: 'white',
    },
};

const UserManageScreen = () => {
    const { tenantCode } = useParams();

    const [showModal, setShowModal] = useState(false);
    const [users, setUsers] = useState([]);
    const [userEmail, setUserEmail] = useState('');
    const [userPassword, setUserPassword] = useState('');
    const [selectedUserIndex, setSelectedUserIndex] = useState(null);
    const [token, setToken] = useState('');
    const [emailError, setEmailError] = useState('');
    const [passwordError, setPasswordError] = useState('');

    useEffect(() => {
        const storedToken = localStorage.getItem('token');
        if (storedToken) {
            setToken(storedToken);
        }
    }, []);

    useEffect(() => {
        if (token) {
            fetchUsers();
        }
    }, [token]);

    const fetchUsers = async () => {
        try {
            const requestBody = JSON.stringify({
                roleName: 'ROLE_USER'
            });
            const response = await fetch(`${ SERVER_URL }/api/v1/admin/${tenantCode}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                },
                body: requestBody
            });

            if (response.ok) {
                const data = await response.json();
                setUsers(data);
            } else {
                console.error('Unsuccessful API call');
            }
        } catch (error) {
            console.error('Error while making API call:', error);
        }
    };

    const validateEmail = (email) => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    };

    const handleAddUser = async () => {
        const requestBody = {
            email: userEmail,
            password: userPassword,
            roleName: 'ROLE_USER'
        };

        try {
            const response = await fetch(`${ SERVER_URL }/api/v1/admin/${tenantCode}/user`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                },
                body: JSON.stringify(requestBody)
            });

            if (response.ok) {
                const data = await response.json();
                setUsers([...users, data]);
                setShowModal(false);
                setUserEmail('');
                setUserPassword('');
            } else {
                console.error('Unsuccessful API call');
            }
        } catch (error) {
            console.error('Error while making API call:', error);
        }
    };

    const handleEditUser = async () => {
        if (!validateEmail(userEmail)) {
            setEmailError('Invalid email address');
            return;
        }

        setEmailError('');

        try {
            const updatedUser = {
                email: userEmail
            };
            const response = await fetch(`${ SERVER_URL }/api/v1/admin/${tenantCode}/user/${users[selectedUserIndex].id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                },
                body: JSON.stringify(updatedUser),
            });
            if (response.ok) {
                const updatedUsers = [...users];
                updatedUsers[selectedUserIndex].email = userEmail;
                setUsers(updatedUsers);
                setShowModal(false);
                setUserEmail('');
                setUserPassword('');
            } else {
                console.error('Unsuccessful API call');
            }
        } catch (error) {
            console.error('Error while making API call:', error);
        }
    };

    const handleDeleteUser = async (userId) => {
        try {
            const response = await fetch(`${ SERVER_URL }/api/v1/admin/${tenantCode}/user/${userId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            });
            if (response.ok) {
                const updatedUsers = users.filter(user => user.id !== userId);
                setUsers(updatedUsers);
            } else {
                console.error('Unsuccessful API call');
            }
        } catch (error) {
            console.error('Error while making API call:', error);
        }
    };

    const handleEditClick = (index) => {
        const user = users[index];
        setSelectedUserIndex(index);
        setUserEmail(user.email);
        setShowModal(true);
    };

    return (
        <div className="text-center mt-5">
            <h2>Manage Users</h2>
            <Button variant="primary" style={styles.primaryButton} className="mb-3" onClick={() => { setShowModal(true); setSelectedUserIndex(null); }}>Add User</Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Email</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {users.map((user, index) => (
                    <tr key={index}>
                        <td>{user.id}</td>
                        <td>{user.email}</td>
                        <td>
                            <Button variant="info" style={styles.infoButton} onClick={() => handleEditClick(index)}>Edit</Button>{' '}
                            <Button variant="danger" onClick={() => handleDeleteUser(user.id)}>Delete</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <Modal show={showModal} onHide={() => { setShowModal(false); setSelectedUserIndex(null); }}>
                <Modal.Header closeButton style={styles.modalHeader}>
                    <Modal.Title>{selectedUserIndex !== null ? 'EDIT USER' : 'ADD USER'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="formUserEmail" className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="email" placeholder="Enter user email" value={userEmail} onChange={(e) => setUserEmail(e.target.value)} />
                            {emailError && <div style={{ color: 'red' }}>{emailError}</div>}
                        </Form.Group>
                        {selectedUserIndex === null && (
                            <Form.Group controlId="formUserPassword" className="mb-3">
                                <Form.Label>Password</Form.Label>
                                <Form.Control type="password" placeholder="Enter user password" value={userPassword} onChange={(e) => setUserPassword(e.target.value)} />
                                {passwordError && <div style={{ color: 'red' }}>{passwordError}</div>}
                            </Form.Group>
                        )}
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => { setShowModal(false); setSelectedUserIndex(null); }}>Close</Button>
                    {selectedUserIndex !== null ?
                        <Button variant="primary" style={styles.primaryButton} onClick={handleEditUser}>Save Changes</Button> :
                        <Button variant="primary" style={styles.primaryButton} onClick={handleAddUser}>Add User</Button>
                    }
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default UserManageScreen;
