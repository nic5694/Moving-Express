const { Builder, By, Key, until } = require('selenium-webdriver');
const assert = require('assert');

let email = 'john@example.com'
let password = 'Psswd123!'
let shipmentId = 'c0a80121-7f5e-4d77-a5a4-5d41b04f5a23'
let inventoryId = '8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613'
let itemId = '1077fb29-e9d3-4e89-a40d-18838566471d'

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

    const opnBtn = await driver.findElement(By.id(`openBtnOption`));
    await opnBtn.click();

    await new Promise((resolve) => setTimeout(resolve, 5000))

    const itemTripleButn = await driver.findElement(By.id(itemId + '-TripleBtn'))
    await itemTripleButn.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))
    const editItemBtn = await driver.findElement(By.id('editItemBtn'))
    await editItemBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))
    const actualItemId = await driver.findElement(By.id('ItemIdInput'))
    assert.strictEqual(await actualItemId.getAttribute('value'), itemId, 'itemId assertion failed');

    const expectedItemName = 'La Joconde'
    const actualItemName = await driver.findElement(By.id('ItemNameInput'))
    assert.strictEqual(await actualItemName.getAttribute('value'), expectedItemName, 'item name assertion failed');

    const expectedItemType = 'ITEM'
    const actualItemType = await driver.findElement(By.id(`ItemSelectInput`))
    assert.strictEqual(await actualItemType.getAttribute('value'), expectedItemType, 'item type assertion failed');

    const expectedDescription = 'asd'
    const actualItemDescription = await driver.findElement(By.id(`descriptionInput`))
    assert.strictEqual(await actualItemDescription.getAttribute('value'), expectedDescription, 'item description assertion failed');

    const expectedHandlingInstructions = 'asd'
    const actualHandlingInstructions = await driver.findElement(By.id(`handlingInstructionsInput`))
    assert.strictEqual(await actualHandlingInstructions.getAttribute('value'), expectedHandlingInstructions, 'item handlingInstructions assertion failed');

    const expectedPrice = '123123'
    const actualPrice = await driver.findElement(By.id(`PriceInput`))
    assert.strictEqual(await actualPrice.getAttribute('value'), expectedPrice, 'item price assertion failed');

    await new Promise((resolve) => setTimeout(resolve, 3000))

    const itemNameInput = await driver.findElement(By.name('ItemNameInput'))
    await itemNameInput.sendKeys('La Joconde')

    const itemSelectInput = await driver.findElement(
        By.id('ItemSelectInput')
    )
    itemSelectInput.click()
    await new Promise((resolve) => setTimeout(resolve, 500))
    itemSelectInput.findElement(By.id('BoxOption')).click()

    const priceInput = await driver.findElement(By.name('PriceInput'))
    await priceInput.sendKeys(100000.0)

    const descriptionInput = await driver.findElement(By.name('descriptionInput'))
    await descriptionInput.sendKeys('La Joconde was painted by Leonardo Da Vinci')

    const handlingInstructionsInput = await driver.findElement(By.name('handlingInstructionsInput'))
    await handlingInstructionsInput.sendKeys('This painting should be handled with extreme care!')

    await new Promise((resolve) => setTimeout(resolve, 3000))

    const saveBtn = await driver.findElement(By.id('saveItemBtn'))
    await saveBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))
    const toastifyDiv = await driver.findElement(
        By.className('Toastify__toast')
    )
    const classAttribute = await toastifyDiv.getAttribute('class')
    assert(
        classAttribute.includes('Toastify__toast--success'),
        'Expected class not found'
    )

    // Close the browser after waiting for another 2 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))
    await driver.quit()
}
test_case()