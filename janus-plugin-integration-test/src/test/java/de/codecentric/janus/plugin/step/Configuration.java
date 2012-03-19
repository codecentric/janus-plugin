package de.codecentric.janus.plugin.step;

import de.codecentric.janus.plugin.library.SeleniumAdapter;
import de.codecentric.janus.plugin.suite.Config;
import org.jbehave.core.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.google.inject.Inject;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Configuration {
    private WebDriver driver;
    private SeleniumAdapter seleniumAdapter;

    @Inject
    public Configuration(SeleniumAdapter selenium) {
        driver = selenium.getDriver();
        this.seleniumAdapter = selenium;
    }

    /*
     * ############################
     * ### GIVEN
     * ############################
     */
    @Given("a clean Jenkins installation")
    public void givenACleanJenkinsInstallation() throws Exception {
        seleniumAdapter.cleanJenkinsConfiguration();
    }

    @Given("an installation with three builds named <creationBuild>, <checkoutBuild> and <commitBuild>")
    public void givenAnInstallationWithThreeBuilds(@Named("creationBuild") String creationBuild,
                                                   @Named("checkoutBuild") String checkoutBuild,
                                                   @Named("commitBuild") String commitBuild)
                                                   throws Exception {
        seleniumAdapter.goToConfigurationPage();
    }

    /*
     * ############################
     * ### WHEN
     * ############################
     */
    @When("a VCS configuration <name> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>")
    public void whenAVCSConfigurationIsAdded(@Named("name") String name,
                                                 @Named("type") String type,
                                                 @Named("creationBuild") String creationBuild,
                                                 @Named("checkoutBuild") String checkoutBuild,
                                                 @Named("commitBuild") String commitBuild) {
        // PENDING
    }

    /*
     * ############################
     * ### THEN
     * ############################
     */
    @Then("a <type> installation <name> can be found")
    public void thenAnCanBeFound(@Named("type") String type,
                                 @Named("name") String name) {
    }
}