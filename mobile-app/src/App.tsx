import { registerRootComponent } from "expo";
import AppNavigator from "./navigation/AppNavigator";

export default function App() {
  return AppNavigator();
}

registerRootComponent(App);
