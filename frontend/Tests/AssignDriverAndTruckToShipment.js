const { By, Builder, until } = require('selenium-webdriver');
require('chromedriver');
const assert = require('assert');

const email = "truckdriver@movingexpress.com";
const password = "Password*";

// its a one time thing
const assignButton = 'a5738ba9-3f98-4a86-a345-b1c0dcccb2d4-assignBtn'

const validTruckVin = '1FMJU2A53CEF92575'


async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build();

    //try {
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
        await driver.sleep(5000);

        await driver.findElement(By.id(assignButton)).click();

        await driver.sleep(2000);

        const selectVin  = await driver.findElement(By.id('truckVin'));

        await selectVin.click()

        await driver.sleep(2000);

        const VinOption  = await driver.findElement(By.id(validTruckVin));
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
            classAttribute.includes('Toastify__toast--success'),'Expected class not found'
        )

        const toastText = await toastifyDiv.getText();

        assert(toastText,'Assigned shipment successfully!')

        // Wait for 5 seconds (adjust as needed)
        await driver.sleep(3000);
        
        await driver.quit();
}
test_case();