//Gene.java
import java.util.*;
import java.lang.Object;

public class Gene{
	//dはベキ乗スケーリング用
	public int node,v=0,seed,m,size,mutateP,scalingFlag,d;
	public int[][] graph;       //グラフ
	public int[] solution;      //解候補
	public int[] solution2;    //一様交叉用配列
	public int[] maskbit;
	public Solution[] gene;     //solutionを作成し、コピーする
	public Solution[] tmpGene;
	Random rnd = new Random();

	public void setGraph(int mat[][]){
		graph = mat;
	}

	public void setValue(int seed,int node,int m,int size,int sFlag,int d){
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
		for(int i = 0;i < size;i++){             
			double f = gene[i].fitness;
			//スケーリング
			if(scalingFlag==1){  //線形スケーリング
				gene[i].setFitnessScaling((f-fmin)/(fmax-fmin));
			//	System.out.println(f+"==="+fmax+"==="+fmin);    
			//	System.out.println(gene[i].fitness);    
			//if(gene[i].fitness==1)System.out.println(i);
			//dump(gene[31].solution);
			}else if(scalingFlag==2){ //ベキ乗スケーリング
				gene[i].setFitness(Math.pow(f,this.d));
			}
		}
		//適応度が小さい順にソート
		Arrays.sort(gene,new MyfitnessComp());
//////////////////    for(int i = 0;i < size;i++)
//			System.out.println(gene[i].fitness+"-");           
	}

	//(4)選択、エリート保存とルーレット戦略(5)交叉、一様交叉
	public void selection(){
		double total = 0;
		for(int i = 0;i < size;i++)//{
			total += gene[i].fitnessScaling;
		//System.out.println(gene[i].fitness);}
		//System.out.println(total);
		//世代共通のマスクビット
		for(int i = 0;i < node;i++)
			maskbit[i] = rnd.nextInt(2);

		for(int j = 0;j < size-1;j+=2){
			int father = roulette(total);
			int mother = roulette(total);			
			//System.out.println(father+"fa-mo"+mother);
			for(int i = 0;i < node;i++){
				solution[i]  = gene[father].solution[i];
				solution2[i] = gene[mother].solution[i];
			}
			//一様交叉
			crossover(solution,solution2,maskbit);
			//dump(solution);
			//System.out.println(father+"."+mother);
			//System.out.println(j+"============================");
			//dump(tmpGene[j+1].solution);
			for(int i = 0;i < node;i++){
				//一時的にtmpGeneに退避
				tmpGene[j].solution[i] = solution[i];
				tmpGene[j+1].solution[i] = solution2[i];
			}		
		}
		//エリートの保存
		tmpGene[size]=gene[size-1];
		setFitness();
		//tmpから本配列geneにコピー。
		//エリート戦略だから適応度最小を捨てる
		for(int i = 1;i < size+1;i++){
			for(int j = 0;j < node;j++)
				solution[j] = tmpGene[i].solution[j];
			gene[i-1].setSolution(solution);
			gene[i-1].setFitness(tmpGene[i].fitness);
			gene[i-1].setFitnessScaling(tmpGene[i].fitnessScaling);  
			gene[i-1].addVio();
		}
				
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
		for(int i = 0;i < size;i++){
			for(int j = 0;j < node;j++){
				solution[j] = gene[i].solution[j];
				double nowP = rnd.nextDouble();
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
		System.out.println(tmp);
		return tmp;	
	}

	//適応度の最小値を表示し返す
	public double getFitnessMinNumber(){
		System.out.println(gene[0].fitness);
		return gene[0].fitness;
	}

	//配列を表示
	public void dump(int[] s){
		for(int i = 0;i < node;i++)
			System.out.printf("%d ",s[i]);
		System.out.println();   
	}


	public static void main(String args[]){
		Matrix mat = new Matrix();
		int seed = 131;
		int node = 90;
		int m = node*(node-1)/4;
		mat.setMatrix(node,m,149);
		mat.makeMatrix();
		Gene test1 = new Gene();
		test1.setGraph(mat.getMat());
		test1.setValue(seed,node,m,100,1,5);
		test1.setFirstGene();
		int count=0;
		int state = 0;
		while(count<1000){
			count++;
			//以下実際の処理
			//適応度評価
			test1.setFitness();//引数は常に必要,ベキ乗スケーリングのときに使う	
			//終了条件(探索成功かエリートが進化しなくなるまで)
			if(test1.getFitnessMaxNumber()==1){
				state = 1;
				test1.dump(test1.gene[99].solution);
				break;
			}
			test1.selection();//選択し交叉
			test1.mutate();
	}
		//break後の処理
		String s = (state==1)? "success":"fail";
		System.out.println(s);
	}
	
}



