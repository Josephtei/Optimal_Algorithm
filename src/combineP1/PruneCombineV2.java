package combineP1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PruneCombineV2 {

	public static CandidatePoint [] cSet;
	public static int inputK = 8;
	public static int candidateNum = 1000;
	public static int peopleNum = 100000;
	public static CombinationSet[][] dpTable;
	public static float [] combinationArray = new float[peopleNum];
	public static Combination maxCombinePtr;
	
	
	public static void main(String[] args) {
		
		cSet = new CandidatePoint[candidateNum];
		readCandidateInf();
		
		long startTime = System.currentTimeMillis(); //起始時間
		Arrays.sort(cSet);
		float answer = pruneDP().combineValue;
		System.out.println(answer);
		//System.out.println(dpTable[finalIndex%2][inputK-1].CombinationList.get(1).CombinationPoint[0].CandidateIndex + " " + dpTable[finalIndex%2][inputK-1].CombinationList.get(1).CombinationPoint[1].CandidateIndex);
		
//		for(int i=0;i<candidateNum;i++)
//			System.out.println(cSet[i].totalContribution);
		
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
	
	
	
	private static Combination pruneDP(){
		
		Combination cb = null;
		dpTable = new CombinationSet[2][inputK];
		boolean firstCombine = false;  //第一個出現的k個點集合
		float cumulation = 0;
		ArrayList<Combination> removeList = new ArrayList<Combination>();
		int optimalTable[] = new int[inputK];
		float threshold;
		Combination optimalPtr = null;
		int finishCount = 0;
		
		for(int i=0;i<inputK;i++)
			optimalTable[i] = 0;
		
		initialTable();
		
		for(int i=1;i<candidateNum;i++){ //dpTable執行|candidate|次數
			
			if(finishCount == (inputK-1))  //終止條件
				break;
			else
				finishCount = 0;
			
			for(int j=(inputK-1);j>=0;j--){ //一列中的K個block
				
				System.out.println(i + " " + j);
				
				try{
					if(firstCombine == true){
						if(j==(inputK-1)){    //最後一column的時候
							dpTable[i%2][j] = new CombinationSet();
							dpTable[i%2][j].maxValue = optimalPtr.combineValue;
							
							for(int k=0 ; k<dpTable[(i+1)%2][j-1].CombinationList.size() ; k++){  //加入左下格}
								
								cb = new Combination(j+1);
								
								for(int l=0;l<j;l++)
									cb.CombinationPoint[l] = dpTable[(i+1)%2][j-1].CombinationList.get(k).CombinationPoint[l];	
								
								cb.CombinationPoint[j] = cSet[i];
								countCombineValue(cb);
								
								if(cb.combineValue > dpTable[i%2][j].maxValue){
									dpTable[i%2][j].maxValue = cb.combineValue;
									optimalPtr = cb;
								}	
							}
							
						}
						else if(j!=0){         //最後一column和第一column除外
							
							cumulation = 0;
							removeList.clear();
							
							for(int k=1;k<=(inputK-(j+1));k++){    //下方格
								if((i+k)>(candidateNum-1))
									cumulation += cSet[candidateNum-1].totalContribution;
								else
									cumulation += cSet[i+k].totalContribution;
							}
							
							float tempMin = 9999999;
							
							if((dpTable[(i+1)%2][j].maxValue+cumulation) <= optimalPtr.combineValue)
								dpTable[(i+1)%2][j].CombinationList.clear();
							else if( (dpTable[(i+1)%2][j].maxValue+cumulation) > optimalPtr.combineValue && (dpTable[(i+1)%2][j].minValue+cumulation) <= optimalPtr.combineValue){
								for(int k=(dpTable[(i+1)%2][j].CombinationList.size()-1);k>=0;k--){
									if((dpTable[(i+1)%2][j].CombinationList.get(k).combineValue+cumulation) <= optimalPtr.combineValue)
										removeList.add(dpTable[(i+1)%2][j].CombinationList.get(k));
									else{
										if(dpTable[(i+1)%2][j].CombinationList.get(k).combineValue < tempMin)
											tempMin = dpTable[(i+1)%2][j].CombinationList.get(k).combineValue;
									}
								}
							}
							if(removeList.size()!=0)
								dpTable[(i+1)%2][j].CombinationList.removeAll(removeList);
							
							dpTable[i%2][j] = (CombinationSet)dpTable[(i+1)%2][j].clone();  //下方格
							dpTable[i%2][j].minValue = tempMin;
							
							float cumulation2 = 0;
							removeList.clear();
							
							for(int k=0;k<=(inputK-(j+1));k++){    //左下格
								if((i+k)>(candidateNum-1))
									cumulation2 += cSet[candidateNum-1].totalContribution;
								else
									cumulation2 += cSet[i+k].totalContribution;
							}
							if((dpTable[(i+1)%2][j-1].maxValue+cumulation2) <= optimalPtr.combineValue)
								dpTable[(i+1)%2][j-1].CombinationList.clear();
							else if( (dpTable[(i+1)%2][j-1].maxValue+cumulation2) > optimalPtr.combineValue && (dpTable[(i+1)%2][j-1].minValue+cumulation2) <= optimalPtr.combineValue){
								for(int k=(dpTable[(i+1)%2][j-1].CombinationList.size()-1);k>=0;k--){
									if((dpTable[(i+1)%2][j-1].CombinationList.get(k).combineValue+cumulation2) <= optimalPtr.combineValue)
										removeList.add(dpTable[(i+1)%2][j-1].CombinationList.get(k));
								}
							}
							
							if(removeList.size()!=0)
								dpTable[(i+1)%2][j-1].CombinationList.removeAll(removeList);
							
							for(int k=(dpTable[(i+1)%2][j-1].CombinationList.size()-1);k>=0;k--){
								
								threshold = optimalPtr.combineValue - cumulation;
								
								cb = new Combination(j+1);
								
								for(int l=0;l<j;l++)
									cb.CombinationPoint[l] = dpTable[(i+1)%2][j-1].CombinationList.get(k).CombinationPoint[l];
								
								cb.CombinationPoint[j] = cSet[i];
								countCombineValue(cb);
								
								if(cb.combineValue > threshold){
									if(cb.combineValue > dpTable[i%2][j].maxValue)
										dpTable[i%2][j].maxValue = cb.combineValue;
									else if(cb.combineValue < dpTable[i%2][j].minValue)
										dpTable[i%2][j].minValue = cb.combineValue;
									dpTable[i%2][j].CombinationList.add(cb);
								}
								
							}           //左下格
							
							if(dpTable[i%2][j].CombinationList.size()==0)
								finishCount++;
						}
						else{    //第一column
							cumulation = 0;
							
							for(int k=1;k<=(inputK-(j+1));k++){
								if((i+k)>(candidateNum-1))
									cumulation += cSet[candidateNum-1].totalContribution;
								else
									cumulation += cSet[i+k].totalContribution;
							}
							if((optimalPtr.combineValue - cumulation) <= 0){  //下方格皆有符合
								dpTable[i%2][j] = (CombinationSet)dpTable[(i+1)%2][j].clone();
								cb = new Combination(1);
								cb.CombinationPoint[0] = cSet[i];
								cb.combineValue = cSet[i].totalContribution;
								dpTable[i%2][j].CombinationList.add(cb);
								dpTable[i%2][j].minValue = cSet[i].totalContribution;
							}
							else{
								threshold = optimalPtr.combineValue - cumulation;
								
								dpTable[i%2][j] = new CombinationSet();
								dpTable[i%2][j].maxValue = cSet[0].totalContribution;
								
								for(int k=0;k<dpTable[(i+1)%2][j].CombinationList.size();k++){
									if(dpTable[(i+1)%2][j].CombinationList.get(k).combineValue > threshold){
										cb = (Combination)(dpTable[(i+1)%2][j].CombinationList.get(k)).clone();
										dpTable[i%2][j].CombinationList.add(cb);
									}
									else{
										if((k-1)<0)
											dpTable[i%2][j].minValue = cSet[0].totalContribution;
										else
											dpTable[i%2][j].minValue = dpTable[(i+1)%2][j].CombinationList.get(k-1).combineValue;
										break;
									}
								}
							}
							if(dpTable[i%2][j].CombinationList.size()==0)
								finishCount++;
						}
					}
					else{  //還沒出現第一組解前，不用做pruning
						dpTable[i%2][j] = (CombinationSet)dpTable[(i+1)%2][j].clone();  //加入下方格
						
						if(j==0){  //1個點的組合
							cb = new Combination(1);
							cb.CombinationPoint[0] = cSet[i];
							cb.combineValue = cSet[i].totalContribution;
							dpTable[i%2][j].CombinationList.add(cb);
						}
						else{
							for(int k=0 ; k<dpTable[(i+1)%2][j-1].CombinationList.size() ; k++){  //加入左下格
								
								cb = new Combination(j+1);
								
								for(int l=0;l<j;l++)
									cb.CombinationPoint[l] = dpTable[(i+1)%2][j-1].CombinationList.get(k).CombinationPoint[l];							
								cb.CombinationPoint[j] = cSet[i];
								
								countCombineValue(cb);
								
								if(cb.combineValue > dpTable[i%2][j].maxValue){
									dpTable[i%2][j].maxValue = cb.combineValue;
									if(j == (inputK-1))
										optimalPtr = cb;
								}
								else if(cb.combineValue < dpTable[i%2][j].minValue)
									dpTable[i%2][j].minValue = cb.combineValue;
									
								dpTable[i%2][j].CombinationList.add(cb);
								
							}
						}
						
						if(i == (inputK-1))
							firstCombine = true;
					}
				}
				catch(CloneNotSupportedException e){
					e.printStackTrace();
				}
			}

		}
		
		return optimalPtr;
	}
	
	
	
	private static void initialTable(){
		CombinationSet cs;
		Combination cb;
		for(int i=0;i<inputK;i++){			
			cs = new CombinationSet();
			if(i == 0){
				cb = new Combination(1);
				cb.CombinationPoint[0] = cSet[0];
				cb.combineValue = cSet[0].totalContribution;
				cs.CombinationList.add(cb);
				cs.maxValue = cSet[0].totalContribution;
				cs.minValue = cSet[0].totalContribution;
			}
				dpTable[0][i] = cs;
		}
	}
	
	
	
	private static void countCombineValue(Combination cm){
		
		float ftemp;
		int itemp;
		float total = 0;
		
		for(int i=0;i<peopleNum;i++)  //清空
			combinationArray[i] = 0;
		
		for(int i=0;i<cm.CombinationPoint.length;i++)
			for(int j=0;j<cm.CombinationPoint[i].helpPeopleNum;j++){
				itemp = cm.CombinationPoint[i].peoList.get(j);
				ftemp = cm.CombinationPoint[i].distList.get(j);
				if(ftemp > combinationArray[itemp])
					combinationArray[itemp] = ftemp;
			}
		
		for(int i=0;i<peopleNum;i++)
			total += combinationArray[i];
		
		cm.combineValue = total;
	}
	
}
