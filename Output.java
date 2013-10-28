//Output.java
import java.util.*;
import java.io.*;

public class Output{
	
	//コマンドラインに表示する
	public static void printMatrix(int mat[][]){
		System.out.println("mat = ");
		System.out.println("↑y");
		for(int i = 0;i < mat.length;i++){
			System.out.print("|");
			for(int j = 0;j < mat.length;j++) System.out.printf("%3d " ,mat[i][j]);
			System.out.println();
		}
		System.out.println("------------------------>x");
	}

	//csvファイルで行列を表示するn*n行列のみ
	public static void exCsv(int mat[][],String name){
		try{
			//FileWriterクラスのインスタンス作成
			PrintWriter fw = new PrintWriter(new BufferedWriter(new FileWriter("grid"+name+".csv")));
			//ファイルに書き込み
			for(int i = 0;i < mat.length;i++){
				for(int j = 0;j < mat.length-1;j++) fw.print(mat[i][j]+",");
				fw.print(mat[i][mat.length-1]);
				fw.println();
			}
			System.out.println("ファイルに書き込みました");
			//ファイルをクローズ
			fw.close();
		}catch(IOException e){
			System.out.println(e + "例外が発生しました");
		}
	}

	public static void reportData(int[][] data){//[何個目][回数]
		try{
			//FileWriterクラスのインスタンス作成
			PrintWriter fw = new PrintWriter(new BufferedWriter(new FileWriter("report_100data.csv")));
			//ファイルに書き込み
			fw.println();
			fw.println(" ,"+113+","+127+","+131+","+139+","+151+","+157+","+163+","+251+","+257+","+271);
			for(int i = 0;i < 10;i++){
				fw.print(" ,");
				for(int j = 0;j < 9;j++){
					fw.print(data[i][j]+",");
				}
				fw.print(data[i][9]);
				fw.println();
			}
			System.out.println("ファイルに書き込みました");
			//ファイルをクローズ
			fw.close();
		}catch(IOException e){
			System.out.println(e + "例外が発生しました");
		}
	}

	public static void exCsv_Graph(int[] vio){
		try{
			//FileWriterクラスのインスタンス作成
			PrintWriter fw = new PrintWriter(new BufferedWriter(new FileWriter("HI.csv")));
			//ファイルに書き込み
			for(int i = 0;i < vio.length;i++){
				fw.print(i+","+vio[i]);                                                                                                                                
				fw.println();
			}
			System.out.println("ファイルに書き込みました");
			//ファイルをクローズ
			fw.close();
		}catch(IOException e){
			System.out.println(e + "例外が発生しました");
		}
	}

}

