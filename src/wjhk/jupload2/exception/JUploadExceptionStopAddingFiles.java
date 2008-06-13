//
// $Id$
//
// jupload - A file upload applet.
//
// Copyright 2008 The JUpload Team
//
// Created: 20 mars 08
// Creator: etienne_sf
// Last modified: $Date$
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

package wjhk.jupload2.exception;

import java.io.File;

import wjhk.jupload2.policies.UploadPolicy;

/**
 * 
 * This exception is a trick, to stop adding files to the file list, when too
 * many files don't match the allowed extension (see
 * <I>wjhk.jupload2.gui.FilePanelDataModel2#addFile(File, File)</I>), or any
 * other control of the current upload policy (see
 * {@link UploadPolicy#createFileData(File, File)}.
 * 
 * @author etienne_sf
 * 
 */
public class JUploadExceptionStopAddingFiles extends JUploadException {

    /**
     * 
     */
    private static final long serialVersionUID = 4395228400366722178L;

    /**
     * @param message
     */
    public JUploadExceptionStopAddingFiles(String message) {
        super(message);
        // No action.
    }

}
