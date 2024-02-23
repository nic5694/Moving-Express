const { By, Builder, until } = require('selenium-webdriver');
require('chromedriver');
const assert = require('assert');

const email = "john@example.com";
const password = "Psswd123!";

async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build();

    //try {
        await driver.get('http://localhost:3000/');
        await driver.findElement(By.id('signinsignuplandingpage')).click();

        // Wait for 1 second
        await driver.sleep(1000);

        const emailLoginInput = await driver.findElement(By.xpath('//*[@id="username"]'));
        await emailLoginInput.sendKeys(email);

        const pwdLoginInput = await driver.findElement(By.xpath('//*[@id="password"]'));
        await pwdLoginInput.sendKeys(password);

        const signInBtn = await driver.findElement(By.xpath('/html/body/div/main/section/div/div[2]/div/form/div[3]/button'));
        signInBtn.click();

        // Wait for 5 seconds (adjust as needed)
        await driver.sleep(5000);

        const shipmentsBtn = await driver.findElement(By.xpath('/html/body/div/div[1]/div[2]/div[1]/div[3]/div[2]/div[3]/a'));
        shipmentsBtn.click();

        // Wait for 2 seconds
        await driver.sleep(2000);

        const quoteStatus = await driver.findElement(By.xpath('//*[@id="root"]/div[1]/div[2]/div[2]/div/div/div[2]/div[1]/span[2]'));
        assert.strictEqual(await quoteStatus.getText(), 'QUOTED', 'quoteStatus assertion failed');

        // Wait for 2 seconds
        await driver.sleep(2000);

        const viewShipmentBtn = await driver.findElement(By.xpath('//*[@id="root"]/div[1]/div[2]/div[2]/div/div/div[3]/button'));
        viewShipmentBtn.click();

        // Wait for 2 seconds
        await driver.sleep(2000);

        const cancelShipmentBtn = await driver.findElement(By.xpath('//*[@id="GenerateShipmentReportBtn"]'));
        cancelShipmentBtn.click();

        await driver.wait(until.alertIsPresent());
        const alert = await driver.switchTo().alert();
        await alert.sendKeys('ShipmentPQR');
        await alert.accept();
        await driver.sleep(2000);
//         assert.strictEqual(await alert.getText(), 'Are you sure you want to cancel this shipment? Enter Shipment Name: "ShipmentPQR" to confirm.', 'Alert message assertion failed');
// // Handle the alert using JavaScript executor
//     await alert.sendKeys('ShipmentPQR');
//     await alert.accept();
}

test_case();
