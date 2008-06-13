//
// $Id: DefaultFileUploadThread.java 287 2007-06-17 09:07:04 +0000 (dim., 17
// juin 2007) felfert $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// 
// Created: ?
// Creator: William JinHua Kwong
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

package wjhk.jupload2.upload;

import java.io.OutputStream;

import javax.swing.JProgressBar;

import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.exception.JUploadExceptionUploadFailed;
import wjhk.jupload2.exception.JUploadIOException;
import wjhk.jupload2.filedata.FileData;
import wjhk.jupload2.policies.UploadPolicy;

/**
 * This class is based on the {@link FileUploadThread} class. It's an abstract
 * class that contains the default implementation for the
 * {@link FileUploadThread} interface. <BR>
 * It contains the following abstract methods, which must be implemented in the
 * children classes. These methods are called in this order: <DIR>
 * <LI>For each upload request (for instance, upload of 3 files with
 * nbFilesPerRequest to 2, makes 2 request: 2 files, then the last one): <DIR>
 * <LI><I>try</I>
 * <LI>{@link #startRequest}: start of the UploadRequest.
 * <LI>Then, for each file to upload (according to the nbFilesPerRequest and
 * maxChunkSize applet parameters) <DIR>
 * <LI>beforeFile(int) is called before writting the bytes for this
 * file (or this chunk)
 * <LI>afterFile(int) is called after writting the bytes for this file
 * (or this chunk) </DIR>
 * <LI>finishRequest() </DIR> </LI>
 * <I>finally</I>cleanRequest()
 * <LI>Call of cleanAll(), to clean up any used resources, common to
 * the whole upload. </DIR>
 */
