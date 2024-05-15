package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import gui.renderShapes.Line;
import main.Main;

public class Converter {

	private Double currentxpos = 0d;
	private Double currentypos = 0d;
	private Double currentzpos = 0d;
	private Double currentsspeed = 0d;
	private Double currentfspeed = 0d;
	private Double lastxpos = 0d;
	private Double lastypos = 0d;
	// private Double lastzpos = 0d;
	// private Double lastsspeed = 0d;
	// private Double lastfspeed = 0d;
	private Double minX = 0d;
	private Double minY = 0d;
	private Double maxX = 0d;
	private Double maxY = 0d;
	private Double maxS = 0d;
	private Double estimatedFeedTime = 0d;
	private Double totalLineLength = 0d;
	private ArrayList<Line> lines = new ArrayList<Line>();
	private ArrayList<String> gcodelines = new ArrayList<String>();
	private Hashtable<Integer, Integer> codeLineToLine = new Hashtable<Integer, Integer>();

	private boolean modeabsolutegcode = true;
	private Project p;

	public double elapsedseconds = 0;

	public Converter(Project p) {
		this.p = p;
	}

	public void convert() {
		long start = System.nanoTime();
		p.converted = false;
		lines.clear();
		codeLineToLine.clear();
		estimatedFeedTime = 0d;
		currentxpos = 0d;
		currentypos = 0d;
		currentzpos = 0d;
		currentsspeed = 0d;
		currentfspeed = 0d;
		lastxpos = 0d;
		lastypos = 0d;
		// lastzpos = 0d;
		// lastsspeed = 0d;
		// lastfspeed = 0d;
		totalLineLength = 0d;
		minX = 0d;
		minY = 0d;
		maxX = 0d;
		maxY = 0d;
		maxS = 0d;
		gcodelines.clear();
		if (!p.projectFile.exists())
			return;
		int linecount = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(p.projectFile));

			convertline("Lasergravur");
			convertline("G0 Z0");

