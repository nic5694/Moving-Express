const { By, Builder, until } = require('selenium-webdriver');
require('chromedriver');
const assert = require('assert');

const email = "john@example.com";
const password = "Psswd123!";

async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build();
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
    const viewShipmentBtn = await driver.findElement(By.xpath('//*[@id="root"]/div[1]/div[2]/div[2]/div/div/div[3]/button'));
    viewShipmentBtn.click();
    await driver.sleep(2000);
    const addInventoryBtn = await driver.findElement(By.xpath('/html/body/div/div[1]/div[3]/div/div[1]/div[2]/div/button'));
    addInventoryBtn.click();
    await driver.sleep(1000);
    const inventoryNameInput = await driver.findElement(By.xpath('/html/body/div/div[1]/div[2]/div/div/div[2]/div/div[1]/div[1]/input'));
    inventoryNameInput.sendKeys('InventoryPQR');
    const inventoryDescInput = await driver.findElement(By.xpath('/html/body/div/div[1]/div[2]/div/div/div[2]/div/div[2]/textarea'));
    inventoryDescInput.sendKeys('InventoryPQR');
    await driver.sleep(1000);
    const submitInventoryBtn = await driver.findElement(By.xpath('/html/body/div/div[1]/div[2]/div/div/div[3]/button'));
    submitInventoryBtn.click();
    await driver.sleep(3000);
}
test_case()