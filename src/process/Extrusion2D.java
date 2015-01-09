package process;

import java.util.ArrayList;
import utils2D.Utils2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

/**
 * ExtrusionType marks the type of extrusion this line represents.
 * 0 - travel
 * 1 - infill
 * 2 - shell
 * 3 - nonretracting travel
 * @author Nick
 *
 */
public class Extrusion2D extends LineSegment2D{
	public final int ExtrusionType;
	
	public Extrusion2D(Point2D point1, Point2D point2, int ExtrusionType) {
		super(point1, point2);
		this.ExtrusionType=ExtrusionType;
	}
	/**
	 * @param topology
	 * @return a set of Extrusion2Ds of the same type as this with the same endpoints,
	 * split anywhere this intersects the lines in topology.
	 */
	public ArrayList<Extrusion2D> splitExtrusion(LineSegment2D[] topology){
		ArrayList<Point2D> hits = new ArrayList<Point2D>();
		for(LineSegment2D l: topology){
			if(l==null) continue;
			Point2D hit = this.intersection(l);
			if(hit!=null) hits.add(hit);
		}
		if(hits.isEmpty()||!Utils2D.equiv(this.firstPoint(),hits.get(0))) hits.add(0,this.firstPoint());
		if(!Utils2D.equiv(this.lastPoint(), hits.get(hits.size()-1))) hits.add(this.lastPoint());
		ArrayList<Point2D> sorted = Utils2D.orderPoints(this.direction(), hits);
		return Utils2D.ConnectPoints(sorted,this.ExtrusionType);
	}
	@Override
	public String toString(){
		return "Extrusion2D[("+this.x0+", "+this.y0+")-("+(x0+dx)+", "+(y0+dy)+") "+ExtrusionType+"]";
	}
}
