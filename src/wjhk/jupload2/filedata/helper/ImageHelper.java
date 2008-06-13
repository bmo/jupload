//
// $Id$
//
// jupload - A file upload applet.
//
// Copyright 2008 The JUpload Team
//
// Created: 12 févr. 08
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

package wjhk.jupload2.filedata.helper;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.filedata.DefaultFileData;
import wjhk.jupload2.filedata.PictureFileData;
import wjhk.jupload2.policies.PictureUploadPolicy;

/**
 * Class that contains various utilities about picture, mainly about picture
 * transformation.
 * 
 * @author etienne_sf
 * 
 */
public class ImageHelper implements ImageObserver {

    /**
     * hasToTransformPicture indicates whether the picture should be
     * transformed. Null if unknown. This can happen (for instance) if no calcul
     * where done (during initialization), or after rotating the picture back to
     * the original orientation. <BR>
     * <B>Note:</B> this attribute is from the class Boolean (and not a simple
     * boolean), to allow null value, meaning <I>unknown</I>.
     */
    private Boolean hasToTransformPicture = null;

    /**
     * The {@link PictureFileData} that this helper will have to help.
     */
    private PictureFileData pictureFileData;

    /**
     * Current rotation of the picture: 0 to 3.
     * 
     * @see PictureFileData
     */
    private int quarterRotation;

    /**
     * Maximum width for the current transformation
     */
    private int maxWidth;

    /**
     * Maximum height for the current transformation
     */
    private int maxHeight;

    /**
     * Defines the number of pixel for the current picture. Used to update the
     * progress bar.
     * 
     * @see #getBufferedImage(boolean, BufferedImage)
     * @see #imageUpdate(Image, int, int, int, int, int)
     */
    private int nbPixelsTotal = -1;

    /**
     * Indicates the number of pixels that have been read.
     * 
     * @see #nbPixelsTotal
     * @see #imageUpdate(Image, int, int, int, int, int)
     */
    private int nbPixelsRead = 0;

    /**
     * Width of picture, after rescaling but without rotation. It should be
     * scale*originalWidth, but, due to rounding number, it can be transformed
     * to scale*originalWidth-1.
     * 
     * @see #initScale()
     */
    private int scaledNonRotatedWidth = -1;

    /**
     * Same as {@link #scaledNonRotatedWidth}
     */
    private int scaledNonRotatedHeight = -1;

    /**
     * The value that has the progress bar when starting to load the picture.
     * The {@link #imageUpdate(Image, int, int, int, int, int)} method will add
     * from 0 to 100, to indicate progress with a percentage value of picture
     * loading.
     */
    private int progressBarBaseValue = 0;

    /**
     * Current scaling factor. If less than 1, means a picture reduction.
     * 
     * @see #initScale()
     */
    private double scale = 1;

    /**
     * Width of picture, after rescaling and rotation. It should be
     * scale*originalWidth or scale*originalHeight (depending on the rotation).
     * But, due to rounding number, it can be transformed to
     * scale*originalWidth-1 or scale*originalHeight-1.
     * 
     * @see #initScale()
     */
    private int scaledRotatedWidth = -1;

    /**
     * Same as {@link #scaledRotatedWidth}, for the height.
     */
    private int scaledRotatedHeight = -1;

    /**
     * The current upload policy must be a {@link PictureUploadPolicy}
     */
    PictureUploadPolicy uploadPolicy;

    /**
     * Standard constructor.
     * 
     * @param uploadPolicy The current upload policy
     * @param pictureFileData The picture file data to help
     * @param targetMaxWidth
     * @param targetMaxHeight
     * @param quarterRotation Current quarter rotation (from 0 to 3)
     */
    public ImageHelper(PictureUploadPolicy uploadPolicy,
            PictureFileData pictureFileData, int targetMaxWidth,
            int targetMaxHeight, int quarterRotation) {
        this.uploadPolicy = uploadPolicy;
        this.pictureFileData = pictureFileData;
        this.maxWidth = targetMaxWidth;
        this.maxHeight = targetMaxHeight;
        this.quarterRotation = quarterRotation;

        // Pre-calculation: should the current picture be rescaled, to match the
        // given target size ?
        initScale();
    }

