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

/**
 * Exception class for handling errors when reading a PDS Inventory file.
 *
 * @author mcayanan
 *
 */
public class InventoryReaderException extends Exception {
    /** Generated serial ID. */
   private static final long serialVersionUID = 4687976349704354553L;

   /**
    * Holds the exception object.
    */
   private Exception exception;

   /** line number where the exception occurred. */
   private int lineNumber;

   /**
    * Constructor.
    *
    * @param exception An exception.
    */
   public InventoryReaderException(Exception exception) {
       super(exception.getMessage());
       this.exception = exception;
       lineNumber = -1;
   }

   /**
    * @return Returns the exception.
    */
   public Exception getException() {
     return exception;
   }

   /**
    * @return Returns the line number associated with the exception.
    * Could be -1 if it was not set.
    */
   public int getLineNumber() {
     return lineNumber;
   }

   /**
    * Sets the line number.
    *
    * @param line An integer value.
    */
   public void setLineNumber(int line) {
     lineNumber = line;
   }
}
