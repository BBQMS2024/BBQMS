import { useState, useEffect } from 'react';
import { fetchData } from '../../fetching/Fetch.js';
import { SERVER_URL } from '../../constants';
import "./AdminProfile.css"

export default function AdminProfile(){
    const [isChecked, setIsChecked] = useState(false);
    const [isQRCodeEnabled, setIsQRCodeEnabled] = useState(false);
    const [qrCodeSrc, setQrCodeSrc] = useState('');
    const [userData, setUserData] = useState('');

    useEffect(() => {
        const storedUserData = localStorage.getItem('userData');
        setUserData(JSON.parse(storedUserData));

        const storedIsTfa =  localStorage.getItem('isTfa');
        let isTfa = JSON.parse(storedIsTfa);

        setIsChecked(isTfa);
        setIsQRCodeEnabled(isTfa);
    }, []);

    const handleSaveChanges = async () =>{
        const url = `${ SERVER_URL }/api/v1/auth/tfa`;
        const { data, success } = await fetchData(url, 'PUT', {
            isTfa: isChecked
        });
        localStorage.setItem('isTfa', isChecked);       
        setIsQRCodeEnabled(isChecked);
        if(!isChecked){
            setQrCodeSrc('');
        }
        if(success){
            let message = 'Success: Your changes have been successfully submitted.';
            if(isChecked){
                message = message + '\nPlease scan QR code.';
            }
            alert(message);
        }else{
            alert('An error occurred. Please try again.');
        }
    }

    const handleCheckBoxChange = () =>{
        setIsChecked(!isChecked);
    }

    const handleGenerateQRCode = () =>{
        if(isQRCodeEnabled){
            const url = `${ SERVER_URL }/api/v1/auth/tfa?email=${userData.email}`;
            fetchData(url, 'GET')
            .then(({ data, success }) => {
                if (success) {
                    setQrCodeSrc(data.qrCode);
                }
            });
        }
    }

    return (
        <div id="account-settings">
            <h1>Account settings</h1>
            <div id="check-box">
                <form>
                    <label htmlFor="2fa">Use two-factor authentication:</label>
                    <input type="checkbox" id="2fa" name="2fa" checked={isChecked} onChange={handleCheckBoxChange}></input>
                </form>
            </div>
            <div id="QR-code">
                <input type="submit" value="Generate QR code" disabled={!isQRCodeEnabled} onClick={handleGenerateQRCode}/>
                <img src={qrCodeSrc}></img>
            </div>
            <div>
                <input type="submit" value="Save changes" onClick={handleSaveChanges}/>
            </div>
        </div>
    );
}