    /**
     * Intialization of scale factor, for the current picture state. The scale
     * is based on the maximum width and height, the current rotation, and the
     * picture size.
     */
    private void initScale() {
        double theta = Math.toRadians(90 * this.quarterRotation);

        // The width and height depend on the current rotation :
        // calculation of the width and height of picture after
        // rotation.
        int nonScaledRotatedWidth = pictureFileData.getOriginalWidth();
        int nonScaledRotatedHeight = pictureFileData.getOriginalHeight();
        if (this.quarterRotation % 2 != 0) {
            // 90° or 270° rotation: width and height are switched.
            nonScaledRotatedWidth = pictureFileData.getOriginalHeight();
            nonScaledRotatedHeight = pictureFileData.getOriginalWidth();
        }
        // Now, we can compare these width and height to the maximum
        // width and height
        double scaleWidth = ((maxWidth < 0) ? 1 : ((double) maxWidth)
                / nonScaledRotatedWidth);
        double scaleHeight = ((maxHeight < 0) ? 1 : ((double) maxHeight)
                / nonScaledRotatedHeight);
        scale = Math.min(scaleWidth, scaleHeight);
        if (scale < 1) {
            // With number rounding, it can happen that width or size
            // became one pixel too big. Let's correct it.
            if ((maxWidth > 0 && maxWidth < (int) (scale * Math.cos(theta) * nonScaledRotatedWidth))
                    || (maxHeight > 0 && maxHeight < (int) (scale
                            * Math.cos(theta) * nonScaledRotatedHeight))) {
                scaleWidth = ((maxWidth < 0) ? 1 : ((double) maxWidth - 1)
                        / (nonScaledRotatedWidth));
                scaleHeight = ((maxHeight < 0) ? 1 : ((double) maxHeight - 1)
                        / (nonScaledRotatedHeight));
                scale = Math.min(scaleWidth, scaleHeight);
            }
        }

        // These variables contain the actual width and height after
        // rescaling, and before rotation.
        scaledRotatedWidth = nonScaledRotatedWidth;
        scaledRotatedHeight = nonScaledRotatedHeight;
        // Is there any rescaling to do ?
        // Patch for the first bug, tracked in the sourceforge bug
        // tracker ! ;-)
        if (scale < 1) {
            scaledRotatedWidth *= scale;
            scaledRotatedHeight *= scale;
        }
        this.uploadPolicy.displayDebug("Resizing factor (scale): " + scale, 10);
        // Due to rounded numbers, the resulting targetWidth or
        // targetHeight
        // may be one pixel too big. Let's check that.
        if (scaledRotatedWidth > maxWidth) {
            this.uploadPolicy.displayDebug("Correcting rounded width: "
                    + scaledRotatedWidth + " to " + maxWidth, 10);
            scaledRotatedWidth = maxWidth;
        }
        if (scaledRotatedHeight > maxHeight) {
            this.uploadPolicy.displayDebug("Correcting rounded height: "
                    + scaledRotatedHeight + " to " + maxHeight, 10);
            scaledRotatedHeight = maxHeight;
        }

        // getBufferedImage will need the two following value:
        if (this.quarterRotation % 2 == 0) {
            scaledNonRotatedWidth = scaledRotatedWidth;
            scaledNonRotatedHeight = scaledRotatedHeight;
        } else {
            scaledNonRotatedWidth = scaledRotatedHeight;
            scaledNonRotatedHeight = scaledRotatedWidth;
        }
    }

