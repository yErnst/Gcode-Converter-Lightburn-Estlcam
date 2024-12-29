package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import gui.preview.Canvas;
import gui.renderShapes.Line;
import main.Main;
import project.Project;

public class GuiWindow extends JFrame {
	private static final long serialVersionUID = -5490909351513234262L;

	ProjectExplorer explorer;
	public Canvas canvas;
	File explorerroot;
	Project p;
	JSlider ShownLineCount;
	JTextField InfoBar;
	JScrollPane viewSP;
	JList<String> gcodeViewer;
	JComboBox<Float> GridSpaceing;
	JMenuBar menuBar;
	JMenu HelpMenu;
	JMenu SettingsMenu;
	JMenu FileMenu;
	JMenuItem AboutMenuItem;
	JMenuItem CMDHelpMenuItem;
	JMenuItem bugHelpMenuItem;
	JMenuItem OpenGcodeItem;
	JMenuItem SaveGcodeItem;
	JMenuItem SaveAsGcodeItem;
	JMenuItem OpenProjectFolder;
	JCheckBoxMenuItem useantialiasing;
	JCheckBoxMenuItem usegrid;
	JCheckBoxMenuItem uselineinfo;
	JButton openNCFile;
	ImageIcon icon;

	public GuiWindow(File explorerroot) {
		icon = new ImageIcon((Main.class.getResource("/images/Gcode-Converter.png")));
		this.explorerroot = explorerroot;
		this.setLocation(0, 0);
		this.setTitle("Gcode Converter " + Main.version + " by yErnst");
		this.setIconImage(icon.getImage());
		this.setSize(1200, 600);
		this.setMinimumSize(new Dimension(1200, 600));
		this.getContentPane().setLayout(null);

		initComponents();
		initListeners();
		update();
		this.setVisible(true);
	}

	public void initComponents() {

		gcodeViewer = new JList<String>();

		viewSP = new JScrollPane(gcodeViewer);
		viewSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		viewSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(viewSP);

		openNCFile = new JButton("Öffnen in Estlcam");
		openNCFile.setFocusable(false);
		this.add(openNCFile);

		Float[] spacings = { 10f, 5f, 4f, 3f, 2.5f, 2f, 1f, 0.5f };
		GridSpaceing = new JComboBox<Float>(spacings);
		GridSpaceing.setFocusable(false);
		GridSpaceing.setName("Grid");
		this.add(GridSpaceing);

		InfoBar = new JTextField();
		InfoBar.setFocusable(false);
		InfoBar.setEditable(false);
		this.add(InfoBar);

		if (!explorerroot.exists())
			explorerroot = new File("");
		explorer = new ProjectExplorer(explorerroot);
		explorer.setLocation(0, 29);
		this.add(explorer);

		canvas = new Canvas(300, 60, this.getWidth() - 616, this.getHeight() - 108);
		canvas.setBorder(new LineBorder(Color.gray));
		p = canvas.getProject();
		this.add(canvas);

		ShownLineCount = new JSlider();
		ShownLineCount.setFocusable(false);
		ShownLineCount.setValue(0);
		ShownLineCount.setMaximum(0);
		ShownLineCount.setPaintTicks(true);
		this.add(ShownLineCount);

		menuBar = new JMenuBar();
		this.add(menuBar);
		// Menu init
		// File Menu
		FileMenu = new JMenu("Datei");
		menuBar.add(FileMenu);
		OpenGcodeItem = new JMenuItem("Gcode Öffnen");
		FileMenu.add(OpenGcodeItem);
		SaveGcodeItem = new JMenuItem("Gcode Speichern");
		FileMenu.add(SaveGcodeItem);
		SaveAsGcodeItem = new JMenuItem("Gcode Speichern unter");
		FileMenu.add(SaveAsGcodeItem);
		OpenProjectFolder = new JMenuItem("Projektordner öffnen");
		FileMenu.add(OpenProjectFolder);
		
		// Settings Menu
		SettingsMenu = new JMenu("Einstellungen");
		menuBar.add(SettingsMenu);
		useantialiasing = new JCheckBoxMenuItem("Antialiasing");
		useantialiasing.setSelected(true);
		SettingsMenu.add(useantialiasing);
		usegrid = new JCheckBoxMenuItem("Gitter");
		usegrid.setSelected(true);
		SettingsMenu.add(usegrid);
		uselineinfo = new JCheckBoxMenuItem("Linineinfobox");
		uselineinfo.setSelected(true);
		SettingsMenu.add(uselineinfo);
		
		// Help Menu
		HelpMenu = new JMenu("Hilfe");
		menuBar.add(HelpMenu);
		AboutMenuItem = new JMenuItem("Über");
		HelpMenu.add(AboutMenuItem);
		CMDHelpMenuItem = new JMenuItem("CMD Help");
		HelpMenu.add(CMDHelpMenuItem);
		bugHelpMenuItem = new JMenuItem("Bug report");
		HelpMenu.add(bugHelpMenuItem);
	}

