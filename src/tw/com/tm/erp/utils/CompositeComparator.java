/**
 * Copyright © 2008 Tasameng Corperation. All rights reserved.
 * -----------------------------------------------------------
 * Create Date Mar 27, 2008
 */
package tw.com.tm.erp.utils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dumars.Tsai
 */
public class CompositeComparator implements Comparator {
	
	private List comparators = new LinkedList();
	
	/** get the comparators, you can manipulate it as need. */ 
    public List getComparators() {
		return comparators;
	}

    /** add a batch of comparators to the condition list */ 
    public void addComparators(Comparator[] comparatorArray) {
		if (comparatorArray == null) {
			return;
		}
		for (int i = 0; i < comparatorArray.length; i++) {
			comparators.add(comparatorArray[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		 for (Iterator iterator = comparators.iterator(); iterator.hasNext();) {
			Comparator comparator = (Comparator) iterator.next();
			int result = comparator.compare(o1, o2);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}
}
