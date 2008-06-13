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

import java.awt.Color;

import javax.swing.JTextArea;

/**
 * This class represents the text area for debug output.
 */
public class JUploadTextArea extends JTextArea {

    /** Generated serialVersionUID */
    private static final long serialVersionUID = -6037767344615468632L;

    /**
     * Constructs a new empty TextArea with the specified number of rows and
     * columns.
     * 
     * @param rows The desired number of text rows (lines).
     * @param columns The desired number of columns.
     */
    public JUploadTextArea(int rows, int columns) {
        super(rows, columns);
        setBackground(new Color(255, 255, 203));
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    /**
     * @see javax.swing.JTextArea#append(java.lang.String)
     */
    @Override
    public final void append(String str) {
        super.append(str);
        setCaretPosition(getText().length());
    }
}