import Cookies from 'js-cookie'
import React, { useEffect, useRef, useState } from 'react'
import Popup from 'reactjs-popup'
import axios from "axios";
import { BlobProvider } from "@react-pdf/renderer";
import ShipmentManifestTemplate, { Shipment } from "./ShipmentManifestTemplate";
import { toast } from "react-toastify";

function AssignedShipments() {
    const [assignedShipments, setAssignedShipments] = useState([])
    const [updateStatusPopUp, setUpdateStatusPopUp] = useState(false)
    const [unasignShipmentPopUp, setUnassignShipmentPopUp] = useState(false)
    const [finalWeightPopUp, setFinalWeightPopUp] = useState(false)
    const [userId, setUserId] = useState('')
    const [generateReport, setGenerateReport] = useState(false)
    const [manifestData, setManifestData] = useState<Shipment | null>(null);
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
        getAssignedShipments()

    }, [])

    const refreshIcon = () => {
        return (
            <svg width="15" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                    d="M50 100C36.0417 100 24.2187 95.1562 14.5312 85.4687C4.84375 75.7812 0 63.9583 0 50C0 36.0417 4.84375 24.2187 14.5312 14.5312C24.2187 4.84375 36.0417 0 50 0C57.1875 0 64.0625 1.48333 70.625 4.45C77.1875 7.41667 82.8125 11.6625 87.5 17.1875V0H100V43.75H56.25V31.25H82.5C79.1667 25.4167 74.6104 20.8333 68.8312 17.5C63.0521 14.1667 56.775 12.5 50 12.5C39.5833 12.5 30.7292 16.1458 23.4375 23.4375C16.1458 30.7292 12.5 39.5833 12.5 50C12.5 60.4167 16.1458 69.2708 23.4375 76.5625C30.7292 83.8542 39.5833 87.5 50 87.5C58.0208 87.5 65.2604 85.2083 71.7188 80.625C78.1771 76.0417 82.7083 70 85.3125 62.5H98.4375C95.5208 73.5417 89.5833 82.5521 80.625 89.5313C71.6667 96.5104 61.4583 100 50 100Z"
                    fill="white" />
            </svg>
        )
    }

    const [optionsVisible, setOptionsVisible] = useState(false);
    const [activeShipmentId, setActiveShipmentId] = useState('');
    const [shipmentOptionsPosition, setShipmentOptionsPosition] = useState({ top: 0, left: 0 });
    const [shipmentOptionsVisible, setShipmentOptionsVisible] = useState(false);
    const optionsRef = useRef<HTMLDivElement>(null);

    interface Shipment {
        shipmentId: string;
        shipmentName: string;
        status: string;
        vin: string;
        email: string;
        phoneNumber: string;
        firstName: string;
        lastName: string;
    }

    const [activeShipment, setActiveShipment] = useState<Shipment>({
        shipmentName: '',
        shipmentId: '',
        status: '',
        vin: '',
        email: '',
        phoneNumber: '',
        firstName: '',
        lastName: '',
    });

    const getAssignedShipments = async () => {
        // get all shipments where driverid is equal to the current driver id
        axios.get(process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/shipments/assigned/driver')
            .then((response) => {
                setAssignedShipments(response.data)
            })
            .catch((error) => {
                toast.error(selectedLanguage === "EN" ? 'Error fetching assigned shipments' : `Erreur lors de la récupération des expéditions assignées.`, {
                    position: 'top-right',
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: false,
                    progress: undefined,
                    theme: 'light',
                })
            })
    }

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (optionsRef.current && !optionsRef.current.contains(event.target as Node)) {
                setOptionsVisible(false);
                setShipmentOptionsVisible(false)
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleOptionsShipmentClick = (shipmentId: string, event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        event.stopPropagation(); // Prevent the click from closing the options immediately
        setActiveShipmentId(shipmentId);
        const dotsPosition = event.currentTarget.getBoundingClientRect();
        setShipmentOptionsPosition({
            top: dotsPosition.bottom + window.scrollY,
            left: dotsPosition.left - 110 + window.scrollX
        });
        setShipmentOptionsVisible(v => !v);
    };

    const generateDriverReport = async (shipmentId: string) => {
        axios.get(process.env.REACT_APP_BACKEND_URL + "api/v1/movingexpress/shipments/" + shipmentId + "/driver-report")
            .then((response) => {
                let data = response.data

                if (data.weight <= 0) {
                    toast.error(selectedLanguage === "EN" ? 'Error generating report because final shipment weight is not set' : `Erreur de génération de rapport car le poids final de l'expédition n'est pas défini.`, {
                        position: 'top-right',
                        autoClose: 5000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: false,
                        progress: undefined,
                        theme: 'light',
                    })
                } else {
                    setManifestData(data)
                    setGenerateReport(true)
                }
            })
            .catch((error) => {
                toast.error(selectedLanguage === "EN" ? 'Error generating report' : `Erreur lors de la génération du rapport`, {
                    position: 'top-right',
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: false,
                    progress: undefined,
                    theme: 'light',
                })
            })
    }

    // ----------------------------------------- unassign shipment ------------------------------------------------------

    const unassignShipment = async () => {
        //@ts-ignore
        let actualShipmentName = document.getElementById('unassignShipmentName-' + activeShipmentId).value
        //@ts-ignore
        let expectedShipmentName = document.getElementById('unassignShipmentName-' + activeShipmentId).getAttribute('placeholder')
        if (actualShipmentName !== expectedShipmentName) {
            toast.error(selectedLanguage === "EN" ? `Entered shipment name doesn't match shipment name` : `Nom de l'expédition saisi ne correspond pas au nom de l'expédition`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: false,
                progress: undefined,
                theme: 'light',
            })

            return
        }

        try {
            await axios.put(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${activeShipmentId}/driver`)

            getAssignedShipments()
            setUnassignShipmentPopUp(false)

            toast.success(selectedLanguage === "EN" ? `Shipment unassigned successfully!` : `Expédition désassignée avec succès !`, {
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


    // ----------------------------------------- update shipment final weight -------------------------------------------

    const setShipmentfinalweigth = async () => {
        // @ts-ignore
        let finalWeight = document.getElementById('finalWeigth').value

        if (finalWeight <= 0 || activeShipmentId == '') {
            toast.error(selectedLanguage === "EN" ? 'Something is wrong with the input value' : `Quelque chose ne va pas avec la valeur d'entrée`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            });
        } else {

            let requestBody = {
                shipmentId: activeShipmentId,
                finalWeight: finalWeight
            }

            try {

                await axios.put(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/setShipmentFinalWeight`, requestBody)

                getAssignedShipments()

                setFinalWeightPopUp(false)

                toast.success(selectedLanguage === "EN" ? `Shipment updated successfully!` : `Envoi mis à jour avec succès !`, {
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


    }

    // ----------------------------------------- update shipment Status weight -------------------------------------------

    const setShipmentStatus = async () => {
        // @ts-ignore
        let shipStatus = document.getElementById('status').value


        let requestBody = {
            status: shipStatus
        }

        console.log(requestBody)

        try {

            await axios.put(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/${activeShipmentId}`, requestBody)

            getAssignedShipments()

            setUpdateStatusPopUp(false)

            toast.success(selectedLanguage === "EN" ? `Shipment updated successfully!` : `Envoi mis à jour avec succès !`, {
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
            {/* Update Shipment Status */}
            <Popup className='my-popup' open={updateStatusPopUp} onClose={() => {
                setUpdateStatusPopUp(false)
            }} position="right top">
                <div className='px-10 py-10 flex flex-col gap-3'>

                    <div style={{ alignItems: "center" }} className='flex flex-row justify-between'>
                        {selectedLanguage === "EN" ?

                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="leading-none text-2xl">Update <span
                                className='text-companyYellow'>Status</span>
                            </div>
                            :

                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="leading-none text-2xl">Mettre à jour le <span
                                className='text-companyYellow'>Statut</span>
                            </div>

                        }



                        <button onClick={() => {
                            setUpdateStatusPopUp(false)
                        }} style={{ alignItems: "center" }}
                            className='w-[30px] h-[30px] justify-center flex bg-companyYellow rounded font-semibold shadow-xl text-white'>X
                        </button>
                    </div>

                    <hr />

                    <div className="font-thin mt-5">
                        {selectedLanguage === "EN" ? `To update a shipment status, choose the relevant status from the dropdown below and click
                        "Update Status." Easy and quick!` : `Pour mettre à jour le statut d'une expédition, choisissez le statut pertinent dans le menu déroulant ci-dessous et cliquez sur "Mettre à jour le statut". Simple et rapide !`}
                    </div>

                    <div className='flex flex-col md:flex-row gap-5 w-full mt-5'>
                        <div className='w-full'>
                            <div className='flex flex-col gap-1'>
                                <input type='text' className='border w-[100%] h-[40px] pl-4 rounded shadow' value={activeShipmentId} readOnly
                                    placeholder='2a7d9f1c-64e5-4b38-9c21-8fd3c0a0e951' />
                                <label className='opacity-50' style={{ fontFamily: 'Bebas Neue, cursive' }}>
                                    {selectedLanguage === "EN" ? `Shipment Id` : `Identifiant de l'expédition`} </label>
                            </div>
                        </div>
                        <div className='w-[50%]'>
                            <div className='flex flex-col gap-1'>
                                <select id='status' className='border w-[100%] pl-4 rounded shadow h-[40px]'>
                                    <option value="QUOTED">{selectedLanguage === "EN" ? `QUOTED` : `CITÉE`}</option>
                                    <option value="LOADING">{selectedLanguage === "EN" ? `LOADING` : `EN CHARGEMENT`}</option>
                                    <option value="TRANSIT">{selectedLanguage === "EN" ? `TRANSIT` : `EN TRANSIT`}</option>
                                    <option value="DELIVERED">{selectedLanguage === "EN" ? `DELIVERED` : `LIVRÉ`}</option>
                                    <option value="CANCELED">{selectedLanguage === "EN" ? `CANCELED` : `ANNULÉ`}</option>
                                </select>
                                <label className='opacity-50' style={{ fontFamily: 'Bebas Neue, cursive' }}>{selectedLanguage === "EN" ? `Status` : `Statut`}</label>
                            </div>

                        </div>
                        <div className='w-full'>
                            <button onClick={() => { setShipmentStatus() }} style={{ fontFamily: 'Bebas Neue, cursive' }}
                                className='px-5 py-2 bg-companyYellow text-white rounded shadow-xl'>{selectedLanguage === "EN" ? `Update Status` : `Modifier Statut`}
                            </button>
                        </div>
                    </div>


                </div>
            </Popup>

            {/* Unassign Shipment */}
            <Popup className='my-popup' open={unasignShipmentPopUp} onClose={() => {
                setUnassignShipmentPopUp(false)
            }} position="right top">
                <div className='px-10 py-10 flex flex-col gap-3'>
                    <div style={{ alignItems: "center" }} className='flex flex-row justify-between'>

                        {selectedLanguage === "EN" ?
                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="leading-none text-2xl">Unnassign <span
                                className='text-companyYellow'>Shipment</span>
                            </div>
                            :

                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="leading-none text-2xl">Désassigner <span
                                className='text-companyYellow'>l'expédition</span>
                            </div>

                        }

                        <button onClick={() => {
                            setUnassignShipmentPopUp(false)
                        }} style={{ alignItems: "center" }}
                            className='w-[30px] h-[30px] justify-center flex bg-companyYellow rounded font-semibold shadow-xl text-white'>X
                        </button>
                    </div>

                    <hr />

                    <div className="font-thin mt-5">
                        {selectedLanguage === "EN" ?
                            `Please confirm your decision to unassign this shipment by entering the shipment name.
                        When confirmed, press the “confirm” button below to proceed with this action.`
                            :
                            `Veuillez confirmer votre décision de désassigner cette expédition .
                        Une fois confirmé, appuyez sur le bouton "Confirmer" ci-dessous pour procéder à cette action.`
                        }
                    </div>

                    <div className='flex flex-col md:flex-row gap-5 w-full mt-5'>
                        <div className='w-full'>
                            <div className='flex flex-col gap-1'>
                                <input type='text'
                                    className='border w-[100%] h-[40px] pl-4 rounded shadow'
                                    value={activeShipmentId} readOnly
                                    placeholder='2a7d9f1c-64e5-4b38-9c21-8fd3c0a0e951'
                                    id={`unassignShipmentId-${activeShipment.shipmentId}`} />
                                <label className='opacity-50' style={{ fontFamily: 'Bebas Neue, cursive' }}>{selectedLanguage === "EN" ? `Shipment Id` : `Identifiant de l'expédition`}</label>
                            </div>
                        </div>
                        <div className='w-full'>
                            <div className='flex flex-col gap-1'>
                                <input type='text'
                                    className='border w-[100%] h-[40px] pl-4 rounded shadow'
                                    placeholder={activeShipment.shipmentName}
                                    id={`unassignShipmentName-${activeShipment.shipmentId}`} />
                                <label className='opacity-50' style={{ fontFamily: 'Bebas Neue, cursive' }}>{selectedLanguage === "EN" ? `Shipment Name` : `Nom de l'expédition`}</label>
                            </div>
                        </div>
                        <div className='w-full'>
                            <button
                                id={`unassignShipmentBtn-${activeShipment.shipmentId}`}

                                onClick={() => { unassignShipment() }}
                                style={{ fontFamily: 'Bebas Neue, cursive' }}
                                className='px-5 py-2 bg-companyYellow text-white rounded shadow-xl'>{selectedLanguage === "EN" ? `Unassign Shipment` : `Désassigner l'expédition`}
                            </button>
                        </div>
                    </div>


                </div >
            </Popup >

            {/* Set Final Weight */}
            < Popup className='my-popup' open={finalWeightPopUp} onClose={() => {
                setFinalWeightPopUp(false)
            }
            } position="right top" >
                <div className='px-10 py-10 flex flex-col gap-3'>

                    <div style={{ alignItems: "center" }} className='flex flex-row justify-between'>

                        {selectedLanguage === "EN" ?

                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="leading-none text-2xl">Final
                                Shipment<span className='text-companyYellow'> Weight</span>
                            </div>
                            :

                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="leading-none text-2xl">Poids final de <span className='text-companyYellow'>l'expédition</span>
                            </div>
                        }


                        <button onClick={() => {
                            setFinalWeightPopUp(false)
                        }} style={{ alignItems: "center" }}
                            className='w-[30px] h-[30px] justify-center flex bg-companyYellow rounded font-semibold shadow-xl text-white'>X
                        </button>
                    </div>

                    <hr />

                    <div className="font-thin mt-5">
                        {selectedLanguage === "EN" ?
                            `To set the final shipment weight, simply enter the total weight in the provided input field and
                        click the "Set Weight" button.`
                            :
                            `Pour définir le poids final de l'expédition, entrez simplement le poids total dans le champ d'entrée fourni et cliquez sur le bouton "Définir le poids".`
                        }
                    </div>

                    <div className='flex flex-col md:flex-row gap-5 w-full mt-5'>
                        <div className='w-full'>
                            <div className='flex flex-col gap-1'>
                                <input type='text' className='border w-[100%] h-[40px] pl-4 rounded shadow' value={activeShipmentId} readOnly
                                    placeholder='2a7d9f1c-64e5-4b38-9c21-8fd3c0a0e951' />
                                <label className='opacity-50' style={{ fontFamily: 'Bebas Neue, cursive' }}>{selectedLanguage === "EN" ? `Shipment Id` : `Identifiant de l'expédition`}</label>
                            </div>
                        </div>

                        <div className='w-[60%]'>
                            <div className='flex flex-col gap-1'>
                                <input id='finalWeigth' type='number' className='border w-[100%] h-[40px] pl-4 rounded shadow'
                                    placeholder='0' />
                                <label className='opacity-50' style={{ fontFamily: 'Bebas Neue, cursive' }}>{selectedLanguage === "EN" ? `Weight ` : `Poids `}
                                    (lb)</label>
                            </div>
                        </div>

                        <div className='w-full'>
                            <button onClick={() => { setShipmentfinalweigth() }} style={{ fontFamily: 'Bebas Neue, cursive' }}
                                className='px-10 py-2 bg-companyYellow text-white rounded shadow-xl'>{selectedLanguage === "EN" ? `Set Final Weight ` : `Définir poids final `}
                            </button>
                        </div>
                    </div>


                </div>
            </Popup >

            <div className='px-[5%] pt-[5%] pb-20 h-[100vh]'>
                <div className="flex flex-row justify-between">

                    {selectedLanguage === "EN" ?
                        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-2xl'>My <span
                            className="text-companyYellow">Shipments</span></div>
                        :
                        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-2xl'>Devis <span
                            className="text-companyYellow"> d'expédition</span></div>
                    }


                    <button onClick={() => {
                        getAssignedShipments()
                    }} style={{ fontFamily: 'Bebas Neue, cursive', alignItems: 'center' }}
                        className="bg-companyYellow text-white py-1 px-4 rounded-sm text-sm flex flex-row gap-2">
                        <span>{selectedLanguage === "EN" ? "Refresh" : "Actualiser"}</span><span>{refreshIcon()}</span>
                    </button>
                </div>

                <hr className='mb-5 mt-2' />

                <div className='overflow-auto'>

                    <table className='border w-[100%] '>

                        <thead style={{ fontFamily: 'Bebas Neue, cursive' }}
                            className='border bg-companyYellow text-white text-sm text-center'>
                            <tr className=''>
                                <td className='border px-3 py-2'>{selectedLanguage === "EN" ? "Shipment Name" : "Nom de l'expédition"}</td>
                                <td className='border px-3 py-2'>{selectedLanguage === "EN" ? "e-mail" : "Courriel"}</td>
                                <td className='border px-3 py-2 hidden lg:table-cell'>{selectedLanguage === "EN" ? "Phone Number" : "téléphone"}</td>
                                <td className='border px-3 py-2 hidden lg:table-cell'>{selectedLanguage === "EN" ? "First Name" : "Prénom"}</td>
                                <td className='border px-3 py-2 hidden lg:table-cell'>{selectedLanguage === "EN" ? "Last Name" : "Nom de Famille"}</td>
                                <td className='border px-3 py-2'>{selectedLanguage === "EN" ? "Status" : "Statut"}</td>
                                <td className='border px-3 py-2'>{selectedLanguage === "EN" ? "Weight" : "Poid"}</td>
                                <td className='border px-3 py-2'></td>
                            </tr>
                        </thead>

                        <tbody>
                            {
                                //@ts-ignore
                                assignedShipments.length == 0 ?
                                    <div className='py-5 text-center font-thin opacity-40'>[No Shipments]</div>
                                    :
                                    assignedShipments.map((shipment: any) => (
                                        <tr className='text-center text-sm'>
                                            <td className='border px-3'>{shipment.shipmentName}</td>
                                            <td className='border px-3'>{shipment.email}</td>
                                            <td className='border px-3 hidden lg:table-cell'>{shipment.phoneNumber}</td>
                                            <td className='border px-3 hidden lg:table-cell'>{shipment.firstName}</td>
                                            <td className='border px-3 hidden lg:table-cell'>{shipment.lastName}</td>
                                            <td className='border px-3 lg:table-cell'>

                                                {selectedLanguage === "EN" ?

                                                    shipment.status
                                                    :

                                                    shipment.status === "QUOTED" ?

                                                        "CITÉE"

                                                        : shipment.status === "LOADING" ?

                                                            "EN CHARGEMENT"

                                                            : shipment.status === "TRANSIT" ?

                                                                "EN TRANSIT"

                                                                : shipment.status === "DELIVERED" ?

                                                                    "LIVRÉ"

                                                                    : shipment.status === "CANCELED" &&

                                                                    "ANNULÉ"

                                                }



                                            </td>
                                            <td className='border px-3 lg:table-cell'>{shipment.weight} lb</td>
                                            <td id={`${shipment.shipmentId}-TripleBtn`}
                                                onClick={(e) => { handleOptionsShipmentClick(shipment.shipmentId, e); setActiveShipment(shipment); }}
                                                className='text-xl text-gray-400 hover:cursor-pointer'>&#8942;</td>
                                        </tr>
                                    ))
                            }
                            {shipmentOptionsVisible && (
                                <div
                                    ref={optionsRef}
                                    className="absolute bg-[rgb(255,255,255,0.5)] backdrop-blur-md rounded shadow-lg"
                                    style={{
                                        top: `${shipmentOptionsPosition.top}px`,
                                        left: `${shipmentOptionsPosition.left}px`,
                                        zIndex: 1000 // Ensure it's above other elements
                                    }}>
                                    <>
                                        <div id='DriverReportBtn'
                                            className="px-3 py-2 cursor-pointer hover:bg-gray-100 text-sm border-b"
                                            onClick={() => {
                                                generateDriverReport(activeShipmentId)
                                            }}>{selectedLanguage === "EN" ? "Generate Report" : "Générer Rapport"}</div>
                                        <div id='UnassignBtn'
                                            className="px-3 py-2 cursor-pointer hover:bg-gray-100 text-sm border-b"
                                            onClick={() => {
                                                setShipmentOptionsVisible(false);
                                                setUnassignShipmentPopUp(true);
                                            }}>{selectedLanguage === "EN" ? "Unassign Shipment" : "Désassigner"}</div>
                                        <div id='UpdateStatusBtn'
                                            className="px-3 py-2 cursor-pointer hover:bg-gray-100 text-sm border-b"
                                            onClick={() => {
                                                setShipmentOptionsVisible(false);
                                                setUpdateStatusPopUp(true);
                                            }}>{selectedLanguage === "EN" ? "Update Status" : "Modifier Statut"}</div>
                                        <div id='UpdateWeightBtn'
                                            className="px-3 py-2 cursor-pointer hover:bg-gray-100 text-sm" onClick={() => {
                                                setShipmentOptionsVisible(false);
                                                setFinalWeightPopUp(true);
                                            }}>{selectedLanguage === "EN" ? "Update Weight" : "Modifier Poid"}</div>
                                    </>
                                </div>
                            )}
                            {generateReport && (
                                // @ts-ignore
                                <BlobProvider document={<ShipmentManifestTemplate {...manifestData} />}>
                                    {({ blob, loading }) => {
                                        if (loading) {
                                            return toast.success(selectedLanguage === 'EN' ? 'Shipment Manifest generated successfully!' : 'Manifeste d\'expéditiongénéré avec succès !', {
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
                                            link.setAttribute('download', selectedLanguage === 'EN' ? 'Shipment Manifest.pdf' : 'Manifest D\'expédition.pdf');
                                            document.body.appendChild(link);
                                            link.click();
                                            document.body.removeChild(link);
                                            setGenerateReport(false);
                                            setOptionsVisible(false);
                                        }
                                        return null;
                                    }}
                                </BlobProvider>

                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div >
    )
}


export default AssignedShipments
