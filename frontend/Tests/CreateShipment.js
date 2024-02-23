const { By, Key, Builder, until } = require('selenium-webdriver')
require('chromedriver')


const email = "shipmentestimator@movingexpress.com"
const password = "Password*"
async function testCreateShipment(){
    let driver = await new Builder().forBrowser('chrome').build()

    await driver.get('http://localhost:3000/')

    const loginBtn = await driver.findElement(By.id('signinsignuplandingpage'))
    loginBtn.click()

    // Wait for 1 seconds
    await new Promise((resolve) => setTimeout(resolve, 1000))

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
    await new Promise((resolve) => setTimeout(resolve, 5000))
    const viewShipmentBtn = await driver.wait(
        until.elementLocated(
        By.xpath(
            '/html/body/div/div[1]/div/div[4]/div[2]/table/tbody/tr[1]/td[7]/button'
        )
        ),
        2000
    )
    viewShipmentBtn.click()

    const requestQuoteBtn = await driver.wait(
        until.elementLocated(
        By.xpath(
            '/html/body/div/div[1]/div/div[4]/div[3]/div/div[16]/div[1]/button'
        )
        ), 1000
    )
    requestQuoteBtn.click()

    const logoutBtn = await driver.wait(
        until.elementLocated(
            By.xpath(
                '/html/body/div/div[1]/div/div[1]/div/div[4]/form/button'
            )
        ),
        2000
    )
    logoutBtn.click()
}
testCreateShipment()