import React from "react";
import { View, StyleSheet, Image } from "react-native";
import WelcomeMessage from "../components/WelcomeMessage";

export default function WelcomeScreen({ route }: { route: any }) {
    const details = route.params.details;
    const { name, welcomeMessage, font, logoUrl } = details;

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
