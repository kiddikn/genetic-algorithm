//HillClimbing.java
import java.util.*;

public class HillClimbing{
	public int n,m,v=0,v1,v2,seed,repetition;
	public int[][] graph;       //グラフ
	static int[] solution;      //解候補
	public int[] vData;
	int count=0;
	Random rnd = new Random();

	public void setGraph(int mat[][]){
		graph = mat;
	}

	HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
	public void violation(int v){
		this.v = v;
		//step3:制約に違反している変数を選択
		int nextValue = 1122;
		int size=alternative.size();
		if(size == 0){
			System.out.println("size equals to 0 means this program has error."); 
		}else{
			int nv = rnd.nextInt(size);
			int check = 0;
			Iterator it = alternative.iterator();
			while(it.hasNext()){
				nextValue = (Integer)it.next();
				if(check==nv){
					break;
				}
				check++;
			}
			if(nextValue==1122)System.out.println("error");
		}
		//step4値の選択
		//nextValueのノードの色を調べる
		int cColor  = solution[nextValue]; 
		int cColor1 = (cColor+1)%3;
		int cColor2 = (cColor+2)%3;
		solution[nextValue] = cColor1;
		v1 = addVio();
		solution[nextValue] = cColor2;    
		v2 = addVio();
		//minで受け取ったインデックスをもとに値を得るmap(配列でもいい)
		map.clear();
		map.put(0,cColor);
		map.put(1,cColor1);
		map.put(2,cColor2);
		solution[nextValue] = map.get(min(v,v1,v2));
	}

	//違反点数v,v1,v2をもとに最小になる値のインデックスを返す
	public int min(int a,int b,int c) {
		int min = a; 
		int check = 0;
		if(min > b){
			min = b;
			check = 1;
		}if(min > c){
			min = c;
			check = 2;
		}
		return check;
	}

	HashSet<Integer> alternative = new HashSet<Integer>();
	public int addVio(){
		//違反点数を計算する
		v=0;
		alternative.clear();
		for(int i = 0;i < n;i++){ 
			for(int j = i+1;j < n;j++){ 
				if(graph[i][j] == 1 && solution[i] == solution[j]){ 
					v++; 
					alternative.add(i);
					alternative.add(j);
				} 
			}
		}
		return v;
	}

	public int getSolution(int i){
		return solution[i];
	}


	public void setSolution(int node){
		this.n = node;
		solution = new int[this.n];
		//Step1 
		for (int k = 0; k < this.n; k++) {
			int r = rnd.nextInt(3);
			//初期値の生成
			solution[k] = r;
		}
	}

	public void setSeed(int seed){
		this.seed = seed;
		rnd.setSeed(this.seed);
	}

	public void setvData(){
		//vDataは違反点数vを保管しておく配列、一番上で定義
		vData = new int[10000];
		for(int z=0;z < 10000;z++)
			vData[z]=0;
	}

	//単体のHCを実行するメソッド
	public int simulateHC(int m,int repetition){
		this.repetition = repetition;
		this.m = m;
		//step2 終了判定
		int i=0,state=0,v=1,tmpV = 10000;
		//以下を変更。無限ループにして0か変化無しが100回連続になるまで繰り返す
		while(i<repetition){
			//for(i = 0;i < repetition;i++){
			v = addVio();
			if(tmpV==v){
				i++;
			}else{
				i=0;
				tmpV=v;
			}
			//下は散布図用
			//System.out.println(v);
			vData[count]=v;
			if(v == 0){
				state = 1;
				break;
			}
			//step3 変数の選択,step4値の選択
			violation(v);
			count++;
			//}
		}
		//step5
		if(state==1){
			System.out.println("success");
			System.out.println("Node:"+n+",Repetition Count:"+count);
			Output.exCsv_Graph(vData);
			return i;
		}else{
			System.out.println("failure");
			return 0;
		}
	}

}

