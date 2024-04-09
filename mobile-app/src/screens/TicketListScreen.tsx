import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet, Dimensions } from 'react-native';
import { Screens } from '../constants/screens';
import { getBranchServices, getTickets } from '../services/fetchData';
import { Colors } from '../constants/colors';
import { Fonts } from '../constants/fonts';
import { Ionicons } from '@expo/vector-icons';
import { getExpoToken } from '../utils/tokenUtils';

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

export default async function TicketListScreen({ route, navigation }: { route: any, navigation: any }){
    
    let tickets = await getTickets(getExpoToken())

  useEffect(() => {
   
    }, []);
    
  const renderItem = ({ item }: { item: any }) => (
    <TouchableOpacity
      style={styles.ticketItem}
      onPress={() => handlePress(item.name)}
    >
      <Text style={styles.ticketName}>{item.name}</Text>
      <View style={styles.iconContainer}>
        <Ionicons name="chevron-forward-circle-outline" size={windowWidth * 0.1} color={Colors.ACCENT} />
      </View>
    </TouchableOpacity>
  );
  async function handlePress(id: any) {

    let ticket = tickets.findIndex(id)     

    navigation.navigate(Screens.TICKET_INFO, {
        ticket:ticket
    })

}
return (
    <View style={styles.container}>
      <Text style={[styles.title]}>Poslovnice</Text>
      <FlatList
        data={tickets}
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
    ticketItem: {
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
    ticketContent: {
      flexDirection: 'row', 
      alignItems: 'center', 
      justifyContent: 'center',
    },
    ticketName: {
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