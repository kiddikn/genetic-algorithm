//EScomma.java
import java.util.*;

public class EScomma{
	public int node,v=0,seed,step,myu,lambda,max;
	public int[][] graph;       //グラフ
	public int[] solution;      //解候補
	public String flag;
	public Solution[] myuData;     //solutionを作成し、コピーする
	public Solution[] lambdaData;

	Random rnd = new Random();

	public void setGraph(int mat[][]){
		graph = mat;
	}

	public void setValue(int seed,int node,int myu,int lambda,int step,String flag){
		this.node = node;
		this.myu = myu;
		this.lambda = lambda;
		this.step = step;
		this.seed = seed;
		this.flag = flag;
		rnd.setSeed(this.seed);
		if(flag=="comma"){
			max = lambda;
		}else if(flag == "plus"){
			max = lambda + myu;
		}else{
			System.out.println("we cannot start this algorithm");
		}
	}
  
	//初期値の設定。1世代目の作成
	//[]myu個目のsolution,[][]番目の色
	public void setFirstGene(){
		myuData = new Solution[myu];
		if(flag == "plus")
			lambdaData = new Solution[max];
		else if(flag == "comma")
			lambdaData = new Solution[lambda];
		solution = new int[node];
		for(int i = 0;i < myu;i++){
			for(int j = 0;j < this.node;j++)
				solution[j] = rnd.nextInt(3);
			myuData[i] = new Solution(node,graph);
			myuData[i].setSolution(solution);
		}
		for(int i = 0;i < max;i++)
			lambdaData[i] = new Solution(node,graph);
	}

	//lambda回突然変異3回ランダム値作成
	public void mutation(){
		for(int count = 0;count < lambda;count++){
			int randMyu = rnd.nextInt(myu);
			int randPos = rnd.nextInt(node);  
			int randColor = 1;
			for(int i =0;i<node;i++)
				solution[i] = myuData[randMyu].solution[i];
			lambdaData[count].setSolution(solution); 

			while(true){
				randColor = rnd.nextInt(3);
				if(myuData[randMyu].solution[randPos]!=randColor)
					break;
			}
			lambdaData[count].mutate(randPos,randColor);
			//dump(lambdaData[count].solution);
			//System.out.println();
		}
	}
	public void mutation_plus(){
		for(int i = 0;i < myu;i++){
			for(int j = 0;j < node;j++)
				solution[j] = myuData[i].solution[j];
			lambdaData[i].setSolution(solution);
		}
		for(int count = myu;count < max;count++){
			int randMyu = rnd.nextInt(myu); 
			int randPos = rnd.nextInt(node);  
			int randColor = 1; 
			for(int i =0;i<node;i++)            
				solution[i] = myuData[randMyu].solution[i];     
			lambdaData[count].setSolution(solution);      
			while(true){                
				randColor = rnd.nextInt(3);      
				if(myuData[randMyu].solution[randPos]!=randColor)  
					break;      
			}
			lambdaData[count].mutate(randPos,randColor);            
		}
	}


	//n+1世代目選出のためのソートを行う
	public int sortSolution(){
		//lambdaDataのソートを行う。違反点数の数でソートしたい
		for(int i = 0;i < max;i++){
			lambdaData[i].addVio();
		}
		Arrays.sort(lambdaData,new MyComparator());
		//for(int k=0;k<max;k++)
			//System.out.print(lambdaData[k].v+"-");
			//System.out.print(lambdaData[0].v);
		//System.out.println();
		int testV = lambdaData[0].v;
		for(int i = 0;i < myu;i++){
			for(int j = 0;j < node;j++){
				solution[j] = lambdaData[i].solution[j];
			}
			myuData[i].setSolution(solution);
		}
		return testV;
	}

	public int getVioMaxNumber(){
		//実装する
		return myuData[myu].v;
	}

	public int getAverageNumber(){
		//実装する
		int tmp=0;
		for(int i = 0;i < myu;i++)
			tmp+=myuData[i].v;
		tmp/=myu;
		return tmp; 
	}

	public int getVioMinNumber(){
		//実装する
		return myuData[0].v;
	}

	public void dump(int[] s){
		for(int i = 0;i < node;i++)
			System.out.printf("%3d ",s[i]);
		System.out.println();   
	}


	public static void main(String args[]){
	  EScomma[] test = new EScomma[50];
		Matrix mat = new Matrix();
		int[] seeds = {113,127,131,139,151,157,163,251,257,271};
		int[] nodes = {30,60,90,120,150};
		int testcount = 0;
		///////////////////////////////////////////////////////
		for(int i = 0;i < nodes.length;i++){
			for(int j = 0;j < seeds.length;j++){//jはseeds
				int node = nodes[i];
				//int m = node * 3;
				int m = node*(node-1)/4;
				mat.setMatrix(node,m,149);
				mat.makeMatrix();
				test[testcount] = new EScomma();
				test[testcount].setGraph(mat.getMat());
				//setValue(seed,node,myu,lambda,step,String flag)
				//あとでstep削除
				test[testcount].setValue(seeds[j],node,10,100,150,"comma");
				test[testcount].setFirstGene();
				int count = 0;
				int tesv = 1110;
				while(count < 1000){
					//繰り返し回数
					count++;
					test[testcount].mutation();
					tesv = test[testcount].sortSolution();
					//ここにgetVioMaxNumberとgetVioMinNumber
					if(tesv == 0){
						System.out.println("success!!"+"node:"+node+"seed:"+seeds[j]+"**count ="+count);
						break;
					}
				}
				if(tesv != 0){
					System.out.println("fail");
				}
				testcount++;
			}
		}
	}

}



