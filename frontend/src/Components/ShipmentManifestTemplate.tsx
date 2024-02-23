import React, { useEffect, useState } from 'react';
import { Document, Page, Text, View, StyleSheet  } from '@react-pdf/renderer';
import Cookies from "js-cookie";

export interface Address {
    addressId: string;
    streetAddress: string;
    city: string;
    postalCode: string;
    country: string;
}

export interface Item {
    itemId: string;
    type: string;
    name: string;
    price: number | null;
    description: string;
    handlingInstructions: string;
}

export interface Inventory {
    inventoryId: string;
    name: string;
    approximateWeight: number | null;
    description: string;
    items: Item[];
}

export interface Shipment {
    shipmentId: string;
    pickupAddress: Address;
    destinationAddress: Address;
    userId: string | null;
    truckId: string | null;
    status: string;
    shipmentName: string;
    approximateWeight: number;
    weight: number;
    email: string;
    phoneNumber: string;
    expectedMovingDate: string;
    actualMovingDate: string;
    firstName: string | null;
    lastName: string | null;
    approximateShipmentValue: number;
    inventoriesItemsResponseModels: Inventory[];
}


const styles = StyleSheet.create({
    page: {
        flexDirection: 'column',
        backgroundColor: '#FFFFFF',
        padding: 30,
        fontFamily: 'Helvetica',
    },
    documentTitle: {
        fontSize: 24,
        textAlign: 'center',
        marginBottom: 20,
        fontWeight: 'bold',
        color: '#003366',
        textDecoration: 'underline',
    },
    subtitle: {
        fontSize: 12,
        fontWeight: 'bold',
        color: '#003366',
        textDecoration: 'underline',
        marginBottom: 4,
    },
    sectionSeparator: {
        marginVertical: 10,
        borderBottomWidth: 1,
        borderBottomColor: '#EEE',
    },
    companyInfoContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginBottom: 20,
    },
    companyInfoText: {
        fontSize: 10,
        color: '#333333',
    },
    shipmentDetails: {
        flexDirection: 'row',
        marginBottom: 20,
    },
    detailColumn: {
        width: '50%',
    },
    detailGroup: {
        marginBottom: 4,
    },
    detailLabel: {
        fontSize: 10,
        fontWeight: 'bold',
        color: '#003366',
    },
    detailValue: {
        fontSize: 10,
        color: '#333333',
    },
    inventorySection: {
        marginBottom: 20,
    },
    inventoryHeader: {
        fontSize: 14,
        fontWeight: 'bold',
        color: '#003366',
        marginBottom: 4,
    },
    inventoryDescription: {
        fontSize: 10,
        color: '#333333',
        marginBottom: 8,
    },
    inventoryStatus: {
        fontSize: 12,
        fontWeight: 'normal',
    },
    table: {
        marginBottom: 20,
        borderColor: '#DDD',
    },
    tableHeader: {
        flexDirection: 'row',
        backgroundColor: '#003366',
        borderColor: '#DDD',
    },
    tableRow: {
        flexDirection: 'row',
        borderColor: '#DDD',
        backgroundColor: '#F0F8FF',
    },
    tableCellHeader: {
        fontSize: 10,
        fontWeight: 'bold',
        padding: 5,
        color: '#FFFFFF',
        textAlign: 'left',
        borderRightWidth: 1,
        flexWrap: 'wrap',
    },
    tableCell: {
        fontSize: 10,
        padding: 4,
        color: '#333333',
        textAlign: 'left',
        borderRightWidth: 1,
        flexWrap: 'wrap',
    },
    lastCell: {
        borderRightWidth: 0,
    },
    smallCell: {
        flex: 0.5,
    },
    largeCell: {
        flex: 2,
    },
});


const checkValue = (value: any) => (value ? value : 'n/a');

