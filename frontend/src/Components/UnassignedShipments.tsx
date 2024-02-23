import Cookies from 'js-cookie';
import React, { useEffect, useState } from 'react'
import Popup from 'reactjs-popup';
import axios from 'axios'
import { toast } from 'react-toastify'
import { useAuth } from '../auth/components/AuthService';

function UnassignedShipments() {

    const [unassignedShipments, setUnassignedShipments] = useState([])
    const [assignToMePopUp, setAssignToMePopUp] = useState(false)

    const userLightModeOrDarkMode = () => {
        let mediaQueryObj = window.matchMedia('(prefers-color-scheme: dark)');
        if (mediaQueryObj.matches)
            return 'dark';
        else
            return 'light'
    }

    //get All shipments where truckid is null
    const fetchData = async () => {
        try {
            const response = await axios.get(process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/unassigned/driver');

            //@ts-ignore
            const mappedShipments = response.data.map((shipment: any) => ({
                shipmentId: shipment.shipmentId,
                driverId: shipment.driverId,
                truckId: shipment.truckId,
                shipmentName: shipment.shipmentName,
                email: shipment.email,
                status: shipment.status,
                phoneNumber: shipment.phoneNumber,
                firstName: shipment.firstName,
                lastName: shipment.lastName
            }));

            setUnassignedShipments(mappedShipments)


        } catch (error) {
            toast.error(selectedLanguage === "EN" ? 'Error Loading Data' : `Erreur de chargement des données`, {
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
        fetchData();
    }, [])

    const [selectedLanguage, setSelectedLanguage] = useState('');

    useEffect(() => {

        const language = Cookies.get("MovingExpress_SelectedLanguage")

        if (language) {
            // set selected Language
            setSelectedLanguage(language)
        } else {
            // set a cookie and usestate to english as default
            Cookies.set('MovingExpress_SelectedLanguage', 'EN', { expires: 7, path: '/' });
            setSelectedLanguage("EN")
        }

    }, [])

    const refreshIcon = () => {
        return (
            <svg width="15" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M50 100C36.0417 100 24.2187 95.1562 14.5312 85.4687C4.84375 75.7812 0 63.9583 0 50C0 36.0417 4.84375 24.2187 14.5312 14.5312C24.2187 4.84375 36.0417 0 50 0C57.1875 0 64.0625 1.48333 70.625 4.45C77.1875 7.41667 82.8125 11.6625 87.5 17.1875V0H100V43.75H56.25V31.25H82.5C79.1667 25.4167 74.6104 20.8333 68.8312 17.5C63.0521 14.1667 56.775 12.5 50 12.5C39.5833 12.5 30.7292 16.1458 23.4375 23.4375C16.1458 30.7292 12.5 39.5833 12.5 50C12.5 60.4167 16.1458 69.2708 23.4375 76.5625C30.7292 83.8542 39.5833 87.5 50 87.5C58.0208 87.5 65.2604 85.2083 71.7188 80.625C78.1771 76.0417 82.7083 70 85.3125 62.5H98.4375C95.5208 73.5417 89.5833 82.5521 80.625 89.5313C71.6667 96.5104 61.4583 100 50 100Z" fill="white" />
            </svg>
        )
    }

    // --------------------------------------- assign truck and driver to a shipement -------------------------------------------------

    // open pop up and set selected shipemnt 

    const [selectedShipmentId, setSelectedShipmentId] = useState("")
    const [allTrcuks, setAllTrucks] = useState([])
    const [driverId, setDriverId] = useState("")
    const auth = useAuth()

    const getUserInfo = async () => {
        try {
            const response = await axios.get(process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/users', {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                },
            });

            // Assuming response.data is a JSON object
            const userData = response.data;
            setDriverId(userData.userId);

        } catch (error) {
            toast.error(selectedLanguage === "EN" ? 'Error fetching driver info' : `Erreur lors de la récupération des informations du conducteur.`, {
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
        getUserInfo()
    }, [])

    const selectedShipmentAndActivatePopUp = (shipmentId: string) => {

        setSelectedShipmentId(shipmentId)

        setAssignToMePopUp(true)

    }



    // get All trucks and set dropdown    

    useEffect(() => {
        getAlltrcukVins()
    }, [])

    const getAlltrcukVins = async () => {
        try {

            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/trucks`)

            let data = response.data
            setAllTrucks(data)

        } catch (error) {
            toast.error(selectedLanguage === "EN" ? 'Error Loading Data' : `Erreur de chargement des données`, {
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


    // assign truck and driver to a shipement function

    const assignTruckAndDriverToShipment = async () => {

        // @ts-ignore
        let vin = document.getElementById('truckVin').value

        let requestbody = {
            shipmentId: selectedShipmentId,
            vin: vin,
            driverId: driverId
        }

        try {

            await axios.put(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments`, requestbody)

            fetchData()

            setAssignToMePopUp(false)

            toast.success(selectedLanguage === "EN" ? `Assigned shipment successfully!` : `Expédition assignée avec succès !`, {
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
            toast.error(selectedLanguage === "EN" ? 'Something wrong happend' : `Quelque chose s'est mal passé`, {
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

    }



    return (
        <div>

            <Popup className='my-popup' open={assignToMePopUp} onClose={() => { setAssignToMePopUp(false) }} position="right top">
                <div className='px-10 py-10 flex flex-col gap-3'>

                    <div style={{ alignItems: "center" }} className='flex flex-row justify-between'>

                        {selectedLanguage === "EN" ?

                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="leading-none text-2xl">Assign <span className='text-companyYellow'>Truck</span></div>

                            :

                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="leading-none text-2xl">Assigner le <span className='text-companyYellow'>Camion</span></div>

                        }

                        <button onClick={() => { setAssignToMePopUp(false) }} style={{ alignItems: "center" }} className='w-[30px] h-[30px] justify-center flex bg-companyYellow rounded font-semibold shadow-xl text-white'>X</button>
                    </div>

                    <hr />

                    <div className="font-thin mt-5">
                        {selectedLanguage === "EN" ?
                            `Please select a truck to assign the shipment to using the drop down field bellow and when done click the “Assign Shipment” button.`
                            :
                            `Veuillez sélectionner un camion pour assigner l'expédition en utilisant le champ de sélection ci-dessous, puis cliquez sur le bouton "Assigner l'expédition" lorsque vous avez terminé.`
                        }
                    </div>

                    <div className='flex flex-col md:flex-row gap-5 w-full mt-5'>
                        <div className='w-full'>
                            <div className='flex flex-col gap-2'>
                                <select id='truckVin' className='border w-[100%] h-[40px] pl-4 rounded shadow'>
                                    {
                                        allTrcuks.length == 0 ?

                                            <option></option>

                                            :


                                            allTrcuks.map((truck: any) => (

                                                <option id={truck.vin}>{truck.vin}</option>

                                            ))


                                    }
                                </select>
                                <label className='opacity-50' style={{ fontFamily: 'Bebas Neue, cursive' }}>
                                    {selectedLanguage === "EN" ? `Truck Vin` : `VIN du camion`}
                                </label>
                            </div>


                        </div>
                        <div className='w-full'>
                            <button id='assignBtn' onClick={() => { assignTruckAndDriverToShipment() }} style={{ fontFamily: 'Bebas Neue, cursive' }} className='px-5 py-2 bg-companyYellow text-white rounded shadow-xl'>{selectedLanguage === "EN" ? `Assign to me` : `Attribuer a moi`}</button>
                        </div>
                    </div>


                </div>
            </Popup>

            <div className='px-[5%] pt-[5%] pb-20'>
                <div className="flex flex-row justify-between">

                    {selectedLanguage === "EN" ?
                        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-2xl'>Unassigned <span className="text-companyYellow">Shipments</span></div>
                        :
                        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-2xl'>Devis <span className="text-companyYellow"> d'expédition</span></div>
                    }


                    <button onClick={() => { }} style={{ fontFamily: 'Bebas Neue, cursive', alignItems: 'center' }} className="bg-companyYellow text-white py-1 px-4 rounded-sm text-sm flex flex-row gap-2">
                        <span>{selectedLanguage === "EN" ? "Refresh" : "Actualiser"}</span><span>{refreshIcon()}</span>
                    </button>
                </div>

                <hr className='mb-5 mt-2' />

                <div className='overflow-auto'>

                    <table className='border w-[100%] '>

                        <thead style={{ fontFamily: 'Bebas Neue, cursive' }} className='border bg-companyYellow text-white text-sm text-center'>
                            <tr className=''>
                                <td className='border py-2'>{selectedLanguage === "EN" ? "Shipment Name" : "Nom de l'expédition"}</td>
                                <td className='border py-2 hidden lg:table-cell'>{selectedLanguage === "EN" ? "e-mail" : "Courriel"}</td>
                                <td className='border py-2'>{selectedLanguage == "EN" ? "Phone Number" : "téléphone"}</td>
                                <td className='border py-2 px-2 hidden lg:table-cell'>{selectedLanguage == "EN" ? "First Name" : "Prénom"}</td>
                                <td className='border py-2 px-2 hidden lg:table-cell'>{selectedLanguage == "EN" ? "Last Name" : "Nom de Famille"}</td>
                                <td className='border py-2'>{selectedLanguage === "EN" ? "Status" : "Statut"}</td>
                                <td className='border py-2'></td>
                            </tr>
                        </thead>

                        <tbody>

                            {
                                //@ts-ignore
                                unassignedShipments.length === 0 ?

                                    <div className='py-5 text-center font-thin opacity-40'>{selectedLanguage === "EN" ? `[No Shipments]` : `[Aucune Expédition]`}</div>

                                    :

                                    unassignedShipments.map((shipment: any) => (

                                        <tr className='text-center text-sm'>
                                            <td id={`${shipment.shipmentId}-name`} className='border px-3'>{shipment.shipmentName}</td>
                                            <td id={`${shipment.shipmentId}-email`} className='border px-3 hidden lg:table-cell'>{shipment.email}</td>
                                            <td id={`${shipment.shipmentId}-phoneNumber`} className='border px-3 '>{shipment.phoneNumber}</td>
                                            <td id={`${shipment.shipmentId}-firstName`} className='border px-3  hidden lg:table-cell'>{shipment.firstName}</td>
                                            <td id={`${shipment.shipmentId}-lastName`} className='border px-3  hidden lg:table-cell'>{shipment.lastName}</td>
                                            <td id={`unassigned-${shipment.shipmentId}`} className='border  px-2'>
                                                {
                                                    selectedLanguage === "EN" ?
                                                        `${shipment.status}`
                                                        :
                                                        shipment.status === "QUOTED"
                                                            ? "CRÉÉ"
                                                            : "Loading..."
                                                }
                                            </td>
                                            <td className='border px-3'><button id={`${shipment.shipmentId}-assignBtn`} onClick={() => { selectedShipmentAndActivatePopUp(shipment.shipmentId) }} style={{ fontFamily: 'Bebas Neue, cursive' }} className="bg-companyYellow text-white py-1 px-5 rounded-sm text-sm shadow-md w-28">{selectedLanguage === "EN" ? "Assign to me" : "Attribuer à moi"}</button></td>
                                        </tr>
                                    ))
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        </div >
    )
}

export default UnassignedShipments