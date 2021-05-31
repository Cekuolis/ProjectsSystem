package lt.vtmc.emiliatoriusTests;

import com.codeborne.selenide.*;

import static com.codeborne.selenide.Selenide.*;

public class NavPage {

    private final SelenideElement pageNavName = $(".navbar-brand");
    private final SelenideElement navButton = $(".dropdown");
    private final SelenideElement userName = $$(".dropdown-item").get(0);
    private final SelenideElement logoutButton = $$(".dropdown-item").get(1);

    //navigating
    public void signOut() {
        clickNavButton();
        logoutButton.click();
    }

    //clicking
    public void clickNavButton() {
        navButton.click();

    }

    public void clickLogoutButton() {
        logoutButton.click();
    }

    //getters
    public SelenideElement getPageNavName() {
        return pageNavName;
    }

    public SelenideElement getNavButton() {
        return navButton;
    }

    public SelenideElement getUserName() {
        return userName;
    }

    public SelenideElement getLogoutButton() {
        return logoutButton;
    }
}
