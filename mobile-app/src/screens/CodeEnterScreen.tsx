import React, { useState } from "react";
import {
    SafeAreaView,
    StyleSheet,
    TextInput,
    Pressable,
    Text,
    Alert,
    ActivityIndicator,
    View,
} from "react-native";
import { registerRootComponent } from "expo";
import { getCompanyBranches, getCompanyDetails } from "../services/fetchData";
import { Dialogs } from "../constants/dialogs";
import { Colors } from "../constants/colors";
import { Screens } from "../constants/screens";

export default function CodeEnterScreen({ navigation }: { navigation: any }) {
    const [text, onChangeText] = React.useState("");
    const [isLoading, setIsLoading] = useState(false);

    function displayInvalidCodeAlert() {
        Alert.alert(
            Dialogs.ERROR.INVALID_CODE,
            Dialogs.ERROR.INVALID_CODE_DESC,
            [{ text: Dialogs.BUTTON.OK }],
            { cancelable: false }
        );
    }

    async function handlePress() {
        setIsLoading(true);
        // await new Promise((resolve) => setTimeout(resolve, 2000));
        const code = text;
        getCompanyDetails(code)
            .then(function (details) {
                setIsLoading(false);
                getCompanyBranches(code)
                .then(function (branches){             
                    navigation.navigate(Screens.BRANCH_PICK, {
                    details: details, branches: branches, code
                });})
   
            })
            .catch((error) => {
                console.error("Error:", error);
                setIsLoading(false);
                displayInvalidCodeAlert();
            });
    }

    return (
        <SafeAreaView style={styles.area}>
            {isLoading && <ActivityIndicator style={styles.loader} size={75} />}
            {isLoading && <View style={styles.loadingArea}></View>}
            <TextInput
                style={styles.input}
                onChangeText={(text) => onChangeText(text)}
                value={text}
                placeholder={Dialogs.PROMPT.ENTER_CODE}
            />
            <Pressable
                style={({ pressed }) => [
                    styles.button,
                    pressed ? styles.buttonPressed : null,
                ]}
                onPress={handlePress}
            >
                <Text style={styles.text}>{Dialogs.BUTTON.SUBMIT}</Text>
            </Pressable>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    loadingArea: {
        position: "absolute",
        zIndex: 2,
        height: "100%",
        width: "100%",
        backgroundColor: "rgba(255, 255, 255, 0.75)",
    },
    loader: {
        position: "absolute",
        zIndex: 100,
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
        backgroundColor: Colors.ACCENT,
    },
    buttonPressed: {
        backgroundColor: Colors.PRIMARY,
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
