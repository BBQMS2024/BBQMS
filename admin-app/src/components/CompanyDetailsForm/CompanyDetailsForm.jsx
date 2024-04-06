import React, { useState } from 'react';
import './CompanyDetailsForm.css';
import {Link, useNavigate} from "react-router-dom";

const SERVER_URL = "http://localhost:8080";

function CompanyDetailsForm() {
  let [name, setName] = useState('');
  let [hqAddress, setHQAddress] = useState('');
  let [welcomeMessage, setWelcomeMessage] = useState('');
  let [font, setFont] = useState('Arial');
  let [file, setFile] = useState('');

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
      if (name === '' || hqAddress === '' || welcomeMessage === '') {
        alert('Error: Please fill out all required fields.');
        return;
      }
      const response = await fetch(`${SERVER_URL}/api/v1/tenants/DFLT`, {
        method: 'PUT',
        body: JSON.stringify({
          name: name,
          hqAddress: hqAddress,
          font: font,
          welcomeMessage: welcomeMessage,
          logo: file
        }),
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (response.ok) {
        alert('Success: Your changes have been successfully submitted.');
      } else {
        const data = await response.json();
        alert(`Error: ${data.message}`);
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error: An error occurred while submitting your changes.');
    }
  }
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('userData');
    navigate('/login');
  };
  return (
      <>
        <div className='heading2'>
          <h2>COMPANY DETAILS</h2>
        </div>
        <div className="form-container-comp">
          <div className="left-column">
            <form>
              <div className="form-group-comp">
                <label className="labels-comp" htmlFor="name" id='Name'>Name</label>
                <input className="inputs-comp"
                       type="text"
                       id="name"
                       value={name}
                       onChange={(e) => setName(e.target.value)}
                       required
                />
              </div>
              <div className="form-group-comp">
                <label className="labels-comp" htmlFor="logo" id='Logo'>Logo</label>
                <input className="inputs-comp"
                       type="file"
                       onChange={handleChange}
                       accept="image/*"
                       required
                />
                {file && <img className="logo" src={file} alt="Uploaded Logo"/>}
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
                       value={hqAddress}
                       onChange={(e) => setHQAddress(e.target.value)}
                       required
                />
              </div>
              <div className="form-group-comp">
                <label className="labels-comp" htmlFor="welcomeMessage">Welcome Message</label>
                <input className="inputs-comp"
                       type="text"
                       id="welcomeMessage"
                       value={welcomeMessage}
                       onChange={(e) => setWelcomeMessage(e.target.value)}
                       required
                />
              </div>
              <div className="welcome-message" style={{fontFamily: font}}>
                <p>{welcomeMessage}</p>
              </div>

              <div className='fontSelect'>
                <label className="labels-comp" htmlFor="font">Font</label>
                <select
                    className="form-select"
                    id="font"
                    value={font}
                    onChange={(e) => setFont(e.target.value)}
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

        <button type="submit" onClick={submitForm}>SUBMIT</button>

        <button id="logout-button" onClick={handleLogout}>LOGOUT</button>

      </>
  );
}

export default CompanyDetailsForm;
