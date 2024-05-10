package gui.preview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
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
	private boolean useantialiasing = true;
	private boolean usegrid = true;
	private float gridSpaceing = 10;

	public Canvas(int x, int y, int width, int height) {
		setBackground(Color.white);
		setBounds(x, y, width, height);
		offsetx = ScreenToWorkspace(-width, 0).getX() / 2;
		offsety = ScreenToWorkspace(0, -height).getY() / 2;
	}
	
	public void setoffset(double offsetx, double offsety) {
		this.offsetx += offsetx/scale;
		this.offsety += offsety/scale;
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
	
	public void setGrindSpaceing(Float spaceing) {
		this.gridSpaceing = spaceing; //in mm
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
		if(pos == null) return new Point(0, 0);
		return ScreenToWorkspace(pos.getX(), pos.getY());
	}
	
	public double getScale() {
		return scale;
	}
	
	public Project getProject(){
		return p;
	}

	public void focusObject() {

		if ((getWidth() / (p.maxX + 20)) < (getHeight() / (p.maxY + 20))) {
			scale = (getWidth() / (p.maxX + 20));
		} else {
			scale = (getHeight() / (p.maxY + 20));
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
		if(useantialiasing)g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw X Y axis.
		g2.setStroke(new BasicStroke(2f));
		g2.setColor(Color.RED); //X
		g2.draw(new Line2D.Double(WorkspaceToScreen(offsetx, offsety).getX(), WorkspaceToScreen(0, 0).getY(), getWidth(), WorkspaceToScreen(0, 0).getY()));
		g2.setColor(Color.GREEN); //Y
		g2.draw(new Line2D.Double(WorkspaceToScreen(0, 0).getX(), WorkspaceToScreen(offsetx, offsety).getY(), WorkspaceToScreen(0, 0).getX(), getHeight()));
		
		// Draw Grid
		if(usegrid)drawGrid(gridSpaceing, g2);
		
		// Draw lines
		int i = 0;
		ArrayList<Line> drawLater = new ArrayList<Line>();
		for (Line line : p.getLinesList()) {
			if (i >= p.visibleLineCount)break;
			if(!p.converted)break;
			g2.setColor(line.getLineColor());
			g2.setStroke(new BasicStroke((float) (line.stroke+scale/1000)));
			if(line.highlited) {
				drawLater.add(line);
			}else {
				g2.draw(new Line2D.Double(WorkspaceToScreen(line.x1, line.y1), WorkspaceToScreen(line.x2, line.y2)));
			}
			
			i++;
		}
		for(Line line : drawLater) {
			g2.setColor(line.getLineColor());
			g2.setStroke(new BasicStroke((float) (line.stroke+1+scale/1000)));
			g2.draw(new Line2D.Double(WorkspaceToScreen(line.x1, line.y1), WorkspaceToScreen(line.x2, line.y2)));
		}

		// cleanup
		g2.setTransform(saveTransform);
		
		if(!Main.basicUI) {
			Main.window.updateStatusText();
		}else {
			Main.windowbasic.updateStatusText();
		}
	}
	
	public void drawGrid(float spaceing, Graphics2D g2){
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.lightGray);
		//Horizontal
		double minX = ScreenToWorkspace(0, 0).getX();
		double minY = ScreenToWorkspace(0, 0).getY();
		double maxX = ScreenToWorkspace(this.getWidth(), this.getHeight()).getX();
		double maxY = ScreenToWorkspace(this.getWidth(), this.getHeight()).getY();
		minX = Math.floor(minX/spaceing)*spaceing;
		minY = Math.floor(minY/spaceing)*spaceing;
		maxX = Math.floor(maxX/spaceing)*spaceing;
		maxY = Math.floor(maxY/spaceing)*spaceing;
		for(Float i = (float) minY; i <= maxY; i+=spaceing) {
			if(i!=0)g2.draw(new Line2D.Double(WorkspaceToScreen(offsetx, offsety).getX(), WorkspaceToScreen(0, i).getY(), getWidth(), WorkspaceToScreen(0, i).getY()));
		}
		
		//Vertical
		for(Float i = (float) minX; i <= maxX; i+=spaceing) {
			if(i!=0)g2.draw(new Line2D.Double(WorkspaceToScreen(i, 0).getX(), WorkspaceToScreen(offsetx, offsety).getY(), WorkspaceToScreen(i, 0).getX(), getHeight()));
		}
	}

}
