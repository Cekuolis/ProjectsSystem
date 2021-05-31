package lt.vtmc.emiliatoriusTests;

import com.codeborne.selenide.*;

import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    private final SelenideElement loginFormEmail = $("input[type='email']");
    private final SelenideElement loginFormPassword = $("input[type='password']");
    private final SelenideElement loginFormButton = $("button[type='submit']");
    private final SelenideElement loginErrorText = $(".swal-text");

    //navigating
    public void openLoginPage() {
        open("http://localhost:3000");
    }

    //clearing and filling
    public void clearEmail() {
        loginFormEmail.clear();
    }

    public void clearPassword() {
        loginFormPassword.clear();
    }

    public void fillEmailWithValidData() {
        clearEmail();
        loginFormEmail.sendKeys("admin@mail.com");
    }

    public void fillEmailWithInvalidData() {
        clearEmail();
        loginFormEmail.sendKeys("nera@mail.com");
    }

    public void fillPasswordWithValidData() {
        clearPassword();
        loginFormPassword.sendKeys("password");
    }

    public void fillPasswordWithInvalidData() {
        clearPassword();
        loginFormPassword.sendKeys("xxxxx");
    }

    //clicking
    public void clickSignInButton() {
        loginFormButton.click();
    }

    //getters
    public SelenideElement getLoginErrorText() {
        return loginErrorText;
    }
}
