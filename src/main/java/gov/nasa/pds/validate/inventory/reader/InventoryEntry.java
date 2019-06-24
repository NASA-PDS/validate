// Copyright © 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
//   conditions and the following disclaimer in the documentation and/or other
//   materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
//   Laboratory, nor the names of its contributors may be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.validate.inventory.reader;

import java.io.File;

/**
 * Class representation of a single entry in a PDS Inventory file.
 *
 * @author mcayanan
 *
 */
public class InventoryEntry {
  /** A product file. */
  private File file;

  /** A checksum. */
  private String checksum;

  /** A logical identifier. */
  private String identifier;

  /** Member status. */
  private String memberStatus;

  /** Default constructor */
  public InventoryEntry() {
      this.file = null;
      this.checksum = "";
      this.identifier = "";
      this.memberStatus = "";
  }

  /**
   * Constructor.
   *
   * @param identifier logical identifier.
   * @param memberStatus member status.
   */
  public InventoryEntry(String identifier, String memberStatus) {
    this(null, "", identifier, memberStatus);
  }

  /**
   * Constructor.
   *
   * @param file A product file.
   * @param checksum checksum.
   * @param identifier logical identifier.
   */
  public InventoryEntry(File file, String checksum, String identifier, String memberStatus) {
      this.file = file;
      this.checksum = checksum;
      this.identifier = identifier;
      this.memberStatus = memberStatus;
  }

  /**
   * Gets the file.
   *
   * @return The file.
   */
  public File getFile() {
      return file;
  }

  /**
   * Gets the checksum.
   *
   * @return Checksum value.
   */
  public String getChecksum() {
      return checksum;
  }

  /**
   * Gets the logical identifier.
   *
   * @return A LID or LIDVID.
   */
  public String getIdentifier() {
      return identifier;
  }

  /**
   * Gets the member status.
   *
   * @return "P", "Primary", "S", or "Secondary"
   */
  public String getMemberStatus() {
    return memberStatus;
  }

  /**
   * Determines whether the object is empty.
   *
   * @return true if the object is empty, false otherwise.
   */
  public boolean isEmpty() {
      if (this.file == null && this.checksum.isEmpty()
          && this.identifier.isEmpty() && this.memberStatus.isEmpty()) {
        return true;
      } else {
        return false;
      }
  }
}
