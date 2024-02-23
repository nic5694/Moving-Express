const { By, Builder } = require('selenium-webdriver')
require('chromedriver')
const assert = require('assert')

let email = 'john@example.com'
let password = 'Psswd123!'
let shipmentId = 'c0a80121-7f5e-4d77-a5a4-5d41b04f5a23'
let inventoryId = '8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613'
let itemId = '69b4be34-f79c-4393-b432-d9ce70a169fc'

async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build()

    await driver.get('http://localhost:3000')

    await new Promise((resolve) => setTimeout(resolve, 1000))

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

    await new Promise((resolve) => setTimeout(resolve, 3000))

    await pwdLoginInput.sendKeys(password)
    const signInBtn = await driver.findElement(
        By.xpath(
            '/html/body/div/main/section/div/div[2]/div/form/div[2]/button'
        )
    )
    signInBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))

    await driver.get('http://localhost:3000/shipments')

    await new Promise((resolve) => setTimeout(resolve, 3000))

    await driver.get('http://localhost:3000/ShipmentDetails?shipmentId=' + shipmentId)

    await new Promise((resolve) => setTimeout(resolve, 3000))

    const inventoryTripleButn = await driver.findElement(By.id(inventoryId + '-TripleBtn'))
    await inventoryTripleButn.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))

    const editBtn = await driver.findElement(By.id(`openBtnOption`));
    await editBtn.click();

    await new Promise((resolve) => setTimeout(resolve, 3000))

    const expectedItemName = 'Vanity'
    const actualItemName = await driver.findElement(By.id(`${itemId}-itemName`))
    assert.strictEqual(await actualItemName.getText(), expectedItemName, 'itemName assertion failed');

    const expectedItemType = 'ITEM'
    const actualItemType = await driver.findElement(By.id(`${itemId}-itemType`))
    assert.strictEqual(await actualItemType.getText(), expectedItemType, 'itemType assertion failed');

    await new Promise((resolve) => setTimeout(resolve, 3000))
    // Close the browser after waiting for another 2 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))
    await driver.quit()
}
test_case()