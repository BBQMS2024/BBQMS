/*import React from "react";
import { View, Text, StyleSheet } from "react-native";

export default function WelcomeMessage({ name, male, settings }) {
  if (!male)
    return (
      <View style={styles.welcomeBox}>
        <Text
          style={{ fontSize: settings.fontSize + 6, fontFamily: settings.font }}
        >
          Dobro došla {name}!
        </Text>
      </View>
    );

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
  );
}

const styles = StyleSheet.create({
  welcomeBox: {
    marginLeft: 25,
    marginTop: 20,
    marginBottom: 20,
  },
});
*/