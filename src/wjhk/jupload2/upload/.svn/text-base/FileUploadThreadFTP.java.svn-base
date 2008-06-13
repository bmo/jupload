//
// $Id: FileUploadThreadFTP.java 136 2007-05-12 20:15:36 +0000 (sam., 12 mai
// 2007) felfert $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// 
// Created: 2007-01-01
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

package wjhk.jupload2.upload;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.MissingResourceException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.exception.JUploadExceptionUploadFailed;
import wjhk.jupload2.filedata.FileData;
import wjhk.jupload2.policies.UploadPolicy;

/**
 * The FileUploadThreadFTP class is intended to extend the functionality of the
 * JUpload applet and allow it to handle ftp:// addresses. <br>
 * Note: this class is not a V4 of the FTP upload. It is named V4, as it
 * inherits from the {@link FileUploadThread} class. <br>
 * <br>
 * In order to use it, simply change the postURL argument to the applet to
 * contain the approperiate ftp:// link. The format is:
 * 
 * <pre>
 *         ftp://username:password@myhost.com:21/directory
 * </pre>
 * 
 * Where everything but the host is optional. There is another parameter that
 * can be passed to the applet named 'binary' which will set the file transfer
 * mode based on the value. The possible values here are 'true' or 'false'. It
 * was intended to be somewhat intelligent by looking at the file extension and
 * basing the transfer mode on that, however, it was never implemented. Feel
 * free to! Also, there is a 'passive' parameter which also has a value of
 * 'true' or 'false' which sets the connection type to either active or passive
 * mode.
 * 
 * @author Evin Callahan (inheritance from DefaultUploadThread built by etienne_sf)
 * @author Daystar Computer Services
 * @see FileUploadThread
 * @see DefaultFileUploadThread
 * @version 1.0, 01 Jan 2007 * Update march 2007, etienne_sf Adaptation to
 *          match all JUpload functions: <DIR>
 *          <LI>Inheritance from the {@link FileUploadThread} class,
 *          <LI>Use of the UploadFileData class,
 *          <LI>Before upload file preparation,
 *          <LI>Upload stop by the user.
 *          <LI> </DIR>
 */
public class FileUploadThreadFTP extends DefaultFileUploadThread {

    // ////////////////////////////////////////////////////////////////////////////////////
    // /////////////////////// PRIVATE ATTRIBUTES
    // ///////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////

    // ////////////////////////////////////////////////////////////////////////////////////
    // /////////////////////// PRIVATE ATTRIBUTES
    // ///////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////

    /**
     * The output stream, where the current file should be written. This output
     * stream should not be used. The buffered one is much faster.
     */
    private OutputStream ftpOutputStream = null;

    /**
     * The buffered stream, that the application should use for upload.
     */
    private BufferedOutputStream bufferedOutputStream = null;

    private Matcher uriMatch;

    // the client that does the actual connecting to the server
    private FTPClient ftp = new FTPClient();

    // info taken from the ftp string
    private String user;

    private String pass;

    private String host;

    private String port;

    private String dir;

    /**
     * Indicates whether the connexion to the FTP server is open or not. This
     * allows to connect once on the FTP server, for multiple file upload.
     */
    private boolean bConnected = false;

    /**
     * This pattern defines the groups and pattern of the ftp syntax.
     */
    public final Pattern ftpPattern = Pattern
            .compile("^ftp://(([^:]+):([^\\@]+)\\@)?([^/:]+):?([0-9]+)?(/(.*))?$");

