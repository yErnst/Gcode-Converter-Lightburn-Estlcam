package main;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.GuiWindow;
import gui.GuiWindowBasic;
import project.Project;

public class Main {

	public static String version = "V3";
	public final static String cmdhelp = "Start Argumente:\nAlle angaben hinter = müssen in \" erfolgen.\n-nogui Verwende die alte Ansicht. Notwendig in verbindung mit -in und -out.\n-p=    Gibt den Projektordner an.\n-in=   Gibt eine Datei zum konvertieren an. Es kommt kein Auswahlfenster.\n-out= Gibt einen abweichende Datei/Pfard zum speichern an. Falls nicht definiert wird die konvertierte Datei wie gewohnt mit der .g90.nc endung gespeichert.";
	public static NumberFormat nf = NumberFormat.getInstance(Locale.US);
	public static DecimalFormat df = (DecimalFormat) nf;
	public static GuiWindow window;
	public static GuiWindowBasic windowbasic;
	public static int ProjectExplorerlimit = 20000;
	public static Double javaCFVersion;
	public static String os;
	static File f = new File("");

	// switches
	static boolean nogui = false;
	static String infilepath = "";
	static String outfilepath = "";
	public static boolean basicUI = false;

	public static void main(String[] args) {
		df.setMinimumIntegerDigits(1);
		df.setMaximumFractionDigits(4);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		javaCFVersion = Double.parseDouble(System.getProperty("java.class.version"));
		os = System.getProperty("os.name");

		for (String s : args) {

			// Projektroot setzten
			if (s.startsWith("-p=")) {
				File file = new File(s.substring(3));
				if (file.exists() && file.isDirectory()) {
					f = file;
				}
			}

			// Alte variante ohne erweiterter GUI verwenden
			if (s.equalsIgnoreCase("-nogui")) {
				nogui = true;
			}
			
			//Zeigt die Verwendung der Kommandozeilenparameter
			if (s.equalsIgnoreCase("-help") || s.equalsIgnoreCase("/?")) {
				JOptionPane.showMessageDialog(null, cmdhelp);
				return;
			}

			// Direkte umwandlung einer Datei
			if (s.startsWith("-in=")) {
				infilepath = s.substring(4);
			}
			// Direkte umwandlung einer Datei. Setzt die Ausgabedatei. Wenn nicht definiert
			// oder nicht verfügbar, wird die umgewandelte Datei wie gewohnt neben die
			// Originaldatei abgelegt.
			if (s.startsWith("-out=")) {
				outfilepath = s.substring(5);
			}

		}
		
		//check java version und OS
		if(javaCFVersion >= 61) {
			//Ab java 17
			basicUI = false;
		}else if(javaCFVersion >= 52){
			//alles davor
			basicUI = true;
		}else {
			JOptionPane.showMessageDialog(null, "Die verwendete Java version ist nicht kompatibel: "+ System.getProperty("java.version") +"\nJava 8 ist die mindestanforderung mit eingeschränkter Funktionalität.\nBenutze bitte Java 17 oder neuer um alle Funktionen nutzen zu können.");
			System.exit(0);
		}
		
		if(!os.toLowerCase().contains("windows")) basicUI = true;
		
		//wird benötigt da es sonst mit alten Java Versionen zu Problemen kommen kann.
		f = new File(f.getAbsolutePath());
		
		if (!nogui) {
			
			if(!basicUI) {
				window = new GuiWindow(f);
			}else {
				windowbasic = new GuiWindowBasic(f);
			}
			
		} else {

			File inFile = new File("");
			File outFile = new File("");
			if (infilepath != "" & new File(infilepath).exists()) {
				inFile = new File(infilepath);
				if(inFile.isDirectory()) OpenFile(inFile);
			} else {
				inFile = OpenFile(f);
			}

			if (inFile.exists() & inFile.isFile() & inFile.getAbsolutePath().endsWith(".nc") & !inFile.getAbsolutePath().endsWith("g90.nc")) {
				Project p = new Project(inFile);
				p.convert();
				if (outfilepath != "") {
					outFile = new File(outfilepath);
					if (outFile.isDirectory()) {
						outFile = new File(outFile.getAbsoluteFile() + "\\" + inFile.getName()+".g90.nc");
					}
					if (!outFile.getAbsolutePath().endsWith(".nc")) {
						outFile = new File(outFile.getAbsolutePath() + ".nc");
					}
					if (!outFile.getAbsolutePath().endsWith(".g90.nc")) {
						outFile = new File(outFile.getAbsolutePath() + "g90.nc");
					}
					p.save(outFile);
				} else {
					p.save(p.getSaveFile());
				}
				String feedback = "Projekt: " + p.getProjectFile().getName() + " konvertiert in " + df.format(p.getConversionTime()) + "s\nVorschubweg ges. "+df.format(p.totalLineLength)+"mm\nVorschubzeit ca.: "+df.format(p.estimatedFeedTime)+"min\nLinien: " + p.getLinesList().size() + "\nGröße: " + df.format(p.maxX - p.minX) + "mm, " + df.format(p.maxY - p.minY) + "mm\n";
				JOptionPane.showMessageDialog(null, feedback);
			}
		}
	}
	
	public static File OpenFile(File f) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Öffnen");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setCurrentDirectory(f);
		fc.setFileFilter(new FileNameExtensionFilter(".nc", "nc"));
		fc.addChoosableFileFilter(new FileNameExtensionFilter(".gcode", "gcode"));
		int rv = fc.showOpenDialog(null);

		if (rv == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		}
		else {
			return f;
		}
	}

}
