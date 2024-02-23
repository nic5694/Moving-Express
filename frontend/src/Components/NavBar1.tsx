import React, {useEffect, useState } from 'react'
import logo from '../Images/ME_Logo.png'
import { Link } from 'react-router-dom'
import Cookies from 'js-cookie'

function NavBar1() {

    const [selectedLanguage, setSelectedLanguage] = useState('');

    const handleSelectChange = (event:any) => {
        const newValue = event.target.value;

        // Update state with the selected value
        setSelectedLanguage(newValue);

        Cookies.set('MovingExpress_SelectedLanguage', newValue, { expires: 7,  path: '/' });

        window.location.reload();

    };

    useEffect(() => {

       const language =  Cookies.get("MovingExpress_SelectedLanguage")

       if(language){
            // set selected Language
            setSelectedLanguage(language)
       } else {
            // set a cookie and usestate to english as default
            Cookies.set('MovingExpress_SelectedLanguage', 'EN', { expires: 7, path: '/' });
            setSelectedLanguage("EN")
       }

    },[])

    return (
        <div>
            {/* Nav/Top section */}
            <div className="flex justify-between px-10 py-5 bg-[#2D2D2D] flex-wrap">
                {/* <a href='/' style={{fontFamily : "Bebas Neue, cursive"}} className="text-white text-lg" >
            
          </a> */}

                <Link
                    id='logoLink'
                    to={'/'}
                    style={{ fontFamily: 'Bebas Neue, cursive' }}
                    className="text-white text-lg"
                >
                    <img src={logo} className="w-24 2xl:w-32" />
                </Link>

                <div style={{alignItems: "center"}} className="flex gap-5">
                    
                    <div className='countryDiv text-white'>
                        <select  className='h-7 bg-transparent' id="selectLanguage" onChange={(e) => handleSelectChange(e)}>
                            <option className='text-black' selected={selectedLanguage == "EN"} value="EN">🇬🇧 EN</option>
                            <option className='text-black' selected={selectedLanguage == "FR"} value="FR">🇫🇷 FR</option>
                        </select>
                    </div>

                    <a
                        style={{ fontFamily: 'Bebas Neue, cursive' }}
                        id="signinsignupnavbar"
                        className="border-[3px] border-companyYellow px-7 py-2 rounded-md text-white"
                        href= {process.env.REACT_APP_BACKEND_URL + "oauth2/authorization/okta"}
                    >
                        {
                            selectedLanguage == "EN" ? 
                            <span>Login | Sign Up</span>
                            : 
                            <span>Se Connecter</span>
                        }
                    </a>
                   
                </div>
            </div>
        </div>
    )
}

export default NavBar1