	public void update() {
		openNCFile.setBounds(300, 30, 150, 30);
		InfoBar.setBounds(0, this.getHeight() - 68, this.getWidth() - 116, 30);
		menuBar.setBounds(0, 0, this.getWidth() - 16, 30);
		ShownLineCount.setBounds(450, 30, this.getWidth() - 466, 30);
		explorer.setSize(300, this.getHeight() - 96);
		explorer.revalidate();
		canvas.setSize(this.getWidth() - 616, this.getHeight() - canvas.getY() - InfoBar.getHeight() - 38);
		GridSpaceing.setBounds(this.getWidth() - 116, this.getHeight() - 68, 100, 30);
		viewSP.setBounds(this.getWidth() - 316, 60, 300, this.getHeight() - canvas.getY() - InfoBar.getHeight() - 38);
		viewSP.revalidate();
	}

	public void updateStatusText() {
		Point2D pos = canvas.getMousScreenPos(canvas.getMousePosition());
		InfoBar.setText("Projekt: " + p.getProjectFile().getName() + " konvertiert in " + Main.df.format(p.getConversionTime()) + "s | Vorschubweg ges. " + Main.df.format(p.totalLineLength) + "mm | Vorschubzeit ca. " + Main.df.format(p.estimatedFeedTime) + "min | Linien:" + p.visibleLineCount + "\\" + p.getLinesList().size() + " | Größe:" + Main.df.format(p.maxX - p.minX) + "mm, " + Main.df.format(p.maxY - p.minY) + "mm | Position: X:" + Main.df.format(pos.getX()) + " Y:" + Main.df.format(pos.getY()) + " Mess: " + Main.df.format(canvas.getMessurmentLength()));
	}

	public void openProject(File f) {
		p = new Project(f);
		p.convert();
		canvas.setProject(p);
		canvas.focusObject();
		ShownLineCount.setMaximum(p.getLinesList().size());
		ShownLineCount.setValue(p.getLinesList().size());
		ShownLineCount.setMajorTickSpacing(ShownLineCount.getMaximum() / 100);
		gcodeViewer.setListData(p.getGcode().toArray(new String[0]));
		Runtime.getRuntime().gc();
	}

