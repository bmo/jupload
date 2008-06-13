//
// $Id: JUploadApplet.java 88 2007-05-02 00:04:52Z /C=DE/ST=Baden-Wuerttemberg/O=ISDN4Linux/OU=Fritz Elfert/CN=svn-felfert@isdn4linux.de/emailAddress=fritz@fritz-elfert.de $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// 
// Created: 2007-04-28
// Creator: felfert
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

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Technical class, used to add tooltips to names. Used in
 * {@link wjhk.jupload2.gui.FilePanelJTable}.
 * 
 * @author felfert
 */
public class NameRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -3096051530077250460L;

    /**
     * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
     *      java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        if (value instanceof String) {
            setToolTipText((String) value);
        }
        return cell;
    }
}
