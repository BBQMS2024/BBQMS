import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import CodeEnterScreen from "../screens/CodeEnterScreen";
import WelcomeScreen from "../screens/WelcomeScreen";
import BranchPickScreen from "../screens/BranchPickScreen"
import TicketListScreen from '../screens/TicketListScreen';
import TicketInfoScreen from '../screens/TicketInfoScreen'
import { Screens } from "../constants/screens";
import AssignedNumberAlert from "../components/AssignedNumberAlert";
import { Ionicons } from '@expo/vector-icons';

const Stack = createStackNavigator();
const Tab = createBottomTabNavigator();

const BottomNavigator = ({ route }) =>  {

  const { details, services, branchID } = route.params;  
  return (
    <Tab.Navigator  screenOptions={{headerShown: false }}>
      <Tab.Screen
        name="Home"
        component={WelcomeScreen}
        initialParams={{ details, services , branchID }}
        options={{
          tabBarIcon: ({ color, size }) => (
            <Ionicons name="home-outline" size={size} color={color} />
          ),
        }}
      />
      <Tab.Screen
        name="Moji tiketi"
        component={TicketListScreen}
        options={{
          tabBarIcon: ({ color, size }) => (
            <Ionicons name="list-outline" size={size} color={color} />
          ),
        }}
      />
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
                <Stack.Screen name={Screens.TICKET_INFO} component={TicketInfoScreen} />
            </Stack.Navigator>
        </NavigationContainer>
    );
};

export default AppNavigator;
