const { By, Builder } = require('selenium-webdriver');
const assert = require('assert');

let email = 'john@example.com';
let password = 'Psswd123!';
let shipmentId = 'c0a80121-7f5e-4d77-a5a4-5d41b04f5a23';
let observerId = '739d49a0-d326-4606-aaaf-b6fe175303b3';

async function testEditObserverCodePermission() {
    let driver = await new Builder().forBrowser('chrome').build();

    try {
        await driver.get('http://localhost:3000');
        await driver.sleep(1000);

        // Log in
        const loginBtn = await driver.findElement(By.id('signinsignuplandingpage'));
        loginBtn.click();
        await driver.sleep(1000);

        // Enter email and password
        const emailLoginInput = await driver.findElement(By.xpath('//*[@id="username"]'));
        await emailLoginInput.sendKeys(email);
        const pwdLoginInput = await driver.findElement(By.xpath('//*[@id="password"]'));
        await pwdLoginInput.sendKeys(password);
        await driver.sleep(3000);

        // Click sign in
        const signInBtn = await driver.findElement(By.xpath('/html/body/div/main/section/div/div[2]/div/form/div[2]/button'));
        signInBtn.click();
        await driver.sleep(3000);

        // Navigate to Shipment Details
        await driver.get(`http://localhost:3000/ShipmentDetails?shipmentId=${shipmentId}`);
        await driver.sleep(3000);

        // Click the edit icon for the observer code
        const editIcon = await driver.findElement(By.xpath(`//*[@id="${observerId}"]/svg/path[1]`)); 

        await editIcon.click();
        await driver.sleep(3000);

        // Change the permission in the dropdown
        const permissionSelect = await driver.findElement(By.id(`select-${observerId}`));
        await permissionSelect.sendKeys('EDIT'); 

        // Click the save (check mark) icon to confirm changes
        const saveIcon = await driver.findElement(By.xpath(`/html/body/div/div[1]/div[4]/div/div[3]/div[1]/div[3]/button[1]/svg/path`));
        await saveIcon.click();
        await driver.sleep(3000);

        
        // Fraud

        // Clean up
        await driver.sleep(2000);
    } finally {
        await driver.quit();
    }
}

testEditObserverCodePermission();
