import React, { useEffect, useState } from 'react'
import logo from '../Images/ME_Logo_Blck.png'
import Cookies from 'js-cookie';

function Footer() {

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
        <div className="bg-companyYellow gap-20  flex pl-[5%] py-[75px]">
            <div className="hidden md:block">
                <img src={logo} className="w-52" />
            </div>

            <div className="flex flex-wrap gap-10 mt-5 ">
                <div className="flex flex-col text-white">
                    <div
                        style={{ fontFamily: 'Bebas Neue, cursive' }}
                        className="text-lg"
                    >
                        {selectedLanguage === "EN" ? 'Account' : 'Compte'}
                    </div>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                </div>
                <div className="flex flex-col text-white">
                    <div
                        style={{ fontFamily: 'Bebas Neue, cursive' }}
                        className="text-lg"
                    >
                        {selectedLanguage === "EN" ? 'Support' : 'Support'}
                    </div>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                </div>

                <div className="flex flex-col text-white">
                    <div
                        style={{ fontFamily: 'Bebas Neue, cursive' }}
                        className="text-lg"
                    >
                        {selectedLanguage === "EN" ? 'Explore' : 'Explorer'}
                    </div>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                </div>

                <div className="flex flex-col text-white">
                    <div
                        style={{ fontFamily: 'Bebas Neue, cursive' }}
                        className="text-lg"
                    >
                        {selectedLanguage === "EN" ? 'Location' : 'Emplacement'}
                    </div>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                    <a href="">{selectedLanguage === "EN" ? 'link' : 'Lien'}</a>
                </div>
            </div>
        </div>
    )
}

export default Footer
