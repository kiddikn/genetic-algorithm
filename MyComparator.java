//
//myComparator.java
//
import java.util.*;
class MyComparator implements Comparator<Solution> {

	public int compare( Solution a, Solution b ) {
		return a.v - b.v;
	}
}