	public void initListeners() {

		gcodeViewer.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ESCAPE)
					gcodeViewer.clearSelection();
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		gcodeViewer.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				Set<Integer> highlite = new HashSet<Integer>();
				for (Integer highliteline : gcodeViewer.getSelectedIndices()) {
					highlite.add(p.getLastLineFromGcodeLine(highliteline) - 1);
				}
				int i = 0;
				for (Line line : p.getLinesList()) {
					if (highlite.contains(i)) {
						line.highlited = true;
					} else {
						line.highlited = false;
					}
					i++;
				}
				canvas.repaint();
			}
		});

		this.setDropTarget(new DropTarget() {
			private static final long serialVersionUID = 512690160108600113L;

			@SuppressWarnings("unchecked")
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					if (droppedFiles.get(0).isFile() && droppedFiles.get(0).getAbsolutePath().endsWith(".nc") && !droppedFiles.get(0).getAbsolutePath().endsWith("g90.nc")) {
						openProject(droppedFiles.get(0));
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		bugHelpMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Öffnen ein \"Issue\" auf https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/issues", "Probleme melden?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					try {
						Desktop.getDesktop().browse(new URI("https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/issues"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		CMDHelpMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, Main.cmdhelp);
			}
		});

		OpenProjectFolder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(explorerroot);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		GridSpaceing.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				canvas.setGrindSpaceing((Float) GridSpaceing.getSelectedItem());
			}
		});

		OpenGcodeItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Öffnen");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setCurrentDirectory(explorerroot);
				fc.setFileFilter(new FileNameExtensionFilter(".nc", "nc"));
				fc.addChoosableFileFilter(new FileNameExtensionFilter(".gcode", "gcode"));
				int rv = fc.showOpenDialog(null);

				if (rv == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fc.getSelectedFile();
					if (selectedFile.getAbsolutePath().endsWith(".nc") && !selectedFile.getAbsolutePath().endsWith("g90.nc")) {
						openProject(selectedFile);
					}
				}
			}
		});

		SaveAsGcodeItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Speichern unter");
				fc.setCurrentDirectory(explorerroot);

				int rv = fc.showSaveDialog(null);

				if (rv == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					if (!fc.getSelectedFile().getName().endsWith(".nc"))
						f = new File(f.getAbsoluteFile() + ".nc");
					if (!f.getName().endsWith(".g90.nc"))
						f = new File(f.getAbsoluteFile() + ".g90.nc");
					p.save(f);
					JOptionPane.showMessageDialog(null, "Gespeichert unter " + f.getAbsolutePath());
				}
			}
		});

		SaveGcodeItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				p.save(p.getSaveFile());
				JOptionPane.showMessageDialog(null, "Gespeichert unter " + p.getSaveFile().getAbsolutePath());
			}
		});

		openNCFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				p.openWithEstlcam();
			}
		});

		AboutMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Gcode Converter " + Main.version + " by yErnst\nhttps://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam", "Über Gcode Converter", JOptionPane.INFORMATION_MESSAGE, icon);
			}
		});

		useantialiasing.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				canvas.setAntialiasing(useantialiasing.isSelected());
			}
		});

		usegrid.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				canvas.setShowGrid(usegrid.isSelected());
			}
		});
		
		uselineinfo.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				canvas.setUselinieninfo(uselineinfo.isSelected());
			}
		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				update();
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});

		explorer.tree.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					TreePath path = explorer.tree.getPathForLocation(e.getX(), e.getY());
					if (path != null) {
						File selectedFile = ((File) (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject()));
						if (selectedFile.isFile()) {

							if (selectedFile.getAbsolutePath().endsWith(".nc") && !selectedFile.getAbsolutePath().endsWith("g90.nc")) {
								openProject(selectedFile);
							} else {
								try {
									Desktop.getDesktop().open(selectedFile);
								} catch (Exception e2) {
									JOptionPane.showMessageDialog(null, "Kein Standardprogramm für diese Dateiendung festgelegt");
								}
							}

						}
					}

					e.isConsumed();
				}
			}
		});

		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					canvas.setLastMousePos(e.getPoint());

				if (e.getButton() == MouseEvent.BUTTON3)
					canvas.setMessStart(canvas.getMousScreenPos(e.getPoint()));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					canvas.setMessStart(new Point(0, 0));
					canvas.setMessEnde(new Point(0, 0));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON2) {
					canvas.focusObject();
				}
			}
		});

		canvas.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Main.window.updateStatusText();
				if(uselineinfo.isSelected())canvas.repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK || e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK + MouseEvent.BUTTON3_DOWN_MASK) {
					canvas.setoffset(canvas.getLastMousePos().getX() - e.getX(), canvas.getLastMousePos().getY() - e.getY());
					canvas.setLastMousePos(e.getPoint());
				}
				if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK || e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK + MouseEvent.BUTTON3_DOWN_MASK) {
					canvas.setMessEnde(canvas.getMousScreenPos(e.getPoint()));
				}
				Main.window.updateStatusText();
			}
		});

		canvas.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (canvas.getScale() + canvas.getScale() * -e.getPreciseWheelRotation() / 10 > 0.5 && canvas.getScale() + canvas.getScale() * -e.getPreciseWheelRotation() / 10 < 1000) {
					canvas.setScale(canvas.getScale() + canvas.getScale() * -e.getPreciseWheelRotation() / 10, e.getX(), e.getY());
				}
			}
		});

		ShownLineCount.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				canvas.getProject().visibleLineCount = ShownLineCount.getValue();
				canvas.repaint();
			}
		});

		ShownLineCount.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if ((int) (ShownLineCount.getValue() + e.getPreciseWheelRotation()) >= 0 && (int) (ShownLineCount.getValue() + e.getPreciseWheelRotation()) <= ShownLineCount.getMaximum())
					ShownLineCount.setValue((int) (ShownLineCount.getValue() + e.getPreciseWheelRotation()));
			}
		});

	}

}
