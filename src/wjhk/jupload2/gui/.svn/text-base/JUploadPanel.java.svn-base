//
// $Id: JUploadPanel.java 303 2007-07-21 07:42:51 +0000 (sam., 21 juil. 2007)
// etienne_sf $
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

import java.awt.Container;
import java.awt.Frame;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import wjhk.jupload2.JUploadApplet;
import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.exception.JUploadExceptionStopAddingFiles;
import wjhk.jupload2.policies.UploadPolicy;
import wjhk.jupload2.policies.UploadPolicyFactory;
import wjhk.jupload2.upload.FileUploadThread;
import wjhk.jupload2.upload.FileUploadThreadFTP;
import wjhk.jupload2.upload.FileUploadThreadHTTP;

/**
 * Main code for the applet (or frame) creation. It contains all creation of
 * necessary elements, and calls to {@link wjhk.jupload2.policies.UploadPolicy}
 * methods to allow easy personalization. <BR>
 * This class remain in the current project structure, even if it's not really
 * used any more. The original reason for this class was that the code would
 * work from within a navigator (an applet) or from a standard java application.
 * <BR>
 * This compatibility is no more maintained, as a lot of code suppose access to
 * navigator parameters. Hope it will be restored...
 * 
 * @author William JinHua Kwong
 * @version $Revision$
 */
