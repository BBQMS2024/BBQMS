import React from "react";
import { View, StyleSheet, Image, Animated } from "react-native";
import WelcomeMessage from "../components/WelcomeMessage";
import { useFonts } from "expo-font";
import * as SplashScreen from "expo-splash-screen";

export default function WelcomeScreen({ route }: { route: any }) {
    const details = route.params.details;
    let { name, welcomeMessage, font, logoUrl } = details;

    if(!font) font = "Arial";
    if(!logoUrl) logoUrl = "../../assets/images/default_logo.png";

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

    const fadeAnim = React.useRef(new Animated.Value(0)).current;

    if (!fontsLoaded) SplashScreen.preventAutoHideAsync();
    else SplashScreen.hideAsync();

    React.useEffect(() => {
        Animated.timing(fadeAnim, {
            toValue: 1,
            duration: 2000,
            useNativeDriver: true,
        }).start();
    }, [fadeAnim]);

    return (
        <View style={styles.container}>
            <View style={styles.logoContainer}>
                <Animated.Image
                    style={[styles.logo, { opacity: fadeAnim }]}
                    source={{ uri: logoUrl }}
                />
            </View>
            <WelcomeMessage name={name} font={font} welcome={welcomeMessage} />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#EEEEEE",
        alignItems: "center",
    },
    logoContainer: {
        marginTop: 80,
        marginBottom: 20,
    },
    logo: {
        width: 250,
        height: 250,
        resizeMode: "contain",
    },
});
