const { By, Builder } = require('selenium-webdriver')
require('chromedriver')
const assert = require('assert')

async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build()

    await driver.get('http://localhost:3000/Reviewer')

    // Wait for 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))

    assert.strictEqual(
        await driver.getCurrentUrl(),
        'http://localhost:3000/Reviewer'
    )

    const viewBtn = await driver.findElement(By.id('btn-a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6'));
    viewBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 2000))


    const expectedQuoteId = 'a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6';
    const expectedPickupStreetAddress = '123 Main St';
    const expectedPickupCity = 'Cityville';
    const expectedPickupCountry = 'CA';
    const expectedPickupPostalCode = 'M1A 1A1';
    const expectedPickupNumberOfRooms = 3;
    const expectedPickupElevator = true;
    const expectedPickupBuildingType = 'Apartment';
    const expectedDestinationStreetAddress = '456 Oak St';
    const expectedDestinationCity = 'Townsville';
    const expectedDestinationCountry = 'USA';
    const expectedDestinationPostalCode = 'M5V 2H1';
    const expectedDestinationNumberOfRooms = 4;
    const expectedDestinationElevator = false;
    const expectedDestinationBuildingType = 'House';
    const expectedFirstName = 'John';
    const expectedLastName = 'Doe';
    const expectedEmailAddress = 'emily.d@example.com';
    const expectedPhoneNumber = '123-555-1234';
    const expectedMovingDate = '2023-12-15';
    const expectedComment = 'Moving details for John Doe';
    const expectedShipmentName = 'ShipmentXYZ';

    const quoteId = await driver.findElement(By.name('DetailQuoteId'));
    assert.strictEqual(await quoteId.getAttribute('value'), expectedQuoteId, 'quoteId assertion failed')

    const pickupElevator = await driver.findElement(By.id('PickupYes'));
    assert.strictEqual(await pickupElevator.isSelected(), expectedPickupElevator, 'pickupElevator assertion failed');

    const destinationElevator = await driver.findElement(By.id('PickupNo'));
    assert.strictEqual(await destinationElevator.isSelected(), expectedDestinationElevator, 'destinationElevator assertion failed');

    const contactMethod = await driver.findElement(By.id('PreferredEmail'));
    assert.strictEqual(await contactMethod.isSelected(), true, 'contactMethod assertion failed');

    const pickupAddress = await driver.findElement(By.id('PickupAddress'));
    assert.strictEqual(await pickupAddress.getAttribute('value'), expectedPickupStreetAddress + ', ' + expectedPickupCountry, 'pickupAddress assertion failed');

    const pickupCity = await driver.findElement(By.id('PickupCity'));
    assert.strictEqual(await pickupCity.getAttribute('value'), expectedPickupCity, 'pickupCity assertion failed');

    const pickupPostalCode = await driver.findElement(By.id('PickupPostalCode'));
    assert.strictEqual(await pickupPostalCode.getAttribute('value'), expectedPickupPostalCode, 'pickupPostalCode assertion failed');

    const pickupNumberOfRooms = await driver.findElement(By.id('PickupNumberOfRooms'));
    assert.strictEqual(await pickupNumberOfRooms.getAttribute('value'), expectedPickupNumberOfRooms.toString(), 'pickupNumberOfRooms assertion failed');

    const pickupBuildingType = await driver.findElement(By.id('PickupBuildingType'));
    assert.strictEqual(await pickupBuildingType.getAttribute('value'), expectedPickupBuildingType, 'pickupBuildingType assertion failed');

    const destinationAddress = await driver.findElement(By.id('DestinationAddress'));
    assert.strictEqual(await destinationAddress.getAttribute('value'), expectedDestinationStreetAddress + ', ' + expectedDestinationCountry, 'destinationAddress assertion failed');

    const destinationCity = await driver.findElement(By.id('DestinationCity'));
    assert.strictEqual(await destinationCity.getAttribute('value'), expectedDestinationCity, 'destinationCity assertion failed');

    const destinationPostalCode = await driver.findElement(By.id('DestinationPostalCode'));
    assert.strictEqual(await destinationPostalCode.getAttribute('value'), expectedDestinationPostalCode, 'destinationPostalCode assertion failed');

    const destinationNumberOfRooms = await driver.findElement(By.id('DestinationNumberOfRooms'));
    assert.strictEqual(await destinationNumberOfRooms.getAttribute('value'), expectedDestinationNumberOfRooms.toString(), 'destinationNumberOfRooms assertion failed');

    const destinationBuildingType = await driver.findElement(By.id('DestinationBuildingType'));
    assert.strictEqual(await destinationBuildingType.getAttribute('value'), expectedDestinationBuildingType, 'destinationBuildingType assertion failed');

    const firstName = await driver.findElement(By.id('FirstName'));
    assert.strictEqual(await firstName.getAttribute('value'), expectedFirstName, 'firstName assertion failed');

    const lastName = await driver.findElement(By.id('LastName'));
    assert.strictEqual(await lastName.getAttribute('value'), expectedLastName, 'lastName assertion failed');

    const emailAddress = await driver.findElement(By.id('Email'));
    assert.strictEqual(await emailAddress.getAttribute('value'), expectedEmailAddress, 'emailAddress assertion failed');

    const phoneNumber = await driver.findElement(By.id('PhoneNumber'));
    assert.strictEqual(await phoneNumber.getAttribute('value'), expectedPhoneNumber, 'phoneNumber assertion failed');

    const comment = await driver.findElement(By.id('Comment'));
    assert.strictEqual(await comment.getAttribute('value'), expectedComment, 'comment assertion failed');

    const shipmentName = await driver.findElement(By.id('ShipmentName'));
    assert.strictEqual(await shipmentName.getAttribute('value'), expectedShipmentName, 'shipmentName assertion failed');

    const movingDate = await driver.findElement(By.id('ExpectedMovingDate'));
    assert.strictEqual(await movingDate.getAttribute('value'), expectedMovingDate, 'expectedMovingDate assertion failed');

    await new Promise((resolve) => setTimeout(resolve, 5000))

    const close = await driver.findElement(By.id('CloseDetail'));
    close.click()

    // Close the browser after waiting for another 2 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))
    await driver.quit()
}
test_case()
