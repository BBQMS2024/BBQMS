const { SERVER_URL } = require("../constants.js");

interface CompanyData {
    name: string;
    welcomeMessage: string;
    font: string;
    logoUrl: string;
}

async function getCompanyDetails(code: string) {
    let details: CompanyData | null = await fetch(
        `${SERVER_URL}/api/v1/tenants/${code}`,
        { method: "GET" }
    )
        .then((response) => {
            console.log(response);
            if (response.status === 200) {
                return response.json();
            } else {
                return null;
            }
        })
        .then((data) => {
            if (!data) return null;
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
            return null;
        });
    return details;
}

export { getCompanyDetails };
