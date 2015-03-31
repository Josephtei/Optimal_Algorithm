package combineP1;

import java.util.*;

public class CombinationSet implements Cloneable{
	
	public ArrayList<Combination> CombinationList;
	public float maxValue;
	public float minValue;
	
	public CombinationSet(){
		CombinationList = new ArrayList<Combination>();
		maxValue = 0;
		minValue = 9999999;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		CombinationSet o = (CombinationSet)super.clone();
//		ArrayList<Combination> tempCombinationList = new ArrayList<Combination>();
//		for(int i=0;i<this.CombinationList.size();i++){
//			Combination co = (Combination)(this.CombinationList.get(i)).clone();
//			tempCombinationList.add(co);
//		}
		
//		o.CombinationList = tempCombinationList;
		
//		Combination co = this.CombinationList.get(0);
//		if(this.CombinationList == null)
//			o.CombinationList = new ArrayList<Combination>();
//		else
			o.CombinationList = (ArrayList)this.CombinationList.clone();
		return o;
	}
}
