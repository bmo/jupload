//
// $Id: PictureUploadPolicy.java 295 2007-06-27 08:43:25 +0000 (mer., 27 juin
// 2007) etienne_sf $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// 
// Created: 2006-05-06
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

package wjhk.jupload2.policies;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.SystemColor;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

import wjhk.jupload2.JUploadApplet;
import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.exception.JUploadExceptionStopAddingFiles;
import wjhk.jupload2.exception.JUploadIOException;
import wjhk.jupload2.filedata.FileData;
import wjhk.jupload2.filedata.PictureFileData;
import wjhk.jupload2.gui.JUploadFileChooser;
import wjhk.jupload2.gui.JUploadImagePreview;
import wjhk.jupload2.gui.JUploadPanel;
import wjhk.jupload2.gui.PictureDialog;
import wjhk.jupload2.gui.PicturePanel;

/**
 * This class add handling of pictures to upload. <BR>
 * <BR>
 * <H4>Functionalities:</H4>
 * <UL>
 * <LI> The top panel (upper part of the applet display) is modified, by using
 * UploadPolicy.{@link wjhk.jupload2.policies.UploadPolicy#createTopPanel(JButton, JButton, JButton, JUploadPanel)}.
 * It contains a <B>preview</B> picture panel, and two additional buttons to
 * rotate the selected picture in one direction or the other.
 * <LI> Ability to set maximum width or height to a picture (with maxPicWidth
 * and maxPicHeight applet parameters, see the global explanation on the <a
 * href="UploadPolicy.html#parameters">parameters</a> section) of the
 * UploadPolicy API page.
 * <LI> Rotation of pictures, by quarter of turn.
 * <LI> <I>(To be implemented)</I> A target picture format can be used, to
 * force all uploaded pictures to be in one picture format, jpeg for instance.
 * All details are in the UploadPolicy <a
 * href="UploadPolicy.html#parameters">parameters</a> section.
 * </UL>
 * <BR>
 * <BR>
 * See an example of HTML that calls this applet, just below.
 * <H4>Parameters</H4>
 * The description for all parameters of all polices has been grouped in the
 * UploadPolicy <a href="UploadPolicy.html#parameters">parameters</a> section.
 * <BR>
 * The parameters implemented in this class are:
 * <UL>
 * <LI> maxPicWidth: Maximum width for the uploaded picture.
 * <LI> maxPicHeight: Maximum height for the uploaded picture.
 * <LI> <I>(To be implemented)</I> targetPictureFormat : Define the target
 * picture format. Eg: jpeg, png, gif...
 * </UL>
 * <A NAME="example">
 * <H4>HTML call example</H4>
 * </A> You'll find below an example of how to put the applet into a PHP page:
 * <BR>
 * <XMP> <APPLET NAME="JUpload" CODE="wjhk.jupload2.JUploadApplet"
 * ARCHIVE="plugins/jupload/wjhk.jupload.jar" <!-- Applet display size, on the
 * navigator page --> WIDTH="500" HEIGHT="700" <!-- The applet call some
 * javascript function, so we must allow it : --> MAYSCRIPT > <!-- First,
 * mandatory parameters --> <PARAM NAME="postURL"
 * VALUE="http://some.host.com/youruploadpage.php"> <PARAM NAME="uploadPolicy"
 * VALUE="PictureUploadPolicy"> <!-- Then, optional parameters --> <PARAM
 * NAME="lang" VALUE="fr"> <PARAM NAME="maxPicHeight" VALUE="768"> <PARAM
 * NAME="maxPicWidth" VALUE="1024"> <PARAM NAME="debugLevel" VALUE="0"> Java 1.4
 * or higher plugin required. </APPLET> </XMP>
 * 
 * @author etienne_sf
 * @version $Revision$
 */

