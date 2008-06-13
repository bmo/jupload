//
// $Id: JUploadPanel.java 205 2007-05-28 20:24:01 +0000 (lun., 28 mai 2007)
// felfert $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// 
// Created: ?
// Creator: etienne_sf
// Last modified: $Date: 2007-05-28 20:24:01 +0000 (lun., 28 mai 2007) $
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

package wjhk.jupload2.gui;

import javax.swing.JFileChooser;

import wjhk.jupload2.policies.UploadPolicy;

/**
 * This class allows easy personalization of the java file chooser. It asks the
 * current upload policy for all current configuration parameters. It is created
 * by the {@link JUploadPanel} class.
 */
public class JUploadFileChooser extends JFileChooser {

    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    // /////////////////////////////// Attributes
    // /////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 
     */
    private static final long serialVersionUID = 6829111419069956687L;

    /** The current upload policy */
    private UploadPolicy uploadPolicy = null;

    private JUploadFileFilter fileFilter = null;

    /** This file view add picture management capabilities to the file chooser */
    private JUploadFileView fileView = null;

    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    // /////////////////////////////// Methods
    // /////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The 'standard' constructor for our file chooser
     * 
     * @param uploadPolicyParam
     */
    public JUploadFileChooser(UploadPolicy uploadPolicyParam) {
        this.uploadPolicy = uploadPolicyParam;

        this.fileFilter = new JUploadFileFilter(this.uploadPolicy);
        this.fileView = new JUploadFileView(this.uploadPolicy, this);

        // XXX:
        // This breaks usability. probably use a persistent value of a
        // cookie later.
        // this.fileChooser.setCurrentDirectory(new File(System
        // .getProperty("user.dir")));
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setMultiSelectionEnabled(true);
        // The file view must be set, whether or not a file filter exists
        // for this upload policy.
        setFileView(this.fileView);
        if (this.uploadPolicy.fileFilterGetDescription() != null) {
            setFileFilter(this.fileFilter);
            // If a file filter has been given to the applet, only these file
            // should be allowed.
            setAcceptAllFileFilterUsed(false);
        }
    }

    /**
     * Shutdown any running task. Currently, only the JUploadFileView may have
     * running tasks, when calculating icon for picture files.
     */
    // TODO remove this method: it should be triggered by itself, when the file
    // chooser is closed.
    public void shutdownNow() {
        this.fileView.shutdownNow();
    }

}
