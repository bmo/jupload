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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import wjhk.jupload2.exception.JUploadExceptionStopAddingFiles;
import wjhk.jupload2.policies.UploadPolicy;

/**
 * Our implementation of DND.
 * 
 * @author William JinHua Kwong
 * @version $Release$
 */
public class DnDListener implements DropTargetListener {

    private JUploadPanel uploadPanel;

    private UploadPolicy uploadPolicy;

    /**
     * Creates a new instance.
     * 
     * @param uploadPanel The corresponding upload panel.
     * @param uploadPolicy 
     */
    public DnDListener(JUploadPanel uploadPanel, UploadPolicy uploadPolicy) {
        this.uploadPanel = uploadPanel;
        this.uploadPolicy = uploadPolicy;
    }

    /**
     * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
     */
    public void dragEnter(DropTargetDragEvent e) {
        if (!e.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            e.rejectDrag();
        }
    }

    /**
     * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
     */
    public void dragOver(@SuppressWarnings("unused")
    DropTargetDragEvent e) {
        // Nothing to do.
    }

    /**
     * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
     */
    public void dropActionChanged(@SuppressWarnings("unused")
    DropTargetDragEvent e) {
        // Nothing to do.
    }

    /**
     * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
     */
    public void drop(DropTargetDropEvent e) {
        if (!e.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            e.rejectDrop();
        } else {
            e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
            try {
                List<?> fileList = (List<?>) e.getTransferable()
                        .getTransferData(DataFlavor.javaFileListFlavor);
                // this.uploadPanel.addFiles((File[]) fileList.toArray(), null);
                Iterator<?> i = fileList.iterator();
                try {
                    while (i.hasNext()) {
                        File[] f = {
                            (File) i.next()
                        };
                        if (f[0].isDirectory()) {
                            this.uploadPanel.addFiles(f, f[0].getParentFile());
                        } else {
                            this.uploadPanel.addFiles(f, null);
                        }
                    }
                } catch (JUploadExceptionStopAddingFiles e2) {
                    // The user want to stop here. Nothing else to do.
                    uploadPolicy.displayWarn(getClass().getName() + ".drop() ["
                            + e.getClass().getName() + "]: " + e2.getMessage());
                }
                e.getDropTargetContext().dropComplete(true);

                // Let's communicate this to the upload policy: there may be
                // something to do now.
                uploadPolicy.afterFileDropped(e);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                e.rejectDrop();
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
                e.rejectDrop();
            }
        }
    }

    /**
     * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
     */
    public void dragExit(@SuppressWarnings("unused")
    DropTargetEvent e) {
        // Nothing to do.
    }
}
