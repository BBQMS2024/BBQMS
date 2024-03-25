import React, { useEffect, useState } from "react";
import {
  View,
  StyleSheet,
  Image,
  Linking,
  Alert,
  Text,
  TouchableOpacity,
} from "react-native";
import { useRoute } from "@react-navigation/native";
import WelcomeMessage from "../components/WelcomeMessage";
import { fetchSettings } from "../services/FetchData";
import LoadingAnimation from "../components/LoadingAnimation";

interface RouteParams {
  code: string;
}

export default function HomeScreen() {
    const route = useRoute()
    const { code } = route.params as RouteParams;
    const [name, setName] = useState("");
    const [welcomeMessage, setWelcomeMessage] = useState("");
    const [font, setFont] = useState("");
    const [logo, setLogo] = useState("");

    useEffect(() => {
      fetchData();
      
    }, []);

    async function fetchData() {
      try {
        const { font, welcomeMessage, logo, name } = await fetchSettings(code);
        setFont(font);
        setLogo(logo.base64Logo);
        setName(name);
        setWelcomeMessage(welcomeMessage)
  
      } catch (error) {
        console.error("Failed to fetch data in HomeScreen:", error);
      }
    }
    if ( name == "" || welcomeMessage == "" || font == "" || logo == ""){
      return <LoadingAnimation />;
    }
    else
    return (
        <View style={styles.container}>
            <View style={styles.image}>
                <Image
                style={{ height: 150, resizeMode: "contain" }}
                source={{uri : logo}}
                ></Image>
            </View>
            <WelcomeMessage name={name} font={font} welcome={welcomeMessage}/>
        </View>
        
      );
    }

    const styles = StyleSheet.create({
        container: {
          flex: 1,
          backgroundColor: "#fff",
          alignItems: "center",
        },
        image: {
          marginTop: 80,
          marginRight: 10,
        },
        text: {
          color: "white",
          fontSize: 22,
          fontWeight: "bold",
        },
      });