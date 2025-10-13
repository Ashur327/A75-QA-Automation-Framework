package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {

    WebDriver driver;
    WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @FindBy(css = "i[title='Create a new playlist']")
    private WebElement createPlaylistIcon;

    @FindBy(css = "li[data-testid='playlist-context-menu-create-simple']")
    private WebElement createSimplePlaylist;

    @FindBy(css = "input[placeholder='↵ to save']")
    private WebElement playlistNameInput;

    @FindBy(css = "div.success.show")
    private WebElement successBanner;

    @FindBy(css = "button[title='Delete this playlist']")
    private WebElement deleteButton;

    // Fetch playlist links dynamically
    public List<WebElement> getPlaylistLinks(String playlistName) {
        return driver.findElements(
                org.openqa.selenium.By.xpath("//li[contains(@class,'playlist')]//a[normalize-space()='" + playlistName + "']")
        );
    }

    // Create playlist if missing
    public void createPlaylistIfNotExists(String playlistName) {
        List<WebElement> playlists = getPlaylistLinks(playlistName);
        if (playlists.isEmpty()) {
            createPlaylistIcon.click();
            createSimplePlaylist.click();
            playlistNameInput.sendKeys(playlistName + "\n");
            wait.until(ExpectedConditions.invisibilityOf(successBanner));
        }
    }

    // Rename playlist
    public void renamePlaylist(String oldName, String newName) {
        List<WebElement> playlists = getPlaylistLinks(oldName);
        if (playlists.isEmpty()) throw new RuntimeException("Playlist '" + oldName + "' not found.");

        WebElement playlist = playlists.get(0);
        Actions actions = new Actions(driver);
        actions.contextClick(playlist).perform();

        WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(
                org.openqa.selenium.By.xpath("//li[contains(@data-testid,'playlist-context-menu-edit')]")
        ));
        editOption.click();

        WebElement nameInput = wait.until(driver2 -> {
            WebElement el = driver2.findElement(
                    org.openqa.selenium.By.xpath("//input[@data-testid='inline-playlist-name-input']")
            );
            return (el.isDisplayed() && el.isEnabled()) ? el : null;
        });

        nameInput.click();
        nameInput.clear();
        Actions typeAction = new Actions(driver);
        typeAction.moveToElement(nameInput).click().sendKeys(newName + "\n").perform();

        wait.until(ExpectedConditions.textToBePresentInElement(
                successBanner,
                "Updated playlist \"" + newName + "\""
        ));
    }

    // Delete playlist
    public void deletePlaylist(String playlistName) {
        List<WebElement> playlists = getPlaylistLinks(playlistName);
        if (playlists.isEmpty()) {
            // create it first so deletion can occur
            createPlaylistIfNotExists(playlistName);
        } else {
            playlists.get(0).click();
        }

        deleteButton.click();

        wait.until(ExpectedConditions.textToBePresentInElement(
                successBanner,
                "Deleted playlist \"" + playlistName + ".\""
        ));
    }

    public String getSuccessBannerText() {
        return successBanner.getText();
    }
}
