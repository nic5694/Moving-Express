const { By, Key, Builder, until } = require('selenium-webdriver')
require('chromedriver')

const { generateKey } = require('crypto')

function generateRandomEmail() {
    const randomString = Math.random().toString(36).substring(2, 10)
    const domain = 'gmail.com'
    const email = `user_${randomString}@${domain}`
    return email
}
let email = generateRandomEmail()
const password = '2_PQBPVC-ggm-'
async function signin_signout_testcase() {
    // email = generateRandomEmail();
    let driver = await new Builder().forBrowser('chrome').build()

    await driver.get('http://localhost:3000/')

    const loginBtn = await driver.findElement(By.id('signinsignuplandingpage'))
    loginBtn.click()

    // Wait for 1 seconds
    await new Promise((resolve) => setTimeout(resolve, 1000))

    const signUpLink = await driver.wait(
        until.elementLocated(
            By.xpath('/html/body/div/main/section/div/div[2]/div/div[1]/p/a')
        ),
        10000
    )

    signUpLink.click()

    // Wait for 1 seconds
    await new Promise((resolve) => setTimeout(resolve, 1000))

    const emailsignUpInput = await driver.findElement(By.name('email'))
    await emailsignUpInput.sendKeys(email)

    const pwdSignUpInput = await driver.wait(
        until.elementLocated(
            By.xpath(
                '/html/body/div/main/section/div/div/div/form/div[1]/div[1]/div/div[2]/div/input'
            )
        ),
        1000
    )
    await pwdSignUpInput.sendKeys(password)

    const continueBtn = await driver.wait(
        until.elementLocated(
            By.xpath(
                '/html/body/div/main/section/div/div/div/form/div[3]/button'
            )
        ),
        1000
    )
    continueBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 2000))
    const AcceptBtn = await driver.findElement(
        By.xpath(
            '/html/body/div/main/section/div/div/div/form/div[3]/button[1]'
        )
    )
    AcceptBtn.click()

    await new Promise((resolve) => setTimeout(resolve, 2000))
    const loginBtnLogin = await driver.findElement(
        By.id('signinsignuplandingpage')
    )
    loginBtnLogin.click()
    // Wait for 1 seconds
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
    await new Promise((resolve) => setTimeout(resolve, 2000))
    await driver.quit()
}

signin_signout_testcase()