    /**
     * This function indicate if the picture has to be modified. For instance :
     * a maximum width, height, a target format...
     * 
     * @return true if the picture must be transformed. false if the file can be
     *         directly transmitted.
     * @throws JUploadException Contains any exception that could be thrown in
     *             this method
     */
    public boolean hasToTransformPicture() throws JUploadException {
        // Animated gif must be transmit as is, as I can't find a way to
        // recreate them.
        if (DefaultFileData.getExtension(pictureFileData.getFile())
                .equalsIgnoreCase("gif")) {
            // If this is an animated gif, no transformation... I can't succeed
            // to create a transformed picture file for them.
            ImageReaderWriterHelper irwh = new ImageReaderWriterHelper(
                    uploadPolicy, pictureFileData);
            int nbImages = irwh.getNumImages(true);
            irwh.dispose();
            irwh = null;
            if (nbImages > 1) {
                // Too bad. We can not transform it.
                // FIXME Be able to transform animated gif.
                this.hasToTransformPicture = Boolean.FALSE;
                uploadPolicy
                        .displayWarn("No transformation for gif picture file, that contain several pictures. (see JUpload documentation for details)");
            }
        }

        // Did we already estimate if transformation is needed ?
        if (this.hasToTransformPicture == null) {

            // First : the easiest test. Should we block metadata ?
            if (this.hasToTransformPicture == null
                    && !((PictureUploadPolicy) uploadPolicy)
                            .getPictureTransmitMetadata()) {
                this.hasToTransformPicture = Boolean.TRUE;
            }
            // Second : another easy test. A rotation is needed ?
            if (this.hasToTransformPicture == null && this.quarterRotation != 0) {
                this.uploadPolicy
                        .displayDebug(
                                pictureFileData.getFileName()
                                        + " : hasToTransformPicture = true (quarterRotation != 0)",
                                20);
                this.hasToTransformPicture = Boolean.TRUE;
            }

            // Second : the picture format is the same ?
            if (this.hasToTransformPicture == null
                    && ((PictureUploadPolicy) this.uploadPolicy)
                            .getTargetPictureFormat() != null) {
                // A target format is positioned: is it the same as the current
                // file format ?
                String target = ((PictureUploadPolicy) this.uploadPolicy)
                        .getTargetPictureFormat().toLowerCase();
                String ext = pictureFileData.getFileExtension().toLowerCase();

                if (target.equals("jpg"))
                    target = "jpeg";
                if (ext.equals("jpg"))
                    ext = "jpeg";

                if (!target.equals(ext)) {
                    this.uploadPolicy
                            .displayDebug(
                                    pictureFileData.getFileName()
                                            + " : hasToTransformPicture = true (targetPictureFormat)",
                                    20);
                    // Correction given by David Gnedt: the following line was
                    // lacking!
                    this.hasToTransformPicture = Boolean.TRUE;
                }
            }

            // Third : should we resize the picture ?
            if (this.hasToTransformPicture == null && scale < 1) {
                this.uploadPolicy.displayDebug(pictureFileData.getFileName()
                        + " : hasToTransformPicture = true (scale < 1)", 20);
                this.hasToTransformPicture = Boolean.TRUE;
            }

            // If we find no reason to transform the picture, then let's let the
            // picture unmodified.
            if (this.hasToTransformPicture == null) {
                this.uploadPolicy.displayDebug(pictureFileData.getFileName()
                        + " : hasToTransformPicture = false", 20);
                this.hasToTransformPicture = Boolean.FALSE;
            }
        }

        return this.hasToTransformPicture.booleanValue();
    }// end of hasToTransformPicture

