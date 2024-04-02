import { useState, useEffect } from 'react';
import "./AdminProfile.css"

export default function AdminProfile(){
    const [isChecked, setIsChecked] = useState(false);
    const [isQRCodeEnabled, setIsQRCodeEnabled] = useState(false);
    const [qrCodeSrc, setQrCodeSrc] = useState('');
    const [userData, setUserData] = useState('');

    useEffect(() => {
        let isTfa = false; //dummy data, treba se dobaviti iz localStorage
        setIsChecked(isTfa);
        setIsQRCodeEnabled(isTfa);
    }, []);

    const handleSaveChanges = () =>{
        //--poziva se save changes endpoint, vrsi se update localStorage
        setIsQRCodeEnabled(isChecked);
        //window.location.reload();
    }

    const handleCheckBoxChange = () =>{
        setIsChecked(!isChecked);
    }

    const handleGenerateQRCode = () =>{
        if(isQRCodeEnabled){
            //poziva se get QRCode
            setQrCodeSrc('https://upload.wikimedia.org/wikipedia/commons/d/d0/QR_code_for_mobile_English_Wikipedia.svg');
        }
    }

    return (
        <div id="account-settings">
            <h1>Account settings</h1>
            <div id="check-box">
                <form>
                    <label htmlForfor="2fa">Use two-factor authentication:</label>
                    <input type="checkbox" id="2fa" name="2fa" checked={isChecked} onChange={handleCheckBoxChange}></input>
                </form>
            </div>
            <div id="QR-code">
                <input type="submit" value="Generate QR code" disabled={!isQRCodeEnabled} onClick={handleGenerateQRCode}/>
                <img src={qrCodeSrc}></img>
            </div>
            <div>
                <input type="submit" value="Save changes" on onClick={handleSaveChanges}/>
            </div>
        </div>
    );
}