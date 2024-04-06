import React, { useEffect } from "react";
import { View, StyleSheet, Animated, Text, FlatList, TouchableOpacity } from "react-native";
import WelcomeMessage from "../components/WelcomeMessage";
import { useFonts } from "expo-font";
const { Fonts } = require("../constants/fonts");
const { Assets } = require("../constants/assets");
const { Colors } = require("../constants/colors");
import { Ionicons } from '@expo/vector-icons';

export default function WelcomeScreen({ route }: { route: any }) {
    const details = route.params.details;
    let { name, welcomeMessage, font, logoUrl } = details;
    let { services } = route.params
 
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

    React.useEffect(() => {
        Animated.timing(fadeAnim, {
            toValue: 1,
            duration: 2000,
            useNativeDriver: true,
        }).start();
    }, [fadeAnim]);

    const renderItem = ({ item }: { item: any }) => (
        <TouchableOpacity
          style={styles.branchItem}
          onPress={() => handlePress(item.name)}
        >
          <View style={styles.branchInfoContainer}>
            <Ionicons name="ios-information-circle" size={24} color={Colors.ACCENT} style={styles.icon} />
            <Text style={styles.branchName}>{item.name}</Text>
            <Ionicons name="chevron-forward" size={24} color={Colors.ACCENT} style={styles.icon} />
          </View>
        </TouchableOpacity>
      );
      async function handlePress(id: any) {
        //to doo
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
            <View style={styles.titleContainer}>
                <Text style={styles.title}>Servisi</Text>
            </View>
            <FlatList
                data={services}
                renderItem={renderItem}
                keyExtractor={(item) => item.id.toString()}
                contentContainerStyle={styles.flatListContainer}
      />
        </View>
    );
}

export const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: Colors.BACKGROUND,
      alignItems: 'center',
      justifyContent: 'center',
    },
    titleContainer: {
      alignSelf: 'flex-start',
      marginLeft: 20,
      marginTop: 30,
      marginBottom: 10, // Adjust the margin to lower the items from the title
    },
    logoContainer: {
      marginBottom: 20, // Add margin to separate logo from title
    },
    logo: {
      width: '80%', // Adjust the logo size to fit better on all screens
      height: undefined, // Allow height to adjust automatically based on width
      aspectRatio: 1, // Maintain aspect ratio
      resizeMode: 'contain',
    },
    title: {
      fontSize: 42,
      fontWeight: 'bold',
      fontFamily: Fonts.ARIAL,
      color: Colors.TEXT_PRIMARY,
    },
    flatListContainer: {
      flexGrow: 1,
      paddingHorizontal: 20,
      paddingTop: 10, // Add padding to the top of the list to separate from title
    },
    branchItem: {
      paddingVertical: 15,
      paddingHorizontal: 20,
      marginBottom: 15,
      borderRadius: 10,
      borderColor: Colors.ACCENT,
      borderWidth: 1,
      shadowColor: '#000',
      shadowOffset: {
        width: 0,
        height: 2,
      },
      shadowOpacity: 0.25,
      shadowRadius: 3.84,
      elevation: 5,
      width: '100%', // Make items take full width of the screen
    },
    branchInfoContainer: {
      flexDirection: 'row', // Arrange items horizontally
      alignItems: 'center', // Center items vertically
    },
    branchName: {
      fontSize: 34,
      fontFamily: Fonts.ARIAL,
      color: Colors.TEXT_PRIMARY,
      marginLeft: 10, // Add some space between text and icon
    },
    icon: {
      marginRight: 10, // Add some space between icon and text
    },
  });