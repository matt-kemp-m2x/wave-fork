package com.tidal.wave.waiter;

import com.tidal.wave.browser.Browser;
import com.tidal.wave.counter.TimeCounter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.tidal.wave.browser.Browser.close;
import static com.tidal.wave.data.GlobalData.getData;
import static com.tidal.wave.data.WaitTime.DEFAULT_WAIT_TIME;
import static com.tidal.wave.data.WaitTime.EXPLICIT_WAIT_TIME;
import static com.tidal.wave.verification.conditions.Condition.exactText;
import static com.tidal.wave.webelement.ElementFinder.find;
import static com.tidal.wave.webelement.ElementFinder.findAll;

public class WaitTest {

    @Before
    public void testSetUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);

        Browser.withOptions(chromeOptions).open("http://www.google.com");
    }

    @After
    public void testClose() {
        close();
    }

    @Test
    public void testDefaultWaitTimeout() {
        find("name:q").sendKeys("Selenium");
        TimeCounter timeCounter = new TimeCounter();
        try {
            find("name:qXXX").clear();
        } catch (RuntimeException e) {
            long timeElapsed = timeCounter.timeElapsed().getSeconds();

            if (timeElapsed < 5) {
                throw new RuntimeException(String.format("Default wait time is not working properly, " +
                        "expected is %s seconds but got %d", getData(DEFAULT_WAIT_TIME), timeElapsed));
            }
        }
    }

    @Test
    public void testExplicitWaitTimeOut() {
        find("name:q").sendKeys("Selenium");
        TimeCounter timeCounter = new TimeCounter();

        try {
            find("name:qXXX").waitFor(7).clear();
        } catch (RuntimeException e) {
            long timeElapsed = timeCounter.timeElapsed().getSeconds();

            if (timeElapsed < 7) {
                throw new RuntimeException(String.format("Explicit wait time is not working properly, " +
                        "expected is %s seconds but got %d", getData(EXPLICIT_WAIT_TIME), timeElapsed));
            }
        }
    }


    @Test
    public void testExplicitWaitTimeOutDoNotOverWriteDefaultWaitTime() {
        find("name:q").waitFor(10).sendKeys("Selenium");
        TimeCounter timeCounter = new TimeCounter();

        try {
            find("name:qXXX").clear();
        } catch (RuntimeException e) {
            long timeElapsed = timeCounter.timeElapsed().getSeconds();

            if (timeElapsed < 5) {
                throw new RuntimeException(String.format("Explicit wait time overwrites default wait time, " +
                        "expected is %s seconds but got %d", getData(DEFAULT_WAIT_TIME), timeElapsed));
            }
        }
    }

    @Test
    public void shouldHaveTextExplicitWait() {
        find("name:q").sendKeys("Selenium");
        findAll("name:q").get(0).sendKeys("Selenium");

        TimeCounter timeCounter = new TimeCounter();

        try {
            find("name:q").waitFor(10).shouldHave(exactText("Cucumber"));
        } catch (RuntimeException e) {
            long timeElapsed = timeCounter.timeElapsed().getSeconds();

            if (!(timeElapsed <= 10)) {
                throw new RuntimeException(String.format("Explicit wait time overwrites default wait time, " +
                        "expected is %s seconds but got %d", getData(DEFAULT_WAIT_TIME), timeElapsed));
            }
        }
    }


}
