import Cookies from 'js-cookie';
import { emit } from 'process';
import React, { useEffect, useState } from 'react'

function ShipmentQuoteDetails(props:any) {

    const [isEstimatorPage, setIsEstimatorPage] = useState(props.isEstimatorPage)

    interface Quote {
        quoteId: string;
        quoteStatus: string;
        pickupStreetAddress: string;
        pickupCity: string;
        pickupCountry: string;
        pickupPostalCode: string;
        pickupNumberOfRooms: number;
        pickupElevator: boolean;
        pickupBuildingType: string;
        destinationStreetAddress: string;
        destinationCity: string;
        destinationCountry: string;
        destinationPostalCode: string;
        destinationNumberOfRooms: number;
        destinationElevator: boolean;
        destinationBuildingType: string;
        firstName: string;
        lastName: string;
        emailAddress: string;
        phoneNumber: string;
        contactMethod: string;
        expectedMovingDate: string;
        initiationDate: string;
        comment: string;
        name: string;
        approximateShipmentValue: number;
        approximateWeight: number;
    }

    let quoteDetails : Quote = props.shipmentQuote

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
    <div className=" overflow-y-scroll pr-10 h-[70vh]">

        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-sm mb-3 bg-companyYellow pl-2 text-white py-1'>
             {selectedLanguage === "EN" ? "Personal Information" : "informations personnelles" }
        </div>

        <div className='flex gap-3 flex-wrap'>
            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                    id="DetailName"
                    name="DetailName"
                    readOnly
                    value={quoteDetails.name || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN" ? "Shipment Name" : "Nom de l'expédition" }
                </label>
            </div>

            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm w-[]"
                    id="DetailQuoteId"
                    name="DetailQuoteId"
                    readOnly
                    value={quoteDetails.quoteId || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN" ? "Quote ID" : "ID du devis" }
                </label>
            </div>

            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm w-[]"
                    id="DetailFirstName"
                    name="DetailFirstName"
                    readOnly
                    value={quoteDetails.firstName || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                   {selectedLanguage === "EN" ? "First Name" : "Prénom" }
                </label>
            </div>

            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm w-[]"
                    id="DetailLastName"
                    name="DetailLastName"
                    readOnly
                    value={quoteDetails.lastName || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN" ? "Last Name" : "Nom de Famille" }
                </label>
            </div>

            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm w-[]"
                    id="DetailEmail"
                    name="DetailEmail"
                    readOnly
                    value={quoteDetails.emailAddress || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                   {selectedLanguage === "EN" ? "E-Mail" : "Courriel" }
                </label>
            </div>

            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm w-[]"
                    id="DetailPhoneNumber"
                    name="DetailPhoneNumber"
                    readOnly
                    value={quoteDetails.phoneNumber || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                   {selectedLanguage === "EN" ? "Phone Number" : "Téléphone" }
                </label>
            </div>

            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                    id="ExpectedMovingDate"
                    name="ExpectedMovingDate"
                    readOnly
                    value={quoteDetails.expectedMovingDate || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN" ? "Expected Moving Date" : "Date de déménagement" }
                </label>
            </div>

            <div className='flex flex-col'>
                <div className='text-[10px] font-bold opacity-70'>{selectedLanguage === "EN" ? "Contact Method" : "Méthode de contact"}</div>
                <div className="flex items-center gap-2">
                    <input
                    type="radio"
                    className="border border-[lightgray] text-xs h-[35px] rounded-sm"
                    id="PreferredEmail"
                    name="PreferredEmail"
                    required
                    readOnly
                    checked={quoteDetails.contactMethod === 'EMAIL'}
                    />
                    <label
                    style={{
                        fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                    >
                    {selectedLanguage === "EN" ? "E-Mail" : "Courriel" }
                    </label>

                    <input
                    type="radio"
                    className="border border-[lightgray] text-xs h-[35px] rounded-sm"
                    id="PreferredPhoneNumber"
                    name="PreferredPhoneNumber"
                    required
                    readOnly
                    checked={quoteDetails.contactMethod === 'PHONE_NUMBER'}
                    />
                    <label
                    style={{
                        fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                    >
                    {selectedLanguage === "EN" ? "Phone Number" : "Téléphone" }
                    </label>

                    <input
                    type="radio"
                    className="border border-[lightgray] text-xs h-[35px] rounded-sm"
                    id="PreferredBoth"
                    name="PreferredBoth"
                    required
                    readOnly
                    checked={quoteDetails.contactMethod === 'BOTH'}
                    />
                    <label
                    style={{
                        fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                    >
                    {selectedLanguage === "EN" ? "Both" : "Les Deux" }
                    </label>
                </div>
            </div>    

        </div>
        
        <div className="flex flex-col gap-1 mt-3">

            <textarea  className='border text-xs py-2 px-2' value={quoteDetails.comment || ''} readOnly></textarea>
            <label
            style={{
                fontFamily:
                'Bebas Neue, cursive',
            }}
            className="text-[#696969] text-xs"
            >
            {selectedLanguage === "EN" ? "Additionnal Comments" : "Commentaires supplémentaires" }
            </label>
        </div>

        {/* -------------------------------------------------------------------- */}

        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-sm mt-8 mb-3 bg-companyYellow pl-2 text-white py-1'>
            {selectedLanguage === "EN" ? "Pick Up Location" : "Lieu de ramassage" }
        </div>

        <div className='flex gap-3 flex-wrap'>
            <div className="flex flex-col gap-1">
                  <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                    id="PickupAddress"
                    name="PickupAddress"
                    required
                    readOnly
                    value={quoteDetails.pickupStreetAddress + ", " + quoteDetails.pickupCountry  || ''}
                  />
                  <label
                    style={{
                      fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                  >
                    {selectedLanguage === "EN" ? "Address" : "Addresse"}
                  </label>
            </div>

            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                    id="PickupCity"
                    name="PickupCity"
                    required
                    readOnly
                    value={quoteDetails.pickupCity || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN" ? "City" : "Ville" }
                </label>
            </div>
           
            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                    id="PickupPostalCode"
                    name="PickupPostalCode"
                    required
                    readOnly
                    value={quoteDetails.pickupPostalCode || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                   {selectedLanguage === "EN" ? "Postal Code" : "code Postal" }
                </label>
            </div>

            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                    id="PickupBuildingType"
                    name="PickupBuildingType"
                    required
                    readOnly
                    value={quoteDetails.pickupBuildingType || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN" ? "building type" : "Type de bâtiment" }
                </label>
            </div>

            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                    id="PickupNumberOfRooms"
                    name="PickupNumberOfRooms"
                    required
                    readOnly
                    value={quoteDetails.pickupNumberOfRooms || ''}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                   {selectedLanguage === "EN" ? "No. of rooms" : "Nombre de pièces" }
                </label>
            </div>

            <div className='flex flex-col'>
                <div className='text-[10px] font-bold opacity-70'>{selectedLanguage === "EN" ? "Is there an elevator ?" : "Y a-t-il un ascenseur ?" }</div>
                <div className="flex items-center gap-2">
                <input
                    type="radio"
                    className="border border-[lightgray] text-xs h-[35px] rounded-sm"
                    id="PickupYes"
                    name="PickupYes"
                    required
                    readOnly
                    checked={quoteDetails.pickupElevator === true}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN" ? "Yes" : "Oui" }
                </label>

                <input
                    type="radio"
                    className="border border-[lightgray] text-xs h-[35px] rounded-sm"
                    id="PickupNo"
                    name="PickupNo"
                    required
                    readOnly
                    checked={quoteDetails.pickupElevator === false}
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN" ? "No" : "Non" }
                </label>
                </div>
            </div>
                  
        </div>

        {/* -------------------------------------------------------------------- */}


        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-sm mt-5 mb-3 bg-companyYellow pl-2 text-white py-1'>
            {selectedLanguage === "EN" ? "Drop Off Destination" : " Destination de livraison" }
        </div>

        <div className='flex gap-3 flex-wrap'>

            <div className="flex flex-col gap-1">
                <input
                type="text"
                className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                id="DestinationAddress"
                name="DestinationAddress"
                required
                readOnly
                value={`${quoteDetails.destinationStreetAddress || ''}, ${quoteDetails.destinationCountry || ''}`}
                />
                <label
                style={{
                    fontFamily:
                    'Bebas Neue, cursive',
                }}
                className="text-[#696969] text-xs"
                >
                {selectedLanguage === "EN" ? "Address" : "Addresse"}
                </label>
            </div>

            <div className="flex flex-col gap-1">
            <input
                type="text"
                className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                id="DestinationCity"
                name="DestinationCity"
                required
                readOnly
                value={quoteDetails.destinationCity || ''}
            />
            <label
                style={{
                fontFamily:
                    'Bebas Neue, cursive',
                }}
                className="text-[#696969] text-xs"
            >
                {selectedLanguage === "EN" ? "City" : "Ville" }
            </label>
            </div>

            <div className="flex flex-col gap-1">
            <input
                type="text"
                className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                id="DestinationPostalCode"
                name="DestinationPostalCode"
                required
                readOnly
                value={quoteDetails.destinationPostalCode || ''}
            />
            <label
                style={{
                fontFamily:
                    'Bebas Neue, cursive',
                }}
                className="text-[#696969] text-xs"
            >
                {selectedLanguage === "EN" ? "Postal Code" : "code Postal" }
            </label>
            </div>

            <div className="flex flex-col gap-1">
            <input
                type="text"
                className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                id="DestinationBuildingType"
                name="DestinationBuildingType"
                required
                readOnly
                value={quoteDetails.destinationBuildingType || ''}
            />
            <label
                style={{
                fontFamily:
                    'Bebas Neue, cursive',
                }}
                className="text-[#696969] text-xs"
            >
                {selectedLanguage === "EN" ? "building type" : "Type de bâtiment" }
            </label>
            </div>
            
            <div className="flex flex-col gap-1">
                    <input
                      type="text"
                      className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                      id="DestinationNumberOfRooms"
                      name="DestinationNumberOfRooms"
                      required
                      readOnly
                      value={quoteDetails.destinationNumberOfRooms || ''}
                    />
                    <label
                      style={{
                        fontFamily:
                          'Bebas Neue, cursive',
                      }}
                      className="text-[#696969] text-xs"
                    >
                      {selectedLanguage === "EN" ? "No. of rooms" : "Nombre de pièces" }
                    </label>
            </div>

            <div className='flex flex-col'>
                  <div className='text-[10px] font-bold opacity-70'> {selectedLanguage === "EN" ? "Is there an elevator ?" : "Y a-t-il un ascenseur ?" }</div>
                  <div className="flex items-center gap-2">
                    <input
                      type="radio"
                      className="border border-[lightgray] text-xs h-[35px] rounded-sm"
                      id="DestinationYes"
                      name="DestinationYes"
                      required
                      readOnly
                      checked={quoteDetails.destinationElevator === true}
                    />
                    <label
                      style={{
                        fontFamily:
                          'Bebas Neue, cursive',
                      }}
                      className="text-[#696969] text-xs"
                    >
                      {selectedLanguage === "EN" ? "Yes" : "Oui" }
                    </label>

                    <input
                      type="radio"
                      className="border border-[lightgray] text-xs h-[35px] rounded-sm"
                      id="DestinationNo"
                      name="DestinationNo"
                      required
                      readOnly
                      checked={quoteDetails.destinationElevator === false}
                    />
                    <label
                      style={{
                        fontFamily:
                          'Bebas Neue, cursive',
                      }}
                      className="text-[#696969] text-xs"
                    >
                      {selectedLanguage === "EN" ? "No" : "Non" }
                    </label>
                  </div>
            </div>

        </div>

        {isEstimatorPage && quoteDetails.quoteStatus == "ACCEPTED" &&
            <>
                <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-sm mt-5 mb-3 bg-companyYellow pl-2 text-white py-1'>Estimations</div>

                <div className='flex gap-3 flex-wrap mb-4'>
                    <div className="flex flex-col gap-1">
              
                        <input
                            type="number"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm w-[]"
                            id="ApproxWeight"
                            name="ApproxWeight"
                            placeholder='0 lb'
                        />
                        
                        <label
                            style={{
                            fontFamily:
                                'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "Approx. Weight (lb)" : "Poids Approx." }
                        </label>
                    </div>

                    <div className="flex flex-col gap-1">
                        
                        <input
                            type="number"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm w-[]"
                            id="ApproxShipmentValue"
                            name="ApproxShipmentValue"
                            placeholder='0.00$'
                        />

                        <label
                            style={{
                            fontFamily:
                                'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                        {selectedLanguage === "EN" ? "Approx. Shipment Value ($)" : " Valeur approx. de l'expédition" }
                        </label>
                    </div>

                </div>
            </>
       }   

    </div>
  )
}

export default ShipmentQuoteDetails