package de.codecentric.janus.plugin.step;

import com.google.inject.Inject;
import de.codecentric.janus.plugin.suite.AbstractStep;
import de.codecentric.janus.plugin.library.SeleniumAdapter;
import org.jbehave.core.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class VCSConfiguration extends AbstractStep {

    private Configuration configuration;

    @Inject
    public VCSConfiguration(SeleniumAdapter selenium,
                            Configuration configuration) {
        super(selenium);
        this.configuration = configuration;
    }

    /*
     * ############################
     * ### GIVEN
     * ############################
     */

    /*
     * ############################
     * ### WHEN
     * ############################
     */
    @When("a VCS configuration <name> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>")
    @Given("a VCS configuration <name> is added with <type> and builds <creationBuild>, <checkoutBuild> and <commitBuild>")
    public void whenAVCSConfigurationIsAdded(@Named("name") String name,
                                             @Named("type") String type,
                                             @Named("creationBuild") String creationBuild,
                                             @Named("checkoutBuild") String checkoutBuild,
                                             @Named("commitBuild") String commitBuild)
            throws Exception {
        goToConfigurationPage();

        // add a new entry to the list
        getAddVCSConfigButton().click();

        // wait until a new configuration entry is added the repeatable list
        waitUntilPageContains(By
                .cssSelector(CSS_SELECTOR.REPEATED_CHUNK_SELECTOR));

        // set values
        getNameInputField().sendKeys(name);
        getVCSSelectBox().selectByValue(type);
        getGenerationBuildSelectBox().selectByValue(creationBuild);
        getCheckoutBuildSelectBox().selectByValue(checkoutBuild);
        getCommitBuildSelectBox().selectByValue(commitBuild);

        // submit the form
        configuration.getSubmitButton().click();
    }
    
    private void select(By by, String selectValue) {
        Select select = new Select(driver.findElement(by));
        select.selectByValue(selectValue);
    }

    /*
     * ############################
     * ### THEN
     * ############################
     */
    @Then("a <type> installation <name> can be found with builds <creationBuild>, <checkoutBuild> and <commitBuild>")
    public void thenAnCanBeFound(@Named("type") String type,
                                 @Named("name") String name,
                                 @Named("creationBuild") String creationBuild,
                                 @Named("checkoutBuild") String checkoutBuild,
                                 @Named("commitBuild") String commitBuild) throws Exception {
        goToConfigurationPage();

        assertThat(getNameInputField().getAttribute("value"),
                is(equalTo(name)));

        assertSelected(getVCSSelectBox(), type);
        assertSelected(getGenerationBuildSelectBox(), creationBuild);
        assertSelected(getCheckoutBuildSelectBox(), checkoutBuild);
        assertSelected(getCommitBuildSelectBox(), commitBuild);
    }

    private void assertSelected(Select select, String value) {
        assertThat(select.getFirstSelectedOption().getAttribute("value"),
                is(equalTo(value)));
    }

    /*
    * ############################
    * ### WEB ELEMENTS
    * ############################
    */
    private WebElement getAddVCSConfigButton() {
        return findByCSS(CSS_SELECTOR.ADD_VCS_CONFIG_BUTTON);
    }
    
    private WebElement getNameInputField() {
        return findByCSS(CSS_SELECTOR.NAME_INPUT_FIELD);
    }
    
    private Select getVCSSelectBox() {
        return findSelectByCSS(CSS_SELECTOR.VCS_SELECT_BOX);
    }

    private Select getGenerationBuildSelectBox() {
        return findSelectByCSS(CSS_SELECTOR.GENERATION_BUILD_SELECT_BOX);
    }

    private Select getCheckoutBuildSelectBox() {
        return findSelectByCSS(CSS_SELECTOR.CHECKOUT_BUILD_SELECT_BOX);
    }

    private Select getCommitBuildSelectBox() {
        return findSelectByCSS(CSS_SELECTOR.COMMIT_BUILD_SELECT_BOX);
    }

    public static interface CSS_SELECTOR {
        String ADD_VCS_CONFIG_BUTTON = ".janusConfig + div .repeatable-add " +
                "button";
        
        String REPEATED_CHUNK_SELECTOR = ".janusConfig + div .repeated-chunk";

        String NAME_INPUT_FIELD = REPEATED_CHUNK_SELECTOR + 
                " input[name=\"_.name\"]";

        String VCS_SELECT_BOX = REPEATED_CHUNK_SELECTOR + 
                " select[name=\"_.vcs\"]";
        
        String GENERATION_BUILD_SELECT_BOX = REPEATED_CHUNK_SELECTOR +
                " select[name=\"_.generationBuildJob\"]";

        String CHECKOUT_BUILD_SELECT_BOX = REPEATED_CHUNK_SELECTOR +
                " select[name=\"_.checkoutBuildJob\"]";

        String COMMIT_BUILD_SELECT_BOX = REPEATED_CHUNK_SELECTOR +
                " select[name=\"_.commitBuildJob\"]";
    }
}