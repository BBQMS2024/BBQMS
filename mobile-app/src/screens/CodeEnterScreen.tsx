import React, { useState } from "react";
import {
    SafeAreaView,
    StyleSheet,
    TextInput,
    Pressable,
    Text,
    Alert,
    ActivityIndicator,
} from "react-native";
import { registerRootComponent } from "expo";
import { getCompanyDetails } from "../services/fetchData";

export default function CodeEnterScreen({ navigation }: { navigation: any }) {
    const [text, onChangeText] = React.useState("");
    const [isLoading, setIsLoading] = useState(false);

    return (
        <SafeAreaView style={styles.area}>
            {isLoading && (
                <ActivityIndicator style={styles.loading} size="large" />
            )}
            <TextInput
                style={styles.input}
                onChangeText={(text) => onChangeText(text)}
                value={text}
                placeholder="Enter your company code here:"
            />
            <Pressable
                style={({ pressed }) => [
                    styles.button,
                    pressed ? styles.buttonPressed : null,
                ]}
                onPress={async () => {
                    const code = text;
                    setIsLoading(true);
                    getCompanyDetails(code).then(function (details) {
                        setIsLoading(false);
                        if (details) {
                            navigation.navigate("WelcomeScreen", {
                                details: details,
                            });
                        } else {
                            Alert.alert(
                                "Invalid Code",
                                "The code you entered is invalid. Please try again.",
                                [{ text: "OK" }],
                                { cancelable: false }
                            );
                        }
                    });
                }}
            >
                <Text style={styles.text}>Submit</Text>
            </Pressable>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    loading: {
        position: "absolute",
        zIndex: 999,
    },
    input: {
        height: 40,
        margin: 12,
        borderWidth: 1,
        padding: 10,
        textAlign: "center",
        width: 220,
    },
    area: {
        justifyContent: "center",
        alignItems: "center",
        flex: 1,
    },
    button: {
        alignItems: "center",
        justifyContent: "center",
        paddingVertical: 12,
        paddingHorizontal: 32,
        borderRadius: 4,
        elevation: 3,
        width: 120,
        backgroundColor: "#548CA8",
    },
    buttonPressed: {
        backgroundColor: "#334257",
    },
    text: {
        fontSize: 16,
        lineHeight: 21,
        fontWeight: "bold",
        letterSpacing: 0.25,
        color: "white",
    },
});

registerRootComponent(CodeEnterScreen);
