//Matrix.java
import java.util.*;

public class Matrix{
	public int n,m,div;
	private int upperTriangle,edge;
	public int[][] mat;
	
	Matrix(){}

	Matrix(int n,int m){                                                                                                                                  
		this.n = n;
		this.m = m;
		mat = new int[n][n];
		//Step1 
		for (int k = 0; k < n; k++) {
			for (int l = 0; l < n; l++) mat[k][l] = 0;
		}
		div = n/3;
		//上三角行列の数
		upperTriangle = div*div*3;
		//(0,1)と(0,2)の数
		edge = div*div*2;
	}

	public void setMatrix(int n,int m,int seed){
		this.n = n;
		this.m = m;
		rnd.setSeed(seed);
		mat = new int[n][n];
		//Step1 
		for (int k = 0; k < n; k++) {
			for (int l = 0; l < n; l++) mat[k][l] = 0;
		}
		div = n/3;
		//上三角行列の数
		upperTriangle = div*div*3;
		//(0,1)と(0,2)の数
		edge = div*div*2;
	}

	Random rnd = new Random(); 
	//乱数を制御して処理速度を上げるためのリスト
	HashSet<Integer> rndset = new HashSet<Integer>();

	//行列の作成
	public void makeMatrix(){
		int i,j,l;
		l = 0;
		rndset.clear();
		for(int k = 0;k <= m*40;k++){
			if(l<m){
				int r = rnd.nextInt(upperTriangle);
				/////////////////////////////////////////////////////////////////////////////////////
				if(rndset.add(r)){
				//乱数で1を設置
					if(r <= edge - 1){
						i = r / (div*2);
						j = r - div*2*i + div;
					}else{
						i = div + (r - edge)/div;
						j = 2*div + (r - edge) - (i - div)*div;  
					}
					mat[i][j] = 1;
					l++;
					i = 0;
					j = 0;
				}
				if(k==m*40) System.out.println("we cannot have suitable data\n");
			}else{break;}
		}
	}

	public int[][] getMat(){
		return mat;
	}

	public void setM(int m){
		this.m = m;
	}
}

