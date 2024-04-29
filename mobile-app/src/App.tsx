import { registerRootComponent } from "expo";
import AppNavigator from "./navigation/AppNavigator";
import AsyncStorage from "@react-native-async-storage/async-storage";

export default function App() {
  AsyncStorage.clear();
  return AppNavigator();
}

registerRootComponent(App);