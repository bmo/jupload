//
// $Id$
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

/**
 * This interface defines the methods of the various FileUploadThread classes.
 * These classes are kept in the CVS, as people often update them for their
 * needs: I don't want to remove them, when I do a 'big bang' within them.
 * Created on 21 nov. 06
 */
public interface FileUploadThread {

    /**
     * Stopping the Thread
     */
    public void stopUpload();

    /**
     * Returns true if someone asks the thread to stop.
     * @return true if the upload has been requested to stop.
     * 
     * @see #stopUpload()
     */
    public boolean isUploadStopped();

    /**
     * Get the server response message.
     * 
     * @return The String that contains the HTTP response message (e.g. "200
     *         OK")
     */
    public String getResponseMsg();

    /**
     * Get the exception that occurs during upload.
     * 
     * @return The exception, or null if no exception were thrown.
     */
    public Exception getException();

    /**
     * Indicate to the UploadThread that nbBytes bytes have been uploaded to the
     * server. It's up to this method to change the display on the progress bar
     * (or whatever other information displayed to the user)
     * 
     * @param nbBytes Number of bytes uploaded.
     */
    public void nbBytesUploaded(long nbBytes);

    /**
     * @return The current number of bytes, already uploaded in this thread.
     */
    public long getUploadedLength();

    /**
     * @return The total number of bytes, to be uploaded in this thread.
     */
    public long getTotalLength();

    /**
     * Closes the connection to the server and releases resources.
     */
    public void close();

    /**
     * @return The start time stamp of this instance.
     */
    public long getStartTime();

    /**
     * @return true if the thread is currently working.
     * @see java.lang.Thread#isAlive()
     */
    public boolean isAlive();

    /**
     * @throws InterruptedException 
     * @see java.lang.Thread#join()
     */
    public void join() throws InterruptedException;

    /**
     * @param millisec 
     * @throws InterruptedException 
     * @see java.lang.Thread#join(long)
     */
    public void join(long millisec) throws InterruptedException;

    /**
     * @see java.lang.Thread#start()
     */
    public void start();

}
