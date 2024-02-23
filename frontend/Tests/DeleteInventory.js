const { By, Builder } = require('selenium-webdriver')
require('chromedriver')
const assert = require('assert')

let email = 'john@example.com'
let password = 'Psswd123!'
let shipmentId = 'c0a80121-7f5e-4d77-a5a4-5d41b04f5a23'
let inventoryId = '8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613'

async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build()

    await driver.get('http://localhost:3000')

    
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // assert.strictEqual(
    //     await driver.getCurrentUrl(),
    //     'http://localhost:3000'
    // )

    const loginBtn = await driver.findElement(By.id('signinsignuplandingpage'))
    loginBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 1000))

    const emailLoginInput = await driver.findElement(
        By.xpath('//*[@id="username"]')
    )
    
    await emailLoginInput.sendKeys(email)
    const pwdLoginInput = await driver.findElement(
        By.xpath('//*[@id="password"]')
    )
    
    await pwdLoginInput.sendKeys(password)
    const signInBtn = await driver.findElement(
        By.xpath(
            '/html/body/div/main/section/div/div[2]/div/form/div[3]/button'
        )
    )
    signInBtn.click()
    
    await new Promise((resolve) => setTimeout(resolve, 3000))

    await driver.get('http://localhost:3000/shipments')

    await new Promise((resolve) => setTimeout(resolve, 3000))

    await driver.get('http://localhost:3000/ShipmentDetails?shipmentId='+ shipmentId)

    await new Promise((resolve) => setTimeout(resolve, 3000))

    const inventoryTripleButn = await driver.findElement(By.id(inventoryId+'_TripleBtn'))
    await inventoryTripleButn.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))

    const detailsBtnOption = await driver.findElement(By.id('deleteBtnOption'))
    await detailsBtnOption.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))

    let deleteBtn = await driver.findElement(By.id('deleteInventoryButton'));
    
    await deleteBtn.click();
    await new Promise(resolve => setTimeout(resolve, 3000));

    await driver.quit()

}
test_case()