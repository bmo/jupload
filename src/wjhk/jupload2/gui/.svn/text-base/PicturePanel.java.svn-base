//
// $Id: PicturePanel.java 95 2007-05-02 03:27:05 +0000 (mer., 02 mai 2007)
// /C=DE/ST=Baden-Wuerttemberg/O=ISDN4Linux/OU=Fritz
// Elfert/CN=svn-felfert@isdn4linux.de/emailAddress=fritz@fritz-elfert.de $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// Copyright 2002 Guillaume Chamberland-Larose
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

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;

import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.filedata.PictureFileData;
import wjhk.jupload2.policies.UploadPolicy;

/**
 * This panel is used to preview picture, when PictureUploadPolicy (or one of
 * its inherited policy) is used. Manages the panel where pictures are
 * displayed. <BR>
 * Each time a user selects a file in the panel file, the PictureUploadPolicy
 * calls
 * {@link #setPictureFile(PictureFileData, AbstractButton, AbstractButton)}. I
 * did an attempt to store the Image generated for the Panel size into the
 * PictureFileData, to avoid to calculate the offscreenPicture each time the
 * user select the same file again. But it doesn't work: the applet quickly runs
 * out of memory, even after numerous calls of System.gc and finalize. <BR>
 * <BR>
 * This file is taken from the PictureApplet ((C) 2002 Guillaume
 * Chamberland-Larose), available here: To contact Guillaume Chamberland-Larose
 * for bugs, patches, suggestions: Please use the forums on the sourceforge web
 * page for this project, located at:
 * http://sourceforge.net/projects/picture-applet/ Updated : 2006 etienne_sf<BR>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. <BR>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. <BR>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

public class PicturePanel extends Canvas implements MouseListener,
        ComponentListener {

    /**
     * 
     */
    private static final long serialVersionUID = -3439340009940699981L;

    private PictureFileData pictureFileData;

    /**
     * offscreenImage contains an image, that can be asked by
     * {@link PictureFileData#getImage(Canvas, boolean)}. It is used to preview
     * this picture.
     */
    private Image offscreenImage = null;

    /**
     * Indicates if the offscreen image should be calculated once and stored, to
     * avoid to calculate it again. <BR>
     * Indications: the offscreen image should be calculate only once for the
     * picturePanel on the applet, and for each display when the user ask to
     * display the fulscreen picture (by a click on the picturePanel).
     */
    private boolean hasToStoreOffscreenPicture = false;

    /**
     * The current upload policy.
     */
    protected UploadPolicy uploadPolicy;

    /**
     * Standard constructor.
     * 
     * @param hasToStoreOffscreenPicture
     * @param uploadPolicy The current upload policy
     */
    public PicturePanel(boolean hasToStoreOffscreenPicture,
            UploadPolicy uploadPolicy) {
        super();

        this.hasToStoreOffscreenPicture = hasToStoreOffscreenPicture;
        this.uploadPolicy = uploadPolicy;

        // We want to trap the mouse actions on this picture.
        addMouseListener(this);

        // We want to know when a resize event occurs (to recalculate
        // offscreenImage)
        addComponentListener(this);
    }

    /**
     * This setter is called by {@link PictureFileData} to set the picture that
     * is to be previewed.
     * 
     * @param pictureFileData The FileData for the image to be displayed. Null
     *            if no picture should be displayed.
     * @param button1 A button that will be activated or not, depending of the
     *            pictures was correctly set into the panel. May be null, if not
     *            button is to be enabled.
     * @param button2 Another button that will be activated or not. May also be
     *            null.
     */
    public void setPictureFile(PictureFileData pictureFileData,
            AbstractButton button1, AbstractButton button2) {
        // First : reset current picture configuration.
        this.pictureFileData = null;
        if (this.offscreenImage != null) {
            this.offscreenImage.flush();
            this.offscreenImage = null;
        }

        // Ask for an immediate repaint, to clear the panel (as offscreenImage
        // is now null).
        repaint(0);

        // Then, we store the new picture data, get the offscreen picture and
        // ask for a repaint.
        boolean enableButton = false;
        if (pictureFileData != null && pictureFileData.canRead()) {
            this.pictureFileData = pictureFileData;

            // A picture has been selected. The buttons must be enabled.
            enableButton = true;

            // Now, we display this picture.
            calculateOffscreenImage();
            if (this.offscreenImage == null) {
                this.uploadPolicy
                        .displayDebug(
                                "PicturePanel.setPictureFile(): offscreenImage is null",
                                1);
            }
            repaint();
        }

        // Let's activate the given button ... if any.
        if (button1 != null) {
            button1.setEnabled(enableButton);
        }
        if (button2 != null) {
            button2.setEnabled(enableButton);
        }
    }

    /**
     * @see java.awt.Canvas#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        // First : clear the panel area.
        g.clearRect(0, 0, getWidth(), getHeight());

        /*
         * The picture is calculated outside of the paint() event. See:
         * calculateOffscreenImage() and componentResized. //Then, check if we
         * must calculate the picture. if (pictureFileData != null) { //Now, we
         * calculate the picture if we don't already have one. if
         * (offscreenImage == null) { calculateOffscreenImage(); } }
         */

        // Then, display the picture, if any is defined.
        if (this.offscreenImage != null) {
            /*
             * uploadPolicy.displayDebug("PicturePanel.paint(): Non null
             * offscreenImage (image : w=" + offscreenImage.getWidth(this) + ",
             * h=" + offscreenImage.getHeight(this) + ", panel: w=" + getWidth() + ",
             * h=" + getHeight() , 20 );
             */
            // Let's center this picture
            int hMargin = (getWidth() - this.offscreenImage.getWidth(this)) / 2;
            int vMargin = (getHeight() - this.offscreenImage.getHeight(this)) / 2;
            g.drawImage(this.offscreenImage, hMargin, vMargin, this);
            // Free the used memory.
            this.offscreenImage.flush();
        } else {
            this.uploadPolicy.displayDebug(
                    "PicturePanel.paint(): offscreenImage is null", 40);
        }
    }

    /**
     * This function adds a quarter rotation to the current picture.
     * 
     * @param quarter Number of quarters (90�) the picture should rotate. 1
     *            means rotating of 90� clockwise (?). Can be negative
     *            (counterclockwise), more than 1...
     */
    public void rotate(int quarter) {
        if (this.pictureFileData != null) {
            Cursor previousCursor = uploadPolicy.setWaitCursor();
            this.pictureFileData.addRotation(quarter);
            // The previously calculated picture is now wrong.
            this.offscreenImage.flush();
            this.offscreenImage = null;
            calculateOffscreenImage();

            repaint();
            uploadPolicy.setCursor(previousCursor);
        } else {
            this.uploadPolicy
                    .displayWarn("Hum, this is really strange: there is no pictureFileData in the PicturePanel! Command is ignored.");
        }
    }

    /**
     * This method get the offscreenImage from the current pictureFileData. This
     * image is null, if pictureFileData is null. In this case, the repaint will
     * only clear the panel rectangle, on the screen.
     */
    private void calculateOffscreenImage() {
        /**
         * Cursor previousCursor = null; if (this.mainContainer != null) {
         * previousCursor = this.uploadPolicy.setWaitCursor(); }
         */
        if (this.pictureFileData == null) {
            // Nothing to do. offscreenImage should be null.
            if (this.offscreenImage != null) {
                this.offscreenImage = null;
                this.uploadPolicy
                        .displayWarn("PicturePanel.calculateOffscreenImage(): pictureFileData is null (offscreenImage set to null");
            }
        } else if (this.offscreenImage == null) {
            this.uploadPolicy
                    .displayDebug(
                            "PicturePanel.calculateOffscreenImage(): trying to calculate offscreenImage (PicturePanel.calculateOffscreenImage()",
                            40);
            try {
                this.offscreenImage = this.pictureFileData.getImage(this,
                        this.hasToStoreOffscreenPicture);
                /*
                 * if (offscreenImage != null) {
                 * uploadPolicy.displayDebug("PicturePanel.calculateOffscreenImage():
                 * got a non null offscreenImage", 40); } else {
                 * uploadPolicy.displayDebug("PicturePanel.calculateOffscreenImage():
                 * got a null offscreenImage", 40); }
                 */
            } catch (JUploadException e) {
                this.uploadPolicy.displayErr(e);
                // We won't try to display the picture for this file.
                this.pictureFileData = null;
                this.offscreenImage = null;
            }
        }

        /*
         * if (previousCursor != null && this.mainContainer != null) {
         * this.mainContainer.setCursor(previousCursor); }
         */
    }

    /**
     * Is it really useful ??
     */
    @Override
    protected void finalize() throws Throwable {
        // super.finalize();
        this.uploadPolicy.displayDebug("Within PicturePanel.finalize()", 90);

        this.pictureFileData = null;
        this.uploadPolicy = null;

        if (this.offscreenImage != null) {
            this.offscreenImage.flush();
            this.offscreenImage = null;
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////
    // /////////////////////// MouseListener interface
    // ////////////////////////////////////////////////////////////////////////////////////////////////////
    /** @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent) */
    public void mouseClicked(@SuppressWarnings("unused")
    MouseEvent arg0) {
        if (this.pictureFileData != null) {
            // Ok, we have a picture. Let's display it.
            this.uploadPolicy.onFileDoubleClicked(pictureFileData);
        }
    }

    /** @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent) */
    public void mouseEntered(@SuppressWarnings("unused")
    MouseEvent arg0) {
        // Nothing to do.
    }

    /** @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent) */
    public void mouseExited(@SuppressWarnings("unused")
    MouseEvent arg0) {
        // Nothing to do.
    }

    /** @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent) */
    public void mousePressed(@SuppressWarnings("unused")
    MouseEvent arg0) {
        // Nothing to do.
    }

    /** @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent) */
    public void mouseReleased(@SuppressWarnings("unused")
    MouseEvent arg0) {
        // Nothing to do.
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////
    // /////////////////////// ComponentListener interface
    // ////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public void componentHidden(@SuppressWarnings("unused")
    ComponentEvent arg0) {
        // No action
    }

    /**
     * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
     */
    public void componentMoved(@SuppressWarnings("unused")
    ComponentEvent arg0) {
        // No action
    }

    /**
     * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
     */
    public void componentResized(@SuppressWarnings("unused")
    ComponentEvent arg0) {
        this.uploadPolicy.displayDebug("Within componentResized", 60);
        if (this.offscreenImage != null) {
            this.offscreenImage.flush();
            this.offscreenImage = null;
        }

        // Then we calculate a new image for this panel.
        calculateOffscreenImage();
        repaint();
    }

    /**
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public void componentShown(@SuppressWarnings("unused")
    ComponentEvent arg0) {
        // No action
    }
}
