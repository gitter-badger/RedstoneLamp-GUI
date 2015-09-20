package net.redstonelamp.gui.activity;

/*
 * This file is part of RedstoneLamp GUI.
 *
 * RedstoneLamp GUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp GUI.  If not, see <http://www.gnu.org/licenses/>.
 */

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.io.File;

public class ServerActivity extends Activity{
	private File file;
	private final File LOCK;

	public ServerActivity(File file){
		super("RedstoneLamp");
		this.file = file;
		LOCK = new File(file, "RedstoneLamp-GUI.LOCK");
		if(LOCK.exists()){
			String[] options = {
					"Yes, continue loading the server.",
					"No, close the program.",
			};
			String message = "Another server is running in this directory, or it crashed. " +
					"Strange issues may occur if two servers are running in the same directory. " +
					"Are you sure to continue?";
			JCheckBox dontAskAgain = new JCheckBox("Don't ask again");
			int result = JOptionPane.showOptionDialog(this,
					new Object[]{options, dontAskAgain}, "Continue?",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[0]);
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
}
