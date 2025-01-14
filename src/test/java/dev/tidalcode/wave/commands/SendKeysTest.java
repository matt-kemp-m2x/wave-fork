package dev.tidalcode.wave.commands;

import com.tidal.utils.filehandlers.Finder;
import dev.tidalcode.wave.browser.Browser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeOptions;

import static dev.tidalcode.wave.webelement.ElementFinder.find;

public class SendKeysTest {

    @Before
    public void initialize() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");

        Browser.withOptions(options).open("file://" + Finder.findFilePath("components/textinput/textinput.html"));
    }

    @After
    public void terminate() {
        Browser.close();
    }

    @Test
    public void inputUsingSetText() {
        find("id:myText1").clear().setText("Tidal Wave");
        find("id:text_submit_button").click();
        String textAfter = find("id:result").getText();
        Assert.assertEquals("", "Tidal Wave QA Team", textAfter);
    }

    @Test(expected = RuntimeException.class)
    public void inputUsingSetTextShouldFail() {
        String textInputLocator = "id:myText1";
        find(textInputLocator).waitFor(6).click().clear().setText("Tidal-Wave" + Keys.TAB);
    }
}
