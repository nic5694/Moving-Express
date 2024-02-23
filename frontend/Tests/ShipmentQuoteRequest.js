const { By, Builder } = require('selenium-webdriver')
require('chromedriver')
const assert = require('assert')

async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build()

    await driver.get('http://localhost:3000/')

    // Wait for 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))

    const shipmentQuoteBtn = await driver.findElement(By.id('shipmentQuoteBtn'))
    shipmentQuoteBtn.click()

    // Wait for 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 3000))

    // Assert that the URL is exactly "http://localhost:3000/ShipmentQuote"
    assert.strictEqual(
        await driver.getCurrentUrl(),
        'http://localhost:3000/ShipmentQuote'
    )

    // Wait for 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // fill the form

    //personal information section
    const firstNameInput = await driver.findElement(By.name('FirstNameInput'))
    await firstNameInput.sendKeys('Youssef')

    const lastNameInput = await driver.findElement(By.name('LastNameInput'))
    await lastNameInput.sendKeys('Chahboune')

    const emailInput = await driver.findElement(By.name('EmailInput'))
    await emailInput.sendKeys('Youssef@example.com')

    const phoneNumberInput = await driver.findElement(
        By.name('PhoneNumberInput')
    )
    await phoneNumberInput.sendKeys('5555555555')

    await driver.executeScript(
        "document.getElementById('MovingDateInput').value='2023-12-31'"
    )

    const contactMethod = await driver.findElement(By.id('BothValue'))
    contactMethod.click()

    const additionalCommentsInput = await driver.findElement(
        By.name('AdditionalCommentsInput')
    )
    additionalCommentsInput.sendKeys('Additional Comments Test')

    // Pick up location section

    const PickUpAddressInput = await driver.findElement(
        By.name('PickUpAddressInput')
    )
    await PickUpAddressInput.sendKeys('2233 rue dollard')

    const CityInputP = await driver.findElement(By.name('CityInputP'))
    await CityInputP.sendKeys('Longueuil')

    const PostalCodeInputP = await driver.findElement(
        By.name('PostalCodeInputP')
    )
    await PostalCodeInputP.sendKeys('J4K 4P3')

    const CountrySelectInputP = await driver.findElement(
        By.id('CountrySelectInputP')
    )
    CountrySelectInputP.click()
    await new Promise((resolve) => setTimeout(resolve, 500))
    CountrySelectInputP.findElement(By.id('CanadaOptionP')).click()

    const BuildingTypeSelectInputP = await driver.findElement(
        By.id('BuildingTypeSelectInputP')
    )
    BuildingTypeSelectInputP.click()
    await new Promise((resolve) => setTimeout(resolve, 500))
    BuildingTypeSelectInputP.findElement(By.id('HouseP')).click()

    const NumberOfRoomsInputP = await driver.findElement(
        By.name('NumberOfRoomsInputP')
    )
    await NumberOfRoomsInputP.sendKeys('3')

    const elevatorP = await driver.findElement(By.id('NoOptionP'))
    await elevatorP.click()

    // Drop off destination section

    const DropOffAddressInput = await driver.findElement(
        By.name('DropOffAddressInput')
    )
    await DropOffAddressInput.sendKeys('495 Grove Street unit 5')

    const CityInputD = await driver.findElement(By.name('CityInputD'))
    await CityInputD.sendKeys('New York')

    const PostalCodeInputD = await driver.findElement(
        By.name('PostalCodeInputD')
    )
    await PostalCodeInputD.sendKeys('NY 10014')

    const CountrySelectInputD = await driver.findElement(
        By.id('CountrySelectInputD')
    )
    CountrySelectInputD.click()
    await new Promise((resolve) => setTimeout(resolve, 500))
    CountrySelectInputD.findElement(By.id('USAOptionD')).click()

    const BuildingTypeSelectInputD = await driver.findElement(
        By.id('BuildingTypeSelectInputD')
    )
    BuildingTypeSelectInputD.click()
    await new Promise((resolve) => setTimeout(resolve, 500))
    BuildingTypeSelectInputD.findElement(By.id('ApartmentD')).click()

    const NumberOfRoomsInputD = await driver.findElement(
        By.name('NumberOfRoomsInputD')
    )
    await NumberOfRoomsInputD.sendKeys('2')

    const elevatorD = await driver.findElement(By.id('YesOptionD'))
    await elevatorD.click()

    // shipment name
    const ShipmentNameInput = await driver.findElement(
        By.name('ShipmentNameInput')
    )
    await ShipmentNameInput.sendKeys('Shipment Test')

    // Wait for 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 500))

    // click the request quote button
    const RequestQuoteBtn = await driver.findElement(By.id('RequestQuoteBtn'))
    await RequestQuoteBtn.click()

    // Wait for 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // validate
    const toastifyDiv = await driver.findElement(
        By.className('Toastify__toast')
    )
    const classAttribute = await toastifyDiv.getAttribute('class')
    // Assert that the class attribute contains the expected class
    assert(
        classAttribute.includes('Toastify__toast--success'),
        'Expected class not found'
    )

    // Close the browser after waiting for another 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 1000))
    await driver.quit()
}

test_case()
