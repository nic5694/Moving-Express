const { By, Builder, until } = require('selenium-webdriver')
require('chromedriver')
const assert = require('assert')

const email = "calebliu3@gmail.com"
const password = "Caleb123*"
async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build()

    await driver.get('http://localhost:3000/ShipmentQuote')
    await new Promise((resolve) => setTimeout(resolve, 2000))

    // Wait for 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // fill the form

    //personal information section
    const firstNameInput = await driver.findElement(By.name('FirstNameInput'))
    await firstNameInput.sendKeys('Do')

    const lastNameInput = await driver.findElement(By.name('LastNameInput'))
    await lastNameInput.sendKeys('Cherry')

    const emailInput = await driver.findElement(By.name('EmailInput'))
    await emailInput.sendKeys('calebliu3@gmail.com')

    const phoneNumberInput = await driver.findElement(
        By.name('PhoneNumberInput')
    )
    await phoneNumberInput.sendKeys('123-345-4567')

    await driver.executeScript(
        "document.getElementById('MovingDateInput').value='2023-12-31'"
    )

    const contactMethod = await driver.findElement(By.id('EmailValue'))
    contactMethod.click()

    const additionalCommentsInput = await driver.findElement(
        By.name('AdditionalCommentsInput')
    )
    additionalCommentsInput.sendKeys('I need to move heavy furniture')

    // Pick up location section

    const PickUpAddressInput = await driver.findElement(
        By.name('PickUpAddressInput')
    )
    await PickUpAddressInput.sendKeys('123 Main Street')

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
    await DropOffAddressInput.sendKeys('1123 Saint Boulevard')

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

    await driver.get('http://localhost:3000/Reviewer')

    // Wait for 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))

    assert.strictEqual(
        await driver.getCurrentUrl(),
        'http://localhost:3000/Reviewer'
    )

    await new Promise((resolve) => setTimeout(resolve, 2000))

    let rows = await driver.findElements(By.css('tbody tr'));
    if (rows.length === 0) {
        throw new Error('No quotes found in the table.');
    }

    // Wait for the last row to be loaded and visible
    await driver.wait(until.elementLocated(By.css('tbody tr:last-child')), 50000);
    const lastRow = await driver.findElement(By.css('tbody tr:last-child'));
    const lastQuoteId = await lastRow.getAttribute('id');

    const quoteStatus = await driver.findElement(By.id(`pending-${lastQuoteId}`))
    assert.strictEqual(await quoteStatus.getText(), 'PENDING', 'quoteStatus assertion failed');

    const viewBtn = await driver.findElement(By.id(`btn-${lastQuoteId}`));
    viewBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 2000))

    const acceptBtn = await driver.findElement(By.id('acceptBtn'))
    acceptBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 2000))

    await driver.get('http://localhost:3000/Estimator')
    await new Promise((resolve) => setTimeout(resolve, 2000))

    assert.strictEqual(
        await driver.getCurrentUrl(),
        'http://localhost:3000/Estimator'
    )

    await new Promise((resolve) => setTimeout(resolve, 2000))
    const quoteStatus2 = await driver.findElement(By.id(`accepted-${lastQuoteId}`))
    assert.strictEqual(await quoteStatus2.getText(), 'ACCEPTED', 'quoteStatus assertion failed');

    await new Promise((resolve) => setTimeout(resolve, 2000))
    const viewBtn2 = await driver.findElement(By.id(`btn-${lastQuoteId}`));
    viewBtn2.click()

    await new Promise((resolve) => setTimeout(resolve, 2000))
    const acceptBtn2 = await driver.findElement(By.id('createBtn'))
    acceptBtn2.click()

    await new Promise((resolve) => setTimeout(resolve, 2000))
    const currStatus = await driver.findElement(By.id(`created-${lastQuoteId}`))
    assert.strictEqual(await currStatus.getText(), 'CREATED', 'quoteStatus assertion failed');

    // Close the browser after waiting for another 2 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))

    await driver.get('http://localhost:3000/')

    const loginBtn = await driver.findElement(By.id('signinsignuplandingpage'))
    loginBtn.click()

    // Wait for 1 seconds
    await new Promise((resolve) => setTimeout(resolve, 1000))

    const emailLoginInput = await driver.findElement(
        By.xpath('//*[@id="username"]')
    )
    await emailLoginInput.sendKeys(email)
    const pwdLoginInput = await driver.findElement(
        By.xpath('//*[@id="password"]')
    )
    await pwdLoginInput.sendKeys(password)
    const signInBtn = await driver.findElement(
        By.xpath(
            '/html/body/div/main/section/div/div[2]/div/form/div[3]/button'
        )
    )
    signInBtn.click()
    await new Promise((resolve) => setTimeout(resolve, 2000))

    await driver.get('http://localhost:3000/Shipments')
    await new Promise((resolve) => setTimeout(resolve, 2000))

    await driver.wait(until.elementLocated(By.css('tbody tr:last-child')), 10000);

    const lastRow2 = await driver.findElement(By.css('tbody tr:last-child'));
    const shipmentIdElement = await lastRow2.findElement(By.css('td:first-child div'));
    const lastShipmentId = await shipmentIdElement.getText();

    await driver.get(`http://localhost:3000/ShipmentDetails?shipmentId=${lastShipmentId}`);

    // Wait for 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))

    assert.strictEqual(
        await driver.getCurrentUrl(),
        `http://localhost:3000/ShipmentDetails?shipmentId=${lastShipmentId}`
    )

    await new Promise((resolve) => setTimeout(resolve, 2000))


    const expectedPickupAddress = '123 Main Street, Longueuil, J4K 4P3, CA';
    const expectedDestinationAddress = '1123 Saint Boulevard, New York, NY 10014, USA';
    const expectedShipmentName = 'Shipment Test';
    const expectedFullName = 'Do Cherry';
    const expectedEmailAddress = 'calebliu3@gmail.com';
    const expectedPhoneNumber = '123-345-4567';
    const expectedShipmentStatus = 'QUOTED';
    const expectedApproximateWeight = '0';
    const expectedMovingDate = '2023-12-31';

    const actualShipmentId = await driver.findElement(By.name('DetailShipmentId'));
    assert.notStrictEqual(actualShipmentId, null, 'shipmentId is null');

    const pickupAddress = await driver.findElement(By.id('PickupAddress'));
    assert.strictEqual(await pickupAddress.getAttribute('value'), expectedPickupAddress, 'pickupAddress assertion failed');

    const destinationAddress = await driver.findElement(By.id('DestinationAddress'));
    assert.strictEqual(await destinationAddress.getAttribute('value'), expectedDestinationAddress, 'destinationAddress assertion failed');

    const fullName = await driver.findElement(By.id('FullName'));
    assert.strictEqual(await fullName.getAttribute('value'), expectedFullName, 'fullName assertion failed');

    const emailAddress = await driver.findElement(By.id('Email'));
    assert.strictEqual(await emailAddress.getAttribute('value'), expectedEmailAddress, 'emailAddress assertion failed');

    const phoneNumber = await driver.findElement(By.id('PhoneNumber'));
    assert.strictEqual(await phoneNumber.getAttribute('value'), expectedPhoneNumber, 'phoneNumber assertion failed');

    const shipmentName = await driver.findElement(By.id('ShipmentName'));
    assert.strictEqual(await shipmentName.getAttribute('value'), expectedShipmentName, 'shipmentName assertion failed');

    const shipmentStatus = await driver.findElement(By.id('ShipmentStatus'));
    assert.strictEqual(await shipmentStatus.getAttribute('value'), expectedShipmentStatus, 'shipmentStatus assertion failed');

    const approximateWeight = await driver.findElement(By.id('ApproximateWeight'));
    assert.strictEqual(await approximateWeight.getAttribute('value'), expectedApproximateWeight, 'approximateWeight assertion failed');

    const movingDate = await driver.findElement(By.id('ExpectedMovingDate'));
    assert.strictEqual(await movingDate.getAttribute('value'), expectedMovingDate, 'expectedMovingDate assertion failed');

    await new Promise((resolve) => setTimeout(resolve, 5000))

    // Close the browser after waiting for another 2 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))
    await driver.quit()
}
test_case()
