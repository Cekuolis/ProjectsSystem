package lt.vtmc.emiliatoriusTests;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.Date;

import static com.codeborne.selenide.Selenide.*;

public class ProjectsPage {

    private final ElementsCollection projectCards = $$(".card");
    private final SelenideElement createProjectButton = $x("//p[normalize-space()='Create new project']");
    private final SelenideElement createProjectName = $x("//div[@class='modal-body']//input[@placeholder='Project name']");
    private final SelenideElement createProjectDescription = $("input[placeholder='Project is...']");
    private final SelenideElement createProjectLastStepButton = $x("//button[normalize-space()='Create']");

    //methods
    Date date = new Date(System.currentTimeMillis());

    //navigating
    public void openProjectsPage() {
        open("http://localhost:3000/projects");
    }

    //clicking
    public void clickCreateProject() {
        createProjectButton.click();
    }

    public void clickCreateProjectLastStepButton() {
        createProjectLastStepButton.click();
    }

    //filling and clearing
    public void fillCreateProjectName() {
        createProjectName.clear();
        createProjectName.sendKeys("Name " + date);
    }

    public void fillCreateProjectDescription() {
        createProjectDescription.clear();
        createProjectDescription.sendKeys("Description " + date);
    }

    //getters
    public ElementsCollection getProjectCards() {
        return projectCards;
    }

}
