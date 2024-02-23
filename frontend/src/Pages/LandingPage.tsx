import React, { useEffect, useState } from 'react';
import logo from '../Images/ME_Logo.png'
import { Link } from 'react-router-dom'
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Bounce, toast } from 'react-toastify'
import Cookies from 'js-cookie'


function LandingPage() {
    let url = process.env.REACT_APP_BACKEND_URL + 'oauth2/authorization/okta'
    const [observerCode, setObserverCode] = useState('');
    const navigate = useNavigate();
    const [infohidden,setInfoHidden] = useState(false)
    const [optionsPosition, setOptionsPosition] = useState({ top: 0, left: 0 });
  
    const handleObserverCodeSubmit = async () => {
        if (!observerCode) {
          
            alert(selectedLanguage === "EN" ? "Please enter the observer code." : "Veuillez entrer le code de l'observateur.");

            return;
        }
        try {
            
          const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}api/v1/movingexpress/shipments/observer/${observerCode}`);
          // Redirect to ObserverShipmentDetails with the shipment details
          navigate(`/ObserverShipmentDetails/${observerCode}`);
        } catch (error) {
            
                
            alert(selectedLanguage === "EN" ? 'Failed to fetch shipment details. Please check the observer code and try again.' : "Échec de récupération des détails d'expédition. Veuillez vérifier le code de l'observateur et réessayer.");

        }
    };

    const generateInfoIcon = () => {
        return (
            <svg width="25" viewBox="0 0 88 88" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M44 0.25C55.6041 0.25 66.7329 4.85969 74.9382 13.065C83.1435 21.2703 87.7532 32.3991 87.7532 44.0031C87.7532 55.6072 83.1435 66.736 74.9382 74.9413C66.7329 83.1466 55.6041 87.7563 44 87.7563C32.396 87.7563 21.2672 83.1466 13.0619 74.9413C4.85658 66.736 0.246887 55.6072 0.246887 44.0031C0.246887 32.3991 4.85658 21.2703 13.0619 13.065C21.2672 4.85969 32.396 0.25 44 0.25ZM50.5625 27.1125C53.8125 27.1125 56.45 24.8563 56.45 21.5125C56.45 18.1688 53.8063 15.9125 50.5625 15.9125C47.3125 15.9125 44.6875 18.1688 44.6875 21.5125C44.6875 24.8563 47.3125 27.1125 50.5625 27.1125ZM51.7063 62.2813C51.7063 61.6125 51.9375 59.875 51.8063 58.8875L46.6688 64.8C45.6063 65.9188 44.275 66.6938 43.65 66.4875C43.3665 66.3832 43.1295 66.181 42.9817 65.9175C42.8339 65.654 42.785 65.3464 42.8438 65.05L51.4063 38C52.1063 34.5688 50.1813 31.4375 46.1 31.0375C41.7938 31.0375 35.4563 35.4063 31.6 40.95C31.6 41.6125 31.475 43.2625 31.6063 44.25L36.7375 38.3313C37.8 37.225 39.0375 36.4438 39.6625 36.6563C39.9705 36.7668 40.2228 36.9936 40.3653 37.2881C40.5079 37.5826 40.5293 37.9212 40.425 38.2313L31.9375 65.15C30.9563 68.3 32.8125 71.3875 37.3125 72.0875C43.9375 72.0875 47.85 67.825 51.7125 62.2813H51.7063Z" fill="#F1C551"/>
            </svg>
        )
    }

    const generatePadlockIcon = () => {
        return (
            <svg className='opacity-40' width="13" viewBox="0 0 68 84" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M34 46.1667C32.7314 46.1518 31.4888 46.526 30.4393 47.2388C29.3898 47.9515 28.5839 48.9687 28.13 50.1534C27.6761 51.338 27.5961 52.6333 27.9006 53.8648C28.2051 55.0964 28.8796 56.2051 29.8333 57.0417V62.8333C29.8333 63.9384 30.2723 64.9982 31.0537 65.7796C31.8351 66.561 32.8949 67 34 67C35.105 67 36.1648 66.561 36.9462 65.7796C37.7276 64.9982 38.1666 63.9384 38.1666 62.8333V57.0417C39.1203 56.2051 39.7948 55.0964 40.0993 53.8648C40.4039 52.6333 40.3238 51.338 39.8699 50.1534C39.416 48.9687 38.6101 47.9515 37.5606 47.2388C36.5111 46.526 35.2685 46.1518 34 46.1667ZM54.8333 29.5V21.1667C54.8333 15.6413 52.6384 10.3423 48.7313 6.43528C44.8243 2.52827 39.5253 0.333336 34 0.333336C28.4746 0.333336 23.1756 2.52827 19.2686 6.43528C15.3616 10.3423 13.1666 15.6413 13.1666 21.1667V29.5C9.85142 29.5 6.672 30.817 4.32779 33.1612C1.98359 35.5054 0.666626 38.6848 0.666626 42V71.1667C0.666626 74.4819 1.98359 77.6613 4.32779 80.0055C6.672 82.3497 9.85142 83.6667 13.1666 83.6667H54.8333C58.1485 83.6667 61.3279 82.3497 63.6721 80.0055C66.0163 77.6613 67.3333 74.4819 67.3333 71.1667V42C67.3333 38.6848 66.0163 35.5054 63.6721 33.1612C61.3279 30.817 58.1485 29.5 54.8333 29.5ZM21.5 21.1667C21.5 17.8515 22.8169 14.672 25.1611 12.3278C27.5053 9.98363 30.6848 8.66667 34 8.66667C37.3152 8.66667 40.4946 9.98363 42.8388 12.3278C45.183 14.672 46.5 17.8515 46.5 21.1667V29.5H21.5V21.1667ZM59 71.1667C59 72.2717 58.561 73.3316 57.7796 74.113C56.9982 74.8944 55.9384 75.3333 54.8333 75.3333H13.1666C12.0616 75.3333 11.0017 74.8944 10.2203 74.113C9.43895 73.3316 8.99996 72.2717 8.99996 71.1667V42C8.99996 40.8949 9.43895 39.8351 10.2203 39.0537C11.0017 38.2723 12.0616 37.8333 13.1666 37.8333H54.8333C55.9384 37.8333 56.9982 38.2723 57.7796 39.0537C58.561 39.8351 59 40.8949 59 42V71.1667Z" fill="black"/>
            </svg>
        )
    }

    // ------------------------------- Localization ------------------------------------------------
    const [selectedOption, setSelectedOption] = useState('');
    const [selectedLanguage, setSelectedLanguage] = useState('');

    const handleSelectChange = (event:any) => {
        const newValue = event.target.value;

        // Update state with the selected value
        setSelectedLanguage(newValue);

        Cookies.set('MovingExpress_SelectedLanguage', newValue, { expires: 7 ,  path: '/' });

    };

    useEffect(() => {

       const language = Cookies.get("MovingExpress_SelectedLanguage")

       if(language){
            // set selected Language
            setSelectedLanguage(language)
       } else {
            // set a cookie and usestate to english as default
            Cookies.set('MovingExpress_SelectedLanguage', 'EN', { expires: 7,path: '/' });
            setSelectedLanguage("EN")
       }

    },[])


    const info = () => {
        
        if(selectedLanguage === "EN") {
            toast.info('Enter the observer code provided by your client to access the desired shipment.', {
                position: "top-right",
                autoClose: 10000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
                transition: Bounce,
            });
        } else if (selectedLanguage === "FR") {
            toast.info("Veuillez saisir le code d'observateur fourni par votre client pour accéder à l'expédition souhaitée.", {
                position: "top-right",
                autoClose: 10000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
                transition: Bounce,
            });
        }
    }
    
    return (
        <div className="LandingPContainer">
            
            <div className='blackGradient' style={{backdropFilter: "blur(2px)"}}>
                {/* Nav/Top section */}
                <div className="flex flex-wrap justify-between px-5 py-10">
                    <div
                        style={{ fontFamily: 'Bebas Neue, cursive' }}
                        className="text-white text-lg"
                    >
                        <img src={logo} className="w-24 2xl:w-32" />
                    </div>

                    <div style={{alignItems: "center"}} className='flex gap-5 countryDiv'> 
                        
                    <div className='countryDiv text-white'>
                        <select  className='h-7 bg-transparent' id="selectLanguage" onChange={(e) => handleSelectChange(e)}>
                            <option className='text-black' selected={selectedLanguage == "EN"} value="EN">🇬🇧 EN</option>
                            <option className='text-black' selected={selectedLanguage == "FR"} value="FR">🇫🇷 FR</option>
                        </select>
                    </div>
                        
                        <a
                            style={{ fontFamily: 'Bebas Neue, cursive' }}
                            id="signinsignuplandingpage"
                            className="border-[3px] border-companyYellow px-7 py-2 rounded-md text-white flex"
                            href={url}
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

                <div className="pb-12 md:pb-20 pt-12">
                    <div className="flex flex-col gap-5 w-[100%]">
                        <div
                            style={{ fontFamily: 'Bebas Neue, cursive' }}
                            className="text-white text-[4rem] md:text-7xl lg:text-[70px] xl:text-[80px] md:text-center tracking-[3px] leading-tight px-5"
                        >
                            <div className='leading-none'>

                                {selectedLanguage == "EN" ? 
                                <span>Move Faster with Us</span>
                                : 
                                <span>Déménager rapidement avec</span>}

                            </div>
                            <div className="text-companyYellow leading-none">
                                Moving Express
                            </div>
                        </div>

                        <div className='flex md:justify-center'>
                            <div style={{ fontFamily: 'Inter, cursive'  }} className="text-white px-5 pr-10 text-sm mb-2 xl:text-lg md:text-center md:w-[45%] md:px-0">
                                {selectedLanguage == "EN" ? 
                                <span>Welcome to Moving express If you do not have an account but desire to request a quote click this link</span>
                                : 
                                <span>Bienvenue chez Moving Express. Si vous n'avez pas de compte mais souhaitez demander un devis, cliquez sur ce lien</span>}
                                <Link id="shipmentQuoteBtn"
                                className="text-companyYellow ml-2 animate-pulse hover:underline duration-300 ease-in-out"
                                to={'/ShipmentQuote'}>
                                    {selectedLanguage == "EN" ? 
                                    <span>shipment quote</span>
                                    : 
                                    <span>Devis d'expédition</span>}
                                </Link>.
                            </div>
                        </div>
                    </div>

                    
                </div>
                
                <div className="px-5 md:pl-0 flex md:justify-center pb-[130px] ">
                    
                    <div className="bg-white px-3 py-3 rounded w-[100%] md:mx-[20%] xl:mx-[30%]">

                        <div className="flex flex-col md:flex-row gap-5 w-[100%]">
                            <div style={{alignItems: "center"}} className='flex flex-row gap-3 w-[100%] md:w-[65%] lg:w-[70%]'>

                                <div>{generatePadlockIcon()}</div>

                                <input
                                    style={{
                                        fontFamily: 'Bebas Neue, cursive',
                                    }}
                                    type="text"
                                    className="border-b pl-3 py-1 w-[100%] md:w-[300px] lg:w-[450px] border-b-[lightgray] focus:tracking-[5px] text-[rgba(0,0,0,0.6)]"
                                    placeholder={selectedLanguage === "EN" ? 'Observer Code' : "Code d'observateur"}
                                    value={observerCode}
                                    onChange={(e) => setObserverCode(e.target.value)}
                                />
                                
                                

                                <button onClick={()=>{info()}} className='rounded-full bg-white shadow-md animate-pulse'>{generateInfoIcon()}</button>
                            </div>

                            <div className='flex w-[100%] md:w-[40%]'>
                                <button
                                    style={{
                                        fontFamily: 'Bebas Neue, cursive',
                                    }}
                                    className="text-white text-[17px] bg-companyYellow py-2 px-2 md:px-10 rounded w-[100%]"
                                    onClick={handleObserverCodeSubmit}
                                    >
                                    {selectedLanguage === "EN" ? 'Verify Code' : "Vérifier code"}
                                </button>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
    )
}

export default LandingPage