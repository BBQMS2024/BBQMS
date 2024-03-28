import React from "react";
import {
    View,
    Text,
    StyleSheet,
    SafeAreaView,
    Dimensions,
    Animated,
} from "react-native";
import { Colors } from "../constants/colors";

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
        color: Colors.PRIMARY,
        fontWeight: "bold",
        marginBottom: 30,
    },
    borderContainer: {
        borderWidth: 1,
        padding: 10,
        width: "90%",
        alignItems: "center",
        borderColor: Colors.ACCENT,
        borderRadius: 10,
    },
    message: {
        textAlign: "center",
        fontSize: 40,
        color: Colors.PRIMARY,
    },
});
