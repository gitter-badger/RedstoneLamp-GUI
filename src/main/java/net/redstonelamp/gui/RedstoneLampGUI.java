package net.redstonelamp.gui;

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

import net.redstonelamp.gui.activity.Activity;
import net.redstonelamp.gui.activity.ServerActivity;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RedstoneLampGUI{
	private static Activity currentRoot = null;

	public static Activity getCurrentRootActivity(){
		return currentRoot;
	}

	public static void main(String[] args){
		JFrame frame = new JFrame("RedstoneLamp");
		frame.setLayout(new GridLayout(2, 1));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
		JButton openButton = new JButton("Open server at...");
		openButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser(new File("."));
			chooser.setDialogTitle("Select RedstoneLamp server home");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			int action = chooser.showOpenDialog(frame);
			if(action == JFileChooser.APPROVE_OPTION){
				File selected = chooser.getSelectedFile();
				File jar = new File("RedstoneLamp.jar");
				if(!jar.isFile()){
					int result = JOptionPane.showConfirmDialog(frame, "Could not find RedstoneLamp installation. " +
							"Would you like to install RedstoneLamp there?");
					if(result == JOptionPane.YES_OPTION){
						installCallback(frame, selected);
					}
					return;
				}
				frame.dispose();
				addHistory(selected);
				currentRoot = new ServerActivity(selected);
			}
		});
		right.add(openButton);
		JButton installButton = new JButton("Install server at...");
		installButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser(".");
			chooser.setDialogTitle("Select directory to install server in");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			int action = chooser.showSaveDialog(frame);
			if(action == JFileChooser.APPROVE_OPTION){
				File selected = chooser.getSelectedFile();
				File jar = new File("RedstoneLamp.jar");
				if(jar.isFile()){
					int result = JOptionPane.showConfirmDialog(frame, "A RedstoneLamp jar installation is present. " +
							"Are you sure you want to reinstall RedstoneLamp there?");
					if(result == JOptionPane.NO_OPTION){
						frame.dispose();
						addHistory(selected);
						currentRoot = new ServerActivity(selected);
						return;
					}
				}
				installCallback(frame, selected);
			}
		});
		frame.add(lowPanel);
		frame.pack();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dimension.width / 2 - frame.getSize().width / 2, dimension.height / 2 - frame.getSize().height / 2);
		frame.setVisible(true);
	}

	private static void installCallback(JFrame frame, File selected){
		JDialog dialog = new JDialog(frame);
		JLabel status = new JLabel("Downloading build...");
		JProgressBar progress = new JProgressBar();
		dialog.pack();
		dialog.setVisible(true);
		try{
			URL url = new URL("http://download.redstonelamp.net/?file=LatestBuild");
			InputStream is = url.openStream();
			int size = is.available();
			progress.setMinimum(0);
			progress.setMaximum(size);
			int size2 = size;
			OutputStream os = new FileOutputStream(new File(selected, "RedstoneLamp.jar"));
			while(size2 > 0){
				int length = Math.min(4096, size2);
				byte[] buffer = new byte[length];
				size2 -= length;
				is.read(buffer);
				progress.setValue(size2);
				os.write(buffer);
			}
			is.close();
			os.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private static List<File> getServerDirectories(){
		File config = new File("redstonelamp-gui-config.json");
		if(!config.isFile()){
			try{
				InputStream is = RedstoneLampGUI.class.getResourceAsStream("redfstonelamp-gui-config.json");
				OutputStream os = new FileOutputStream(config);
				IOUtils.copy(is, os);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		List<File> out = new ArrayList<>();
		StringBuilder configContents = new StringBuilder();
		try{
			InputStream is = new FileInputStream(config);
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			is.close();
			try{
				JSONObject json = new JSONObject(new String(buffer));
				JSONArray array = json.getJSONArray("history");
				for(int i = 0; i < array.length(); i++){
					JSONObject object = array.getJSONObject(i);
					out.add(new File(object.getString("path")));
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return out;
	}

	private static void addHistory(File file){
		File config = new File("redstonelamp-gui-config.json");
		if(!config.isFile()){
			try{
				InputStream is = RedstoneLampGUI.class.getResourceAsStream("redfstonelamp-gui-config.json");
				OutputStream os = new FileOutputStream(config);
				IOUtils.copy(is, os);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		try{
			InputStream is = new FileInputStream(config);
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			is.close();
			try{
				JSONObject json = new JSONObject(new String(buffer));
				JSONArray array = json.getJSONArray("history");
				JSONObject object = new JSONObject();
				object.put("path", file.getCanonicalPath());
				array.put(object);
			}catch(JSONException e){
				e.printStackTrace();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
