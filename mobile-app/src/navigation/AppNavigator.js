import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import CodeEnterScreen from "../screens/CodeEnterScreen";
import WelcomeScreen from "../screens/WelcomeScreen";
import BranchPickScreen from "../screens/BranchPickScreen"
import TicketListScreen from '../screens/TicketListScreen';
import { Screens } from "../constants/screens";
import AssignedNumberAlert from "../components/AssignedNumberAlert";

const Stack = createStackNavigator();
const Tab = createBottomTabNavigator();

const BottomNavigator = ({ route }) =>  {

  const { details, services } = route.params;

  return (
    <Tab.Navigator>
      <Tab.Screen name="Home" component={WelcomeScreen} initialParams={{details,services}}/>
      <Tab.Screen name="My Tickets" component={TicketListScreen} details={details}/>
    </Tab.Navigator>
  );
};

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
                <Stack.Screen name={Screens.BOTTOM_NAV} component={BottomNavigator} />
                <Stack.Screen name={Screens.ASSIGNED_NUMBER} component={AssignedNumberAlert} />
            </Stack.Navigator>
        </NavigationContainer>
    );
};

export default AppNavigator;
