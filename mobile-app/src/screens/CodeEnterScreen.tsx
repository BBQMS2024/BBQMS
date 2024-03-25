import React from 'react';
import { SafeAreaView, StyleSheet, TextInput, Button, Pressable, Text, Alert } from 'react-native';
import { registerRootComponent } from 'expo';
import { postCode } from '../services/fetchData';

export default function CodeEnterScreen({ navigation }: { navigation: any }) {
    const [text, onChangeText] = React.useState('');
    return (
        <SafeAreaView style={styles.area}>
            <TextInput
                style={styles.input}
                onChangeText={(text) => onChangeText(text)}
                value={text}
                placeholder='Enter your company code here:'
            />
            <Pressable
                style={({ pressed }) => [
                    styles.button,
                    pressed ? styles.buttonPressed : null,
                ]}
                onPress={async () => {
                    const code = text;
                    const details = await postCode(code);
                    if (details) {
                        navigation.navigate('WelcomeScreen', { details: details });
                    } else {
                        Alert.alert(
                            'Invalid Code',
                            'The code you entered is invalid. Please try again.',
                            [
                                { text: 'OK' }
                            ],
                            { cancelable: false }
                        );

                    }

                }}>
                <Text style={styles.text}>Submit</Text>
            </Pressable>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    input: {
        height: 40,
        margin: 12,
        borderWidth: 1,
        padding: 10,
        textAlign: 'center',
        width: 220
    },
    area: {
        justifyContent: 'center',
        alignItems: 'center',
        flex: 1
    },
    button: {
        alignItems: 'center',
        justifyContent: 'center',
        paddingVertical: 12,
        paddingHorizontal: 32,
        borderRadius: 4,
        elevation: 3,
        width: 120,
        backgroundColor: '#548CA8',
    },
    buttonPressed: {
        backgroundColor: '#334257',
    },
    text: {
        fontSize: 16,
        lineHeight: 21,
        fontWeight: 'bold',
        letterSpacing: 0.25,
        color: 'white',
    }
});

registerRootComponent(CodeEnterScreen);

