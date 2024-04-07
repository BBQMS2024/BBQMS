import { TEST_URL } from "../constants/api";

const { SERVER_URL } = require("../constants/api");
const { Dialogs } = require("../constants/dialogs");
const { Fonts } = require("../constants/fonts");
const { Assets } = require("../constants/assets");
const { validateCodeFormat } = require("../utils/validation");

interface CompanyData {
    name: string;
    welcomeMessage: string;
    font: string;
    logoUrl: string;
}

interface BranchData {
    id: string,
    name: string,
    tellerStations: Array<Int32Array>;
}

interface ServiceData{
    id: string,
    name: string
}

interface DisplayData{
    id: string,
    name: string,
}

async function getCompanyDetails(code: string) {
    if (!validateCodeFormat(code)) throw new Error(Dialogs.ERROR.INVALID_CODE);
    let details: CompanyData = await fetch(
        `${SERVER_URL}/api/v1/tenants/${code}`,
        { method: "GET" }
    )
        .then((response) => {
            console.log(response);
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error(Dialogs.ERROR.INVALID_CODE);
            }
        })
        .then((data) => {
            const companyData: CompanyData = {
                name: data.name,
                welcomeMessage: data.welcomeMessage,
                font: data.font,
                logoUrl: data.logo.base64Logo,
            };
            if(!companyData.font)
                companyData.font = Fonts.DEFAULT_FONT;
            if(!companyData.logoUrl)
                companyData.logoUrl = Assets.DEFAULT_LOGO;
            return companyData;
        })
        .catch((error) => {
            console.error("Error:", error);
            throw error;
        });
    return details;
}

async function getCompanyBranches(code: any) {
    const branchList: BranchData[] = [];
    let branches: BranchData[] = await fetch(
        `${TEST_URL}/branches/${code}`,
        { method: "GET" }
    )
        .then((response) => {
            console.log(response);
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error(Dialogs.ERROR.INVALID_CODE);
            }
        })
        .then((data) => {
            data.forEach((branch: any) => {
                const tellerList: Int32Array[] = [];
                branch.tellerStations.forEach((teller: any) => {
                    tellerList.push(teller)
                })

                const branchData: BranchData = {
                    id: branch.id,
                    name: branch.name,
                    tellerStations: tellerList
                };

                branchList.push(branchData);
            });

            return branchList;
        })
        .catch((error) => {
            console.error("Error:", error);
            throw error;
        });
    return branches;
    
}

async function getBranchServices(code:string, id: string) {
    const serviceList: ServiceData[] = [];
    let services: ServiceData[] = await fetch(
        `${TEST_URL}/branches/${code}/${id}/services`,
        { method: "GET" }
    )
        .then((response) => {
            console.log(response);
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error(Dialogs.ERROR.INVALID_CODE);
            }
        })
        .then((data) => {
            console.log(data)
            data.forEach((service: any) => {

                const serviceData: ServiceData = {
                    id: service.id,
                    name: service.name
                };
                serviceList.push(serviceData);
            });

            return serviceList;
        })
        .catch((error) => {
            console.error("Error:", error);
            throw error;
        });
    return services;
}

async function  getUnassignedDisplays(code: string) {;
    let display: DisplayData = await fetch(
        `${TEST_URL}/displays/unassigned/${code}`,
        { method: "GET" }
    )
        .then((response) => {
            console.log(response);
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error(Dialogs.ERROR.INVALID_CODE);
            }
        })
        .then((data) => {
            
            return data
        })
        .catch((error) => {
            console.error("Error:", error);
            throw error;
        });
    return display;
}


export { getCompanyDetails, getBranchServices, getCompanyBranches };
