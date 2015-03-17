package combineP1;

import java.util.ArrayList;
import java.util.List;

public class CandidatePoint {
	public float totalContribution;
	public List <Integer> peoList;
	public List <Float> distList;
	
	public CandidatePoint(){
		peoList = new ArrayList<Integer>();
		distList = new ArrayList<Float>();
	}
}
