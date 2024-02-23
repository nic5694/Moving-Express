const { By, Builder, until } = require('selenium-webdriver');
const assert = require('assert');

const email = "truckdriver@movingexpress.com";
const password = "Password*";

const shipmentId = "2872a01b-7a51-4790-8b9f-0bb849f8ec49"
const validTruckVin = '1FMJU2A53CEF92575'

async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build();

    await driver.get('http://localhost:3000/');

    await driver.sleep(1000);

    await driver.findElement(By.id('signinsignuplandingpage')).click();

    // Wait for 1 second
    await driver.sleep(1000);

    const emailLoginInput = await driver.findElement(By.xpath('//*[@id="username"]'));
    await emailLoginInput.sendKeys(email);

    const pwdLoginInput = await driver.findElement(By.xpath('//*[@id="password"]'));
    await pwdLoginInput.sendKeys(password);

    const signInBtn = await driver.findElement(By.xpath('/html/body/div/main/section/div/div[2]/div/form/div[2]/button'));
    signInBtn.click();

    // Wait for 5 second
    await driver.sleep(10000);

    await driver.findElement(By.id(shipmentId + "-assignBtn")).click();

    await driver.sleep(2000);

    const selectVin = await driver.findElement(By.id('truckVin'));

    await selectVin.click()

    await driver.sleep(2000);

    const VinOption = await driver.findElement(By.id(validTruckVin));
    await VinOption.click();

    await driver.sleep(2000);

    // click on assign button 

    await driver.findElement(By.id('assignBtn')).click();

    await driver.sleep(2000);

    // check tostify if its sucessful 
    const toastifyDiv = await driver.findElement(
        By.className('Toastify__toast')
    )
    const classAttribute = await toastifyDiv.getAttribute('class')
    // Assert that the class attribute contains the expected class
    assert(
        classAttribute.includes('Toastify__toast--success'), 'Expected class not found'
    )

    const toastText = await toastifyDiv.getText();

    assert(toastText, 'Assigned shipment successfully!')

    const assignedShipmentBtn = await driver.findElement(By.xpath('//*[@id="root"]/div[1]/div/div[1]/div/div[4]/button'));
    assignedShipmentBtn.click();

    await driver.sleep(5000);

    const tripleBtn = await driver.findElement(By.id(shipmentId + '-TripleBtn'))
    tripleBtn.click()

    await driver.sleep(2000);

    const unassignBtnOption = await driver.findElement(By.id('UnassignBtn'))
    unassignBtnOption.click()

    await driver.sleep(5000);

    const actualShipmentId = await driver.findElement(By.id('unassignShipmentId-' + shipmentId));
    assert.strictEqual(await actualShipmentId.getAttribute('value'), shipmentId, 'shipmentId assertion failed');

    const shipmentNameInput = await driver.findElement(By.id('unassignShipmentName-' + shipmentId))
    await shipmentNameInput.sendKeys('Test Shipment')

    const unassignShipmentBtn = await driver.findElement(By.id('unassignShipmentBtn-' + shipmentId))
    unassignShipmentBtn.click()

    await driver.sleep(5000);

    // check tostify if its sucessful 
    const toastifyDiv2 = await driver.findElement(
        By.className('Toastify__toast')
    )
    const classAttribute2 = await toastifyDiv2.getAttribute('class')
    // Assert that the class attribute contains the expected class
    assert(
        classAttribute2.includes('Toastify__toast--success'), 'Expected class not found'
    )

    const toastText2 = await toastifyDiv2.getText();

    assert(toastText2, 'Shipment unassigned successfully!')

    // Wait for 5 seconds (adjust as needed)
    await driver.sleep(3000);
    await driver.quit();
}
test_case();