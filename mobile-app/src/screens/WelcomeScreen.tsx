import React from "react";
import { View, StyleSheet, Image } from "react-native";
import WelcomeMessage from "../components/WelcomeMessage";
import { useFonts } from "expo-font";
import * as SplashScreen from "expo-splash-screen";

export default function WelcomeScreen({ route }: { route: any }) {
    const details = route.params.details;
    const { name, welcomeMessage, font, logoUrl } = details;

    let [fontsLoaded] = useFonts({
        Arial: require("../../assets/fonts/arial.ttf"),
        "Times New Roman": require("../../assets/fonts/times-new-roman.ttf"),
        Verdana: require("../../assets/fonts/verdana.ttf"),
        Helvetica: require("../../assets/fonts/helvetica.ttf"),
        Montserrat: require("../../assets/fonts/montserrat.ttf"),
        Calibri: require("../../assets/fonts/calibri.ttf"),
        Futura: require("../../assets/fonts/futura.ttf"),
        Bodoni: require("../../assets/fonts/bodoni.ttf"),
        Rockwell: require("../../assets/fonts/rockwell.ttf"),
        "Comic Sans MS": require("../../assets/fonts/comic-sans-ms.ttf"),
    });

    if (!fontsLoaded) SplashScreen.preventAutoHideAsync();
    else SplashScreen.hideAsync();
    return (
        <View style={styles.container}>
            <View style={styles.image}>
                <Image style={styles.logo} source={{ uri: logoUrl }}></Image>
            </View>
            <WelcomeMessage name={name} font={font} welcome={welcomeMessage} />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#548CA8",
        flexDirection: "row",
    },
    image: {
        marginTop: 80,
        marginRight: 10,
        marginBottom: 20,
        marginLeft: 20,
    },
    logo: {
        width: 100,
        height: 100,
        resizeMode: "contain",
    },
    text: {
        color: "white",
        fontSize: 22,
        fontWeight: "bold",
    },
});
