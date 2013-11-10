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
	public void setFirstGene(){
		myuData = new Solution[myu];
		lambdaData = new Solution[max];
		solution = new int[node];
		for(int i = 0;i < myu;i++){
			for(int j = 0;j < this.node;j++)
				solution[j] = rnd.nextInt(3);
			myuData[i] = new Solution(node,graph);
			myuData[i].setSolution(solution);
			/*System.out.print(myuData[i].v+"-");     
			System.out.print(i+"-");
			dump(myuData[i].solution);
			*/
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
			for(int i = 0;i < node;i++)
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
		int testV = lambdaData[0].v;
		for(int i = 0;i < myu;i++){
			for(int j = 0;j < node;j++){
				solution[j] = lambdaData[i].solution[j];
			}
			myuData[i].setV(lambdaData[i].v);
			myuData[i].setSolution(this.solution);
		}
	/*	for(int i = 0;i < max;i++){
			System.out.print(lambdaData[i].v+"--");               
			System.out.print(i+"-");
			dump(lambdaData[i].solution);
			System.out.println();
		}*/
		
			return testV;
	}

	public int getVioMaxNumber(){
		//実装する
		System.out.println(myuData[myu-1].v);
		return myuData[myu-1].v;
	}

	public int getAverageNumber(){
		//実装する
		int tmp=0;
		for(int i = 0;i < myu;i++)
			tmp+=myuData[i].v;
		tmp/=myu;
		System.out.println(tmp);
	 	return tmp;	
	}

	public int getVioMinNumber(){
		//実装する
		System.out.println(myuData[0].v);
		return myuData[0].v;
	}

	public void dump(int[] s){
		for(int i = 0;i < node;i++)
			System.out.printf("%d ",s[i]);
		System.out.println();   
	}


	public static void main(String args[]){
	  EScomma[] test = new EScomma[50];
		int[] x1 = new int[1000];
		int[] y1 = new int[1000];       
		int[] z1 = new int[1000];       
		for(int i = 0;i < 1000;i++){
			x1[i] = 0;
			y1[i] = 0;         
			z1[i] = 0;         
		}
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
				test[testcount].setValue(seeds[j],node,10,50,150,"comma");
				test[testcount].setFirstGene();
				int count = 0;
				int tesv = 1110;
				while(count < 1000){
					//繰り返し回数
					count++;
					//mutation_plusかmutation
					test[testcount].mutation();
					tesv = test[testcount].sortSolution();
					//ここにgetVioMaxNumberとgetVioMinNumber
					//x1[count-1]=test[testcount].getVioMinNumber();
					//y1[count-1]=test[testcount].getAverageNumber();
					//z1[count-1]=test[testcount].getVioMaxNumber();
					if(tesv == 0){
						System.out.println(
								"success!!"+"node:"+node+
								"seed:"+seeds[j]+"**count ="+count);
						//最初のデータのみファイルに書き込む
						//調整用
						if(testcount == 0)
							//Output.exCsv_Graph(x1,y1,z1);
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



