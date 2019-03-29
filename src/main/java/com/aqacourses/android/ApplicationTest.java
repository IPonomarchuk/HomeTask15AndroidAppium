package com.aqacourses.android;

import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ApplicationTest {

    // Driver
    private WebDriver driver;
    private WebDriverWait wait;

    // Test string - message
    private final String EMPTY_CART_MESSAGE = "Корзина пуста";

    // Credentials
    private final String EMAIL = "g5444355@nwytg.net";
    private final String PASSWORD = "Temp123";

    // WebElements
    @FindBy(xpath = ".//*[@resource-id='ua.com.rozetka.shop:id/menu_item_header_tv_sign_in']")
    private WebElement signInLink;

    @FindBy(xpath = ".//*[@resource-id='ua.com.rozetka.shop:id/sign_in_et_login']")
    private WebElement emailInput;

    @FindBy(xpath = ".//*[@resource-id='ua.com.rozetka.shop:id/sign_in_et_password']")
    private WebElement passwordInput;

    @FindBy(xpath = ".//*[@resource-id='ua.com.rozetka.shop:id/ll_background']")
    private WebElement signInButton;

    @FindBy(xpath = ".//*[@resource-id='ua.com.rozetka.shop:id/main_tv_to_catalog']")
    private WebElement catalogButton;

    @FindBy(xpath = "//android.widget.TextView[@text='Каталог товаров']")
    private WebElement catalogOfProductsPage;

    @FindBy(xpath = ".//*[@resource-id='ua.com.rozetka.shop:id/bottom_bar_tv_buy']")
    private WebElement buyNowButton;

    @FindBy(xpath = ".//*[@resource-id='ua.com.rozetka.shop:id/contact_data_et_first_name']")
    private WebElement contactData;

    @FindBy(xpath = ".//*[@resource-id='ua.com.rozetka.shop:id/iv_cart_offer_item_menu']")
    private WebElement itemMenu;

    @FindBy(
            xpath =
                    "//android.widget.TextView[@resource-id='ua.com.rozetka.shop:id/title'][@text='Удалить из корзины']")
    private WebElement deleteFromCartButton;

    @FindBy(xpath = ".//*[@text='Корзина пуста']")
    private WebElement emptyCartMessage;

    private String CATEGORY_NAME_XPATH = ".//*[@text='%s']";

    private String PRODUCT_INDEX_XPATH =
            "//android.widget.LinearLayout[@resource-id='ua.com.rozetka.shop:id/ll_root'][@index='%s']";

    /**
     * Set all capabilities for Android testing
     *
     * @throws MalformedURLException
     */
    @Before
    public void setUp() throws MalformedURLException {
        // Initialize application
        File app = new File("src/main/resources/Rozetka_v3.11.0.apk");

        // Capabilities
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        // Set device type
        desiredCapabilities.setCapability("device", "Android");

        // Set browser type. Leave it empty if it's app
        desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, "");

        // Set path to app file
        desiredCapabilities.setCapability("app", app.getAbsolutePath());

        // Set name of the device
        desiredCapabilities.setCapability("deviceName", "Nexus 6");

        // Set platform name
        desiredCapabilities.setCapability("platformName", "Android");

        // Set package of the app
        desiredCapabilities.setCapability("appPackage", "ua.com.rozetka.shop");

        // Set activity of the app
        desiredCapabilities.setCapability("appActivity", "ua.com.rozetka.shop.ui.InitActivity");

        // Initialize driver
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);

        // Initialize WebDriverWait
        wait = new WebDriverWait(driver, 10);

        // Initialize WebElements
        PageFactory.initElements(driver, this);
    }

    /**
     * Open Rozetka application, log in, add product to cart, then delete it from cart and verify
     * message
     */
    @Test
    public void testApplicationTest() {

        // Wait and click on sign in link
        waitTillElementIsVisible(signInLink);
        signInLink.click();

        // Wait for login form and login
        waitTillElementIsVisible(emailInput);
        emailInput.sendKeys(EMAIL);
        driver.navigate().back();
        passwordInput.sendKeys(PASSWORD);
        driver.navigate().back();
        signInButton.click();

        // Wait and click on catalog
        waitTillElementIsVisible(catalogButton);
        catalogButton.click();

        // Open needed categories by name
        waitTillElementIsVisible(catalogOfProductsPage);
        openCategoryByName("Ноутбуки и компьютеры");
        openCategoryByName("Ноутбуки");
        openCategoryByName("Для несложных задач");

        // Open first product
        openProductByIndex("0");

        // Wait and click on buyNowButton
        waitTillElementIsVisible(buyNowButton);
        buyNowButton.click();

        // Wait till contact data is visible and click on back button
        waitTillElementIsVisible(contactData);
        driver.navigate().back();

        // Wait till item menu is visible, click on it and choose "Delete from cart"
        waitTillElementIsVisible(itemMenu);
        itemMenu.click();
        waitTillElementIsVisible(deleteFromCartButton);
        deleteFromCartButton.click();

        // Wait for message to verify that everything is ok
        waitTillElementIsVisible(emptyCartMessage);
        Assert.assertEquals("Message is incorrect", EMPTY_CART_MESSAGE, emptyCartMessage.getText());
    }

    /** Open the category by name */
    public void openCategoryByName(String name) {
        waitTillElementIsVisible(driver.findElement(By.xpath(String.format(CATEGORY_NAME_XPATH, name))));
        driver.findElement(By.xpath(String.format(CATEGORY_NAME_XPATH, name))).click();
    }

    /** Open the product by index */
    public void openProductByIndex(String index) {
        waitTillElementIsVisible(driver.findElement(By.xpath(String.format(PRODUCT_INDEX_XPATH, index))));
        driver.findElement(By.xpath(String.format(PRODUCT_INDEX_XPATH, index))).click();
    }

    /**
     * Wait for element visibility
     *
     * @param element
     */
    private void waitTillElementIsVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /** Making driver quit */
    @After
    public void tearDown() {
        driver.quit();
    }
}
