import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Dimensions } from 'react-native';
import { Fonts } from '../constants/fonts';
import { Colors } from '../constants/colors';
const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

export default function TicketInfoScreen({ route }: {route: any}) {

  const ticket = route.params.ticket 
    console.log(ticket)
  useEffect(() => {

  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Ticket Details</Text>
      {ticket ? (
        <View style={styles.detailsContainer}>
          <Text style={styles.detailText}>Naziv: {ticket.name}</Text>
          <Text style={styles.detailText}>Generisano: {new Date(ticket.date).toLocaleString()}</Text>
          <Text style={styles.detailText}>Podići na šalterima:</Text>
          {ticket.stations.map((station: string, index: number) => (
            <Text key={index} style={styles.stationText}>{"- "+ station}</Text>
          ))}
        </View>
      ) : (
        <Text>Loading...</Text>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: Colors.BACKGROUND,
      justifyContent: 'flex-start',
      paddingHorizontal: windowWidth * 0.05,
      paddingTop: windowHeight * 0.2,
    },
    title: {
      fontSize: 28,
      fontWeight: 'bold',
      marginBottom: windowHeight * 0.03,
      fontFamily: Fonts.ARIAL,
      alignSelf: 'center',
    },
    detailsContainer: {
      marginTop: windowHeight * 0.03,
    },
    detailText: {
      fontSize: 20,
      marginBottom: 10,
      fontFamily: Fonts.ARIAL,
    },
    stationText: {
        fontSize: 20,
        marginBottom: 5,
        fontFamily: Fonts.ARIAL,
        marginLeft: 10,
      }
  });