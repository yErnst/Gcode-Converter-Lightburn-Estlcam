package gui.preview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import gui.renderShapes.Line;
import main.Main;
import project.Project;

public class Canvas extends JPanel {
	private static final long serialVersionUID = -1046656837972608707L;

	private double offsetx = 0;
	private double offsety = 0;
	private double scale = 5;
	private Project p = new Project(new File(""));
	private Point2D lastmousepos = new Point(0, 0);
	private Point2D messStart = new Point(0, 0);
	private Point2D messEnde = new Point(0, 0);
	private boolean useantialiasing = true;
	private boolean usegrid = true;
	private boolean uselineinfo = true;
	private float gridSpaceing = 10;
	private float infoboxscale = 1.5f;

	public Canvas(int x, int y, int width, int height) {
		setBackground(Color.white);
		setBounds(x, y, width, height);
		offsetx = ScreenToWorkspace(-width, 0).getX() / 2;
		offsety = ScreenToWorkspace(0, -height).getY() / 2;
	}

	public void setoffset(double offsetx, double offsety) {
		this.offsetx += offsetx / scale;
		this.offsety += offsety / scale;
		repaint();
	}

	public void setScale(double scale, int x, int y) {
		Point2D Mouseprescale = ScreenToWorkspace(x, y);
		this.scale = scale;
		Point2D Mousepostscale = ScreenToWorkspace(x, y);
		offsetx += (Mouseprescale.getX() - Mousepostscale.getX());
		offsety += (Mouseprescale.getY() - Mousepostscale.getY());
		repaint();
	}

	public void setProject(Project p) {
		this.p = p;
	}

	public Point2D getLastMousePos() {
		return lastmousepos;
	}

	public void setMessStart(Point2D messStart) {
		this.messStart = messStart;
	}

	public void setMessEnde(Point2D messEnde) {
		this.messEnde = messEnde;
		repaint();
	}

	public double getMessurmentLength() {
		return Point2D.distance(messStart.getX(), messStart.getY(), messEnde.getX(), messEnde.getY());
	}

	public void setLastMousePos(Point2D mousepos) {
		lastmousepos = mousepos;
	}

	public void setAntialiasing(boolean enabled) {
		useantialiasing = enabled;
		repaint();
	}

	public void setShowGrid(boolean usegrid) {
		this.usegrid = usegrid;
		repaint();
	}
	
	public void setUselinieninfo(boolean uselinieninfo) {
		this.uselineinfo = uselinieninfo;
	}

	public void setGrindSpaceing(Float spaceing) {
		this.gridSpaceing = spaceing; // in mm
		repaint();
	}

	private Point2D WorkspaceToScreen(double x, double y) {
		x = (x - offsetx) * scale;
		y = (y - offsety) * scale;
		return new Point2D.Double(x, y);
	}

	private Point2D ScreenToWorkspace(double x, double y) {
		x = x / scale + offsetx;
		y = y / scale + offsety;
		return new Point2D.Double(x, y);
	}

	public Point2D getMousScreenPos(Point2D pos) {
		if (pos == null)
			return new Point(0, 0);
		return ScreenToWorkspace(pos.getX(), pos.getY());
	}

	public double getScale() {
		return scale;
	}

	public Project getProject() {
		return p;
	}

	public void focusObject() {

		if ((getWidth() / (p.maxX - p.minX + 20)) < (getHeight() / (p.maxY - p.minY + 20))) {
			scale = (getWidth() / (p.maxX - p.minX + 20));
		} else {
			scale = (getHeight() / (p.maxY - p.minY + 20));
		}

		offsetx = p.minX - 10;
		offsety = p.minY - 10;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		AffineTransform saveTransform = g2.getTransform();

		// Rendering Hints
		if (useantialiasing)
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw X Y axis.
		g2.setStroke(new BasicStroke(2f));
		g2.setColor(Color.RED); // X
		g2.draw(new Line2D.Double(WorkspaceToScreen(offsetx, offsety).getX(), WorkspaceToScreen(0, 0).getY(), getWidth(), WorkspaceToScreen(0, 0).getY()));
		g2.setColor(Color.GREEN); // Y
		g2.draw(new Line2D.Double(WorkspaceToScreen(0, 0).getX(), WorkspaceToScreen(offsetx, offsety).getY(), WorkspaceToScreen(0, 0).getX(), getHeight()));

		// Draw Grid
		if (usegrid)
			drawGrid(gridSpaceing, g2);

		// Draw lines
		int i = 0;
		ArrayList<Line> drawLater = new ArrayList<Line>();
		for (Line line : p.getLinesList()) {
			if (i >= p.visibleLineCount)
				break;
			if (!p.converted)
				break;
			g2.setColor(line.getLineColor(p.maxS));
			g2.setStroke(new BasicStroke((float) (line.stroke + scale / 1000)));
			if (line.highlited) {
				drawLater.add(line);
			} else {
				g2.draw(new Line2D.Double(WorkspaceToScreen(line.x1, line.y1), WorkspaceToScreen(line.x2, line.y2)));
			}

			i++;
		}
		for (Line line : drawLater) {
			g2.setColor(line.getLineColor(p.maxS));
			g2.setStroke(new BasicStroke((float) (line.stroke + 1 + scale / 1000)));
			g2.draw(new Line2D.Double(WorkspaceToScreen(line.x1, line.y1), WorkspaceToScreen(line.x2, line.y2)));
		}

		// Draw MessurmentLine
		if (messEnde.getX() != 0 && messEnde.getY() != 0) {
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke((float) (2 + scale / 1000)));
			g2.draw(new Line2D.Double(WorkspaceToScreen(messStart.getX(), messStart.getY()), WorkspaceToScreen(messEnde.getX(), messEnde.getY())));
			String len = Main.df.format(getMessurmentLength());
			int MessEndeX = (int) WorkspaceToScreen((messEnde.getX()), 0).getX() - len.length() * 5;
			int MessEndeY = (int) WorkspaceToScreen(0, messEnde.getY()).getY() - 5;
			g2.setColor(Color.lightGray);
			g2.fillRect(MessEndeX - 20, MessEndeY - 11, 80, 13);
			g2.setColor(Color.black);
			g2.drawString(len + "mm", MessEndeX, MessEndeY);
		}

