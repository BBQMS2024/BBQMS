import { useState } from "react";
import HomePageCard from "../../components/HomePageCard/HomePageCard";
import HomePageCardLight from "../../components/HomePageCard/HomePageCardLight";
import { SERVER_URL } from "../../constants";
import './HomePage.css'
export default function HomePage() {

    return (
        <main>
            <h1 className="h1-hp">Admin panel</h1>
            <HomePageCard/>
            <HomePageCardLight/>
            <div className="center">
            </div>
        </main>
    )
}
