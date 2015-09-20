package net.redstonelamp.gui;

/*
 * This file is part of RedstoneLamp-GUI.
 *
 * RedstoneLamp-GUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp-GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp-GUI.  If not, see <http://www.gnu.org/licenses/>.
 */

import net.redstonelamp.gui.activity.Activity;
import net.redstonelamp.gui.activity.ServerActivity;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;

public class RedstoneLampGUI{
	private static Activity currentRoot = null;
	public static void main(String[] args){
		JFrame frame = new JFrame("RedstoneLamp");
		frame.setLayout(new GridLayout(2, 1));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JButton openButton = new JButton("Load server");
		openButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser(new File("."));
			chooser.setDialogTitle("Select RedstoneLamp server home");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			int action = chooser.showOpenDialog(frame);
			if(action == JFileChooser.APPROVE_OPTION){
				frame.dispose();
				currentRoot = new ServerActivity(chooser.getSelectedFile());
			}
		});
		JLabel label = new JLabel("RedstoneLamp");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		frame.add(label);
		JPanel lowPanel = new JPanel();
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		lowPanel.add(left);
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		lowPanel.add(right);
		right.add(openButton);
		right.add(new JButton("test"));
		frame.add(lowPanel);
		frame.pack();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dimension.width / 2 - frame.getSize().width / 2, dimension.height / 2 - frame.getSize().height / 2);
		frame.setVisible(true);
	}

	public static Activity getCurrentRootActivity(){
		return currentRoot;
	}
}