    /**
     * Creates a new instance. Performs the connection to the server based on
     * the matcher created in the main.
     * @param filesDataParam 
     * @param uploadPolicy 
     * @param progress 
     * 
     * @throws JUploadException
     * @throws IllegalArgumentException if any error occurs. message is error
     */
    public FileUploadThreadFTP(FileData[] filesDataParam,
            UploadPolicy uploadPolicy, JProgressBar progress)
            throws JUploadException {
        super(filesDataParam, uploadPolicy, progress);

        // Some choherence checks, for parameter given to the applet.

        // stringUploadSuccess: unused in FTP mode. Must be null.
        if (uploadPolicy.getStringUploadSuccess() != null) {
            uploadPolicy
                    .displayWarn("FTP mode: stringUploadSuccess parameter ignored (forced to null)");
            uploadPolicy.setProperty(UploadPolicy.PROP_STRING_UPLOAD_SUCCESS,
                    null);
        }

        // nbFilesPerRequest: must be 1 in FTP mode.
        if (uploadPolicy.getNbFilesPerRequest() != 1) {
            uploadPolicy
                    .displayWarn("FTP mode: nbFilesPerRequest parameter ignored (forced to 1)");
            uploadPolicy.setProperty(UploadPolicy.PROP_NB_FILES_PER_REQUEST,
                    "1");
        }

        // maxChunkSize: must be unlimited (no chunk management in FTP mode).
        if (uploadPolicy.getMaxChunkSize() != Long.MAX_VALUE) {
            uploadPolicy
                    .displayWarn("FTP mode: maxChunkSize parameter ignored (forced to Long.MAX_VALUE)");
            uploadPolicy.setProperty(UploadPolicy.PROP_MAX_CHUNK_SIZE, Long
                    .toString(Long.MAX_VALUE));
        }
    }

    /** @see DefaultFileUploadThread#beforeRequest(int, int) */
    @Override
    void beforeRequest(@SuppressWarnings("unused")
    int firstFileToUploadParam, @SuppressWarnings("unused")
    int nbFilesToUploadParam) throws JUploadException {

        // If not already connected ... we connect to the server.
        if (!this.bConnected) {
            // Let's connect to the FTP server.
            String url = this.uploadPolicy.getPostURL();
            this.uriMatch = this.ftpPattern.matcher(url);
            if (!this.uriMatch.matches()) {
                throw new JUploadException("invalid URI: " + url);
            }
            this.user = this.uriMatch.group(2) == null ? "anonymous"
                    : this.uriMatch.group(2);
            this.pass = this.uriMatch.group(3) == null ? "JUpload"
                    : this.uriMatch.group(3);
            this.host = this.uriMatch.group(4); // no default server
            this.port = this.uriMatch.group(5) == null ? "21" : this.uriMatch
                    .group(5);
            this.dir = this.uriMatch.group(7) == null ? "/" : this.uriMatch
                    .group(7);

            // do connect.. any error will be thrown up the chain
            try {
                this.ftp.setDefaultPort(Integer.parseInt(this.port));
                this.ftp.connect(this.host);
                this.uploadPolicy.displayDebug("Connected to " + this.host, 3);
                this.uploadPolicy.displayDebug(this.ftp.getReplyString(), 20);

                if (!FTPReply.isPositiveCompletion(this.ftp.getReplyCode()))
                    throw new JUploadException("FTP server refused connection.");

                // given the login information, do the login
                this.ftp.login(this.user, this.pass);
                this.uploadPolicy.displayDebug(this.ftp.getReplyString(), 20);

                if (!FTPReply.isPositiveCompletion(this.ftp.getReplyCode()))
                    throw new JUploadException("Invalid username / password");

                this.ftp.changeWorkingDirectory(this.dir);
                this.uploadPolicy.displayDebug(this.ftp.getReplyString(), 20);

                if (!FTPReply.isPositiveCompletion(this.ftp.getReplyCode()))
                    throw new JUploadException("Invalid directory specified");

                this.bConnected = true;
            } catch (Exception e) {
                this.uploadException = e;
                throw new JUploadException("Could not connect to server ("
                        + e.getMessage() + ")");
            }
        } // if(!bConnected)
    }

    /** @see DefaultFileUploadThread#afterFile(int) */
    @Override
    void afterFile(@SuppressWarnings("unused")
    int index) {
        // Nothing to do
    }

