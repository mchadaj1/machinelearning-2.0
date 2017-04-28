
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
 *    MACEQLearningSelector.java
 *    Copyright (C) 2004 Francesco De Comité
 *
 */
 
import algorithms.*;
import qlearning.*;
import environment.*;
import agents.*;
import java.util.*;
import Log.*;
import java.lang.*;

import dataset.Dataset;

/** The basic Q-Learning algorithm adapted to the NashQ

	<a href="http://www.cs.ualberta.ca/~sutton/book/ebook/node65.html">Sutton & Barto p 149 Q-Learning</a>

 @author Francesco De Comite (decomite at lifl.fr)
 @version $Revision: 1.0 $ 


*/

public class MACEQLearningSelector extends MACESelector {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    //private QLearningSelector myl;
    private boolean ffirst=true;
	
	public MACEQLearningSelector(){
	   
	   super();
	   //this.myl=new QLearningSelector();
	   
	  
	   if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	   
	   
    }
	
    
    public MACEQLearningSelector(IDefaultValueChooser dvc){
	   
	   super(dvc);
	   //this.myl=new QLearningSelector(dvc);
	   if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	   
	   
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
		if (Trace.verbosity==true) Trace.getTraceObject().printTrace(); 
		return this.getChoice(l);   	  
		
		
		
			    
	}

    
    
    /*
	public void learn(IState s1, IState s2, IAction a,double r) {
		
		if (Trace.verbosity==true) Trace.getTraceObject().printTrace();  
		/*
		if (this.ffirst) {
			this.ffirst=false;
	        this.myl.setEpsilon(this.getEpsilon()); 
	        this.myl.setGamma(this.getGamma());
	        this.myl.setAlpha(this.getAlpha());
	        
	        
	        this.myl.setDecay(this.getDecay());
	        this.myl.setTau(this.getTau());
	        
	        this.myl.setAlphaDecayPower(this.getAlphaDecayPower());
	        this.myl.setGeometricDecay(this.getGeometricDecay());
	        
        }
        
		
		
		//Get the agent being processed	
		
        MACEState so=(MACEState)s1;
		MACEAgent ag=so.getAgent();
		//IAction act=ca.getAction(ag);
		if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Old("+s1+") New("+s2+") Action("+a+") Reward("+r+") prevQSA("+this.myl.getValue(s1,act)+")");
		this.myl.learn(s1,s2,a,r);
        if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Old("+s1+") New("+s2+") Action("+a+") Reward("+r+") postQSA("+this.myl.getValue(s1,act)+")");		
				
	}
	*/
	
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
	/*
	public IAction getChoice(IAgent ag,ActionList l){
		if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	  
	
		if (this.ffirst) {
			this.ffirst=false;
	        this.myl.setEpsilon(this.getEpsilon()); 
	        this.myl.setAlpha(this.getAlpha());
	        this.myl.setGamma(this.getGamma());
	        
	        
	        this.myl.setDecay(this.getDecay());
	        this.myl.setTau(this.getTau());
	        
	        this.myl.setAlphaDecayPower(this.getAlphaDecayPower());
	        this.myl.setGeometricDecay(this.getGeometricDecay());
        }
			
		return this.myl.getChoice(l);
		
		
			    
	}
*/	
	public String toString() {
		return this.memory.toString();
    }
   
}    
