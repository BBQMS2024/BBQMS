import React, { useState } from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet } from 'react-native';
import { Screens } from '../constants/screens';
import { getBranchServices } from '../services/fetchData';
import { Colors } from '../constants/colors';
import { Fonts } from '../constants/fonts';


export default function BranchPickScreen({ route, navigation }: { route: any, navigation: any }){

  const { branches } = route.params
    
  const renderItem = ({ item }:{ item: any }) => (
    <TouchableOpacity
      style={styles.branchItem}
      onPress={() => handlePress(item.name)}
    >
      <Text style={{ fontSize: 28, fontFamily: Fonts.ARIAL}}>{item.name}</Text>
    </TouchableOpacity>
  );

  async function handlePress(id: any) {

    getBranchServices(route.params.code, id)
        .then(function (services) {           
                navigation.navigate(Screens.WELCOME, {
                details: route.params.details, services: services
            })

        })
        .catch((error) => {
            console.error("Error:", error);
        });
}
return (
    <View style={styles.container}>
      <Text style={styles.title}>Poslovnice</Text>
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
      backgroundColor: '#fff', // Set background color if necessary
      justifyContent: 'center', // Align the list to the center vertically
      paddingTop: 100, // Add padding to the top of the container to move the list lower
    },
    title: {
        fontSize: 42,
        fontWeight: 'bold',
        marginBottom: 30,
        marginLeft: 10,
        fontFamily: Fonts.ARIAL
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