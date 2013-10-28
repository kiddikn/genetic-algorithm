public class Wk1{
	public static void main(String[] args){
   int n,m;
	 n = 9;
	 m = n*(n-1)/4;
	 System.out.println("密結合問題");
	 Matrix mat = new Matrix(n,m);
	 mat.setMatrix(n,m,157);
	 mat.makeMatrix();
	 Output.printMatrix(mat.getMat());

	 m = n*3;
	 System.out.println("疎結合問題");     
	 //Matrix mat2 = new Matrix(n,m);
	 Matrix mat2 = new Matrix();
	 mat2.setMatrix(n,m,157);
	 mat2.makeMatrix();
	 Output.printMatrix(mat2.getMat());
	 //out.exCsv(mat2.getMat(),"疎結合問題");
	}
}
