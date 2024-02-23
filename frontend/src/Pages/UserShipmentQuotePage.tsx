import React, { useEffect, useState } from 'react'
import NormalNavBar from '../Components/NormalNavBar'
import axios from 'axios'
import { toast } from 'react-toastify'
//@ts-ignore
import { validate, res } from 'react-email-validator';
import Cookies from 'js-cookie'
import { useAuth } from '../auth/components/AuthService'
import ScrollToTopBtn from '../Components/ScrollToTopBtn';

function UserShipmentQuotePage() {
    const missingFieldError = (value:any) =>{
        if(value === "" || value <= 0 || value == null || value === "null"){
            toast.error(selectedLanguage === "EN" ? 'Missing Field(s)' : 'Champ(s) manquant(s)', {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })

            return true

        } 

        return false
    }

    const IsEmailValid = (email:string) => {
        validate(email);
        if(res){
            return true;
        }
        else{
            // the email is invalid
            toast.error(selectedLanguage === "EN" ?'Email Not Valid':'Adresse e-mail non valide', {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })

            return false;
        }
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        // personal info
        let firstName: string = (e.target as any).form[0].value
        if(missingFieldError(firstName)){
            return;
        }
        let lastName: string = (e.target as any).form[1].value
        if(missingFieldError(lastName)){
            return;
        }
        let email: string = (e.target as any).form[2].value
        if(missingFieldError(email)){
            return;
        }
        
        if(!IsEmailValid(email)){
            return;
        }

        let phonenumber: string = (e.target as any).form[3].value
        if(missingFieldError(phonenumber)){
            return;
        }
        let movingDate: Date = (e.target as any).form[4].value
        if(!movingDate){
            toast.error('Missing Field(s)', {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })
            return;
        }
        //Radio Btns
        let emailValueRadioBtn: boolean = (e.target as any).form[5].checked
        let PhoneNumberRadioBtn: boolean = (e.target as any).form[6].checked
        let BothValueRadioBtn: boolean = (e.target as any).form[7].checked
        let additionalComments: string = (e.target as any).form[8].value

        if(additionalComments === ""){
            additionalComments = "No Comments"
        }

        let wayToContact: string = 'BOTH'
        if (emailValueRadioBtn) {
            wayToContact = 'EMAIL'
        } else if (PhoneNumberRadioBtn) {
            wayToContact = 'PHONE_NUMBER'
        } else if (BothValueRadioBtn) {
            wayToContact = 'BOTH'
        }

        //pick up location info
        let pickUpAddress: string = (e.target as any).form[9].value
        if(missingFieldError(pickUpAddress)){
            return;
        }
        let cityP: string = (e.target as any).form[10].value
        if(missingFieldError(cityP)){
            return;
        }
        let postalCodeP: string = (e.target as any).form[11].value
        if(missingFieldError(postalCodeP)){
            return;
        }
        let countryP: string = (e.target as any).form[12].value
        let buildingTypeP: string = (e.target as any).form[13].value
        if(missingFieldError(buildingTypeP)){
            return;
        }
        let numberofRoomP: number = (e.target as any).form[14].value
        if(missingFieldError(numberofRoomP)){
            return;
        }
        // elevator Pick up
        let elevatorYesRadioBtnP: boolean = (e.target as any).form[15].checked
        let elevatorNoRadioBtnP: boolean = (e.target as any).form[16].checked

        let elevatorisPresentP: boolean
        if (elevatorYesRadioBtnP) {
            elevatorisPresentP = true
        } else {
            elevatorisPresentP = false
        }

        // drop off destination
        let dropOffAddress: string = (e.target as any).form[17].value
        if(missingFieldError(dropOffAddress)){
            return;
        }
        let cityD: string = (e.target as any).form[18].value
        if(missingFieldError(cityD)){
            return;
        }
        let postalCodeD: string = (e.target as any).form[19].value
        if(missingFieldError(postalCodeD)){
            return;
        }
        let countryD: string = (e.target as any).form[20].value
        let buildingTypeD: string = (e.target as any).form[21].value
        if(missingFieldError(buildingTypeD)){
            return;
        }
        let numberofRoomD: number = (e.target as any).form[22].value
        if(missingFieldError(numberofRoomD)){
            return;
        }
        // elevator Drop off
        let elevatorYesRadioBtnD: boolean = (e.target as any).form[23].checked
        let elevatorNoRadioBtnD: boolean = (e.target as any).form[24].checked

        let elevatorisPresentD: boolean
        if (elevatorYesRadioBtnD) {
            elevatorisPresentD = true
        } else {
            elevatorisPresentD = false
        }

        // shipment name
        let shipmentName: string = (e.target as any).form[25].value
        if(missingFieldError(shipmentName)){
            return;
        }




        const quoteForm = {
            pickupStreetAddress: pickUpAddress,
            pickupCity: cityP,
            pickupCountry: countryP,
            pickupPostalCode: postalCodeP,
            pickupNumberOfRooms: numberofRoomP,
            pickupElevator: elevatorisPresentP,
            pickupBuildingType: buildingTypeP,
            destinationStreetAddress: dropOffAddress,
            destinationCity: cityD,
            destinationCountry: countryD,
            destinationPostalCode: postalCodeD,
            destinationNumberOfRooms: numberofRoomD,
            destinationElevator: elevatorisPresentD,
            destinationBuildingType: buildingTypeD,
            firstName: firstName,
            lastName: lastName,
            emailAddress: email,
            phoneNumber: phonenumber,
            contactMethod: wayToContact,
            expectedMovingDate: movingDate,
            comment: additionalComments,
            shipmentName: shipmentName
        }
        
        clearInputFields()

        try {
            // Make a POST request using Axios
            const response = await axios.post(
                process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/quotes/request',
                quoteForm,
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            )

            toast.success('Request quote sent successfully !', {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: 'light',
            })
        } catch (error) {
            console.error('Error:', error)
            toast.error('Error something happened', {
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

    const clearInputFields = () => {
        
        var quoteForm  = document.getElementById("quoteForm");
        // @ts-ignore
        quoteForm.reset()
    }


    const[userEmail, setUserEmail] = useState('');

    const auth = useAuth()

    const getUserEmail = async () => {
        try {
          const response = await axios.get(process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/users', {
            headers: {
              // @ts-ignore
              'X-XSRF-TOKEN': auth.getXsrfToken(),
            },
          });
      
          // Assuming response.data is a JSON object
          const userData = response.data;

          setUserEmail(userData['email'])
    
          // getCustomerInfo(authCustomerId)
        } catch (error) {
          toast.error(selectedLanguage === "EN" ?'Error fetching user info':'Erreur lors de la récupération des informations utilisateur.', {
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
      };

    useEffect(() => {
        getUserEmail();
    },[])

    const [selectedLanguage, setSelectedLanguage] = useState('');

    useEffect(() => {

        const language =  Cookies.get("MovingExpress_SelectedLanguage")
 
        if(language){
             // set selected Language
             setSelectedLanguage(language)
        } else {
             // set a cookie and usestate to english as default
             Cookies.set('MovingExpress_SelectedLanguage', 'EN', { expires: 7,path: '/' });
             setSelectedLanguage("EN")
        }
 
     },[])


  return (
    <div>
        <NormalNavBar />


        <ScrollToTopBtn />


        <div className="px-[5%] py-20">
                <div id='top' className="flex flex-col gap-3 pb-7">
                    <div
                        style={{ fontFamily: 'Bebas Neue, cursive' }}
                        className="text-3xl"
                    >
                        {selectedLanguage === "EN" ? 
                        <>Shipment
                        <span className="text-companyYellow"> Quote</span></>
                        : 
                        <>Devis
                        <span className="text-companyYellow"> d'expédition</span></>
                        }
                    </div>

                    <div className="pb-5 font-light text-sm opacity-90 lg:pr-[20%]">
                        {selectedLanguage === "EN" ? 
                            <span>
                                A shipment quote plays a vital role in ensuring the
                                smooth and organized movement of goods from the point of
                                origin to the destination. It helps both the shipper and
                                the carrier have a clear understanding of the logistics
                                involved, reducing the risk of errors, delays, and
                                misunderstandings during the transportation process.
                            </span>
                            : 
                            <span>
                                Une cotation d'expédition joue un rôle essentiel pour garantir le 
                                déplacement fluide et organisé des marchandises du point d'origine 
                                à la destination. Elle aide à ce que le chargeur et le transporteur 
                                aient une compréhension claire de la logistique impliquée, réduisant 
                                ainsi le risque d'erreurs, de retards et de malentendus pendant 
                                le processus de transport.
                            </span>
                        }
                    </div>

                    <hr className="border-1 border-companyYellow" />
                </div>

                <div>
                    <form id="quoteForm">
                        <div className="flex flex-col gap-2 mb-10">
                            <div
                                style={{ fontFamily: 'Bebas Neue, cursive' }}
                                className="text-xl"
                            >
                                {selectedLanguage === "EN" ? 
                                    <>
                                        Personal 
                                        <span className="text-companyYellow ml-1">
                                            information
                                        </span>
                                    </>
                                    : 
                                    <>
                                        informations 
                                        <span className="text-companyYellow ml-1">personnelles</span>
                                    </>
                                }
                            </div>

                            <div className="pb-5 font-light text-sm opacity-90">
                            {selectedLanguage === "EN" ? 
                                <>
                                    Please provide and fill in all your personal
                                    information in the section below.
                                </>
                                : 
                                <>
                                    Veuillez fournir et remplir toutes vos informations personnelles dans la section ci-dessous.
                                </>
                                }
                            </div>

                            <div className="bg-white shadow-lg border rounded-md flex flex-col gap-5 px-10 py-10">
                                <div className="flex flex-row flex-wrap gap-5">
                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="text"
                                            className="border border-[lightgray] text-sm h-[35px] px-4 rounded-sm"
                                            id="FirstNameInput"
                                            name="FirstNameInput"
                                            placeholder={selectedLanguage === "EN" ? "First Name" : "Prénom" }
                                            required
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "First Name" : "Prénom" }
                                        </label>
                                    </div>

                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="text"
                                            className="border border-[lightgray] text-sm h-[35px] px-4 rounded-sm"
                                            id="LastNameInput"
                                            name="LastNameInput"
                                            placeholder={selectedLanguage === "EN" ? "Last Name" : "Nom de Famille" }
                                            required
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "Last Name" : "Nom de Famille" }
                                        </label>
                                    </div>

                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="email"
                                            className="border border-[lightgray] text-sm h-[35px] px-4 rounded-sm"
                                            id="EmailInput"
                                            name="EmailInput"
                                            placeholder={selectedLanguage === "EN" ? "E-Mail" : "Courriel" }
                                            value={userEmail}
                                            readOnly
                                            required
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "E-Mail" : "Courriel" }
                                        </label>
                                    </div>

                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="text"
                                            className="border border-[lightgray] text-sm  h-[35px] px-4 rounded-sm py-1"
                                            name="PhoneNumberInput"
                                            id="PhoneNumberInput"
                                            placeholder={selectedLanguage === "EN" ? "Phone Number" : "Téléphone" }
                                            required
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "Phone Number" : "Téléphone" }
                                        </label>
                                    </div>
                                </div>

                                <div className="flex gap-5 flex-wrap">
                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="date"
                                            className="border border-[lightgray] w-[200px] rounded-sm px-4 py-1"
                                            name="MovingDateInput"
                                            id="MovingDateInput"
                                            required
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "Expected Moving Date" : "Date de déménagement" }
                                        </label>
                                    </div>

                                    <div className="text-[#696969] text-sm">
                                        <div>
                                            {selectedLanguage === "EN" ? "How to contact you ?" : "Comment vous contacter ?" }
                                        </div>

                                        <div className="flex gap-5">
                                            <label className="flex gap-1">
                                                <input
                                                    type="radio"
                                                    name="contact"
                                                    id="EmailValue"
                                                    value="email"
                                                />
                                                <span>{selectedLanguage === "EN" ? "E-Mail" : "Courriel" }</span>
                                            </label>

                                            <label className="flex gap-1">
                                                <input
                                                    type="radio"
                                                    name="contact"
                                                    id="PhoneNumberValue"
                                                    value="phonenumber"
                                                />
                                                <span>{selectedLanguage === "EN" ? "Phone Number" : "Téléphone" }</span>
                                            </label>

                                            <label className="flex gap-1">
                                                <input
                                                    type="radio"
                                                    name="contact"
                                                    id="BothValue"
                                                    value="both"
                                                />
                                                <span>{selectedLanguage === "EN" ? "Both" : "Les Deux" }</span>
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <div>
                                    <textarea
                                        className="w-[100%] border-[lightgray] h-[100px] border text-sm px-3 py-3 rounded-sm"
                                        placeholder={selectedLanguage === "EN" ? "Additional comments / instrcutions (Specific hours, crane required...)" : "Commentaires / Instructions supplémentaires (Heures spécifiques, grue nécessaire...)" }
                                        id="AdditionalCommentsInput"
                                        name="AdditionalCommentsInput"
                                    />
                                </div>
                            </div>
                        </div>

                        <div className="flex flex-col gap-2 mb-10">
                            <div
                                style={{ fontFamily: 'Bebas Neue, cursive' }}
                                className="text-xl"
                            >
                                {selectedLanguage === "EN" ? 
                                <>
                                    Pick Up 
                                    <span className="text-companyYellow ml-1">
                                        Location
                                    </span>
                                </> 
                                : 
                                <>
                                    Lieu de
                                    <span className="text-companyYellow ml-1">
                                    ramassage
                                    </span>
                                </> 
                                }
                            </div>

                            <div className="pb-5 font-light text-sm opacity-90">
                                {selectedLanguage === "EN" ? 
                                <>
                                    Please provide and fill in all the information
                                    about the pick up location in the section below.
                                </> 
                                : 
                                <>
                                    Veuillez fournir et remplir toutes les informations concernant le lieu de ramassage dans la section ci-dessous.
                                </> 
                                }
                            </div>

                            <div className="bg-white shadow-lg border rounded-md flex flex-col gap-5 px-10 py-10">
                                <div className="flex flex-col gap-1">
                                    <input
                                        type="text"
                                        className="border border-[lightgray] text-sm  h-[35px] px-4 rounded-sm py-1 w-[85%]"
                                        placeholder={selectedLanguage === "EN" ? "Pick up address" : "Adresse de ramassage" }
                                        id="PickUpAddressInput"
                                        name="PickUpAddressInput"
                                        required
                                    />
                                    <label
                                        style={{
                                            fontFamily: 'Bebas Neue, cursive',
                                        }}
                                        className="text-[#696969] text-sm"
                                    >
                                        {selectedLanguage === "EN" ? "Pick up address" : "Adresse de ramassage" }
                                    </label>
                                </div>

                                <div className="flex gap-5 flex-wrap">
                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="text"
                                            className="border border-[lightgray] text-sm  h-[35px] px-4 rounded-sm py-1 w-[100%]"
                                            id="CityInputP"
                                            name="CityInputP"
                                            placeholder={selectedLanguage === "EN" ? "City" : "Ville" }
                                            required
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "City" : "Ville" }
                                        </label>
                                    </div>

                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="text"
                                            className="border border-[lightgray] text-sm  h-[35px] px-4 rounded-sm py-1 w-[100%]"
                                            id="PostalCodeInputP"
                                            name="PostalCodeInputP"
                                            placeholder={selectedLanguage === "EN" ? "Postal Code" : "code Postal" }
                                            required
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "Postal Code" : "code Postal" }
                                        </label>
                                    </div>

                                    <div className="flex flex-col gap-1">
                                        <select
                                            id="CountrySelectInputP"
                                            className="border border-[lightgray] py-1 px-1"
                                        >
                                            <optgroup>
                                                <option
                                                    id="CanadaOptionP"
                                                    value="CA"
                                                >
                                                    CA
                                                </option>
                                                <option
                                                    id="USAOptionP"
                                                    value="USA"
                                                >
                                                    USA
                                                </option>
                                            </optgroup>
                                        </select>
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "Country" : "Pays" }
                                        </label>
                                    </div>
                                </div>

                                <div className="flex gap-5 flex-wrap">
                                    <div className="flex flex-col gap-1">
                                        <select
                                            id="BuildingTypeSelectInputP"
                                            className="border border-[lightgray] py-1 px-1 w-[200px]"
                                        >
                                            <optgroup>
                                                <option
                                                    id="nullP"
                                                    value="null"
                                                ></option>
                                                <option
                                                    id="HouseP"
                                                    value="house"
                                                >
                                                    House
                                                </option>
                                                <option
                                                    id="VillaP"
                                                    value="villa"
                                                >
                                                    Villa
                                                </option>
                                                <option
                                                    id="CondoP"
                                                    value="condo"
                                                >
                                                    Condo
                                                </option>
                                                <option
                                                    id="ApartmentP"
                                                    value="Apartment"
                                                >
                                                    Apartment
                                                </option>
                                            </optgroup>
                                        </select>
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "building type" : "Type de bâtiment" }
                                        </label>
                                    </div>

                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="number"
                                            className="border border-[lightgray] text-sm  h-[35px] px-4 rounded-sm py-1"
                                            placeholder="0"
                                            id="NumberOfRoomsInputP"
                                            name="NumberOfRoomsInputP"
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "No. of rooms" : "Nombre de pièces" }
                                        </label>
                                    </div>

                                    <div className="text-[#696969] text-sm">
                                        <div> {selectedLanguage === "EN" ? "Is there an elevator ?" : "Y a-t-il un ascenseur ?" }</div>

                                        <div className="flex gap-5">
                                            <label className="flex gap-1">
                                                <input
                                                    type="radio"
                                                    name="elevatorP"
                                                    id="YesOptionP"
                                                    value="yes"
                                                />
                                                <span>{selectedLanguage === "EN" ? "Yes" : "Oui" }</span>
                                            </label>

                                            <label className="flex gap-1">
                                                <input
                                                    type="radio"
                                                    name="elevatorP"
                                                    id="NoOptionP"
                                                    value="no"
                                                />
                                                <span>{selectedLanguage === "EN" ? "No" : "Non" }</span>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="flex flex-col gap-2 mb-10">
                            <div
                                style={{ fontFamily: 'Bebas Neue, cursive' }}
                                className="text-xl"
                            >
                                {selectedLanguage === "EN" ? 
                                <>
                                    Drop Off
                                    <span className="text-companyYellow ml-1">
                                        Destination
                                    </span>
                                </> 
                                : 
                                <>
                                    Destination de 
                                    <span className="text-companyYellow ml-1">
                                        livraison
                                    </span>
                                </>
                                
                                }
                            </div>

                            <div className="pb-5 font-light text-sm opacity-90">
                            {selectedLanguage === "EN" ? 
                                <>
                                   Please provide and fill in all the information
                                    about the drop off destination in the section
                                    below.
                                </> 
                                : 
                                <>
                                    Veuillez fournir et remplir toutes les informations concernant la destination de livraison dans la section ci-dessous.
                                </>
                            }
                            </div>

                            <div className="bg-white shadow-lg border rounded-md flex flex-col gap-5 px-10 py-10">
                                <div className="flex flex-col gap-1">
                                    <input
                                        type="text"
                                        className="border border-[lightgray] text-sm  h-[35px] px-4 rounded-sm py-1 w-[85%]"
                                        placeholder={selectedLanguage === "EN" ? "Drop Off address" : "Adresse de livraison" }
                                        name="DropOffAddressInput"
                                        id="DropOffAddressInput"
                                        required
                                    />
                                    <label
                                        style={{
                                            fontFamily: 'Bebas Neue, cursive',
                                        }}
                                        className="text-[#696969] text-sm"
                                    >
                                       {selectedLanguage === "EN" ? "Drop Off address" : "Adresse de livraison" }
                                    </label>
                                </div>

                                <div className="flex gap-5 flex-wrap">
                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="text"
                                            className="border border-[lightgray] text-sm  h-[35px] px-4 rounded-sm py-1 w-[100%]"
                                            placeholder={selectedLanguage === "EN" ? "City" : "Ville" }
                                            name="CityInputD"
                                            id="CityInputD"
                                            required
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "City" : "Ville" }
                                        </label>
                                    </div>

                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="text"
                                            className="border border-[lightgray] text-sm  h-[35px] px-4 rounded-sm py-1 w-[100%]"
                                            placeholder={selectedLanguage === "EN" ? "Postal Code" : "code Postal" }
                                            id="PostalCodeInputD"
                                            name="PostalCodeInputD"
                                            required
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "Postal Code" : "code Postal" }
                                        </label>
                                    </div>

                                    <div className="flex flex-col gap-1">
                                        <select
                                            id="CountrySelectInputD"
                                            className="border border-[lightgray] py-1 px-1"
                                        >
                                            <optgroup>
                                                <option
                                                    id="CanadaOptionD"
                                                    value="CA"
                                                >
                                                    CA
                                                </option>
                                                <option
                                                    id="USAOptionD"
                                                    value="USA"
                                                >
                                                    USA
                                                </option>
                                            </optgroup>
                                        </select>
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                           {selectedLanguage === "EN" ? "Country" : "Pays" }
                                        </label>
                                    </div>
                                </div>

                                <div className="flex gap-5 flex-wrap">
                                    <div className="flex flex-col gap-1">
                                        <select
                                            id="BuildingTypeSelectInputD"
                                            className="border border-[lightgray] py-1 px-1 w-[200px]"
                                        >
                                            <optgroup>
                                                <option
                                                    id="nullD"
                                                    value="null"
                                                ></option>
                                                <option
                                                    id="HouseD"
                                                    value="house"
                                                >
                                                    House
                                                </option>
                                                <option
                                                    id="VillaD"
                                                    value="villa"
                                                >
                                                    Villa
                                                </option>
                                                <option
                                                    id="CondoD"
                                                    value="condo"
                                                >
                                                    Condo
                                                </option>
                                                <option
                                                    id="ApartmentD"
                                                    value="Apartment"
                                                >
                                                    Apartment
                                                </option>
                                            </optgroup>
                                        </select>
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                           {selectedLanguage === "EN" ? "building type" : "Type de bâtiment" }
                                        </label>
                                    </div>

                                    <div className="flex flex-col gap-1">
                                        <input
                                            type="number"
                                            className="border border-[lightgray] text-sm  h-[35px] px-4 rounded-sm py-1"
                                            placeholder="0"
                                            name="NumberOfRoomsInputD"
                                            id="NumberOfRoomsInputD"
                                        />
                                        <label
                                            style={{
                                                fontFamily:
                                                    'Bebas Neue, cursive',
                                            }}
                                            className="text-[#696969] text-sm"
                                        >
                                            {selectedLanguage === "EN" ? "No. of rooms" : "Nombre de pièces" }
                                        </label>
                                    </div>

                                    <div className="text-[#696969] text-sm">
                                        <div>{selectedLanguage === "EN" ? "Is there an elevator ?" : "Y a-t-il un ascenseur ?" }</div>

                                        <div className="flex gap-5">
                                            <label className="flex gap-1">
                                                <input
                                                    type="radio"
                                                    name="elevatorD"
                                                    id="YesOptionD"
                                                    value="yes"
                                                />
                                                <span>{selectedLanguage === "EN" ? "Yes" : "Oui" }</span>
                                            </label>

                                            <label className="flex gap-1">
                                                <input
                                                    type="radio"
                                                    name="elevatorD"
                                                    id="NoOptionD"
                                                    value="no"
                                                />
                                                <span>{selectedLanguage === "EN" ? "No" : "Non" }</span>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div>
                            <div className="flex gap-5">
                                <div className="flex flex-col gap-1 w-[60%] sm:w-[40%] md:w-[30%]">
                                    <input
                                        style={{ height: '35px' }}
                                        type="text"
                                        className="border px-6 text-sm rounded-sm"
                                        placeholder={selectedLanguage === "EN" ? "Enter Shipment Name" : "Nom de l'expédition" }
                                        name="ShipmentNameInput"
                                        id="ShipmentNameInput"
                                        required
                                    />
                                    <label
                                        style={{
                                            fontFamily: 'Bebas Neue, cursive',
                                        }}
                                        className="text-[#696969]"
                                    >
                                        {selectedLanguage === "EN" ? "Shipment Name" : "Nom de l'expédition" }
                                    </label>
                                </div>

                                <div>
                                    <button
                                        type="submit"
                                        onClick={handleSubmit}
                                        className="text-white text-sm bg-companyYellow px-8 py-2 rounded-md shadow-md"
                                        style={{
                                            fontFamily: 'Bebas Neue, cursive',
                                        }}
                                        name="RequestQuoteBtn"
                                        id="RequestQuoteBtn"
                                    >
                                        {selectedLanguage === "EN" ? "send" : "envoyer" }
                                    </button>
                                    
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
    </div>
  )
}

export default UserShipmentQuotePage
