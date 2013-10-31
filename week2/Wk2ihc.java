//kadai2 IHCを実行するためのプログラム
public class Wk2ihc{
	public static void main(String[] args){
		//seed151,matrixseed157がIHCに有効
		//733,739,809疎結合でsuccess
		int m,node,repetition,seed,restart;//変数repetitionはシミュレーションの繰り返し回数、restartは繰り返し山登り法の繰り返し回数
		//以下のseedsは適切な値を調べるための実験用
		/*int[] seeds = {59,61,71,73,79,83,89,97,101,103,107,109,
			113,127,131,137,709,719,727,733,739,743,
			751,757,809,997,991,983,977,971,967,953}; 
		 */
		int[] seeds = {113,127,131,139,151,157,163,251,257,271};
		int[] nodes = {30,60,90,120,150};

		//IHCではrestart
		//色塗り問題の生成
		repetition = 200;
		//for(int k = 0;k < 10;k++){
			//node =nodes[k];
	//		System.out.println("---------------node:"+node+"----------------");
			HillClimbing hc = new HillClimbing();
			//for(int so = 0;so < 10;so++){
				//seed = seeds[so];
				//System.out.println("seed:"+seeds[so]);   
			seed=157;
			node=30;	
			Matrix mat = new Matrix();
				m = 3 * node;        //疎結合
				//m = node*(node-1)/4;   //密結合
				mat.setMatrix(node,m,149);
				mat.makeMatrix();
				hc.setvData();
				hc.setGraph(mat.getMat());
				System.out.println("疎結合");            
				hc.setSeed(seed);
				//繰り返し山登り法
				int a=0;
				for(restart=0;restart<10;restart++){
					hc.setSolution(node);
					a=hc.simulateHC(m,repetition);
					if(a!=0)break;
				}
			}
		}
	//}
//}


