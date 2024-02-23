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

    await driver.get('http://localhost:3000/ShipmentDetails?shipmentId=' + shipmentId)

    await new Promise((resolve) => setTimeout(resolve, 3000))

    const addItem = await driver.findElement(By.id(`addItem-${inventoryId}`));
    addItem.click()

    await new Promise((resolve) => setTimeout(resolve, 3000))

    const itemNameInput = await driver.findElement(By.name('ItemNameInput'))
    await itemNameInput.sendKeys('La Joconde')

    const itemSelectInput = await driver.findElement(
        By.id('ItemSelectInput')
    )
    itemSelectInput.click()
    await new Promise((resolve) => setTimeout(resolve, 500))
    itemSelectInput.findElement(By.id('ItemOption')).click()

    const priceInput = await driver.findElement(By.name('PriceInput'))
    await priceInput.sendKeys(100000.0)

    const weightInput = await driver.findElement(By.name('WeightInput'))
    await weightInput.sendKeys(10.0)

    const descriptionInput = await driver.findElement(By.name('descriptionInput'))
    await descriptionInput.sendKeys('La Joconde was painted by Leonardo Da Vinci')

    const handlingInstructionsInput = await driver.findElement(By.name('handlingInstructionsInput'))
    await handlingInstructionsInput.sendKeys('This painting should be handled with extreme care!')

    const addItemBtn = await driver.findElement(By.id('AddItemBtn'))
    await addItemBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 2000))
    assert.strictEqual(
        await driver.getCurrentUrl(),
        'http://localhost:3000/ShipmentDetails?shipmentId=' + shipmentId
    )

    const toastifyDiv = await driver.findElement(
        By.className('Toastify__toast')
    )
    const classAttribute = await toastifyDiv.getAttribute('class')
    // Assert that the class attribute contains the expected class
    assert(
        classAttribute.includes('Toastify__toast--success'),
        'Expected class not found'
    )

    // Close the browser after waiting for another 2 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))
    await driver.quit()
}
test_case()