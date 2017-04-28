
package MACE; 

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
 *    MACEAbstractLearningSelector.java
 *    Copyright (C) 2004 Francesco De Comité
 *
 */
 
import algorithms.*;
import qlearning.*;
import environment.*;
import agents.*;
import dataset.Dataset;

/** The basic Q-Learning algorithm adapted to the NashQ

	<a href="http://www.cs.ualberta.ca/~sutton/book/ebook/node65.html">Sutton & Barto p 149 Q-Learning</a>

 @author Francesco De Comite (decomite at lifl.fr)
 @version $Revision: 1.0 $ 


*/

public class MACESelector extends QLearningSelector {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MACESelector(){   
	   super();
    }
	
    
    public MACESelector(IDefaultValueChooser dvc){
	   super(dvc);
    }
	
	public void learn(IState s1, IState s2, MACEComposedAction ca,double r) {
		throw new MACEException ("ERROR! This method can not be instantiated in this class");		
	}
		
	/*  add
	 *  Adds an agent to the memory structure
	 *  @param ag Agent
	 */
	public void add(MACEAgent ag) {
		throw new MACEException ("ERROR! This method can not be instantiated in this class");
    }
	
	/** getChoice
	    @param ag Agent asking for the choice
	    @param  l List of possible actions to choose from
	    @return Choosen action
	    Only Epsilon-greedy choice for MAC Nash-Q
	    TODO: expand it to other choosing methods
	  */  
	//                             +------------- This defines the agent for which the choice is made  
	//                             V
	//                       ==============  
	public IAction getChoice(IAgent ag,ActionList l){
		throw new MACEException ("ERROR! This method can not be instantiated in this class");	    
	}
	
	
	/** extractDataset
	  * Required by Interface but not implemented, will throw an exception if used
	  */		
	public Dataset extractDataset() {
	    throw new MACEException ("ERROR! This method can not be instantiated in this class");
	}
	
	
     /** toString()
      *  @return Q Function for all Agents
     */
      
     public String toString() {	    
	    throw new MACEException ("ERROR! This method can not be instantiated in this class"); 
	    
	}
}    
