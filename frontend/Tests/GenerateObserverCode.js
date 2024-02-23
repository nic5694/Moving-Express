const { By, Builder } = require('selenium-webdriver');
const assert = require('assert');

let email = 'john@example.com';
let password = 'Psswd123!';
let shipmentId = 'c0a80121-7f5e-4d77-a5a4-5d41b04f5a23';

async function testGenerateObserverCode() {
    let driver = await new Builder().forBrowser('chrome').build();

    try {
        await driver.get('http://localhost:3000');
        await driver.sleep(1000);

        // Log in
        const loginBtn = await driver.findElement(By.id('signinsignuplandingpage'));
        loginBtn.click();

        await driver.sleep(1000);

        const emailLoginInput = await driver.findElement(By.xpath('//*[@id="username"]'));
        await emailLoginInput.sendKeys(email);

        const pwdLoginInput = await driver.findElement(By.xpath('//*[@id="password"]'));
        await driver.sleep(3000);
        await pwdLoginInput.sendKeys(password);

        const signInBtn = await driver.findElement(By.xpath('/html/body/div/main/section/div/div[2]/div/form/div[2]/button'));
        signInBtn.click();

        await driver.sleep(3000);

        // Navigate to Shipment Details
        await driver.get(`http://localhost:3000/ShipmentDetails?shipmentId=${shipmentId}`);
        await driver.sleep(3000);

        // Interacting with ObserverCodeComponent
        const observerNameInput = await driver.findElement(By.id('observerCodeName'));
        await observerNameInput.sendKeys('Test Observer');
        await driver.sleep(3000);

        const permissionSelect = await driver.findElement(By.id('observerCodePermission'));
        await permissionSelect.sendKeys('FULL');
        await driver.sleep(3000);

        const generateCodeButton = await driver.findElement(By.xpath("//button[contains(text(), 'Generate Code')]"));
        await generateCodeButton.click();
        await driver.sleep(3000);

        // Assertions
        let successMessage = await driver.findElement(By.xpath("//*[contains(text(), 'Observer Code generated successfully!')]"));
        assert.strictEqual(await successMessage.isDisplayed(), true, 'Success message not displayed');

        // Clean up
        await driver.sleep(2000);
    } finally {
        await driver.quit();
    }
}

testGenerateObserverCode();
