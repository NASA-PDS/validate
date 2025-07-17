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

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.util.Utility;
import software.amazon.awssdk.services.opensearch.endpoints.internal.Value;

public class InMemoryRegistrar implements TargetRegistrar {

  private static Logger LOG = LoggerFactory.getLogger(InMemoryRegistrar.class);
  private ValidationTarget rootTarget;
  private Map<String, ValidationTarget> targets = new HashMap<>();
  private Map<String, ValidationTarget> collections = new HashMap<>();
  private Map<String, ValidationTarget> bundles = new HashMap<>();
  private Set<String> referencedTargetLocations = new HashSet<>();
  private Map<Identifier, String> identifierDefinitions = new HashMap<>();
  private Map<String, Set<Identifier>> identifierDefinitionsByLid = new HashMap<>();
  private Map<Identifier, String> identifierReferenceLocations = new HashMap<>();
  private Set<Identifier> referencedIdentifiers = new HashSet<>();
  private Map<String, Set<Identifier>> referencedIdentifiersByLid = new HashMap<>();

  @Override
  public ValidationTarget getRoot() {
    return rootTarget;
  }

  @Override
  public synchronized void addTarget(String parentLocation, TargetType type, String location) {
    ValidationTarget target;
    try {
      target = ValidationTarget.build(location, type);
      if (parentLocation == null) {
        this.rootTarget = target;
      }

      if (type.equals(TargetType.BUNDLE)) {
        this.bundles.put(location, target);
      } else if (type.equals(TargetType.COLLECTION)) {
        this.collections.put(location, target);
      }

      this.targets.put(location, target);
      LOG.debug("addTarget(): location: {}, target: {}", location, target);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public synchronized Collection<ValidationTarget> getChildTargets(ValidationTarget parent) {
    List<ValidationTarget> children = new ArrayList<>();
    String parentLocation = parent.getLocation() + File.separator;

    for (String targetLocation : targets.keySet()) {
      if (targetLocation.startsWith(parentLocation)
          && !targetLocation.substring(parentLocation.length()).contains(File.separator)) {
        children.add(targets.get(targetLocation));
      }
    }

    Collections.sort(children);
    return children;
  }

  @Override
  public synchronized boolean hasTarget(String targetLocation) {
    return targets.containsKey(targetLocation);
  }

  @Override
  public synchronized int getTargetCount(TargetType type) {
    int count = 0;

    for (Map.Entry<String, ValidationTarget> entry : targets.entrySet()) {
      if (entry.getValue().getType() == type) {
        ++count;
      }
    }
    return count;
  }

  @Override
  public synchronized void setTargetIsLabel(String location, boolean isLabel) {
    targets.get(location).setLabel(isLabel);

    // Labels refer to themselves.
    if (isLabel) {
      addTargetReference(location, location);
    }
  }

  @Override
  public synchronized int getLabelCount() {
    int count = 0;
    for (Map.Entry<String, ValidationTarget> entry : targets.entrySet()) {
      if (entry.getValue().isLabel()) {
        ++count;
      }
    }

    return count;
  }

  @Override
  public synchronized void setTargetIdentifier(String location, Identifier identifier) {
    targets.get(location).setIdentifier(identifier);
    LOG.debug("setTargetIdentifier:identifier,location {},{}", identifier, location);
    identifierDefinitions.put(identifier, location);

    identifierDefinitionsByLid.computeIfAbsent(identifier.getLid(), x -> new HashSet<>()).add(identifier);
  }

  @Override
  public synchronized void addTargetReference(String referenceLocation, String targetLocation) {
    referencedTargetLocations.add(targetLocation);
  }

  @Override
  public synchronized boolean isTargetReferenced(String location) {
    return referencedTargetLocations.contains(location);
  }

  @Override
  public synchronized void addIdentifierReference(String referenceLocation, Identifier identifier) {
    referencedIdentifiers.add(identifier);
    identifierReferenceLocations.put(identifier, referenceLocation);

    String lid = identifier.getLid();

    referencedIdentifiersByLid.computeIfAbsent(identifier.getLid(), x -> new HashSet<>()).add(identifier);
  }

  @Override
  public synchronized boolean isIdentifierReferenced(Identifier identifier, boolean orNearNeighbor) {
    boolean result = referencedIdentifiers.contains(identifier);
    if (!result && orNearNeighbor) {
      for (Identifier id : this.referencedIdentifiersByLid.getOrDefault(identifier.getLid(), Collections.emptySet())) {
        result = identifier.nearNeighbor(id);
        if (result) break;
      }
    }
    return result;
  }

  @Override
  public synchronized String getTargetForIdentifier(Identifier identifier, boolean orNearNeighbor) {
    String result = null;
    if (this.identifierDefinitions.containsKey(identifier)) {
      result = identifierDefinitions.get(identifier);
    } else if (orNearNeighbor) {
      for (Identifier id : identifierDefinitionsByLid.getOrDefault(identifier.getLid(), Collections.emptySet())) {
        if (id.nearNeighbor(identifier)) {
          result = this.identifierDefinitions.get(id);
        }
      }
    }
    return result;
  }

  @Override
  public Map<Identifier, String> getIdentifierDefinitions() {
    return this.identifierDefinitions;
  }

  @Override
  public synchronized Collection<String> getUnreferencedTargets() {
    Set<String> unreferencedTargets = new TreeSet<>();
    // Ignore directory targets
    // LOG.debug("getUnreferencedTargets: targets.keySet(),targets.keySet().size()
    // {},{}",targets.keySet(),targets.keySet().size());
    int fileCount = 0;
    int unreferencedCount = 0;

    Set<String> candidates = new TreeSet<>(targets.keySet());
    candidates.removeAll(referencedTargetLocations);
    // The function Utility.isDir() has a bug as of 01/20/2021. It thinks a file
    // with no extension is a directory.
    // This bug causes any files without extension to be ignored to be checked for
    // unreferenced.
    // Until the bug in Utility.java is fixed, the code is this class will not have
    // a correct accounting
    // of unreferenced files.

    for (String target : candidates) {
      fileCount += 1;
      boolean isDir = Utility.isDir(target);
      LOG.debug("getUnreferencedTargets: fileCount,target,Utility.isDir(target) {},{},{}",
          fileCount, target, isDir);
      if (!isDir) {
        unreferencedCount += 1;
        unreferencedTargets.add(target);
        LOG.debug(
            "getUnreferencedTargets: UNREFERENCED_TARGETS_ADD,fileCount,target,Utility.isDir(target),unreferencedCount {},{},{},{}",
            fileCount, target, isDir, unreferencedCount);
        LOG.debug(
            "getUnreferencedTargets: UNREFERENCED_TARGETS_ADD,fileCount,unreferencedCount,target {},{},{}",
            fileCount, unreferencedCount, target);
      }
    }
    LOG.debug("getUnreferencedTargets: UNREFERENCED_COUNT fileCount,unreferencedCount {},{}",
        fileCount, unreferencedCount);
    return unreferencedTargets;
  }

  @Override
  public synchronized Collection<Identifier> getUnreferencedIdentifiers() {
    List<Identifier> unreferencedIdentifiers = new ArrayList<>();
    for (Identifier id : identifierDefinitions.keySet()) {
      boolean found = this.referencedIdentifiers.contains(id);
      if (!found) {
        for (Identifier ri : referencedIdentifiersByLid.getOrDefault(id.getLid(), new HashSet<>())) {
          if (ri.nearNeighbor(id)) {
            found = true;
            break;
          }
        }
      }
      if (!found) {
        unreferencedIdentifiers.add(id);
      }
    }
    return unreferencedIdentifiers;
  }

  @Override
  public synchronized Collection<IdentifierReference> getDanglingReferences() {
    Set<Identifier> undefinedIdentifiers = new HashSet<>();
    undefinedIdentifiers.addAll(referencedIdentifiers);
    undefinedIdentifiers.removeAll(identifierDefinitions.keySet());

    Set<IdentifierReference> danglingRefs = new TreeSet<>();
    for (Identifier identifier : undefinedIdentifiers) {
      danglingRefs
          .add(new IdentifierReference(identifierReferenceLocations.get(identifier), identifier));
    }

    return danglingRefs;
  }

  @Override
  public synchronized String getIdentifierReferenceLocation(Identifier id, boolean orNearNeighbor) {
    String result = null;
    if (this.identifierReferenceLocations.containsKey(id)) {
      result = this.identifierReferenceLocations.get(id);
    } else if (orNearNeighbor) {
      for (Identifier ri : identifierReferenceLocations.keySet()) {
        if (ri.nearNeighbor(id)) {
          result = identifierReferenceLocations.get(ri);
          break;
        }
      }
    }
    return result;
  }

  @Override
  public Map<String, ValidationTarget> getCollections() {
    return collections;
  }

  @Override
  public void setCollections(Map<String, ValidationTarget> collections) {
    this.collections = collections;
  }

  @Override
  public Map<String, ValidationTarget> getBundles() {
    return bundles;
  }

  @Override
  public void setBundles(Map<String, ValidationTarget> bundles) {
    this.bundles = bundles;
  }

  @Override
  public Map<String, ValidationTarget> getTargets() {
    return targets;
  }

  @Override
  public void setTargets(Map<String, ValidationTarget> targets) {
    this.targets = targets;
  }
}
