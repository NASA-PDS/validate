// Copyright 2006-2013, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$

package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

/**
 * A class to allow reading and writing text file.
 *
 */
public class FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);


    public static void main(String[] args) {
        // Helpful to debug if function is working properly.
        System.out.println("Hello, World");
        String[] textArray = {"line1","line2"};
        appendTextToOneLine("somefile.txt",textArray);
    }

    /**
     * Open the file for writing and write a text string.
     *
     * @param filename  Output filename
     * @param textArray An array of strings
     * @return None 
     */
    public static void appendTextToOneLine(String filename, String textStr) {
        String[] textArray = new String [1];
        textArray[0] = textStr;
        appendTextToOneLine(filename,textArray);
    }

    /**
     * Open the file for writing and write textArray separated by a space to file all in one line.
     *
     * @param filename  Output filename
     * @param textArray An array of strings
     * @return None 
     */
    public static void appendTextToOneLine(String filename, String[] textArray) {
        try {
            FileWriter writer = new FileWriter(filename, false);
            for(int i = 0; i< textArray.length; i++){
                System.out.println(textArray[i]);
                if (i == 0) {
                    writer.write(textArray[i]);
                } else {
                    writer.write(" " + textArray[i]);  // Put a space in between each element.
                }
            }
            writer.write("\n");   // Write new line as last write before closing.
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Print a stack trace to a text file.
     *
     * @param filename  Output filename.
     * @param exception The exception to print stack trace of.
     * @return None 
     */

    public static void printStackTraceToFile(String filename, Exception exception) {
        //File file = new File("validate_stack_traces.log");

        // If a name is not provided, use the hard-code filename.
        if (filename == null) {
            filename = "validate_stack_traces.log";
        }
        // Do a sanity check that the name should end with .log or .txt so as not to overwrite a source code.
        if (!filename.endsWith(".txt") && !filename.endsWith(".log")) {
            System.out.println("FileService:printStackTraceToFile:ERROR: This function can only write to file that ends with .txt or .log");
            System.exit(0);
        }

        // Print the stack strace to a temporary file and then concatenate to an existing file if it exists.
        File file = new File(filename + ".temp");
        try {
            PrintStream ps = new PrintStream(file);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            ps.print("--------------------------------------------------------------------------------\n");
            ps.print("EXCEPTION_TIME " + dtf.format(now) + "\n");  // Report when the error occurred so it can be found if searched for.
            exception.printStackTrace(ps);  // Print the stack from exception from the parameter.
            ps.close();

            // Concatenate the temporary file if an existing file already exists.
            File existingFile = new File(filename);
            if (existingFile.exists()) {
                // Read the temporary file as string.
                String newFileStr = FileUtils.readFileToString(file);
                // Concatenate the temporary file to the existing file.
                FileUtils.write(existingFile, newFileStr, true); // true for append
                // To not overwhelm the log file for a normal user, only print these messages if in debug mode.
                if (LOG.isDebugEnabled()) {
                    LOG.error("A stack trace has been appended to file " + filename + " EXCEPTION_TIME " + dtf.format(now));
                    System.out.println("FileService:printStackTraceToFile:ERROR: A stack trace has been appended to file " + filename + " EXCEPTION_TIME " + dtf.format(now));
                }
            } else {
                // If existing file does not exist, rename the temporary file to a real file.
                file.renameTo(new File(filename));
                // To not overwhelm the log file for a normal user, only print these messages if in debug mode.
                if (LOG.isDebugEnabled()) {
                    LOG.error("A stack trace has been written to file " + filename + " EXCEPTION_TIME " + dtf.format(now));
                    System.out.println("FileService:printStackTraceToFile:ERROR: A stack trace has been written to file " + filename + " EXCEPTION_TIME " + dtf.format(now));
                }
            }
        } catch (Exception tempException) {
            System.out.println("FileService:printStackTraceToFile: Cannot create PrintStream or write to file " + filename);
            System.exit(0);
        }
    }

    /**
     * Print a document to a text file.
     *
     * @param filename  Output filename.
     * @param docSource The document (Document) to be printed
     * @return None 
     */

    public static void printDocumentToFile(String filename, Document docSource) {
        // Write a document (Document) to external file.  Useful for debugging.

        // If a name is not provided, use the hard-code filename.
        if (filename == null) {
            filename = "validate_document_dump.log";
        }
        // Do a sanity check that the name should end with .log or .txt so as not to overwrite a source code.
        if (!filename.endsWith(".txt") && !filename.endsWith(".log")) {
            System.out.println("FileService:printStackTraceToFile:ERROR: This function can only write to file that ends with .txt or .log");
            System.exit(0);
        }

        try {
            DOMSource domSource = new DOMSource(docSource);
            printDocumentToFile(filename, domSource);
        } catch(Exception ex) {
            printStackTraceToFile(null,ex);
        }
    }

    /** 
     * Print a document (DOMSource) to a text file.
     *
     * @param filename  Output filename.
     * @param domSource The DOM source to be printed
     * @return None 
     */

    public static void printDocumentToFile(String filename, DOMSource domSource) {
        // Write a document (DOMSource) to external file.  Useful for debugging.

        // If a name is not provided, use the hard-code filename.
        if (filename == null) {
            filename = "validate_document_dump.log";
        }
        // Do a sanity check that the name should end with .log or .txt so as not to overwrite a source code.
        if (!filename.endsWith(".txt") && !filename.endsWith(".log")) {
            System.out.println("FileService:printStackTraceToFile:ERROR: This function can only write to file that ends with .txt or .log");
            System.exit(0);
        }

        // Print the document to an external file.  Note that this overwrite any existing file.
        File file = new File(filename);
        try {
            StringWriter strWriter = new StringWriter();
            StreamResult result = new StreamResult(strWriter);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            FileWriter fileWriter = new FileWriter(file.getPath());
            fileWriter.write(strWriter.toString());
            fileWriter.close();
        } catch(IOException ex) {
            printStackTraceToFile(null,ex);
        } catch(TransformerException ex) {
            printStackTraceToFile(null,ex);
        }
    }





}
