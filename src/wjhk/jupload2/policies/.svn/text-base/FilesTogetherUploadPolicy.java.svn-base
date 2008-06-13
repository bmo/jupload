//
// $Id$
//
// jupload - A file upload applet.
//
// Copyright 2007 The JUpload Team
//
// Created: 10 oct. 07
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

package wjhk.jupload2.policies;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.dnd.DropTargetDropEvent;

import wjhk.jupload2.JUploadApplet;
import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.gui.JUploadPanel;

/**
 * 
 * Upload policy that is a simple box. It's a 'simple' drop target. Upload
 * starts immediately, when files are dropped on the applet. <BR>
 * The only component displayed on the applet is the progress bar.
 * 
 * @author etienne_sf
 * 
 */
public class FilesTogetherUploadPolicy extends DefaultUploadPolicy {

    /**
     * The JUpload constructor for this upload policy. Like all upload policies,
     * this constructor is called by the {@link UploadPolicyFactory}
     * 
     * @param theApplet
     * @throws JUploadException
     */
    public FilesTogetherUploadPolicy(JUploadApplet theApplet)
            throws JUploadException {
        super(theApplet);
    }

    /**
     * This methods allow the upload policy to override the default disposition
     * of the components on the applet.
     * 
     * @see UploadPolicy#addComponentsToJUploadPanel(JUploadPanel)
     */

    public void addComponentsToJUploadPanel(JUploadPanel jUploadPanel) {
        // Set the global layout of the panel.
        jUploadPanel.setLayout(new GridLayout(1, 1));
        jUploadPanel.setLayout(new BorderLayout());
        // Then, add on the screen of the only component that is visible.
        jUploadPanel.add(jUploadPanel.getProgressBar(), BorderLayout.CENTER);
        // Now, we add the log window.
        jUploadPanel.showOrHideLogWindow();
        jUploadPanel.add(jUploadPanel.getJLogWindowPane(), BorderLayout.SOUTH);
    }

    /**
     * Default reaction after a successful drop operation: no action.
     * 
     * @see UploadPolicy#afterFileDropped(DropTargetDropEvent)
     */

    public void afterFileDropped(DropTargetDropEvent dropEvent) {
        getApplet().getUploadPanel().doStartUpload();
    }

}
