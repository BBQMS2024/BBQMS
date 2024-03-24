import React from "react";
import { View, Text, StyleSheet } from "react-native";

interface WelcomeMessageProps {
  name: string;
  settings: string;
}

export default function WelcomeMessage(params : WelcomeMessageProps ) {

    return (
      <View style={styles.welcomeBox}>
        <Text
          style={{fontFamily: params.settings, fontSize: 32}}
        >
          Dobro došli {params.name}!
        </Text>
      </View>
    );

  /*
  const letters = "aeioukh";
  const lastLetter = name.slice(-1);
  if (!letters.includes(lastLetter)) name += "e";

  return (
    <View style={styles.welcomeBox}>
      <Text
        style={{
          fontSize: settings.fontSize + 6,
          fontFamily: settings.font,
        }}
      >
        Dobro došao {name}!
      </Text>
    </View>
  );*/
}

const styles = StyleSheet.create({
  welcomeBox: {
    marginLeft: 25,
    marginRight: 25,
    marginTop: 70,
    marginBottom: 20,
  },
});
