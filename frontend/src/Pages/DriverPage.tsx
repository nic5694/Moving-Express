import Cookies from 'js-cookie';
import React, { useEffect, useState } from 'react'
import logo from '../Images/ME_Logo.png'
import { Link } from 'react-router-dom';
import UnassignedShipments from '../Components/UnassignedShipments';
import AssignedShipments from '../Components/AssignedShipments';
import DriverProfile from '../Components/DriverProfile';

function DriverPage() {

    const [menuOpen, setMenuOpen] = useState(false)

    const menuIcon = () => {
        return (
            <svg className='pt-1' width="25" viewBox="0 0 89 75" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect width="89" height="13" fill="white" />
                <rect y="31" width="89" height="13" fill="white" />
                <rect y="62" width="89" height="13" fill="white" />
            </svg>
        )
    }

    // ------------------------------ Page Switching ------------------------------------

    const [currentPage, setCurrentPage] = useState("unassigned_section")


    // ------------------------------ Localization --------------------------------------


    const [selectedLanguage, setSelectedLanguage] = useState('');

    const handleSelectChange = (event:any) => {
        const newValue = event.target.value;
  
        // Update state with the selected value
        setSelectedLanguage(newValue);
  
        Cookies.set('MovingExpress_SelectedLanguage', newValue, { expires: 7 ,  path: '/'  });
  
        window.location.reload();
  
    };
  
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

        {/* container that makes the side bar next to the content (flex when the size of the screen is large) */}

        <div className='lg:flex'>

            {/* Side Bar */}
            <>
                <div className='w-[300px] bg-[#2D2D2D] h-[100vh] hidden lg:block py-10 lg:flex justify-center'>
                <div className='flex flex-col text-white'>
                    <div style={{alignItems: "center"}} className='flex flex-row gap-5'>
                        <img src={logo} alt="logo" className="w-24 2xl:w-32" />
                        <div className='countryDiv hidden lg:block'>
                            <select  className='bg-transparent h-7 text-white' id="selectLanguage" onChange={(e) => handleSelectChange(e)}>
                                <option className='text-black' selected={selectedLanguage == "EN"} value="EN">🇬🇧 EN</option>
                                <option className='text-black' selected={selectedLanguage == "FR"} value="FR">🇫🇷 FR</option>
                            </select>
                        </div>
                    </div>

                    <br />

                    <div style={{ fontFamily: 'Bebas Neue, cursive', letterSpacing: "1px" }} className='text-2xl border-l-4 leading-none pl-2 mt-10 border-l-companyYellow'>
                        {selectedLanguage === "EN" ? "Driver" : "Conducteur"}
                    </div>
                        
                    <div className='pt-10'>
                    <button  onClick={() => {setCurrentPage("unassigned_section")}} className={currentPage === "unassigned_section" ? "opacity-100" : "opacity-30 hover:underline hover:opacity-100 duration-300 ease-in-out underline-offset-4 decoration-2 decoration-companyYellow"} style={{ fontFamily: 'Bebas Neue, cursive', letterSpacing: "1px" }}>
                        {selectedLanguage === "EN" ? "Unassigned Shipments" : "Expéditions non assignées"}
                    </button>
                    </div>

                    <div className='pt-2'>
                    <button  onClick={() => {setCurrentPage("assigned_section")}} className={currentPage === "assigned_section" ? "opacity-100" : "opacity-30 hover:underline hover:opacity-100 duration-300 ease-in-out underline-offset-4 decoration-2 decoration-companyYellow"} style={{ fontFamily: 'Bebas Neue, cursive', letterSpacing: "1px" }}>
                        {selectedLanguage === "EN" ? "My Shipments" : "Mes Expéditions"}
                    </button>
                    </div>

                    <div className='pt-2'>
                    <button onClick={() => {setCurrentPage("Profile")}} className={currentPage === "Profile" ? "opacity-100" : "opacity-30 hover:underline hover:opacity-100 duration-300 ease-in-out underline-offset-4 decoration-2 decoration-companyYellow"} style={{ fontFamily: 'Bebas Neue, cursive', letterSpacing: "1px" }}>
                        {selectedLanguage === "EN" ? "Profile" : "Profil"}
                    </button>
                    </div>

                    <div className='pt-10'>
                    <form action={process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/logout`} method="POST">
                        <button className=' bg-companyYellow px-3 py-1 rounded text-black hover:px-8 duration-300 ease-in-out' style={{ fontFamily: 'Bebas Neue, cursive' }} type={'submit'} >
                        {selectedLanguage === "EN" ? "Log out" : "déconnexion"}
                        </button>
                    </form>
                    </div>
                </div>
                </div>

                {/* phone version nav bar */}
                <div style={{ alignItems: "center" }} className="px-5 py-4 flex justify-between lg:hidden bg-[#2D2D2D]">

                <div>
                    <img src={logo} alt="logo" className="w-24 2xl:w-32" />
                </div>

                <div style={{alignItems: "center"}} className='flex gap-5'>
                    <div className='countryDiv'>
                        <select  className='h-7 bg-[rgb(0,0,0,0)] text-white' id="selectLanguage" onChange={(e) => handleSelectChange(e)}>
                            <option className='text-black' selected={selectedLanguage == "EN"} value="EN">🇬🇧 EN</option>
                            <option className='text-black' selected={selectedLanguage == "FR"} value="FR">🇫🇷 FR</option>
                        </select>
                    </div>
                    <button onClick={() => { setMenuOpen(!menuOpen) }}>
                    {menuIcon()}
                    </button>
                </div>

                </div>
                    <div className='absolute lg:hidden text-[gray] z-10' style={menuOpen ? { width: "100%", height: "100%", overflow: "hidden" } : { height: "0", overflow: "hidden" }}>
                    <div className='bg-white shadow-2xl'>
                        <div onClick={() => {setCurrentPage("unassigned_section"); setMenuOpen(!menuOpen)}}>
                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='px-5 py-2 border-b bg-white hover:bg-companyYellow hover:text-[#2D2D2D] hover:pl-10 duration-300 ease-in-out '>{selectedLanguage === "EN" ? "Unassigned Shipments" : "Expéditions non assignées"}</div>
                        </div>

                        <div onClick={() => {setCurrentPage("assigned_section"); setMenuOpen(!menuOpen)}}>
                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='px-5 py-2 border-b bg-white hover:bg-companyYellow hover:text-[#2D2D2D] hover:pl-10 duration-300 ease-in-out '>{selectedLanguage === "EN" ? "My Shipments" : "Mes Expéditions"}</div>
                        </div>

                        <div onClick={() => {setCurrentPage("Profile"); setMenuOpen(!menuOpen)}} >
                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='px-5 py-2 border-b bg-white hover:bg-companyYellow hover:text-[#2D2D2D] hover:pl-10 duration-300 ease-in-out '>{selectedLanguage === "EN" ? "Profile" : "Profil"}</div>
                        </div>

                        <form action={process.env.REACT_APP_BACKEND_URL + `api/v1/movingexpress/logout`} method="POST">
                            <div> 
                                <button className='w-full text-start'  type={'submit'} >
                                    <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='px-5 py-2 border-b bg-white hover:bg-companyYellow hover:text-[#2D2D2D] hover:pl-10 duration-300 ease-in-out '>{selectedLanguage === "EN" ? "Log out" : "déconnexion"}</div>
                                </button>
                            </div>
                        </form>
                    </div>
                    </div>
            </>

            {/* Components Rendering Container */}
            <div className='w-[100%]'>

                {
                    currentPage === "unassigned_section" ? 

                    <UnassignedShipments/>

                    : currentPage === "assigned_section" ? 

                    <AssignedShipments />

                    : currentPage === "Profile" && 

                    <DriverProfile />
                }

            </div>
        
        </div>


    </div>
  )
}

export default DriverPage