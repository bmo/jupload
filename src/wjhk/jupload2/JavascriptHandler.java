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
import wjhk.jupload2.filedata.FileData;


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
     * Command code, select files.
     */
    public final static String COMMAND_SELECT_FILES = "selectFiles";

    public final static String COMMAND_SELECT_FILE = "selectFile";

    public final static String COMMAND_GET_FILE = "getFile";
    /**
     * Command code, select files.
     */
    public final static String COMMAND_GET_STATS = "getStats";

    /**
     * Command code, cancel upload
     */
    public final static String COMMAND_CANCEL_UPLOAD = "cancelUpload";

    /**
     * Command code, cancel upload
     */
    public final static String COMMAND_STOP_UPLOAD = "stopUpload";

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
    private String jsResponse = null;
    private boolean jscmdEmpty = true;
    private boolean jsrespReady = false;
    private Object[] jsArgs;
    /**
     * The command result, or null if the thread is not currently running
     * command.
     */
    private String jsCommandResult = null;





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
    public synchronized String doCommandX(String command) {
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
    public synchronized String doCommand(String command, Object... args) {
        if (!this.jscmdEmpty) {
            return RETURN_BUSY;
        }
        //Toggle status.
        this.jscmdEmpty = false;
        this.jsrespReady = false;
        this.jsCommand = command;
        this.jsArgs = args;
        uploadPolicy.displayDebug(
                  "JavascriptHandler - doCommand(): jsCommand is: ["
                          + getCommand() + "]", 50);
        this.notify();
        // wait for response  - note that some operations need to return immediately, especially those that would generate an event.
        if (!this.jsrespReady) {
            try {
                wait();
            } catch (InterruptedException ex) { uploadPolicy.displayDebug(
                  "Interrupted - proceeding",50);}
        }
        String outp = (this.jsResponse==null ? "NULL" : this.jsResponse); 
        uploadPolicy.displayDebug(
                  "JavascriptHandler - doCommand(): finished : ["
                          + outp+ "]", 50);
        this.jscmdEmpty = true;
        return this.jsResponse;
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

    public synchronized Object[] getArgs() {
      return this.jsArgs;
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
    public synchronized void doNotify() throws InterruptedException {
        this.notify();
    }

    /**
     * Method to run when thread is started.
     */
    public void run() {
        boolean notified=false;
        // Run in continuous loop waiting for commands to execute
        while (true) {
            try {
                // simply wait to be notified that a command is ready to run
                doWait();
                notified =false;
                uploadPolicy.displayDebug("run(): Exited doWait()...", 50);

                // handle new command
                String curCommand = getCommand();       // TODO consolidate to one call.
                Object[] args = getArgs();

                if (curCommand != null) {
                    if (curCommand.equals(COMMAND_START_UPLOAD)) {    //* ASYNCHRONOUS -- don't wait the calling thread
                        // start the upload
                        notified=true;
                        doNotify(); // don't wait -
                        uploadPolicy.displayDebug(
                                "run(): Calling doStartUpload()", 50);
                        if (args.length>0 && (args[0]!=null)) {
                          // do single upload of a particular file
                          if (args[0] instanceof String) {
                            uploadPolicy.displayDebug(
                                "Argument is "+args[0], 50);
                            //jUploadPanel.doStartUploadSingle((String) args[0]);
                          } else if (args[0] instanceof Integer){
                            uploadPolicy.displayDebug(
                                "Argument is an int"+ args[0].toString(), 50);
                            //jUploadPanel.doStartUploadSingle((Integer) args[0]);
                          }
                        } else {
                          jUploadPanel.doStartUpload();  //
                          this.jsResponse=null;
                        }
                    }

                    if (curCommand.equals(COMMAND_STOP_UPLOAD)) {
                        // start the upload
                        uploadPolicy.displayDebug(
                                "run(): Calling doStopUpload()", 50);
                        jUploadPanel.doStopUpload();
                    }

                    if (curCommand.equals(COMMAND_CANCEL_UPLOAD)) {      //* ASYNCHRONOUS -- don't wait the calling thread
                        // stop the upload
                        notified=true;
                        doNotify(); // don't wait -
                        uploadPolicy.displayDebug(
                                "run(): Calling doCancelUpload()", 50);
                         jUploadPanel.doStopUpload();
                        //jUploadPanel.doStopUpload();
                        this.jsResponse = "ASYNC";
                    }
                    if (curCommand.equals(COMMAND_GET_STATS)) {
                      String gsj = uploadPolicy.getStatsJSON();
                      // start the upload
                      uploadPolicy.displayDebug(
                              "run(): Calling getStats()", 50);
                      uploadPolicy.displayDebug(
                              gsj, 50);
                      //jUploadPanel.doStopUpload();
                      this.jsResponse = gsj;
                    }

                  if (curCommand.equals(COMMAND_GET_FILE)) {
                    uploadPolicy.displayDebug(
                            "run(): Calling getFile()", 50);
                    if (args.length > 0 && (args[0] != null)) {
                      if (args[0] instanceof String) {
                        uploadPolicy.displayDebug(
                                "Argument is " + args[0], 50);
                        FileData fd = jUploadPanel.getFilePanel().getFileByExternalId((String)args[0]);
                        this.jsResponse = (fd == null ? null : fd.getJSON());
                      } else if (args[0] instanceof Integer) {
                        uploadPolicy.displayDebug(
                                "Argument is an int" + args[0].toString(), 50);
                        FileData fd = jUploadPanel.getFilePanel().getFileByExternalIndex((Integer) args[0]);
                        this.jsResponse = (fd == null ? null : fd.getJSON());

                      } else this.jsResponse = null;

                    } else {
                      this.jsResponse = null;
                      uploadPolicy.displayDebug(
                              "before getFile() -- no argument", 50);
                    }
                  }



              if (curCommand.equals(COMMAND_SELECT_FILES)) {       //* ASYNCHRONOUS -- don't wait the calling thread
                        // pop the select file dialog, but signal for the other thread to continue, fist
                        notified=true;
                        doNotify(); // don't wait -

                        uploadPolicy.displayDebug(
                                "run(): Calling selectFiles()", 50);

                        jUploadPanel.doSelectFiles();
                        this.jsResponse = null;
                        //jUploadPanel.doStopUpload();
                    }
                   this.jsrespReady = true;
                   if (!notified)
                     doNotify();
                }

            } catch (InterruptedException eInterrupted) {
                uploadPolicy.displayDebug("Interrupted: ["
                        + eInterrupted.getMessage() + "]", 50);
            } catch (Exception eOther) {
                uploadPolicy.displayDebug("Exception: [" + eOther.toString()+eOther.getMessage()
                        + "]", 50);
            }
        }
    } // run()

} // class JavascriptHandler

