import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import { registerRootComponent } from 'expo';
import React, { useEffect, useState } from 'react';
import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import AsyncStorage from "@react-native-async-storage/async-storage";
import HomeScreen from './screens/HomeScreen';

//import HomeScreen from "./screens/HomeScreen";


const Stack = createNativeStackNavigator();

export default function App() {
  const [accountID, setAccountID] = useState(1);

/*  const getData = async () => {
    try {
      const value = await AsyncStorage.getItem("account");
      console.log("Id u async storage: " + value);
      if (value != null) {
        const id = parseInt(value);
        setAccountID(id);
      } else setAccountID(0);
    } catch (e) {
      console.log("Error when geting data: " + e);
    }
  };
  */
  useEffect(() => {


    
  }, []);


  return (
      <>
        <StatusBar hidden />
        <NavigationContainer>
          <Stack.Navigator>
            <Stack.Screen
              name="Home"
              component={HomeScreen}
              options={{ title: "Welcome", headerShown: false }}
              initialParams={{ accountID: accountID }}/>
          </Stack.Navigator>
        </NavigationContainer>
      </>
    );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

registerRootComponent(App);
