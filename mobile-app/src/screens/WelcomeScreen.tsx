import React, { useEffect } from "react";
import { View, StyleSheet, Animated, Text } from "react-native";
import WelcomeMessage from "../components/WelcomeMessage";
import { useFonts } from "expo-font";
const { Fonts } = require("../constants/fonts");
const { Assets } = require("../constants/assets");
const { Colors } = require("../constants/colors");

export default function WelcomeScreen({ route }: { route: any }) {
    const details = route.params.details;
    let { name, welcomeMessage, font, logoUrl } = details;

    let [fontsLoaded] = useFonts({
        [Fonts.ARIAL]: Assets.ARIAL,
        [Fonts.TIMES_NEW_ROMAN]: Assets.TIMES_NEW_ROMAN,
        [Fonts.VERDANA]: Assets.VERDANA,
        [Fonts.HELVETICA]: Assets.HELVETICA,
        [Fonts.MONTSERRAT]: Assets.MONTSERRAT,
        [Fonts.CALIBRI]: Assets.CALIBRI,
        [Fonts.FUTURA]: Assets.FUTURA,
        [Fonts.BODONI]: Assets.BODONI,
        [Fonts.ROCKWELL]: Assets.ROCKWELL,
        [Fonts.COMIC_SANS_MS]: Assets.COMIC_SANS_MS,
    });

    const fadeAnim = React.useRef(new Animated.Value(0)).current;

    React.useEffect(() => {
        Animated.timing(fadeAnim, {
            toValue: 1,
            duration: 2000,
            useNativeDriver: true,
        }).start();
    }, [fadeAnim]);

    if (!fontsLoaded) return <Text>Loading...</Text>;
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
        backgroundColor: Colors.BACKGROUND,
        alignItems: "center",
        justifyContent: "center",
    },
    logoContainer: {
        
    },
    logo: {
        width: 250,
        height: 250,
        resizeMode: "contain",
    },
});
