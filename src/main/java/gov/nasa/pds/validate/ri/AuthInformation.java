package gov.nasa.pds.validate.ri;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AuthInformation {
  final public static AuthInformation NO_AUTH = new AuthInformation(false, "", "" ,"");
  final private boolean trustSelfSigned;
  final private String password;
  final private String url;
  final private String username;

  private AuthInformation (boolean tss, String pwd, String un, String url) {
    this.password = pwd;
    this.trustSelfSigned = tss;
    this.url = url;
    this.username = un;
  }

  public static AuthInformation buildFrom (String filename)
      throws IOException,ParserConfigurationException,SAXException {
    boolean tss;
    File file = new File(filename);
    Scanner textReader;
    String line = null, pwd, un, url;

    if (filename == null || filename.length() == 0) return NO_AUTH;
    if (!file.exists())
      throw new IOException("Filename '" + filename + "' does not exist");

    // Get the first non-comment line
    textReader = new Scanner(file,Charset.defaultCharset().name());
    while (textReader.hasNext() && line == null) {
      line = textReader.nextLine().strip();
      if (line.charAt(0) == '#') line = null;
    }
    textReader.close();
    
    // Determine which file processing to use
    if (line.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) { // XML
      // <registry url="http://localhost:9200" index="registry" auth="/path/to/auth.cfg" />
      DocumentBuilder builder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
      Document document = builder.parse(file);
      NodeList registries = document.getElementsByTagName("registry");

      if (registries.getLength() != 1)
        throw new SAXException("There should be one and only registry tag in the harvest config file but found " + registries.getLength());
      if (registries.item(0).getAttributes().getNamedItem("auth") == null)
        throw new SAXException("Requires an authorization file or 'auth' attribute on <registry>.");

      filename = registries.item(0).getAttributes().getNamedItem("auth").getNodeValue();
      url = registries.item(0).getAttributes().getNamedItem("url").getNodeValue();
      url += ("/" + registries.item(0).getAttributes().getNamedItem("index").getNodeValue());
    }
    else { // java property
      FileInputStream input = new FileInputStream(file);
      Properties properties = new Properties();
      properties.load(input);
      url = properties.getProperty("url");
      filename = properties.getProperty("credentials");
      input.close();
    }
    
    // Get credentials
    FileInputStream input = new FileInputStream(filename);
    Properties properties = new Properties();
    properties.load(input);
    pwd = properties.getProperty("password");
    tss = Boolean.valueOf(properties.getProperty("trust.self-signed", "false"));
    un = properties.getProperty("user");
    input.close();
    return new AuthInformation(tss, pwd, un, url);
  }

  public String getPassword() {
    return password;
  }
  public boolean getTrustSelfSigned() {
    return trustSelfSigned;
  }
  public String getUsername() {
    return username;
  }
  public String getUrl() {
    return url;
  }
}
