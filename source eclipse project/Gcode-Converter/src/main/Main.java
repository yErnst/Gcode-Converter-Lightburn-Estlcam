package main;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

	File file;
	File outfile;
	Double cxpos = 0d;
	Double cypos = 0d;
	Double czpos = 0d;
	Double cspos = 0d;
	Double cfpos = 0d;
	boolean isabsolute = true;

	public Main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Gcode Converter v1 - Open");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setCurrentDirectory(new File("C:\\"));

		//start path for filechooser
		if (args.length > 0) {

			File f = new File(args[0]);
			if (f.exists() && f.isDirectory())
				fc.setCurrentDirectory(f);

		}

		fc.setFileFilter(new FileNameExtensionFilter(".nc", "nc"));
		fc.addChoosableFileFilter(new FileNameExtensionFilter(".gcode", "gcode"));
		int rv = fc.showOpenDialog(null);

		if (rv == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			if (file.exists() && file.canRead()) {

				outfile = new File(file.getAbsolutePath() + ".g90.nc");
				if (!outfile.exists()) {

					startconversion();

				} else if (JOptionPane.showConfirmDialog(null, "Willst du wirklich die Ausgabedatei überschreiben?","Datei existiert bereits!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						
					startconversion();

				}

			} else {

				JOptionPane.showMessageDialog(null, "Can not access source file" + file.getAbsolutePath());

			}

		}

	}
	
	

	public void startconversion() {

		long starttime = System.currentTimeMillis();

		try {

			Scanner scg = new Scanner(file);
			FileWriter fw = new FileWriter(outfile);
			fw.write("Lasergravur\n");

			while (scg.hasNextLine()) {

				String line = scg.nextLine();

				if (line.startsWith("G")) {

					line = line.replaceAll("Y", " Y");
					line = line.replaceAll("Z", " Z");
					line = line.replaceAll("S", " S");
					line = line.replaceAll("F", " F");
					String[] splitcode = line.split(" ");

					if (splitcode[0].equalsIgnoreCase("G91")) {

						isabsolute = false;

					} else if (splitcode[0].equalsIgnoreCase("G90")) {

						isabsolute = true;

					} else if (splitcode[0].equalsIgnoreCase("G1") | splitcode[0].equalsIgnoreCase("G0")) {

						fw.write(analysegcodeline(splitcode, isabsolute) + "\n");

					}

				} else {

					fw.write(line + "\n");

				}

			}

			fw.close();
			scg.close();
			JOptionPane.showMessageDialog(null,"Fisished in: " + String.valueOf(System.currentTimeMillis() - starttime) + "ms", "Done!",JOptionPane.PLAIN_MESSAGE);
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Uuups. Something went wrong.");
			System.exit(1);
		}	

	}
	
	

	public String analysegcodeline(String[] splitcode, boolean isabsolute) {
		String gcode = "";
		if (isabsolute) {

			for (String string : splitcode) {

				if (string.startsWith("G")) {
					gcode = string;

				} else if (string.startsWith("X")) {
					cxpos = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				} else if (string.startsWith("Y")) {
					cypos = Double.parseDouble(string.substring(1));
					gcode += " Y" + cypos * -1;
				} else if (string.startsWith("Z")) {
					czpos = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				} else if (string.startsWith("S")) {
					cspos = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				} else if (string.startsWith("F")) {
					cfpos = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				}

			}

			return gcode;

		} else {

			for (String string : splitcode) {

				if (string.startsWith("G")) {
					gcode = string;

				} else if (string.startsWith("X")) {
					cxpos += Double.parseDouble(string.substring(1));
					gcode += " X" + cxpos;
				} else if (string.startsWith("Y")) {
					cypos += Double.parseDouble(string.substring(1));
					gcode += " Y" + cypos * -1;
				} else if (string.startsWith("Z")) {
					czpos += Double.parseDouble(string.substring(1));
					gcode += " Z" + czpos;
				} else if (string.startsWith("S")) {
					cspos = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				} else if (string.startsWith("F")) {
					cfpos = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				}

			}

			return gcode;

		}

	}
	
	

	public static void main(String[] args) {
		new Main(args);

	}

}
