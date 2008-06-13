//
// $Id: JUploadPanel.java 303 2007-07-21 07:42:51 +0000 (sam., 21 juil. 2007)
// etienne_sf $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// 
// Created: ?
// Creator: etienne_sf
// Last modified: $Date: 2007-10-08 10:02:41 +0200 (lun., 08 oct. 2007) $
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import wjhk.jupload2.policies.UploadPolicy;

/**
 * Global applet popup menu. It currently contains only the debug on/off menu
 * entry.
 */

final class JUploadPopupMenu extends JPopupMenu implements ItemListener {

    /** A generated serialVersionUID */
    private static final long serialVersionUID = -5473337111643079720L;

    /**
     * Identifies the menu item that will set debug mode on or off (on means:
     * debugLevel=100)
     */
    JCheckBoxMenuItem cbMenuItemDebugOnOff = null;

    /**
     * The current upload policy.
     */
    private UploadPolicy uploadPolicy;

    JUploadPopupMenu(UploadPolicy uploadPolicy) {
        this.uploadPolicy = uploadPolicy;

        // ////////////////////////////////////////////////////////////////////////
        // Creation of the menu items
        // ////////////////////////////////////////////////////////////////////////
        // First: debug on or off
        this.cbMenuItemDebugOnOff = new JCheckBoxMenuItem("Debug on");
        this.cbMenuItemDebugOnOff
                .setState(this.uploadPolicy.getDebugLevel() == 100);
        add(this.cbMenuItemDebugOnOff);
        // ////////////////////////////////////////////////////////////////////////
        this.cbMenuItemDebugOnOff.addItemListener(this);
    }

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (this.cbMenuItemDebugOnOff == e.getItem()) {
            this.uploadPolicy.setDebugLevel((this.cbMenuItemDebugOnOff
                    .isSelected() ? 100 : 0));
        }
    }
}
