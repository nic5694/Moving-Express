import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useAuth } from '../auth/components/AuthService';
import Cookies from 'js-cookie';

function DriverProfile() {

    const[driverInfo, setDriverInfo] = useState("")

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
            setDriverInfo(userData);

        } catch (error) {
        console.error('Error fetching user info:', error);
      }
    }

    useEffect(() => {
        getUserInfo()
    },[])

    // ---------------------------------- Localization -----------------------------------------
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
    <div  className='flex justify-center px-[5%] mb-20 mt-20'>

        <div className="flex flex-col">
            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-2xl border-l-4 border-l-companyYellow leading-none pl-2'>{selectedLanguage === "EN" ? 'Driver ' : 'Profil du '} <span className='text-companyYellow  '>{selectedLanguage === "EN" ? 'Profile' : 'Conducteur'}  </span></div>


            <div className='border rounded-lg shadow-xl bg-white mt-5 flex justify-center py-14 md:py-20 px-18'>

                <div style={{alignItems: "center"}} className=' flex-col md:flex-row flex justify-center gap-10 md:gap-20 px-10'>
                    <div className='flex flex-col gap-3'>
                        
                        <div>
                            {/* @ts-ignore */}
                            <img src={driverInfo.profilePictureUrl} style={{alignItems: "center"}} className='w-[150px] h-[150px] bg-white border shadow-lg rounded-full' />
                        </div>
                        
                        <div style={{fontFamily:'Bebas Neue, cursive',}}className="text-[#696969] text-xs text-center">{selectedLanguage === "EN" ? "Profile picture" : "Photo de profil"}</div>
                    </div>

                    <div className='flex flex-col gap-3'>
                        
                        <div className='flex gap-2 flex-wrap'>

                            <div className="flex flex-col gap-1 w-[100%] md:w-[45%]">
                                <input
                                    type="text"
                                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                                    id="DetailQuoteId"
                                    name="DetailQuoteId"
                                    readOnly
                                    // @ts-ignore
                                    value={driverInfo.firstName || ""}
                                />
                                <label
                                    style={{
                                    fontFamily:
                                        'Bebas Neue, cursive',
                                    }}
                                    className="text-[#696969] text-xs"
                                >
                                    {selectedLanguage === "EN" ? "First name" : "Prénom"}
                                </label>
                            </div>

                            <div className="flex flex-col gap-1 w-[100%] md:w-[45%]">
                                <input
                                    type="text"
                                    className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                                    id="DetailQuoteId"
                                    name="DetailQuoteId"
                                    readOnly
                                    // @ts-ignore
                                    value={driverInfo.lastName || ""}
                                />
                                <label
                                    style={{
                                    fontFamily:
                                        'Bebas Neue, cursive',
                                    }}
                                    className="text-[#696969] text-xs"
                                >
                                    {selectedLanguage === "EN" ? "Last name" : "Nom de famille"}
                                </label>
                            </div>

                        </div>

                        <div className="flex flex-col gap-1 w-[100%] md:w-[92%]">
                            <input
                                type="text"
                                className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                                id="DetailQuoteId"
                                name="DetailQuoteId"
                                readOnly
                                // @ts-ignore
                                value={driverInfo.email || ""}
                            />
                            <label
                                style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                                }}
                                className="text-[#696969] text-xs"
                            >
                                {selectedLanguage === "EN" ? "E-mail Address" : "Adresse Courriel"}
                            </label>
                        </div>
                        
                        <div className="flex flex-col gap-1 w-[100%] md:w-[92%]">
                            <input
                                type="text"
                                className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                                id="DetailQuoteId"
                                name="DetailQuoteId"
                                readOnly
                                // @ts-ignore
                                value={driverInfo.phoneNumber || ""}
                            />
                            <label
                                style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                                }}
                                className="text-[#696969] text-xs"
                            >
                                {selectedLanguage === "EN" ? "Phone Number" : "téléphone"}
                            </label>
                        </div>
                    </div>
                </div>

            </div>
        </div>

    </div>
    
  )
}

export default DriverProfile