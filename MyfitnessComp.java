//
//myfitnessScalingComp.java
//
import java.util.*;
class MyfitnessComp implements Comparator<Solution> {

	public int compare( Solution a, Solution b ) {
		if(a.fitnessScaling - b.fitnessScaling > 0){
			return 1;}
		else if(a.fitnessScaling - b.fitnessScaling < 0 ){
			return -1;}
		else{return 0;}
	}
}

