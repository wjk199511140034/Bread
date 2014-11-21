package mesh3d;

import java.util.ArrayList;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;
import math.geom3d.Point3D;
import math.geom3d.Vector3D;
import math.geom3d.line.LineSegment3D;

public class Surface3D extends Mesh3D{
	public Surface3D(Tri3D[] ts) {
		this.tris = ts;
		makeBB();
	}

	@Override
	public boolean closed(){return false;};
	
	public LineSegment3D[] overlap(Mesh3D m){
		LineSegment3D[] output = new LineSegment3D[m.triCount()*this.triCount()];
		int j=0;
		for(Tri3D tS:tris){
			for(Tri3D tM:m.getTris()){
				Point3D[] hits = tS.overlap(tM);
				if(hits!=null){
					Vector3D edge = new Vector3D(hits[0],hits[1]);
					Vector3D cross = Vector3D.crossProduct(tS.normal(), tM.normal());
					//Orient line segments so that the inside of the model is CCW from them, viewed facing the surface normal.
					output[j]=Vector3D.dotProduct(edge, cross)>0 ? new LineSegment3D(hits[0],hits[1]) :
						new LineSegment3D(hits[1],hits[0]);
				}
			}
		}
		return output;
	}
	/**
	 * @return A 2d projection of this Surface's topology.
	 */
	public LineSegment2D[] topology(){
		ArrayList<LineSegment2D> output = new ArrayList<LineSegment2D>();;
		for(Tri3D t:tris){
			Point2D[] ps = t.getPoints2D();
			//Generate the 2d projection of each triangle
			LineSegment2D[] ls = new LineSegment2D[]{new LineSegment2D(ps[0],ps[1]),
					new LineSegment2D(ps[1],ps[2]),
					new LineSegment2D(ps[2],ps[0])};
			//Throw away any lines which we already have in the topology.
			boolean[] checks = new boolean[]{true,true,true};
			for(LineSegment2D l:output){
				for(int i=0;i<3;i++){
					if(checks[i]&&(l.almostEquals(ls[i], Constants.tol)||l.almostEquals(ls[i].reverse(), Constants.tol))){
						checks[i]=false;
					}
				}				
			}
			//Add the lines we want to keep to our topology.
			for(int i=0;i<3;i++){
				if(checks[i])output.add(ls[i]);
			}
		}
		return output.toArray(new LineSegment2D[output.size()]);
	}
}