import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import CodeEnterScreen from "../screens/CodeEnterScreen";
import WelcomeScreen from "../screens/WelcomeScreen";

const Stack = createStackNavigator();

const AppNavigator = () => {
    return (
        <NavigationContainer>
            <Stack.Navigator
                initialRouteName="CodeEnterScreen"
                screenOptions={{
                    headerShown: false,
                }}
            >
                <Stack.Screen
                    name="CodeEnterScreen"
                    component={CodeEnterScreen}
                />
                <Stack.Screen name="WelcomeScreen" component={WelcomeScreen} />
            </Stack.Navigator>
        </NavigationContainer>
    );
};

export default AppNavigator;
