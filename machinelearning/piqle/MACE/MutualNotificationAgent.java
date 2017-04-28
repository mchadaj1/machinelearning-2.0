

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

/*    MutualNotificationAgent.java
 *    Apr 07
 * 	  
 *    Copyright (C) 2007 Francesco De Comité
 *    Copyright (C) 2007 Pedro Colla
 *
 *    This is a joint effort to implement an alternative Multi-Agent Cooperative framework (MAC)
 */
package MACE;

/**
 * MutualNotification.java
 * 
 * Copyright (C) 2007 Francesco De Comité (decomite_at_lifl.fr)
 *           (C) 2007 Pedro Colla (pcolla@frsf.utn.edu.ar)
 *
 */



import environment.*;
import agents.IAgent;
import algorithms.AbstractMemorySelector;
import algorithms.IStrategy;
import algorithms.ISelector;
import Log.Trace;
import dataset.Dataset;

import java.util.*;

public class MutualNotificationAgent extends MACEAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MutualNotificationAgent(String aid,IEnvironment s,ISelector al,Filter fi){
		super(aid,s,al,fi);
    }
     
     /* evaluate
      * Method to evaluate if a learning cycle has been successful or not
      * @return true if the cycle has been successful and false if not
      */ 
     public double evaluate() {
	     if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
	     MACESelector al=(MACESelector) this.algorithm;
         
	     double delta=this.currQSA-this.lastQSA;
	     if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Agent("+this.getId()+") Threshold("+this.threshold+") EVALUATE:("+delta+")");
	     if (delta >= this.threshold) {
		     return this.currQSA-this.lastQSA;
	     }    
	     return 0.0;
     }
        
     /* commit
      * Method to operate over the agent beliefs (if needed) if the learning cycle has been successful
      * sort of an end tail of the episode. Normally should be left without any action but it might
      * be overriden by some specific action
      */  
     public void commit() {
	     if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
	     if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Agent("+this.getId()+") Above Threshold, committing");
         return;
	 }
	 
	 
	 
	 
	 /* rollback
	  * if the learning cycle is deemed as unsuccessful this stamp back the original pre-learning values
	  */
	 public void rollback() {
		 if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
		 MACESelector al=(MACESelector) this.algorithm;
		 if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Agent("+this.getId()+") Below Threshold, rolling back");
		 al.setValue(this.lastfilteredPrevState,this.lastfilteredPostState,this.lastappliedAction,lastQSA);
    	 return;
     }
        	
     /** learn
     *  @param o Old State
     *  @param c Current State
     *  @param a Composed Action
     *  @param r Reward
     *  This method extracts what is relevant of the State using the filter for both the old and current States
     *  and extract the action from this agent out of the Composed action.
     *  The restricted states and the action are then sent to the algorithm for learning.
     */
    
    protected void learn(IState o,IState c,MACEComposedAction a,double r){
        if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
         
        //If learning is enabled    
	    if(this.learningEnabled) {
		   MACEState no=(MACEState)o;
		   MACEState nc=(MACEState)c;
		   
		   //Get the filtered state
           if (this.filter != null) {
		      no=(MACEState)this.filter.filterState(o,this.universe);
		      nc=(MACEState)this.filter.filterState(c,this.universe);
	       }   
	       
	       //Get the Algorithm 
	       //MACIAction ia=(MACIAction) a.get((Object)this);
	       no.setAgent(this);
	       MACESelector al=(MACESelector) this.algorithm;
	       IAction act=(IAction)a.getAction(this);
	       
	       //Get the Q(s,a) prior to the computation, also store the State and Action used to recover it.
	       this.lastQSA=al.getRewardStore().get(no,act);
	       this.lastfilteredPrevState=no;
	       this.lastfilteredPostState=nc;
	       this.lastappliedAction=act;
	       
	       //Go to learn
	       al.learn(no,nc,act,r);
	       
	       //Get the Q(s,a) after the learning has been made
	       this.currQSA=al.getRewardStore().get(no,act);
	       
	       
           if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Agent ("+this.getId()+") old Q(s,a)="+this.lastQSA+" new Q(s,a)="+this.currQSA);	       	       
	       if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Agent ("+this.getId()+") Q(s,a):"+al.toString());	       
	       
	       
        }   
        
	    return;	    
   } 
   
     
}
