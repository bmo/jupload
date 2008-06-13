//
// $Id: JUploadException.java 95 2007-05-02 03:27:05Z
// /C=DE/ST=Baden-Wuerttemberg/O=ISDN4Linux/OU=Fritz
// Elfert/CN=svn-felfert@isdn4linux.de/emailAddress=fritz@fritz-elfert.de $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// 
// Created: 2006-05-09
// Creator: etienne_sf
// Last modified: $Date$
//
// This program is free software; you can redistribute it and/or modify it under
// the terms of the GNU General Public License as published by the Free Software
// Foundation; either version 2 of the License, or (at your option) any later
// version. This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// details. You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software Foundation, Inc.,
// 675 Mass Ave, Cambridge, MA 02139, USA.

package wjhk.jupload2.exception;

/**
 * A new kind of exceptions. Currently : no other specialization than its name.
 * 
 * @author etienne_sf
 * @version $Revision$
 */
public class JUploadException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = -6386378085666411905L;

    private String location = null;

    /**
     * Creates a new instance with a specified message.
     * 
     * @param message The message to be associated with this instance.
     */
    public JUploadException(String message) {
        super(message);
        StackTraceElement[] trace = super.getStackTrace();
        if (trace.length > 0) {
            StackTraceElement se = trace[0];
            this.location = se.getClassName() + "." + se.getMethodName()
                    + " in " + se.getFileName() + ", line "
                    + se.getLineNumber();
        }
    }

    /**
     * Creates a new instance with a specified original exception.
     * 
     * @param ex The exception that was originally thrown.
     */
    public JUploadException(Throwable ex) {
        super(ex);
        StackTraceElement[] trace = ex.getStackTrace();
        if (trace.length > 0) {
            StackTraceElement se = trace[0];
            this.location = se.getClassName() + "." + se.getMethodName()
                    + " in " + se.getFileName() + ", line "
                    + se.getLineNumber();
        }
    }

    /**
     * Creates a new instance with a specified message and original exception.
     * 
     * @param message The message to be associated with this instance.
     * @param ex The exception that was originally thrown.
     */
    public JUploadException(String message, Throwable ex) {
        super(message, ex);
        StackTraceElement[] trace = ex.getStackTrace();
        if (trace.length > 0) {
            StackTraceElement se = trace[0];
            this.location = se.getClassName() + "." + se.getMethodName()
                    + " in " + se.getFileName() + ", line "
                    + se.getLineNumber();
        }
    }

    /**
     * Retrieves the human readable location of this exception (Class.method,
     * filename, linenumber)
     * 
     * @return The location where this exception was thrown.
     */
    public String getLocation() {
        return (null == this.location) ? "unknown location" : this.location;
    }

    /**
     * Returns JUploadExceptionClassName:CauseClassName. For instance:<BR>
     * wjhk.jupload2.exception.JUploadIOException:FileNotFoundException <BR>
     * or<BR>
     * wjhk.jupload2.exception.JUploadIOException (if there is no cause given to
     * the JUploadException constructor).
     * 
     * @return The class name(s) that can be displayed in an error message.
     */
    public String getClassNameAndClause() {
        if (getCause() == null) {
            return this.getClass().getName();
        } else {
            return this.getClass().getName() + ":"
                    + this.getCause().getClass().getName();
        }
    }
}
