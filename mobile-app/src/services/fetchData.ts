const { SERVER_URL } = require("../constants/api");
const { Dialogs } = require("../constants/dialogs");
const { validateCode } = require("../utils/validation");

interface CompanyData {
    name: string;
    welcomeMessage: string;
    font: string;
    logoUrl: string;
}

async function getCompanyDetails(code: string) {
    if (!validateCode(code)) throw new Error(Dialogs.ERROR.INVALID_CODE);
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
            return companyData;
        })
        .catch((error) => {
            console.error("Error:", error);
            throw error;
        });
    return details;
}

export { getCompanyDetails };
