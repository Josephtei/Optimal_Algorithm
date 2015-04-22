package combineP1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GreedyAlg {

	public static CandidatePoint [] cSet;
	public static int inputK = 8;
	public static int candidateNum = 1000;
	public static int peopleNum = 100000;
	public static float [] combinationArray = new float [peopleNum];
	public static byte [] chooseCandidate = new byte [candidateNum];
	
	public static void main(String[] args) {
		
		cSet = new CandidatePoint[candidateNum];
		readCandidateInf();

		long startTime = System.currentTimeMillis(); //起始時間
		
		greedyProcess();
		
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
			br = new BufferedReader(new FileReader("candidateInf10.out"));
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

	private static void greedyProcess(){
		
		CandidatePoint nowMax = null;
		int combinationArrayIndex;
		float distValue;
		float increasingValue = 0;
		float maxIncreasing = 0;
		float finalValue = 0;
		
		for(int i=0;i<peopleNum;i++){
			combinationArray[i]=0;
		}
		
		for(int i=0;i<candidateNum;i++)
			chooseCandidate[i] = 0;
		
		for(int i=0;i<inputK;i++){
			for(int j=0;j<candidateNum;j++){
				if(chooseCandidate[cSet[j].CandidateIndex]!=1){
					for(int k=0;k<cSet[j].helpPeopleNum;k++){
						combinationArrayIndex = cSet[j].peoList.get(k);
						distValue = cSet[j].distList.get(k);
						if(combinationArray[combinationArrayIndex] < distValue)
							increasingValue += (distValue - combinationArray[combinationArrayIndex]);
					}
					if(increasingValue > maxIncreasing){
						maxIncreasing = increasingValue;
						nowMax = cSet[j];
					}
					increasingValue = 0;
				}
			}
			finalValue += maxIncreasing;
			chooseCandidate[nowMax.CandidateIndex] = 1;
			maxIncreasing = 0;
			for(int j=0;j<nowMax.helpPeopleNum;j++){
				combinationArrayIndex = nowMax.peoList.get(j);
				distValue = nowMax.distList.get(j);
				if(combinationArray[combinationArrayIndex] < distValue)
					combinationArray[combinationArrayIndex] = distValue;
			}
			nowMax = null;
		}
		
		float temp = 0;
		for(int i=0;i<peopleNum;i++)
			temp += combinationArray[i];
		System.out.println(temp);
		//return finalValue;
	}
	
}
