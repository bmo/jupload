//
// $Id: FilePanelJTable.java 95 2007-05-02 03:27:05 +0000 (mer., 02 mai 2007)
// /C=DE/ST=Baden-Wuerttemberg/O=ISDN4Linux/OU=Fritz
// Elfert/CN=svn-felfert@isdn4linux.de/emailAddress=fritz@fritz-elfert.de $
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

import java.util.Date;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import wjhk.jupload2.policies.UploadPolicy;

/**
 * This class is the JTable that display file information to the users. Data is
 * handled by the wjhk.jupload2.gui.FilePanelDataModel2 class.
 */
public class FilePanelJTable extends JTable implements MouseListener {
    /**
     * 
     */
    private static final long serialVersionUID = 5422667664740339798L;

    protected int sortedColumnIndex = -1;

    protected boolean sortedColumnAscending = true;

    // The current UploadPolicy
    UploadPolicy uploadPolicy;

    // The current DataModel
    FilePanelDataModel2 filePanelDataModel;

    /**
     * Creates a new instance.
     * 
     * @param jup The parent upload panel.
     * @param uploadPolicy The policy for retrieval of various settings.
     */
    public FilePanelJTable(JUploadPanel jup, UploadPolicy uploadPolicy) {
        this.uploadPolicy = uploadPolicy;

        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setDefaultRenderer(Long.class, new SizeRenderer(uploadPolicy));
        setDefaultRenderer(Date.class, new DateRenderer(uploadPolicy));
        setDefaultRenderer(String.class, new NameRenderer());

        // setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new SortHeaderRenderer());
        // We add the mouse listener on the header (to manage column sorts) and
        // on the main part (to manage
        // the contextual popup menu)
        header.addMouseListener(this);
        addMouseListener(jup);
    }

    /**
     * Set the model. Forces the model to be a FilePanelDataModel2. This method
     * calls the {@link JTable#setModel(javax.swing.table.TableModel)} method.
     * 
     * @param filePanelDataModel
     */
    public void setModel(FilePanelDataModel2 filePanelDataModel) {
        super.setModel(filePanelDataModel);
        this.filePanelDataModel = filePanelDataModel;
    }

    /**
     * Retrieve the currently sorted column.
     * 
     * @return the index of the currently sorted column.
     */
    public int getSortedColumnIndex() {
        return this.sortedColumnIndex;
    }

    /**
     * Retrieve the current sort order.
     * 
     * @return true, if the current sort order is ascending, false otherwise.
     */
    public boolean isSortedColumnAscending() {
        return this.sortedColumnAscending;
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent event) {
        // Displays the contextual menu ?
        this.uploadPolicy.getApplet().getUploadPanel()
                .maybeOpenPopupMenu(event);
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent event) {
        // Displays the contextual menu ?
        this.uploadPolicy.getApplet().getUploadPanel()
                .maybeOpenPopupMenu(event);
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent event) {
        // Is this a double-click ?
        if (event.getClickCount() == 2) {
            // Let's open the 'big' preview, if we're in picture mode.
            // We should have one selected row. Let's check it, you never knows
            // ! ;-)
            int selectedRow = getSelectedRow();
            if (selectedRow >= 0) {
                this.uploadPolicy.onFileSelected(this.filePanelDataModel
                        .getFileDataAt(selectedRow));
            }

            // FIXME The double click is not received here, unless on it is on
            // the column header

        } else if (!this.uploadPolicy.getApplet().getUploadPanel()
                .maybeOpenPopupMenu(event)) {
            // We did not open the displays the contextual menu. So we do what
            // we have to do: sort the clicked column
            TableColumnModel colModel = getColumnModel();
            int index = colModel.getColumnIndexAtX(event.getX());
            int modelIndex = colModel.getColumn(index).getModelIndex();

            FilePanelDataModel2 model = (FilePanelDataModel2) getModel();
            if (model.isSortable(modelIndex)) {
                if (this.sortedColumnIndex == index) {
                    this.sortedColumnAscending = !this.sortedColumnAscending;
                }
                this.sortedColumnIndex = index;

                model.sortColumn(modelIndex, this.sortedColumnAscending);
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(@SuppressWarnings("unused")
    MouseEvent event) {
        // Nothing to do.
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(@SuppressWarnings("unused")
    MouseEvent event) {
        // Nothing to do.
    }

    /**
     * @see javax.swing.JTable#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        // Ignore extra messages, and no action before initialization.
        if (e.getValueIsAdjusting() || this.uploadPolicy == null)
            return;

        //
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (lsm.isSelectionEmpty()) {
            this.uploadPolicy.onFileSelected(null);
        } else {
            int selectedRow = lsm.getMinSelectionIndex();
            // if one file is selected, we let the current upload policy reacts.
            // Otherwise, we don't do anything.
            if (selectedRow == lsm.getMaxSelectionIndex()) {
                Cursor previousCursor = this.uploadPolicy.setWaitCursor();
                this.uploadPolicy.onFileSelected(this.filePanelDataModel
                        .getFileDataAt(selectedRow));
                this.uploadPolicy.setCursor(previousCursor);
            }
        }
    }
}
