import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import CodeEnterScreen from "../screens/CodeEnterScreen";
import WelcomeScreen from "../screens/WelcomeScreen";
import BranchPickScreen from "../screens/BranchPickScreen"
import { Screens } from "../constants/screens";
import AssignedNumberAlert from "../components/AssignedNumberAlert";

const Stack = createStackNavigator();

const AppNavigator = () => {
    return (
        <NavigationContainer>
            <Stack.Navigator
                initialRouteName={Screens.CODE_ENTER}
                screenOptions={{
                    headerShown: false,
                }}
            >
                <Stack.Screen
                    name={Screens.CODE_ENTER}
                    component={CodeEnterScreen}
                />
                <Stack.Screen name={Screens.BRANCH_PICK} component={BranchPickScreen} />
                <Stack.Screen name={Screens.WELCOME} component={WelcomeScreen} />
                <Stack.Screen name={Screens.ASSIGNED_NUMBER} component={AssignedNumberAlert} />
            </Stack.Navigator>
        </NavigationContainer>
    );
};

export default AppNavigator;
