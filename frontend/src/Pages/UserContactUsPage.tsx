import React, { useEffect, useState } from 'react'
import NormalNavBar from '../Components/NormalNavBar'
import Cookies from 'js-cookie'

function UserContactUsPage() {

     const PhoneIcon = () => {
         return(
            <svg className='ml-3 mr-1' width="25" viewBox="0 0 509 928" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="20" y="20" width="469" height="888" rx="20" stroke="#F1C551" stroke-width="40"/>
                <circle cx="254" cy="799" r="50" fill="#F1C551"/>
                <circle cx="86" cy="81" r="19" fill="#F1C551"/>
                <rect x="131" y="62" width="138" height="38" rx="19" fill="#F1C551"/>
            </svg>
         )
    }

    const LocationIcon = () => {
        return(
            <svg width="40" viewBox="0 0 1048 834" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M772 250C772 388.071 660.071 500 522 500C383.929 500 272 388.071 272 250C272 111.929 383.929 0 522 0C660.071 0 772 111.929 772 250ZM400.866 250C400.866 316.901 455.099 371.134 522 371.134C588.901 371.134 643.134 316.901 643.134 250C643.134 183.099 588.901 128.866 522 128.866C455.099 128.866 400.866 183.099 400.866 250Z" fill="#F1C551"/>
                <path d="M525.107 730.027L300 366L292 347.072L521 376.5L751.5 347.072L743.5 366L525.107 730.027Z" fill="#F1C551"/>
                <path d="M767.698 643.587C870.701 654.017 952.764 670.759 1000.95 691.171C1049.14 711.584 1060.71 734.508 1033.83 756.328C1006.96 778.148 943.17 797.624 852.527 811.684C761.883 825.745 649.538 833.59 533.204 833.984C416.871 834.378 303.165 827.298 210.015 813.86C116.865 800.422 49.5679 781.39 18.7343 759.765C-12.0992 738.141 -4.71564 715.153 39.7211 694.426C84.1578 673.7 163.12 656.413 264.159 645.293L322.613 665.024C244.305 673.643 183.106 687.04 148.666 703.104C114.226 719.168 108.504 736.984 132.401 753.744C156.298 770.504 208.455 785.254 280.65 795.669C352.845 806.084 440.971 811.572 531.134 811.267C621.296 810.961 708.368 804.881 778.62 793.983C848.872 783.086 898.31 767.991 919.138 751.08C939.966 734.169 931 716.402 893.654 700.581C856.308 684.761 792.706 671.786 712.875 663.702L767.698 643.587Z" fill="#F1C551"/>
            </svg>
        )
    }

    const EmailIcon = () => {
        return(
            <svg width="40" viewBox="0 0 1063 624" fill="none" xmlns="http://www.w3.org/2000/svg">
                <mask id="path-1-inside-1_99_2" fill="white">
                <rect x="0.175537" width="1062" height="624" rx="10"/>
                </mask>
                <rect x="0.175537" width="1062" height="624" rx="10" stroke="#F1C551" stroke-width="80" mask="url(#path-1-inside-1_99_2)"/>
                <path d="M1010.7 21.7147L530.175 332.189L51.6682 23.0623L1010.7 21.7147Z" stroke="#F1C551" stroke-width="40"/>
            </svg>
        )
    }

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

        <div className='px-[5%] py-20'>

            <div className="flex flex-col gap-3">

                {selectedLanguage === "EN" ? 
                     <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="text-3xl">Contact <span className='text-companyYellow'>US</span></div>
                : 
                    <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="text-3xl">Contactez-<span className='text-companyYellow'>Nous</span></div>
                }

                <div className="pb-5 font-light text-sm opacity-90 lg:pr-[20%]">
                    {selectedLanguage === "EN" ? 
                        "If You have any questions, feel free to get in touch with us."
                    : 
                        "Si vous avez des questions, n'hésitez pas à nous contacter."
                    }
                    
                 </div>

                <hr className="border-1 border-companyYellow" />
            </div>

            

            <div style={{alignItems: "center"}}  className='flex lg:justify-around xl:justify-evenly lg:px-[8%] flex-wrap mt-10'>

                <div className='hidden lg:flex flex-col gap-12 text-sm justify-center'>

                    <div style={{alignItems: "center"}} className='flex flex-row gap-4'>
                        <div>{PhoneIcon()}</div>
                        <div className='flex flex-col border-l-2 border-companyYellow pl-4'>
                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-companyYellow text-xl'>
                            {selectedLanguage === "EN" ? 
                                "Phone Number"
                            : 
                                "Numéro de téléphone"
                            }
                            </div>
                            <div className='font-light'>(555)-555-5555</div>
                        </div>
                    </div>

                    <div style={{alignItems: "center"}} className='flex flex-row gap-4'>
                        <div>{LocationIcon()}</div>
                        <div className='flex flex-col border-l-2 border-companyYellow pl-4'>
                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-companyYellow text-xl'>
                                {selectedLanguage === "EN" ? 
                                    "Location"
                                    : 
                                    "Emplacement"
                                }
                            </div>
                            <div className='font-light'>123 street, City ,Country</div>
                        </div>
                    </div>

                    <div style={{alignItems: "center"}} className='flex flex-row gap-4'>
                        <div>{EmailIcon()}</div>
                        <div className='flex flex-col border-l-2 border-companyYellow pl-4'>
                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-companyYellow text-xl'>
                            {selectedLanguage === "EN" ? 
                                "E-mail"
                                : 
                                "Courriel"
                            }
                            </div>
                            <div className='font-light'>movingexpressintl@gmail.com</div>
                        </div>
                    </div>

                </div>

                
                <div className='block flex flex-wrap gap-5 md:mr-20 lg:hidden'>
                    <div className=' py-5 px-5 rounded-md shadow-md bg-white border'>
                        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-companyYellow'>
                        {selectedLanguage === "EN" ? 
                            "Phone Number"
                        : 
                            "Numéro de téléphone"
                        }
                        </div>
                        <hr className="border-1 border-companyYellow" />
                        <div className='text-sm mt-2'>(555)-555-5555</div>
                    </div>

                    <div className=' py-5 px-5 rounded-md shadow-md bg-white border'>
                        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-companyYellow'>
                        {selectedLanguage === "EN" ? 
                            "Location"
                            : 
                            "Emplacement"
                        }
                        </div>
                        <hr className="border-1 border-companyYellow" />
                        <div className='text-sm mt-2'>123 street, City ,Country </div>
                    </div>

                    <div className='py-5 px-5 rounded-md shadow-md bg-white border'>
                        <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-companyYellow'>
                            {selectedLanguage === "EN" ? 
                                "E-mail"
                                : 
                                "Courriel"
                            }
                        </div>
                        <hr className="border-1 border-companyYellow" />
                        <div className='text-dm mt-2'>movingexpressintl@gmail.com</div>
                    </div>
                </div>

                <div>
                    <div className="flex flex-col gap-3 lg:hidden  mt-10">
                        
                        {selectedLanguage === "EN" ? 
                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="text-3xl">Message <span className='text-companyYellow'>form</span></div>
                            : 
                            <div style={{ fontFamily: 'Bebas Neue, cursive' }} className="text-3xl">Formulaire de <span className='text-companyYellow'>message</span></div>
                        }

                        <hr className="border-1 border-companyYellow mb-10" />
                    </div>

                    {/* form */}
                    <form id='contactForm' className="flex flex-col gap-10" action="mailto:movingexpressintl@gmail.com" method="post" encType="text/plain">

                        <div className="flex flex-row gap-4 flex-wrap">
                            <input style = {{lineHeight : "1.05", letterSpacing : "1px"}}  className="border-b font-thin border-b-[rgba(0,0,0,0.3)] pb-2 text-sm" name="first-name" type="text" placeholder={selectedLanguage === "EN" ? "First Name" : "Prénom"} />

                            <input style = {{lineHeight : "1.05", letterSpacing : "1px"}}  className="border-b font-thin border-b-[rgba(0,0,0,0.3)] pb-2 text-sm"  type="text" name="last-name" placeholder={selectedLanguage === "EN" ? "Last Name" : "Nom de famille"} />

                            <input style = {{lineHeight : "1.05", letterSpacing : "1px"}}  className="border-b font-thin border-b-[rgba(0,0,0,0.3)] pb-2 text-sm"  type="email" name="email" placeholder={selectedLanguage === "EN" ? "E-Mail" : "Courriel"} />
                        </div>

                        <div>
                            <input name="subject" style = {{lineHeight : "1.05", letterSpacing : "1px"}}  className="border-b font-thin border-b-[rgba(0,0,0,0.3)] w-full pb-2 text-sm"  type="text" placeholder={selectedLanguage === "EN" ? "Subject" : "Sujet"} />
                        </div>

                        <div>
                            {/*@ts-ignore*/}
                            <textarea id="body" name="body" style = {{lineHeight : "1.05", letterSpacing : "1px"}}  className="border-b font-thin border-b-[rgba(0,0,0,0.3)] w-full pb-2 text-sm" cols="61" rows="4" placeholder='Message'></textarea>
                        </div>

                        <div className="flex gap-4">
                            <button type='submit' style={{ fontFamily: 'Bebas Neue, cursive' }} className="text-white bg-companyYellow w-20 rounded-sm opacity-100 hover:-translate-y-1 hover:opacity-75 duration-300 ease-in-out ">{selectedLanguage === "EN" ? "Send" : "Envoyer"}</button>

                            <button type='reset' style={{ fontFamily: 'Bebas Neue, cursive' }} className=" bg-white border-2 border-companyYellow text-companyYellow w-20 rounded-sm opacity-100 hover:-translate-y-1 hover:opacity-75 duration-300 ease-in-out ">{selectedLanguage === "EN" ? "Clear" : "Effacer"}</button>
                        </div>

                    </form>
                </div>
               
            </div>
        
        </div>
    </div>
  )
}

export default UserContactUsPage