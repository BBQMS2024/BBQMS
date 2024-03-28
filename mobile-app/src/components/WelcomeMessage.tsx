import React from "react";
import {
    View,
    Text,
    StyleSheet,
    SafeAreaView,
    Dimensions,
    Animated,
} from "react-native";

interface WelcomeMessageProps {
    name: string;
    welcome: string;
    font: string;
}

export default function WelcomeMessage(params: WelcomeMessageProps) {
    const screenWidth = Dimensions.get("window").width;
    const fontSize = screenWidth * 0.09;

    return (
        <SafeAreaView style={styles.container}>
            <Animated.Text style={[styles.title, { fontSize }]}>
                {params.name}
            </Animated.Text>
            <View style={styles.borderContainer}>
                <Text style={[styles.message, { fontFamily: params.font }]}>
                    {params.welcome}
                </Text>
            </View>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    container: {
        alignItems: "center",
    },
    title: {
        color: "#334257",
        fontWeight: "bold",
        marginBottom: 15,
    },
    borderContainer: {
        borderWidth: 1,
        padding: 10,
        width: "90%",
        alignItems: "center",
        borderColor: "#548CA8",
    },
    message: {
        textAlign: "center",
        fontSize: 40,
        color: "#334257",
    },
});
