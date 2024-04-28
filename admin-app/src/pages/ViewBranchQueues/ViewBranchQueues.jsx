import React, { useState, useEffect } from 'react';
import { Table, Dropdown, DropdownButton, Button } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { TimePicker } from '../../components/TimePicker/TimePicker.jsx'
import { formatDate } from '../../utils/DateUtils.js'
import { addSimpleParamToUrl, addSortToUrl } from '../../utils/QueryParamsService.js';
import { SortContextProvider } from '../../context/SortContext.jsx';
import { SortableHeader } from '../../components/Table/SortableHeader.jsx';
import { fetchData } from '../../fetching/Fetch.js';
import { useParams } from 'react-router-dom';
import { SERVER_URL } from '../../constants.js';

export default function ViewBranchQueues() {
    const [selectedBranch, setSelectedBranch] = useState(null);
    const [selectedService, setSelectedService] = useState(null);
    const [branches, setBranches] = useState([]);
    const [services, setServices] = useState([]);
    const [queue, setQueue] = useState([]);
    const { tenantCode } = useParams();
    const [ startTime, setStartTime ] = useState({});
    const [ endTime, setEndTime ] = useState({});

    const url = `${ SERVER_URL }/api/v1/`;

    useEffect(() => {
        fetchData(`${ url }branches/${ tenantCode }`, 'GET')
            .then(response => setBranches(response.data))
            .catch(error => console.error('Error fetching branches:', error));
    }, []);

    async function fetchQueuesForBranch(branch, service, sort) {
        try {
            let baseUrl = `${ url }branches/${ tenantCode }/${ branch.id }/queue`;
            if (sort) {
                baseUrl = addSortToUrl(sort, baseUrl);
            }

            if (service) {
                baseUrl = addSimpleParamToUrl('serviceId', service.id, baseUrl);
            }

            const response = await fetchData(baseUrl, 'GET');

            if (response.success) {
                setQueue(response.data);
            } else {
                console.error('Error fetching queue:', response.error);
            }
        } catch (error) {
            console.error('Error fetching queue:', error);
        }
    }

    async function fetchServicesForBranch(branch) {
        try {
            const response = await fetchData(`${ url }branches/${ tenantCode }/${ branch.id }/services`, 'GET');
            if (response.success) {
                setServices(response.data);
            } else {
                console.error('Error fetching services:', response.error);
            }
        } catch (error) {
            console.error('Error fetching services:', error);
        }
    }

    async function onServiceChange(service) {
        setSelectedService(service);
        await fetchQueuesForBranch(selectedBranch, service);
    }

    async function onBranchChange(branch) {
        setSelectedService(null);
        setSelectedBranch(branch);
        await fetchQueuesForBranch(branch, null);
        await fetchServicesForBranch(branch);
    }

    function onStartTimeChange(time) {
        console.log(time);
    }

    function onEndTimeChange(time) {
        console.log(time);
    }

    return (
        <div className="text-center mt-5">
            <h2>Queue</h2>
            <DropdownButton title={ selectedBranch ? selectedBranch.name : 'Select Branch' }
                            variant="btn btn-outline-secondary">
                { branches.map((branch, index) => (
                    <Dropdown.Item key={ index }
                                   onClick={ () => onBranchChange(branch) }>
                        { branch.name }
                    </Dropdown.Item>
                )) }
            </DropdownButton>

            <div className="mx-auto d-inline">
                <DropdownButton style={ { marginTop: '20px' } }
                                title={ selectedService ? selectedService.name : 'Select Service' }
                                variant="btn btn-outline-secondary">
                    { services.map((service, index) => (
                        <Dropdown.Item key={ index }
                                       onClick={ () => onServiceChange(service) }>
                            { service.name }
                        </Dropdown.Item>
                    )) }
                </DropdownButton>
            </div>

            <div className="d-flex justify-content-end  gap-3 me-5">
                <TimePicker title="Start time" onChange={ onStartTimeChange } />
                <TimePicker title="End time" onChange={ onEndTimeChange } />
                <Button onClick={ () => console.log('filtering') }
                        style={{
                            height: 'fit-content',
                            alignSelf: 'end',
                            backgroundColor: 'var(--light-blue)'
                        }}>
                    Filter now
                </Button>
            </div>


            <div style={ { marginTop: '20px' } }>
                <Table striped bordered hover>
                    <thead>
                        <SortContextProvider onSort={ sort => fetchQueuesForBranch(selectedBranch, selectedService, sort) }>
                            <tr>
                                <SortableHeader columnName="number">
                                    Ticket Number
                                </SortableHeader>

                                <SortableHeader columnName="serviceId">
                                    Service
                                </SortableHeader>

                                <SortableHeader columnName="createdAt">
                                    Created At
                                </SortableHeader>

                                <SortableHeader columnName="createdAt">
                                    Elapsed Time
                                </SortableHeader>
                            </tr>
                        </SortContextProvider>
                    </thead>

                    <tbody>
                        { queue.map((ticket, index) => {
                            const createdAt = new Date(ticket.createdAt);
                            const currentTime = new Date();
                            const elapsedTime = Math.floor((currentTime - createdAt) / (1000 * 60));

                            return (
                                <tr key={ `${ index }-${ ticket.id }` }>
                                    <td>{ ticket.number }</td>
                                    <td>{ ticket.service.name }</td>
                                    <td>{ formatDate(ticket.createdAt) }</td>
                                    <td>{ elapsedTime } minutes</td>
                                </tr>
                            );
                        }) }
                    </tbody>
                </Table>
            </div>
        </div>
    );
}
