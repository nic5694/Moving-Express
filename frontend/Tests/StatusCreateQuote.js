const { By, Builder } = require('selenium-webdriver')
require('chromedriver')
const assert = require('assert')

async function test_case() {
    let driver = await new Builder().forBrowser('chrome').build()

    await driver.get('http://localhost:3000/Estimator')

    // Wait for 5 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))

    assert.strictEqual(
        await driver.getCurrentUrl(),
        'http://localhost:3000/Estimator'
    )

    await new Promise((resolve) => setTimeout(resolve, 2000))
    const quoteStatus = await driver.findElement(By.id('accepted-a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6'))
    assert.strictEqual(await quoteStatus.getText(), 'ACCEPTED', 'quoteStatus assertion failed');

    await new Promise((resolve) => setTimeout(resolve, 2000))
    const viewBtn = await driver.findElement(By.id('btn-a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6'));
    viewBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 2000))
    const acceptBtn = await driver.findElement(By.id('createBtn'))
    acceptBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 2000))
    const currStatus = await driver.findElement(By.id('created-a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6'))
    assert.strictEqual(await currStatus.getText(), 'CREATED', 'quoteStatus assertion failed');

    // Close the browser after waiting for another 2 seconds
    await new Promise((resolve) => setTimeout(resolve, 2000))
    await driver.quit()
}
test_case()