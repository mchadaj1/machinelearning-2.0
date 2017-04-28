

/**
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
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA.
 */

/*    MACEAction.java
 *    Apr 07
 * 	  
 *    Copyright (C) 2007 Francesco De Comit�
 *    Copyright (C) 2007 Pedro Colla
 *
 *    This is a joint effort to implement an alternative Multi-Agent Cooperative framework (MAC)
 */
package MACE;

/**
 * MACEAction.java
 * 
 * Copyright (C) 2007 Francesco De Comit� (decomite_at_lifl.fr)
 *           (C) 2007 Pedro Colla
 *
 */


import environment.*;
import agents.*;
import algorithms.*;
import referees.*;
import Log.*;
//import SPI.*;
import java.util.*;

public class MACEAction implements IAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /** The game's rules */
    protected IEnvironment myEnvironment; 
    protected IState oldState;
    protected IState currentState;
   
	public MACEAction(){
		
		if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
		//this.myEnvironment=u;
	}

		/** Clone an Action. */

	public IAction copy() {
	  
	  throw new MACEException ("ERROR! This method can not be instantiated in this class");
	  
    } 
    /** setcurrentState
     *  This is a modification of applyAction(IState,double) in the PiQLe architecture 
     *  the currentState is overriden from the arbitre.
     */
	public void setcurrentState(IState s) {
		this.currentState=s;
    }
    /** setoldState
      * This is a modification of applyAction(IState,double) in the PiQLe architecture 
      * the oldState is overriden from the arbitre.
      */
    public void setoldState(IState s) {
		this.oldState=s;
    }
    public IAction getNullAction() {
	  throw new MACEException ("ERROR! This method can not be instantiated in this class");
    }    
    
     
	// For use with Neural Networks
	
	/** Size of an Action's coding (for NN). */
	public int nnCodingSize() {
		throw new MACEException ("ERROR! This method can not be instantiated in this class");
	
    }		 

	/** Action's coding (for NN). */
	public double[] nnCoding() {
		throw new MACEException ("ERROR! This method can not be instantiated in this class");
	
    }	
	

	/** Q-Learning memorizing techniques use hashcoding : it is necessary to redefine it for each problem/game */
	public int hashCode() {
		throw new MACEException ("ERROR! This method can not be instantiated in this class");
	  
    }	
	


	/** Q-Learning memorizing techniques use equality: it is necessary to redefine it for each problem/game */
	public boolean equals(Object o) {
		throw new MACEException ("ERROR! This method can not be instantiated in this class");
	  
    }  
    //public boolean isFinal(){
	//  return this.myEnvironment.isFinal(this); 
    //}

    public void setEnvironment(IEnvironment ct){this.myEnvironment=ct;}
    
    
    //public ActionList getActionList(){
	//  return myEnvironment.getActionList(this); 
    //}
	
       
    //public IState modify(IAction a){
	//  return this.myEnvironment.successorState(this,a); 
    //}

    public IEnvironment getEnvironment(){return myEnvironment;}

    //public double getReward(IState old,IAction a){
	//return this.myEnvironment.getReward(old,this,a); 
    //}
	
}
