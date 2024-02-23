import React, { useState, useRef, useEffect } from 'react'
import NormalNavBar from '../Components/NormalNavBar'
import { useAuth } from '../auth/components/AuthService'
import axios from 'axios'
import { toast } from 'react-toastify'
import { Link, redirect, useNavigate, useParams } from 'react-router-dom'
import { useLocation } from 'react-router-dom';
import InventoryPopUpForm from '../Components/InventoryPopUpForm'
import ScrollToTopBtn from '../Components/ScrollToTopBtn'
import ItemPopUpForm from '../Components/ItemPopUpForm'
import logo from '../Images/ME_Logo.png'
import { BlobProvider } from '@react-pdf/renderer'
import ShipmentReportTemplate from '../Components/ShipmentReportTemplate'
import Cookies from 'js-cookie'
import Popup from 'reactjs-popup'



axios.defaults.withCredentials = true
function ObserverShipmentDetails() {
    //let url = process.env.REACT_APP_BACKEND_URL + 'oauth2/authorization/okta'
    let url = '/'

    let location = useLocation();
    const auth = useAuth()
    const { observerCode } = useParams()

    const [inventories, setInventories] = useState([]);
    const [items, setItems] = useState([]);
    const [displayDetail, setDisplayDetail] = useState(false)


    const [operation, setOperation] = useState('');
    const [itemOperation, setItemOperation] = useState('');

    const [inventoryPopUpIsDisplayed, setInventoryPopUpIsDisplayed] = useState(false);
    const [itemPopUpIsDisplayed, setItemPopUpIsDisplayed] = useState(false);

    const [selectedInventoryObject, setSelectedInventoryObject] = useState(null)
    const [selectedItemObject, setSelectedItemObject] = useState(null)
    const [permissionLevel, setPermissionLevel] = useState('');

    const [permissionExist, setPermissionExist] = useState(false)




    //    let observerCod = new URLSearchParams(location.search).get('observerCode');


    let params = new URLSearchParams(location.search);
    let shipmentId: string = params.get('shipmentId') || '';

    const getShipmentDetailsByObserver = async (shipmentId: string) => {
        if (!observerCode) return;

        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/observer/${observerCode}`, {});
            var data = response.data;

            const shipmentDetail: Shipment = {
                shipmentId: data.shipmentId,
                status: data.status,
                expectedMovingDate: data.expectedMovingDate,
                actualMovingDate: data.actualMovingDate,
                shipmentName: data.shipmentName,
                approximateWeight: data.approximateWeight,
                weight: data.weight,
                pickupAddress: data.pickupAddress,
                destinationAddress: data.destinationAddress,
                vin: '',
                userId: '',
                email: data.email,
                phoneNumber: data.phoneNumber,
                firstName: data.firstName,
                lastName: data.lastName
            };
            getInventories(data.shipmentId, observerCode)
            setSelectedShipment(shipmentDetail)
            setDisplayDetail(true);
            getobserverCode(data.shipmentId)
        } catch (error) {
            toast.error(selectedLanguage === "EN" ?'Error fetching shipment details' : `Erreur lors de la récupération des détails de l'expédition`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            });
            setDisplayDetail(false);
        }
    };


    useEffect(() => {
        getShipmentDetailsByObserver(shipmentId)
    }, [])

    useEffect(() => {
        if (inventoryPopUpIsDisplayed) {
            //window.scrollTo({ top: -5, behavior: 'smooth' });
            //document.body.style.overflow = 'hidden';
            setOptionsVisible(v => false);
        } else {
            //document.body.style.overflow = 'auto';
        }
    }, [inventoryPopUpIsDisplayed]);

    useEffect(() => {
        if (itemPopUpIsDisplayed) {
            //window.scrollTo({ top: 0, behavior: 'smooth' });
            // document.body.style.overflow = 'hidden';
            setItemOptionsVisible(v => false);
        } else {
            // document.body.style.overflow = 'auto';
        }
    }, [itemPopUpIsDisplayed]);



    interface Address {
        addressId: string;
        streetAddress: string;
        city: string;
        country: string;
        postalCode: string;
    }

    interface Shipment {
        shipmentId: string;
        status: string;
        expectedMovingDate: string;
        actualMovingDate: string;
        shipmentName: string;
        approximateWeight: number;
        weight: number;
        pickupAddress: Address;
        destinationAddress: Address;
        vin: string;
        userId: string;
        email: string;
        phoneNumber: string;
        firstName: string;
        lastName: string;
    }

    const [selectedShipment, setSelectedShipment] = useState<Shipment>({
        shipmentId: '',
        status: '',
        expectedMovingDate: '',
        actualMovingDate: '',
        shipmentName: '',
        approximateWeight: 0,
        weight: 0,
        pickupAddress: {
            addressId: '',
            streetAddress: '',
            city: '',
            country: '',
            postalCode: '',
        },
        destinationAddress: {
            addressId: '',
            streetAddress: '',
            city: '',
            country: '',
            postalCode: '',
        },
        vin: '',
        userId: '',
        email: '',
        phoneNumber: '',
        firstName: '',
        lastName: '',
    });

    const getobserverCode = async (shipmentId:any) => {
        try {

            const responseObserverCode = await axios.get(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${shipmentId}/observers/${observerCode}`, {});
            let data = responseObserverCode.data
            setPermissionLevel(data.permission)
        } catch (error) {
            toast.error(selectedLanguage === "EN" ?'Error something happend':`Erreur, quelque chose s'est produit`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
        }
    }

    useEffect(() => {
        setPermissionExist(true)
    }, [permissionLevel])

    const [optionsVisible, setOptionsVisible] = useState(false);
    const [itemOptionsVisible, setItemOptionsVisible] = useState(false);

    const [optionsPosition, setOptionsPosition] = useState({ top: 0, left: 0 });
    const [itemOptionsPosition, setItemOptionsPosition] = useState({ top: 0, left: 0 });

    const [activeInventoryId, setActiveInventoryId] = useState<string>('');
    const [activeItemId, setActiveItemId] = useState<string>('');
    const optionsRef = useRef<HTMLDivElement>(null);
    const [displayAddItem, setDisplayAddItem] = useState(false)
    const [selectedInventoryId, setSelectedInventoryId] = useState('')
    const navigate = useNavigate();

    const userLightModeOrDarkMode = () => {
        let mediaQueryObj = window.matchMedia('(prefers-color-scheme: dark)');
        if (mediaQueryObj.matches)
            return 'dark';
        else
            return 'light';// true or false depending on light or dark mode

    }

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (optionsRef.current && !optionsRef.current.contains(event.target as Node)) {
                setOptionsVisible(false);
                setItemOptionsVisible(false)
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const missingFieldError = (value: any) => {
        if (value === "" || value <= 0 || value === null || value === "null") {
            toast.error(selectedLanguage === "EN" ?  'Missing Field(s)' : 'Champ(s) manquant(s)', {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })

            return true

        }

        return false
    }

    const AddItemFunctionality = async () => {
        // personal info

        //@ts-ignore
        let itemName: string = document.getElementById('ItemNameInput').value;
        if (missingFieldError(itemName)) {
            toast.error(selectedLanguage === "EN" ? 'Item name is invalid !' : `Nom de l'élément invalide !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
            return;
        }

        //@ts-ignore
        let itemType: string = document.getElementById('ItemSelectInput').value
        if (missingFieldError(itemType)) {
            toast.error(selectedLanguage === "EN" ? 'Item type is invalid !' : `Type d'élément invalide !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
            return;
        }

        //@ts-ignore
        let itemPrice: number = document.getElementById('PriceInput').value

        //@ts-ignore
        let itemDescription: string = document.getElementById('descriptionInput').value

        //@ts-ignore
        let itemHandlingInstructions: string = document.getElementById('handlingInstructionsInput').value

        const itemForm = {
            name: itemName,
            type: itemType,
            price: itemPrice,
            description: itemDescription,
            handlingInstructions: itemHandlingInstructions
        }


        try {
            // Make a POST request using Axios
            const response = await axios.post(
                process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${selectedShipment.shipmentId}/observers/inventories/${selectedInventoryId}/items`,
                itemForm,
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            )


            setItemPopUpIsDisplayed(false);
            toast.success(selectedLanguage === "EN" ? 'Item sent successfully !' : `Élément envoyé avec succès !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
            getItems(selectedInventoryId)

        } catch (error) {
            toast.error(selectedLanguage === "EN" ?'Error something happened' : `Erreur, quelque chose s'est produit.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
        }
    }

    const deleteItem = async (inventoryId: string, itemId: string) => {
        try {
            await axios.get(process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${selectedShipment.shipmentId}/observers/inventories/${inventoryId}/items/${itemId}`)
                .then((r: any) => {
                    setSelectedItemObject(r.data)
                    setItemOperation('delete')
                    setItemPopUpIsDisplayed(true);
                })
        } catch (error) {
            toast.error(selectedLanguage === "EN" ?'Could not delete the Item' : `Impossible de supprimer l'article.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
        }
    }

    const editItem = async (inventoryId: string, itemId: string) => {
        try {
            await axios.get(process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${selectedShipment.shipmentId}/observers/inventories/${inventoryId}/items/${itemId}`)
                .then((r: any) => {
                    setSelectedItemObject(r.data)
                    setItemOperation('edit')
                    setItemPopUpIsDisplayed(true);
                })
        } catch (error) {
            toast.error(selectedLanguage === "EN" ?'Could not edit item' : `Impossible de modifier l'article`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
        }
    }

    const handleOptionsClick = (inventoryId: string, event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        event.stopPropagation(); // Prevent the click from closing the options immediately
        setActiveInventoryId(inventoryId);
        const dotsPosition = event.currentTarget.getBoundingClientRect();
        setOptionsPosition({
            top: dotsPosition.bottom + window.scrollY,
            left: dotsPosition.left - 50 + window.scrollX
        });
        setOptionsVisible(v => !v);
    };

    const handleOptionsItemClick = (itemId: string, event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        event.stopPropagation(); // Prevent the click from closing the options immediately
        setActiveItemId(itemId);
        const dotsPosition = event.currentTarget.getBoundingClientRect();
        setItemOptionsPosition({
            top: dotsPosition.bottom + window.scrollY,
            left: dotsPosition.left - 50 + window.scrollX
        });
        setItemOptionsVisible(v => !v);
    };


    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (optionsRef.current && !optionsRef.current.contains(event.target as Node)) {
                setOptionsVisible(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    //handle inventory update 
    const editInventory = async (inventoryId: string) => {
        try {
            // Make a Get Inventory by id request using Axios
            const response = await axios.get(
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + selectedShipment.shipmentId + '/observers/inventories/' + inventoryId
            )
            setSelectedInventoryObject(response.data)
            setOperation('edit')
            setInventoryPopUpIsDisplayed(true);

        } catch (error) {
            toast.error(selectedLanguage === "EN" ?'Could not get the Invetory details' : `Impossible d'obtenir les détails de l'inventaire.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
        }

    }

    //handle delete inventory 
    const deleteInventory = async (inventoryId: string) => {
        try {
            // Make a Get Inventory by id request using Axios
            const response = await axios.get(
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + selectedShipment.shipmentId + '/observers/inventories/' + inventoryId
            )
            setSelectedInventoryObject(response.data)
            setOperation('delete')
            setInventoryPopUpIsDisplayed(true);
        } catch (error) {
            toast.error(selectedLanguage === "EN" ?'Could not get the Invetory details' : `Impossible d'obtenir les détails de l'inventaire.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
        }
    }

    //handle get inventory details 
    const getInventory = async (inventoryId: string) => {
        try {
            // Make a Get Inventory by id request using Axios
            const response = await axios.get(
                `${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${selectedShipment.shipmentId}/observers/inventories/${inventoryId}`,
                { params: { observerCode } }
            );
            setSelectedInventoryObject(response.data);
            setOperation('details');
            setInventoryPopUpIsDisplayed(true);
        } catch (error: any) {
            toast.error(selectedLanguage === "EN" ?'Could not get the Invetory details' : `Impossible d'obtenir les détails de l'inventaire.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            });
        }
    };


    //handle add inventory 
    const addInventory = async () => {
        setOperation('add')
        setInventoryPopUpIsDisplayed(true);
    }

    //handle get All inventories 


    const getInventories = async (shipmentId: any, observerCode: any) => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${shipmentId}/observers/inventories`,
                { params: { observerCode } }
            );
            setInventories(response.data);
        } catch (error: any) {
            const errorMessage = error.response?.data.message || 'An error occurred';
            toast.error(selectedLanguage =="EN" ? 'An error occurred' : `Une Erreur c'est produite`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            });
        }
    }
    const checkIfAnyActionsCanBePerformedOnShipment = (): boolean => {
        return selectedShipment.status !== 'CANCELED' && selectedShipment.status !== 'TRANSIT' && selectedShipment.status !== 'DELIVERED';
    }


    const returnProperIventoryPopUpTitle = (operation: any) => {
        if (operation == "add") {
            return (<span
                className='text-companyYellow'>{selectedLanguage === "EN" ? "New Inventory" : "Nouvel inventaire"}</span>)
        } else if (operation == "edit") {
            return (<span
                className='text-companyYellow'>{selectedLanguage === "EN" ? "Edit Inventory" : "Modifier l'inventaire"}</span>)
        } else if (operation == "details") {
            return (<span className='text-companyYellow'>{selectedLanguage === "EN" ? "Details" : "Détails"}</span>)
        } else if (operation == "delete") {
            return (<span
                className='text-companyYellow'>{selectedLanguage === "EN" ? "Delete Inventory" : "Supprimer l'inventaire"}</span>)
        }
    }

    const returnProperInventoryPopUpButton = (operation: any) => {
        if (operation == "add") {
            return (
                <button style={{fontFamily: 'Bebas Neue, cursive'}} onClick={() => {
                    AddInventoryFunctionality()
                }} className='px-2.5 py-1 text-sm bg-companyYellow text-white rounded-sm'>
                    {selectedLanguage === "EN" ? "Add New Inventory" : "Ajouter un nouvel inventaire"}
                </button>
            )
        } else if (operation == "edit") {
            return (
                <button style={{fontFamily: 'Bebas Neue, cursive'}} onClick={() => {
                    EditInventoryFunctionality()
                }} id='saveInventoryButton' className='px-2.5 py-1 text-sm bg-companyYellow text-white rounded-sm'>
                    {selectedLanguage === "EN" ? "Save Inventory" : "Sauvegarder l'inventaire"}
                </button>
            )
        } else if (operation == "delete") {
            return (
                <button style={{fontFamily: 'Bebas Neue, cursive'}} onClick={() => {
                    deleteInventoryFunctionality()
                }} id='deleteInventoryButton' className='px-2.5 py-1 text-sm bg-companyYellow text-white rounded-sm'>
                    {selectedLanguage === "EN" ? "Confirm Delete" : "Confirmer la suppression"}
                </button>
            )
        }
    }

    const returnProperItemPopUpTitle = (operation: any) => {
        if (operation == "add") {
            return (<span className='text-companyYellow'>{selectedLanguage === "EN" ? "Add" : "Ajouter"}</span>)
        } else if (operation == "edit") {
            return (<span className='text-companyYellow'>{selectedLanguage === "EN" ? "Edit" : "Modifier"}</span>)
        } else if (operation == "details") {
            return (<span className='text-companyYellow'>{selectedLanguage === "EN" ? "Details" : "Details"}</span>)
        } else if (operation == "delete") {
            return (<span className='text-companyYellow'>{selectedLanguage === "EN" ? "Delete" : "Supprimer"}</span>)
        }
    }

    const returnInventoryButton = () => {

        if (permissionLevel === "READ") {
            return (
                <div id='detailsBtnOption' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { getInventory(activeInventoryId) }}>{selectedLanguage === "EN" ? "Details" : "Détails"}</div>
            )
        }

        else if (permissionLevel === "EDIT") {
            return (
                <>
                    <div id='editBtnOption' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { editInventory(activeInventoryId) }}>{selectedLanguage === "EN" ? "Edit" : "Modifier"}</div>
                    <div id='detailsBtnOption' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { getInventory(activeInventoryId) }}>{selectedLanguage === "EN" ? "Details" : "Détails"}</div>
                </>
            )
        }
        else if (permissionLevel === "FULL") {
            return (
                <>
                    <div id='deleteBtnOption' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { deleteInventory(activeInventoryId) }}>{selectedLanguage === "EN" ? "Delete" : "Effacer"}</div>
                    <div id='editBtnOption' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { editInventory(activeInventoryId) }}>{selectedLanguage === "EN" ? "Edit" : "Modifier"}</div>
                    <div id='detailsBtnOption' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { getInventory(activeInventoryId) }}>{selectedLanguage === "EN" ? "Details" : "Détails"}</div>
                </>
            )
        }
    }

    const returnItemButton = () => {


        if (permissionLevel === "READ") {
            return (
                <div id='detailsItemBtn' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { getItem(activeInventoryId, activeItemId) }}>{selectedLanguage === "EN" ? "Details" : "Détails"}</div>
            )
        }

        else if (permissionLevel === "EDIT") {
            return (
                <>
                    <div id='editItemBtn' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { editItem(activeInventoryId, activeItemId) }}>{selectedLanguage === "EN" ? "Edit" : "Modifier"}</div>
                    <div id='detailsItemBtn' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { getItem(activeInventoryId, activeItemId) }}>{selectedLanguage === "EN" ? "Details" : "Détails"}</div>
                </>
            )
        }
        else if (permissionLevel === "FULL") {
            return (
                <>
                    <div id='deleteItemBtn' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { deleteItem(activeInventoryId, activeItemId) }}>{selectedLanguage === "EN" ? "Delete" : "Effacer"}</div>
                    <div id='editItemBtn' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { editItem(activeInventoryId, activeItemId) }}>{selectedLanguage === "EN" ? "Edit" : "Modifier"}</div>
                    <div id='detailsItemBtn' className="p-1 cursor-pointer hover:bg-gray-100" onClick={() => { getItem(activeInventoryId, activeItemId) }}>{selectedLanguage === "EN" ? "Details" : "Détails"}</div>
                </>
            )
        }
    }

    const returnProperItemPopUpButton = (operation: any) => {
        if (operation == "add") {
            return (
                <button style={{fontFamily: 'Bebas Neue, cursive'}} onClick={() => {
                    AddItemFunctionality()
                }} className='px-2.5 py-1 text-sm bg-companyYellow text-white rounded-sm'>
                    {selectedLanguage === "EN" ? "Add" : "Ajouter"}
                </button>
            )
        } else if (operation == "edit") {
            return (
                <button style={{fontFamily: 'Bebas Neue, cursive'}} onClick={() => {
                    EditItemFunctionality()
                }} id='saveItemBtn' className='px-2.5 py-1 text-sm bg-companyYellow text-white rounded-sm'>
                    {selectedLanguage === "EN" ? "Save" : "Sauvgarder"}
                </button>
            )
        } else if (operation == "delete") {
            return (
                <button style={{fontFamily: 'Bebas Neue, cursive'}} onClick={() => {
                    DeleteItemFunctionality()
                }} id='confirmDeleteItemBtn' className='px-2.5 py-1 text-sm bg-companyYellow text-white rounded-sm'>
                    {selectedLanguage === "EN" ? "Confirm" : "Confirmer"}
                </button>
            )
        }
    }

    const EditItemFunctionality = async () => {
        //@ts-ignore
        let itemName: string = document.getElementById('ItemNameInput').value;
        if (missingFieldError(itemName)) {
            toast.error(selectedLanguage === "EN" ?'Item name is invalid !' : `Le nom de l'article n'est pas valide !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
            return;
        }

        //@ts-ignore
        let itemType: string = document.getElementById('ItemSelectInput').value
        if (missingFieldError(itemType)) {
            toast.error(selectedLanguage === "EN" ?'Item type is invalid !' : `Le type d'article n'est pas valide !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
            return;
        }

        //@ts-ignore
        let itemPrice: number = document.getElementById('PriceInput').value


        //@ts-ignore
        let itemDescription: string = document.getElementById('descriptionInput').value

        //@ts-ignore
        let itemHandlingInstructions: string = document.getElementById('handlingInstructionsInput').value

        const itemForm = {
            name: itemName,
            type: itemType,
            price: itemPrice,
            description: itemDescription,
            handlingInstructions: itemHandlingInstructions
        }

        try {
            // Make a PUT request using Axios
            if (selectedItemObject && selectedItemObject['itemId']) {
                const response = await axios.put(
                    process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${selectedShipment.shipmentId}/observers/inventories/${selectedInventoryId}/items/${selectedItemObject['itemId']}`,
                    itemForm,
                    {
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    }
                )

                setItemPopUpIsDisplayed(false);
                toast.success(selectedLanguage === "EN" ?'Item edited successfully !' : `Article modifié avec succès !`, {
                    position: 'top-right',
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: userLightModeOrDarkMode(),
                })
                getItems(selectedInventoryId)
            } else {
                throw new Error('Selected item object or itemId is null');
            }
        } catch (error) {
            console.error('Error:', error)
            toast.error(selectedLanguage === "EN" ?'Error something happened' : ` Une erreur s'est produite.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
        }
    }

    const DeleteItemFunctionality = async () => {
        try {

            const response = await axios.delete(
                //@ts-ignore
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + selectedShipment.shipmentId + '/observers/inventories/' + selectedInventoryId + '/items/' + selectedItemObject['itemId']
            )

            getItems(selectedInventoryId)
            setItemPopUpIsDisplayed(false)

            // if its successfull then successfull notification + get all items + close pop up
            toast.success(selectedLanguage === "EN" ?'Item deleted successfully!' : `Article supprimé avec succès !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })
        }
            // else error notification
        catch (error) {
            toast.error(selectedLanguage === "EN" ?'Could not delete item' : `Impossible de supprimer l'article.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })
        }
    }

    //--------------------------------------- Delete , Add & Edit Functionalities with axios HERE !!!! -----------------------------------------------

    const deleteInventoryFunctionality = async () => {
        // make your delete with axios and pass the inventoryId
        try {

            const response = await axios.delete(
                `${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${selectedShipment.shipmentId}/observers/inventories/${activeInventoryId}`,
                { params: { observerCode } }
            );

            // if its successfull then successfull notification + get all inventories + close pop up
            toast.success(selectedLanguage === "EN" ? 'Inventory deleted successfully!' : `L'inventaire a été supprimé avec succès !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })

            getInventories(selectedShipment.shipmentId, observerCode)

            setInventoryPopUpIsDisplayed(false)

        }
        // else error notification
        catch (error: any) {
            toast.error(selectedLanguage === "EN" ?'Could not delete inventory' : `Impossible de supprimer l'inventaire.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })
        }
    }

    const EditInventoryFunctionality = async () => {
        //@ts-ignore
        let inventoryNamePopUp = document.getElementById('inventoryNamePopUp').value;

        //@ts-ignore
        let inventoryStatusPopUp = document.getElementById('inventoryStatusPopUp').value;

        //@ts-ignore
        let inventorydescriptionPopUp = document.getElementById('inventoryDiscriptionPopUp').value;

        // create a inventory object
        let inventoryRequest = {
            name: inventoryNamePopUp,
            description: inventorydescriptionPopUp,
            inventoryStatus: inventoryStatusPopUp
        }


        try {

            const response = await axios.put(
                `${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${selectedShipment.shipmentId}/observers/inventories/${activeInventoryId}`,
                inventoryRequest,
                { params: { observerCode } }
            );

            // if its successfull then successfull notification + get all inventories + close pop up
            toast.success( selectedLanguage === "EN" ?'Inventory Edited successfully!' : `Inventaire modifié avec succès !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })

            getInventories(selectedShipment.shipmentId, observerCode)

            setInventoryPopUpIsDisplayed(false)

        }
        // else error notification
        catch (error: any) {

            toast.error(selectedLanguage === "EN" ? 'Error Saving Inventory Details' : `Erreur lors de l'enregistrement des détails de l'inventaire.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
        }
    }

    const AddInventoryFunctionality = async () => {
        // get all the values from the fields

        //@ts-ignore
        let inventoryNamePopUp = document.getElementById('inventoryNamePopUp').value;;

        //@ts-ignore
        let inventoryStatusPopUp = document.getElementById('inventoryStatusPopUp').value;

        //@ts-ignore
        let inventoryDescriptionPopUp = document.getElementById('inventoryDiscriptionPopUp').value;

        // create a inventory object

        let inventoryRequest = {
            name: inventoryNamePopUp,
            description: inventoryDescriptionPopUp,
            inventoryStatus: inventoryStatusPopUp
        }

        // do your post with axios and pass the inventory object
        try {

            const response = await axios.post(
                `${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${selectedShipment.shipmentId}/observers/inventories`,
                inventoryRequest,
                { params: { observerCode } }
            );

            // if its successfull then successfull notification + get all inventories + close pop up
            toast.success(selectedLanguage === "EN" ? 'Inventory added successfully!' : `Inventaire ajouté avec succès !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })

            getInventories(selectedShipment.shipmentId, observerCode)

            setInventoryPopUpIsDisplayed(false)

        }
        // else error notification
        catch (error: any) {
            toast.error(selectedLanguage === "EN" ? 'Could not add Invetory' : `Impossible d'ajouter l'inventaire.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })
        }

    }

    //--------------------------------------- Inventory Accordion implementation HERE !!!! -----------------------------------------------

    const [InventoryAccordionSelected, setInventoryAccordionSelected] = useState(null)

    const toggleOpenAccordion = (activeInventoryId: any) => {
        setItems([])
        setOptionsVisible(false)
        getItems(activeInventoryId)
        // setInventoryAccordionSelected(activeInventoryId)
        // setSelectedInventoryId(activeInventoryId)
    }

    const toggleCloseAccordion = (activeInventoryId: any) => {
        setInventoryAccordionSelected(null)
        setOptionsVisible(false)
    }

    //get all items in an inventory
    const getItems = async (inventoryId: any) => {
        try {
            const response = await axios.get(
                `${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${selectedShipment.shipmentId}/observers/inventories/${inventoryId}/items`,
                { params: { observerCode } }
            );
            setItems(response.data);
            setInventoryAccordionSelected(inventoryId)
            setSelectedInventoryId(inventoryId)
        }
        catch (error) {
            toast.error(selectedLanguage === "EN" ? 'Could not get items' : `Impossible d'obtenir les articles.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })
        }
    }

    const getItem = async (inventoryId: string, itemId: string) => {
        try {
            const response = await axios.get(
                `${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${selectedShipment.shipmentId}/observers/inventories/${inventoryId}/items/${itemId}`,
                { params: { observerCode } }
            );
            setSelectedItemObject(response.data);
            setItemOperation('details');
            setItemPopUpIsDisplayed(true);
        }
        catch (error) {
            toast.error(selectedLanguage === "EN" ? 'Could not get item' : `Impossible d'obtenir l'article.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })
        }
    }

    const addItem = async () => {
        setItemOperation('add')
        setItemPopUpIsDisplayed(true)
    }
    const [generateReport, setGenerateReport] = useState(false);
    const [inventoriesWithItem, setInventoriesWithItem] = useState([]);

    const getInventoriesItem = async (inventoriesDetailsOnly: any, shipment: any) => {

        const fetchOperations = inventoriesDetailsOnly.map(async (inventory: any) => {
            const response = await axios.get(
                `${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${shipment.shipmentId}/observers/inventories/${inventory.inventoryId}/items`,
                { params: { observerCode } }
            );
            const items = response.data; 
    
            return {
                ...inventory, // Spread the existing inventory properties
                items: items 
            };
        });
    
        // Wait for all fetch operations to complete
        const inventoriesWithItems = await Promise.all(fetchOperations);
    
        //@ts-ignore
        setInventoriesWithItem(inventoriesWithItems);
        setGenerateReport(true);

    };

    const handleGenerateReport = () => {
        getInventoriesItem(inventories, selectedShipment)
    };

    // --------------------------------------- Localization --------------------------------
    const [selectedLanguage, setSelectedLanguage] = useState('');

    const handleSelectChange = (event: any) => {
        const newValue = event.target.value;

        // Update state with the selected value
        setSelectedLanguage(newValue);

        Cookies.set('MovingExpress_SelectedLanguage', newValue, {expires: 7, path: '/'});

        window.location.reload();

    };

    useEffect(() => {

        const language = Cookies.get("MovingExpress_SelectedLanguage")

        if (language) {
            // set selected Language
            setSelectedLanguage(language)
        } else {
            // set a cookie and usestate to english as default
            Cookies.set('MovingExpress_SelectedLanguage', 'EN', {expires: 7, path: '/'});
            setSelectedLanguage("EN")
        }

    }, [])
    

    return (
        <div>
            {/* Nav/Top section */}
            <div style={{alignItems: "center"}} className="flex justify-between px-4 md:px-10 py-5 gap-3 bg-[#2D2D2D]">
                <div
                    style={{ fontFamily: 'Bebas Neue, cursive' }}
                    className="text-white text-lg"
                >
                    <img src={logo} className="w-20 2xl:w-20" />
                </div>

                <div style={{alignItems: "center"}} className='flex flex-row gap-3'>
                    <div className='countryDiv text-white'>
                        <select className='h-7 bg-transparent' id="selectLanguage"
                                onChange={(e) => handleSelectChange(e)}>
                            <option className='text-black' selected={selectedLanguage == "EN"} value="EN">🇬🇧 EN</option>
                            <option className='text-black' selected={selectedLanguage == "FR"} value="FR">🇫🇷 FR</option>
                        </select>
                    </div>
                    <a
                        style={{ fontFamily: 'Bebas Neue, cursive' }}
                        id="signinsignuplandingpage"
                        className="border-[3px] border-companyYellow px-5 py-2 rounded-md text-white text-center"
                        href={url}
                    >
                       {selectedLanguage === "EN" ? `Log out`: `Déconnexion`} 
                    </a>
                </div>

            </div>

            <ScrollToTopBtn />

            {/* Inventory pop up */}
            <Popup className='my-popup' open={inventoryPopUpIsDisplayed} onClose={() => {
                setInventoryPopUpIsDisplayed(false)
            }} position="right top">


                <div className=' w-[100%] h-[100%] rounded-lg px-5 py-5 shadow-2xl '>

                    <div style={{alignItems: "center"}} className='flex justify-between '>
                        <div style={{fontFamily: 'Bebas Neue, cursive'}}
                             className='text-xl'>{selectedLanguage === "EN" ? "Inventory |" : "Inventaire |"}

                            {returnProperIventoryPopUpTitle(operation)}

                        </div>

                        <button className="text-white bg-companyYellow w-[30px] h-[30px] text-sm rounded pb-1"
                                onClick={() => setInventoryPopUpIsDisplayed(false)}>
                            X
                        </button>
                    </div>

                    <div className='mt-5 border-b border-t'>
                        {/* form componment eiter (empty + no inventory id) or (full + editable ) or (full + read only)*/}
                        <InventoryPopUpForm inventoryObject={selectedInventoryObject}
                                            operation={operation}></InventoryPopUpForm>
                    </div>

                    <div className='flex justify-end pt-3'>
                        {returnProperInventoryPopUpButton(operation)}
                    </div>

                </div>

            </Popup>

            {/* Item pop up */}
            <Popup className='my-popup' open={itemPopUpIsDisplayed} onClose={() => {
                setItemPopUpIsDisplayed(false)
            }} position="right top">
                <div className='w-[100%] h-[100%] rounded-lg px-5 py-5 shadow-2xl'>

                    <div style={{alignItems: "center"}} className='flex justify-between mb-3'>
                        <div className="text-xl"
                             style={{fontFamily: 'Bebas Neue, cursive'}}>{selectedLanguage === "EN" ? "Item |" : "Article |"}
                            {returnProperItemPopUpTitle(itemOperation)}
                        </div>
                        <button className="text-white bg-companyYellow w-[30px] h-[30px] text-sm rounded pb-1"
                                onClick={() => setItemPopUpIsDisplayed(false)}>
                            X
                        </button>
                    </div>

                    <div className="border-b border-t">
                        <ItemPopUpForm itemObject={selectedItemObject} operation={itemOperation}></ItemPopUpForm>
                    </div>

                    <div className='flex justify-end mt-3'>
                        {returnProperItemPopUpButton(itemOperation)}
                    </div>

                </div>
            </Popup>

            {/* Shipment Details Section */}
            <div className='bg-white px-[5%] pt-20 mb-5'>

                {/* Shipment Title Section + print + cancel buttons */}
                <div className='flex flex-row items-center justify-between mb-5 flex-wrap'>
                    {selectedLanguage === "EN" ?
                        <div style={{fontFamily: 'Bebas Neue, cursive'}} className='text-2xl'>Shipment <span
                            className="text-companyYellow">Details</span></div>
                        :
                        <div style={{fontFamily: 'Bebas Neue, cursive'}} className='text-2xl'>détails de <span
                            className="text-companyYellow">l'Expédition</span></div>
                    }

                    <div className='flex-col flex md:flex-row  gap-3'>
                    {checkIfAnyActionsCanBePerformedOnShipment() ? ( permissionLevel === "FULL") &&
                        <div>
                            <button id='GenerateShipmentReportBtn' style={{ fontFamily: 'Bebas Neue, cursive' }} className='px-2.5 py-1 bg-[#949494] text-white rounded-sm text-sm shadow-md' 
                            onClick={() => { setDisplayDetail(false); handleGenerateReport();}}> 
                            {selectedLanguage === 'EN' ? 'Generate Report' : 'Générer rapport'}</button>
                            
                            {generateReport && (
                                <div className="hidden">
                                    <BlobProvider document={<ShipmentReportTemplate shipment={selectedShipment} inventories={inventoriesWithItem}/>}>
                                        {({ blob, url, loading, error }) => {
                                            if (loading) {
                                                return  toast.success(selectedLanguage === 'EN' ? 'Client Report generated successfully!' : 'Rapport client généré avec succès !', {
                                                    position: 'top-right',
                                                    autoClose: 5000,
                                                    hideProgressBar: false,
                                                    closeOnClick: true,
                                                    pauseOnHover: true,
                                                    draggable: true,
                                                    progress: undefined,
                                                    theme: 'light',
                                                });
                                            }

                                            if (blob) {
                                                const pdfUrl = URL.createObjectURL(blob);
                                                const link = document.createElement('a');
                                                link.href = pdfUrl;
                                                link.setAttribute('download', 'ClientReport.pdf');
                                                document.body.appendChild(link);
                                                link.click();
                                                document.body.removeChild(link);
                                                setGenerateReport(false); 
                                            }
                                            return null;
                                        }}
                                    </BlobProvider>
                                </div>
                            )}
                        </div> : <></>
                        }
                    </div>

                </div>

                <hr className="border-1 border-grey mb-6" />

                {/* ShipmentID + ShipmentName + ShipmentFullName + Email*/}
                <div className='flex gap-4 flex-wrap my-2'>
                    <div className="w-[100%] md:w-[280px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="w-[100%] border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="DetailShipmentId"
                            name="DetailShipmentId"
                            value={selectedShipment.shipmentId || ''}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? " Shipment ID" : "ID d'expédition"}
                        </label>
                    </div>
                    <div className="w-[100%] md:w-[200px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="ShipmentName"
                            name="ShipmentName"
                            required

                            value={selectedShipment.shipmentName || ''}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "Shipment Name" : "Nom de l'expédition"}
                        </label>
                    </div>
                    <div className="w-[100%] md:w-[280px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="FullName"
                            name="FullName"
                            required
                            value={`${selectedShipment.firstName || ''} ${selectedShipment.lastName || ''}`}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "Full Name" : "Nom complet"}
                        </label>
                    </div>
                    <div className="w-[100%] md:w-[280px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="Email"
                            name="Email"
                            required

                            value={selectedShipment.email || ''}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "E-Mail" : "Courriel"}
                        </label>
                    </div>
                </div>

                {/* Phone Number + Expected Moving Date */}
                <div className='flex flex-row gap-4 flex-wrap my-2'>
                    <div className="w-[100%] md:w-[280px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="PhoneNumber"
                            name="PhoneNumber"
                            required

                            value={selectedShipment.phoneNumber || ''}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "Phone Number" : "Téléphone"}
                        </label>
                    </div>
                    <div className="w-[100%] md:w-[280px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="ExpectedMovingDate"
                            name="ExpectedMovingDate"
                            required

                            value={selectedShipment.expectedMovingDate || ''}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "Expected Moving Date" : "Date de déménagement"}
                        </label>
                    </div>
                </div>

                {/* Pickup Address + Destination Address*/}
                <div className='flex flex-row gap-4 flex-wrap my-2'>
                    <div className="w-[100%] md:w-[500px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="PickupAddress"
                            name="PickupAddress"
                            required

                            value={`${selectedShipment.pickupAddress.streetAddress || ''}, ${selectedShipment.pickupAddress.city || ''}, ${selectedShipment.pickupAddress.postalCode || ''}, ${selectedShipment.pickupAddress.country || ''}`}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                           {selectedLanguage === "EN" ? "Pickup Address" : "Adresse de ramassage"}
                        </label>
                    </div>

                    <div className="w-[100%] md:w-[500px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="DestinationAddress"
                            name="DestinationAddress"
                            required

                            value={`${selectedShipment.destinationAddress.streetAddress || ''}, ${selectedShipment.destinationAddress.city || ''}, ${selectedShipment.destinationAddress.postalCode || ''}, ${selectedShipment.destinationAddress.country || ''}`}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                           {selectedLanguage === "EN" ? "Destination Address" : "Adresse de destination"}
                        </label>
                    </div>
                </div>

                {/* Shipment Status + Approximate Weight*/}
                <div className='flex flex-row gap-4 flex-wrap my-2'>
                    <div className="w-[100%] md:w-[280px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="ShipmentStatus"
                            name="ShipmentStatus"
                            required
                            value={selectedShipment.status || ''}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                           {selectedLanguage === "EN" ? "Shipment Status" : "État de l'expédition"}
                        </label>
                    </div>

                    <div className="w-[100%] md:w-[280px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="ApproximateWeight"
                            name="ApproximateWeight"
                            required
                            value={selectedShipment.approximateWeight}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "Approximate Weight" : "Poids approximatif"}
                        </label>
                    </div>
                    <div className="w-[100%] md:w-[280px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="FinalWeight"
                            name="FinalWeight"
                            required
                            value={selectedShipment.weight}
                            readOnly
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "Final Weight" : "Poids Final"}
                        </label>
                    </div>
                </div>

            </div>

            {/* Inventory Section */}
            <div className='px-[5%] pb-10'>
                <div className='flex flex-col '>

                    {/* Inventory title + Add New inventory btn */}
                    <div className='flex flex-row items-center justify-between mb-5'>

                        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-2xl'>{selectedLanguage === "EN" ? "Inventories" : "Inventaires"}</div>

                        <div className='flex flex-row'>
                            <div>
                                {checkIfAnyActionsCanBePerformedOnShipment() ? (permissionLevel === "EDIT" || permissionLevel === "FULL") && <button id='AddInventoryBtn' className='px-3 py-[2px] bg-white border-[3px] border-companyYellow border-dashed text-companyYellow rounded-sm txt-sm' style={{ fontFamily: 'Bebas Neue, cursive' }} onClick={() => { addInventory() }}> {selectedLanguage === "EN" ? "Add Inventory" : "Ajouter inventaire"} <span className='pl-2'>+</span></button> : ''}
                            </div>
                        </div>

                    </div>

                    {/* New Inventory Accordion (Youssef CH) */}
                    {inventories.length === 0 ?
                        <div className='text-center opacity-25 font-light'>{selectedLanguage === "EN" ? "[ No Inventories ]" : "[ Aucun inventaire ]"}</div>
                        :
                        <div className='Accordion'>
                            {
                                inventories.map((inventory: any, i) => (

                                    <div className='item mb-1 py-1'>

                                        <div style={{ alignItems: "center" }} className='title flex justify-between border-b-[1px] border-gray-300 pb-2'>

                                            <h4 style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-gray-600 text-lg'>{inventory.name} <span className='text-sm text-companyYellow pl-1'>
                                                
                                                {selectedLanguage === "EN"
                                                    ? `[ ${inventory.inventoryStatus} ]`

                                                    : inventory.inventoryStatus === "CREATED"
                                                        ? "[ CRÉÉ ]"
                                                        : inventory.inventoryStatus === "PACKED"
                                                            ? "[ EMBALLÉ ]"
                                                            : inventory.inventoryStatus === "IN_PROGRESS"
                                                                ? "[ EN COURS ]"
                                                                : "[ LOADING... ]"

                                                }</span>
                                            </h4>

                                            <div className='flex flex-row gap-3'>
                                                <div>
                                                    {checkIfAnyActionsCanBePerformedOnShipment() ? (permissionLevel === "EDIT" || permissionLevel === "FULL") &&

                                                        <button id='AddItem' className='px-3 py-[2px] bg-companyYellow text-white rounded-sm text-[15px] shadow-md opacity-40 hover:opacity-100 duration-200 ease-in-out' style={{ fontFamily: 'Bebas Neue, cursive' }}
                                                            onClick={() => { addItem(); setSelectedInventoryId(inventory.inventoryId) }}> {selectedLanguage === "EN" ? "Add Item" : "Ajouter article"}<span className='pl-2'>+</span>
                                                        </button>
                                                        : ''}
                                                </div>

                                                <div id={`${inventory.inventoryId}-TripleBtn`} onClick={(e) => handleOptionsClick(inventory.inventoryId, e)} className='text-xl text-gray-400 hover:cursor-pointer'>&#8942;</div>
                                            </div>

                                        </div>

                                        <div className={InventoryAccordionSelected === inventory.inventoryId ? "h-[100%] overflow-visible py-5 px-5 transition-all " : "overflow-hidden max-h-0 transition-all"}>
                                            <div>
                                                <div>
                                                    {items.length === 0 ?
                                                        <div className='opacity-20 text-sm font-light'>
                                                            [ No Items ]
                                                        </div> :
                                                        <div>
                                                            {items.map((item: any) => (
                                                                <div className='item py-1'>

                                                                    <div style={{alignItems: "center"}}
                                                                        className='title py-1 flex justify-between border-[1px] border-gray-300 px-3 shadow-md rounded-md'>
                                                                        <div style={{alignItems: "baseline"}}
                                                                            className='flex flex-row gap-5'>
                                                                            <h4 id={`${item.itemId}-itemType`}
                                                                                style={{fontFamily: 'Bebas Neue, cursive'}}
                                                                                className='bg-companyYellow text-white px-10 rounded text-md italic'>
                                                                                    { selectedLanguage === "EN" ? 

                                                                                        item.type  === "ITEM" ? "Item" : "Box"

                                                                                    : 

                                                                                        item.type  === "ITEM" ? "Article" : "Boite"
                                                                                    
                                                                                    }
                                                                                </h4>
                                                                            <h4 id={`${item.itemId}-itemName`}
                                                                                className=' text-black text-md font-light'>{item.name}</h4>
                                                                        </div>

                                                                        <div className='flex flex-row gap-3'>
                                                                            <div>
                                                                                {checkIfAnyActionsCanBePerformedOnShipment() ?
                                                                                    <div id={`${item.itemId}-TripleBtn`}
                                                                                        onClick={(e) => handleOptionsItemClick(item.itemId, e)}
                                                                                        className='text-xl text-gray-400 hover:cursor-pointer'>&#8942;</div>
                                                                                    : ''}
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            ))}
                                                        </div>
                                                    }
                                                </div>

                                                {checkIfAnyActionsCanBePerformedOnShipment() ? (permissionLevel === "EDIT" || permissionLevel === "FULL") &&

                                                    <button
                                                        className='px-3 py-1 mt-5 bg-white border-companyYellow border-dashed border-[2px] text-companyYellow rounded-sm text-[15px] w-[100%] flex flex-row justify-between opacity-40 hover:opacity-100 duration-200 ease-in-out'
                                                        onClick={() => {
                                                            addItem();
                                                            setSelectedInventoryId(inventory.inventoryId)
                                                        }}>
                                                        <span>{selectedLanguage === "EN" ? "Add New Item" : "Ajouter nouvel article"}</span>
                                                        <span>+</span>
                                                    </button>
                                                : 
                                                    <></>
                                                }
                                            </div>

                                        </div>
                                    </div>
                                ))
                            }

                            {optionsVisible && (
                                <div
                                    ref={optionsRef}
                                    className="absolute bg-white bg-opacity-75 p-2 rounded shadow-lg"
                                    style={{
                                        top: `${optionsPosition.top}px`,
                                        left: `${optionsPosition.left}px`,
                                        zIndex: 1000 // Ensure it's above other elements
                                    }}>

                                    {permissionExist ?
                                        //@ts-ignore
                                        checkIfAnyActionsCanBePerformedOnShipment() && returnInventoryButton() : <></>
                                    }


                                    {activeInventoryId === InventoryAccordionSelected ?

                                        <div id='openBtnOption' className="p-1 cursor-pointer hover:bg-gray-100"
                                        onClick={() => {
                                            toggleCloseAccordion(activeInventoryId)
                                        }}>{selectedLanguage === "EN" ? "Close" : "Fermer"}</div>
                                        :
                                        <div id='openBtnOption' className="p-1 cursor-pointer hover:bg-gray-100"
                                        onClick={() => {
                                            toggleOpenAccordion(activeInventoryId)
                                        }}>{selectedLanguage === "EN" ? "Open" : "Ouvrir"}</div>
                                    }

                                </div>
                            )}
                            {itemOptionsVisible && (
                                <div
                                    ref={optionsRef}
                                    className="absolute bg-white bg-opacity-75 p-2 rounded shadow-lg"
                                    style={{
                                        top: `${itemOptionsPosition.top}px`,
                                        left: `${itemOptionsPosition.left}px`,
                                        zIndex: 1000 // Ensure it's above other elements
                                    }}>
                                    {permissionExist ?
                                        //@ts-ignore
                                        checkIfAnyActionsCanBePerformedOnShipment() && returnItemButton() : <></>
                                    }
                                </div>
                            )}
                        </div>
                    }
                </div>
            </div>

        </div>
    );
}


export default ObserverShipmentDetails;