    /** @see DefaultFileUploadThread#beforeFile(int) */
    @Override
    void beforeFile(int index) throws JUploadException {
        try {
            setTransferType(index);
            // just in case, delete anything that exists

            // No delete, as the user may not have the right for that. We use,
            // later, the store command:
            // If the file already exists, it will be replaced.
            // ftp.deleteFile(filesToUpload[index].getFileName());

            // Let's open the stream for this file.
            this.ftpOutputStream = this.ftp
                    .storeFileStream(this.filesToUpload[index].getFileName());
            // The upload is done through a BufferedOutputStream. This speed up
            // the upload in an unbelivable way ...
            this.bufferedOutputStream = new BufferedOutputStream(
                    this.ftpOutputStream);
        } catch (IOException e) {
            throw new JUploadException(e);
        }
    }

    /** @see DefaultFileUploadThread#cleanAll() */
    @Override
    void cleanAll() {
        try {
            if (this.ftp.isConnected()) {
                this.ftp.disconnect();
                this.uploadPolicy.displayDebug("disconnected", 20);
            }
        } catch (IOException e) {
            // then we arent connected
            this.uploadPolicy.displayDebug("Not connected", 20);
        } finally {
            this.ftpOutputStream = null;
            this.bufferedOutputStream = null;
        }
    }

    /** @see DefaultFileUploadThread#cleanRequest() */
    @Override
    void cleanRequest() throws JUploadException {
        if (this.bufferedOutputStream != null) {
            try {
                this.bufferedOutputStream.close();
                this.ftpOutputStream.close();
                if (!this.ftp.completePendingCommand()) {
                    throw new JUploadExceptionUploadFailed(
                            "ftp.completePendingCommand() returned false");
                }
            } catch (IOException e) {
                throw new JUploadException(e);
            } finally {
                this.bufferedOutputStream = null;
            }
        }
    }

    /** @see DefaultFileUploadThread#finishRequest() */
    @Override
    int finishRequest() {
        return 200;
        // Nothing to do
    }

    /** @see DefaultFileUploadThread#getAdditionnalBytesForUpload(int) */
    @Override
    long getAdditionnalBytesForUpload(@SuppressWarnings("unused")
    int indexFile) {
        // Default: no additional byte.
        return 0;
    }

    /** @see DefaultFileUploadThread#getResponseBody() */
    @Override
    String getResponseBody() {
        return "";
    }

    /** @see DefaultFileUploadThread#getOutputStream() */
    @Override
    OutputStream getOutputStream() {
        return this.bufferedOutputStream;
    }

    /** @see DefaultFileUploadThread#startRequest(long, boolean, int, boolean) */
    @Override
    void startRequest(@SuppressWarnings("unused")
    long contentLength, @SuppressWarnings("unused")
    boolean bChunkEnabled, @SuppressWarnings("unused")
    int chunkPart, @SuppressWarnings("unused")
    boolean bLastChunk) {
        // Nothing to do
    }

    /**
     * Will set the binary/ascii value based on the parameters to the applet.
     * This could be done by file extension too but it is not implemented.
     * 
     * @param index The index of the file that we want to upload, in the array
     *            of files to upload.
     * @throws IOException if an error occurs while setting mode data
     */
    private void setTransferType(@SuppressWarnings("unused")
    int index) throws IOException {
        // FileData file
        try {
            String binVal = this.uploadPolicy.getString("binary");

            // read the value given from the user
            if (Boolean.getBoolean(binVal))
                this.ftp.setFileType(FTP.BINARY_FILE_TYPE);
            else
                this.ftp.setFileType(FTP.ASCII_FILE_TYPE);

        } catch (MissingResourceException e) {
            // should set based on extension (not implemented)
            this.ftp.setFileType(FTP.BINARY_FILE_TYPE);
        }

        // now do the same for the passive/active parameter
        try {
            String pasVal = this.uploadPolicy.getString("passive");

            if (Boolean.getBoolean(pasVal)) {
                this.ftp.enterRemotePassiveMode();
                this.ftp.enterLocalPassiveMode();
            } else {
                this.ftp.enterLocalActiveMode();
                this.ftp.enterRemoteActiveMode(
                        InetAddress.getByName(this.host), Integer
                                .parseInt(this.port));
            }
        } catch (MissingResourceException e) {
            this.ftp.enterRemotePassiveMode();
            this.ftp.enterLocalPassiveMode();
        }
    }
}
