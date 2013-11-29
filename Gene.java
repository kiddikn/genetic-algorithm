//Gene.java
import java.util.*;
import java.lang.Object;

public class Gene{
	//dはベキ乗スケーリング用
	public int node,v=0,seed,m,size,scalingFlag,d;
	public int[][] graph;       //グラフ
	public int[] solution;      //解候補
	public int[] solution2;    //一様交叉用配列
	public int[] maskbit;
	public double mutateP;
	public Solution[] gene;     //solutionを作成し、コピーする
	public Solution[] tmpGene;
	Random rnd = new Random();

	public void setGraph(int mat[][]){
		graph = mat;
	}

	public void setValue(double mutateP,int seed,int node,int m,int size,int sFlag,int d){
		this.mutateP = mutateP;
		this.node = node;
		this.m = m;
		this.seed = seed;
		this.scalingFlag = sFlag;
		this.size = size;
		this.d = d;
		rnd.setSeed(this.seed);
	}

	//(1)初期値の設定。1世代目の作成
	public void setFirstGene(){
		gene = new Solution[size];
		tmpGene = new Solution[size+1];
		solution = new int[node];
		solution2 = new int[node]; 
		maskbit = new int[node];
		for(int i = 0;i < size;i++){
			for(int j = 0;j < this.node;j++){
				solution[j] = rnd.nextInt(3);
				solution2[j] = 0;
				maskbit[j] = 0;
			}
			gene[i] = new Solution(node,graph);
			gene[i].setSolution(solution);
		}
		for(int i = 0;i < size+1;i++)
			tmpGene[i] = new Solution(node,graph);
	}

	//(3)適応度評価,適応度求めてソートを行う
	public void setFitness(){
		double fmax=0,fmin=1;
		//違反点数をつける
		for(int i = 0;i < size;i++){
			gene[i].addVio();
			//適応度算出
			double t = gene[i].v;
			gene[i].setFitness(1-t / m);
			if(fmax<gene[i].fitness)fmax = gene[i].fitness;
			if(fmin>gene[i].fitness)fmin = gene[i].fitness; 
		}
		if(fmax!=fmin){
			for(int i = 0;i < size;i++){             
				double f = gene[i].fitness;
				//スケーリング
				if(scalingFlag==1){  //線形スケーリング
					double fs = (f-fmin)/(fmax-fmin); 
					gene[i].setFitnessScaling(fs);
				}else if(scalingFlag==2){ //ベキ乗スケーリング
					double fs = Math.pow(f,this.d);
					gene[i].setFitnessScaling(fs);
				}
			}
		}else{ 
			for(int i = 0;i < size;i++){
				double f = gene[i].fitness;   
				if(scalingFlag==1){ 
					//System.out.println("fmax is equal to fmin.");
					//double fs = (f-fmin+1)/(fmax-fmin+1); 
					gene[i].setFitnessScaling(f);
				}else if(scalingFlag==2){
					double fs = Math.pow(f,this.d);  
					gene[i].setFitnessScaling(fs);        
				}
			}
		}


		//適応度が小さい順にソート
		Arrays.sort(gene,new MyfitnessComp());
	}

	//(4)選択、エリート保存とルーレット戦略(5)交叉、一様交叉(6)突然変異
	public void selection(){
		double total = 0;
		for(int i = 0;i < size;i++)
			total += gene[i].fitnessScaling;
		//世代共通のマスクビット
		for(int i = 0;i < node;i++)
			maskbit[i] = rnd.nextInt(2);
		//System.out.println(total);
		for(int j = 0;j < size;j+=2){
			int father = roulette(total);
			int mother = roulette(total);			
			for(int i = 0;i < node;i++){
				solution[i]  = gene[father].solution[i];
				solution2[i] = gene[mother].solution[i];
			}
			//一様交叉
			crossover(solution,solution2,maskbit);
			//一時的にtmpGeneに退避
			tmpGene[j].setSolution(solution);
			tmpGene[j+1].setSolution(solution2);
		}
		//エリートの保存
		for(int i = 0;i < node;i++)
			tmpGene[size].solution[i] = gene[size-1].solution[i];
		for(int i = 0;i < size+1;i++){
			tmpGene[i].addVio();
			double t = tmpGene[i].v;
			tmpGene[i].setFitness(1-t / m);
		}	
		Arrays.sort(tmpGene,new MyfitnessComp());
		mutate();
		Arrays.sort(tmpGene,new MyfitnessComp());

		//tmpから本配列geneにコピー。
		//エリート戦略だから適応度最小を捨てる
		for(int i = 1;i < size+1;i++){
			for(int j = 0;j < node;j++)
				solution[j] = tmpGene[i].solution[j];
			gene[i-1].setSolution(solution);
		}
		setFitness();		
	}

