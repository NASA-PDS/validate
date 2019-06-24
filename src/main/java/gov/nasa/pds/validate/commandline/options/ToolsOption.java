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

package gov.nasa.pds.validate.commandline.options;

import org.apache.commons.cli.Option;

/**
 * Class that extends Apache's Option class. Provides a simpler interface to
 * build command-line option flags.
 *
 *
 * @author mcayanan
 *
 */
public class ToolsOption extends Option {
    /**
     * Constructor.
     *
     * @param opt Short name of the option.
     * @param longOpt Long name of the option. Can be set to 'null'.
     * @param description Description of the option.
     */
    public ToolsOption(String opt, String longOpt, String description) {
        super(opt, longOpt, false, description);
    }

    public ToolsOption(Flag flag) {
        this(flag.getShortName(), flag.getLongName(), flag.getDescription());
        if (flag.getArgType() != null) {
          if (flag.allowsMultipleArgs()) {
            hasArgs(flag.getArgName(), flag.getArgType());
          } else {
            hasArg(flag.getArgName(), flag.getArgType());
          }
        }
    }

    /**
     * Requires a single argument to follow the option.
     *
     * @param name Sets the display name of the argument value.
     * @param type Sets the data type allowed for this argument.
     */
    public void hasArg(String name, Object type) {
        hasArg(name, type, false);
    }

    /**
     * Allows a single argument to be passed into the option.
     *
     * @param name Sets the display name of the argument value.
     * @param type Sets the data type allowed for this argument.
     * @param isOptional Set to 'true' if the argument is optional,
     * 'false' otherwise.
     */
    public void hasArg(String name, Object type, boolean isOptional) {
        char nullChar = '\0';
        hasArgs(1, name, type, nullChar, isOptional);
    }

    /**
     * Requires an argument to follow the option. This method allows the option
     * to take in multiple arguments. Does not define a maximum
     * number of allowable arguments.
     *
     * The separator value is set to the space character ' '.
     *
     * @param name Sets the display name of the argument value.
     * @param type Sets the data type allowed for this argument.
     */
    public void hasArgs(String name, Object type) {
       char argSeparator = ',';
        hasArgs(name, type, argSeparator, false);
    }

    /**
     * Requires an argument to follow the option. Allows multiple arguments
     * to be passed in to the option. Does not define a maximum number of
     * allowable arguments.
     *
     *
     * @param name Sets the display name of the argument value.
     * @param type Sets the data type allowed for this argument.
     * @param separator Sets the separator value allowed in between the
     * argument values being passed in.
     */
    public void hasArgs(String name, Object type, char separator) {
        hasArgs(name, type, separator, false);
    }

    /**
     * Allows multiple arguments to be passed in to the option. Does not
     * define a maximum number of allowable arguments.
     *
     * @param name Sets the display name of the argument value.
     * @param type Sets the data type allowed for this argument.
     * @param separator Sets the separator value allowed in between the
     * argument values being passed in.
     * @param isOptional Set to 'true' if an argument is optional,
     * 'false' otherwise.
     */
    public void hasArgs(String name, Object type, char separator,
            boolean isOptional) {
        hasArgs(Option.UNLIMITED_VALUES, name, type, separator, isOptional);
    }

    /**
     * Defines an argument's "properties" for an option.
     *
     * @param numArgs Max number of arguments allowed.
     * @param name Sets the display name of the argument value.
     * @param type Sets the data type allowed for this argument.
     * @param separator Sets the separator value allowed in between the
     * argument values being passed in.
     * @param isOptional Set to 'true' if an argument is optional, 'false'
     * otherwise.
     */
    public void hasArgs(int numArgs, String name, Object type, char separator,
            boolean isOptional) {
        setArgs(numArgs);
        setArgName(name);
        setType(type);
        setValueSeparator(separator);
        setOptionalArg(isOptional);
    }

}
