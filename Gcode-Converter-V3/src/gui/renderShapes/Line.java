package gui.renderShapes;

import java.awt.Color;

public class Line{
	
	public final double x1;
	public final double y1;
	public final double x2;
	public final double y2;
	public final double length;
	public float stroke;
	public boolean highlited = false;
	public final boolean GzeroRapid;
	public final int svalue;
	
	public Line(double x1, double y1, double x2, double y2, Double svalue, float stroke , double length, boolean GzeroRapid) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.svalue = svalue.intValue();
		this.GzeroRapid = GzeroRapid;
		this.stroke = stroke;
		this.length = length;
	}
	
	public void setLineStroke(float stroke) {
		this.stroke = stroke;
	}
	
	public Color getLineColor(int maxS) {
		if(maxS < 1)return Color.black;
		if(!highlited) {
			if(GzeroRapid)return Color.BLUE;
			return new Color(svalue * 255 / maxS, 0, 0);
		}else {
			if(svalue > 0) {
				return Color.orange;
			}else {
				return Color.cyan;
			}
		}
		
	}

}
