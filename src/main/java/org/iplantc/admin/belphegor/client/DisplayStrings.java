package org.iplantc.admin.belphegor.client;

import org.iplantc.core.uicommons.client.CommonUIDisplayStrings;

public interface DisplayStrings extends CommonUIDisplayStrings {
    String header();

    String subHeader();

    String next();

    String license();

    String toolNamePanelCaption();

    String toolNameLabel();

    String toolNameEmptyText();

    String toolDescLabel();

    String toolDescEmptyText();

    String toolAttributionLabel();

    String toolAttributionEmptyText();

    String toolDistributionPanelCaption();

    String toolLinkCaption();

    String toolLinkDefaultText();

    String toolVersionCaption();

    String toolVersionDefaultText();

    String licenseGNU();

    String licenseBerkley();

    String licenseMIT();

    String licenseApache();

    String licenseOther();

    String otherLicenseLabel();

    String otherLicenseDefaultText();

    String toolTypeCaption();

    String toolTypeCommandLine();

    String toolTypeWebService();

    /**
     * Heading for the Edit Validator dialog.
     * 
     * @return a string representing the text
     */
    String validationEditDialogHeading();

    /**
     * Caption for the Rule Type drop-down on the Edit Rule dialog.
     * 
     * @return a string representing the text
     */
    String ruleType();

    String addStaticText();

    /**
     * Label for a newly created property.
     * 
     * @param n a number to insert into the label
     * @return a string representing the text
     */
    String newPropertyLabel(int n);

    /**
     * Label for newly created static text.
     * 
     * @param n a number to insert into the label
     * @return a string representing the text
     */
    String newStaticTextLabel(int n);

    /**
     * Label for a newly created property group.
     * 
     * @param n a number to insert into the label
     * @return a string representing the text
     */
    String newPropertyGroupLabel(int n);

    /**
     * Caption for deployed component panel.
     * 
     * @return a string representing the text
     */
    String deployedComponent();

    /**
     * Caption for deployed component location label.
     * 
     * @return a string representing the text
     */
    String toolLocation();

    /**
     * Default text for deployed component location field.
     * 
     * @return a string representing the text
     */
    String toolLocationDefaultText();

    /**
     * Text for the template.
     * 
     * @return a string representing the text
     */
    String template();

    /**
     * Text for the component.
     * 
     * @return a string representing the text
     */
    String component();

    /**
     * Text for the template name.
     * 
     * @return a string representing the text
     */
    String templateName();

    /**
     * Caption for input/ output data objects grid.
     * 
     * @return a string representing the text
     */
    String inputOutput();

    /**
     * Text for input file type
     * 
     * @return string representing the text
     */
    String inputFile();

    /**
     * Text for output file type
     * 
     * @return string representing the text
     */
    String outputFile();

    /**
     * Text for command line parameter type
     * 
     * @return string representing the text
     */
    String parameter();

    /**
     * Label for the parameters tab
     * 
     * @return string representing the text
     */
    String arguments();

    /**
     * Text for unordered command line parameter type
     * 
     * @return string representing the text
     */
    String unorderedParameter();

    /**
     * Text for a button to add a tool
     * 
     * @return string representing the text
     */
    String newTemplate();

    /**
     * Caption for the center panel on the landing page
     * 
     * @return string representing the text
     */
    String templateList();

    /**
     * Text when multiplicity = 1
     * 
     * @return string representing the text
     */
    String multiplicityOne();

    /**
     * Text when multiplicity > 1
     * 
     * @return string representing the text
     */
    String multiplicityMany();

    /**
     * Text when an invalid name is entered
     * 
     * @return string representing the text
     */
    String nameValidationMsg();

    String editTemplate();

    String noComponents();

    String addPanel();

    String addWidget();

    String previewUI();

    String multiplicity();

    String commandLineOrder();

    String infoType();

    String requestNewTool();

    String jsonPreview();

    String newToolRequestError();

    String groupName();

    String flag();

    /**
     * Label for the Descriptive Text area.
     * 
     * @return string representing the text
     */
    String staticTextLabel();

    /**
     * Text to show in a Descriptive Text area when no text has been entered.
     * 
     * @return string representing the text
     */
    String staticTextEmpty();

    String defaultValue();

    String parameterType();

    String displayInGUI();

    String typeOfFieldNeeded();

    String toolTipText();

    String propertyEditorTrue();

    String propertyEditorFalse();

    String userInputRequired();

    /**
     * Label for the "required" check box in the inputs/outputs dialog
     * 
     * @return string representing the text
     */
    String requiredCheckBoxLabel();

    String validationRules();

    String noRulesToDisplay();

    String publish();

    String publishSuccess();
    
    String publishedAppLinkText();

    /**
     * Label for a button to publish to a user's private workspace
     * 
     * @return
     */
    String publishPrivate();

    String selectedTool();

    String componentFieldEmptyText();

    String nameFieldEmptyText();

    String versionColumnHeader();

    String fileCaption();

    String outputFileName();

    String inputMultiplicityOption();

    String outPutMultiplicityOption();

    String infoTypePrompt();

    String defaultValueLabel();

    String link();

    String srcBin();

    String srcLinkPrompt();

    String toolDesc();

    String version();

    String docLink();

    String upldTestData();

    String cmdLineRun();

    String addnlData();

    String comments();

    String contactTab();

    String toolTab();

    String otherTab();

    String submitRequest();

    String submitting();

    String inValidUrl();

    /**
     * Localized text for a publishing error message with an optional second line of additional details.
     * 
     * @param errMsg optional second line of additional details.
     * @return string representing the text
     */
    String publishFailure(String errMsg);

    String previewDeployedDialogCaption();

    String previewDeployedDialogText();

