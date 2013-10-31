//kadai2 HCを実行するためのプログラム
public class Wk2{
	public static void main(String[] args){
		int m=0;
		int node,repetition,seed;//変数
		int[] seeds = {113,127,131,139,151,157,163,251,257,271}; 
		int[] nodes = {30,60,90,120,150};
		//レポート用に実行回数を入れる配列を作成
		int[][] dataReport;
		dataReport = new int [10][10]; 
		for(int i=0;i<10;i++)                
			for(int j=0;j<10;j++)                
				dataReport[i][j]=0;    
		//IHCではrestart
		//色塗り問題の生成
		node = 120;
		repetition = 200;
		int l = 0;//すべてのデータを配列にいれて保管するための変数
		for(int k = 0;k <5;k++){
			node = nodes[k];
			System.out.println("---------------node:"+node+"----------------");   
			HillClimbing hc = new HillClimbing();
			//密結合seedを変えて10回実験
			for(int den = 0;den < 10;den++){
				seed = seeds[den];
				System.out.println("seed:"+seeds[den]);
				Matrix mat = new Matrix();
				//シミュレーションの開始
				m = node*(node-1)/4;  //密結合
				mat.setMatrix(node,m,149);
				mat.makeMatrix();
				hc.setGraph(mat.getMat());
				System.out.println("密結合");   
				hc.setSeed(seed);
				hc.setCount();
				hc.setSolution(node);
				dataReport[l][den]=hc.simulateHC(m,repetition);
				}
			l++;
			//疎結合seedを変えて10回実験
			for(int so = 0;so < 10;so++){
				Matrix mat2 = new Matrix();
				seed = seeds[so];     
				m = 3 * node;        //疎結合
				mat2.setMatrix(node,m,149);
				mat2.makeMatrix();
				hc.setGraph(mat2.getMat());
				System.out.println("疎結合");            
				System.out.println("seed:"+seeds[so]);
				hc.setSeed(seed);
				hc.setCount();
				hc.setSolution(node);
				dataReport[l][so]=hc.simulateHC(m,repetition);
			}
			l++;
		}
		//Output.reportData(dataReport);
	}
}