public class PictureUploadPolicy extends DefaultUploadPolicy implements
        ActionListener, ImageObserver {

    /**
     * Indicates that a BufferedImage is to be created when the user selects the
     * file. <BR>
     * If true : the Image is loaded once from the hard drive. This consumes
     * memory, but is interesting for big pictures, when they are resized (see
     * {@link #maxWidth} and {@link #maxHeight}). <BR>
     * If false : it is loaded for each display on the applet, then once for the
     * upload. <BR>
     * <BR>
     * Default : false, because the applet, while in the navigator, runs too
     * quickly out of memory.
     * 
     * @see wjhk.jupload2.policies.UploadPolicy#DEFAULT_STORE_BUFFERED_IMAGE
     */
    private boolean storeBufferedImage;

    /**
     * Iimage type that should be uploaded (JPG, GIF...). It should be a
     * standard type, as the JVM will create this file. If null, the same format
     * as the original file is used. <BR>
     * Currently <B>this flag is ignored when createBufferedImage is false</B> .
     * <BR>
     * Default: null.
     * 
     * @see wjhk.jupload2.policies.UploadPolicy#DEFAULT_TARGET_PICTURE_FORMAT
     */
    private String targetPictureFormat;

    /**
     * Stored value for the fileChooserIconFromFileContent applet property.
     * 
     * @see UploadPolicy#PROP_FILE_CHOOSER_IMAGE_PREVIEW
     */
    private boolean fileChooserImagePreview = UploadPolicy.DEFAULT_FILE_CHOOSER_IMAGE_PREVIEW;

    /**
     * Indicates wether or not the preview pictures must be calculated by the
     * BufferedImage.getScaledInstance() method.
     */
    private boolean highQualityPreview;

    /**
     * Maximal width for the uploaded picture. If the actual width for the
     * picture is more than maxWidth, the picture is resized. The proportion
     * between widht and height are maintained. Negative if no maximum width (no
     * resizing). <BR>
     * Default: -1.
     * 
     * @see wjhk.jupload2.policies.UploadPolicy#DEFAULT_MAX_WIDTH
     */
    private int maxWidth = -1;

    /**
     * Maximal height for the uploaded picture. If the actual height for the
     * picture is more than maxHeight, the picture is resized. The proportion
     * between width and height are maintained. Negative if no maximum height
     * (no resizing). <BR>
     * Default: -1.
     * 
     * @see wjhk.jupload2.policies.UploadPolicy#DEFAULT_MAX_HEIGHT
     */
    private int maxHeight = -1;

    /**
     * Used to control the compression of a jpeg written file, after
     * transforming a picture.
     * 
     * @see UploadPolicy#PROP_PICTURE_COMPRESSION_QUALITY
     */
    private float pictureCompressionQuality = UploadPolicy.DEFAULT_PICTURE_COMPRESSION_QUALITY;

    /**
     * Used to control whether PictureFileData should add metadata to
     * transformed picture files, before upload (or remove metadata from
     * normally untransformed picture files).
     */
    private boolean pictureTransmitMetadata;

    /**
     * @see UploadPolicy
     */
    private int realMaxWidth = -1;

    /**
     * @see UploadPolicy
     */
    private int realMaxHeight = -1;

    /**
     * Button to allow the user to rotate the picture one quarter
     * counter-clockwise.
     */
    private JButton rotateLeftButton;

    /**
     * Button to allow the user to rotate the picture one quarter clockwise.
     */
    private JButton rotateRightButton;

    /**
     * The picture panel, where the selected picture is displayed.
     */
    private PicturePanel picturePanel;

    /**
     * The standard constructor, which transmit most informations to the
     * super.Constructor().
     * 
     * @param theApplet Reference to the current applet. Allows access to
     *            javascript functions.
     * @throws JUploadException 
     */
    public PictureUploadPolicy(JUploadApplet theApplet) throws JUploadException {
        super(theApplet);

        // Creation of the PictureFileDataPolicy, from parameters given to the
        // applet, or from default values.
        setFileChooserImagePreview(UploadPolicyFactory.getParameter(theApplet,
                PROP_FILE_CHOOSER_IMAGE_PREVIEW,
                DEFAULT_FILE_CHOOSER_IMAGE_PREVIEW, this));
        setHighQualityPreview(UploadPolicyFactory.getParameter(theApplet,
                PROP_HIGH_QUALITY_PREVIEW, DEFAULT_HIGH_QUALITY_PREVIEW, this));
        setMaxHeight(UploadPolicyFactory.getParameter(theApplet,
                PROP_MAX_HEIGHT, DEFAULT_MAX_HEIGHT, this));
        setMaxWidth(UploadPolicyFactory.getParameter(theApplet, PROP_MAX_WIDTH,
                DEFAULT_MAX_WIDTH, this));
        setPictureCompressionQuality(UploadPolicyFactory.getParameter(
                theApplet, PROP_PICTURE_COMPRESSION_QUALITY,
                DEFAULT_PICTURE_COMPRESSION_QUALITY, this));
        setPictureTransmitMetadata(UploadPolicyFactory.getParameter(theApplet,
                PROP_PICTURE_TRANSMIT_METADATA,
                DEFAULT_PICTURE_TRANSMIT_METADATA, this));
        setRealMaxHeight(UploadPolicyFactory.getParameter(theApplet,
                PROP_REAL_MAX_HEIGHT, DEFAULT_REAL_MAX_HEIGHT, this));
        setRealMaxWidth(UploadPolicyFactory.getParameter(theApplet,
                PROP_REAL_MAX_WIDTH, DEFAULT_REAL_MAX_WIDTH, this));
        setStoreBufferedImage(UploadPolicyFactory.getParameter(theApplet,
                PROP_STORE_BUFFERED_IMAGE, DEFAULT_STORE_BUFFERED_IMAGE, this));
        setTargetPictureFormat(UploadPolicyFactory
                .getParameter(theApplet, PROP_TARGET_PICTURE_FORMAT,
                        DEFAULT_TARGET_PICTURE_FORMAT, this));

        // The UploadPolicyFactory class will call displayParameterStatus(), so
        // that
        // we display all applet parameters, after initialization.
    }

    /**
     * This methods actually returns a {@link PictureFileData} instance. It
     * allows only pictures: if the file is not a picture, this method returns
     * null, thus preventing the file to be added to the list of files to be
     * uploaded.
     * 
     * @param file The file selected by the user (called once for each added
     *            file).
     * @return An instance of {@link PictureFileData} or null if file is not a
     *         picture.
     * @see wjhk.jupload2.policies.UploadPolicy#createFileData(File,File)
     */
    @Override
    public FileData createFileData(File file, File root)
            throws JUploadExceptionStopAddingFiles {
        PictureFileData pfd = null;
        try {
            pfd = new PictureFileData(file, root, this);
        } catch (JUploadIOException e) {
            displayErr(e);
        }

        // If we get a pfd, let' check that it's a picture.
        if (pfd != null) {
            if (pfd.isPicture()) {
                return pfd;
            } else {
                // We now use the JUploadExceptionStopAddingFiles exception, to
                // allow the user to stop adding files.
                String msg = String.format(getString("notAPicture"), file
                        .getName());

                // Alert only once, when several files are not pictures... hum,
                displayWarn(msg);
                if (JOptionPane.showConfirmDialog(null, msg, "alert",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE) == JOptionPane.CANCEL_OPTION) {
                    // The user want to stop to add files to the list. For
                    // instance,
                    // when he/she added a whole directory, and it contains a
                    // lot of
                    // files that don't match the allowed file extension.
                    throw new JUploadExceptionStopAddingFiles(
                            "Stopped by the user");
                }
            }
        }
        return null;
    }

    /**
     * This method override the default topPanel, and adds:<BR>
     * <UL>
     * <LI>Two rotation buttons, to rotate the currently selected picture.
     * <LI>A Preview area, to view the selected picture
     * </UL>
     * 
     * @see wjhk.jupload2.policies.UploadPolicy#createTopPanel(JButton, JButton,
     *      JButton, JUploadPanel)
     */
    @Override
    public JPanel createTopPanel(JButton browse, JButton remove,
            JButton removeAll, JUploadPanel jUploadPanel) {
        // The top panel is verticaly divided in :
        // - On the left, the button bar (buttons one above another)
        // - On the right, the preview PicturePanel.

        // Creation of specific buttons
        this.rotateLeftButton = new JButton(getString("buttonRotateLeft"));
        this.rotateLeftButton.setIcon(new ImageIcon(getClass().getResource(
                "/images/rotateLeft.gif")));
        this.rotateLeftButton.addActionListener(this);
        this.rotateLeftButton.addMouseListener(jUploadPanel);
        this.rotateLeftButton.setEnabled(false);

        this.rotateRightButton = new JButton(getString("buttonRotateRight"));
        this.rotateRightButton.setIcon(new ImageIcon(getClass().getResource(
                "/images/rotateRight.gif")));
        this.rotateRightButton.addActionListener(this);
        this.rotateRightButton.addMouseListener(jUploadPanel);
        this.rotateRightButton.setEnabled(false);

        // The button bar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        buttonPanel.add(browse);
        buttonPanel.add(this.rotateLeftButton);
        buttonPanel.add(this.rotateRightButton);
        buttonPanel.add(removeAll);
        buttonPanel.add(remove);

        // The preview PicturePanel
        JPanel pPanel = new JPanel();
        pPanel.setLayout(new GridLayout(1, 1));
        pPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));

        this.picturePanel = new PicturePanel(true, this);
        this.picturePanel.addMouseListener(jUploadPanel);
        pPanel.add(this.picturePanel);
        // Setting specific cursor for this panel, default for other parts of
        // the applet.
        setCursor(null);

        // And last but not least ... creation of the top panel:
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(buttonPanel);
        topPanel.add(pPanel);

        jUploadPanel.setBorder(BorderFactory
                .createLineBorder(SystemColor.controlDkShadow));

        return topPanel;
    }// createTopPanel

    /**
     * This method handles the clicks on the rotation buttons. All other actions
     * are managed by the {@link DefaultUploadPolicy}.
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        displayInfo("Action : " + e.getActionCommand());
        if (e.getActionCommand() == this.rotateLeftButton.getActionCommand()) {
            this.picturePanel.rotate(-1);
        } else if (e.getActionCommand() == this.rotateRightButton
                .getActionCommand()) {
            this.picturePanel.rotate(1);
        }
    }// actionPerformed

    /**
     * @see wjhk.jupload2.policies.UploadPolicy#onFileSelected(wjhk.jupload2.filedata.FileData)
     */
    @Override
    public void onFileSelected(FileData fileData) {
        if (fileData != null) {
            displayDebug("File selected: " + fileData.getFileName(), 30);
        }
        if (this.picturePanel != null) {
            Cursor previousCursor = setWaitCursor();
            this.picturePanel.setPictureFile((PictureFileData) fileData,
                    this.rotateLeftButton, this.rotateRightButton);
            // this.rotateLeftButton.setEnabled(fileData != null);
            // this.rotateRightButton.setEnabled(fileData != null);
            // TODO remove the two lines above, if tests are Ok.
            setCursor(previousCursor);
        }
    }

    /**
     * Open the 'big' preview dialog box. It allows the user to see a full
     * screen preview of the choosen picture.<BR>
     * This method does nothing if the panel has no selected picture, that is
     * when pictureFileData is null.
     * 
     * @see UploadPolicy#onFileDoubleClicked(FileData)
     */
    @Override
    public void onFileDoubleClicked(FileData pictureFileData) {
        if (pictureFileData == null) {
            // No action
        } else if (!(pictureFileData instanceof PictureFileData)) {
            displayWarn("PictureUploadPolicy: received a non PictureFileData in onFileDoubleClicked");
        } else {
            new PictureDialog(null, (PictureFileData) pictureFileData, this);
        }
    }

    /** @see UploadPolicy#beforeUpload() */
    @Override
    public void beforeUpload() {
        // We clear the current picture selection. This insures a correct
        // managing of enabling/disabling of
        // buttons, even if the user stops the upload.
        getApplet().getUploadPanel().getFilePanel().clearSelection();
        if (this.picturePanel != null) {
            this.picturePanel.setPictureFile(null, this.rotateLeftButton,
                    this.rotateRightButton);
        }

        // Then, we call the standard action, if any.
        super.beforeUpload();
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////
    // /////////////////////// Getters and Setters
    // ////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Getter for fileChooserImagePreview.
     * 
     * @return Current value for the applet parameter: fileChooserImagePreview
     * @see UploadPolicy#PROP_FILE_CHOOSER_IMAGE_PREVIEW
     */
    public boolean getFileChooserImagePreview() {
        return fileChooserImagePreview;
    }

    /**
     * Setter for fileChooserIconFromFileContent. Current allowed
     * values are: -1, 0, 1. Default value is 0.
     * 
     * @param fileChooserImagePreview new value to store, for the applet
     *            parameter: fileChooserImagePreview.
     * @see UploadPolicy#PROP_FILE_CHOOSER_IMAGE_PREVIEW
     */
    public void setFileChooserImagePreview(boolean fileChooserImagePreview) {
        this.fileChooserImagePreview = fileChooserImagePreview;
    }

    /** @return the applet parameter <I>highQualityPreview</I>. */
    public boolean getHighQualityPreview() {
        return this.highQualityPreview;
    }

    /** @param highQualityPreview the highQualityPreview to set */
    void setHighQualityPreview(boolean highQualityPreview) {
        this.highQualityPreview = highQualityPreview;
    }

    /**
     * @return Returns the maxHeight, that should be used by pictures non
     *         transformed (rotated...) by the applet.
     */
    public int getMaxHeight() {
        return this.maxHeight;
    }

    /** @param maxHeight the maxHeight to set */
    void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * @return Returns the maxWidth, that should be used by pictures non
     *         transformed (rotated...) by the applet.
     */
    public int getMaxWidth() {
        return this.maxWidth;
    }

    /** @param maxWidth the maxWidth to set */
    void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * @return The current value for picture compression.
     */
    public float getPictureCompressionQuality() {
        return this.pictureCompressionQuality;
    }

    /**
     * @see #pictureCompressionQuality
     * @param pictureCompressionQuality The new value for picture compression.
     */
    void setPictureCompressionQuality(float pictureCompressionQuality) {
        this.pictureCompressionQuality = pictureCompressionQuality;
    }

    /**
     * @return The current value for transmission (or no transmission) of
     *         picture metadata.
     */
    public boolean getPictureTransmitMetadata() {
        return this.pictureTransmitMetadata;
    }

    /**
     * @see #pictureTransmitMetadata
     * @param pictureTransmitMetadata The new value for this attribute.
     */
    void setPictureTransmitMetadata(boolean pictureTransmitMetadata) {
        this.pictureTransmitMetadata = pictureTransmitMetadata;
    }

    /**
     * @return Returns the maxHeight, that should be used by pictures that are
     *         transformed (rotated...) by the applet.
     */
    public int getRealMaxHeight() {
        return (this.realMaxHeight == Integer.MAX_VALUE) ? this.maxHeight
                : this.realMaxHeight;
    }

    /** @param realMaxHeight the realMaxHeight to set */
    void setRealMaxHeight(int realMaxHeight) {
        this.realMaxHeight = realMaxHeight;
    }

    /**
     * @return Returns the maxWidth, that should be used by pictures that are
     *         transformed (rotated...) by the applet.
     */
    public int getRealMaxWidth() {
        return (this.realMaxWidth == Integer.MAX_VALUE) ? this.maxWidth
                : this.realMaxWidth;
    }

    /** @param realMaxWidth the realMaxWidth to set */
    void setRealMaxWidth(int realMaxWidth) {
        this.realMaxWidth = realMaxWidth;
    }

    /** @return Returns the createBufferedImage. */
    public boolean hasToStoreBufferedImage() {
        return this.storeBufferedImage;
    }

    /** @param storeBufferedImage the storeBufferedImage to set */
    void setStoreBufferedImage(boolean storeBufferedImage) {
        this.storeBufferedImage = storeBufferedImage;
    }

    /** @return Returns the targetPictureFormat. */
    public String getTargetPictureFormat() {
        return this.targetPictureFormat;
    }

    /** @param targetPictureFormat the targetPictureFormat to set */
    void setTargetPictureFormat(String targetPictureFormat) {
        this.targetPictureFormat = targetPictureFormat;
    }

    /**
     * This method manages the applet parameters that are specific to this
     * class. The super.setProperty method is called for other properties.
     * 
     * @param prop The property which value should change
     * @param value The new value for this property. If invalid, the default
     *            value is used.
     * @see wjhk.jupload2.policies.UploadPolicy#setProperty(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void setProperty(String prop, String value) throws JUploadException {
        // The, we check the local properties.
        if (prop.equals(PROP_FILE_CHOOSER_IMAGE_PREVIEW)) {
            setFileChooserImagePreview(UploadPolicyFactory.parseBoolean(value,
                    getFileChooserImagePreview(), this));
        } else if (prop.equals(PROP_STORE_BUFFERED_IMAGE)) {
            setStoreBufferedImage(UploadPolicyFactory.parseBoolean(value,
                    this.storeBufferedImage, this));
        } else if (prop.equals(PROP_HIGH_QUALITY_PREVIEW)) {
            setHighQualityPreview(UploadPolicyFactory.parseBoolean(value,
                    this.highQualityPreview, this));
        } else if (prop.equals(PROP_MAX_HEIGHT)) {
            setMaxHeight(UploadPolicyFactory.parseInt(value, this.maxHeight,
                    this));
        } else if (prop.equals(PROP_MAX_WIDTH)) {
            setMaxWidth(UploadPolicyFactory
                    .parseInt(value, this.maxWidth, this));
        } else if (prop.equals(PROP_PICTURE_COMPRESSION_QUALITY)) {
            setPictureCompressionQuality(UploadPolicyFactory.parseFloat(value,
                    this.pictureCompressionQuality, this));
        } else if (prop.equals(PROP_PICTURE_TRANSMIT_METADATA)) {
            setPictureTransmitMetadata(UploadPolicyFactory.parseBoolean(value,
                    this.pictureTransmitMetadata, this));
        } else if (prop.equals(PROP_REAL_MAX_HEIGHT)) {
            setRealMaxHeight(UploadPolicyFactory.parseInt(value,
                    this.realMaxHeight, this));
        } else if (prop.equals(PROP_REAL_MAX_WIDTH)) {
            setRealMaxWidth(UploadPolicyFactory.parseInt(value,
                    this.realMaxWidth, this));
        } else if (prop.equals(PROP_TARGET_PICTURE_FORMAT)) {
            setTargetPictureFormat(value);
        } else {
            // Otherwise, transmission to the mother class.
            super.setProperty(prop, value);
        }
    }

    /** @see DefaultUploadPolicy#displayParameterStatus() */
    @Override
    public void displayParameterStatus() {
        super.displayParameterStatus();

        displayDebug("======= Parameters managed by PictureUploadPolicy", 20);
        displayDebug(PROP_FILE_CHOOSER_IMAGE_PREVIEW + ": "
                + getFileChooserImagePreview(), 20);
        displayDebug(PROP_HIGH_QUALITY_PREVIEW + " : "
                + this.highQualityPreview, 20);
        displayDebug(PROP_PICTURE_COMPRESSION_QUALITY + " : "
                + getPictureCompressionQuality(), 20);
        displayDebug(PROP_PICTURE_TRANSMIT_METADATA + " : "
                + getPictureTransmitMetadata(), 20);
        if (this.maxWidth != DEFAULT_MAX_WIDTH
                || this.maxHeight != DEFAULT_MAX_HEIGHT) {
            displayDebug(PROP_MAX_WIDTH + " : " + this.maxWidth + ", "
                    + PROP_MAX_HEIGHT + " : " + this.maxHeight, 20);
        }
        if (this.realMaxWidth != DEFAULT_REAL_MAX_WIDTH
                || this.realMaxHeight != DEFAULT_REAL_MAX_HEIGHT) {
            displayDebug(PROP_REAL_MAX_WIDTH + " : " + this.realMaxWidth + ", "
                    + PROP_REAL_MAX_HEIGHT + " : " + this.realMaxHeight, 20);
        }
        displayDebug(PROP_STORE_BUFFERED_IMAGE + " : "
                + this.storeBufferedImage, 20);
        displayDebug(PROP_TARGET_PICTURE_FORMAT + " : "
                + this.targetPictureFormat, 20);
        displayDebug("", 20);
    }

    /**
     * Calls the {@link DefaultUploadPolicy#setWaitCursor()} method, then erases
     * the picture panel specific cursor.
     * 
     * @see DefaultUploadPolicy#setCursor(Cursor)
     */
    @Override
    public Cursor setWaitCursor() {
        Cursor previousCursor = super.setWaitCursor();
        picturePanel.setCursor(null);
        return previousCursor;
    }

    /**
     * Calls the {@link DefaultUploadPolicy#setCursor(Cursor)} method, then set
     * the picture panel specific cursor.
     * 
     * @see DefaultUploadPolicy#setCursor(Cursor)
     */
    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(null);
        picturePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Creates the file chooser, from the default implementation, then add an
     * accessory to preview pictures.
     * 
     * @see UploadPolicy#createFileChooser()
     */
    @Override
    public JUploadFileChooser createFileChooser() {
        JUploadFileChooser jufc = super.createFileChooser();
        if (getFileChooserImagePreview()) {
            jufc.setAccessory(new JUploadImagePreview(jufc, this));
        }
        return jufc;
    }

    /**
     * Returns an icon, calculated from the image content. Currently only
     * pictures managed by ImageIO can be displayed. Once upon a day, axtracting
     * the first picture of a video may become reality... ;-)
     * 
     * @return The calculated ImageIcon, or null if no picture can be extracted.
     * @see UploadPolicy#fileViewGetIcon(File)
     * @see UploadPolicy#PROP_FILE_CHOOSER_ICON_FROM_FILE_CONTENT
     */
    @Override
    public Icon fileViewGetIcon(File file) {
        /*
         * // Default is to retuen a null ImageIcon. ImageIcon imageIcon = null;
         * if (null != file) { try { // First, we load the picture BufferedImage
         * image = ImageIO.read(file); if (image == null) { displayDebug(
         * file.getName() + " is not an image (in
         * PictureUploadPolicy.fileViewGetIcon()", 80); } else { BufferedImage
         * resized = resizePicture(image, getFileChooserIconSize(),
         * getFileChooserIconSize(), false, this); imageIcon = new
         * ImageIcon(resized); } } catch (IllegalArgumentException e) { //
         * ignore, but still displays a warning.
         * displayWarn(e.getClass().getName() + ": " + e.getMessage()); } catch
         * (IOException e) { displayErr(e); } } return imageIcon;
         */
        return PictureFileData.getImageIcon(file, getFileChooserIconSize(),
                getFileChooserIconSize());
    }

    /** Implementation of the ImageObserver interface 
     * @param arg0 
     * @param arg1 
     * @param arg2 
     * @param arg3 
     * @param arg4 
     * @param arg5 
     * @return true or false 
     */
    public boolean imageUpdate(@SuppressWarnings("unused")
    Image arg0, @SuppressWarnings("unused")
    int arg1, @SuppressWarnings("unused")
    int arg2, @SuppressWarnings("unused")
    int arg3, @SuppressWarnings("unused")
    int arg4, @SuppressWarnings("unused")
    int arg5) {
        return true;
    }
}