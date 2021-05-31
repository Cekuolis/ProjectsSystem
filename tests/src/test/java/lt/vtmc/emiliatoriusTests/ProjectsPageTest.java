package lt.vtmc.emiliatoriusTests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.junit.TextReport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProjectsPageTest {

    LoginPage login = new LoginPage();
    NavPage navigation = new NavPage();
    ProjectsPage projects = new ProjectsPage();

    @Rule
    public TextReport textReport = new TextReport();

    @Before
    public void setupLoginPage() {
        Configuration.headless = true;
        login.openLoginPage();
        login.fillEmailWithValidData();
        login.fillPasswordWithValidData();
        login.clickSignInButton();
    }

    @Test
    public void checkIfProjectCardsVisible() {
        int i = 0;
        //wait for cards to appear
        projects.getProjectCards().get(i).shouldBe(Condition.exist);
        while (projects.getProjectCards().get(i).exists()) {
            projects.getProjectCards().get(i).shouldBe(Condition.visible);
            i++;
        }
    }

    @Test
    public void checkIfProjectCardsNotVisibleAfterLogout() {
        navigation.signOut();
        projects.openProjectsPage();
        projects.getProjectCards().get(0).shouldNot(Condition.exist);
    }

}
