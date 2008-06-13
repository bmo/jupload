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

package wjhk.jupload2.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import wjhk.jupload2.policies.UploadPolicy;

/**
 * Default File Filter used by the {@link wjhk.jupload2.policies.DefaultUploadPolicy} to filter the
 * allowed file in the JFileChooser. This class is an empty one: it just calls
 * the {
 */
public class JUploadFileFilter extends FileFilter {

    UploadPolicy uploadPolicy = null;

    JUploadFileFilter(UploadPolicy uploadPolicy) {
        this.uploadPolicy = uploadPolicy;
    }

    /**
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(File file) {
        return this.uploadPolicy.fileFilterAccept(file);
    }

    /**
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    @Override
    public String getDescription() {
        return this.uploadPolicy.fileFilterGetDescription();
    }

}
