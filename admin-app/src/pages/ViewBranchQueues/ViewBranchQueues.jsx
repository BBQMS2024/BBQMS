import React, { useState, useEffect } from 'react';
import { Table, Dropdown, DropdownButton, Button } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useInterval } from '../../hooks/hooks.jsx';
import { TimePicker } from '../../components/TimePicker/TimePicker.jsx'
import { formatDate, getDifference, getToday, todayWithTime } from '../../utils/DateUtils.js';
import { addSimpleParamToUrl, addSortToUrl, parseSortFromUrl } from '../../utils/QueryParamsService.js';
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
    const [ startTime, setStartTime ] = useState({ hour: 0, minutes: 0});
    const [ endTime, setEndTime ] = useState({ hour: 23, minutes: 59 });

    const url = `${ SERVER_URL }/api/v1/`;

    useEffect(() => {
        fetchData(`${ url }branches/${ tenantCode }`, 'GET')
            .then(response => setBranches(response.data))
            .catch(error => console.error('Error fetching branches:', error));
    }, [])

    useInterval(() => {
        if (selectedBranch) {
            fetchQueuesForBranch(selectedBranch, selectedService);
        }
    }, 10000, [selectedBranch, selectedService])

    async function fetchQueuesForBranch(branch, service) {
        try {
            let baseUrl = `${ url }branches/${ tenantCode }/${ branch.id }/queue`;

            const sort = parseSortFromUrl(window.location.href);
            if (sort) {
                baseUrl = addSortToUrl(sort, baseUrl);
            }

            if (service) {
                baseUrl = addSimpleParamToUrl('serviceId', service.id, baseUrl);
            }

            const startInstant = todayWithTime(startTime.hour, startTime.minutes);
            const endInstant = todayWithTime(endTime.hour, endTime.minutes);

            baseUrl = addSimpleParamToUrl('createdAfter', startInstant.toISOString(), baseUrl);
            baseUrl = addSimpleParamToUrl('createdBefore', endInstant.toISOString(), baseUrl);

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
        setStartTime(time);
    }

    function onEndTimeChange(time) {
        setEndTime(time);
    }

    return (
        <div className="text-center">
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
                <TimePicker title="Start time"
                            onChange={ onStartTimeChange }
                            defaultHour="0"
                            defaultMinute="0" />
                <TimePicker title="End time"
                            onChange={ onEndTimeChange }
                            defaultHour="23"
                            defaultMinute="59" />
                <Button onClick={ () => fetchQueuesForBranch(selectedBranch, selectedService) }
                        style={{
                            height: 'fit-content',
                            alignSelf: 'end',
                            backgroundColor: 'var(--light-blue)',
                            padding: '6px 20px'
                        }}>
                    Filter
                </Button>
            </div>

            <div style={ { marginTop: '20px' } }>
                <Table striped bordered hover>
                    <thead>
                        <SortContextProvider onSort={ () => fetchQueuesForBranch(selectedBranch, selectedService) }>
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
                            const currentTime = getToday();
                            const elapsedTime = getDifference(currentTime, createdAt, 'minutes');

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
