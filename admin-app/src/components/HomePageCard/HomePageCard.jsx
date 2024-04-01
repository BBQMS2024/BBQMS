import React from "react";
import './HomePageCard.css'

export default function HomePageCard() {
    return (
        <div>
            <div className="card card-hp" style={{border: '1px solid #334257', backgroundColor: '#476072'}}>
                <div className="card-body">
                    <h5 className="card-title card-title-hp" style={{color: 'white'}}>Card title</h5>
                    <p className="card-text card-text-hp" style={{color: 'white'}}>Some quick example text to build on
                        the card title and make up the bulk of
                        the card's content.</p>
                    <div className="button-container-hp">
                        <a href="#" className="btn btn-primary button-hp"
                           style={{backgroundColor: '#548CA8', border: '1px solid #548CA8'}}>Go somewhere</a>
                    </div>
                </div>
            </div>
        </div>
    )
}
