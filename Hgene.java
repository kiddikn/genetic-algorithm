//HibridGene.java
import java.util.*;
import java.lang.Object;

public class Hgene extends HillClimbing{

	public void setSolution(int[] ga){
		solution = new int[ga.length];
		for(int i = 0;i < ga.length;i++)
			this.solution[i] = ga[i];
	}

	public void setNode(int n){
		this.n = n;
	}

	public int getCount(){
		return this.count;
	}

	public static void main(String args[]){
		Gene[] test = new Gene[40];
		Hgene[] elite = new Hgene[400];
		Matrix mat = new Matrix();
		int[] seeds = {113,127,131,139,151,157,163,251,257,271};
		int[] nodes = {60,90,120,150};   
		int testcount = 0;  
		int rep = 1000;
		int state,flag=0;
		int size = 50;
		int repetition = 100;
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
				int counts = 0;
				int tmp = 0;
				int hcount = 0;
				while(count < rep){      
					count++;
					ans = test[testcount].getFitnessMaxNumber();
					if(ans==1){
						state = 1;
						//test[testcount].dump(test[testcount].gene[size-1].solution);
						break;
					}
					//以下実際の処理
					//適応度評価
					test[testcount].setFitness();
					test[testcount].selection();//選択し交叉
					//ハイブリッドする処理が以下に来る
					for(int hc = 0;hc < 10;hc++){
						counts = 0;
						flag = 0;
						elite[hcount] = new Hgene();
						elite[hcount].setGraph(mat.getMat());
						elite[hcount].setSeed(seeds[j]);
						elite[hcount].setCount();
						elite[hcount].setNode(nodes[i]);
						elite[hcount].setSolution(test[testcount].getSolution(size-1-hc));
						counts = elite[hcount].simulateHC(m,repetition);
						tmp += elite[hcount++].getCount();
						System.out.println("**");
						
						if(counts > 0){
							flag = 1;
							break;
						}
					}
					if(flag==1)break;
				}
				String s = (state==1)? "GAsuccess\n":"";
				String f = (flag==1)? "HGAsuccess\n":"";
				String g = (state==0&&flag==0)? "FAIL\n":"";
				System.out.println(s + f +"node:"+nodes[i]+"seed:"+seeds[j]+"max"+ans+"count:"+count+"HCcount"+tmp+"ans"+(count*size+tmp));
				testcount++;
			}
		}
	}

}



