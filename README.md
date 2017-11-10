# SeleniumDriverAutomation

Selenium IDE:
Selenium IDE is an integrated development environment for Selenium tests. It is implemented as a Firefox extension,
and allows you to record, edit, and debug tests. Selenium IDE includes the entire Selenium Core, allowing you to
easily and quickly record and play back tests in the actual environment that they will run.

Selenium RC:

```java
// Selenium 2
DesiredCapabilities capabilities = new DesiredCapabilities();
System.setProperty(FirefoxDriver.SystemProperty.DRIVER_XPI_PROPERTY, "D:\\Software\\CBCP\\webdriver.xpi");

// Selenium 3
System.out.println("FireFox Driver Path « "+ geckodriverCloudRootPath);
File temp = File.createTempFile("geckodriver",	null);
chromtmp.setExecutable(true);
FileUtils.copyURLToFile(new URL( geckodriverCloudRootPath ), temp);

System.setProperty("webdriver.gecko.driver", temp.getAbsolutePath() );
capabilities.setCapability("marionette", true);

File file = new File(browserBinaryPath);
System.out.println("Binary Path : "+browserBinaryPath);
FirefoxBinary binary = new FirefoxBinary(file);

FirefoxProfile profile = new FirefoxProfile();
profile.setPreference("browser.startup.homepage", "about:blank");
profile.setPreference("browser.startup.homepage_override.mstone", "ignore");

profile.setPreference("xpinstall.signatures.required", false);
profile.setPreference("toolkit.telemetry.reportingpolicy.firstRun", false);
profile.setPreference("intl.accept_languages", "no,en-us,en");
profile.setPreference( "app.update.enabled", false);
profile.setPreference( "browser.tabs.autoHide", true);
profile.setAcceptUntrustedCertificates( true );
profile.setAssumeUntrustedCertificateIssuer( true );
profile.setEnableNativeEvents( true );
profile.setPreference("browser.link.open_newwindow.disabled_in_fullscreen", true);

WebDriver driver = new FirefoxDriver(binary, profile, capabilities);
```