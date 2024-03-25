const { SERVER_URL } = require('../constants.js');

async function postCode(code: string) {
    /*
    let details = await fetch(`${SERVER_URL}/api/v1/tenants/${code}`, {
        method: 'GET',
    }).then(response => {
        console.log(response)
        if (response.status === 200) {
            return response.json();
        } else {
            return null;
        }
    })
        .then(data => {
            return data;
        })
        .catch((error) => {
            console.error('Error:', error);
        });
        */
    let details = {
        "id": 42,
        "code": "T001",
        "name": "Example Tenant",
        "hqAddress": "123 Main Street, City, Country",
        "font": "Arial",
        "welcomeMessage": "Welcome to our Bank!",
        "logo": {
            "id": 42,
            "base64Logo": ""
        }
    }
    return details;
}

export { postCode }