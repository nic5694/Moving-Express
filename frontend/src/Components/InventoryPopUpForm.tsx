import Cookies from 'js-cookie';
import React, { useEffect, useState } from 'react'

function InventoryPopUpForm(props:any) {

    let operation = props.operation

    interface inventory {
        inventoryId: string;
        name: string;
        inventoryStatus: string;
        shipmentId: string;
        approximateWeight: number;
        description: string;
    }

    let inventoryObjectToDisplay : inventory = props.inventoryObject

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
    <div className="overscroll-auto pr-10 pb-5 pt-5">
        {operation != "add" && 
        <div className='flex gap-3 flex-wrap'>
            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] w-[200px] text-xs h-[35px] px-4 rounded-sm"
                    id="iventoryIdPopUp"
                    name="iventoryIdPopUp"
                    value={operation=="details" || operation=="delete" || operation == "edit" ?  inventoryObjectToDisplay['inventoryId'] : undefined }
                    readOnly
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN"? "Iventory Id" : "ID d'inventaire"}
                </label>
            </div>

            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] w-[200px] text-xs h-[35px] px-4 rounded-sm"
                    id="shipmentIdPopUp"
                    name="shipmentIdPopUp"
                    value={operation == "details" || operation == "delete" || operation == "edit" ? inventoryObjectToDisplay['shipmentId'] : undefined}
                    readOnly
                />
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                   {selectedLanguage === "EN"? "Shipment Id" : "ID d'expédition"}
                </label>
            </div>

        </div>}

        <div className='flex gap-3 flex-wrap mt-2'>
            <div className="flex flex-col gap-1">
                <input
                    type="text"
                    className="border border-[lightgray] w-[200px] text-xs h-[35px] px-4 rounded-sm"
                    id="inventoryNamePopUp"
                    name="inventoryNamePopUp"
                    defaultValue={operation == "details" || operation == "delete" || operation == "edit" ? inventoryObjectToDisplay['name'] : undefined}
                    readOnly={(operation === "details" || operation === "delete")}

                />
                
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN"? "Iventory Name" : "Nom de l'inventaire"}
                </label>
            </div>

            <div className="flex flex-col gap-1">
                <select className="border border-[lightgray] w-[150px] text-xs h-[35px] px-4 rounded-sm" name="inventoryStatusPopUp" id="inventoryStatusPopUp" disabled={operation == "details" || operation == "delete"}>
                    {operation == 'details' || operation == "delete"
                    ? 
                    <option value={inventoryObjectToDisplay['inventoryStatus']}>
                        
                        {selectedLanguage === "EN"? inventoryObjectToDisplay['inventoryStatus'] 
                        
                        : inventoryObjectToDisplay['inventoryStatus'] === "CREATED"
                        ? "CRÉÉ"
                        : inventoryObjectToDisplay['inventoryStatus'] === "IN_PROGRESS"
                        ? "EN COURS"
                        :inventoryObjectToDisplay['inventoryStatus'] === "PACKED"
                        ? "EMBALLÉ"
                        : "LOADING..."
                        
                        }
                    
                    </option> 
                    : 
                    operation == "add" ?
                    <>     
                        <option defaultChecked value="CREATED">{selectedLanguage === "EN"? "CREATED" : "CRÉÉ"}</option>
                    </> : 
                    // thats edit option
                    <>
                        <option selected={inventoryObjectToDisplay['inventoryStatus'] === 'null'} value="null"></option>
                        <option selected={inventoryObjectToDisplay['inventoryStatus'] === 'CREATED'} value="CREATED">{selectedLanguage === "EN"? "CREATED" : "CRÉÉ"}</option>
                        <option selected={inventoryObjectToDisplay['inventoryStatus'] === 'IN_PROGRESS'} value="IN_PROGRESS">{selectedLanguage === "EN"? "IN_PROGRESS" : "EN COURS"}</option>
                        <option selected={inventoryObjectToDisplay['inventoryStatus'] === 'PACKED'} value="PACKED">{selectedLanguage === "EN"? "PACKED" : "EMBALLÉ"}</option>
                    </>
                    }
                </select>
                <label
                    style={{
                    fontFamily:
                        'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN"? "Inventory Status" : "État d'inventaire"}
                </label>
            </div>

            

        </div>
        
        <div className="flex flex-col gap-1 mt-3">

            <textarea id='inventoryDiscriptionPopUp' name='inventoryDiscriptionPopUp'  rows={4}  className='border border-[lightgray] text-xs py-2 px-2 h-[100%]'
            
                defaultValue={operation == "details" || operation == "delete" || operation == "edit" ? inventoryObjectToDisplay['description'] : undefined}
                
                readOnly={(operation === "details" || operation === "delete")}

            ></textarea>
            <label
            style={{
                fontFamily:
                'Bebas Neue, cursive',
            }}
            className="text-[#696969] text-xs"
            >
                {selectedLanguage === "EN"? "Inventory Discription" : "Description d'inventaire"}
            </label>
        </div>

    </div>
  )
}

export default InventoryPopUpForm