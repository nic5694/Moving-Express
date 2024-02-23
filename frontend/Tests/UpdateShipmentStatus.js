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

    // Wait for 5 seconds (adjust as needed)
    await driver.sleep(5000);

    const myShipmentsBtn = await driver.findElement(By.xpath('/html/body/div/div[1]/div/div[1]/div/div[4]/button'));
    myShipmentsBtn.click();
    const tdElements = await driver.findElements(By.className('border px-3'));
    assert.equal(tdElements.length, 2);


    // Update shipment status
    await driver.findElement(By.id('myShipmentsButton')).click();
    await driver.sleep(1000); 

    // Assuming there's an ID or way to open the update status popup
    await driver.findElement(By.id('openUpdateStatusPopupButton')).click();
    
    await driver.sleep(1000); 

    // Update the shipment status
    await driver.findElement(By.id('status')).sendKeys('LOADING');
    await driver.sleep(1000);

    await driver.findElement(By.id('updateStatusButton')).click();
    await driver.sleep(3000);

    assert.equal(await driver.findElement(By.id('status')).getText(), 'LOADING');

    await driver.quit();
}
test_case()