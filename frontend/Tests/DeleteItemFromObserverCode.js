const { Builder, By, Key, until } = require('selenium-webdriver');
const assert = require('assert');

let inventoryId = '8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613'
let itemId = '73ccdeb8-e009-4d10-8101-8f1d74c1eff0'
let observerCode = 'T4XLY8'

async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build()

    await driver.get('http://localhost:3000')

    await new Promise((resolve) => setTimeout(resolve, 1000))

    const observerCodeInput = await driver.findElement(By.id('observerCodeInput'))
    await observerCodeInput.sendKeys(observerCode)

    const opnObserverCodeBtn = await driver.findElement(By.id(`observerCodeBtn`));
    await opnObserverCodeBtn.click();

    await new Promise((resolve) => setTimeout(resolve, 5000))

    const inventoryTripleButn = await driver.findElement(By.id(inventoryId + '-TripleBtn'))
    await inventoryTripleButn.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))

    const opnBtn = await driver.findElement(By.id(`openBtnOption`));
    await opnBtn.click();

    await new Promise((resolve) => setTimeout(resolve, 5000))

    const itemTripleButn = await driver.findElement(By.id(itemId + '-TripleBtn'))
    await itemTripleButn.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))
    const detailsBtn = await driver.findElement(By.id('deleteItemBtn'))
    await detailsBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))
    const actualItemId = await driver.findElement(By.id('ItemIdInput'))
    assert.strictEqual(await actualItemId.getAttribute('value'), itemId, 'itemId assertion failed');

    const expectedItemName = 'ASD'
    const actualItemName = await driver.findElement(By.id('ItemNameInput'))
    assert.strictEqual(await actualItemName.getAttribute('value'), expectedItemName, 'item name assertion failed');

    const expectedItemType = 'ITEM'
    const actualItemType = await driver.findElement(By.id(`ItemSelectInput`))
    assert.strictEqual(await actualItemType.getAttribute('value'), expectedItemType, 'item type assertion failed');

    const expectedDescription = 'ASD'
    const actualItemDescription = await driver.findElement(By.id(`descriptionInput`))
    assert.strictEqual(await actualItemDescription.getAttribute('value'), expectedDescription, 'item description assertion failed');

    const expectedHandlingInstructions = 'ASD'
    const actualHandlingInstructions = await driver.findElement(By.id(`handlingInstructionsInput`))
    assert.strictEqual(await actualHandlingInstructions.getAttribute('value'), expectedHandlingInstructions, 'item handlingInstructions assertion failed');

    const expectedPrice = '$1223'
    const actualPrice = await driver.findElement(By.id(`PriceInput`))
    assert.strictEqual(await actualPrice.getAttribute('value'), expectedPrice, 'item price assertion failed');

    await new Promise((resolve) => setTimeout(resolve, 3000))
    const deleteBtn = await driver.findElement(By.id('confirmDeleteItemBtn'))
    await deleteBtn.click()

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