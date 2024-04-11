import React, { useState } from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet, Dimensions } from 'react-native';
import { Screens } from '../constants/screens';
import { getBranchServices } from '../services/fetchData';
import { Colors } from '../constants/colors';
import { Fonts } from '../constants/fonts';
import { Ionicons } from '@expo/vector-icons';

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

export default function BranchPickScreen({ route, navigation }: { route: any, navigation: any }){

  const { branches } = route.params


  const renderItem = ({ item }: { item: any }) => (
    <TouchableOpacity
      style={styles.branchItem}
      onPress={() => handlePress(item.id)}
    >
      <Text style={styles.branchName}>{item.name}</Text>
      <View style={styles.iconContainer}>
        <Ionicons name="chevron-forward-circle-outline" size={windowWidth * 0.1} color={Colors.ACCENT} />
      </View>
    </TouchableOpacity>
  );
  async function handlePress(id: any) {
    getBranchServices(route.params.code, id)
        .then(function (services) {           
                navigation.navigate(Screens.BOTTOM_NAV, {
                details: route.params.details, services: services, branchID: id
            })

        })
        .catch((error) => {
            console.error("Error:", error);
        });
}
return (
    <View style={styles.container}>
      <Text style={[styles.title]}>Poslovnice</Text>
      <FlatList
        data={branches}
        renderItem={renderItem}
        keyExtractor={(item) => item.id.toString()}
        contentContainerStyle={styles.flatListContainer}
      />
    </View>
  );
};

const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: '#fff',
      justifyContent: 'center',
      paddingTop: windowHeight * 0.1,
    },
    title: {
      fontSize: windowWidth * 0.1,
      fontWeight: 'bold',
      marginBottom: windowHeight * 0.03,
      marginLeft: windowWidth * 0.05,
      fontFamily: Fonts.ARIAL,
    },
    flatListContainer: {
      flexGrow: 1,
      paddingHorizontal: windowWidth * 0.05,
      paddingBottom: windowHeight * 0.05,
    },
    branchItem: {
      paddingHorizontal: windowWidth * 0.05,
      paddingVertical: windowHeight * 0.06, // Adjust the padding to make the items taller
      marginBottom: windowHeight * 0.02,
      borderRadius: windowWidth * 0.05,
      backgroundColor: '#fff',
      shadowColor: Colors.ACCENT,
      shadowOffset: {
        width: 0,
        height: 2,
      },
      shadowOpacity: 0.25,
      shadowRadius: 3.84,
      elevation: 5,
    },
    branchContent: {
      flexDirection: 'row', 
      alignItems: 'center', 
      justifyContent: 'center',
    },
    branchName: {
      flex: 1, 
      fontSize: windowWidth * 0.08,
      fontFamily: Fonts.ARIAL,
      textAlign: 'center',
    },
    icon: {
      marginLeft: windowWidth * 0.02,
    },
    iconContainer: {
        alignItems: 'center',
        marginTop: windowHeight * 0.02, // Adjust the margin to position the icon below the branch name
      },
});