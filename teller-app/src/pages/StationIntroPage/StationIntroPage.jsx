import React, { useState, useEffect } from 'react';
import { Dropdown, Button } from 'react-bootstrap';
import { fetchData } from '../../fetching/Fetch.js';
import 'bootstrap/dist/css/bootstrap.min.css';
import { SERVER_URL } from '../../constants.js';
import { useNavigate } from 'react-router-dom';

const StationIntroPage = () => {
    const navigate = useNavigate();

    const [branches, setBranches] = useState([]);
    const [selectedBranch, setSelectedBranch] = useState(null);
    const [stations, setStations] = useState([]);
    const [selectedStation, setSelectedStation] = useState(null);

    const url = `${ SERVER_URL }/api/v1/`;

    useEffect(() => {
        fetchData(`${ url }branches/DFLT`, 'GET')
            .then(response => response.data)
            .then(setBranches)
            .catch(console.error)
    }, []);

    function handleBranchSelect(branch) {
        setSelectedBranch(branch)
        fetchData(`${ url }stations/DFLT/${ branch.id }`, 'GET')
            .then(response => response.data)
            .then(setStations)
            .catch(console.error)
    }

    function handleStationSelect(station) {
        navigate(`/teller-queue/${ station.id }`)
    }

    return (
        <div className="text-center mt-5">
            <div className="border p-3" style={{ maxWidth: '500px', margin: '0 auto' }}>
                <h1 className="mb-4">Select Branch and Station</h1>

                <Dropdown>
                    <Dropdown.Toggle variant="primary" id="dropdown-branch" style={{ width: '100%' }}>
                        {selectedBranch ? selectedBranch.name : 'Select Branch'}
                    </Dropdown.Toggle>
                    <Dropdown.Menu style={{ width: '100%' }}>
                        {branches.map(branch => (
                            <Dropdown.Item key={branch.id} onClick={() => handleBranchSelect(branch)}>
                                {branch.name}
                            </Dropdown.Item>
                        ))}
                    </Dropdown.Menu>
                </Dropdown>

                {selectedBranch && (
                    <div className="mt-3">
                        <Dropdown>
                            <Dropdown.Toggle variant="primary" id="dropdown-station" style={{ width: '100%' }}>
                                {selectedStation ? selectedStation.name : 'Select Station'}
                            </Dropdown.Toggle>
                            <Dropdown.Menu style={{ width: '100%' }}>
                                {stations.map(station => (
                                    <Dropdown.Item key={station.id} onClick={() => handleStationSelect(station)}>
                                        {station.name}
                                    </Dropdown.Item>
                                ))}
                            </Dropdown.Menu>
                        </Dropdown>
                    </div>
                )}
            </div>
        </div>
    );
};

export default StationIntroPage;
