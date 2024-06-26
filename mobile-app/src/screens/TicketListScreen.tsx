import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet, Dimensions, ActivityIndicator } from 'react-native';
import { Screens } from '../constants/screens';
import { getTickets } from '../services/fetchData';
import { Colors } from '../constants/colors';
import { Fonts } from '../constants/fonts';
import { Ionicons } from '@expo/vector-icons';
import { getExpoToken } from '../utils/tokenUtils';
const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

export default function TicketListScreen({ navigation }: { navigation: any }){

    const [tickets, setTickets] = useState<any[]>([]);

    useEffect(() => {
        const fetchTickets = async () => {
            try {
                const fetchedTickets = await getTickets(getExpoToken());
                setTickets(fetchedTickets);
            } catch (error) {
                console.error('Error fetching tickets:', error);
            }
        };
        fetchTickets();

        const intervalId = setInterval(fetchTickets, 10000);

        return () => clearInterval(intervalId);
    }, []);

    
    const renderItem = ({ item }: { item: any }) => {
      return (
        <TouchableOpacity
            style={styles.ticketItem}
            onPress={() => handlePress(item.id)}
        >
            <Text style={styles.ticketName}>{item.name}</Text>
            <View style={styles.stationContainer}>
                <Text style={styles.stationTitle}>Šalteri</Text>
                {item.stations.map((station: string, index: number) => (
                    <Text key={index} style={styles.stationList}>{station}</Text>
                ))}
            </View>
            <View style={styles.iconContainer}>
                <Ionicons name="information-circle-outline" size={windowWidth * 0.1} color={Colors.ACCENT} />
            </View>
        </TouchableOpacity>
    );
      };

    async function handlePress(id: any) {

      let ticket = tickets.find(item => item.id == id)    
      navigation.navigate(Screens.TICKET_INFO, {
        ticket:ticket
    })

}

if (tickets.length === 0) {
  return (
      <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color={Colors.PRIMARY} />
      </View>
  );
} else {
  return (
      <View style={styles.container}>
          <Text style={styles.title}>Tiketi</Text>
          <FlatList
              data={tickets}
              renderItem={renderItem}
              keyExtractor={(item) => item.id.toString()}
              contentContainerStyle={styles.flatListContainer}
          />
      </View>
  );
}

};

const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: Colors.BACKGROUND,
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
      paddingVertical: windowHeight * 0.06, 
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
        marginTop: windowHeight * 0.02, 
      },
      loadingContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    stationList: {
      fontSize: windowWidth * 0.05,
      color: 'gray', 
      marginTop: 5,
  },
  stationTitle: {
    fontSize: windowWidth * 0.06,
    fontWeight: 'bold',
    marginBottom: 5,
},
stationContainer: {
  marginTop: 30,
},
});