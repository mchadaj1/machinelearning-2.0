
package algorithms; 
/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation; either version 2.1 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    alo0ng with this program; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA.
 */

/*
 *    QLearningSelector.java
 *    Copyright (C) 2004 Francesco De Comit�
 *
 */

import environment.IAction;
import environment.IState;
import qlearning.ActionStatePair;
import qlearning.RewardMemorizer;
import qlearning.IDefaultValueChooser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/** The basic Q-Learning algorithm. 

	<a href="http://www.cs.ualberta.ca/~sutton/book/ebook/node65.html">Sutton & Barto p 149 Q-Learning</a>

 @author Francesco De Comite (decomite at lifl.fr)
 @version $Revision: 1.0 $ 


*/

public class QLearningSelector extends  AbstractMemorySelector{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QLearningSelector(){
	memory=new RewardMemorizer(); 
    }
	
	public QLearningSelector(IDefaultValueChooser dvc){
		memory=new RewardMemorizer(dvc);
	}

    /** When <code>states</code> and <code>actions</code> are memorized, 
     * one can enumerate them and build histograms showing the distribution of Q(s,a) values


*/
    
    public void showHistogram(){
	((RewardMemorizer)memory).makeHistogram(); 
	((RewardMemorizer)memory).displayHistogram(); 
    }

	public void printBestActions() {
		Set<ActionStatePair> keys = new HashSet<ActionStatePair>((((RewardMemorizer) memory).getKeys()));
		Iterator enu = keys.iterator();
		ActionStatePair courante = null;
		Set<IState> done = new HashSet<>();
		while (enu.hasNext()) {
			courante = (ActionStatePair) enu.next();
			final IState state = courante.getState();
			if (!done.contains(state)) {
				final IAction a = bestAction(state);
				System.out.println("State: " + state + "->" + a + " = " + getValue(state, a));
				done.add(state);
			}
		}
	}
}    
