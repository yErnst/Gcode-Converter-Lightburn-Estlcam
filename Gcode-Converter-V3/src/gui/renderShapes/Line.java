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
	public final Double svalue;
	
	public Line(double x1, double y1, double x2, double y2, double svalue, float stroke , double length, boolean GzeroRapid) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.svalue = svalue;
		this.GzeroRapid = GzeroRapid;
		this.stroke = stroke;
		this.length = length;
	}
	
	public void setLineStroke(float stroke) {
		this.stroke = stroke;
	}
	
	public Color getLineColor(Double maxS) {
		if(!highlited) {
			if(GzeroRapid)return Color.BLUE;
			return new Color(svalue.intValue() * 255 / maxS.intValue(), 0, 0);
		}else {
			if(svalue > 0) {
				return Color.orange;
			}else {
				return Color.cyan;
			}
		}
		
	}

}
