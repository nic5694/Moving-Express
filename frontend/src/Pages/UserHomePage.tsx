import React, { useEffect, useState } from 'react'
import UserHomeNav from '../Components/UserHomeNav'
import ScrollToTopBtn from '../Components/ScrollToTopBtn'
import { Link } from 'react-router-dom'
import truckIcon from '../Images/truck icon.png'
import ShipmentQuoteIMG from '../Images/shipment quote.png'
import ApprovedIMG from '../Images/Created.png'
import InventoryIMG from '../Images/inventory.jpg'
import ReadyIMG from '../Images/ready.jpg'
import Cookies from 'js-cookie'

function UserHomePage() {

  // done with localization for this page

  const [selectedLanguage, setSelectedLanguage] = useState('');
  
    useEffect(() => {

      const language =  Cookies.get("MovingExpress_SelectedLanguage")

      if(language){
          // set selected Language
          setSelectedLanguage(language)
      } else {
          // set a cookie and usestate to english as default
          Cookies.set('MovingExpress_SelectedLanguage', 'EN', { expires: 7,path: '/'});
          setSelectedLanguage("EN")
      }

  },[])

  return (
    <div>
      <ScrollToTopBtn />

      <div className='UserHomePageContainer bg-[lightgray]'>

        <div className='bg-[rgb(0,0,0,0.3)] backdrop-blur-[5px]'>
           
          <UserHomeNav />


          <div className=' flex flex-col gap-5 px-8 pb-20'>
          

            <div className='text-center pt-10'>
              
              <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-5xl md:text-6xl lg:text-[70px] text-white leading-none'>
      
                {
                  selectedLanguage == "EN" ? 
                  <span>Welcome To</span>
                  : 
                  <span>Bienvenue à</span>
                }
              </div>
              <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-5xl md:text-6xl lg:text-[70px] text-companyYellow leading-none'>Moving Express</div>
            </div>

            <div className='flex justify-center'>
              <div className='text-white text-center opacity-90 font-light text-sm w-[90%] md:w-[60%] lg:w-[50%] xl:w-[35%]'>
                {
                  selectedLanguage == "EN" ? 
                  <span>We're here to make your move hassle-free. Explore our services for a personalized solution. Thanks for choosing Moving Express.</span>
                  : 
                  <span>Nous sommes là pour rendre votre déménagement sans tracas. Explorez nos services pour une solution personnalisée. Merci d'avoir choisi Moving Express.</span>
                }
              </div>
            </div>

            <div className='flex flex-row justify-center gap-5 mt-3'>
              <Link to={"/UserShipmentQuote"}>
                <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-white text-sm bg-companyYellow border-2 px-5 py-1 border-companyYellow rounded hover:translate-y-[-5px] duration-300 ease-in-out'>
                  {
                    selectedLanguage == "EN" ? 
                    <span>Make a Quote</span>
                    : 
                    <span>Faire un devis</span>
                  }
                </div>
              </Link>

              <Link to={"/Shipments"}>
                <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-white text-sm border-2 px-5 py-1 border-companyYellow rounded hover:translate-y-[-5px] duration-300 ease-in-out'>
                  {
                    selectedLanguage == "EN" ? 
                    <span>My Shipments</span>
                    : 
                    <span>Mes expéditions</span>
                  }
                </div>
              </Link>
            </div>

          </div>


        </div>
        
       
        

      </div>

      <div className='pt-20 text-center px-[10%] pb-20'>
        <div style={{ fontFamily: 'Bebas Neue, cursive',alignItems: "center" }} className='text-2xl flex justify-center'>
          {
            selectedLanguage == "EN" ? 
            <>Shipment <span className='text-companyYellow pl-1 pr-3'> Process</span> </>
            : 
            <>Processus <span className='text-companyYellow pl-1 pr-3'> d'expédition</span> </>
          }
          <img className='w-14' src={truckIcon} />
        </div>
        <div className="py-3 font-light text-sm opacity-90 px-[5%] lg:px-[8%] xl:px-[20%]">
          {
            selectedLanguage == "EN" ? 
            <> 
              The section below provides a detailed explanation of the shipment process, from creating a quote to the departure of the truck to the destination. Explore each step to understand the complete flow of the shipping process.            </>
            : 
            <>
              La section ci-dessous explique en détail le processus d'expédition, depuis la création d'un devis jusqu'au départ du camion vers la destination. Découvrez chaque étape pour comprendre le déroulement complet du processus d'expédition.
            </>
          }
        </div>
      </div>

      <hr className='mx-[10%] border-companyYellow' />

      <div style={{alignItems: "center"}} className='flex flex-col py-10 gap-20 px-[10%]'>

        <div style={{alignItems: "center"}}  className='flex flex-row gap-20'>

          <div className='flex flex-col gap-3'>
            <div style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}} className='flex flex-row gap-2'>
              <div style={{alignItems: "center"}} className='bg-white border-[6px] border-companyYellow w-10 h-10 rounded-full font-bold flex justify-center'>1</div>
              <div className='text-2xl'>
                {selectedLanguage === "EN" ? "Shipment Quote" : "Devis d'expédition"}
              </div>
            </div>
            <div className='lg:w-[400px] font-light text-sm opacity-90 text-justify'>
            {selectedLanguage === "EN" ?
                "To generate a shipment quote, you can do it either from the landing page or from your account. Simply click on the 'Shipment Quote' link in the navigation bar at the top and proceed to fill in all the necessary information."
              : 
                "Pour générer un devis d'expédition, vous pouvez le faire depuis la page d'accueil ou depuis votre compte. Il vous suffit de cliquer sur le lien 'Devis d'expédition' dans la barre de navigation en haut, puis de remplir toutes les informations nécessaires."
              }
            </div>

            <div className='mt-3'>
              <Link to={'/UserShipmentQuote'}>
                <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-companyYellow text-sm border-2 px-5 py-1 w-[110px] border-companyYellow rounded hover:bg-[#2D2D2D] hover:text-white duration-300 ease-in-out shadow-lg'>
                  
                  {selectedLanguage === "EN" ? "Make a Quote" : "Faire un devis"}
                </div>
              </Link>
            </div>
          </div>

          <div className='hidden lg:block'>
            <img className='bg-white shadow-lg border rounded w-[400px] h-[200px]' src={ShipmentQuoteIMG}/>
          </div>

        </div>

        <div style={{alignItems: "center"}} className='flex flex-row gap-20'>

          <div className='hidden lg:block'>
            <img className='bg-white shadow-lg border rounded w-[400px] h-[200px]' src={ApprovedIMG}/>
          </div>
          
          <div className='flex flex-col gap-3'>
            <div style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}} className='flex flex-row gap-2'>
              <div style={{alignItems: "center"}} className='bg-white border-[6px] border-companyYellow w-10 h-10 rounded-full font-bold flex justify-center'>2</div>
              <div className='text-2xl'>
                {selectedLanguage === "EN" ? "Shipment Created" : "Expédition créée"}
              </div>
            </div>
            <div className='lg:w-[400px] font-light text-sm opacity-90 text-justify'>
              {selectedLanguage === "EN" ?
                "When your quote has been accepted and a shipment has been created, you can access your shipments easily. Just click on the 'Shipments' link in the navigation bar to view and manage your shipments. You can also keep track of the status of the quote on your profile page by clicking your name at the top."
              : 
                "Lorsque votre devis a été accepté et qu'une expédition a été créée, vous pouvez accéder facilement à vos expéditions. Il vous suffit de cliquer sur le lien 'Expéditions' dans la barre de navigation pour voir et gérer vos expéditions. Vous pouvez également suivre le statut du devis sur votre page de profil en cliquant sur votre nom en haut."
              }
            </div>
         
            <div className='mt-3'>
              <Link to={'/Shipments'}>
                <div style={{ fontFamily: 'Bebas Neue, cursive' }} className='text-companyYellow text-sm border-2 px-5 py-1 w-[120px] border-companyYellow rounded hover:bg-[#2D2D2D] hover:text-white duration-300 ease-in-out shadow-lg'>
                  {selectedLanguage === "EN" ? "View Shipments" : "Voir expéditions"}
                </div>
              </Link>
            </div>
          
          </div>

        </div>

        <div style={{alignItems: "center"}}  className='flex flex-row gap-20'>

          <div className='flex flex-col gap-3'>
            <div style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}} className='flex flex-row gap-2'>
              <div style={{alignItems: "center"}} className='bg-white border-[6px] border-companyYellow w-10 h-10 rounded-full font-bold flex justify-center'>3</div>
              <div className='text-2xl'>
                {selectedLanguage === "EN" ? "Start Filling Your Inventory" : "Commencez à remplir votre inventaire"}
              </div>
            </div>
            <div className='lg:w-[400px] font-light text-sm opacity-90 text-justify'>
            {selectedLanguage === "EN" ?
                "To fill in the inventory, click on the eye icon of one of the shipments. This will take you to the Shipment Details page where you can view all the details of the shipment. You can also add observer codes, create inventories, and add items inside of them."
              : 
                "Pour remplir l'inventaire, cliquez sur l'icône de l'œil d'une des expéditions. Cela vous dirigera vers la page des détails de l'expédition où vous pourrez consulter tous les détails de l'expédition. Vous pouvez également ajouter des codes d'observateur, créer des inventaires et ajouter des articles à l'intérieur."
            }
            </div>
          </div>

          <div className='hidden lg:block'>
            <img className='bg-white shadow-lg border rounded w-[400px] h-[200px]' src={InventoryIMG}/>
          </div>

        </div>

        <div style={{alignItems: "center"}} className='flex flex-row gap-20'>

          <div className='hidden lg:block'>
            <img style={{filter: "grayscale(100%)"}} className='bg-white shadow-lg border rounded w-[400px] h-[230px]' src={ReadyIMG}/>
          </div>

          <div className='flex flex-col gap-3 '>
            <div style={{ fontFamily: 'Bebas Neue, cursive', alignItems: "end"}} className='flex flex-row gap-2'>
              <div style={{alignItems: "center"}} className='bg-white border-[6px] border-companyYellow w-10 h-10 rounded-full font-bold flex justify-center'>4</div>
              <div className='text-2xl'>
                {selectedLanguage === "EN" ? "Ready For Departure !" : "Prêt au départ !"}
              </div>
            </div>
            <div className='lg:w-[400px] font-light text-sm opacity-90 text-justify'>
            {selectedLanguage === "EN" ?
                "When your inventory is full, and the truck driver updates the shipment status to 'no longer packing,' the truck(s) are ready to depart to the drop-off location."
              : 
                "Lorsque votre inventaire est complet et que le chauffeur de camion met à jour le statut de l'expédition à 'plus en cours de préparation', le ou les camions sont prêts à partir vers le lieu de livraison."
            }
            </div>
            
            <div className='border-b-[30px] border-b-[rgb(73,73,73)] w-[100%]'>
              <div className='mt-5 truck w-[100px] '>
                <img className='w-20' src={truckIcon} />
              </div>
            </div>
          
          </div>
        </div>

      </div>

      <hr className='mx-[10%] pb-20 border-companyYellow' />


      
    </div>
  )
}

export default UserHomePage