		// Draw LineInfoBox
		Point mpos = this.getMousePosition();
		if (mpos != null && uselineinfo) {
			Line l = p.getClosesVisableLineToPoint(this.getMousScreenPos(mpos));
			if (l != null) {
				//repaint line with a color gradient
				g2.setPaint(new GradientPaint(WorkspaceToScreen(l.x1, l.y1), new Color(46, 200, 34), WorkspaceToScreen(l.x2, l.y2), Color.blue));
				g2.setStroke(new BasicStroke((float) (l.stroke + scale / 1000)));
				g2.draw(new Line2D.Double(WorkspaceToScreen(l.x1, l.y1), WorkspaceToScreen(l.x2, l.y2)));
				
				//draw start(green) and end(blue) dots
				g2.setColor(new Color(46, 142, 34));
				g2.fill(new Ellipse2D.Double(WorkspaceToScreen(l.x1, 0).getX()-4, WorkspaceToScreen(0, l.y1).getY()-4, 8, 8));
				g2.setColor(Color.blue);
				g2.fill(new Ellipse2D.Double(WorkspaceToScreen(l.x2, 0).getX()-4, WorkspaceToScreen(0, l.y2).getY()-4, 8, 8));
				
				//prep infobox drawing
				mpos.x += 20;
				g2.scale(infoboxscale, infoboxscale);
				mpos.x = (int) (mpos.x/infoboxscale);
				mpos.y = (int) (mpos.y/infoboxscale);
				g2.setColor(new Color(200, 200, 200, 230));
				
				//draw Infobox
				g2.fillRect(mpos.x-3, mpos.y-10, ("Länge:" + Main.df.format(l.length) + "mm").length()*7, 73);
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(5f));
				g2.drawString("Länge:" + Main.df.format(l.length) + "mm", mpos.x, mpos.y);
				g2.drawString("Power:" + l.svalue, mpos.x, mpos.y+10);
				if(l.GzeroRapid){
					g2.drawString("Feed:" + "G0", mpos.x, mpos.y+20);
				}else {
					g2.drawString("Feed:" + l.feed, mpos.x, mpos.y+20);
				}
				g2.setColor(new Color(46, 142, 34)); //Line start
				g2.drawString("X1:" + Main.df.format(l.x1) , mpos.x, mpos.y+30);
				g2.drawString("Y1:" + Main.df.format(l.y1), mpos.x, mpos.y+40);
				g2.setColor(Color.blue); //Line end
				g2.drawString("X2:" + Main.df.format(l.x2), mpos.x, mpos.y+50);
				g2.drawString("Y2:" + Main.df.format(l.y2), mpos.x, mpos.y+60);
			}
		}

		// cleanup
		g2.setTransform(saveTransform);

		if (!Main.basicUI) {
			Main.window.updateStatusText();
		} else {
			Main.windowbasic.updateStatusText();
		}

	}

	public void drawGrid(float spaceing, Graphics2D g2) {
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.lightGray);
		// Horizontal
		double minX = ScreenToWorkspace(0, 0).getX();
		double minY = ScreenToWorkspace(0, 0).getY();
		double maxX = ScreenToWorkspace(this.getWidth(), this.getHeight()).getX();
		double maxY = ScreenToWorkspace(this.getWidth(), this.getHeight()).getY();
		minX = Math.floor(minX / spaceing) * spaceing;
		minY = Math.floor(minY / spaceing) * spaceing;
		maxX = Math.floor(maxX / spaceing) * spaceing;
		maxY = Math.floor(maxY / spaceing) * spaceing;
		for (Float i = (float) minY; i <= maxY; i += spaceing) {
			if (i != 0)
				g2.draw(new Line2D.Double(WorkspaceToScreen(offsetx, offsety).getX(), WorkspaceToScreen(0, i).getY(), getWidth(), WorkspaceToScreen(0, i).getY()));
		}

		// Vertical
		for (Float i = (float) minX; i <= maxX; i += spaceing) {
			if (i != 0)
				g2.draw(new Line2D.Double(WorkspaceToScreen(i, 0).getX(), WorkspaceToScreen(offsetx, offsety).getY(), WorkspaceToScreen(i, 0).getX(), getHeight()));
		}
	}

}
