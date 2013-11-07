//EScomma.java
import java.util.*;

public class EScomma{
	public int node,v=0,seed,step,myu,lambda;
	public int[][] graph;       //グラフ
	static int[] solution;      //解候補
	public Solution[] myuData;     //solutionを作成し、コピーする
	public Solution[] lambdaData;

	Random rnd = new Random();

	public void setGraph(int mat[][]){
		graph = mat;
	}

	public void setValue(int seed,int node,int myu,int lambda,int step){
		this.node = node;
		this.myu = myu;
		this.lambda = lambda;
		this.step = step;
		this.seed = seed;
		rnd.setSeed(this.seed);
	}
  
	//初期値の設定。1世代目の作成
	//[]myu個目のsolution,[][]番目の色
	public void setFirstGene(){
		myuData = new Solution[myu];
		lambdaData = new Solution[lambda];
		solution = new int[node];
		for(int l = 0;l < node;l++)
			solution[l]=0;
		for(int i = 0;i < myu;i++){
			for(int j = 0;j < this.node;j++)
				solution[j] = rnd.nextInt(3);
			/*for(int k = 0;k<node;k++)
				System.out.print(" "+solution[k]);
			System.out.println();*/
			myuData[i] = new Solution(node,graph);
			myuData[i].setSolution(solution);
			/*for(int k = 0;k<node;k++)        
				System.out.print("*"+myuData[i].solution[k]);   
			System.out.println(); */
		}
		for(int i = 0;i < lambda;i++)
			lambdaData[i] = new Solution(node,graph);
		/*for(int i = 0;i < myu;i++){
			for(int j = 0;j<node;j++)
				System.out.print(myuData[i].solution[j]);
			System.out.println();
			}*/
	}

	//lambda回突然変異3回ランダム値作成
	public void mutation(){
		for(int count = 0;count < lambda;count++){
			int randMyu = rnd.nextInt(myu);
			int randPos = rnd.nextInt(node);  
			int randColor = 1;
			/*lambdaData[count] = 
				new Solution(node,graph,myuData[randMyu].solution);
				*/
			for(int i =0;i<node;i++)
				solution[i] = myuData[randMyu].solution[i];
			lambdaData[count].setSolution(solution); 

			//for(int k = 0;k<node;k++)     
			//System.out.print(lambdaData[count].solution[k]);  
			//System.out.println();
			//dump(myuData[randMyu].solution);
			while(true){
				randColor = rnd.nextInt(3);
				if(myuData[randMyu].solution[randPos]!=randColor)
					break;
			}
			//System.out.print(randMyu+"--");
			//System.out.print(randPos);
			//			dump(lambdaData[count].solution);
			//		System.out.print("randMyu"+randMyu+"randPos"+randPos+"randColor"+randColor);
			//	System.out.println();
			lambdaData[count].mutate(randPos,randColor);
			// dump(lambdaData[count].solution);
			//System.out.println();    
			//	dump(lambdaData[count]);   
		}
	}

	//n+1世代目選出のためのソートを行う
	public int sortSolution(){
		//lambdaDataのソートを行う。違反点数の数でソートしたい
		/////////////////**************************************************//////
		for(int i = 0;i < lambda;i++){
			lambdaData[i].addVio();
		//	System.out.println(lambdaData[i].v);
		}
		Arrays.sort(lambdaData,new MyComparator());
		//for(int i = 0;i < lambda;i++)  
			//dump(lambdaData[i]);	
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
		//lambdaData[max].v
		return 1000;
	}

	public int getAverageNumber(){
		//実装する
		//for(int i = 0;i < max(lambdaかlambda+myu);i++)
		//tmp=0;
		//tmp+=lambdaData[i].v;
		//tmp/=max;
		//return tmp; 
		return 1000;
	}

	public int getVioMinNumber(){
		//実装する
		//lambdaData[0].v
		return 1000;
	}

	public void dump(int[] s){
		for(int i = 0;i < node;i++)
			System.out.printf("%3d ",s[i]);
		System.out.println();   
	}


	public static void main(String args[]){
		EScomma test1 = new EScomma();
		Matrix mat = new Matrix();
		int node = 30,m = node*(node-1)/4;
		mat.setMatrix(node,m,149);
		mat.makeMatrix();
		test1.setGraph(mat.getMat());
		//setValue(int seed,int node,int myu,int lambda,int step)
		test1.setValue(157,node,10,100,1000);
		test1.setFirstGene();
		int count = 0;
		int tesv = 1110;
		for(int i = 0;i < 1000;i++){
      //繰り返し回数
			count++;
			test1.mutation();
			tesv = test1.sortSolution();
			//ここにgetVioMaxNumberとgetVioMinNumber
			if(tesv == 0){
				System.out.println("success!!count ="+count);
				break;
			}
		}
		if(tesv != 0){
			System.out.println("fail");
		}
	}

}



