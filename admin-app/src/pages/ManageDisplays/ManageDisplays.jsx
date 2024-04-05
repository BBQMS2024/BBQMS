import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import Table from "react-bootstrap/Table";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import "./ManageDisplays.css";
import { fetchData } from "../../fetching/Fetch";
import { SERVER_URL } from '../../constants.js';

const ManageDisplays = () => {
    const { tenantCode } = useParams();
    const [displays, setDisplays] = useState([]);
    const [showAdd, setShowAdd] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [showDelete, setShowDelete] = useState(false);
    const [selectedDisplayId, setSelectedDisplayId] = useState(-1);
    const [displayNameInput, setDisplayNameInput] = useState("");
    const [branches, setBranches] = useState([]);

    useEffect(() => {
        fetchData(`${SERVER_URL}/api/v1/branches/${tenantCode}`, "GET")
            .then((res) => {
                if (res.success) {
                    setBranches(res.data);
                }
            })
            .catch((error) => {
                console.error("Error fetching branches:", error);
            });
    }, [tenantCode]);

    const getDisplays = () => {
        fetchData(
            `${SERVER_URL}/api/v1/displays/${tenantCode}`,
            "GET"
        ).then((res) => {
            if (res.success) {
                setDisplays(res.data);
            }
        });
    };

    const addDisplay = (displayName) => {
        fetchData(
            `${SERVER_URL}/api/v1/displays/${tenantCode}`,
            "POST",
            { name: displayName }
        ).then((res) => {
            if (res.success) {
                getDisplays();
            }
        });
    };

    const editDisplay = (displayName) => {
        fetchData(
            `${SERVER_URL}/api/v1/displays/${tenantCode}/${selectedDisplayId}`,
            "PUT",
            { name: displayName }
        ).then((res) => {
            if (res.success) {
                getDisplays();
            }
        });
    };

    const deleteDisplay = () => {
        fetchData(
            `${SERVER_URL}/api/v1/displays/${tenantCode}/${selectedDisplayId}`,
            "DELETE"
        ).then((res) => {
            if (res.success) {
                getDisplays();
            }
        });
    };

    useEffect(() => {
        getDisplays();
    }, []);

    return (
        <>
            <div id="root-displays">
                <h2>Manage Displays</h2>
                <Table id="table-custom-displays" variant="light" striped bordered hover>
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Branch</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {displays.map((display) => (
                            <tr key={display.id}>
                                <td>{display.id}</td>
                                <td>{display.name}</td>
                                <td>{display.branch.name}</td>
                                <td>
                                    <Button
                                        id="button-edit"
                                        className="button-custom-displays button-custom-blue-displays"
                                        variant="primary"
                                        onClick={() => {
                                            setDisplayNameInput(display.name);
                                            setShowEdit(true);
                                            setSelectedDisplayId(display.id);
                                        }}
                                    >
                                        Edit
                                    </Button>
                                    <Button
                                        id="button-delete"
                                        className="button-custom"
                                        variant="danger"
                                        onClick={() => {
                                            setShowDelete(true);
                                            setSelectedDisplayId(display.id);
                                        }}
                                    >
                                        Delete
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
                <Button
                    id="button-add-displays"
                    className="button-custom-displays button-custom-blue-displays"
                    variant="success"
                    onClick={() => {
                        setShowAdd(true);
                    }}
                >
                    Add Display
                </Button>
                <Modal show={showAdd}>
                    <Modal.Header className="modal-custom-header-displays">
                        <Modal.Title>Add Display</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form>
                            <Form.Group className="mb-3" controlId="formBasicEmail">
                                <Form.Label>Display Name</Form.Label>
                                <Form.Control
                                    value={displayNameInput}
                                    onChange={(e) => setDisplayNameInput(e.target.value)}
                                    type="text"
                                    placeholder="Enter Display Name"
                                />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicBranch">
                                <Form.Label>Branch</Form.Label>
                                <Form.Select>
                                    {branches.map(branch => (
                                        <option key={branch.id} value={branch.id}>{branch.name}</option>
                                    ))}
                                </Form.Select>
                            </Form.Group>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button
                            variant="secondary"
                            onClick={() => {
                                setDisplayNameInput("");
                                setShowAdd(false);
                            }}
                        >
                            Close
                        </Button>
                        <Button
                            className="button-custom-blue-displays"
                            variant="primary"
                            onClick={() => {
                                if (displayNameInput === "") {
                                    alert("Display name cannot be empty!");
                                    return;
                                }
                                setShowAdd(false);
                                addDisplay(displayNameInput);
                                setDisplayNameInput("");
                            }}
                        >
                            Add Display
                        </Button>
                    </Modal.Footer>
                </Modal>

                <Modal show={showEdit}>
                    <Modal.Header className="modal-custom-header">
                        <Modal.Title>Edit Display</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form>
                            <Form.Group className="mb-3" controlId="formBasicEmail">
                                <Form.Label>Display Name</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Enter Display Name"
                                    value={displayNameInput}
                                    onChange={(e) => setDisplayNameInput(e.target.value)}
                                />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicBranch">
                                <Form.Label>Branch</Form.Label>
                                <Form.Select>
                                    {branches.map(branch => (
                                        <option key={branch.id} value={branch.id}>{branch.name}</option>
                                    ))}
                                </Form.Select>
                            </Form.Group>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button
                            variant="secondary"
                            onClick={() => {
                                setDisplayNameInput("");
                                setShowEdit(false);
                            }}
                        >
                            Close
                        </Button>
                        <Button
                            className="button-custom-blue-displays"
                            variant="primary"
                            onClick={() => {
                                if (displayNameInput === "") {
                                    alert("Display name cannot be empty!");
                                    return;
                                }
                                setShowEdit(false);
                                editDisplay(displayNameInput);
                                setDisplayNameInput("");
                            }}
                        >
                            Edit Display
                        </Button>
                    </Modal.Footer>
                </Modal>

                <Modal show={showDelete}>
                    <Modal.Header className="modal-custom-header">
                        <Modal.Title>Delete Display</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <p>Are you sure you want to delete this display?</p>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button
                            variant="secondary"
                            onClick={() => {
                                setDisplayNameInput("");
                                setShowDelete(false);
                            }}
                        >
                            Close
                        </Button>
                        <Button
                            variant="danger"
                            onClick={() => {
                                setShowDelete(false);
                                deleteDisplay();
                            }}
                        >
                            Delete Display
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        </>
    );
};

export default ManageDisplays;
