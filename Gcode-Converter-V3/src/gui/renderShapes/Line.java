package gui.renderShapes;

import java.awt.Color;

public class Line{
	
	public final double x1;
	public final double y1;
	public final double x2;
	public final double y2;
	public final double length;
	private Color color;
	public float stroke;
	public boolean highlited = false;
	
	public Line(double x1, double y1, double x2, double y2, Color color, float stroke , double length) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
		this.stroke = stroke;
		this.length = length;
	}
	
	public void setLineColor(Color color) {
		this.color = color;
	}
	
	public void setLineStroke(float stroke) {
		this.stroke = stroke;
	}
	
	public Color getLineColor() {
		if(!highlited) {
			return color;
		}else {
			if(color.getRed() > 0) {
				return Color.orange;
			}else {
				return Color.cyan;
			}
		}
		
	}

}
