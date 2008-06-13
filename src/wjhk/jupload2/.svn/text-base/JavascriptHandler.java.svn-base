/*
 * $Id$
 */

/*
 * Copyright (C) 2008 Ed Huott
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package wjhk.jupload2;

import wjhk.jupload2.gui.JUploadPanel;
import wjhk.jupload2.policies.UploadPolicy;

/**
 * Separate thread spawned by the (signed) applet at initialization time so it
 * will run in a context with the same privileges. Does nothing but wait to be
 * notified of the presence of a command to be executed in the jsCommand String
 * variable.
 */
public class JavascriptHandler extends Thread {

    /**
     * Command code, for upload.
     */
    public final static String COMMAND_START_UPLOAD = "startUpload";

    /**
     * One return code for doCommand: indicates that the thread is busy, and can
     * execute this command.
     */
    public final static String RETURN_BUSY = "busy";

    /**
     * One return code for doCommand: indicates that the thread is busy, and can
     * execute this command.
     */
    public final static String RETURN_STARTED = "started";

    /**
     * Reference to the current upload policy.
     */
    private UploadPolicy uploadPolicy = null;

    /**
     * Reference to the main panel of the applet.
     */
    private JUploadPanel jUploadPanel = null;

    /**
     * The current command, or null if the thread is not currently running
     * command.
     */
    private String jsCommand = null;

    /**
     * Constructor for JavascriptHandler
     * 
     * @param uploadPolicy The current upload policy. Used for debug output.
     * @param theJUploadPanel Whose methods will will be invoked in order to
     *            execute the received commands
     */
    public JavascriptHandler(UploadPolicy uploadPolicy,
            JUploadPanel theJUploadPanel) {
        this.uploadPolicy = uploadPolicy;
        this.jUploadPanel = theJUploadPanel;
        
        //Let's start our thread.
        this.start();
    }

    /**
     * Method for passing a command (String) to be executed (asynchronously) by
     * the run() method of this object's thread. Commands are accepted only if
     * there is no previous command still executing. (Commands are not queued.)
     * Return value indicates if command was successfully submitted.
     * 
     * @param command
     * @return the command string argument on success, empty string on failure.
     */
    public synchronized String doCommand(String command) {
        if (this.jsCommand != null) {
            // The previous command not yet finished, we do nothing, and
            // indicate it.
            return RETURN_BUSY;
        }

        this.jsCommand = command;
        uploadPolicy.displayDebug(
                "JavascriptHandler - doCommand(): jsCommand is: ["
                        + this.jsCommand + "]", 50);

        // send notify() to waiting thread so that command gets executed.
        this.notify();

        // The job will go on.
        return RETURN_STARTED;
    }

    /**
     * Synchronized method allows for safely accessing jsCommand string
     * 
     * @return Returns the current command
     */
    public synchronized String getCommand() {
        uploadPolicy.displayDebug("getCommand(): jsCommand is: ["
                + this.jsCommand + "]", 50);
        return this.jsCommand;
    }

    /**
     * Synchronized method allows for safely clearing jsCommand string
     */
    public synchronized void clearCommand() {
        this.jsCommand = null;
        uploadPolicy.displayDebug("clearCommand(): jsCommand is: ["
                + this.jsCommand + "]", 50);
    }

    /**
     * Synchronized method to enable call to wait()
     * 
     * @throws InterruptedException
     */
    public synchronized void doWait() throws InterruptedException {
        wait();
    }

    /**
     * Method to run when thread is started.
     */
    public void run() {
        // Run in continuous loop waiting for commands to execute
        while (true) {
            try {
                // simply wait to be notified that a command is ready to run
                doWait();
                uploadPolicy.displayDebug("run(): Exited doWait()...", 50);

                // handle new command
                String curCommand = getCommand();
                if (curCommand != null) {
                    if (curCommand.equals(COMMAND_START_UPLOAD)) {
                        // start the upload
                        uploadPolicy.displayDebug(
                                "run(): Calling doStartUpload()", 50);
                        jUploadPanel.doStartUpload();
                    }

                    // prepare for next command by resetting jsCommand variable
                    clearCommand();
                }
            } catch (InterruptedException eInterrupted) {
                uploadPolicy.displayDebug("Interrupted: ["
                        + eInterrupted.getMessage() + "]", 50);
            } catch (Exception eOther) {
                uploadPolicy.displayDebug("Exception: [" + eOther.getMessage()
                        + "]", 50);
            }
        }
    } // run()

} // class JavascriptHandler
