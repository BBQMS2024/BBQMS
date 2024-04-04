import React from "react";
import { useNavigate, useParams } from 'react-router-dom';
import './HomePageCard.css'

export default function HomePageCard( {title, backgroundColor, buttonColor, url} ) {
    const navigate = useNavigate();
    return (
        <div>
            <div className="card card-hp" style={{border: '1px solid #334257', backgroundColor: backgroundColor, height: '200px'}}>
                <div className="card-body">
                    <h5 className="card-title card-title-hp" style={{color: 'white'}}>{title}</h5>
                    <p className="card-text card-text-hp" style={{color: 'white'}}></p>
                    <div className="button-container-hp">
                        <a href="#" className="btn btn-primary button-hp"
                           style={{backgroundColor: buttonColor}} onClick ={ () => navigate(url) } >Open</a>
                    </div>
                </div>
            </div>
        </div>
    )
}
