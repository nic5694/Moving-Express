import { toast } from 'react-toastify'
import NormalNavBar from '../Components/NormalNavBar'
import { useAuth } from '../auth/components/AuthService'
import axios from 'axios'
import React, { useEffect, useState } from 'react'
import Cookies from 'js-cookie'

function UserProfilePage() {

  const[editActive, setEditActive] = useState(false)
  const[customerInfo, setCustomerInfo] = useState('')
  
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
      const email = userData.email
      setCustomerInfo(userData)
      getAllQuotes(email)

      // getCustomerInfo(authCustomerId)
    } catch (error) {
      toast.error(selectedLanguage === "EN" ? 'Error fetching user info' : 'Erreur lors de la récupération des informations utilisateur.', {
        position: 'top-right',
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined
      })
    }
  };

  useEffect(()=>{
    getUserInfo()
  },[])

  const saveUserInfo = async () => {

    setEditActive(false)
    
    //@ts-ignore
    let phoneNumber : number = document.getElementById('phoneNumberInput').value || ""
    //@ts-ignore
    let lastName : string = document.getElementById('lastNameInput').value || ""

    //@ts-ignore
    let firstName : string = document.getElementById('firstNameInput').value || ""

    let customerObject = {
      //@ts-ignore
      userId : customerInfo['userId'],
      //@ts-ignore
      profilePictureUrl : customerInfo['profilePictureUrl'],
      //@ts-ignore
      email : document.getElementById('emailInput').value,
      //@ts-ignore
      firstName : firstName,
      //@ts-ignore
      lastName : lastName,
      //@ts-ignore
      phoneNumber: phoneNumber
    }

    try {
      const response = await axios.put(process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/users', customerObject , {
        headers: {
          // @ts-ignore
          'X-XSRF-TOKEN': auth.getXsrfToken(),
        },
      });


      getUserInfo()

      toast.success(selectedLanguage === "EN" ? 'Your information has been updated successfully' : 'Vos informations ont été mises à jour avec succès.', {
        position: 'top-right',
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined
    })
    } catch (error) {
      toast.error(selectedLanguage === "EN" ? 'Error updating your information' : 'Erreur lors de la mise à jour de vos informations.', {
        position: 'top-right',
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined
      })
    }

  }

  const refreshIcon = () => {
    return (
      <svg width="15" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M50 100C36.0417 100 24.2187 95.1562 14.5312 85.4687C4.84375 75.7812 0 63.9583 0 50C0 36.0417 4.84375 24.2187 14.5312 14.5312C24.2187 4.84375 36.0417 0 50 0C57.1875 0 64.0625 1.48333 70.625 4.45C77.1875 7.41667 82.8125 11.6625 87.5 17.1875V0H100V43.75H56.25V31.25H82.5C79.1667 25.4167 74.6104 20.8333 68.8312 17.5C63.0521 14.1667 56.775 12.5 50 12.5C39.5833 12.5 30.7292 16.1458 23.4375 23.4375C16.1458 30.7292 12.5 39.5833 12.5 50C12.5 60.4167 16.1458 69.2708 23.4375 76.5625C30.7292 83.8542 39.5833 87.5 50 87.5C58.0208 87.5 65.2604 85.2083 71.7188 80.625C78.1771 76.0417 82.7083 70 85.3125 62.5H98.4375C95.5208 73.5417 89.5833 82.5521 80.625 89.5313C71.6667 96.5104 61.4583 100 50 100Z" fill="white" />
      </svg>
    )
  }

  const AcceptedIcon = () => {
    return (
      <svg className='mr-1' width="20" viewBox="0 0 75 62" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M24.6168 61.6015L0 36.9847L11.2183 25.7664L24.6168 39.2046L63.7817 0L75 11.2183L24.6168 61.6015Z" fill="lightgreen"/>
      </svg>
    )
  }

  const CreatedIcon = () => {
    return (
      <svg width="25" viewBox="0 0 100 80" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M97.5 55H95V38.1094C95 36.125 94.2031 34.2187 92.7969 32.8125L77.1875 17.2031C75.7812 15.7969 73.875 15 71.8906 15H65V7.5C65 3.35937 61.6406 0 57.5 0H7.5C3.35938 0 0 3.35937 0 7.5V57.5C0 61.6406 3.35938 65 7.5 65H10C10 73.2812 16.7188 80 25 80C33.2812 80 40 73.2812 40 65H60C60 73.2812 66.7188 80 75 80C83.2812 80 90 73.2812 90 65H97.5C98.875 65 100 63.875 100 62.5V57.5C100 56.125 98.875 55 97.5 55ZM25 72.5C20.8594 72.5 17.5 69.1406 17.5 65C17.5 60.8594 20.8594 57.5 25 57.5C29.1406 57.5 32.5 60.8594 32.5 65C32.5 69.1406 29.1406 72.5 25 72.5ZM75 72.5C70.8594 72.5 67.5 69.1406 67.5 65C67.5 60.8594 70.8594 57.5 75 57.5C79.1406 57.5 82.5 60.8594 82.5 65C82.5 69.1406 79.1406 72.5 75 72.5ZM87.5 40H65V22.5H71.8906L87.5 38.1094V40Z" fill="#F1C551"/>
      </svg>
    )
  }

  const DeclinedIcon = () => {
    return (
      <svg className='mr-2' width="18" viewBox="0 0 75 75" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M13.4713 0L0 13.4713L6.87898 20.3503L23.8854 37.6433L6.87898 54.6497L0 61.242L13.4713 75L20.3503 68.121L37.6433 50.828L54.6497 68.121L61.242 75L75 61.242L68.121 54.6497L50.828 37.6433L68.121 20.3503L75 13.4713L61.242 0L54.6497 6.87898L37.6433 23.8854L20.3503 6.87898L13.4713 0Z" fill="red"/>
      </svg>
    )
  }

  const PendingIcon = () => {
    return (
      <svg width="25" viewBox="0 0 100 87" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M41.8706 43.9142C43.526 40.2766 45.8817 37.0008 48.8033 34.2739C51.7249 31.5469 55.1551 29.4222 58.898 28.0212C62.6408 26.6201 66.623 25.9702 70.6171 26.1084C74.6112 26.2466 78.539 27.1704 82.176 28.8268C85.8136 30.4822 89.0894 32.8379 91.8163 35.7595C94.5433 38.6811 96.668 42.1113 98.069 45.8542C99.4701 49.597 100.12 53.5792 99.9818 57.5733C99.8436 61.5674 98.9198 65.4952 97.2634 69.1322C94.8527 74.4543 90.9569 78.9675 86.0439 82.1297C81.131 85.2919 75.4097 86.9687 69.567 86.9588C57.8276 86.9588 47.0882 80.176 42.0446 69.567H0V60.8711C0.260876 55.9145 3.65227 51.8709 10.1742 48.6099C16.6961 45.349 24.8702 43.6533 34.7835 43.4794C37.2618 43.4794 39.6097 43.6968 41.8706 43.9142ZM34.7835 0C39.6532 0.130438 43.7403 1.82613 47.0012 5.08709C50.2622 8.34804 51.8709 12.4351 51.8709 17.3918C51.8709 22.3484 50.2622 26.4355 47.0012 29.6964C43.7403 32.9574 39.6532 34.5661 34.7835 34.5661C29.9138 34.5661 25.8267 32.9574 22.5658 29.6964C19.3048 26.4355 17.6961 22.3484 17.6961 17.3918C17.6961 12.4351 19.3048 8.34804 22.5658 5.08709C25.8267 1.82613 29.9138 0.130438 34.7835 0ZM69.567 78.2629C75.3327 78.2629 80.8623 75.9725 84.9393 71.8955C89.0163 67.8185 91.3067 62.2889 91.3067 56.5232C91.3067 50.7575 89.0163 45.2279 84.9393 41.1509C80.8623 37.0739 75.3327 34.7835 69.567 34.7835C63.8013 34.7835 58.2717 37.0739 54.1947 41.1509C50.1177 45.2279 47.8273 50.7575 47.8273 56.5232C47.8273 62.2889 50.1177 67.8185 54.1947 71.8955C58.2717 75.9725 63.8013 78.2629 69.567 78.2629ZM65.2191 43.4794H71.741V55.7406L82.3499 61.8711L79.089 67.5235L65.2191 59.5233V43.4794Z" fill="gray"/>
      </svg>
    )
  }

  const[quotes,setQuotes] = useState([])

  const getAllQuotes = async (email:string) => {
    try{

      const response = await axios.get(process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/quotes/Email/' + email)
      
      let quotes = response.data
      setQuotes(quotes)

    } catch(error){
      toast.error(selectedLanguage === "EN" ? 'Error fetching quotes' : 'Erreur lors de la récupération des citations.', {
        position: 'top-right',
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: 'light'
      })
    }
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
      <NormalNavBar/>

      <div style={{alignItems: "center"}} className='flex flex-row justify-center gap-5 flex-wrap py-14 pb-20 px-[5%]'>
        
        {/* User info Section */}
        <div className="w-[100%] md:w-[65%] lg:w-[50%] bg-white border border-gray-300 rounded-md shadow-md px-10 py-5 ">
          
          <div style={{alignItems: "center", fontFamily: 'Bebas Neue, cursive'}} className='flex flex-row justify-between'>
            
            {selectedLanguage === "EN" ?  
              <div className='text-xl'>Profile <span className='text-companyYellow'>Details</span></div>
            : 
              <div className='text-xl'>Détails <span className='text-companyYellow'>Profil</span></div>
            }
            

            {!editActive && 
            <div>
              <button onClick={() => setEditActive(true)} className='bg-companyYellow text-white py-1 px-3 rounded-sm text-sm'>{selectedLanguage === "EN" ? "Edit Profile" : "Modifier profil"}</button>
            </div>
            }
          </div>

          <div className='flex flex-col gap-2 py-10'>
            
            <div className='flex justify-center'>
              {/* @ts-ignore */}
              <img src={customerInfo['profilePictureUrl']} className='rounded-full shadow-lg w-[115px]' />
            </div>
            
            <div style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}} className='flex justify-center text-sm opacity-50'>{selectedLanguage === "EN" ? "Profile picture" : "Photo de profil"}</div> 
          </div>

          <div className='flex justify-center'>
            <div className='flex justify-center flex-col gap-3 pb-10'>

              <div className='block lg:flex flex-wrap gap-3'>
                <div className='flex flex-col gap-1'>
                  {/* @ts-ignore */}
                  <input id='firstNameInput' type="text" style={editActive? {opacity: "black" } : {color: "rgba(156,163,175,1)"}}   className='w-[100%] border text-sm py-1 px-2' defaultValue={customerInfo['firstName']} placeholder='First Name' readOnly={!editActive}/>
                  <label style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}}  className='text-sm opacity-50'>{selectedLanguage === "EN" ? "First name" : "Prénom"}</label>
                </div>

                <div className='flex flex-col gap-1'>
                  {/* @ts-ignore */}
                  <input id='lastNameInput' type="text" style={editActive? {opacity: "black" } : {color: "rgba(156,163,175,1)"}}  className='border text-sm py-1 px-2' defaultValue={customerInfo['lastName']} placeholder={selectedLanguage === "EN" ? "Last name" : "Nom de famille"} readOnly={!editActive} />
                  <label style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}}  className='text-sm opacity-50'>{selectedLanguage === "EN" ? "Last name" : "Nom de famille"}</label>
                </div>
              </div>
              
              <div className='block lg:flex  flex-wrap gap-3'>
                <div className='flex flex-col gap-1'>
                  {/* @ts-ignore */}
                  <input id='emailInput'  type="text"  className='border text-sm py-1 px-2 text-gray-400' defaultValue={customerInfo['email']} placeholder='Email' readOnly/>
                  <label style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}}  className='text-sm opacity-50'>{selectedLanguage === "EN" ? "E-mail" : "Courriel"}</label>
                </div>

                <div className='flex flex-col gap-1'>
                  {/* @ts-ignore */}
                  <input id='phoneNumberInput'  type="number" style={editActive? {opacity: "black" } : {color: "rgba(156,163,175,1)"}} className='border text-sm py-1 px-2' defaultValue={customerInfo['phoneNumber'] || ""} placeholder={selectedLanguage === "EN" ? "Phone Number" : "téléphone"} readOnly={!editActive} />
                  <label style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}}  className='text-sm opacity-50' >{selectedLanguage === "EN" ? "Phone Number" : "téléphone"}</label>
                </div>
              </div>

            </div>
          </div>
          
          { editActive && 
            
            <div className='flex justify-center gap-3'>
              <button onClick={() => saveUserInfo()} style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}}  className='bg-companyYellow text-white py-1 px-5 rounded text-sm shadow-md'>{selectedLanguage === "EN" ? "Save" : "Enregistrer"}</button>
              <button onClick={() => setEditActive(false)} style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}}  className='bg-[#2D2D2D] text-white py-1 px-5 rounded text-sm shadow-md'>{selectedLanguage === "EN" ? "Cancel" : "Annuler"}</button>
            </div> 
          }
        </div>

        {/* User Quote info Section */}
        <div className="w-[100%] md:w-[65%] lg:w-[40%] bg-white border border-gray-300 h-[450px] rounded-md shadow-md py-5 px-5">

          <div style={{alignItems: "center"}} className='flex justify-between'>
            
            {selectedLanguage === "EN" ? 
            
             <div style={{ fontFamily: 'Bebas Neue, cursive'}} className='text-lg' >Quote <span className="text-companyYellow">Status</span></div>
            
            : 
            
              <div style={{ fontFamily: 'Bebas Neue, cursive'}} className='text-lg' >Statut des <span className="text-companyYellow"> devis</span></div>
            
            }
            <div>
              <button onClick={() => {
                // @ts-ignore
                getAllQuotes(customerInfo['email'])}
                
                } style={{ fontFamily: 'Bebas Neue, cursive', alignItems: 'center' }} className="bg-companyYellow text-white py-2 px-2 rounded-full text-sm flex flex-row gap-2"><span>{refreshIcon()}</span></button></div>
          </div>

          <hr className='my-3'/>

          <div className='h-[320px] overflow-auto flex flex-col gap-2'>

            {
              quotes.map((quote: any) => (
                <div className='text-sm flex justify-between px-3 pr-5 py-3 bg-white rounded shadow border'>
                  <div >{quote.name} 
                    <span className='text-companyYellow font-bold text-[10px] ml-1'>
                      {
                        selectedLanguage === "EN" ? 
                        `[${quote.quoteStatus}]`
                        :
                        quote.quoteStatus === "ACCEPTED" 
                        ? "[ACCEPTÉ]" 
                        : quote.quoteStatus === "PENDING" 
                        ? "[EN ATTENTE]" 
                        : quote.quoteStatus === "CREATED"
                        ? "[CRÉÉ]"
                        : quote.quoteStatus === "DECLINED"
                        ? "[REFUSER]"
                        : "[Loading...]"
                      }
                      
                    </span>
                  </div>
                  <div>
                    {quote.quoteStatus === "ACCEPTED"? AcceptedIcon() : quote.quoteStatus === "PENDING"? PendingIcon() : quote.quoteStatus === "CREATED"? CreatedIcon() : DeclinedIcon()}
                  </div>
                </div>
                )
              )
            }

            

          </div>

        </div>
      </div>

    </div>
  )
}

export default UserProfilePage
