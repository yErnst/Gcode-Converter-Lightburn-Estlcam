package project;

import java.awt.Desktop;
import java.awt.geom.Point2D;
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
	public int maxS = 0;
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

	public Integer getLastLineFromGcodeLine(Integer linenum) {
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
		System.out.println("Projekt: " + getProjectFile().getName() + " konvertiert in " + Main.df.format(getConversionTime()) + "s\nVorschubweg ges. " + Main.df.format(totalLineLength) + "mm\nVorschubzeit ca.: " + Main.df.format(estimatedFeedTime) + "min\nLinien: " + getLinesList().size() + "\nGr��e: " + Main.df.format(maxX - minX) + "mm, " + Main.df.format(maxY - minY) + "mm\n");
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
		//JOptionPane.showMessageDialog(null, "Gespeichert unter " + getSaveFile().getAbsolutePath());
		try {
			if (converted)
				Desktop.getDesktop().open(getSaveFile());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Bitte lege Estlcam als Standardanwendung f�r Dateien mit der .nc endung fest");
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
				// JOptionPane.showMessageDialog(null, "Gespeichert unter " +
				// f.getAbsolutePath());
				return true;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Keine Daten vorhanden. Bitte �ffne zuerst ein CNC Program.");
		}
		return false;

	}

	public Line getClosesVisableLineToPoint(Point2D p) {
		if(lines.isEmpty())return null;
		double mindistace = Double.MAX_VALUE;
		Line closestline = null;
		int i = 0;
		for(Line l : lines){
			Point2D closespoint = getClosestPointOnSegment(l.x1, l.y1, l.x2, l.y2, p.getX(), p.getY());
			if(closespoint != null) {
				double distance = Point2D.distanceSq(closespoint.getX(), closespoint.getY(), p.getX(), p.getY());
				if (distance < mindistace && i < visibleLineCount) {
					mindistace = distance;
					closestline = l;
				}
			}
			i++;
		}
		if(mindistace <= 1) {
			return closestline;
		}
		return null;
	}

	private static Point2D getClosestPointOnSegment(double sx1, double sy1, double sx2, double sy2, double px, double py) {
		double xDelta = sx2 - sx1;
		double yDelta = sy2 - sy1;

		if ((xDelta == 0) && (yDelta == 0)) {
			return null;
		}

		double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Point2D closestPoint;
		if (u < 0) {
			closestPoint = new Point2D.Double(sx1, sy1);
		} else if (u > 1) {
			closestPoint = new Point2D.Double(sx2, sy2);
		} else {
			closestPoint = new Point2D.Double(sx1 + u * xDelta, sy1 + u * yDelta);
		}
		return closestPoint;
	}

}
