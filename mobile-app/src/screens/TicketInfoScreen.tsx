import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet } from 'react-native';

const TicketDetailsScreen = ({ route }: {route: any}) => {

  const { ticket } = route.params;


  useEffect(() => {

  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Ticket Details</Text>
      {ticket ? (
        <View style={styles.detailsContainer}>
          <Text>Ticket Name: {ticket.name}</Text>
          <Text>Generated: {ticket.date}</Text>
          <Text>Service: {ticket.service}</Text>
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
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  detailsContainer: {
    alignItems: 'flex-start',
  },
});

export default TicketDetailsScreen;
