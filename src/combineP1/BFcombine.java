package combineP1;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BFcombine {
	
	public static CandidatePoint [] cSet;
	public static int inputK = 4;
	public static int candidateNum = 100;
	public static int peopleNum = 10000;
	public static CombinationSet[][] dpTable;
	
	public static void main(String[] args) {
		
		cSet = new CandidatePoint[candidateNum];
		readCandidateInf();
		
		long startTime = System.currentTimeMillis(); //起始時間
		bruteforceDP();
		System.out.println(bestCombination());
		
		System.out.println(dpTable[(candidateNum-1)%2][inputK-1].CombinationList.size());
		
		long endTime = System.currentTimeMillis();
		long totTime = endTime - startTime;
		System.out.println("Using Time:" + totTime);
	}
	
	private static void readCandidateInf(){
		
		BufferedReader br;
		String [] line;
		CandidatePoint p;
		int lineCounter = 0;
		
		try{
			br = new BufferedReader(new FileReader("candidateInf1.out"));
			while(br.ready()){
				if(lineCounter == candidateNum)
					break;
				line = br.readLine().split(" ");
				p = new CandidatePoint();
				p.CandidateIndex = lineCounter;
				lineCounter++;
				p.helpPeopleNum = (line.length-1)/2;
				for(int i=0;i<line.length;i++){
					if(i == 0)
						p.totalContribution = Float.parseFloat(line[i]);
					else{
						if(i%2 == 1)
							p.peoList.add(Integer.parseInt(line[i]));
						else
							p.distList.add(Float.parseFloat(line[i]));
					}
				}
				cSet[p.CandidateIndex] = p;
			}
			br.close();
		}
		catch(IOException e){
			System.out.println("IOException: " + e);
		}
		
	}
	
	private static void bruteforceDP(){
		
		Combination cb = null;
		dpTable = new CombinationSet[2][inputK];
		initialTable();
		for(int i=1;i<candidateNum;i++){ //dpTable執行|candidate|次數
			for(int j=0;j<inputK;j++){ //一列中的K個block
				try{
					dpTable[i%2][j] = (CombinationSet)dpTable[(i+1)%2][j].clone();
				}
				catch(CloneNotSupportedException e){
					e.printStackTrace();
				}
				if(j==0){
					cb = new Combination(1);
					cb.CombinationPoint[0] = cSet[i];
					dpTable[i%2][j].CombinationList.add(cb);
				}
				else{
					for(int k=0 ; k<dpTable[(i+1)%2][j-1].CombinationList.size() ; k++){
						cb = new Combination(j+1);
						for(int l=0;l<j;l++)
							cb.CombinationPoint[l] = dpTable[(i+1)%2][j-1].CombinationList.get(k).CombinationPoint[l];
						cb.CombinationPoint[j] = cSet[i];
						
						dpTable[i%2][j].CombinationList.add(cb);
//						try{
//							cb = (Combination)(dpTable[(i+1)%2][j-1].CombinationList.get(k)).clone();
//						}
//						catch(CloneNotSupportedException e){
//							e.printStackTrace();
//						}
//						cb.CombinationPoint.add(cSet[i]);
//						dpTable[i%2][j].CombinationList.add(cb);
					}
				}
			}
			
			for(int j=0;j<inputK;j++)
				dpTable[(i+1)%2][j].CombinationList = null;
		}
	}
	
	private static void initialTable(){
		CombinationSet cs;
		Combination cb;
		for(int i=0;i<inputK;i++){			
			cs = new CombinationSet();
			if(i == 0){
				cb = new Combination(1);
				cb.CombinationPoint[0] = cSet[0];
				cs.CombinationList.add(cb);
			}
				dpTable[0][i] = cs;
		}
	}
	
	private static float bestCombination(){
		
		float combinationArray[] = new float[peopleNum];
		float maxReductionDist = 0;
		float tempTotalDist = 0;
		CandidatePoint temp;
		Combination maxCom;
		
		for(int i=0;i<peopleNum;i++)
			combinationArray[i] = 0;
		
		for(int i=0;i<dpTable[(candidateNum-1)%2][inputK-1].CombinationList.size();i++){
			for(int j=0; j<inputK;j++){
				temp = dpTable[(candidateNum-1)%2][inputK-1].CombinationList.get(i).CombinationPoint[j];
				for(int k=0;k<temp.helpPeopleNum;k++){
					if(combinationArray[temp.peoList.get(k)] < temp.distList.get(k))
						combinationArray[temp.peoList.get(k)] = temp.distList.get(k);
				}
			}
			for(int j=0;j<peopleNum;j++)
				tempTotalDist += combinationArray[j];
			if(tempTotalDist > maxReductionDist){
				maxReductionDist = tempTotalDist;
				maxCom = dpTable[(candidateNum-1)%2][inputK-1].CombinationList.get(i);
			}
			tempTotalDist = 0;
			for(int j=0;j<peopleNum;j++)
				combinationArray[j] = 0;
		}
		
		return maxReductionDist;
	}
	
}
