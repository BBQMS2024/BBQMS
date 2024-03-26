import React, { useState } from 'react';
import './CompanyDetailsForm.css';


const SERVER_URL = "http://localhost:8080";

function CompanyDetailsForm() {
  // State hooks
  let [name, setName] = useState('');
  let [hqAddress, setHQAddress] = useState('');
  let [welcomeMessage, setWelcomeMessage] = useState('');
  let [font, setFont] = useState('Arial');
  let [file, setFile] = useState('');

  // Function to handle file change
  function handleChange(e) {
    const file = e.target.files[0];
    const reader = new FileReader();

    reader.onloadend = () => {
      setFile(reader.result);
    };

    reader.readAsDataURL(file);
    // TO DO - add/update this url in database
  }

  // Function to handle name change
  const handleNameChange = (event) => {
    setName(event.target.value);
  };

  // Function to submit form
  async function submitForm() {
    try {
      const response = await fetch(`${SERVER_URL}/api/v1/tenants/DFLT`, {
        method: 'PUT',
        body: JSON.stringify({
          name: name,
          hqAddress: hqAddress,
          font: font,
          welcomeMessage: welcomeMessage,
          logo: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII="
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

  return (
      <>
        <div className='heading2'>
          <h2>COMPANY DETAILS</h2>
        </div>
        <div className="form-container">
          <div className="left-column">
            <form>
              <div className="form-group">
                <label htmlFor="name" id='Name'>Name</label>
                <input
                    type="text"
                    id="name"
                    value={name}
                    onChange={handleNameChange}
                    required
                />
              </div>
              <div className="form-group">
                <label htmlFor="logo" id='Logo'>Logo</label>
                <input
                    type="file"
                    onChange={handleChange}
                    accept="image/*"
                    required
                />
                {file && <img className="logo" src={file} alt="Uploaded Logo" />}
              </div>
            </form>
          </div>
          <div className="right-column">
            <form>
              <div className="form-group">
                <label htmlFor="hqAddress">HQ Address</label>
                <input
                    type="text"
                    id="hqAddress"
                    value={hqAddress}
                    onChange={(e) => setHQAddress(e.target.value)}
                    required
                />
              </div>
              <div className="form-group">
                <label htmlFor="welcomeMessage">Welcome Message</label>
                <input
                    type="text"
                    id="welcomeMessage"
                    value={welcomeMessage}
                    onChange={(e) => setWelcomeMessage(e.target.value)}
                    required
                />
                <div className="welcome-message" style={{ fontFamily: font }} >
                  <p>{welcomeMessage}</p>
                </div>
              </div>
              <div>
                <label htmlFor="font">Font</label>
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
      </>
  );
}

export default CompanyDetailsForm;