    /**
     * This function resizes the picture, if necessary, according to the
     * maxWidth and maxHeight, given to the ImageHelper constructor. <BR>
     * This function should only be called if isPicture is true. Otherwise, an
     * exception is raised. <BR>
     * Note (Update given by David Gnedt): the highquality will condition the
     * call of getScaledInstance, instead of a basic scale Transformation. The
     * generated picture is of better quality, but this is longer, especially on
     * 'small' CPU. Time samples, with one picture from my canon EOS20D, on a
     * PII 500M: <BR>
     * ~3s for the full screen preview with highquality to false, and a quarter
     * rotation. 12s to 20s with highquality to true. <BR>
     * ~5s for the first (small) preview of the picture, with both highquality
     * to false or true.
     * 
     * @param highquality (added by David Gnedt): if set to true, the
     *            BufferedImage.getScaledInstance() is called. This generates
     *            better image, but consumes more CPU.
     * @param sourceBufferedImage The image to resize or rotate or both or no
     *            tranformation...
     * @return A BufferedImage which contains the picture according to current
     *         parameters (resizing, rotation...), or null if this is not a
     *         picture.
     * @throws JUploadException Contains any exception thrown from within this
     *             method.
     */
    public BufferedImage getBufferedImage(boolean highquality,
            BufferedImage sourceBufferedImage) throws JUploadException {
        long msGetBufferedImage = System.currentTimeMillis();
        double theta = Math.toRadians(90 * this.quarterRotation);

        BufferedImage returnedBufferedImage = null;

        this.uploadPolicy.displayDebug("getBufferedImage: start", 10);

        try {
            AffineTransform transform = new AffineTransform();

            if (this.quarterRotation != 0) {
                double translationX = 0, translationY = 0;
                this.uploadPolicy.displayDebug("getBufferedImage: quarter: "
                        + this.quarterRotation, 30);

                // quarterRotation is one of 0, 1, 2, 3 : see addRotation.
                // If we're here : it's not 0, so it's one of 1, 2 or 3.
                switch (this.quarterRotation) {
                    case 1:
                        translationX = 0;
                        translationY = -scaledRotatedWidth;
                        break;
                    case 2:
                        translationX = -scaledRotatedWidth;
                        translationY = -scaledRotatedHeight;
                        break;
                    case 3:
                        translationX = -scaledRotatedHeight;
                        translationY = 0;
                        break;
                    default:
                        this.uploadPolicy
                                .displayWarn("Invalid quarterRotation : "
                                        + this.quarterRotation);
                        this.quarterRotation = 0;
                        theta = 0;
                }
                transform.rotate(theta);
                transform.translate(translationX, translationY);
            }

            // If we have to rescale the picture, we first do it:
            if (scale < 1) {
                if (highquality) {
                    this.uploadPolicy
                            .displayDebug(
                                    "getBufferedImage: Resizing picture(using high quality picture)",
                                    40);

                    // SCALE_AREA_AVERAGING forces the picture calculation
                    // algorithm.
                    // Other parameters give bad picture quality.
                    Image img = sourceBufferedImage.getScaledInstance(
                            scaledNonRotatedWidth, scaledNonRotatedHeight,
                            Image.SCALE_AREA_AVERAGING);

                    // the localBufferedImage may be 'unknown'.
                    int localImageType = sourceBufferedImage.getType();
                    if (localImageType == BufferedImage.TYPE_CUSTOM) {
                        localImageType = BufferedImage.TYPE_INT_BGR;
                    }

                    BufferedImage tempBufferedImage = new BufferedImage(
                            scaledNonRotatedWidth, scaledNonRotatedHeight,
                            localImageType);

                    // drawImage can be long. Let's follow its progress,
                    // with the applet progress bar.
                    this.nbPixelsTotal = scaledNonRotatedWidth
                            * scaledNonRotatedHeight;
                    this.nbPixelsRead = 0;

                    // Let's draw the picture: this code do the rescaling.
                    this.uploadPolicy.displayDebug(
                            "getBufferedImage: Before drawImage", 100);
                    tempBufferedImage.getGraphics().drawImage(img, 0, 0, this);
                    this.uploadPolicy.displayDebug(
                            "getBufferedImage: After drawImage", 100);

                    tempBufferedImage.flush();

                    img.flush();
                    img = null;
                    pictureFileData
                            .freeMemory("ImageHelper.getBufferedImage()");

                    // tempBufferedImage contains the rescaled picture. It's
                    // the source image for the next step (rotation).
                    sourceBufferedImage = tempBufferedImage;
                    tempBufferedImage = null;
                } else {
                    // The scale method adds scaling before current
                    // transformation.
                    this.uploadPolicy
                            .displayDebug(
                                    "getBufferedImage: Resizing picture(using standard quality picture)",
                                    40);
                    transform.scale(scale, scale);
                }
            }

            uploadPolicy.displayDebug(
                    "getBufferedImage: Picture is now rescaled", 80);

            if (transform.isIdentity()) {
                returnedBufferedImage = sourceBufferedImage;
            } else {
                AffineTransformOp affineTransformOp = null;
                // Pictures are Ok.
                affineTransformOp = new AffineTransformOp(transform,
                        AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                returnedBufferedImage = affineTransformOp
                        .createCompatibleDestImage(sourceBufferedImage, null);
                // Checks, after the fact the pictures produces by the Canon
                // EOS 30D are not properly resized: colors are 'strange'
                // after resizing.
                uploadPolicy.displayDebug(
                        "getBufferedImage: returnedBufferedImage.getColorModel(): "
                                + sourceBufferedImage.getColorModel()
                                        .toString(), 80);
                uploadPolicy.displayDebug(
                        "getBufferedImage: returnedBufferedImage.getColorModel(): "
                                + sourceBufferedImage.getColorModel()
                                        .toString(), 80);
                affineTransformOp.filter(sourceBufferedImage,
                        returnedBufferedImage);
                affineTransformOp = null;

                returnedBufferedImage.flush();
            }
        } catch (Exception e) {
            throw new JUploadException(e.getClass().getName() + " ("
                    + this.getClass().getName() + ".getBufferedImage()) : "
                    + e.getMessage());
        }

        if (returnedBufferedImage != null
                && this.uploadPolicy.getDebugLevel() >= 60) {
            this.uploadPolicy.displayDebug("getBufferedImage: "
                    + returnedBufferedImage, 60);
            this.uploadPolicy.displayDebug("getBufferedImage: MinX="
                    + returnedBufferedImage.getMinX(), 60);
            this.uploadPolicy.displayDebug("getBufferedImage: MinY="
                    + returnedBufferedImage.getMinY(), 60);
        }

        this.uploadPolicy.displayDebug("getBufferedImage: was "
                + (System.currentTimeMillis() - msGetBufferedImage)
                + " ms long", 100);
        pictureFileData.freeMemory("ImageHelper.getBufferedImage()");
        return returnedBufferedImage;
    }

    /**
     * Implementation of the ImageObserver interface. Used to follow the
     * drawImage progression, and update the applet progress bar.
     * 
     * @param img
     * @param infoflags
     * @param x
     * @param y
     * @param width
     * @param height
     * @return Whether or not the work must go on.
     * 
     */
    public boolean imageUpdate(Image img, int infoflags, int x, int y,
            int width, int height) {
        if ((infoflags & ImageObserver.WIDTH) == ImageObserver.WIDTH) {
            this.progressBarBaseValue = this.uploadPolicy.getApplet()
                    .getUploadPanel().getProgressBar().getValue();
            this.uploadPolicy.displayDebug(
                    "  imageUpdate (start of), progressBar geValue: "
                            + this.progressBarBaseValue, 100);
            int max = this.uploadPolicy.getApplet().getUploadPanel()
                    .getProgressBar().getMaximum();
            this.uploadPolicy.displayDebug(
                    "  imageUpdate (start of), progressBar maximum: " + max,
                    100);
        } else if ((infoflags & ImageObserver.SOMEBITS) == ImageObserver.SOMEBITS) {
            this.nbPixelsRead += width * height;
            int percentage = (int) ((long) this.nbPixelsRead * 100 / this.nbPixelsTotal);
            this.uploadPolicy.getApplet().getUploadPanel().getProgressBar()
                    .setValue(this.progressBarBaseValue + percentage);
            // TODO: drawImage in another thread, to allow repaint of the
            // progress bar ?
            // Current status: the progress bar is only updated ... when
            // draImage returns, that is: when everything is finished. NO
            // interest.
            this.uploadPolicy.getApplet().getUploadPanel().getProgressBar()
                    .repaint(100);
        } else if ((infoflags & ImageObserver.ALLBITS) == ImageObserver.ALLBITS) {
            this.uploadPolicy.displayDebug(
                    "  imageUpdate, total number of pixels: "
                            + this.nbPixelsRead + " read", 100);
        }

        // We want to go on, after these bits
        return true;
    }
}
