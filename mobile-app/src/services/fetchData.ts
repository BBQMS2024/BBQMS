import * as FileSystem from "expo-file-system";
import { Buffer } from "buffer";
import * as Sharing from "expo-sharing";
import { Platform, PermissionsAndroid } from "react-native";
import AsyncStorage from "@react-native-async-storage/async-storage";

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
  id: string;
  name: string;
  tellerStations: Array<Int32Array>;
}

interface ServiceData {
  id: string;
  name: string;
}

interface TicketData {
  id: string;
  name: string;
  date: Date;
  number: number;
  stations: Array<String>;
}

async function getCompanyDetails(code: string) {
  if (!validateCodeFormat(code)) throw new Error(Dialogs.ERROR.INVALID_CODE);
  let details: CompanyData = await fetch(
    `${SERVER_URL}/api/v1/tenants/${code}`,
    { method: "GET" }
  )
    .then((response) => {
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
      if (!companyData.font) companyData.font = Fonts.DEFAULT_FONT;
      if (!companyData.logoUrl) companyData.logoUrl = Assets.DEFAULT_LOGO;
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
    `${SERVER_URL}/api/v1/branches/${code}`,
    { method: "GET" }
  )
    .then((response) => {
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
          tellerList.push(teller);
        });

        const branchData: BranchData = {
          id: branch.id,
          name: branch.name,
          tellerStations: tellerList,
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

async function getBranchServices(code: string, id: string) {
  const serviceList: ServiceData[] = [];
  let services: ServiceData[] = await fetch(
    `${SERVER_URL}/api/v1/branches/${code}/${id}/services`,
    { method: "GET" }
  )
    .then((response) => {
      if (response.status === 200) {
        return response.json();
      } else {
        throw new Error(Dialogs.ERROR.INVALID_CODE);
      }
    })
    .then((data) => {
      data.forEach((service: any) => {
        const serviceData: ServiceData = {
          id: service.id,
          name: service.name,
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

/*
async function getTickets(token: string) {
  const ticketList: TicketData[] = [];
  let tickets: TicketData[] = await fetch(
    `${SERVER_URL}/tickets/devices/${token}`,
    { method: "GET" }
  )
    .then((response) => {
      if (response.status === 200) {
        return response.json();
      } else {
        throw new Error(Dialogs.ERROR.INVALID_CODE);
      }
    })
    .then((data) => {
      let ticket = data;

      const stationList: String[] = [];
      ticket.stations.forEach((station: any) => {
        stationList.push(station.name);
      });

      const ticketData: TicketData = {
        id: ticket.ticket.id,
        name: ticket.ticket.branch.name,
        date: new Date(ticket.ticket.createdAt),
        stations: stationList,
        number: ticket.ticket.number,
      };
      ticketList.push(ticketData);

      return ticketList;
    })
    .catch((error) => {
      console.error("Error:", error);
      throw error;
    });
  return tickets;
}
*/

async function generateTicket(
  token: string,
  branchId: number,
  serviceId: number
) {
  let tickets: TicketData = await fetch(`${SERVER_URL}/api/v1/tickets`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      branchId: `${branchId}`,
      serviceId: `${serviceId}`,
      deviceToken: token,
    }),
  })
    .then((response) => {
      if (response.status === 200) {
        return response.json();
      } else {
        throw new Error(Dialogs.ERROR.INVALID_CODE);
      }
    })
    .then(async (data) => {
      await printTicket(data.ticket.id);
      console.log(data.ticket.id);
      return data.ticket;
    })
    .catch((error) => {
      console.error("Error:", error);
      throw error;
    });
  return tickets;
}

function printTicket(ticketId: number) {
  console.log("Printing ticket...");
  fetch(`${SERVER_URL}/api/v1/tickets/${ticketId}/print`, {
    method: "GET",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(
          `Failed to fetch PDF: ${response.status} - ${response.statusText}`
        );
      }
      return response.arrayBuffer();
    })
    .then((pdfByteArray) => {
      const filename = `ticket_${ticketId}`;
      return savePdfToFile(pdfByteArray, filename);
    })
    .then((filePath) => {
      if (filePath) {
        console.log("PDF saved successfully at: ", filePath);
      } else {
        console.log("Failed to save PDF.");
      }
    })
    .catch((error) => {
      console.error("Error printing ticket: ", error);
    });
}

async function savePdfToFile(pdfByteArray: ArrayBuffer, filename: string) {
  try {
    const folderPath = await FileSystem.documentDirectory + "pdfs/";
    await FileSystem.makeDirectoryAsync(folderPath, { intermediates: true });
    const filePath = folderPath + filename + ".pdf";
    const buffer = Buffer.from(pdfByteArray);
    const base64String = buffer.toString("base64");
    await FileSystem.writeAsStringAsync(filePath, base64String, {
      encoding: FileSystem.EncodingType.Base64,
    });
    saveFile(filePath, filename, "application/pdf");
    return filePath;
  } catch (error) {
    console.error("Error saving PDF: ", error);
    return null;
  }
}

async function saveFile(uri: string, filename: string, mimetype: string) {
  // await AsyncStorage.removeItem("permission");
  if (Platform.OS === "android") {
    const permission = await AsyncStorage.getItem("permission");
    console.log("PERMISSION : " + permission);
    if (!permission) {
      // Check if permission is already granted
      //let x = await Permissions.askAsync(Permissions.MANAGE_EXTERNAL_STORAGE);
      const permissions =
        await FileSystem.StorageAccessFramework.requestDirectoryPermissionsAsync();
      console.log(permissions);
      if (permissions.granted)
        await AsyncStorage.setItem("permission", permissions.directoryUri);
      if (permissions.granted) {
        const base64 = await FileSystem.readAsStringAsync(uri, {
          encoding: FileSystem.EncodingType.Base64,
        });

        await FileSystem.StorageAccessFramework.createFileAsync(
          permissions.directoryUri,
          filename,
          mimetype
        )
        .then(async (uri) => {
          await FileSystem.writeAsStringAsync(uri, base64, {
            encoding: FileSystem.EncodingType.Base64,
            });
          })
          .catch((e) => console.log(e));
      } else {
        Sharing.shareAsync(uri);
      }
    } else {
      // Permission already granted
      const base64 = await FileSystem.readAsStringAsync(uri, {
        encoding: FileSystem.EncodingType.Base64,
      });

      if (permission !== null)
        await FileSystem.StorageAccessFramework.createFileAsync(
          permission,
          filename,
          mimetype
        )
          .then(async (uri) => {
            await FileSystem.writeAsStringAsync(uri, base64, {
              encoding: FileSystem.EncodingType.Base64,
            });
          })
          .catch((e) => console.log(e));
    }
  } else {
    Sharing.shareAsync(uri);
  }
}

export {
  getCompanyDetails,
  getBranchServices,
  getCompanyBranches,
  generateTicket,
};
