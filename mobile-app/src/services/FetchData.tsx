

const API_BASE_URL = "https://virtserver.swaggerhub.com/EKAPIC1/BBQMS/1.0.0";


export async function fetchSettings(code: string) {
    try {
      const response = await fetch(
        `${API_BASE_URL}/api/v1/tenants/${code}`
      );
      const data = await response.json();
  
      return data;
    } catch (error) {
      console.error("Failed to fetch settings in TasksScreen:", error);
    }
  }


