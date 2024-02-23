import Cookies from 'js-cookie';
import React, { useEffect, useState } from 'react'

function ScrollToTopBtn() {

    const [selectedLanguage, setSelectedLanguage] = useState('');

    useEffect(() => {

        const language =  Cookies.get("MovingExpress_SelectedLanguage")
 
        if(language){
             // set selected Language
             setSelectedLanguage(language)
        } else {
             // set a cookie and usestate to english as default
             Cookies.set('MovingExpress_SelectedLanguage', 'EN', { expires: 7 });
             setSelectedLanguage("EN")
        }
 
     },[])

    const generateGoToTopBtnDesign = () => {
        return (
            <div style={{transform: "rotate(180deg)", opacity: "75%"}}>
                <svg width="30" height="30" viewBox="0 0 133 134" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ maxWidth: '100%', maxHeight: '100%' }}>
                    <path d="M64.5251 96.4749C65.892 97.8417 68.108 97.8417 69.4749 96.4749L91.7487 74.201C93.1156 72.8342 93.1156 70.6181 91.7487 69.2513C90.3819 67.8844 88.1658 67.8844 86.799 69.2513L67 89.0503L47.201 69.2513C45.8342 67.8844 43.6181 67.8844 42.2513 69.2513C40.8844 70.6181 40.8844 72.8342 42.2513 74.201L64.5251 96.4749ZM63.5 40L63.5 94L70.5 94L70.5 40L63.5 40Z" fill="black"/>
                </svg>
            </div>
        );
    }

  return (
    <div className="flex justify-end mr-[3%] ">
        <button onClick={() => {window.scrollTo({ top: 0, behavior: 'smooth' });}} style={{alignItems: "center"}} className="fixed flex justify-center text-white z-10 bg-companyYellow rounded-full w-10 10 h-10 bottom-14 opacity-50 transition duration-500 hover:opacity-100 ease-in-out mr-1 shadow-md border">
            {generateGoToTopBtnDesign()}
        </button>
        <div className='fixed flex justify-center z-10 h-10 text-[10px] bottom-3 font-semibold opacity-60'>
            {selectedLanguage === "EN" ? "Back To Top" : "Haut De Page"}
        </div>
    </div>  
  )
}

export default ScrollToTopBtn