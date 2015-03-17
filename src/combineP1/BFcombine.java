package combineP1;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BFcombine {
	
	public static ArrayList <CandidatePoint> cSet;
	
	public static void main(String[] args) {
		cSet = new ArrayList <CandidatePoint>();
		readCandidateInf();
	}
	
	private static void readCandidateInf(){
		
		BufferedReader br;
		String [] line;
		CandidatePoint p;
		
		try{
			br = new BufferedReader(new FileReader("candidateInf.out"));
			while(br.ready()){
				line = br.readLine().split(" ");
				p = new CandidatePoint();
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
				cSet.add(p);
			}
		}
		catch(IOException e){
			System.out.println("IOException: " + e);
		}
	}
}
