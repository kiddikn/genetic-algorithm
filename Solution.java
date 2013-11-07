import java.util.*;

public class Solution{
	static int node;
	public int v;
	static int[][] graph;
	//できればprivateにしてセッターをつくる
	//staticにしたらクラス間で共有される
	public int[] solution;

	Random rnd = new Random();
/*	Solution(int[] sol){
		for(int i = 0;i < node;i++)
			solution[i] = sol[i];
		v = 0;
	}*/

	Solution(int node,int[][] mat){
		this.node = node;
		this.graph = mat;
		solution = new int[node];
		for(int i = 0;i < node;i++)
			solution[i] = 0;
		v = 0;
	}

	public void setSolution(int[] sol){
		for(int i = 0;i < node;i++)
			solution[i] = sol[i];
	}

	public void setStaticValue(int node,int[][] mat){
		this.node = node;
		this.graph = mat;
		solution  = new int[this.node];
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
