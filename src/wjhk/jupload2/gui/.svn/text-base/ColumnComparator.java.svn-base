//
// $Id: ColumnComparator.java 95 2007-05-02 03:27:05Z
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

import java.util.Comparator;
import wjhk.jupload2.filedata.FileData;

/**
 * Technical class, used to sort rows in the
 * wjhk.jupload2.gui.FilePanelDataModel2 class.
 */
public class ColumnComparator implements Comparator<FileData> {
    protected int index;

    protected boolean ascending;

    /**
     * Creates a new instance.
     * 
     * @param index The column index of the table data to be compared
     * @param ascending Specifies the sort order.
     */
    public ColumnComparator(int index, boolean ascending) {
        this.index = index;
        this.ascending = ascending;
    }

    /**
     * @param one 
     * @param two 
     * @return -1, 0 or 1, as usual.
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public int compare(FileData one, FileData two) {
        // if (one instanceof DefaultFileData && two instanceof DefaultFileData)
        // {
        Object oOne;
        Object oTwo;
        switch (this.index) {
            case FilePanelDataModel2.COLINDEX_NAME:
                oOne = ((FileData) one).getFileName();
                oTwo = ((FileData) two).getFileName();
                break;
            case FilePanelDataModel2.COLINDEX_SIZE:
                oOne = new Long(((FileData) one).getFileLength());
                oTwo = new Long(((FileData) two).getFileLength());
                break;
            case FilePanelDataModel2.COLINDEX_DIRECTORY:
                oOne = ((FileData) one).getDirectory();
                oTwo = ((FileData) two).getDirectory();
                break;
            case FilePanelDataModel2.COLINDEX_MODIFIED:
                oOne = ((FileData) one).getLastModified();
                oTwo = ((FileData) two).getLastModified();
                break;
            default:
                return 0;
        }
        if (oOne instanceof Comparable && oTwo instanceof Comparable) {
            return ((Comparable) oOne).compareTo(oTwo)
                    * (this.ascending ? 1 : -1);
        }
        // }
        return 0;
    }
}
