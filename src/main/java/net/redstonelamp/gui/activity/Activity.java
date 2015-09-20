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

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Activity extends JFrame{
	private Activity parent = null;
	private Activity child = null;

	public Activity(String title){
		this(title, null);
	}

	public Activity(String title, Activity parent){
		super(title);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent e){
				if(parent != null){
					parent.requestFocus();
				}
			}

			@Override
			public void windowGainedFocus(WindowEvent e){
				if(child != null){
					child.requestFocus();
				}
			}
		});
	}

	public Activity getChild(){
		return child;
	}
}
