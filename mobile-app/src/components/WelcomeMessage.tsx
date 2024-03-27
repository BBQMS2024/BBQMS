import React from "react";
import { View, Text, StyleSheet } from "react-native";

interface WelcomeMessageProps {
  name: string;
  welcome: string;
  font : string;
}

export default function WelcomeMessage(params : WelcomeMessageProps ) {

    return (
      <View style={styles.welcomeBox}>
        <Text
          style={{fontFamily: params.font, fontSize: 28}}
        >
          {params.welcome}{'\n'}{params.name}
        </Text>
      </View>
    );
}

const styles = StyleSheet.create({
  welcomeBox: {
    marginLeft: 20,
    marginRight: 20,
    marginTop: 80,
    marginBottom: 10,
    backgroundColor : "#548CA8",
    flex: 1,
  },
});
