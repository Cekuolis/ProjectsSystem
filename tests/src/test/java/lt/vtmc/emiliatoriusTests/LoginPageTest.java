package lt.vtmc.emiliatoriusTests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.junit.TextReport;
import org.junit.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;

public class LoginPageTest {

    LoginPage login = new LoginPage();
    NavPage navigation = new NavPage();
    ProjectsPage projects = new ProjectsPage();

    @Rule
    public TextReport textReport = new TextReport();

    @Before
    public void setupLoginPage() {
        Configuration.headless = true;
        login.openLoginPage();
    }

    @Test
    public void userCanLoginWithValidData() {
        login.fillEmailWithValidData();
        login.fillPasswordWithValidData();
        login.clickSignInButton();
        navigation.getPageNavName().should(Condition.matchText("Emiliatorius'3"));
    }

    //does not work, element is not displayed, but if i click manually it is displayed
    @Test
    public void validateUsernameAfterLogin() {
        userCanLoginWithValidData();
        navigation.clickNavButton();
        navigation.getUserName().shouldHave(Condition.text("admin@mail.com"));
    }

    @Test
    public void userCanNotLoginWithNoData() {
        login.clearEmail();
        login.clearPassword();
        login.clickSignInButton();
        login.getLoginErrorText().shouldHave(Condition.text("Bad email or password!"));
    }

    @Test
    public void userCanNotLoginWithNoName() {
        login.clearEmail();
        login.fillPasswordWithValidData();
        login.clickSignInButton();
        login.getLoginErrorText().shouldHave(Condition.text("Bad email or password!"));
    }

    @Test
    public void userCanNotLoginWithNoPassword() {
        login.fillEmailWithValidData();
        login.clearPassword();
        login.clickSignInButton();
        login.getLoginErrorText().shouldHave(Condition.text("Bad email or password!"));
    }

    @Test
    public void userCanNotLoginWithBadPassword() {
        login.fillEmailWithValidData();
        login.fillPasswordWithInvalidData();
        login.clickSignInButton();
        login.getLoginErrorText().shouldHave(Condition.text("Bad email or password!"));
    }

    @Test
    public void userCanNotLoginWithBadName() {
        login.fillEmailWithInvalidData();
        login.fillPasswordWithValidData();
        login.clickSignInButton();
        login.getLoginErrorText().shouldHave(Condition.text("Bad email or password!"));
    }

    @Test
    public void userCanLogout() {
        userCanLoginWithValidData();
        navigation.clickNavButton();
        navigation.clickLogoutButton();
        Assert.assertEquals("http://localhost:3000/", url());
        navigation.getNavButton().shouldNot(Condition.exist);
    }

    @Test
    public void userCanNotGoBackAfterLogout() {
        userCanLogout();
        back();
        navigation.getNavButton().shouldNot(Condition.exist);
        navigation.getPageNavName().shouldNot(Condition.exist);
    }
}