    String previewSubmit();

    String values();

    String toolName();

    String requestConfirmMsg();

    String msgTemplateDelete();

    String deleteFailed();

    String navigateWarning();

    String confirm();

    String saved();

    String templateSaved();

    String saveFailed();

    String addOutputFile();

    String addInputFile();

    String fileType();

    String display();

    String done();

    String order();

    String defaultVal();

    String cmdLineOrderingCaption();

    String previewFailure();

    String noParams();

    String publishOrderingWarning();

    String listBoxFormatNotice();

    String folder();

    String multiplicityFolder();

    /**
     * Localized text telling the user to select a parameter or parameter group.
     * 
     * @return string representing the text
     */
    String selectParameter();

    /**
     * Localized text indicating a non-integer has been entered into an integer field.
     * 
     * @return string representing the text
     */
    String mustBeInt();

    /**
     * Localized text indicating a non-number has been entered into an number field.
     * 
     * @return string representing the text
     */
    String mustBeNumber();

    /**
     * Localized text indicating a non-number has been entered into an number field.
     * 
     * @return string representing the text
     */
    String toolRequestSucess();

    /**
     * Localized text indicating the user's home dir was not found.
     * 
     * @return string representing the text
     */
    String noHomeDir();

    /**
     * Localized text for the home button.
     * 
     * @return string representing the text
     */
    String home();

    /**
     * Localized text representing the "int below" rule.
     * 
     * @param value the maximum allowed value
     * @return string representing the text
     */
    String intBelow(String value);

    /**
     * Localized text representing the "int above" rule.
     * 
     * @param value the minimum allowed value
     * @return string representing the text
     */
    String intAbove(String value);

    /**
     * Localized text representing the "int above" rule.
     * 
     * @param value1 the minimum allowed value
     * @param value2 the maximum allowed value
     * @return string representing the text
     */
    String intRange(String value1, String value2);

    /**
     * Localized text to show when there is no filter.
     * 
     * @return string representing the text
     */
    String filterEmptyText();

    /**
     * Localized text for anything that has no title.
     * 
     * @return string representing the text
     */
    String untitled();

    /**
     * Localized error message to show when existing integrations couldn't be loaded.
     * 
     * @return string representing the text
     */
    String cantRetrieveIntegrations();

    /**
     * Localized error message to show when templates couldn't be loaded.
     * 
     * @return string representing the text
     */
    String cantRetrieveTemplates();

    /**
     * Localized error message to show when a template requested by the user couldn't be loaded.
     * 
     * @return string representing the text
     */
    String cantLoadTemplate();

    /**
     * Localized error message to show when data for the "Type of information" drop-down couldn't be
     * loaded.
     * 
     * @return string representing the text
     */
    String cantLoadInfoTypes();

    /**
     * Localized error message to show when a service couldn't be queried for a new UUID.
     * 
     * @return string representing the text
     */
    String cantGenerateUuid();

    /**
     * Localized error message to show when rule types couldn't be loaded.
     * 
     * @return string representing the text
     */
    String cantLoadRuleTypes();

    /**
     * Localized error message to show when widget types couldn't be loaded.
     * 
     * @return string representing the text
     */
    String cantLoadWidgetTypes();

    /**
     * Localized error message to show when deployed components couldn't be loaded.
     * 
     * @return string representing the text
     */
    String cantLoadDeployedComponents();

    /**
     * Localized error message to show when information about the current user couldn't be loaded.
     * 
     * @return string representing the text
     */
    String cantLoadUserInfo();

    /**
     * Text to show when no executable has been defined
     * 
     * @return string representing the text
     */
    String executableNameDefault();

    /**
     * Text asking the user to enter command line arguments
     * 
     * @return string representing the text
     */
    String enterCmdLineArgs();

    /**
     * Caption for the command line preview box
     * 
     * @return string representing the text
     */
    String cmdLinePreview();

    /**
     * Localized text for boolean property editor grid
     * 
     * @return string representing the text
     */
    String booleanValue();

    /**
     * Localized text for multi threaded
     * 
     * @return string representing the text
     */
    String isMultiThreaded();

    /**
     * Localized text for the Template Summary grid.
     * 
     * @return string representing the text
     */
    String lastEdited();

    /**
     * 
     * Display text for back button
     * 
     * @return a String representing the text.
     */
    String back();

    /**
     * Localized text for about tito
     * 
     * @return string representing the text
     */
    String aboutTito();

    /**
     * Localized text for optional pass flag check box
     * 
     * @return string representing the text
     */
    String passFlag();

    /**
     * Localized text for empty labels in the parameter tree
     * 
     * @return string representing the text
     */
    String noTreeLabel();

    /**
     * Localized text for retaining file checkbox prompt
     * 
     * @return string representing the text
     */
    String retainFilePrompt();
    
    /**
     * Localized text for publish to workspace date
     * 
     * @return string representing the text
     */
    String publishToWorkspaceOn();

    /**
     * Localized text for a default publishing error message when no error message could be parsed from
     * the service call response.
     * 
     * @return string representing the text
     */
    String publishFailureDefaultMessage();

    /**
     * Localized text for a publishing error message when the App JSON is empty (e.g. when the user info
     * could not be retrieved).
     * 
     * @return string representing the text
     */
    String publishErrorEmptyJson();

    /**
     * Error message for display in the error dialog details when a publish service call fails.
     * 
     * @param errorName
     * @param errorMessage
     * @return localized error string.
     */
    String publishErrorReport(String errorName, String errorMessage);

    /**
     * Localized text for names of newly copied Apps.
     * 
     * @param origName The name of the App being copied.
     * @return string representing the text
     */
    String copyOfAppName(String origName);
}
