const { Builder, By, Key, until } = require('selenium-webdriver');
const assert = require('assert');

let inventoryId = '8a4b1e77-2fb3-4d3c-9e6c-af9d8b1f3613'
let itemId = '1077fb29-e9d3-4e89-a40d-18838566471d'
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
    await itemNameInput.clear();
    await itemNameInput.sendKeys('La Joconde')

    const itemSelectInput = await driver.findElement(
        By.id('ItemSelectInput')
    )
    itemSelectInput.click()
    await new Promise((resolve) => setTimeout(resolve, 500))
    itemSelectInput.findElement(By.id('ItemOption')).click()

    const priceInput = await driver.findElement(By.name('PriceInput'))
    await priceInput.clear();
    await priceInput.sendKeys(123400.0)

    const descriptionInput = await driver.findElement(By.name('descriptionInput'))
    await descriptionInput.clear();
    await descriptionInput.sendKeys('La Joconde was painted from a lady who was reknowned to not smile easily')

    const handlingInstructionsInput = await driver.findElement(By.name('handlingInstructionsInput'))
    await handlingInstructionsInput.clear();
    await handlingInstructionsInput.sendKeys('This painting should be handled with LOTS LOTS LOTS of care!')

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