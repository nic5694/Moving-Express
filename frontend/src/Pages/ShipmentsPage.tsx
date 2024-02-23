import React, { useState } from 'react'
import NormalNavBar from '../Components/NormalNavBar'
import { useAuth } from '../auth/components/AuthService'
import axios from 'axios'
import { useEffect } from 'react'
import Cookies from 'js-cookie'
import ShipmentContainer from '../Components/ShipmentContainer'

axios.defaults.withCredentials = true
function ShipmentsPage() {

  const [userShipments, setUserShipments] = useState([]);

  const auth = useAuth()

  const checkIfProfileExists = async () => {
    await axios.get(process.env.REACT_APP_BACKEND_URL + "api/v1/movingexpress/users?simpleCheck=true", {
      headers: {
        // @ts-ignore
        "X-XSRF-TOKEN": auth.getXsrfToken(),
      }
    }).then(r => {
      getShipments(Cookies.get('email'))
    })
  }

  const getShipments = async (email: any) => {

    await axios.get(process.env.REACT_APP_BACKEND_URL + "api/v1/movingexpress/shipments", {
      params: {
        email: email
      }
    }).then((r: any) => {
      setUserShipments(r.data)
    })
  }

  useEffect(() => {
    checkIfProfileExists();
  }, [])

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

      <div className='px-[5%] pt-20 pb-[10%]'>
        <div className="flex flex-col gap-3 pb-7">
          <div
            style={{ fontFamily: 'Bebas Neue, cursive' }}
            className="text-3xl"
          >

            {selectedLanguage === "EN" ? 
              <>
                All
                <span className="text-companyYellow"> Shipments</span>
              </>
            : 
              <>
                Tous les 
                <span className="text-companyYellow"> expéditions</span>
              </>
            }
          </div>

          <div className="pb-5 font-light text-sm opacity-90 lg:pr-[10%] text-justify">
            
            {selectedLanguage === "EN" ? 
            
            <>
            Welcome to the Shipments Page! Here, you can effortlessly manage and monitor all your created and ongoing shipments.  
            Whether you're tracking the progress of a shipment or need detailed information about your shipments, this page provides a 
            centralized view of your logistics activities. Explore the list of your shipments, each accompanied by essential details such 
            as shipment name, status and weight. To delve deeper into a specific shipment, simply click on the "Eye Icon" button.
            </>
            : 
            <>
             Bienvenue sur la page des expéditions ! Ici, vous pouvez gérer et surveiller facilement toutes vos expéditions 
             créées et en cours. Que vous suiviez l'avancement d'une expédition ou que vous ayez besoin d'informations 
             détaillées sur vos expéditions, cette page offre une vue centralisée de vos activités logistiques. Explorez 
             la liste de vos expéditions, chacune accompagnée d'informations essentielles telles que le nom de l'expédition, 
             le statut et le poids. Pour approfondir une expédition spécifique, il vous suffit de cliquer sur l'icône représentant 
             un œil.
            </>
          }
          </div>

          <hr className="border-1 border-companyYellow" />
        </div>

        <div>

          {userShipments.length == 0
            ? (
            
             <div className='text-center opacity-25 font-light pb-[10%]'> { selectedLanguage === "EN" ? "[ No Shipments ]" : "[ Aucune Expédition ]" }</div>)


            : (

              <div className='flex flex-wrap gap-5'>

                {userShipments.map((shipment: any) => (<ShipmentContainer shipment={shipment} />))}

              </div>


            )}

        </div>

      </div>

    </div>
  );
}


export default ShipmentsPage;
