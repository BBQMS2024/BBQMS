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
import { CommonActions } from "@react-navigation/native";
import WelcomeMessage from "../components/WelcomeMessage";

export default function HomeScreen() {

    // const { accountID } = route.params;
    const [companyName, setcompanyName] = useState("Opcina centar");
    const [settings, setSettings] = useState("arial");

    useEffect(() => {
      
    }, []);

/*
    <View style={styles.image}>
    <Image
      style={{ height: 350, resizeMode: "contain" }}
      source={require("")}
    ></Image>
  </View>
*/

    return (
        <View style={styles.container}>
            <View style={styles.image}>
                <Image
                style={{ height: 150, resizeMode: "contain" }}
                source={require("../../assets/icon.png")}
        ></Image>
      </View>
            <Text style={styles.text}>Dobro došli </Text>
            <WelcomeMessage name={companyName} settings={settings} />
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