export default function ShipmentManifestTemplate (props: Shipment){
    const [shipment, setShipment] = useState(props);
    const [inventoriesWithItem, setInventoriesWithItem] = useState(props.inventoriesItemsResponseModels);
    const selectedLanguage = Cookies.get("MovingExpress_SelectedLanguage")

    const translation = {
        EN: {
            clientReport: "Client Report",
            movingExpress: "Moving Express",
            email: "Email",
            phoneNumber: "Phone Number",
            mainOffice: "Main Office",
            shipmentDetails: "Shipment Details",
            shipmentId: "Shipment ID",
            shipmentName: "Shipment Name",
            customerName: "Customer's Name",
            pickupAddress: "Pickup Address",
            destinationAddress: "Destination Address",
            expectedMovingDate: "Expected Moving Date",
            approximateWeight: "Approximate Weight",
            itemNo: "Item No.",
            type: "Type",
            name: "Name",
            price: "Price",
            description: "Description",
            handlingInstructions: "Handling Instructions",
            noInventories: "No inventories available.",
        },
        FR: {
            clientReport: "Rapport Client",
            movingExpress: "Moving Express",
            email: "Email",
            phoneNumber: "Numéro de téléphone",
            mainOffice: "Bureau Principal",
            shipmentDetails: "Détails de l'Expédition",
            shipmentId: "ID de l'Expédition",
            shipmentName: "Nom de l'Expédition",
            customerName: "Nom du Client",
            pickupAddress: "Adresse de Ramassage",
            destinationAddress: "Adresse de Destination",
            expectedMovingDate: "Date de Déménagement Prévue",
            approximateWeight: "Poids Approximatif",
            itemNo: "Numéro d'Article",
            type: "Type",
            name: "Nom",
            price: "Prix",
            description: "Description",
            handlingInstructions: "Instructions de Manipulation",
            noInventories: "Aucun inventaire disponible.",
        },
    };

    const getRowStyle = (index: number) => ({
        ...styles.tableRow,
        backgroundColor: index % 2 === 0 ? '#F0F8FF' : '#FFFFFF',
    });
    return (
        <Document>
            <Page size="A4" style={styles.page}>
                <Text style={styles.documentTitle}>
                    {selectedLanguage === "EN" ? "Shipment Manifest" : "Manifeste D'Expédition"}
                </Text>

                {/* Company Information Section */}
                <View style={styles.companyInfoContainer}>
                    <View>
                        <Text style={styles.companyInfoText}>Moving Express</Text>
                        <Text style={styles.companyInfoText}>
                            {selectedLanguage === "EN" ? "Email: info@movinexpress.com" : "Email: info@movinexpress.com"}
                        </Text>
                        <Text style={styles.companyInfoText}>
                            {selectedLanguage === "EN" ? "Phone: +1 123-456-7890" : "Téléphone: +1 123-456-7890"}
                        </Text>
                        <Text style={styles.companyInfoText}>
                            {selectedLanguage === "EN" ? "Main Office: 1234 Main St, City, Country" : "Bureau Principal: 1234 Main St, Ville, Pays"}
                        </Text>
                    </View>
                </View>

                {/* Shipment Details Section */}
                <Text style={styles.subtitle}>
                    {selectedLanguage === "EN" ? "Shipment Details" : "Détails de l'Expédition"}
                </Text>
                <View style={styles.shipmentDetails}>
                    <View style={styles.detailColumn}>
                        <View style={styles.detailGroup}>
                            <Text style={styles.detailValue}>{selectedLanguage === "EN" ? "Shipment ID: " : "ID de l'Expédition: "} {shipment.shipmentId}</Text>
                        </View>
                        <View style={styles.detailGroup}>
                            <Text style={styles.detailLabel}>{selectedLanguage === "EN" ? "Shipment Name:" : "Nom de l'Expédition:"} {shipment.shipmentName}</Text>
                        </View>
                        <View style={styles.detailGroup}>
                            <Text style={styles.detailLabel}>{selectedLanguage === "EN" ? "Customer's Name:" : "Nom du Client:"} {shipment.firstName} {shipment.lastName}</Text>
                        </View>
                        <View style={styles.detailGroup}>
                            <Text style={styles.detailLabel}>{selectedLanguage === "EN" ? "Truck VIN:" : "Identifiant du camion:"} {shipment.truckId}</Text>
                        </View>

                    </View>
                    <View style={styles.detailColumn}>
                        <View style={styles.detailGroup}>
                            <Text style={styles.detailLabel}>{selectedLanguage === "EN" ? "Shipment weight:" : "Poids du transport:"} {shipment.weight} lb</Text>
                        </View>
                        <View style={styles.detailGroup}>
                            <Text style={styles.detailLabel}>{selectedLanguage === "EN" ? "Pickup Address:" : "Adresse de Ramassage:"} {`${shipment.pickupAddress.streetAddress}, ${shipment.pickupAddress.city}, ${shipment.pickupAddress.postalCode}, ${shipment.pickupAddress.country}`}</Text>
                        </View>
                        <View style={styles.detailGroup}>
                            <Text style={styles.detailLabel}>{selectedLanguage === "EN" ? "Destination Address:" : "Adresse de Destination:"} {`${shipment.destinationAddress.streetAddress}, ${shipment.destinationAddress.city}, ${shipment.destinationAddress.postalCode}, ${shipment.destinationAddress.country}`}</Text>
                        </View>
                    </View>
                </View>

                {/* Inventories Section */}
                {inventoriesWithItem?.map((inventory: Inventory, index: any) => (
                    <View key={index} style={styles.inventorySection}>
                        <Text style={styles.inventoryHeader}>
                            {inventory.name}
                        </Text>
                        <Text style={styles.inventoryDescription}>{checkValue(inventory.description)}</Text>
                        <View style={styles.detailGroup}>
                            <Text style={styles.detailLabel}>
                                {selectedLanguage === "EN" ? "Approximate Weight:" : "Poids Approximatif:"} {checkValue(inventory.approximateWeight)} lb
                            </Text>
                        </View>

                        <View style={styles.table}>
                            <View style={styles.tableHeader}>
                                <Text style={[styles.tableCellHeader, styles.smallCell]}>{selectedLanguage === "EN" ? "Item No." : "Numéro d'Article"}</Text>
                                <Text style={[styles.tableCellHeader, styles.smallCell]}>{selectedLanguage === "EN" ? "Type" : "Type"}</Text>
                                <Text style={[styles.tableCellHeader, styles.smallCell]}>{selectedLanguage === "EN" ? "Name" : "Nom"}</Text>
                                <Text style={[styles.tableCellHeader, styles.smallCell]}>{selectedLanguage === "EN" ? "Price" : "Prix"}</Text>
                                <Text style={[styles.tableCellHeader, styles.largeCell]}>{selectedLanguage === "EN" ? "Description" : "Description"}</Text>
                                <Text style={[styles.tableCellHeader, styles.largeCell]}>{selectedLanguage === "EN" ? "Handling Instructions" : "Instructions de Manipulation"}</Text>
                            </View>
                            {inventory.items?.map((item: Item, itemIndex: any) => (
                                <View key={itemIndex} style={getRowStyle(itemIndex)}>
                                    <Text style={[styles.tableCell, styles.smallCell]}>{itemIndex + 1 }</Text>
                                    <Text style={[styles.tableCell, styles.smallCell]}>{checkValue(item.type)}</Text>
                                    <Text style={[styles.tableCell, styles.smallCell]}>{checkValue(item.name)}</Text>
                                    <Text style={[styles.tableCell, styles.smallCell]}>{checkValue(item.price)}</Text>
                                    <Text style={[styles.tableCell, styles.largeCell]}>{checkValue(item.description)}</Text>
                                    <Text style={[styles.tableCell, styles.largeCell]}>{checkValue(item.handlingInstructions)}</Text>
                                </View>
                            ))}
                        </View>
                    </View>
                )) || <Text>{selectedLanguage === "EN" ? "No inventories available." : "Aucun inventaire disponible."}</Text>}
            </Page>
        </Document>
    );

};
