const { By, Builder } = require('selenium-webdriver')
require('chromedriver')
const assert = require('assert')

async function test_case() {
    const email = "truckdriver@movingexpress.com"
    const password = "Password*"
    let driver = await new Builder().forBrowser('firefox').build()
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
    await driver.sleep(5000);

    const myShipmentsBtn = await driver.findElement(By.xpath('/html/body/div/div[1]/div/div[1]/div/div[4]/button'));
    myShipmentsBtn.click();
    await driver.sleep(2000);

    const tripleBtn = await driver.findElement(By.xpath('//*[@id="{shipment.shipmentId}-TripleBtn"]'));
    tripleBtn.click();

    await driver.sleep(1000);

    const generateShipmentManifestBtn = await driver.findElement(By.xpath('//*[@id="editItemBtn"]'));
    generateShipmentManifestBtn.click();

    await driver.sleep(2000);

    //assert that a pdf is generated and a new tab is opened
    const tabs = await driver.getAllWindowHandles();
    assert.equal(tabs.length, 2);
    await driver.quit();
}
test_case()