//
//myfitnessScalingComp.java
//
import java.util.*;
class MyfitnessComp implements Comparator<Solution> {

	public int compare( Solution a, Solution b ) {
		if(a.fitness - b.fitness > 0){
			return 1;}
		else if(a.fitness - b.fitness < 0 ){
			return -1;}
		else{return 0;}
	}
}