			String line;
			while ((line = reader.readLine()) != null) {
				convertline(line);
				linecount++;
			}
			reader.close();
			p.gcode = gcodelines;
			p.codeLineToLine = codeLineToLine;
			p.lines = lines;
			p.minX = minX;
			p.minY = minY;
			p.maxX = maxX;
			p.maxY = maxY;
			p.maxS = maxS;
			p.totalLineLength = totalLineLength;
			p.estimatedFeedTime = estimatedFeedTime * 1.02; // +2% wegen Beschleunigungsrampen in Estlcam. Ob dieser Wert stimmt ist nicht
															// getestet.
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error on line " + linecount + " " + e.getMessage());
		}
		p.converted = true;
		long stop = System.nanoTime();
		elapsedseconds = (stop - start) / 1_000_000_000.0;
	}

	private void addGcodeLine(String s) {
		gcodelines.add(s);
		codeLineToLine.put(gcodelines.size() - 1, lines.size());
	}

	private void convertline(String gcodeline) throws Exception {

		if (gcodeline.toUpperCase().startsWith("G")) {
			gcodeline = gcodeline.toUpperCase();
			gcodeline = gcodeline.replace("X", " X");
			gcodeline = gcodeline.replace("Y", " Y");
			gcodeline = gcodeline.replace("Z", " Z");
			gcodeline = gcodeline.replace("S", " S");
			gcodeline = gcodeline.replace("F", " F");
			gcodeline = gcodeline.replace("  ", " ");
			String[] linesinLine = gcodeline.split("G");
			for (int i = 1; i < linesinLine.length; i++) {
				processSplitLine(("G" + linesinLine[i]).split(" "));
				updateMinMax();
			}

		} else {

			if (!gcodeline.startsWith("M2")) {
				addGcodeLine(gcodeline);
			}

		}

	}

	private void updateMinMax() {
		if (currentxpos > maxX)
			maxX = currentxpos;
		if (currentypos > maxY)
			maxY = currentypos;
		if (currentxpos < minX)
			minX = currentxpos;
		if (currentypos < minY)
			minY = currentypos;
		if(currentsspeed > maxS)
			maxS = currentsspeed;
	}

	private void processSplitLine(String[] splitgcodeline) throws Exception {
		if (splitgcodeline[0].equalsIgnoreCase("G90")) {
			modeabsolutegcode = true;
		} else if (splitgcodeline[0].equalsIgnoreCase("G91")) {
			modeabsolutegcode = false;
		} else if (splitgcodeline[0].equalsIgnoreCase("G1") || splitgcodeline[0].equalsIgnoreCase("G01") || splitgcodeline[0].equalsIgnoreCase("G0") || splitgcodeline[0].equalsIgnoreCase("G00")) {
			ConvertLinearMove(splitgcodeline);
		} else {
			System.out.println("Unknown gcode " + String.join(" ", splitgcodeline));
			// addGcodeLine(String.join(" ", splitgcodeline));
		}
	}

	private void ConvertLinearMove(String[] splitgcodeline) throws Exception {
		String gcode = "";
		boolean isGZero = false;
		if (modeabsolutegcode) {

			for (String string : splitgcodeline) {

				if (string.startsWith("G")) {
					gcode = string;
					if (string.equalsIgnoreCase("G0") || string.equalsIgnoreCase("G00"))
						isGZero = true;

				} else if (string.startsWith("X")) {
					currentxpos = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				} else if (string.startsWith("Y")) {
					currentypos = Double.parseDouble(string.substring(1));
					gcode += " Y" + Main.df.format(currentypos * -1);
				} else if (string.startsWith("Z")) {
					currentzpos = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				} else if (string.startsWith("S")) {
					currentsspeed = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				} else if (string.startsWith("F")) {
					currentfspeed = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				} else {
					gcode += " " + string;
				}

			}
			if (gcode.length() > 3) {
				addline(isGZero);
				addGcodeLine(gcode);
			}

		} else {

			for (String string : splitgcodeline) {

				if (string.startsWith("G")) {
					gcode = string;
					if (string.equalsIgnoreCase("G0") || string.equalsIgnoreCase("G00"))
						isGZero = true;

				} else if (string.startsWith("X")) {
					currentxpos += Double.parseDouble(string.substring(1));
					gcode += " X" + Main.df.format(currentxpos);
				} else if (string.startsWith("Y")) {
					currentypos += Double.parseDouble(string.substring(1));
					gcode += " Y" + Main.df.format(currentypos * -1);
				} else if (string.startsWith("Z")) {
					currentzpos += Double.parseDouble(string.substring(1));
					gcode += " Z" + Main.df.format(currentzpos);
				} else if (string.startsWith("S")) {
					currentsspeed = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				} else if (string.startsWith("F")) {
					currentfspeed = Double.parseDouble(string.substring(1));
					gcode += " " + string;
				} else {
					gcode += " " + string;
				}

			}
			if (gcode.length() > 3) {
				addline(isGZero);
				addGcodeLine(gcode);
			}

		}
	}

	private void addline(boolean isG0) {
		double length = getLength(lastxpos, lastypos, currentxpos, currentypos);
		totalLineLength += length;
		if (isG0) {
			lines.add(new Line(lastxpos, lastypos, currentxpos, currentypos, currentsspeed, 2, length, true));
		} else {
			lines.add(new Line(lastxpos, lastypos, currentxpos, currentypos, currentsspeed, 2, length, false));
			if (currentfspeed != 0)
				estimatedFeedTime += length / currentfspeed;
		}
		lastxpos = currentxpos;
		lastypos = currentypos;
		// lastzpos = currentzpos;
		// lastsspeed = currentsspeed;
		// lastfspeed = currentfspeed;
	}

	private double getLength(double x1, double y1, double x2, double y2) {
		double a = x1 - x2;
		double b = y1 - y2;
		// return 0d;
		return Math.sqrt(a * a + b * b);
	}

}
