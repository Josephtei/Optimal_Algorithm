package combineP1;

import java.util.*;

public class Combination implements Cloneable{
	
	public CandidatePoint [] CombinationPoint;
	public float combineValue;
	
	public Combination(){
	}
	
	public Combination(int a){
		CombinationPoint = new CandidatePoint[a];
		combineValue = 0;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		Combination o = (Combination)super.clone();
		o.CombinationPoint = (this.CombinationPoint).clone();
		return o;
	}
	
}
