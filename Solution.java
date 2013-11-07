import java.util.*;

public class Solution{
	int node,v;
	public int[][] graph;
	//できればprivateにしてセッターをつくる
	//staticにしたらクラス間で共有される
	public int[] solution;

	Random rnd = new Random();
	Solution(int node,int[][] mat,int[] sol){
		this.node = node;	
		solution  = new int[node];
		for(int i = 0;i < this.node;i++)
			solution[i] = sol[i];
		v = 0;
		this.graph = mat;
	}

	public int getSolutionValue(int pos){
		return solution[pos];
	}

	public void mutate(int randPos,int randColor){
		solution[randPos]=randColor;
	}

	public void addVio(){
		//違反点数を計算する
		v=0;
		for(int i = 0;i < node;i++)
			for(int j = i+1;j < node;j++) 
				if(graph[i][j] == 1 && solution[i] == solution[j])
					v++; 
	}

}
