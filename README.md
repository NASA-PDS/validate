# Validate Tool
Project containing software for validating PDS4 products and PDS3 volumes. 
The software is packaged in a JAR file with a command-line wrapper script
to execute validation.

# Dependencies
Two dependencies for this tool are not yet in Maven Central repository so they will need to be installed in your local maven repository in order to build and compile.

## PDS3 Product Tools
1. Clone the repo and checkout v4.0.0:
```
git clone https://github.com/NASA-PDS-Incubator/pds3-product-tools.git
cd pds3-product-tools
git checkout tags/v4.0.0
```
2. Run `mvn install` to install the package in your local maven repo.

## PDS4 JParser
1. Clone the repo and checkout v1.0.0:
```
git clone https://github.com/NASA-PDS-Incubator/pds4-jparser.git
cd pds4-jparser
git checkout tags/v1.0.0
```
2. Run `mvn install` to install the package in your local maven repo.

# Build
The software can be compiled and built with the "mvn compile" command but in order 
to create the JAR file, you must execute the "mvn compile jar:jar" command. 

In order to create a complete distribution package, execute the 
following commands: 

% mvn site
% mvn package


# Documentation
The documentation including release notes, installation and operation of the 
software should be online at 
https://pds-engineering.jpl.nasa.gov/development/pds4/current/preparation/validate/. If it is not 
accessible, you can execute the "mvn site:run" command and view the 
documentation locally at http://localhost:8080.
