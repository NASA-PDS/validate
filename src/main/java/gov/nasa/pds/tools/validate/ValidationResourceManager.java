package gov.nasa.pds.tools.validate;

import gov.nasa.pds.tools.label.LabelValidator;
import gov.nasa.pds.tools.util.FileFinder;
import gov.nasa.pds.tools.validate.rule.RuleContext;
import gov.nasa.pds.tools.validate.rule.ValidationRuleManager;

import java.io.File;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.config.ConfigParser;

/**
 * Implements an object that allows validation rules to get
 * resource objects of the proper class.
 */
public enum ValidationResourceManager {

    INSTANCE;

    private Catalog catalog;
    private ValidationRuleManager ruleManager;
    private LabelValidator labelValidator;
    private TargetRegistrar targetRegistrar;
    private ProblemListener problemListener;

    /**
     * Initializes the singleton instance.
     */
    private ValidationResourceManager() {
	ConfigParser parser = new ConfigParser();
	URL commandsURL = ClassLoader.getSystemResource("validation-commands.xml");

    // The below are used by the cucumber because the commandsURL will be null.
    if (commandsURL == null) {
        commandsURL = FileFinder.findMyFile("." + File.separator + "target" + File.separator + "classes","validation-commands",".xml");
    }

	try {
	    parser.parse(commandsURL);
	} catch (Exception e) {
	    System.err.println("Could not parse validation configuration from " + commandsURL);
	    System.err.println("ValidationResourceManager: Could not parse validation configuration from commandsURL " + commandsURL +  " resource " + "validation-commands.xml" );
	}

	catalog = CatalogFactory.getInstance().getCatalog();
	ruleManager = new ValidationRuleManager(catalog);

	try {
	    labelValidator = new LabelValidator();
	} catch (ParserConfigurationException ex) {
	    System.err.println("Could not initialize the label validator: " + ex);
	} catch (TransformerConfigurationException ex) {
	    System.err.println("Could not initialize the label validator: " + ex);
	}	    
    }

    public void setTargetRegistrar(TargetRegistrar registrar) {
	targetRegistrar = registrar;
    }

    public void setProblemListener(ProblemListener listener) {
	problemListener = listener;
    }

    /*
     * Gets a resource of a desired class.
     *
     * @throws InstantiationException if a resource instance cannot
     *   be instantiated
     */
    public <T> T getResource(Class<T> clazz) {
        if (clazz == Catalog.class) {
	    // Return a singleton.
	    return (T) catalog;
	} else if (clazz == Context.class) {
	    return (T) (new RuleContext());
	} else if (clazz == TargetRegistrar.class) {
	    return (T) targetRegistrar;
	} else if (clazz == ProblemListener.class) {
	    return (T) problemListener;
	} else if (clazz == ValidationRuleManager.class) {
	    // Return a singleton.
	    return (T) ruleManager;
	} else if (clazz == LabelValidator.class) {
	    // Return a singleton.
	    return (T) labelValidator;
	} else {
	    try {
		return clazz.newInstance();
	    } catch (InstantiationException ex) {
		System.err.println(
				   "Could not create an instance of class "
				   + clazz.getName() + ": " + ex
				   );
		return null;
	    } catch (IllegalAccessException ex) {
		System.err.println(
				   "Could not create an instance of class "
				   + clazz.getName() + ": " + ex
				   );
		return null;
	    }
	}

    }

}
