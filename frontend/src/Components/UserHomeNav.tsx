import React, {useEffect, useState} from 'react'
import logo from '../Images/ME_Logo.png'
import {Link} from 'react-router-dom'
import {AuthProvider, useAuth} from '../auth/components/AuthService'
import axios from 'axios'
import Cookies from 'js-cookie'
import { toast } from 'react-toastify'

function UserHomeNav(props: any) {

    const [username, setUserName] = useState('')
    const [profilePictureURL, setProfilePictureURL] = useState('')
    const logoutURL = process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/logout'

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

            const firstName = userData.firstName;
            const profilePictureURL = userData.profilePictureUrl;
            setUserName(firstName)
            setProfilePictureURL(profilePictureURL)
        } catch (error) {
            toast.error(selectedLanguage === "EN" ?'Error fetching user info' : `Erreur lors de la récupération des informations utilisateur.`, {
                position: 'top-right',
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined
            });
            
        }
    };

    useEffect(() => {
        getUserInfo()
    }, [])

    const [menuOpen, setMenuOpen] = useState(false)

    const menuIcon = () => {
        return (
            <svg className='pt-1' width="25" viewBox="0 0 89 75" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect width="89" height="13" fill="white"/>
                <rect y="31" width="89" height="13" fill="white"/>
                <rect y="62" width="89" height="13" fill="white"/>
            </svg>
        )
    }

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

            {/* phone version nav bar */}
            <div style={menuOpen ? {alignItems: "center", backgroundColor: "#2D2D2D"} : {alignItems: "center"}}
                 className="px-5 py-4 flex justify-between lg:hidden ">

                <div>
                    <img src={logo} alt="logo" className="w-24 2xl:w-32"/>
                </div>

                <div className='flex gap-5'>
                    <div className='countryDiv text-white'>
                        <select className='h-7 bg-transparent' id="selectLanguage"
                                onChange={(e) => handleSelectChange(e)}>
                            <option className='text-black' selected={selectedLanguage == "EN"} value="EN">🇬🇧 EN</option>
                            <option className='text-black' selected={selectedLanguage == "FR"} value="FR">🇫🇷 FR</option>
                        </select>
                    </div>
                    <button onClick={() => {
                        setMenuOpen(!menuOpen)
                    }}>
                        {menuIcon()}
                    </button>
                </div>

            </div>

            <div className='absolute lg:hidden text-[gray]'
                 style={menuOpen ? {width: "100%", height: "100%", overflow: "hidden"} : {
                     height: "0",
                     overflow: "hidden"
                 }}>
                <div className='bg-white shadow-2xl'>
                    <Link to={"/Home"}>
                        <div style={{fontFamily: 'Bebas Neue, cursive'}}
                             className='px-5 py-2 border-b bg-white hover:bg-companyYellow hover:text-[#2D2D2D] hover:pl-10 duration-300 ease-in-out '>{selectedLanguage === "EN" ? "Home" : "Accueil"}</div>
                    </Link>
                    <Link to={"/UserShipmentQuote"}>
                        <div style={{fontFamily: 'Bebas Neue, cursive'}}
                             className='px-5 py-2 border-b bg-white hover:bg-companyYellow hover:text-[#2D2D2D] hover:pl-10 duration-300 ease-in-out '>{selectedLanguage === "EN" ? "Shipment Quote" : "Devis d'expédition"}</div>
                    </Link>
                    <Link to={"/Shipments"}>
                        <div style={{fontFamily: 'Bebas Neue, cursive'}}
                             className='px-5 py-2 border-b bg-white hover:bg-companyYellow hover:text-[#2D2D2D] hover:pl-10 duration-300 ease-in-out '>{selectedLanguage === "EN" ? "Shipments" : "Expéditions"}</div>
                    </Link>
                    <Link to={"/ContactUs"}>
                        <div style={{fontFamily: 'Bebas Neue, cursive'}}
                             className='px-5 py-2 border-b bg-white hover:bg-companyYellow hover:text-[#2D2D2D] hover:pl-10 duration-300 ease-in-out '>{selectedLanguage === "EN" ? "Contact Us" : "Contact Nous"}</div>
                    </Link>
                    <Link to={"/Profile"}>
                        <div style={{fontFamily: 'Bebas Neue, cursive'}}
                             className='px-5 py-2 border-b bg-white hover:bg-companyYellow hover:text-[#2D2D2D] hover:pl-10 duration-300 ease-in-out '>{selectedLanguage === "EN" ? "Profile" : "Profil"}</div>
                    </Link>

                    <form action={logoutURL} method={"post"}>
                    <button type='submit' style={{fontFamily: 'Bebas Neue, cursive'}}
                            className='px-5 py-2 border-b bg-white text-left hover:bg-companyYellow w-[100%] hover:text-[#2D2D2D] hover:pl-10 duration-300 ease-in-out '>{selectedLanguage === "EN" ? "Log Out" : "Déconnexion"}</button>
                </form>
            </div>
        </div>

    {/* normal version nav bar */
    }
    <div style={{alignItems: "center"}} className='hidden lg:block px-5 py-5 pt-10 lg:flex justify-between text-white'>
        <div><img src={logo} alt="logo" className="w-24 2xl:w-32"/></div>

        <div style={{letterSpacing: "1px"}} className='flex flex-row gap-10 text-sm pl-24'>
            <div style={{fontFamily: 'Bebas Neue, cursive'}}><Link
                className='hover:underline underline-offset-4 decoration-2 decoration-companyYellow'
                to={"/Home"}>{selectedLanguage === "EN" ? "Home" : "Accueil"}</Link></div>
            <div style={{fontFamily: 'Bebas Neue, cursive'}}><Link
                className='hover:underline underline-offset-4 decoration-2 decoration-companyYellow'
                to={"/UserShipmentQuote"}>{selectedLanguage === "EN" ? "Shipment Quote" : "Devis d'expédition"}</Link>
            </div>
            <div style={{fontFamily: 'Bebas Neue, cursive'}}><Link
                className='hover:underline underline-offset-4 decoration-2 decoration-companyYellow'
                to={"/Shipments"}>{selectedLanguage === "EN" ? "Shipments" : "Expéditions"}</Link></div>
            <div style={{fontFamily: 'Bebas Neue, cursive'}}><Link
                className='hover:underline underline-offset-4 decoration-2 decoration-companyYellow'
                to={"/ContactUs"}>{selectedLanguage === "EN" ? "Contact Us" : "Contact Nous"}</Link></div>
        </div>

        <div style={{alignItems: "center"}} className='flex flex-row gap-5 text-sm'>

            <div style={{alignItems: "center"}} className='flex gap-3'>

                <div>
                    <img src={profilePictureURL} className='rounded-full w-[30px]'/>
                </div>

                <div style={{fontFamily: 'Bebas Neue, cursive', letterSpacing: "1px"}}>
                    <Link to={'/Profile'} className='hover:text-companyYellow hover:underline'>{username}</Link>
                </div>
            </div>

            <div className='countryDiv text-white'>
                <select className='h-7 bg-transparent' id="selectLanguage" onChange={(e) => handleSelectChange(e)}>
                    <option className='text-black' selected={selectedLanguage == "EN"} value="EN">🇬🇧 EN</option>
                    <option className='text-black' selected={selectedLanguage == "FR"} value="FR">🇫🇷 FR</option>
                </select>
            </div>

            <form
                action={logoutURL}
                method="post"
            >
                <button type='submit' style={{fontFamily: 'Bebas Neue, cursive'}}
                        className='border-[2px] py-1 px-5 rounded-sm border-companyYellow text-white'>{selectedLanguage === "EN" ? "Log Out" : "Déconnexion"}</button>
            </form>
        </div>


    </div>


</div>
)
}

export default UserHomeNav