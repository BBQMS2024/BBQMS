import React, { useState, useEffect } from 'react';
import { View, Modal, Text, Button, StyleSheet, Dimensions } from 'react-native';
import { Colors } from '../constants/colors';
import { generateTicket } from '../services/fetchData';
import { getExpoToken } from '../utils/tokenUtils';

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

export default function ({ visible, onClose, number }: { visible: any, onClose: any, number: number}) {

  const [modalVisible, setModalVisible] = useState(visible);

  useEffect(() => {

    setModalVisible(visible);
    if (visible) {
      const timer = setTimeout(() => {
        setModalVisible(false);
        onClose();
      }, 10000);

      return () => clearTimeout(timer);
    }
  }, [visible]);

  if(number != 0 )
  return (
    <Modal
      animationType="slide"
      transparent={true}
      visible={modalVisible}
      onRequestClose={() => {
        onClose();
      }}
    >
      <View style={styles.modalContainer}>
        <View style={styles.modalContent}>
          <Text style={styles.message}>Vaš broj je:</Text>
          <View style={styles.numberContainer}>
            <Text style={styles.number}>{number}</Text>
          </View>
          <View style={styles.buttonContainer}>
            <Button title="Dismiss" onPress={onClose} color={Colors.ACCENT} />
          </View>
        </View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  modalContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)', // Semi-transparent background
  },
  modalContent: {
    backgroundColor: 'white',
    padding: windowWidth * 0.05, // Responsive padding
    borderRadius: windowWidth * 0.05, // Responsive border radius
    width: windowWidth * 0.8, // Responsive width
    alignItems: 'center', // Center content horizontally
  },
  message: {
    fontSize: windowWidth * 0.05, // Responsive font size
    marginBottom: windowHeight * 0.02, // Responsive margin
  },
  numberContainer: {
    marginBottom: windowHeight * 0.04, // Responsive margin
  },
  number: {
    fontSize: windowWidth * 0.12, // Responsive font size
    fontWeight: 'bold',
  },
  buttonContainer: {
    marginTop: windowHeight * 0.02, // Responsive margin
    alignSelf: 'flex-end', // Align button to the right
  },
});