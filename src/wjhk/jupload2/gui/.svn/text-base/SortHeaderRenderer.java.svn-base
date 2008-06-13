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

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Techical class, to display the column headers, for column that may be sorted.
 */
public class SortHeaderRenderer extends DefaultTableCellRenderer {
    /**
     * 
     */
    private static final long serialVersionUID = -4104776293873798189L;

    private static final Icon NONSORTED = new SortArrowIcon(SortArrowIcon.NONE);

    private static final Icon ASCENDING = new SortArrowIcon(
            SortArrowIcon.ASCENDING);

    private static final Icon DESCENDING = new SortArrowIcon(
            SortArrowIcon.DESCENDING);

    /**
     * Creates a new instance.
     */
    public SortHeaderRenderer() {
        setHorizontalTextPosition(LEFT);
        setHorizontalAlignment(CENTER);
    }

    /**
     * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
     *      java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            @SuppressWarnings("unused")
            boolean isSelected, @SuppressWarnings("unused")
            boolean hasFocus, @SuppressWarnings("unused")
            int row, int col) {
        int index = -1;
        boolean ascending = true;
        if (table instanceof FilePanelJTable) {
            FilePanelJTable sortTable = (FilePanelJTable) table;
            index = sortTable.getSortedColumnIndex();
            ascending = sortTable.isSortedColumnAscending();
        }
        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                setForeground(header.getForeground());
                setBackground(header.getBackground());
                setFont(header.getFont());
            }
        }
        Icon icon = ascending ? ASCENDING : DESCENDING;
        setIcon(col == index ? icon : NONSORTED);
        setText((value == null) ? "" : value.toString());
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        return this;
    }
}
