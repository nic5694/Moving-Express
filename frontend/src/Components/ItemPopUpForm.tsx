import Cookies from 'js-cookie';
import React, { useEffect, useState } from 'react'

function ItemPopUpForm(props: any) {

    let operation = props.operation

    interface item {
        itemId: string;
        name: string;
        type: string;
        inventoryId: string;
        description: string;
        handlingInstructions: string;
        price: number;
    }

    let itemObjectToDisplay: item = props.itemObject

    const [selectedLanguage, setSelectedLanguage] = useState('');

    useEffect(() => {

        const language = Cookies.get("MovingExpress_SelectedLanguage")

        if (language) {
            // set selected Language
            setSelectedLanguage(language)
        } else {
            // set a cookie and usestate to english as default
            Cookies.set('MovingExpress_SelectedLanguage', 'EN', { expires: 7, path: '/' });
            setSelectedLanguage("EN")
        }

    }, [])

    return (
        <div className="overscroll-auto pr-10 pb-5 pt-3">
            {operation != "add" &&
                <div className='flex gap-3 flex-wrap'>

                    <div className="flex-col w-[100%] flex md:flex-row flex-wrap gap-3">
                        <div className="flex flex-col gap-1 w-[60%]">
                            <input
                                type="text"
                                className=" border border-[lightgray]  text-xs h-[35px] px-4 rounded-sm"
                                id="ItemIdInput"
                                name="ItemIdInput"
                                placeholder={selectedLanguage === "EN" ? "Item ID" : "ID d'article"}
                                defaultValue={itemObjectToDisplay['itemId']}
                                required
                                readOnly={(operation === "details" || operation === "delete" || operation === "edit")}
                            />
                            <label
                                style={{
                                    fontFamily:
                                        'Bebas Neue, cursive',
                                }}
                                className="text-[#696969] text-xs"
                            >
                                {selectedLanguage === "EN" ? "Item ID" : "ID d'article"}
                            </label>
                        </div>

                    </div>
                </div>
            }

            <div className='flex gap-3 flex-wrap mt-2'>
                <div className="flex-col w-[100%] flex md:flex-row flex-wrap gap-3">
                    <div className="flex flex-col gap-1">
                        <input
                            type="text"
                            className=" border border-[lightgray]  text-xs h-[35px] px-4 rounded-sm"
                            id="ItemNameInput"
                            name="ItemNameInput"
                            placeholder={selectedLanguage === "EN" ? "Item Name" : "Nom de l'article"}
                            defaultValue={operation == "details" || operation == "delete" || operation == "edit" ? itemObjectToDisplay['name'] : undefined}
                            readOnly={(operation === "details" || operation === "delete")}
                            required
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "Item Name" : "Nom de l'article"}
                        </label>
                    </div>

                    <div className="flex flex-col gap-1">
                        <input
                            type="price"
                            className="border border-[lightgray] text-xs h-[35px] px-4 rounded-sm"
                            id="PriceInput"
                            name="PriceInput"
                            placeholder={selectedLanguage === "EN" ? "Item Price" : "Prix d'article"}
                            defaultValue={
                                itemObjectToDisplay[`price`] === null ? "" :
                                operation === "details" || operation === "delete"
                                    ? `$${itemObjectToDisplay['price']}`
                                    : operation === "edit"
                                        ? `${itemObjectToDisplay['price']}`
                                        : 0
                            }
                            readOnly={(operation === "details" || operation === "delete")}
                            required
                        />
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "Item Price" : "Prix d'article"}
                        </label>
                    </div>

                    <div className="flex flex-col gap-1">
                        <select
                            id="ItemSelectInput"
                            className="border border-[lightgray] py-1 px-1"
                            disabled={operation === "details" || operation === "delete"}
                        >
                            <optgroup>
                                <option
                                    id="ItemOption"
                                    value="ITEM"
                                >
                                    {selectedLanguage === "EN" ? "ITEM" : "ARTICLE"}
                                </option>
                                <option
                                    id="BoxOption"
                                    value="BOX"
                                >
                                    {selectedLanguage === "EN" ? "BOX" : "BOITE"}
                                </option>
                            </optgroup>
                        </select>
                        <label
                            style={{
                                fontFamily:
                                    'Bebas Neue, cursive',
                            }}
                            className="text-[#696969] text-xs"
                        >
                            {selectedLanguage === "EN" ? "Item Type" : "Type d'article"}
                        </label>
                    </div>
                </div>
            </div>

            <div className="flex flex-col mt-3 gap-1 h-[15vh]">
                <textarea
                    className="border border-[lightgray] text-xs h-[100%] px-4 py-1 rounded-sm"
                    name="descriptionInput"
                    id="descriptionInput"
                    placeholder={selectedLanguage === "EN" ? "Description" : "Description"}
                    defaultValue={operation == "details" || operation == "delete" || operation == "edit" ? itemObjectToDisplay['description'] : undefined}
                    readOnly={(operation === "details" || operation === "delete")}
                    required
                />
                <label
                    style={{
                        fontFamily:
                            'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN" ? "Item Description" : "Description d'article"}
                </label>
            </div>

            <div className="flex flex-col gap-1 mt-3 h-[15vh]">
                <textarea
                    className="border border-[lightgray] text-xs  h-[100%] px-4 rounded-sm py-1"
                    name="handlingInstructionsInput"
                    id="handlingInstructionsInput"
                    placeholder={selectedLanguage === "EN" ? "Handling Instructions" : "Instructions de manipulation"}
                    defaultValue={operation == "details" || operation == "delete" || operation == "edit" ? itemObjectToDisplay['handlingInstructions'] : undefined}
                    readOnly={(operation === "details" || operation === "delete")}
                    required
                />
                <label
                    style={{
                        fontFamily:
                            'Bebas Neue, cursive',
                    }}
                    className="text-[#696969] text-xs"
                >
                    {selectedLanguage === "EN" ? "Handling instructions" : "Instructions de manipulation"}
                </label>
            </div>
        </div>
    )
}

export default ItemPopUpForm