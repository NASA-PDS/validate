// Copyright 2006-2017, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate;

import java.util.Collection;
import java.util.Map;

/**
 * Defines an interface for keeping track of validation targets
 * and their attributes.
 *
 * <p>The registrar enables answering these questions:</p>
 * <ul>
 *   <li>Is every target either a label or have a reference from a label?</li>
 *   <li>Does every referenced identifier (e.g., PDS4 LID) have a definition
 *   in some target?</li>
 *   <li>Is any identifier defined twice?</li>
 * </ul>
 */
public interface TargetRegistrar {

	/**
	 * Gets the root location.
	 *
	 * @return the root location
	 */
	ValidationTarget getRoot();

    /**
     * Indicates that a target was discovered in the subtree
     * being validated.
     *
     * @param parentLocation the parent target location, or null, if this is the root target
     * @param type the target type
     * @param location the target location
     */
    void addTarget(String parentLocation, TargetType type, String location);

    /**
     * Gets the locations of children of a given target.
     *
     * @param parent the parent target
     * @return a collection of child targets
     */
    Collection<ValidationTarget> getChildTargets(ValidationTarget parent);

    /**
     * Tests whether a target exists for a location.
     *
     * @param targetLocation the target location
     * @return true, if a target has been added for that location, false otherwise
     */
    boolean hasTarget(String targetLocation);

    /**
     * Gets the number of targets encountered so far for a target type.
     *
     * @param type the target type
     * @return the target count for the given type
     */
    int getTargetCount(TargetType type);

    /**
     * Indicates whether a target is a label.
     *
     * @param location the target location
     * @param isLabel true, if the target is a label
     */
    void setTargetIsLabel(String location, boolean isLabel);

    /**
     * Gets the number of labels encountered so far.
     *
     * @return the label count
     */
    int getLabelCount();

    /**
     * Sets an identifier for uniquely identifying the target.
     *
     * @param location the target location
     * @param identifier the target identifier
     */
    void setTargetIdentifier(String location, Identifier identifier);

    /**
     * Adds a reference from a label to a target.
     *
     * @param referenceLocation the label location
     * @param targetLocation the target location referred to in the label
     */
    void addTargetReference(String referenceLocation, String targetLocation);

    /**
     * Tests whether a target was referenced.
     *
     * @param location the target location
     * @return true, if the target was referenced
     */
    boolean isTargetReferenced(String location);

    /**
     * Adds a reference to a target identifier.
     *
     * @param referenceLocation the location of the reference
     * @param identifier the target identifier being referenced
     */
    void addIdentifierReference(String referenceLocation, Identifier identifier);

    /**
     * Tests whether an identifier was referenced.
     *
     * @param identifier the identifier
     * @return true, if the identifier was referenced
     */
    boolean isIdentifierReferenced(Identifier identifier);

    /**
     * Gets the location where an identifier was defined.
     *
     * @param identifier the identifier
     * @return the location where it was defined, or null if not defined
     */
    String getTargetForIdentifier(Identifier identifier);


    /**
     * Gets a mapping of identifiers to their locations.
     * 
     * @return a mapping of identifiers to the location where it was defined.
     */
    Map<Identifier, String> getIdentifierDefinitions();
    
    /**
     * Gets a collection of target locations that are never referenced.
     *
     * @return a collection of unreferenced targets
     */
    Collection<String> getUnreferencedTargets();

    /**
     * Gets a collection of identifiers that are defined but never
     * referenced.
     *
     * @return a collection of unreferenced identifiers
     */
    Collection<Identifier> getUnreferencedIdentifiers();

    /**
     * Gets a collection of identifiers that are referenced but not defined.
     *
     * @return a collection of dangling identifier references
     */
    Collection<IdentifierReference> getDanglingReferences();
    
    /**
     * Gets a collection of referenced identifiers.
     *
     * @return a collection of referenced identifiers.
     */
    Collection<Identifier> getReferencedIdentifiers();
    
    /**
     * Gets the location of where the given identifier is referenced.
     * 
     * @param id The identifier to find.
     * 
     * @return The location where the given identifier is referenced.
     */
    String getIdentifierReferenceLocation(Identifier id); 

    public Map<String, ValidationTarget> getCollections();

    public void setCollections(Map<String, ValidationTarget> collections);

    public Map<String, ValidationTarget> getBundles();

    public void setBundles(Map<String, ValidationTarget> bundles);

}
