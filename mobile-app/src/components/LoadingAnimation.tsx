import { View } from "react-native";
import React from "react";
import LottieView from "lottie-react-native";

export default function LoadingAnimation() {
  return (
    <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
      <LottieView
        source={require("../../assets/animations/loading.json")}
        autoPlay
        loop
        style={{ width: 200, height: 200 }}
      />
    </View>
  );
}