public abstract class DefaultFileUploadThread extends Thread implements
        FileUploadThread {

    // ////////////////////////////////////////////////////////////////////////////////////
    // /////////////////////// VARIABLES ///////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////

    /*
     * etienne_sf: this parameter is now removed. The incoming list of
     * files to upload is now only managed in the constructor: it's up to it to
     * manage files that can't be read. /** The given array containing the files
     * to upload. Stored in the constructor, and used in the run() method.
     */
    // FileData[] filesDataParam = null;
    /**
     * This array will contain a 'copy' of the relevant element of the
     * filesDataParam array (see the constructor). After filling the
     * filesToUpload array, the UploadThread will only manipulate it: all access
     * to the file contained by the filesDataParam given to the constructor will
     * be done through the {@link UploadFileData} contained by this array.
     */
    UploadFileData[] filesToUpload = null;

    /**
     * The upload policy contains all parameters needed to define the way files
     * should be uploaded, including the URL.
     */
    UploadPolicy uploadPolicy = null;

    /**
     * The value of the applet parameter maxChunkSize, or its default value.
     */
    // TODO to be moved to HTTP ????
    long maxChunkSize;

    /**
     * Maximum number of files for FTP upload.
     */
    int nbMaxFilesPerUpload;

    /**
     * If set to 'true', the thread will stop the crrent upload. This attribute
     * is not private as the {@link UploadFileData} class us it.
     * 
     * @see UploadFileData#uploadFile(java.io.OutputStream, long)
     */
    boolean stop = false;

    /**
     * Thread Exception, if any occured during upload.
     */
    Exception uploadException = null;

    // ////////////////////////////////////////////////////////////////////////////////////
    // /////////////////////// PRIVATE ATTRIBUTES
    // ///////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////

    /**
     * The progressBar bar, that will indicate to the user the upload state (0
     * to 100%).
     */
    private JProgressBar progressBar = null;

    /**
     * The total number of bytes to be sent. This allows the calculation of the
     * progressBar bar
     */
    private long totalFilesLength = 0;

    /** Current number of bytes that have been uploaded. */
    private long uploadedLength = 0;

    private long startTime;

    /**
     * The response message from the server, if any
     */
    private String responseMsg;

    /**
     * Creates a new instance.
     * 
     * @param filesDataParam The files to be uploaded.
     * @param uploadPolicy The upload policy to be applied.
     * @param progressBar The progressBar bar to be updated.
     */
    public DefaultFileUploadThread(FileData[] filesDataParam,
            UploadPolicy uploadPolicy, JProgressBar progressBar) {
        this.uploadPolicy = uploadPolicy;
        this.progressBar = progressBar;

        // ////////////////////////////////////////////////////////////////////////////
        // Let's read the up-to-date upload parameters
        // First: how many files can be read. The files that can't be read won't
        // be uploaded.
        int nbFilesToUpload = 0;
        for (int i = 0; i < filesDataParam.length; i += 1) {
            if (filesDataParam[i].canRead()) {
                nbFilesToUpload += 1;
            }
        }
        this.filesToUpload = new UploadFileData[nbFilesToUpload];

        // Patch to manage files that can't be read: we don't want them in
        // filesToUpload!
        // So let's add to the filesToUpload array, only the files to upload.
        // The use of a Vector instead of an array for filesToUpload would be
        // easier here. But all the implementation of the whole upload code is
        // based on filesToUpload being an array.
        // TODO display a proper message to the user, when a file can't be read.
        // Currently, a warning is written in the applet output. But it won't be
        // visible if the log window is hidden. In this case, the user must see
        // the 'readable' column in the applet, and see that it's not checked.
        // Not really intuitive!
        int iFileIndex = 0;
        for (int i = 0; i < filesDataParam.length; i += 1) {
            if (filesDataParam[i].canRead()) {
                this.filesToUpload[iFileIndex++] = new UploadFileData(
                        filesDataParam[i], this, uploadPolicy);
            } else {
                uploadPolicy.displayWarn(filesDataParam[i].getFileName()
                        + " is read only: it won't be uploaded.");
            }
        }

        // We don't store any additional parameters here: their value can be
        // updated by the real class that'll be used. See FileUploadThreadFTP
        // constructor of instance.
        // Upload parameters are read in the run() method, below.
    }

    /**
     * @see wjhk.jupload2.upload.FileUploadThread#getUploadedLength()
     */
    public long getUploadedLength() {
        return this.uploadedLength;
    }

    /**
     * @see wjhk.jupload2.upload.FileUploadThread#getTotalLength()
     */
    public long getTotalLength() {
        return this.totalFilesLength;
    }

    /** @see FileUploadThread#stopUpload() */
    public void stopUpload() {
        this.stop = true;
    }

    /** @see FileUploadThread#isUploadStopped() */
    public boolean isUploadStopped() {
        return this.stop;
    }

    /**
     * Get the server Output.
     * 
     * @return The StringBuffer that contains the full server HTTP response.
     */
    public String getResponseMsg() {
        return this.responseMsg;
    }

    /**
     * Get the exception that occurs during upload.
     * 
     * @return The exception, or null if no exception were thrown.
     */
    public Exception getException() {
        return this.uploadException;
    }

    /**
     * Used by the UploadFileData.uploadFile(java.io.OutputStream, long)
     * for each uploaded buffer
     * 
     * @see wjhk.jupload2.upload.FileUploadThread#nbBytesUploaded(long)
     */
    public void nbBytesUploaded(long nbBytes) {
        this.uploadedLength += nbBytes;
    }

    /**
     * This method is called before the upload. It calls the
     * {@link FileData#beforeUpload()} method for all files to upload, and
     * prepares the progressBar bar (if any), with total number of bytes to
     * upload.
     */
    final private void beforeUpload() throws JUploadException {

        for (int i = 0; i < this.filesToUpload.length && !this.stop; i++) {
            if (null != this.progressBar) {
                this.progressBar.setValue(i * 100);
                this.progressBar.setString(String.format(this.uploadPolicy
                        .getString("preparingFile"), new Integer(i + 1),
                        new Integer(this.filesToUpload.length)));
            }
            this.filesToUpload[i].beforeUpload();
            // totalFilesLength is used to correctly displays the progressBar
            // bar.
            this.totalFilesLength += this.filesToUpload[i].getRemainingLength();
        }

        if (null != this.progressBar) {
            this.progressBar.setValue(0);
            // Setting the maximum value of the progress bar to the total length
            // of all files could easily overflow the int, so we use percent
            // units here.
            this.progressBar.setMaximum(100);
            this.progressBar.setString("");
        }
    }

    /**
     * This methods upload overhead for the file number indexFile in the
     * filesDataParam given to the constructor. For instance, in HTTP, the
     * upload contains a head and a tail for each files.
     * 
     * @param indexFile The index of the file in the filesDataParam array, whose
     *            addtional length is asked.
     * @return The additional number of bytes for this file.
     */
    abstract long getAdditionnalBytesForUpload(int indexFile)
            throws JUploadIOException;

    /**
     * This method is called before starting of each request. It can be used to
     * prepare any work, before starting the request. For instance, in HTTP, the
     * tail must be properly calculated, as the last one must be different from
     * the others.
     * 
     * @param firstFileToUploadParam
     * @param nbFilesToUploadParam
     */
    abstract void beforeRequest(int firstFileToUploadParam,
            int nbFilesToUploadParam) throws JUploadException;

    /**
     * This method is called for each upload request to the server. The number
     * of request to the server depends on: <DIR>
     * <LI>The total number of files to upload.
     * <LI>The value of the nbFilesPerRequest applet parameter.
     * <LI>The value of the maxChunkSize applet parameter. </DIR> The main
     * objective of this method is to open the connection to the server, where
     * the files to upload will be written. It should also send any header
     * necessary for this upload request. The {@link #getOutputStream()} methods
     * is then called to know where the uploaded files should be written. <BR>
     * Note: it's up to the class containing this method to internally manage
     * the connection.
     * 
     * @param contentLength The total number of bytes for the files (or the
     *            chunk) to upload in this query.
     * @param bChunkEnabled True if this upload is part of a file (can occurs
     *            only if the maxChunkSize applet parameter is set). False
     *            otherwise.
     * @param chunkPart The chunk number. Should be ignored if bChunkEnabled is
     *            false.
     * @param bLastChunk True if in chunk mode, and this upload is the last one.
     *            Should be ignored if bChunkEnabled is false.
     */
    abstract void startRequest(long contentLength, boolean bChunkEnabled,
            int chunkPart, boolean bLastChunk) throws JUploadException;

    /**
     * This method is called at the end of each request.
     * 
     * @return The response status code from the server (200 == OK)
     * @see #startRequest(long, boolean, int, boolean)
     */
    abstract int finishRequest() throws JUploadException;

    /**
     * This method is called before sending the bytes corresponding to the file
     * whose index is given in argument. If the file is splitted in chunks (see
     * the maxChunkSize applet parameter), this method is called before each
     * chunk for this file.
     * 
     * @param index The index of the file that will be sent just after
     */
    abstract void beforeFile(int index) throws JUploadException;

    /**
     * Idem as {@link #beforeFile(int)}, but is called after each file (and
     * each chunks for each file).
     * 
     * @param index The index of the file that was just sent.
     */
    abstract void afterFile(int index) throws JUploadException;

    /**
     * Clean any used resource of the last executed request. In HTTP mode, the
     * output stream, input stream and the socket should be cleaned here.
     */
    abstract void cleanRequest() throws JUploadException;

    /**
     * Clean any used resource, like a 'permanent' connection. This method is
     * called after the end of the last request (see on the top of this page for
     * details).
     */
    abstract void cleanAll() throws JUploadException;

    /**
     * Get the output stream where the files should be written for upload.
     * 
     * @return The target output stream for upload.
     */
    abstract OutputStream getOutputStream() throws JUploadException;

    /**
     * Return the the body for the server response. That is: the server response
     * without the http header. This the real functionnal response from the
     * server application, that would be outputed, for instance, by any 'echo'
     * PHP command.
     */
    abstract String getResponseBody();

    /**
     * Add a String that has been read from the server response.
     * 
     * @param msg The server message to be set.
     */
    void setResponseMsg(String msg) {
        this.responseMsg = msg;
    }

    // ////////////////////////////////////////////////////////////////////////////////////
    // /////////////////////// PRIVATE FUNCTIONS
    // /////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retrieve the start time of this thread.
     * 
     * @return The time this thread was started in ms.
     */
    public final long getStartTime() {
        return this.startTime;
    }

    /**
     * The heart of the program. This method prepare the upload, then calls
     * doUpload for each HTTP request.
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    final public void run() {
        boolean bUploadOk = true;

        // Let's read up to date upload parameters.
        // These parameters may be changed by the subclasses parameter of this
        // class: so we read them as late as possible, that is: here!
        this.maxChunkSize = uploadPolicy.getMaxChunkSize();
        this.nbMaxFilesPerUpload = uploadPolicy.getNbFilesPerRequest();

        // this inhibits status-update (progress bar and status bar)
        // from within the timer loop.
        this.startTime = 0;

        this.uploadedLength = 0;
        this.totalFilesLength = 0;

        try {
            if (null != this.progressBar) {
                this.progressBar.setValue(0);
                // We allow percentage to be change, for each uploaded file.
                // This allows to show how picture preparation is progressing to
                // the user.
                this.progressBar.setMaximum(this.filesToUpload.length * 100);
            }

            // Prepare upload, for all files to be uploaded.
            beforeUpload();
            this.startTime = System.currentTimeMillis();
            beforeRequest(0, this.filesToUpload.length);

            // Let's go through all files.
            int iFirstFileForThisUpload = 0;
            int iNbFilesForThisUpload = 0;
            int currentFile = 0;
            long nextUploadContentLength = 0; // The contentLength of file
            // managed byt the current loop.
            long currentUploadContentLength = 0;// The current contentLength of
            // files between
            // iFirstFileForThisUpload and
            // iNbFilesForThisUpload

            // ////////////////////////////////////////////////////////////////////////////////////////
            // We upload files, according to the current upload policy.
            while (iFirstFileForThisUpload + iNbFilesForThisUpload < this.filesToUpload.length
                    && bUploadOk && !this.stop) {
                currentFile = iFirstFileForThisUpload + iNbFilesForThisUpload;
                // Calculate the size of this file upload
                nextUploadContentLength = this.filesToUpload[currentFile]
                        .getRemainingLength()
                        + getAdditionnalBytesForUpload(currentFile);

                // If we already had one or more files to upload, and the new
                // upload content length is more
                // the the maxChunkSize, we upload what we already have to.
                if (iNbFilesForThisUpload > 0
                        && currentUploadContentLength + nextUploadContentLength > this.maxChunkSize
                        && !this.stop) {
                    // Let's do an upload.
                    bUploadOk = doUpload(iFirstFileForThisUpload,
                            iNbFilesForThisUpload);
                    iFirstFileForThisUpload += iNbFilesForThisUpload;
                    iNbFilesForThisUpload = 0;
                    currentUploadContentLength = 0;
                }

                // Let's add the current file to the list of files for the next
                // upload.
                currentUploadContentLength += nextUploadContentLength;
                iNbFilesForThisUpload += 1;

                // If the current file is bigger than the maxChunkSize:
                // a) We did an upload in the previous 'if'.
                // b) We upload this file alone, and it will use chunks (see
                // doUpload).
                if (currentUploadContentLength > this.maxChunkSize
                        && !this.stop) {
                    // Let's do an upload.
                    bUploadOk = doUpload(iFirstFileForThisUpload,
                            iNbFilesForThisUpload);
                    iFirstFileForThisUpload += iNbFilesForThisUpload;
                    iNbFilesForThisUpload = 0;
                    currentUploadContentLength = 0;
                }
                // Do we attain the maximum number of files in one upload ?
                if (iNbFilesForThisUpload == this.nbMaxFilesPerUpload
                        && !this.stop) {
                    // Let's do an upload.
                    bUploadOk = doUpload(iFirstFileForThisUpload,
                            iNbFilesForThisUpload);
                    iFirstFileForThisUpload += iNbFilesForThisUpload;
                    iNbFilesForThisUpload = 0;
                    currentUploadContentLength = 0;
                }
            }// while

            if (iNbFilesForThisUpload > 0 && bUploadOk && !this.stop) {
                // Some files are still to upload. Let's finish the job.
                bUploadOk = doUpload(iFirstFileForThisUpload,
                        iNbFilesForThisUpload);
            }

            // Let's show everything is Ok
            if (null != this.progressBar) {
                if (bUploadOk || this.stop) {
                    if (!this.stop)
                        this.progressBar.setString(String.format(
                                this.uploadPolicy.getString("nbUploadedFiles"),
                                new Integer(iFirstFileForThisUpload
                                        + iNbFilesForThisUpload)));
                    else
                        this.progressBar.setString(String.format(
                                this.uploadPolicy.getString("infoAborted"),
                                new Integer(iFirstFileForThisUpload - 1)));
                } else {
                    this.progressBar.setString(this.uploadPolicy
                            .getString("errDuringUpload"));
                }
            }
        } catch (JUploadException e) {
            bUploadOk = false;
            this.uploadException = e;
            this.uploadPolicy.displayErr(e);
            this.progressBar.setString(e.getMessage());
        } finally {
            // In all cases, we try to free all reserved resources.
            this.uploadPolicy.displayDebug(
                    "FileUploadThread: within run().finally", 70);
            try {
                UploadFileData f;
                for (int i = 0; i < this.filesToUpload.length; i++) {
                    f = this.filesToUpload[i];
                    if (f != null) {
                        f.afterUpload();
                    }
                }
            } catch (Exception e) {
                this.uploadPolicy.displayWarn(e.getClass().getName() + " in "
                        + getClass().getName() + ".run() (finally)");
            }
        }

        // If the upload was unsuccessful, we try to alert the webmaster.
        if (!bUploadOk && !this.stop) {
            this.uploadPolicy.sendDebugInformation("Error in Upload");
        }

        // Enf of thread.

    }// run

    /**
     * Actual execution file upload. It's called by the run methods, once for
     * all files, or file by file, depending on the UploadPolicy. <BR>
     * This method is called by the run() method. The prerequisite are : <DIR>
     * <LI>If the contentLength for the nbFilesToUploadParam is more than the
     * maxChunkSize, then nbFilesToUploadParam is one.
     * <LI>nbFilesToUploadParam is less (or equal) than the
     * nbMaxFilesPerUpload. </DIR>
     * 
     * @param firstFileToUploadParam The index of the first file to upload, in
     *            the {@link #filesToUpload} area.
     * @param nbFilesToUploadParam Number of file to upload, in the next HTTP
     *            upload request. These files are taken from the
     *            {@link #filesToUpload} area
     */
    final private boolean doUpload(int firstFileToUploadParam,
            int nbFilesToUploadParam) {
        boolean bReturn = true;
        boolean bLastChunk = false;
        boolean bChunkEnabled = false;
        int chunkPart = 0;
        int nbFilesToUpload = 0;
        long totalContentLength = 0;
        long totalFileLength = 0;
        long contentLength = 0;
        long thisChunkSize = 0;
        int firstFileToUpload = 0;
        String msg;

        if (nbFilesToUploadParam == 1) {
            msg = (firstFileToUploadParam + 1) + "/"
                    + (this.filesToUpload.length);
        } else {
            msg = (firstFileToUploadParam + 1) + "-"
                    + (firstFileToUploadParam + nbFilesToUploadParam) + "/"
                    + (this.filesToUpload.length);
        }

        if (!this.stop && (null != this.progressBar)) {
            this.progressBar.setString(String.format(this.uploadPolicy
                    .getString("infoUploading"), msg));
        }

        // Let's be optimistic: we calculate the total upload length. Then,
        // we'll test that this is less that the
        // maximum chunk size ... if any is defined.
        try {
            // Prepare upload, for all files to be uploaded.
            // We have to do it again, in case we don't upload all files at
            // once. In HTTP header the last tail
            // is different from the other one.
            beforeRequest(firstFileToUploadParam, nbFilesToUploadParam);

            for (int i = 0; i < nbFilesToUploadParam && !this.stop; i++) {
                // Total length, for HTTP upload.
                totalContentLength += this.filesToUpload[firstFileToUploadParam
                        + i].getUploadLength();
                totalContentLength += getAdditionnalBytesForUpload(firstFileToUploadParam
                        + i);
                // Total file length: used to manage the progess bar (we don't
                // follow the bytes uploaded within headers and forms).
                totalFileLength += this.filesToUpload[firstFileToUploadParam
                        + i].getUploadLength();

                this.uploadPolicy.displayDebug("file "
                        + (firstFileToUploadParam + i)
                        + ": content="
                        + this.filesToUpload[firstFileToUploadParam + i]
                                .getUploadLength()
                        + " bytes, getAdditionnalBytesForUpload="
                        + getAdditionnalBytesForUpload(firstFileToUploadParam
                                + i) + " bytes", 80);
            }// for
        } catch (JUploadException e) {
            this.uploadPolicy.displayErr(e);
            this.uploadException = e;
        }

        // Ok, now we check that the totalContentLength is less than the chunk
        // size.
        if (totalFileLength >= this.maxChunkSize) {
            // hum, hum, we have to download file by file, with chunk enabled.
            // This a prerequisite of this method.
            if (nbFilesToUploadParam > 1) {
                this.uploadException = new JUploadException(
                        "totalContentLength >= chunkSize: nbFilesToUploadParam should be more than 1 (doUpload)");
            }
            bChunkEnabled = true;
        }

        // This while enables the chunk management:
        // In chunk mode, it loops until the last chunk is uploaded. This works
        // only because, in chunk mode,
        // files are uploaded one y one (the for loop within the while loops
        // through ... 1 unique file).
        // In normal mode, it does nothing, as the bLastChunk is set to true in
        // the first test, within the while.
        while (!bLastChunk && this.uploadException == null && !this.stop) {
            try {
                // First: chunk management.
                if (bChunkEnabled) {
                    // Let's manage chunk:
                    // Files are uploaded one by one. This is checked just
                    // above.
                    firstFileToUpload = firstFileToUploadParam;
                    nbFilesToUpload = 1;
                    chunkPart += 1;
                    bLastChunk = (contentLength > this.filesToUpload[firstFileToUploadParam]
                            .getRemainingLength());

                    // Is this the last chunk ?
                    if (bLastChunk) {
                        thisChunkSize = this.filesToUpload[firstFileToUploadParam]
                                .getRemainingLength();
                    } else {
                        thisChunkSize = this.maxChunkSize;
                    }
                    contentLength = thisChunkSize
                            + getAdditionnalBytesForUpload(firstFileToUploadParam);
                } else {
                    // Chunk not activate. We upload all files at once.
                    bLastChunk = true;
                    contentLength = totalContentLength;
                    firstFileToUpload = firstFileToUploadParam;
                    nbFilesToUpload = nbFilesToUploadParam;
                }

                // Ok, we've prepare the job for chunk upload. Let's do it!
                startRequest(contentLength, bChunkEnabled, chunkPart,
                        bLastChunk);

                for (int i = 0; i < nbFilesToUpload && !this.stop; i++) {
                    // Write to Server the head(4 Lines), a File and the tail.

                    // Let's add any file-specific header.
                    beforeFile(firstFileToUpload + i);

                    // In chunk mode, we already calculate the correct
                    // chunkSize.
                    if (!bChunkEnabled) {
                        // If not chunk mode, then the file is uploaded in one
                        // shot.
                        thisChunkSize = this.filesToUpload[firstFileToUpload
                                + i].getUploadLength();
                    }

                    // Actual upload of the file:
                    this.filesToUpload[firstFileToUpload + i].uploadFile(
                            getOutputStream(), thisChunkSize);

                    // If we are not in chunk mode, or if it was the last chunk,
                    // upload should be finished.
                    if (!bChunkEnabled || bLastChunk) {
                        if (!this.stop && (null != this.progressBar))
                            this.progressBar.setString(String
                                    .format(this.uploadPolicy
                                            .getString("infoUploaded"), msg));
                        if (this.filesToUpload[firstFileToUpload + i]
                                .getRemainingLength() > 0) {
                            this.uploadException = new JUploadExceptionUploadFailed(
                                    "Files has not be entirely uploaded. The remaining size is "
                                            + this.filesToUpload[firstFileToUpload
                                                    + i].getRemainingLength()
                                            + " bytes. File size was: "
                                            + this.filesToUpload[firstFileToUpload
                                                    + i].getUploadLength()
                                            + " bytes.");
                        }
                    }
                    // Let's add any file-specific header.
                    afterFile(firstFileToUpload + i);
                }
                getOutputStream().flush();

                // Let's finish the request, and wait for the server Output, if
                // any (not applicable in FTP)
                int status = finishRequest();

                // We now ask to the uploadPolicy, if it was a success.
                // If not, the isUploadSuccessful should raise an exception.
                if (!this.stop)
                    this.uploadPolicy.checkUploadSuccess(status,
                            getResponseMsg(), getResponseBody());

            } catch (Exception e) {
                this.uploadException = e;
                bReturn = false;
                /*
                 * The error will be managed by the main thread. We just store
                 * it, for now. this.uploadPolicy.displayErr(this.uploadPolicy
                 * .getString("errDuringUpload"), e);
                 */
            }

            // Debug output: always called, so that the debug file is correctly
            // filled.
            this.uploadPolicy.displayDebug(
                    "-------- Response Body Start --------", 80);
            this.uploadPolicy.displayDebug(quoteCRLF(getResponseBody()), 80);
            this.uploadPolicy.displayDebug(
                    "--------- Response Body End ---------", 80);
        } // while(!bLastChunk && uploadException==null && !stop)

        try {
            cleanRequest();
        } catch (JUploadException e) {
            this.uploadException = e;
            bReturn = false;
        }

        if (this.uploadException == null) {
            // The upload was Ok, we remove the uploaded files from the
            // filePanel.
            for (int i = 0; i < nbFilesToUpload && !this.stop; i++) {
                this.uploadPolicy.getApplet().getUploadPanel().getFilePanel()
                        .remove(this.filesToUpload[firstFileToUpload + i]);
            }
        } else {
            this.uploadPolicy.displayErr(this.uploadException);
        }

        return bReturn;
    }

    /** @see FileUploadThread#close() */
    public void close() {
        try {
            cleanAll();
        } catch (JUploadException e) {
            this.uploadPolicy.displayErr(e);
        }
    }

    /**
     * Replace \r and \n by correctly displayed end of line characters. Used to display debug ouptut.
     * 
     * @param s The original string
     * @return The string with \r and \n modified, to be correctly displayed.
     */
    public final String quoteCRLF(String s) {
        return s.replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n\n");
    }
}
