import React, {useState, useRef} from 'react'
import NormalNavBar from '../Components/NormalNavBar'
import {useAuth} from '../auth/components/AuthService'
import axios from 'axios'
import {useEffect} from 'react'
import {toast} from 'react-toastify'
import {Link, redirect, useNavigate} from 'react-router-dom'
import {useLocation} from 'react-router-dom';
import InventoryPopUpForm from '../Components/InventoryPopUpForm'
import ScrollToTopBtn from '../Components/ScrollToTopBtn'
import ItemPopUpForm from '../Components/ItemPopUpForm'
import Cookies from 'js-cookie'
import Popup from 'reactjs-popup';
import '../App.css'
import {BlobProvider, PDFDownloadLink} from '@react-pdf/renderer';
import ShipmentReportTemplate from '../Components/ShipmentReportTemplate'

axios.defaults.withCredentials = true
function ShipmentDetailsPage() {
    const auth = useAuth()

    const [inventories, setInventories] = useState([]);
    const [items, setItems] = useState([]);

    const [operation, setOperation] = useState('');
    const [itemOperation, setItemOperation] = useState('');

    const [inventoryPopUpIsDisplayed, setInventoryPopUpIsDisplayed] = useState(false);
    const [itemPopUpIsDisplayed, setItemPopUpIsDisplayed] = useState(false);

    const [selectedInventoryObject, setSelectedInventoryObject] = useState(null)
    const [selectedItemObject, setSelectedItemObject] = useState(null)

    let location = useLocation();
    let params = new URLSearchParams(location.search);
    let shipmentId: string = params.get('shipmentId') || '';

    // //-----------------------{Observer Code Part}--------------------------------------

    const [observerCodes, setObserverCodes] = useState([])
    const [editObserverCode, setEditObservercode] = useState("")
    const [selectSectionKey, setSelectSectionKey] = useState(0);

    const generateTrashCanIconSVG = () => {
        return (
            <svg width="20" viewBox="0 0 512 512" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                    d="M96 472C95.952 478.307 98.4074 484.376 102.828 488.875C107.248 493.374 113.272 495.936 119.579 496H392.421C398.728 495.936 404.752 493.374 409.172 488.875C413.593 484.376 416.048 478.307 416 472V152H96V472ZM128 184H384V464H128V184Z"
                    fill="red"/>
                <path
                    d="M168 216H200V416H168V216ZM240 216H272V416H240V216ZM312 216H344V416H312V216ZM328 88V40C328 26.542 318.512 16 306.4 16H205.6C193.488 16 184 26.542 184 40V88H64V120H448V88H328ZM216 48H296V88H216V48Z"
                    fill="red"/>
            </svg>
        )
    }

    const generateEditIconSVG = () => {
        return (
            <svg width="22" viewBox="0 0 85 85" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                    d="M24.7917 60.2544L40.4211 60.2013L74.5344 26.4138C75.8732 25.075 76.6098 23.2971 76.6098 21.4058C76.6098 19.5146 75.8732 17.7367 74.5344 16.3979L68.9173 10.7808C66.2398 8.10334 61.5684 8.1175 58.9121 10.7702L24.7917 44.5648V60.2544ZM63.9094 15.7888L69.5371 21.3952L63.8811 26.9981L58.264 21.3846L63.9094 15.7888ZM31.875 47.5186L53.2313 26.3642L58.8484 31.9813L37.4956 53.1286L31.875 53.1463V47.5186Z"
                    fill="#BBBBBB"/>
                <path
                    d="M17.7083 74.375H67.2917C71.1981 74.375 74.375 71.1981 74.375 67.2917V36.5925L67.2917 43.6758V67.2917H28.8929C28.8008 67.2917 28.7052 67.3271 28.6131 67.3271C28.4963 67.3271 28.3794 67.2952 28.259 67.2917H17.7083V17.7083H41.9581L49.0415 10.625H17.7083C13.8019 10.625 10.625 13.8019 10.625 17.7083V67.2917C10.625 71.1981 13.8019 74.375 17.7083 74.375Z"
                    fill="#BBBBBB"/>
            </svg>

        )
    }

    const generateSaveIconSVG = () => {
        return (
            <svg width="20" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                    d="M44.2428 84.3902L16.0417 56.1891L23.092 49.1389L44.2428 70.2897L89.6366 24.8958L96.6868 31.9461L44.2428 84.3902Z"
                    fill="#4ECB71"/>
            </svg>

        )
    }

    const generateCancelIconSVG = () => {
        return (
            <svg width="14" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                    d="M62.336 57.9141C62.6263 58.2044 62.8566 58.5491 63.0138 58.9284C63.1709 59.3078 63.2518 59.7144 63.2518 60.125C63.2518 60.5356 63.1709 60.9422 63.0138 61.3216C62.8566 61.7009 62.6263 62.0456 62.336 62.3359C62.0456 62.6263 61.7009 62.8566 61.3216 63.0137C60.9422 63.1709 60.5356 63.2517 60.125 63.2517C59.7144 63.2517 59.3078 63.1709 58.9285 63.0137C58.5491 62.8566 58.2044 62.6263 57.9141 62.3359L32 36.418L6.08597 62.3359C5.49959 62.9223 4.70429 63.2517 3.87503 63.2517C3.04577 63.2517 2.25047 62.9223 1.66409 62.3359C1.07771 61.7496 0.748291 60.9543 0.748291 60.125C0.748291 59.2957 1.07771 58.5004 1.66409 57.9141L27.5821 32L1.66409 6.08594C1.07771 5.49956 0.748291 4.70426 0.748291 3.875C0.748291 3.04574 1.07771 2.25044 1.66409 1.66406C2.25047 1.07768 3.04577 0.748261 3.87503 0.74826C4.70429 0.74826 5.49959 1.07768 6.08597 1.66406L32 27.582L57.9141 1.66406C58.5005 1.07768 59.2958 0.74826 60.125 0.74826C60.9543 0.748261 61.7496 1.07768 62.336 1.66406C62.9223 2.25044 63.2518 3.04574 63.2518 3.875C63.2518 4.70426 62.9223 5.49956 62.336 6.08594L36.418 32L62.336 57.9141Z"
                    fill="#BBBBBB"/>
            </svg>
        )
    }

    const refreshIcon = () => {
        return (
            <svg width="15" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                    d="M50 100C36.0417 100 24.2187 95.1562 14.5312 85.4687C4.84375 75.7812 0 63.9583 0 50C0 36.0417 4.84375 24.2187 14.5312 14.5312C24.2187 4.84375 36.0417 0 50 0C57.1875 0 64.0625 1.48333 70.625 4.45C77.1875 7.41667 82.8125 11.6625 87.5 17.1875V0H100V43.75H56.25V31.25H82.5C79.1667 25.4167 74.6104 20.8333 68.8312 17.5C63.0521 14.1667 56.775 12.5 50 12.5C39.5833 12.5 30.7292 16.1458 23.4375 23.4375C16.1458 30.7292 12.5 39.5833 12.5 50C12.5 60.4167 16.1458 69.2708 23.4375 76.5625C30.7292 83.8542 39.5833 87.5 50 87.5C58.0208 87.5 65.2604 85.2083 71.7188 80.625C78.1771 76.0417 82.7083 70 85.3125 62.5H98.4375C95.5208 73.5417 89.5833 82.5521 80.625 89.5313C71.6667 96.5104 61.4583 100 50 100Z"
                    fill="white"/>
            </svg>
        )
    }

    const generateObserverCode = async () => {

        // @ts-ignore
        let observerName = document.getElementById("observerCodeName").value

        // @ts-ignore
        let observerPermission = document.getElementById("observerCodePermission").value

        if (observerName.length > 20) {
            toast.error(selectedLanguage === "EN" ? 'observer name too long' : "Nom de l'observateur trop long.", {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            });
            return;
        }

        if (!observerName) {
            toast.error(selectedLanguage === "EN" ? 'Please enter an observer name.' : "Veuillez entrer un nom d'observateur.", {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            });
            return;
        }

        if (observerPermission == "null") {
            toast.error(selectedLanguage === "EN" ? 'Please pick an permission for your observer code.' : "Veuillez choisir une autorisation pour votre code d'observateur.", {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            });
            return;
        }

        try {
            const observerData = {
                name: observerName,
                permission: observerPermission,
                observerCode: '', // The backend generates this
            };

            const response = await axios.post(
                `${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${shipmentId}/observers`,
                observerData,
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            );

            getAllObserverCodes()

            toast.success(selectedLanguage === "EN" ? `Observer Code: ${response.data.observerCode} generated successfully!` : `Code d'observateur : ${response.data.observerCode} généré avec succès !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            });
            // ... Handle response, update UI accordingly
        } catch (error) {
            toast.error(selectedLanguage === "EN" ? 'Error generating observer code' : "Erreur lors de la génération du code d'observateur.", {
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
    };

    const EditObserverCode = async (observerId: any, observerObject: any) => {

        //no longer in edit mode
        setEditObservercode('')

        // getting the new permission for the edit
        // @ts-ignore
        let newPermission = document.getElementById(`select-${observerId}`).value

        let observerData = {
            name: observerObject.name,
            permission: newPermission,
            observerCode: observerObject.observerCode
        }


        try {

            const response = await axios.put(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${shipmentId}/observers/${observerId}`, observerData)
            getAllObserverCodes()

            toast.success(selectedLanguage === "EN" ? `Observer Code: ${response.data.observerCode} Permission changed to ${response.data.permission} successfully!` : `Code d'observateur : ${response.data.observerCode} Permission modifiée avec succès à ${response.data.permission} !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            });

        } catch (error) {
            toast.error(selectedLanguage === "EN" ? 'Error editing observer code' : "Erreur lors de la modification du code d'observateur.", {
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

    const getAllObserverCodes = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${shipmentId}/observers`)
            let data = response.data
            setObserverCodes(data)
        } catch (error) {
            toast.error( selectedLanguage === "EN" ? 'Error fetching observer codes' : "Erreur lors de la récupération des codes d'observateur.", {
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

    const cancelSave = () => {

        setEditObservercode("")

        setSelectSectionKey((prevKey) => prevKey + 1);

    }

    useEffect(() => {
        getAllObserverCodes()
    }, [])

    const deleteObserverCode = async (observerId: any) => {
        try {
            const response = await axios.delete(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${shipmentId}/observers/${observerId}`)
            getAllObserverCodes()
            toast.success(selectedLanguage === "EN" ? `Observer Code deleted successfully!` : `Code d'observateur supprimé avec succès !`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            });
        } catch (error) {
            toast.error(selectedLanguage === "EN" ? 'Error deleting observer code' : `Erreur lors de la suppression du code d'observateur.`, {
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

    //---------------------------------------{End Here}-------------------------------
    useEffect(() => {
        if (inventoryPopUpIsDisplayed) {
            setOptionsVisible(v => false);
        }
    }, [inventoryPopUpIsDisplayed]);

    useEffect(() => {
        if (itemPopUpIsDisplayed) {
            setItemOptionsVisible(v => false);
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
    const showPDF = () => {
        setGen(true)
    }

    const [gen, setGen] = useState(false);

    const [displayDetail, setDisplayDetail] = useState(false)

    const [optionsVisible, setOptionsVisible] = useState(false);
    const [itemOptionsVisible, setItemOptionsVisible] = useState(false);

    const [optionsPosition, setOptionsPosition] = useState({top: 0, left: 0});
    const [itemOptionsPosition, setItemOptionsPosition] = useState({top: 0, left: 0});

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
        if (value === "" || value <= 0 || value == null || value === "null") {
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

    useEffect(() => {

        if (displayAddItem) {
            window.scrollTo({top: 0, behavior: 'smooth'});
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = 'auto';
        }

    }, [displayAddItem])

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
                process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${shipmentId}/inventories/${selectedInventoryId}/items`,
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
            console.error('Error:', error)
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

    const handleOptionsClick = (inventoryId: string, event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        event.stopPropagation(); // Prevent the click from closing the options immediately
        setActiveInventoryId(inventoryId);
        setActiveInventoryId(inventoryId)
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


    const getShipmentDetails = async (shipmentId: string) => {
        setDisplayDetail(true)

        try {
            const response = await axios.get(process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${shipmentId}`, {});

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

            setSelectedShipment(shipmentDetail)

        } catch (error) {
            toast.error(selectedLanguage === "EN" ?'Error Loading Data' : `Erreur lors du chargement des données.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                //closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: userLightModeOrDarkMode(),
            })
        }
    }

    //handle inventory update 
    const editInventory = async (inventoryId: string) => {
        try {
            // Make a Get Inventory by id request using Axios
            const response = await axios.get(
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + selectedShipment.shipmentId + '/inventories/' + inventoryId
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
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + selectedShipment.shipmentId + '/inventories/' + inventoryId
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
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + selectedShipment.shipmentId + '/inventories/' + inventoryId
            )
            setSelectedInventoryObject(response.data)
            setOperation('details')
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

    //handle cancel shipment
    const cancelShipment = async (shipmentId: string) => {
        var userInput = window.prompt('Are you sure you want to cancel this shipment? Enter Shipment Name: "' + selectedShipment.shipmentName + '" to confirm.')
        if (userInput === selectedShipment.shipmentName) {
            axios.delete(process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + shipmentId)
                .then((response: any) => {
                    toast.success(selectedLanguage === "EN" ?'Shipment Cancelled successfully!' : `L'expédition a été annulée avec succès !`, {
                        position: 'top-right',
                        autoClose: 5000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                        theme: userLightModeOrDarkMode(),
                    })

                    return navigate('/Shipments')
                })
                .catch((error: any) => {
                    toast.error(selectedLanguage === "EN" ? 'Error Cancelling Shipment' : `Erreur lors de l'annulation de l'expédition.`, {
                        position: 'top-right',
                        autoClose: 5000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                        theme: userLightModeOrDarkMode(),
                    })
                });
        }
    }

    //handle add inventory 
    const addInventory = async () => {
        setOperation('add')
        setInventoryPopUpIsDisplayed(true);
    }


    useEffect(() => {
        if (shipmentId) {
            getShipmentDetails(shipmentId);
            getInventories(shipmentId);
        }
    }, [shipmentId]);

    const getInventories = async (shipmentId: any) => {
        await axios.get(process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${shipmentId}/inventories`)
            .then((response: any) => {
                setInventories(response.data)
            })
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
                    process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${shipmentId}/inventories/${selectedInventoryId}/items/${selectedItemObject['itemId']}`,
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
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + selectedShipment.shipmentId + '/inventories/' + selectedInventoryId + '/items/' + selectedItemObject['itemId']
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
                //@ts-ignore
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + selectedShipment.shipmentId + '/inventories/' + selectedInventoryObject['inventoryId']
            )

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

            getInventories(selectedShipment.shipmentId)

            setInventoryPopUpIsDisplayed(false)

        }
            // else error notification
        catch (error) {
            
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



        // do your PUT with axios and pass the inventory object

        try {

            const response = await axios.put(
                //@ts-ignore
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + selectedShipment.shipmentId + '/inventories/' + selectedInventoryObject['inventoryId'],
                inventoryRequest
            )

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

            getInventories(selectedShipment.shipmentId)

            setInventoryPopUpIsDisplayed(false)

        }
            // else error notification
        catch (error) {
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
        let inventoryNamePopUp = document.getElementById('inventoryNamePopUp').value;
        ;

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
                //@ts-ignore
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/' + selectedShipment.shipmentId + '/inventories',
                inventoryRequest
            )

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

            getInventories(selectedShipment.shipmentId)

            setInventoryPopUpIsDisplayed(false)
        }
            // else error notification
        catch (error) {
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
            await axios.get(process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${shipmentId}/inventories/${inventoryId}/items`)
                .then((r: any) => {
                    setItems(r.data)
                    setInventoryAccordionSelected(inventoryId)
                    setSelectedInventoryId(inventoryId)
                })
        } catch (error) {
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
            await axios.get(process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${shipmentId}/inventories/${inventoryId}/items/${itemId}`)
                .then((r: any) => {
                    setSelectedItemObject(r.data)
                    setItemOperation('details')
                    setItemPopUpIsDisplayed(true);
                })
        } catch (error) {
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
        setItemPopUpIsDisplayed(true)
        setItemOperation('add')
    }

    const deleteItem = async (inventoryId: string, itemId: string) => {
        try {
            await axios.get(process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${shipmentId}/inventories/${inventoryId}/items/${itemId}`)
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

    // ----------------------------------------------- localization section -----------------------------------------------

    const [selectedLanguage, setSelectedLanguage] = useState('');

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

    const editItem = async (inventoryId: string, itemId: string) => {
        try {
            await axios.get(process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/shipments/${shipmentId}/inventories/${inventoryId}/items/${itemId}`)
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

    const [generateReport, setGenerateReport] = useState(false);
    const [inventoriesWithItem, setInventoriesWithItem] = useState([]);

    const getInventoriesItem = async (inventoriesDetailsOnly: any, shipment: any) => {

        const fetchOperations = inventoriesDetailsOnly.map(async (inventory: any) => {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${shipment.shipmentId}/inventories/${inventory.inventoryId}/items`);
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


    return (
        <div>

            <NormalNavBar/>

            <ScrollToTopBtn/>

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


            <div className='bg-white px-[5%] pt-20'>

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
                        
                        <div>
                            <button
                                id='GenerateShipmentReportBtn'
                                style={{fontFamily: 'Bebas Neue, cursive'}}
                                className='px-2.5 py-1 bg-[#949494] text-white rounded-sm text-sm shadow-md'
                                onClick={() => {
                                    setDisplayDetail(false);
                                    handleGenerateReport();
                                }}
                            >
                                {selectedLanguage === 'EN' ? 'Generate Report' : 'Générer rapport'}
                            </button>

                            {generateReport && (
                                <div className="hidden">
                                    <BlobProvider document={<ShipmentReportTemplate shipment={selectedShipment}
                                                                                    inventories={inventoriesWithItem}
                                                                                    selectedLanguage={selectedLanguage}/>}>
                                        {({blob, loading}) => {
                                            if (loading) {
                                                return toast.success(selectedLanguage === 'EN' ? 'Client Report generated successfully!' : 'Rapport client généré avec succès !', {
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
                                                link.setAttribute('download', selectedLanguage === 'EN' ? 'Client Report.pdf' : 'Rapport Client.pdf');
                                                document.body.appendChild(link);
                                                link.click();
                                                document.body.removeChild(link);
                                                setGenerateReport(false);
                                            }
                                        }}
                                    </BlobProvider>
                                </div>
                            )}
                        </div>
                    

                        {checkIfAnyActionsCanBePerformedOnShipment() &&
                            <div>
                                <button id='cancelShipmentBtn'
                                        className='px-2.5 py-1 bg-[#FF0000] text-white rounded-sm text-sm shadow-md'
                                        style={{fontFamily: 'Bebas Neue, cursive'}} onClick={() => {
                                    setDisplayDetail(false);
                                    cancelShipment(shipmentId)
                                }}>{selectedLanguage === "EN" ? "Cancel Shipment" : "Annuler l'expédition"}</button>
                            </div>
                        }
                    </div>

                </div>

                <hr className="border-1 border-grey mb-6"/>

                {/* ShipmentID + ShipmentName + ShipmentFullName + Email*/}
                <div className='flex gap-4 flex-wrap my-2'>
                    <div className="w-[100%] md:w-[280px] flex flex-col gap-1">
                        <input
                            type="text"
                            className="w-[100%] border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="DetailShipmentId"
                            name="DetailShipmentId"
                            required
                            value={selectedShipment.shipmentId || ''}
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
                            {selectedLanguage === "EN" ? "Final Weight (lb)" : "Poids Final (lb)"}
                        </label>
                    </div>
                </div>

            </div>

            {/* ---------------------------observer code section ------------------------------------ */}
            <div className='px-[5%] py-10 pb-16'>

                <div>
                    <div style={{fontFamily: 'Bebas Neue, cursive'}}
                         className='text-2xl mb-4'>{selectedLanguage === "EN" ? "Observer Codes" : "Codes d'observation"}</div>

                    <div className='flex flex-wrap gap-5'>
                        <input id="observerCodeName"
                               className="w-[100%] md:w-[20%] border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                               type="text" placeholder={selectedLanguage === "EN" ? "Code Name" : "Nom de Code"}/>
                        <select id="observerCodePermission"
                                className="w-[100%] md:w-[20%] border border-[lightgray] text-xs h-[35px] px-4 rounded-sm">
                            <option value="READ">{selectedLanguage === "EN" ? "Read-Only" : "Accès Lecture"}</option>
                            <option value="EDIT">{selectedLanguage === "EN" ? "Edit Access" : "Accès Modifier"}</option>
                            <option value="FULL">{selectedLanguage === "EN" ? "Full Access" : "Accès Total"}</option>
                        </select>

                        <div className='flex gap-3'>
                            <button onClick={() => generateObserverCode()} style={{fontFamily: 'Bebas Neue, cursive'}}
                                    className="py-2 bg-companyYellow rounded text-white px-5 md:h-[100%] md:py-0 shadow-lg">{selectedLanguage === "EN" ? "Generate Code" : "Générer code"}</button>
                            <button onClick={() => getAllObserverCodes()}
                                    style={{fontFamily: 'Bebas Neue, cursive', alignItems: 'center'}}
                                    className="bg-companyYellow text-white px-[10px] rounded-full text-sm flex flex-row gap-2">
                                <span>{refreshIcon()}</span></button>
                        </div>

                    </div>


                    <hr className='my-5'/>

                    <div style={observerCodes.length != 0 ? {height: "250px"} : {height: "50px"}}
                         className='overflow-auto border-b'>

                        {observerCodes.length != 0 ?

                            observerCodes.map((observerCode: any) => (

                                <div key={observerCode.observerId} style={{alignItems: "center"}}
                                     className='px-[20px] gap-[5%] md:gap-[10%] lg:gap-[19%] py-2 w-[100%] flex border shadow-md rounded-lg overflow-auto mb-2'>

                                    <div style={{fontFamily: 'Bebas Neue, cursive'}} className=''>
                                        <div
                                            className='w-[120px] text-center py-1 bg-companyYellow rounded italic text-lg text-white'>
                                            {observerCode.name}
                                        </div>
                                    </div>

                                    <div
                                        className="tracking-[10px] text-sm font-light">{observerCode.observerCode}</div>

                                    <select id={`select-${observerCode.observerId}`}
                                            key={`select-${selectSectionKey}-${observerCode.observerId}`}
                                            defaultValue={observerCode.permission}
                                            disabled={editObserverCode == observerCode.observerId ? false : true}
                                            className='border border-[lightgray] text-xs h-[30px] px-4 rounded-sm'>
                                        <option
                                            value="READ">{selectedLanguage === "EN" ? "Read-Only" : "Accès Lecture"}</option>
                                        <option
                                            value="EDIT">{selectedLanguage === "EN" ? "Edit Access" : "Accès Modifier"}</option>
                                        <option
                                            value="FULL">{selectedLanguage === "EN" ? "Full Access" : "Accès Total"}</option>
                                    </select>

                                    <div style={{alignItems: "center"}} className='flex flex-row gap-5'>
                                        {editObserverCode == observerCode.observerId ?

                                            <>
                                                <button onClick={() => {
                                                    EditObserverCode(observerCode.observerId, observerCode)
                                                }}
                                                        className='hover:scale-110 duration-200 ease-in-out'>{generateSaveIconSVG()}</button>
                                                <button onClick={() => {
                                                    cancelSave()
                                                }}
                                                        className='hover:scale-110 duration-200 ease-in-out'>{generateCancelIconSVG()}</button>
                                            </>

                                            :

                                            <button id={`select-${observerCode.observerId}`} onClick={() => {
                                                setEditObservercode(observerCode.observerId)
                                            }}
                                                    className='hover:scale-110 duration-200 ease-in-out'>{generateEditIconSVG()}</button>

                                        }
                                        <button onClick={() => deleteObserverCode(observerCode.observerId)}
                                                className='hover:scale-110 duration-200 ease-in-out'>{generateTrashCanIconSVG()}</button>
                                    </div>

                                </div>
                            ))

                            :

                            <div
                                className='opacity-25'>{selectedLanguage === "EN" ? "[ No Code Generated ]" : "[ Aucun code généré ]"}</div>
                        }

                    </div>

                </div>
            </div>

            {/* ---------------------------observer code section End ------------------------------------ */}

            {/* Inventory Section */}
            <div className='px-[5%] pb-10'>
                <div className='flex flex-col '>

                    {/* Inventory title + Add New inventory btn */}
                    <div className='flex flex-row items-center justify-between mb-5'>

                        <div style={{fontFamily: 'Bebas Neue, cursive'}}
                             className='text-2xl'>{selectedLanguage === "EN" ? "Inventories" : "Inventaires"}</div>

                        <div className='flex flex-row'>
                            <div>
                                {checkIfAnyActionsCanBePerformedOnShipment() ? <button id='AddInventoryBtn'
                                                                                       className='px-3 py-[2px] bg-white border-[3px] border-companyYellow border-dashed text-companyYellow rounded-sm txt-sm'
                                                                                       style={{fontFamily: 'Bebas Neue, cursive'}}
                                                                                       onClick={() => {
                                                                                           addInventory()
                                                                                       }}>{selectedLanguage === "EN" ? "Add Inventory" : "Ajouter inventaire"}
                                    <span className='pl-2'>+</span></button> : ''}
                            </div>
                        </div>

                    </div>

                    {/* New Inventory Accordion (Youssef CH) */}
                    {inventories.length == 0 ?
                        <div
                            className='text-center opacity-25 font-light'>{selectedLanguage === "EN" ? "[ No Inventories ]" : "[ Aucun inventaire ]"}</div>
                        :
                        <div className='Accordion'>
                            {
                                inventories.map((inventory: any, i) => (

                                    <div className='item mb-1 py-1'>

                                        <div style={{alignItems: "center"}}
                                             className='title flex justify-between border-b-[1px] border-gray-300 pb-2'>

                                            <h4 style={{fontFamily: 'Bebas Neue, cursive'}}
                                                className='text-gray-600 text-lg'>{inventory.name} <span
                                                className='text-sm text-companyYellow pl-1'>
                                                {selectedLanguage === "EN"
                                                    ? `[ ${inventory.inventoryStatus} ]`

                                                    : inventory.inventoryStatus === "CREATED"
                                                        ? "[ CRÉÉ ]"
                                                        : inventory.inventoryStatus === "PACKED"
                                                            ? "[ EMBALLÉ ]"
                                                            : inventory.inventoryStatus === "IN_PROGRESS"
                                                                ? "[ EN COURS ]"
                                                                : "[ LOADING... ]"

                                                }
                                            </span></h4>

                                            <div className='flex flex-row gap-3'>
                                                <div>
                                                    {checkIfAnyActionsCanBePerformedOnShipment() ?
                                                        <button id='AddItem'
                                                                className='px-3 py-[2px] bg-companyYellow text-white rounded-sm text-[15px] shadow-md opacity-40 hover:opacity-100 duration-200 ease-in-out'
                                                                style={{fontFamily: 'Bebas Neue, cursive'}}
                                                                onClick={() => {
                                                                    addItem();
                                                                    setSelectedInventoryId(inventory.inventoryId)
                                                                }}>{selectedLanguage === "EN" ? "Add Item" : "Ajouter article"}
                                                            <span className='pl-2'>+</span></button> : ''}
                                                </div>

                                                <div id={`${inventory.inventoryId}-TripleBtn`}
                                                     onClick={(e) => handleOptionsClick(inventory.inventoryId, e)}
                                                     className='text-xl text-gray-400 hover:cursor-pointer'>&#8942;</div>
                                            </div>

                                        </div>

                                        <div
                                            className={InventoryAccordionSelected == inventory.inventoryId ? "h-[100%] overflow-visible py-5 px-5 transition-all " : "overflow-hidden max-h-0 transition-all"}>
                                            <div>
                                                <div>
                                                    {items.length == 0 ?
                                                        <div className='opacity-20 text-sm font-light'>
                                                            {selectedLanguage === "EN" ? "[ No Items ]" : "[Aucun Article]"}
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

                                                {checkIfAnyActionsCanBePerformedOnShipment() &&

                                                    <button
                                                        className='px-3 py-1 mt-5 bg-white border-companyYellow border-dashed border-[2px] text-companyYellow rounded-sm text-[15px] w-[100%] flex flex-row justify-between opacity-40 hover:opacity-100 duration-200 ease-in-out'
                                                        onClick={() => {
                                                            addItem();
                                                            setSelectedInventoryId(inventory.inventoryId)
                                                        }}>
                                                        <span>{selectedLanguage === "EN" ? "Add New Item" : "Ajouter nouvel article"}</span>
                                                        <span>+</span>
                                                    </button>
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
                                    {checkIfAnyActionsCanBePerformedOnShipment() &&
                                        <>
                                            <div id='editBtnOption' className="p-1 cursor-pointer hover:bg-gray-100"
                                                 onClick={() => {
                                                     editInventory(activeInventoryId)
                                                 }}>{selectedLanguage === "EN" ? "Edit" : "Modifier"}</div>
                                            <div id='deleteBtnOption' className="p-1 cursor-pointer hover:bg-gray-100"
                                                 onClick={() => {
                                                     deleteInventory(activeInventoryId)
                                                 }}>{selectedLanguage === "EN" ? "Delete" : "Effacer"}</div>
                                            <div id='detailsBtnOption' className="p-1 cursor-pointer hover:bg-gray-100"
                                                 onClick={() => {
                                                     getInventory(activeInventoryId)
                                                 }}>{selectedLanguage === "EN" ? "Details" : "Détails"}</div>
                                        </>}
                                    {activeInventoryId == InventoryAccordionSelected ?

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
                                    {checkIfAnyActionsCanBePerformedOnShipment() &&
                                        <>
                                            <div id='editItemBtn' className="p-1 cursor-pointer hover:bg-gray-100"
                                                 onClick={() => {
                                                     editItem(activeInventoryId, activeItemId)
                                                 }}>{selectedLanguage === "EN" ? "Edit" : "Modifier"}</div>
                                            <div id='deleteItemBtn' className="p-1 cursor-pointer hover:bg-gray-100"
                                                 onClick={() => {
                                                     deleteItem(activeInventoryId, activeItemId)
                                                 }}>{selectedLanguage === "EN" ? "Delete" : "Effacer"}</div>
                                            <div id='detailsItemBtn' className="p-1 cursor-pointer hover:bg-gray-100"
                                                 onClick={() => {
                                                     getItem(activeInventoryId, activeItemId)
                                                 }}>{selectedLanguage === "EN" ? "Details" : "Détails"}</div>
                                        </>}
                                </div>
                            )}
                        </div>
                    }

                </div>
            </div>

        </div>
    );
}


export default ShipmentDetailsPage;