package combineP1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PruneCombine {

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
		int finalIndex = pruneDP();
		System.out.println(dpTable[finalIndex%2][inputK-1].maxValue);
		System.out.println(dpTable[finalIndex%2][inputK-1].CombinationList.size());
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
			br = new BufferedReader(new FileReader("candidateInf7.out"));
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
	
	
	
	private static int pruneDP(){
		
		Combination cb = null;
		dpTable = new CombinationSet[2][inputK];
		boolean firstCombine = false;  //第一個出現的k個點集合
		float cumulation = 0;
		ArrayList<Combination> removeList = new ArrayList<Combination>();
		int optimalTable[] = new int[inputK];
		int nowOptimalK = 1;
		
		for(int i=0;i<inputK;i++)
			optimalTable[i] = 0;
		
		initialTable();
		
		for(int i=1;i<candidateNum;i++){ //dpTable執行|candidate|次數
			for(int j=0;j<inputK;j++){ //一列中的K個block
				System.out.println(i + " " + j);
				try{
					if(firstCombine == true){
						for(int k=0;k<(inputK-(j+1));k++){
							if(i+k>=(candidateNum-1))
								cumulation += cSet[candidateNum-1].totalContribution;
							else
								cumulation += cSet[i+k].totalContribution;
						}
						if((dpTable[(i+1)%2][inputK-1].maxValue - cumulation) > 0){
							if(j==0){   //1個元素的集合時，因有排序所以到某一個大於後，即可break
								for(int k=(dpTable[(i+1)%2][j].CombinationList.size()-1);k>=0;k--){
									if((dpTable[(i+1)%2][j].CombinationList.get(k).combineValue+cumulation) > dpTable[(i+1)%2][inputK-1].maxValue)
										break;
									else
										removeList.add(dpTable[(i+1)%2][j].CombinationList.get(k));
								}
							}
							else if(j!=(inputK-1)){
								if((dpTable[(i+1)%2][j].maxValue+cumulation) <= dpTable[(i+1)%2][inputK-1].maxValue)
									dpTable[(i+1)%2][j].CombinationList.clear();
								else if( (dpTable[(i+1)%2][j].maxValue+cumulation) > dpTable[(i+1)%2][inputK-1].maxValue && (dpTable[(i+1)%2][j].minValue+cumulation) <= dpTable[(i+1)%2][inputK-1].maxValue){
									for(int k=(dpTable[(i+1)%2][j].CombinationList.size()-1);k>=0;k--){
										if((dpTable[(i+1)%2][j].CombinationList.get(k).combineValue+cumulation) <= dpTable[(i+1)%2][inputK-1].maxValue)
											removeList.add(dpTable[(i+1)%2][j].CombinationList.get(k));
									}
								}
	
							}
						}
						if(removeList.size()!=0){
							//System.out.println(removeList.size());
							dpTable[(i+1)%2][j].CombinationList.removeAll(removeList);
						}
										
						dpTable[i%2][j] = (CombinationSet)dpTable[(i+1)%2][j].clone();
						cumulation = 0;
						removeList.clear();
					}
					else
						dpTable[i%2][j] = (CombinationSet)dpTable[(i+1)%2][j].clone();
				}
				catch(CloneNotSupportedException e){
					e.printStackTrace();
				}
				if(j==0){  //1個點的組合
					cb = new Combination(1);
					cb.CombinationPoint[0] = cSet[i];
					cb.combineValue = cSet[i].totalContribution;
					dpTable[i%2][j].CombinationList.add(cb);
				}
				else{
					for(int k=0 ; k<dpTable[(i+1)%2][j-1].CombinationList.size() ; k++){  //左下角的都加過來
						
						cb = new Combination(j+1);
						
						for(int l=0;l<j;l++)
							cb.CombinationPoint[l] = dpTable[(i+1)%2][j-1].CombinationList.get(k).CombinationPoint[l];							
						cb.CombinationPoint[j] = cSet[i];
						
						countCombineValue(cb);
						
//						if(j == (inputK-1)){
//							if(cb.combineValue > dpTable[i%2][j].maxValue){
//								dpTable[i%2][j].maxValue = cb.combineValue;
//								maxCombinePtr = cb;
//							}
//						}
//						else{
//							if(cb.combineValue > dpTable[i%2][j].maxValue)
//								dpTable[i%2][j].maxValue = cb.combineValue;
//							else if(cb.combineValue < dpTable[i%2][j].minValue)
//								dpTable[i%2][j].minValue = cb.combineValue;
//							dpTable[i%2][j].CombinationList.add(cb);
//						}
						
						if(cb.combineValue > dpTable[i%2][j].maxValue)
							dpTable[i%2][j].maxValue = cb.combineValue;
						else if(cb.combineValue < dpTable[i%2][j].minValue)
							dpTable[i%2][j].minValue = cb.combineValue;
						dpTable[i%2][j].CombinationList.add(cb);
						
					}
					if(firstCombine == true)
					{;}
					else if(j==(inputK-1) && dpTable[(i+1)%2][j-1].CombinationList.size()!=0 && dpTable[(i+1)%2][j].maxValue == 0)
						firstCombine = true;
				}
			}
			
			for(int j=1;j<inputK;j++){
				if(dpTable[i%2][j-1].maxValue + cSet[i+1].totalContribution <= dpTable[i%2][j].maxValue){
					nowOptimalK++;
				}
				else
					break;
			}
			
			if(nowOptimalK == inputK)
				return i;
			else{
				//System.out.println(nowOptimalK);
				nowOptimalK = 1;
			}
				
			for(int j=0;j<inputK;j++)
				dpTable[(i+1)%2][j].CombinationList = null;
		}
		
		return candidateNum-1;
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
