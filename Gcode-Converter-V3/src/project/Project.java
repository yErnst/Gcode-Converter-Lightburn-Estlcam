package project;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import gui.renderShapes.Line;
import main.Main;

public class Project {

	protected final File projectFile;
	public boolean converted;
	protected ArrayList<Line> lines = new ArrayList<Line>();
	protected ArrayList<String> gcode = new ArrayList<String>();
	protected Hashtable<Integer, Integer> codeLineToLine = new Hashtable<Integer, Integer>();
	public Double minX = 0d;
	public Double minY = 0d;
	public Double maxX = 0d;
	public Double maxY = 0d;
	public Double maxS = 0d;
	public Double totalLineLength = 0d;
	public Double estimatedFeedTime = 0d;
	private Converter projectConverter;
	public int visibleLineCount = 0;

	public Project(File file) {
		this.projectFile = file;
		projectConverter = new Converter(this);
	}

	public File getProjectFile() {
		return projectFile;
	}
	
	public Integer getLastLineFromGcodeLine(Integer linenum){
		return codeLineToLine.get(linenum);
	}

	public ArrayList<Line> getLinesList() {
		if (converted)
			return lines;
		return new ArrayList<Line>();
	}
	
	public ArrayList<String> getGcode() {
		if (converted)
			return gcode;
		return new ArrayList<String>();
	}

	public void addline(Line line) {
		lines.add(line);
	}

	public void convert() {
		projectConverter.convert();
		visibleLineCount = lines.size();
		System.out.println("Projekt: " + getProjectFile().getName() + " konvertiert in " + Main.df.format(getConversionTime()) + "s\nVorschubweg ges. "+Main.df.format(totalLineLength)+"mm\nVorschubzeit ca.: "+Main.df.format(estimatedFeedTime)+"min\nLinien: " + getLinesList().size() + "\nGröße: " + Main.df.format(maxX - minX) + "mm, " + Main.df.format(maxY - minY) + "mm\n");
		Runtime.getRuntime().gc();
	}

	public double getConversionTime() {
		return projectConverter.elapsedseconds;
	}

	public File getSaveFile() {
		return new File(projectFile.getAbsolutePath() + ".g90.nc");
	}

	public void openWithEstlcam() {
		save(getSaveFile());
		try {
			if (converted)
				Desktop.getDesktop().open(getSaveFile());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Bitte lege Estlcam als Standardanwendung für Dateien mit der .nc endung fest");
		}
	}

	public boolean save(File f) {
		if (converted && gcode.size() != 0) {

			try {
				FileWriter fw = new FileWriter(f);
				for (String line : gcode) {
					fw.write(line + "\n");
				}
				fw.close();
				JOptionPane.showMessageDialog(null, "Gespeichert unter " + f.getAbsolutePath());
				return true;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Keine Daten vorhanden. Bitte öffne zuerst ein CNC Program.");
		}
		return false;

	}

}
