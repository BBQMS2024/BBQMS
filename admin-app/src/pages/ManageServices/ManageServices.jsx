import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import Header from "../../components/Header/Header";
import Table from "react-bootstrap/Table";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import "./ManageServices.css";

const ManageServices = () => {
    const [services, setServices] = useState([]);
    const [showAdd, setShowAdd] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [showDelete, setShowDelete] = useState(false);

    const staticServices = [
        {
            id: 1,
            name: "Service 1",
        },
        {
            id: 2,
            name: "Service 2",
        },
        {
            id: 3,
            name: "Service 3",
        },
    ];

    useEffect(() => {
        setServices(staticServices);
    }, []);

    return (
        <>
            <div id="root">
                <h2>Manage Services</h2>
                <Table id="table-custom" variant="light" striped bordered hover>
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {services.map((service) => (
                            <tr key={service.id}>
                                <td>{service.id}</td>
                                <td>{service.name}</td>
                                <td>
                                    <Button
                                        id="button-edit"
                                        className="button-custom button-custom-blue"
                                        variant="primary"
                                        onClick={() => setShowEdit(true)}
                                    >
                                        Edit
                                    </Button>
                                    <Button
                                        id="button-delete"
                                        className="button-custom"
                                        variant="danger"
                                        onClick={() => setShowDelete(true)}
                                    >
                                        Delete
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
                <Button
                    id="button-add"
                    className="button-custom button-custom-blue"
                    variant="success"
                    onClick={() => {
                        setShowAdd(true);
                    }}
                >
                    Add Service
                </Button>
                <Modal show={showAdd}>
                    <Modal.Header className="modal-custom-header">
                        <Modal.Title>Add Service</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form>
                            <Form.Group
                                className="mb-3"
                                controlId="formBasicEmail"
                            >
                                <Form.Label>Service Name</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Enter Service Name"
                                />
                            </Form.Group>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button
                            variant="secondary"
                            onClick={() => {
                                setShowAdd(false);
                            }}
                        >
                            Close
                        </Button>
                        <Button
                            className="button-custom-blue"
                            variant="primary"
                        >
                            Add Service
                        </Button>
                    </Modal.Footer>
                </Modal>

                <Modal show={showEdit}>
                    <Modal.Header className="modal-custom-header">
                        <Modal.Title>Edit Service</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form>
                            <Form.Group
                                className="mb-3"
                                controlId="formBasicEmail"
                            >
                                <Form.Label>Service Name</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Enter Service Name"
                                />
                            </Form.Group>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button
                            variant="secondary"
                            onClick={() => {
                                setShowEdit(false);
                            }}
                        >
                            Close
                        </Button>
                        <Button
                            className="button-custom-blue"
                            variant="primary"
                        >
                            Edit Service
                        </Button>
                    </Modal.Footer>
                </Modal>

                <Modal show={showDelete}>
                    <Modal.Header className="modal-custom-header">
                        <Modal.Title>Delete Service</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <p>Are you sure you want to delete this service?</p>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button
                            variant="secondary"
                            onClick={() => {
                                setShowDelete(false);
                            }}
                        >
                            Close
                        </Button>
                        <Button variant="danger">Delete Service</Button>
                    </Modal.Footer>
                </Modal>
            </div>
        </>
    );
};

export default ManageServices;
