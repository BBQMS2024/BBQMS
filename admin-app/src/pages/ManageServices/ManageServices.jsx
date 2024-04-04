import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import Header from "../../components/Header/Header";
import Table from "react-bootstrap/Table";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import "./ManageServices.css";
import { fetchData } from "../../fetching/Fetch";

const ManageServices = () => {
    const { tenantCode } = useParams();
    const [services, setServices] = useState([]);
    const [showAdd, setShowAdd] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [showDelete, setShowDelete] = useState(false);
    const [selectedServiceId, setSelectedServiceId] = useState(-1);
    const [serviceNameInput, setServiceNameInput] = useState("");

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

    const getServices = () => {
        fetchData(
            `http://localhost:8080/api/v1/tenants/${tenantCode}/services`,
            "GET"
        ).then((res) => {
            if (res.success) {
                setServices(res.data);
            }
        });
    };

    const addService = (serviceName) => {
        fetchData(
            `http://localhost:8080/api/v1/tenants/${tenantCode}/services`,
            "POST",
            { name: serviceName }
        ).then((res) => {
            if (res.success) {
                getServices();
            }
        });
    };

    const editService = (serviceName) => {
        fetchData(
            `http://localhost:8080/api/v1/tenants/${tenantCode}/services/${selectedServiceId}`,
            "PUT",
            { name: serviceName }
        ).then((res) => {
            if (res.success) {
                getServices();
            }
        });
    };

    const deleteService = () => {
        fetchData(
            `http://localhost:8080/api/v1/tenants/${tenantCode}/services/${selectedServiceId}`,
            "DELETE"
        ).then((res) => {
            if (res.success) {
                getServices();
            }
        });
    };

    useEffect(() => {
        getServices();
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
                                        onClick={() => {
                                            setServiceNameInput(service.name);
                                            setShowEdit(true);
                                            setSelectedServiceId(service.id);
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
                                            setSelectedServiceId(service.id);
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
                                    value={serviceNameInput}
                                    onChange={(e) =>
                                        setServiceNameInput(e.target.value)
                                    }
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
                                setServiceNameInput("");
                                setShowAdd(false);
                            }}
                        >
                            Close
                        </Button>
                        <Button
                            className="button-custom-blue"
                            variant="primary"
                            onClick={() => {
                                if(serviceNameInput === "") {
                                    alert("Service name cannot be empty!");
                                    return;
                                }
                                setShowAdd(false);
                                addService(serviceNameInput);
                                setServiceNameInput("");
                            }}
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
                                    value={serviceNameInput}
                                    onChange={(e) =>
                                        setServiceNameInput(e.target.value)
                                    }
                                />
                            </Form.Group>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button
                            variant="secondary"
                            onClick={() => {
                                setServiceNameInput("");
                                setShowEdit(false);
                            }}
                        >
                            Close
                        </Button>
                        <Button
                            className="button-custom-blue"
                            variant="primary"
                            onClick={() => {
                                if(serviceNameInput === "") {
                                    alert("Service name cannot be empty!");
                                    return;
                                }
                                setShowEdit(false);
                                editService(serviceNameInput);
                                setServiceNameInput("");
                            }}
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
                                setServiceNameInput("");
                                setShowDelete(false);
                            }}
                        >
                            Close
                        </Button>
                        <Button
                            variant="danger"
                            onClick={() => {
                                setShowDelete(false);
                                deleteService();
                            }}
                        >
                            Delete Service
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        </>
    );
};

export default ManageServices;
