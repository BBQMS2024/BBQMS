import React, { useEffect } from "react";
import { View, StyleSheet, Animated, Text, FlatList, TouchableOpacity } from "react-native";
import WelcomeMessage from "../components/WelcomeMessage";
import { useFonts } from "expo-font";
const { Fonts } = require("../constants/fonts");
const { Assets } = require("../constants/assets");
const { Colors } = require("../constants/colors");

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

    const renderItem = ({ item }:{ item: any }) => (
        <TouchableOpacity
          style={styles.branchItem}
          onPress={() => handlePress(item.name)}
        >
          <Text style={{ fontSize: 28, fontFamily: Fonts.ARIAL}}>{item.name}</Text>
        </TouchableOpacity>
      );
    
      async function handlePress(id: any) {
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

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: Colors.BACKGROUND,
        alignItems: "center",
        justifyContent: "center",
    },
    logoContainer: {
        
    },
    logo: {
        width: 250,
        height: 250,
        resizeMode: "contain",
    },
    title: {
        fontSize: 42,
        fontWeight: 'bold',
        marginBottom: 30,
        marginTop: 30,
        fontFamily: Fonts.ARIAL
      },
      titleContainer: {
        alignSelf: 'flex-start',
        marginLeft: 20, // Adjust the margin as needed
        marginTop: 30, // Adjust the margin as needed
      },
    flatListContainer: {
      flexGrow: 1,
      paddingHorizontal: 20,
      paddingBottom: 20, // Add padding to the bottom of the list
    },
    branchItem: {
      paddingVertical: 15,
      borderBottomWidth: 1,
      borderBottomColor: '#ccc',
      backgroundColor: Colors.ACCENT
    },
});
