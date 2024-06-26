import React, { useEffect, useRef, useState } from "react";
import {
  View,
  StyleSheet,
  Animated,
  Text,
  FlatList,
  TouchableOpacity,
  Platform,
  Alert,
} from "react-native";
import WelcomeMessage from "../components/WelcomeMessage";
import { useFonts } from "expo-font";
const { Fonts } = require("../constants/fonts");
const { Assets } = require("../constants/assets");
const { Colors } = require("../constants/colors");
import { Ionicons } from "@expo/vector-icons";
import AssignedNumberAlert from "../components/AssignedNumberAlert";
//import * as Notifications from "expo-notifications";
//import * as Device from "expo-device";
import { getExpoToken, setExpoToken } from "../utils/tokenUtils";
import { generateTicket } from "../services/fetchData";

export default function WelcomeScreen({ route }: { route: any }) {
  const { details } = route.params;
  let { services } = route.params;
  let { branchID } = route.params;

  let { name, welcomeMessage, font, logoUrl } = details;

  /*
    Notifications.setNotificationHandler({
        handleNotification: async () => ({
          shouldShowAlert: true,
          shouldPlaySound: true,
          shouldSetBadge: false,
        }),
      });
  */

  let [fontsLoaded] = useFonts({
    [Fonts.ARIAL]: Assets.ARIAL,
    [Fonts.TIMES_NEW_ROMAN]: Assets.TIMES_NEW_ROMAN,
    [Fonts.VERDANA]: Assets.VERDANA,
    [Fonts.HELVETICA]: Assets.HELVETICA,
    [Fonts.MONTSERRAT]: Assets.MONTSERRAT,
    [Fonts.CALIBRI]: Assets.CALIBRI,
    [Fonts.FUTURA]: Assets.FUTURA,
    [Fonts.BODONI]: Assets.BODONI,
    [Fonts.ROCKWELL]: Assets.ROCKWELL,
    [Fonts.COMIC_SANS_MS]: Assets.COMIC_SANS_MS,
  });

  const fadeAnim = React.useRef(new Animated.Value(0)).current;

  useEffect(() => {
    Animated.timing(fadeAnim, {
      toValue: 1,
      duration: 2500,
      useNativeDriver: true,
    }).start();
  }, [fadeAnim]);

  /*
  useEffect(() => {
    getToken();
  }, []);

  async function getToken() {
    try {
      await registerForPushNotificationsAsync().then((token) => {
        if (token != undefined) setExpoToken(token);
      });
    } catch (error) {
      console.error("Failed to fetch data in TasksScreen:", error);
    }
  }
  */

  const [modalVisible, setModalVisible] = useState(false);
  const [ticket, setTicket] = useState(0);

  const openModal = () => {
    setModalVisible(true);
  };

  const closeModal = () => {
    setModalVisible(false);
  };

  const renderItem = ({ item }: { item: any }) => (
    <TouchableOpacity
      style={styles.branchItem}
      onPress={() => handlePress(item.id)}
    >
      <View style={styles.branchInfoContainer}>
        <Ionicons
          name="information-circle"
          size={24}
          color={Colors.ACCENT}
          style={styles.icon}
        />
        <Text style={styles.branchName}>{item.name}</Text>
        <Ionicons
          name="chevron-forward"
          size={24}
          color={Colors.ACCENT}
          style={styles.icon}
        />
      </View>
    </TouchableOpacity>
  );

  async function handlePress(serviceID: any) {
    const assignTicket = async () => {
      try {
        const fetchedTicket = await generateTicket(
          getExpoToken(),
          branchID,
          serviceID
        );
        setTicket(fetchedTicket.number);
      } catch (error) {
        console.error("Error fetching tickets:", error);
      }
    };

    assignTicket();
    openModal();
  }

  if (!fontsLoaded) return <Text>Loading...</Text>;
  return (
    <View style={styles.container}>
      <View style={styles.logoContainer}>
        <Animated.Image
          style={[styles.logo, { opacity: fadeAnim }]}
          source={{ uri: logoUrl }}
        />
      </View>
      <WelcomeMessage name={name} font={font} welcome={welcomeMessage} />
      <FlatList
        data={services}
        renderItem={renderItem}
        keyExtractor={(item) => item.id.toString()}
        contentContainerStyle={styles.flatListContainer}
      />
      <AssignedNumberAlert
        visible={modalVisible}
        number={ticket}
        onClose={closeModal}
      />
    </View>
  );
}

/*
async function registerForPushNotificationsAsync() {
  let token;

  if (Platform.OS === "android") {
    await Notifications.setNotificationChannelAsync("default", {
      name: "default",
      importance: Notifications.AndroidImportance.MAX,
      vibrationPattern: [0, 250, 250, 250],
      lightColor: "#FF231F7C",
    });
  }

  if (Device.isDevice) {
    const { status: existingStatus } =
      await Notifications.getPermissionsAsync();
    let finalStatus = existingStatus;
    if (existingStatus !== "granted") {
      const { status } = await Notifications.requestPermissionsAsync();
      finalStatus = status;
    }
    if (finalStatus !== "granted") {
      alert("Failed to get push token for push notification!");
      return;
    }

    token = (await Notifications.getExpoPushTokenAsync()).data;
  } else {
    alert("Must use physical device for Push Notifications");
  }
  return token;
}
*/

export const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.BACKGROUND,
    alignItems: "center",
    justifyContent: "center",
  },
  titleContainer: {
    alignSelf: "flex-start",
    width: "100%",
  },
  logoContainer: {
    marginBottom: 20, // Add margin to separate logo from title
    fontFamily: Fonts.ARIAL,
    color: Colors.TEXT_PRIMARY,
  },
  logo: {
    marginTop: 80, // Add margin to separate logo from title
    width: "40%", // Adjust the logo size to fit better on all screens
    height: undefined, // Allow height to adjust automatically based on width
    aspectRatio: 1, // Maintain aspect ratio
    resizeMode: "contain",
  },
  title: {
    fontSize: 42,
    textAlign: "center",
    marginTop: 20,
    fontFamily: Fonts.ARIAL,
    color: Colors.TEXT_PRIMARY,
  },
  flatListContainer: {
    flexGrow: 1,
    paddingHorizontal: 20,
    paddingTop: 10, // Add padding to the top of the list to separate from title
  },
  branchItem: {
    marginTop: 10, // Add margin between items
    paddingVertical: 15,
    paddingHorizontal: 20,
    borderRadius: 10,
    borderColor: Colors.ACCENT,
    borderWidth: 1,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
    width: "100%", // Make items take full width of the screen
  },
  branchInfoContainer: {
    flexDirection: "row", // Arrange items horizontally
    alignItems: "center", // Center items vertically
  },
  branchName: {
    fontSize: 30,
    fontFamily: Fonts.ARIAL,
    color: Colors.TEXT_PRIMARY,
    marginLeft: 10, // Add some space between text and icon
  },
  icon: {
    marginRight: 10, // Add some space between icon and text
  },
});