public class JUploadPanel extends JPanel implements ActionListener,
        MouseListener {

    /**
     * 
     */
    private static final long serialVersionUID = -1212601012568225757L;

    private static final double gB = 1024L * 1024L * 1024L;

    private static final double mB = 1024L * 1024L;

    private static final double kB = 1024L;

    /** The popup menu of the applet */
    private JUploadPopupMenu jUploadPopupMenu;

    // Timeout at DEFAULT_TIMEOUT milliseconds
    private final static int DEFAULT_TIMEOUT = 100;

    /**
     * The upload status (progressbar) gets updated every (DEFAULT_TIMEOUT *
     * PROGRESS_INTERVAL) ms.
     */
    private final static int PROGRESS_INTERVAL = 10;

    /**
     * The counter for updating the upload status. The upload status (progress
     * bar) gets updated every (DEFAULT_TIMEOUT * PROGRESS_INTERVAL) ms.
     */
    private int update_counter = 0;

    // ------------- VARIABLES ----------------------------------------------

    /**
     * The Drag and Drop listener, that will manage the drop event. All pplet
     * element should register this instance, so that the user see the whole
     * applet as a unique drop target.
     */
    private DnDListener dndListener = null;

    private JButton browseButton = null, removeButton = null,
            removeAllButton = null, uploadButton = null, stopButton = null;

    private JUploadFileChooser fileChooser = null;

    private FilePanel filePanel = null;

    private JProgressBar progressBar = null;

    private JLabel statusLabel = null;

    /**
     * The log window. It's created by {@link JUploadApplet}.
     */
    private JUploadTextArea logWindow = null;

    /**
     * The log window pane contains the log window, and the relevant scroll
     * bars. It's actually this pane that is displayed, as a view on the log
     * window.
     */
    private JScrollPane jLogWindowPane = null;

    /**
     * Used to wait for the upload to finish.
     */
    private Timer timerUpload = new Timer(DEFAULT_TIMEOUT, this);

    /**
     * This 5 second long timer, is used to flush the progress bar ... 5
     * seconds, after the upload finished. The progress bar will get back to 0%!
     */
    private Timer timerAfterUpload = new Timer(5000, this);

    private UploadPolicy uploadPolicy = null;

    protected FileUploadThread fileUploadThread = null;

    // ------------- CONSTRUCTOR --------------------------------------------

    /**
     * Standard constructor.
     * 
     * @param containerParam The container, where all GUI elements are to be
     *            created.
     * @param logWindow The log window that should already have been created.
     *            This allows putting text into it, before the effective
     *            creation of the layout.
     * @param uploadPolicyParam The current UploadPolicy. Null if a new one must
     *            be created.
     * @throws Exception 
     * @see UploadPolicyFactory#getUploadPolicy(wjhk.jupload2.JUploadApplet)
     */
    public JUploadPanel(@SuppressWarnings("unused")
    Container containerParam, JUploadTextArea logWindow,
            UploadPolicy uploadPolicyParam) throws Exception {
        this.logWindow = logWindow;
        this.uploadPolicy = uploadPolicyParam;
        this.jUploadPopupMenu = new JUploadPopupMenu(this.uploadPolicy);

        // First: create standard components.
        createStandardComponents();
        logWindow.addMouseListener(this);

        // Then: display them on the applet
        this.uploadPolicy.addComponentsToJUploadPanel(this);

        // Define the drop target.
        dndListener = new DnDListener(this, uploadPolicy);
        new DropTarget(this, dndListener);
        new DropTarget(this.filePanel.getDropComponent(), dndListener);
        new DropTarget(this.logWindow, dndListener);

        // The JUploadPanel will listen to Mouse messages for the standard
        // component. The current only application of this, it the CTRL+Righ
        // Click, that triggers the popup menu, which allow to switch debug on.
        browseButton.addMouseListener(this);
        removeAllButton.addMouseListener(this);
        removeButton.addMouseListener(this);
        stopButton.addMouseListener(this);
        uploadButton.addMouseListener(this);

        jLogWindowPane.addMouseListener(this);
        logWindow.addMouseListener(this);
        progressBar.addMouseListener(this);
        statusLabel.addMouseListener(this);

        /*
         * // Setup Top Panel setupTopPanel(); // Setup File Panel. //
         * this.filePanel = (null == this.filePanel) ? new //
         * FilePanelTableImp(this, // this.uploadPolicy) : this.filePanel; //
         * this.add((Container) this.filePanel); // Setup Progress Panel.
         * setupProgressPanel(this.uploadButton, this.progressBar,
         * this.stopButton); // Setup status bar setupStatusBar(); // Setup
         * logging window. setupLogWindow();
         */

        // Setup File Chooser.
        try {
            this.fileChooser = uploadPolicyParam.createFileChooser();
        } catch (Exception e) {
            this.uploadPolicy.displayErr(e);
        }
    }

    // ----------------------------------------------------------------------

    /**
     * Creates all components used by the default upload policy. <BR>
     * You can change the component position of these components on the applet,
     * by creating a new upload policy, and override the
     * {@link UploadPolicy#addComponentsToJUploadPanel(JUploadPanel)} method.<BR>
     * You should keep these components, as there content is managed by the
     * internal code of the applet. <BR>
     * <U>Note:</U> this method will create component only if they were not
     * already created. That is only if the relevant attribute contain a null
     * value. If it's not the case, the already created component are keeped
     * unchanged.
     */
    private void createStandardComponents() {
        // -------- JButton browse --------
        if (this.browseButton == null) {
            this.browseButton = new JButton(this.uploadPolicy
                    .getString("buttonBrowse"));
            this.browseButton.setIcon(new ImageIcon(getClass().getResource(
                    "/images/explorer.gif")));
        }
        this.browseButton.addActionListener(this);

        // -------- JButton remove --------
        if (this.removeButton == null) {
            this.removeButton = new JButton(this.uploadPolicy
                    .getString("buttonRemoveSelected"));
            this.removeButton.setIcon(new ImageIcon(getClass().getResource(
                    "/images/recycle.gif")));
        }
        this.removeButton.setEnabled(false);
        this.removeButton.addActionListener(this);

        // -------- JButton removeAll --------
        if (this.removeAllButton == null) {
            this.removeAllButton = new JButton(this.uploadPolicy
                    .getString("buttonRemoveAll"));
            this.removeAllButton.setIcon(new ImageIcon(getClass().getResource(
                    "/images/cross.gif")));
        }
        this.removeAllButton.setEnabled(false);
        this.removeAllButton.addActionListener(this);

        // -------- JButton upload --------
        if (null == this.uploadButton) {
            this.uploadButton = new JButton(this.uploadPolicy
                    .getString("buttonUpload"));
            this.uploadButton.setIcon(new ImageIcon(getClass().getResource(
                    "/images/up.gif")));
        }
        this.uploadButton.setEnabled(false);
        this.uploadButton.addActionListener(this);

        // -------- JProgressBar progress --------
        filePanel = new FilePanelTableImp(this, this.uploadPolicy);

        // -------- JProgressBar progress --------
        if (null == this.progressBar) {
            this.progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
            this.progressBar.setStringPainted(true);
        }

        // -------- JButton stop --------
        if (null == this.stopButton) {
            this.stopButton = new JButton(this.uploadPolicy
                    .getString("buttonStop"));
            this.stopButton.setIcon(new ImageIcon(getClass().getResource(
                    "/images/cross.gif")));
        }
        this.stopButton.setEnabled(false);
        this.stopButton.addActionListener(this);

        // -------- JButton stop --------
        if (this.jLogWindowPane == null) {
            this.jLogWindowPane = new JScrollPane();
            this.jLogWindowPane
                    .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            this.jLogWindowPane
                    .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }
        this.jLogWindowPane.getViewport().add(this.logWindow);
        this.jLogWindowPane.setPreferredSize(null);

        // -------- statusLabel --------
        this.statusLabel = new JLabel("JUpload V" + JUploadApplet.VERSION);
    }

    /**
     * This methods show or hides the logWindow, depending on the following
     * applet parameters. The following conditions must be met, to hide the log
     * window: <DIR>
     * <LI>showLogWindow (must be False)
     * <LI>debugLevel (must be 0 or less) </DIR>
     */
    public void showOrHideLogWindow() {
        // Etienne: we should not more remove and re-add the component, as it
        // will be added at a different place, that where it was placed by
        // the upload policy! (see addComponentsToJUploadPanel)
        if (this.uploadPolicy.getShowLogWindow()
                || this.uploadPolicy.getDebugLevel() > 0) {
            // The log window should be visible.
            //
            this.jLogWindowPane.setVisible(true);
        } else {
            // It should be hidden.
            this.jLogWindowPane.setVisible(false);
        }
        // Let's recalculate the component display
        validate();
    }

    // ----------------------------------------------------------------------
    /**
     * Add files to the current file list.
     */
    protected void addFiles(File[] f, File root) throws JUploadExceptionStopAddingFiles {
        this.filePanel.addFiles(f, root);
        if (0 < this.filePanel.getFilesLength()) {
            this.removeButton.setEnabled(true);
            this.removeAllButton.setEnabled(true);
            this.uploadButton.setEnabled(true);
        }
    }

    // ///////////////////////////////////////////////////////////////////////////////
    // ///////////////// Action methods
    // ///////////////////////////////////////////////////////////////////////////////

    /**
     * Reaction of the panel to a Time event.
     */
    private void actionPerformedTimerExpired() {
        // Time for an update now.
        this.update_counter = 0;
        if (null != this.progressBar
                && (this.fileUploadThread.getStartTime() != 0)) {
            long duration = (System.currentTimeMillis() - this.fileUploadThread
                    .getStartTime()) / 1000;
            double done = this.fileUploadThread.getUploadedLength();
            double total = this.fileUploadThread.getTotalLength();
            double percent;
            double cps;
            long remaining;
            String eta;
            try {
                percent = 100.0 * done / total;
            } catch (ArithmeticException e1) {
                percent = 100;
            }
            try {
                cps = done / duration;
            } catch (ArithmeticException e1) {
                cps = done;
            }
            try {
                remaining = (long) ((total - done) / cps);
                if (remaining > 3600) {
                    eta = String.format(this.uploadPolicy
                            .getString("timefmt_hms"), new Long(
                            remaining / 3600), new Long((remaining / 60) % 60),
                            new Long(remaining % 60));
                } else if (remaining > 60) {
                    eta = String.format(this.uploadPolicy
                            .getString("timefmt_ms"), new Long(remaining / 60),
                            new Long(remaining % 60));
                } else
                    eta = String.format(this.uploadPolicy
                            .getString("timefmt_s"), new Long(remaining));
            } catch (ArithmeticException e1) {
                eta = this.uploadPolicy.getString("timefmt_unknown");
            }
            this.progressBar.setValue((int) percent);
            String unit = this.uploadPolicy.getString("speedunit_b_per_second");
            if (cps >= gB) {
                cps /= gB;
                unit = this.uploadPolicy.getString("speedunit_gb_per_second");
            } else if (cps >= mB) {
                cps /= mB;
                unit = this.uploadPolicy.getString("speedunit_mb_per_second");
            } else if (cps >= kB) {
                cps /= kB;
                unit = this.uploadPolicy.getString("speedunit_kb_per_second");
            }
            String status = String.format(this.uploadPolicy
                    .getString("status_msg"), new Integer((int) percent),
                    new Double(cps), unit, eta);
            this.statusLabel.setText(status);
            this.uploadPolicy.getApplet().getAppletContext().showStatus(status);
        }
    }

    /**
     * The upload is finished, let's react to this interesting event.
     */
    private void actionPerformedUploadFinished() {
        // The upload is finished
        this.uploadPolicy.displayDebug(
                "JUploadPanel: after !fileUploadThread.isAlive()", 60);
        this.timerUpload.stop();
        String svrRet = this.fileUploadThread.getResponseMsg();
        Exception ex = this.fileUploadThread.getException();

        // Restore enable state, as the upload is finished.
        this.stopButton.setEnabled(false);
        this.browseButton.setEnabled(true);

        // Free resources of the upload thread.
        this.fileUploadThread.close();
        this.fileUploadThread = null;

        try {
            this.uploadPolicy.afterUpload(ex, svrRet);
        } catch (JUploadException e1) {
            this.uploadPolicy.displayErr(
                    "error in uploadPolicy.afterUpload (JUploadPanel)", e1);
        }

        boolean haveFiles = (0 < this.filePanel.getFilesLength());
        this.uploadButton.setEnabled(haveFiles);
        this.removeButton.setEnabled(haveFiles);
        this.removeAllButton.setEnabled(haveFiles);

        this.uploadPolicy.getApplet().getAppletContext().showStatus("");
        this.statusLabel.setText(" ");

        // We'll put the progress bar back to 0% (ready for another upload) in 5
        // seconds.
        this.timerAfterUpload.start();
    }

    /**
     * Reaction to the timerAfterUpload timer event. The progress bar get back
     * from 100% to 0%.
     */
    private void actionClearProgressBar() {
        this.progressBar.setValue(0);
        this.progressBar.setString(null);
        this.timerAfterUpload.stop();
    }

    /**
     * Reaction to a click on the browse button.
     */
    public void doBrowse() {
        // Browse clicked
        if (null != this.fileChooser) {
            try {
                int ret = this.fileChooser.showOpenDialog(new Frame());
                if (JFileChooser.APPROVE_OPTION == ret)
                    addFiles(this.fileChooser.getSelectedFiles(),
                            this.fileChooser.getCurrentDirectory());
                // We stop any running task for the JUploadFileView
                this.fileChooser.shutdownNow();
            } catch (Exception ex) {
                this.uploadPolicy.displayErr(ex);
            }
        }
    }

    /**
     * Reaction to a click on the remove button. This method actually removes
     * the selected files in the file list.
     */
    public void doRemove() {
        this.filePanel.removeSelected();
        if (0 >= this.filePanel.getFilesLength()) {
            this.removeButton.setEnabled(false);
            this.removeAllButton.setEnabled(false);
            this.uploadButton.setEnabled(false);
        }
    }

    /**
     * Reaction to a click on the removeAll button. This method actually removes
     * all the files in the file list.
     */
    public void doRemoveAll() {
        this.filePanel.removeAll();
        this.removeButton.setEnabled(false);
        this.removeAllButton.setEnabled(false);
        this.uploadButton.setEnabled(false);
    }

    /**
     * Reaction to a click on the upload button. This method can be called from
     * outside to start the upload.
     */
    public void doStartUpload() {
        // Check that the upload is ready (we ask the uploadPolicy. Then,
        // we'll call beforeUpload for each
        // FileData instance, that exists in allFiles[].

        // ///////////////////////////////////////////////////////////////////////////////////////////////
        // IMPORTANT: It's up to the UploadPolicy to explain to the user
        // that the upload is not ready!
        // ///////////////////////////////////////////////////////////////////////////////////////////////
        if (this.uploadPolicy.isUploadReady()) {
            this.uploadPolicy.beforeUpload();

            this.browseButton.setEnabled(false);
            this.removeButton.setEnabled(false);
            this.removeAllButton.setEnabled(false);
            this.uploadButton.setEnabled(false);
            this.stopButton.setEnabled(true);

            // The FileUploadThread instance depends on the protocol.
            if (this.uploadPolicy.getPostURL().substring(0, 4).equals("ftp:")) {
                // fileUploadThread = new
                // FileUploadThreadFTP(filePanel.getFiles(), uploadPolicy,
                // progress);
                try {
                    this.fileUploadThread = new FileUploadThreadFTP(
                            this.filePanel.getFiles(), this.uploadPolicy,
                            this.progressBar);
                } catch (JUploadException e1) {
                    // Too bad !
                    uploadPolicy.displayErr(e1);
                }
            } else {
                // fileUploadThread = new
                // FileUploadThreadV4(filePanel.getFiles(), uploadPolicy,
                // progress);
                this.fileUploadThread = new FileUploadThreadHTTP(this.filePanel
                        .getFiles(), this.uploadPolicy, this.progressBar);
            }
            this.fileUploadThread.start();

            // Create a timer.
            this.timerUpload.start();
            this.uploadPolicy.displayDebug("Timer started", 60);

        } // if isIploadReady()
    }

    /**
     * Reaction to a click on the stop button. This stops the running on upload.
     * This method can be called from outside to start the upload.
     */
    public void doStopUpload() {
        this.fileUploadThread.stopUpload();
    }

    // ///////////////////////////////////////////////////////////////////////////////
    // ///////////////// Implementation of the ActionListener
    // ///////////////////////////////////////////////////////////////////////////////

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            // Which timer is it ?
            if (this.timerUpload.isRunning()) {
                // timer is expired
                if ((this.update_counter++ > PROGRESS_INTERVAL)
                        || (!this.fileUploadThread.isAlive())) {
                    actionPerformedTimerExpired();
                }
                if (!this.fileUploadThread.isAlive()) {
                    actionPerformedUploadFinished();
                }
            } else if (this.timerAfterUpload.isRunning()) {
                actionClearProgressBar();
            }
            return;
        }
        this.uploadPolicy.displayDebug("Action : " + e.getActionCommand(), 1);
        if (e.getActionCommand() == this.browseButton.getActionCommand()) {
            doBrowse();
        } else if (e.getActionCommand() == this.removeButton.getActionCommand()) {
            // Remove clicked
            doRemove();
        } else if (e.getActionCommand() == this.removeAllButton
                .getActionCommand()) {
            // Remove All clicked
            doRemoveAll();
        } else if (e.getActionCommand() == this.uploadButton.getActionCommand()) {
            // Upload clicked
            doStartUpload();
        } else if (e.getActionCommand() == this.stopButton.getActionCommand()) {
            // We request the thread to stop its job.
            doStopUpload();
        }
        // focus the table. This is necessary in order to enable mouse events
        // for triggering tooltips.
        this.filePanel.focusTable();
    }

    // ///////////////////////////////////////////////////////////////////////////////
    // ///////////////// Implementation of the MouseListener
    // ///////////////////////////////////////////////////////////////////////////////

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        maybeOpenPopupMenu(mouseEvent);
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent mouseEvent) {
        maybeOpenPopupMenu(mouseEvent);
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent mouseEvent) {
        maybeOpenPopupMenu(mouseEvent);
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent mouseEvent) {
        maybeOpenPopupMenu(mouseEvent);
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            // We have a double-click. Let's tell it to the current upload
            // policy...
            this.uploadPolicy.onFileDoubleClicked(filePanel
                    .getFileDataAt(mouseEvent.getPoint()));
        } else {
            maybeOpenPopupMenu(mouseEvent);
        }
    }

    /**
     * This method opens the popup menu, if the mouseEvent is relevant. In this
     * case it returns true. Otherwise, it does nothing and returns false.
     * 
     * @param mouseEvent The triggered mouse event.
     * @return true if the popup menu was opened, false otherwise.
     */
    boolean maybeOpenPopupMenu(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()
                && ((mouseEvent.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK)) {
            if (this.jUploadPopupMenu != null) {
                this.jUploadPopupMenu.show(mouseEvent.getComponent(),
                        mouseEvent.getX(), mouseEvent.getY());
                return true;
            }
        }
        return false;
    }

    /**
     * @return the browseButton
     */
    public JButton getBrowseButton() {
        return browseButton;
    }

    /**
     * @return the dndListener
     */
    public DnDListener getDndListener() {
        return dndListener;
    }

    /**
     * @return the filePanel
     */
    public FilePanel getFilePanel() {
        return this.filePanel;
    }

    /**
     * The component that contains the log window. It is used to display the
     * content of the log window, with the relevant scroll bars.
     * 
     * @return the jLogWindowPane
     */
    public JScrollPane getJLogWindowPane() {
        return jLogWindowPane;
    }

    /**
     * Get the log window, that is: the component where messages (debug, info,
     * error...) are written. You should not use this component directly, but:
     * <UL>
     * <LI>To display messages: use the UploadPolicy.displayXxx methods.
     * <LI>To place this component on the applet, when overriding the
     * {@link UploadPolicy#addComponentsToJUploadPanel(JUploadPanel)} method:
     * use the {@link #getJLogWindowPane()} method instead. The
     * {@link #logWindow} is embbeded in it.
     * </UL>
     * 
     * @return the logWindow
     */
    protected JUploadTextArea getLogWindow() {
        return logWindow;
    }

    /**
     * @return the progressBar
     */
    public JProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * @return the removeAllButton
     */
    public JButton getRemoveAllButton() {
        return removeAllButton;
    }

    /**
     * @return the removeButton
     */
    public JButton getRemoveButton() {
        return removeButton;
    }

    /**
     * @return the statusLabel
     */
    public JLabel getStatusLabel() {
        return statusLabel;
    }

    /**
     * @return the stopButton
     */
    public JButton getStopButton() {
        return stopButton;
    }

    /**
     * @return the uploadButton
     */
    public JButton getUploadButton() {
        return uploadButton;
    }

    /**
     * Standard setter for filePanel.
     * 
     * @param filePanel 
     */
    public void setFilePanel(FilePanel filePanel) {
        this.filePanel = filePanel;
    }

}
