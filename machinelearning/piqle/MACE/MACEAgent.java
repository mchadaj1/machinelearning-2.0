

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

/*    MACEAgent.java
 *    Apr 07
 * 	  
 *    Copyright (C) 2007 Francesco De Comit�
 *    Copyright (C) 2007 Pedro Colla
 *
 *    This is a joint effort to implement an alternative Multi-Agent Cooperative framework (MAC)
 */
package MACE;

/**
 * MACEAgent.java
 * 
 * Copyright (C) 2007 Francesco De Comit� (decomite_at_lifl.fr)
 *           (C) 2007 Pedro Colla (pcolla@frsf.utn.edu.ar)
 *
 */



import environment.*;
import agents.IAgent;
import algorithms.IStrategy;
import algorithms.ISelector;
import Log.Trace;
import dataset.Dataset;

import java.util.*;

public class MACEAgent implements IAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected   HashMap<String,MACEAgent> neighbours;
	protected String id;
	protected double globalReward;
	public    MACEFilter filter;
	
    /** The current state of the agent. */
	protected IState currentState = null;
	
	/** The algorithm which chooses the action among the liste of possible ones.xx*/
	protected ISelector algorithm;
	
	/** The universe in which the agent lives (to allow the referee to communicate with graphical interface) */
	protected IEnvironment universe;
	
	/** The last action performed by this agent */
	protected IAction lastAction=null;
	
	/** the last Q(s,a), the filtered state it's stored under and the applied action achieved */
	protected double  lastQSA;
	protected double  currQSA;
	protected IState  lastfilteredPrevState;
	protected IState  lastfilteredPostState;
	protected IAction lastappliedAction;
	protected boolean broadcast=false;
	protected double  threshold=0.0000001;
	
	
	/** The previous state of the Agent */
	protected IState oldState=null;
	
	/** Learning state*/
	protected boolean learningEnabled=true;
	
	public MACEAgent(String aid,IEnvironment s,ISelector al,Filter fi){
		//super(aid,s,al,fi); 
		if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
		// REMARK Instantiate HashMap types
		this.neighbours=new HashMap<String,MACEAgent>();
		
		//Store the link to the environment
		this.universe=s;
		
		//Store the link to the algorithm
		this.algorithm=al;
		
		//Store the link to the filter
		
		this.filter=(MACEFilter)fi;
		if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Created Filter for Agent("+this+")");
		
		this.filter.owner=this;
		if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Created Filter for Agent("+this+")");
		
		//This is the Id of this agent
		this.id=aid;
		
		//Just some trace data placed here in case verbosity is raised
		if (Trace.verbosity==true) Trace.getTraceObject().printClass(this);
	}
	       
    /** getAlgorithm
     *  @return Algorithm
     *  required at several places to recover the algorithm from the Agent
     */
		       
	
	public IStrategy getAlgorithm() {
	  return this.algorithm; 
	}
	
	/** getEnvironment
     *  @return Environment the agent is playing with
     *  required at several places to recover the algorithm from the Agent
     */
	
    public IEnvironment getEnvironment() {return this.universe;}

    /** Learning control methods
      */
    public void enableLearning() {learningEnabled=true;}
    public void freezeLearning() {learningEnabled=false;}

    
    
	
	/** addNeighbour
	 *  A Neighbour is added indexed by her Id
	 */
    public void addNeighbour(MACEAgent a) {
	  this.neighbours.put(a.getId(),a);
	        
    }    
    /** getNeighbour
     *  Retrieve a Neighbour by her Id
     */
    public MACEAgent getNeighbour(String id) {
	    // REMARK  no need to cast
	  return this.neighbours.get(id);
    }    

    /** getActionList
     *  @return List of actions
     *  This override is because of the need to use a filtered state to produce the actions
     */
     
    public ActionList getActionList() {
		
	    if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
		
	    MACEState ns=(MACEState) this.filter.filterState(this.currentState,this.universe);
	    
	    //TODO:Fix this inconsistency, I'm using the IState environment instead of my own
	    //Not doing this flows the call to the MACEEnvironment which is wrong
	    	    
	    ns.setAgent(this);
        ActionList al=ns.getActionList();
		if (Trace.verbosity==true) Trace.getTraceObject().printLog(3,"Agent("+this.getId()+") ActionList \n"+al);
		
		return al;
		
		
	}
        
	
	/* getId
	 * @return Returns the Agent Id string
	 */	
	public String getId() {
	  return this.id;	
    }
		
	/** printQSA
	 *  Prints the Q(s,a) for this agent
	 *
	 */
	 public String printQSA() {
		 
		 MACESelector al=(MACESelector)this.algorithm;
		 return al.toString();
     }	
     
	/**  choose
	 *   Ask the algorithm to choose the next action.
	 *   @return Action choosen
	 */
	
	public IAction choose() {
	  ActionList l=getActionList();
	  
	  MACESelector al=(MACESelector) this.algorithm;
	  IAction prov=al.getChoice(this,l); 
	  
	  this.lastAction=(IAction)prov.copy();
	  return prov;
	}
        
    /** applyAction
     *  @param a ComposedAction to apply
     *  Apply the action and learn from it
     */
    public void applyAction(MACEComposedAction a) { 
	  
	  if(this.learningEnabled) {
		 double rew=this.getLastReward();
	     this.learn(this.oldState,this.currentState,a,rew);
	           }   
	  
	}   

	// This method shouldn't be called when operating with multi-agent
    public IAction act() {
      throw new MACEException ("ERROR! This method can not be instantiated in this class");
	  //return this.applyAction(this.choose());
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
	       
	       //Get the Q(s,a) prior to the computation, also store the State and Action used to recover it.
	       this.lastQSA=al.getRewardStore().get(no,a);
	       this.lastfilteredPrevState=no;
	       this.lastfilteredPostState=nc;
	       this.lastappliedAction=a;
	       
	       //Go to learn
	       al.learn(no,nc,a,r);
	       
	       //Get the Q(s,a) after the learning has been made
	       this.currQSA=al.getRewardStore().get(no,a);
	       
	       
           if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Agent ("+this.getId()+") old Q(s,a)="+this.lastQSA+" new Q(s,a)="+this.currQSA);	       	       
	       if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Agent ("+this.getId()+") Q(s,a):"+algorithm.toString());	       
	       
	       
        }   
        
	    return;	    
   } 
   
   
   /** newEpisode
	 *  Prepares the agent for a new episode
	 */
   	public void newEpisode() {

	//Reset the reward object for this agent   		
	MACEReward.getRewardObject().set(this,0.0);
	
	this.lastAction=null;
	this.oldState=null;
	
	this.broadcast=false;
	
	
	//Reset the new episode at the algorithm level
	if(this.algorithm!=null) {
	    this.algorithm.newEpisode(); 
    }    
    
    }

	/** Set of modifications to allow for notification algorithms to operate
	 */
	
	/* getlastQSA
	 * Returns the Q(s,a) prior to the last learning cycle
	 * @return Last QSA prior to the learning cycle, it might be used to restore this value
	 */
     public double  getlastQSA()            {return lastQSA; }
     
     /* getcurrQSA
	 * Returns the Q(s,a) after the last learning cycle
	 * @return QSA after the learning cycle, it might be used to operate over this value
	 */
     public double  getcurrQSA()            {return currQSA; }
     
     /* getfilteredPostState
	 * Returns the state after the action has been applied
	 */
     public IState  getfilteredPostState()  {return this.lastfilteredPostState; }
     
     /* getappliedAction
	 * @return Action applied
	 */
     public IAction getappliedAction()      {return this.lastappliedAction; }
     
     /* getfilteredPrevState
	 * Returns the State before the action has been applied
	 * @return State before the action has been applied
	 */
     public IState  getfilteredPrevState () {return this.lastfilteredPrevState; }
     
     /* evaluate
      * Method to evaluate if a learning cycle has been successful or not
      * @return true if the cycle has been successful and false if not
      */ 
     public double evaluate() {
	     return 0.0;
     }
        
     /* commit
      * Method to operate over the agent beliefs (if needed) if the learning cycle has been successful
      * sort of an end tail of the episode. Normally should be left without any action but it might
      * be overriden by some specific action
      */  
     public void commit() {
	     if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
         return;
	 }
	 /*  
     public void finalize() {
	     if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
         return;
	 }
	 */
	 /* finalize
      * Method to ensure the operate over the agent beliefs (if needed) if the learning cycle has been successful
      * sort of an end tail of the episode. Normally should be left without any action but it might
      * be overriden by some specific action
      */  
     public void finalize(double reward) {
	     if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
	     if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Agent("+this.getId()+") Finalizing");
         
	     MACESelector al=(MACESelector) this.algorithm;
		 if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Agent("+this.getId()+") Finalizing Last State with Q(s,a)="+this.currQSA);
		 
		 
		 
		 //Find out the reward obtained in the transition between the old and new state
	     double r=this.getLastReward();
		 
		 al.setValue(this.lastfilteredPostState,this.lastfilteredPostState,this.lastappliedAction,r+reward);
	     return;
	 }
	 /* rollback
	  * if the learning cycle is deemed as unsuccessful this stamp back the original pre-learning values
	  */
	 public void rollback() {
		 if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
		 MACESelector al=(MACESelector) this.algorithm;
		 al.setValue(this.lastfilteredPrevState,this.lastfilteredPostState,this.lastappliedAction,lastQSA);
    	 return;
     }
    
   
   	/** getLastAction
	 *  @return the lastAction
	 */
	public IAction getLastAction() {
	
		return lastAction;
	}
	
	/** getCurrentState
	 *  @return the lastAction
	 */
	public IState getCurrentState() {
	
	  return this.currentState; 
	}
	
	/** setoldState
      * This is a modification of applyAction(IState,double) in the PiQLe architecture 
      * the oldState is overriden from the arbitre.
      */
    public void setoldState(IState s) {
		this.oldState=s;
    }
     /** setcurrentState
     *  This is a modification of applyAction(IState,double) in the PiQLe architecture 
     *  the currentState is overriden from the arbitre.
     */
	public void setcurrentState(IState s) {
		this.currentState=s;
    }
    
    /** getOldState
     *  get the Old state for the agent
     *  @return Old State
     */
    
	public IState getOldState(){return this.oldState; }
	
    /** setInitialState
     *  @param s Intial state
     */
    public void setInitialState(IState s) {
	
	  this.oldState=s;
	  this.currentState=s; 
	}
	
	/**setLastAction
	 * @param lastAction the lastAction to set
	 */
	public final void setLastAction(IAction lastAction) {
	
		this.lastAction = lastAction;
	}
	
	/**getLastReward
	 * @return Last reward for this agent
	 */
	public double getLastReward() {
		double r=MACEReward.getRewardObject().get(this);
		r+=this.globalReward;
	    return r;
    }
    /**setReward
	 * @param r Global reward to set
	 */
	public void setReward(double r) {
		this.globalReward=r;
		
    }
    
    public void setThreshold(double t) {
	    this.threshold=t;
    }
    public double getThreshold() {
	    return this.threshold;
    }
 
   public static IAgent readAgent(IEnvironment s){
	   throw new MACEException ("ERROR! This method can not be instantiated in this class");
	   }
   public static IAgent readAgent(String fichier, IEnvironment s){
	   throw new MACEException ("ERROR! This method can not be instantiated in this class");
	   }
   public static IAgent readAgent(String fichier) {
	   throw new MACEException ("ERROR! This method can not be instantiated in this class");
	   }
   
   public void saveAgent() {
	   throw new MACEException ("ERROR! This method can not be instantiated in this class");
   }
   
   
   /** From the states and actions encountered during the exploration, extracts a dataset in a
	 format suitable for the Neural Network treatment. */
	public Dataset extractDataset(){
	   throw new MACEException ("ERROR! This method can not be instantiated in this class");
   }
	

   public void saveAgent(String s) {
	   throw new MACEException ("ERROR! This method can not be instantiated in this class");
   }
	public void explainValues() {
		throw new MACEException ("ERROR! This method can not be instantiated in this class");
	}
	
	public String toString() {
		return this.id;
    }		
}
