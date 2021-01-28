package uk.ac.nott.cs.g53dia.agent.ScoreAgent;

import java.util.ArrayList;

public class CombinationAndPermutation {
	
	public ArrayList<Object> permHelperList = new ArrayList<Object>();
	
	public ArrayList<ArrayList<Object>> outputList = new ArrayList<ArrayList<Object>>();
	
	public void perm(ArrayList<Object> A,int k,int n){
        int i;
        if(k == n){ 
            for(i = 0; i < n; i++){
                permHelperList.add(A.get(i));
            }
            outputList.add(permHelperList);
            permHelperList = new ArrayList<Object>();
        } 
        else {
            for(i = k; i < n; i++){
                Object temp;   
                temp = A.get(i);
                A.set(i, A.get(k));
                A.set(k, temp);
                perm(A, k+1, n);
                temp = A.get(i);
                A.set(i, A.get(k));
                A.set(k, temp);
            }
        }
    }
	
	public ArrayList<ArrayList<Object>> getSubsets(ArrayList<Object> set){
		ArrayList<ArrayList<Object>> allsubsets = new ArrayList<ArrayList<Object>>();
		int max = 1 << set.size(); //The number of subset
		for(int i=0; i<max; i++){
			int index = 0;
			int k = i;
			ArrayList<Object> s = new ArrayList<Object>();
			while(k > 0){
				if((k&1) > 0){
					s.add(set.get(index));
				}
				k >>= 1;
				index++;
			}
			allsubsets.add(s);
		}
		return allsubsets;
	}
	
	public ArrayList<ArrayList<Object>> getCombinationAndPermutation(ArrayList<Object> input) {
		 ArrayList<ArrayList<Object>> subsets = getSubsets(input);
		 permHelperList = new ArrayList<Object>();
		 outputList = new ArrayList<ArrayList<Object>>();
		 for(ArrayList<Object> A : subsets) {
			 perm(A,0,A.size());
	     }
		 return outputList;
	}
	
    public static void main(String args[]){
    	CombinationAndPermutation p = new CombinationAndPermutation();
        ArrayList<Object> b = new ArrayList<Object>();
        b.add(1);b.add(2);b.add(3);b.add(4);
        ArrayList<ArrayList<Object>> s = p.getCombinationAndPermutation(b);
        for(ArrayList<Object> set : s){
			System.out.println(set);
		}
    }
}
