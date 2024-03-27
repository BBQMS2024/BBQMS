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
import LoadingAnimation from "../components/LoadingAnimation";

interface CompanyData {
  name: string,
  welcomeMessage: string,
  font: string,
  logoUrl: string
}

export default function HomeScreen() {
    const route = useRoute()
    const { name, welcomeMessage, font, logoUrl } = route.params as CompanyData;

    useEffect(() => {
      
    }, []);

    if ( name == "" || welcomeMessage == "" || font == "" || logoUrl == "" ){
      return <LoadingAnimation />;
    }
    else
    return (
        <View style={styles.container}>
            <View style={styles.image}>
                <Image
                style={styles.logo}
                source={{uri : logoUrl}}
                ></Image>
            </View>
            <WelcomeMessage name={name} font={font} welcome={welcomeMessage}/>
        </View>
        
      );
    }

    const styles = StyleSheet.create({
        container: {
          flex: 1,
          backgroundColor : "#548CA8",
          flexDirection: 'row',
        },
        image: {
          marginTop: 80,
          marginRight: 10,
          marginBottom: 20,
          marginLeft: 20
        },
        logo: {
          width: 100,
          height: 100,
          resizeMode: 'contain',
        },
        text: {
          color: "white",
          fontSize: 22,
          fontWeight: "bold",
        },
      });