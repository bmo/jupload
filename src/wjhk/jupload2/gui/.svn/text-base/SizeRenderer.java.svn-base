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

import wjhk.jupload2.policies.UploadPolicy;

/**
 * Technical class, used to display file sizes. Used in
 * {@link wjhk.jupload2.gui.FilePanelJTable}.
 * @author felfert
 * @version $Revision$
 */
public class SizeRenderer extends DefaultTableCellRenderer {
    /**
     * 
     */
    private static final long serialVersionUID = -2029129064667754146L;

    private static final double gB = 1024L * 1024L * 1024L;

    private static final double mB = 1024L * 1024L;

    private static final double kB = 1024L;

    private String sizeunit_gigabytes;

    private String sizeunit_megabytes;

    private String sizeunit_kilobytes;

    private String sizeunit_bytes;

    /**
     * Creates a new instance.
     * 
     * @param uploadPolicy The policy to be used for providing the translated
     *            unit strings.
     */
    public SizeRenderer(UploadPolicy uploadPolicy) {
        super();
        this.sizeunit_gigabytes = uploadPolicy.getString("unitGigabytes");
        this.sizeunit_megabytes = uploadPolicy.getString("unitMegabytes");
        this.sizeunit_kilobytes = uploadPolicy.getString("unitKilobytes");
        this.sizeunit_bytes = uploadPolicy.getString("unitBytes");
    }

    /**
     * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
     *      java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        if (value instanceof Long) {
            double d = ((Long) value).doubleValue();
            String unit = this.sizeunit_bytes;
            if (d >= gB) {
                d /= gB;
                unit = this.sizeunit_gigabytes;
            } else if (d >= mB) {
                d /= mB;
                unit = this.sizeunit_megabytes;
            } else if (d >= kB) {
                d /= kB;
                unit = this.sizeunit_kilobytes;
            }
            setValue(String.format("%1$,3.2f %2$s", new Double(d), unit));
            super.setHorizontalAlignment(RIGHT);
        }
        return cell;
    }
}
