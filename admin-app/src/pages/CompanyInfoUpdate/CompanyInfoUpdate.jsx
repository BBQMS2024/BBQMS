import React, { useContext, useEffect, useState } from 'react';
import './CompanyInfoUpdate.css';
import { useNavigate, useParams } from 'react-router-dom';
import { SERVER_URL } from '../../constants.js';
import { UserContext } from '../../context/UserContext.jsx';
import { fetchData } from '../../fetching/Fetch.js';

export default function CompanyInfoUpdate() {
    const navigate = useNavigate();
    const { tenantCode } = useParams();

    const [name, setName] = useState('');
    const [hqAddress, setHQAddress] = useState('');
    const [welcomeMessage, setWelcomeMessage] = useState('');
    const [font, setFont] = useState('Arial');
    const [file, setFile] = useState('');

    const { user, setUser } = useContext(UserContext);

    useEffect(() => {
        // Na pocetku popunimo polja sa vec postojecim podacima
        const url = `${ SERVER_URL }/api/v1/tenants/${ tenantCode }`;

        fetchData(url, 'GET')
            .then(({ data, success }) => {
                if (success) {
                    setName(data.name);
                    setHQAddress(data.hqAddress);
                    setWelcomeMessage(data.welcomeMessage);
                    setFont(data.font);
                    setFile(data.logo.base64Logo);
                }
            });

    }, []);

    function handleChange(e) {
        const file = e.target.files[0];
        const reader = new FileReader();

        reader.onloadend = () => {
            setFile(reader.result);
        };

        reader.readAsDataURL(file);
    }

    async function submitForm() {
        try {
            const url = `${ SERVER_URL }/api/v1/tenants/${ tenantCode }`;
            const { data, success } = await fetchData(url, 'PUT', {
                name: name,
                hqAddress: hqAddress,
                font: font,
                welcomeMessage: welcomeMessage,
                logo: file
            });

            if (success) {
                alert('Success: Your changes have been successfully submitted.');
            } else {
                alert(`Error while trying to save your changes. Please try again.`);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error: An error occurred while submitting your changes.');
        }
    }

    return (
        <div className="company-info-update-wrapper">
            <div className="heading2">
                <h2>COMPANY DETAILS</h2>
            </div>
            <div className="form-container-comp">
                <div className="left-column">
                    <form>
                        <div className="form-group-comp">
                            <label className="labels-comp" htmlFor="name" id="Name">Name</label>
                            <input className="inputs-comp"
                                   type="text"
                                   id="name"
                                   value={ name }
                                   onChange={ (e) => setName(e.target.value) }
                                   required
                            />
                        </div>
                        <div className="form-group-comp">
                            <label className="labels-comp" htmlFor="logo" id="Logo">Logo</label>
                            <input className="inputs-comp"
                                   type="file"
                                   onChange={ handleChange }
                                   accept="image/*"
                                   required
                            />
                            { file && <img className="logo" src={ file } alt="Uploaded Logo" /> }
                        </div>
                    </form>
                </div>
                <div className="right-column">
                    <form>
                        <div className="form-group-comp">
                            <label className="labels-comp" htmlFor="hqAddress">HQ Address</label>
                            <input className="inputs-comp"
                                   type="text"
                                   id="hqAddress"
                                   value={ hqAddress }
                                   onChange={ (e) => setHQAddress(e.target.value) }
                                   required
                            />
                        </div>
                        <div className="form-group-comp">
                            <label className="labels-comp" htmlFor="welcomeMessage">Welcome Message</label>
                            <input className="inputs-comp"
                                   type="text"
                                   id="welcomeMessage"
                                   value={ welcomeMessage }
                                   onChange={ (e) => setWelcomeMessage(e.target.value) }
                                   required
                            />
                        </div>
                        <div className="welcome-message">
                            <p  style={ { fontFamily: font } }>{ welcomeMessage }</p>
                        </div>

                        <div className="fontSelect">
                            <label className="labels-comp" htmlFor="font">Font</label>
                            <select
                                className="form-select"
                                id="font"
                                value={ font }
                                onChange={ (e) => setFont(e.target.value) }
                                required
                            >
                                <option value="Arial">Arial</option>
                                <option value="Times New Roman">Times New Roman</option>
                                <option value="Verdana">Verdana</option>
                                <option value="Helvetica">Helvetica</option>
                                <option value="Montserrat">Montserrat</option>
                                <option value="Calibri">Calibri</option>
                                <option value="Futura">Futura</option>
                                <option value="Bodoni">Bodoni</option>
                                <option value="Rockwell">Rockwell</option>
                                <option value="Comic Sans MS">Comic Sans MS</option>
                            </select>
                        </div>
                    </form>
                </div>
            </div>
            <button type="submit" onClick={ submitForm } className="company-info-submit-btn">Submit</button>
        </div>
    );
}
