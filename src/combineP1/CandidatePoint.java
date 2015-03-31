package combineP1;

import java.util.ArrayList;
import java.util.List;

public class CandidatePoint implements Comparable<CandidatePoint>{
	public float totalContribution;
	public List <Integer> peoList;
	public List <Float> distList;
	public int CandidateIndex;
	public int helpPeopleNum;
	
	public CandidatePoint(){
		peoList = new ArrayList<Integer>();
		distList = new ArrayList<Float>();
	}
	
	public int compareTo(CandidatePoint o){
		return Float.compare(o.totalContribution , this.totalContribution);
	}
}
