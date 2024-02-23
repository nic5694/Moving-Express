const { Builder, By, Key, until } = require('selenium-webdriver');
const assert = require('assert');

const shipmentId = "2872a01b-7a51-4790-8b9f-0bb849f8ec49"
async function test_case() {
    const email = "truckdriver@movingexpress.com"
    const password = "Password*"
    let driver = await new Builder().forBrowser('chrome').build()
    await driver.get('http://localhost:3000/');
    await driver.findElement(By.id('signinsignuplandingpage')).click();

    // Wait for 1 second
    await driver.sleep(1000);

    const emailLoginInput = await driver.findElement(By.xpath('//*[@id="username"]'));
    await emailLoginInput.sendKeys(email);

    const pwdLoginInput = await driver.findElement(By.xpath('//*[@id="password"]'));
    await pwdLoginInput.sendKeys(password);

    const signInBtn = await driver.findElement(By.xpath('/html/body/div/main/section/div/div[2]/div/form/div[2]/button'));
    signInBtn.click();

    // Wait for 5 seconds (adjust as needed)
    await driver.sleep(5000);

    const expectedShipmentName = "Test Shipment"
    const actualShipmentName = await driver.findElement(By.id(shipmentId + '-name'));
    assert.strictEqual(await actualShipmentName.getText(), expectedShipmentName, 'shipmentName assertion failed');

    const expectedEmail = "liucaleb8@gmail.com"
    const actualEmail = await driver.findElement(By.id(shipmentId + '-email'));
    assert.strictEqual(await actualEmail.getText(), expectedEmail, 'shipmentName assertion failed');

    const expectedPhoneNumber = "789-555-3123"
    const actualPhoneNumber = await driver.findElement(By.id(shipmentId + '-phoneNumber'));
    assert.strictEqual(await actualPhoneNumber.getText(), expectedPhoneNumber, 'shipmentName assertion failed');

    const expectedFirstName = "Emily"
    const actualFirstName = await driver.findElement(By.id(shipmentId + '-firstName'));
    assert.strictEqual(await actualFirstName.getText(), expectedFirstName, 'shipmentName assertion failed');

    const expectedLastName = "Davis"
    const actualLastName = await driver.findElement(By.id(shipmentId + '-lastName'));
    assert.strictEqual(await actualLastName.getText(), expectedLastName, 'shipmentName assertion failed');

    const expectedStatus = "QUOTED"
    const actualStatus = await driver.findElement(By.id('unassigned-' + shipmentId));
    assert.strictEqual(await actualStatus.getText(), expectedStatus, 'shipmentName assertion failed');
    await driver.quit();
}
test_case()