	//一様交叉,solutionとsolution2を交叉させて保存
	public void crossover(int[] a,int[] b,int[] mask){
		int tmp=2;
		for(int i = 0;i < node;i++){
			//maskbitが1のときだけ反転
			if(mask[i]==1){
				tmp = a[i];
				a[i] = b[i];
				b[i] = tmp;
			}
		}
	}

	//ルーレットであたった個体番号をかえす
	public int roulette(double total){
		double dart = total * rnd.nextDouble();
		double sum = 0;
		int i;
		for(i = 0;i < size;i++){
			sum += gene[i].fitnessScaling;
			if(dart < sum)
				break;
		}
		return i;
	}

	//(6)突然変異
	public void mutate(){
		//最適解は突然変異させない
		for(int i = 0;i < size-1;i++){
			for(int j = 0;j < node;j++){
				solution[j] = gene[i].solution[j];
				double nowP = rnd.nextDouble();
				//System.out.println(nowP+"nowP,mutateP"+mutateP);
				if(nowP < mutateP){
					int tmpColor = solution[j];
					int nextColor = 1;
					while(true){
						nextColor = rnd.nextInt(3);
						if(nextColor != tmpColor)
							break;
					}
					solution[j] = nextColor;
				}
			}
			gene[i].setSolution(solution);
		}

	}

	//適応度の最大値を表示し返す
	public double getFitnessMaxNumber(){
		//System.out.println(gene[size-1].fitness);
		return gene[size-1].fitness;
	}

	//適応度の平均値を表示し返す
	public double getFitnessAveNumber(){
		double tmp=0;
		for(int i = 0;i < size;i++)
			tmp += gene[i].fitness;
		tmp/=size;
		//System.out.println(tmp);
		return tmp;	
	}

	//適応度の最小値を表示し返す
	public double getFitnessMinNumber(){
		//System.out.println(gene[0].fitness);
		return gene[0].fitness;
	}

	//配列を表示
	public void dump(int[] s){
		for(int i = 0;i < node;i++)
			System.out.printf("%d ",s[i]);
		System.out.println();   
	}

	public int[] getSolution(int i){
		return gene[i].solution;
	}


	public static void main(String args[]){
		
		Gene[] test = new Gene[40];
		Matrix mat = new Matrix();
		int[] seeds = {113,127,131,139,151,157,163,251,257,271};
		int[] nodes = {30,60,120,150};   
		int testcount = 0;  
		int rep = 1000;
		int state;
		int size = 100;
		for(int i = 0;i < nodes.length;i++){          
			for(int j = 0;j < seeds.length;j++){//jはseeds    
				state = 0;
				int node = nodes[i];  
				double ans = 0;
				//int m = node * 3;          //疎結合                
				int m = node*(node-1)/4;   //密結合             
				mat.setMatrix(node,m,149);    
				mat.makeMatrix();     
				test[testcount] = new Gene();
				test[testcount].setGraph(mat.getMat());      
				test[testcount].setValue(0.1,seeds[j],node,m,size,1,4);
				test[testcount].setFirstGene();           
				int count = 0;     
				while(count < rep){      
					count++;
					ans = test[testcount].getFitnessMaxNumber();
					if(ans==1){
						state = 1;
						test[testcount].dump(test[testcount].gene[size-1].solution);
						break;
					}
					//以下実際の処理
					//適応度評価
					test[testcount].setFitness();
					test[testcount].selection();//選択し交叉
				}
				String s = (state==1)? "success":"fail";
				System.out.println(s + "node:"+nodes[i]+"seed:"+seeds[j]+"max"+ans);
				testcount++;
			}
		}
	}

}



