
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
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA.
 */

/*    MACEArbitre.java
 *    Apr 07
 * 	  
 *    Copyright (C) 2007 Francesco De Comit�
 *    Copyright (C) 2007 Pedro Colla
 *
 *    This is a joint effort to implement an alternative Multi-Agent Cooperative framework (MAC)
 */
package MACE;

//import java.util.ArrayList;
import java.util.Iterator;

import environment.*;

import algorithms.*;
import java.util.*;




/**
 * A referee to schedule the multi-agent case : 
 * <ul>
 * <li> Maintains a list of individual agents</li>
 * <li> Ask each agent to choose its own action.</li>
 * <li> Informs the environment about the whole actions set.</li>
 * <li> After receiving feedback from the environment, informs the agents
 * of the new state and the reward.</li>
 * </ul>
 * A new approach of the multi-agent case due to Pedro Colla.
 * @author Francesco De Comit�
 * @author Pedro Colla
 *
 * Apr 2007
 
 */
public class MACEArbitre implements IArbitre{
	
	
	 /** Reward from the current episode */
    protected double totalReward; 
    
    protected int tokenNum=0;

    /** Maximal length of an episode */
    protected int maxIter=1000; 
    
    /** Current and previous states*/
    
    protected IState currentState;
    protected IState oldState;
    public    IState lastState;
    
    /** Read the maximal length of an episode */
    public int getMaxIter(){return this.maxIter;}

    // Copy to the Environment
    protected IEnvironment universe;
    
    //The multi-agent farm (The Swarm)
    protected HashMap<String,MACEAgent> Swarm;
    protected boolean verbosity=false;    /** Verbosity */
    protected boolean notification=false; /** notification */
    //Constructor
    //@param MACIEnvironment u
    
    
    public MACEArbitre (IEnvironment u){
	    this.universe=u; 
    	Swarm=new HashMap<String,MACEAgent>();
    }
    
    /** Change the maximal length of an episode. */
    public void setMaxIter(int t){
	  if(t>0) this.maxIter=t;}

	/** Get the current state the agent is in
	 * @return Current State
	 */
	public IState getcurrentState() {
		return this.currentState;
    }
    
    /** Get the previous state the agent was in
     *  @return Previous state
     */
     
    public IState getoldState(){
	    return this.oldState;
    } 
    /** Verbose*/
    public void setVerbosity(){
	  this.verbosity=true; 
	}
	
	/** Turn On Notification*/
	public void setNotification() {
	  this.notification=true; 
	}	
	
	/** Turn Off Notification*/
	public void resetNotification() {
	  this.notification=false; 
	}	
	
    /** Mute*/
    public void unsetVerbosity(){
	  this.verbosity=false;
	}
    // Reward of last episode
    public double getRewardForEpisode(){
    	return this.totalReward;
    }
    
    /*============================================================================================================
      Methods to deal with the swarm of agents
     *============================================================================================================*/
     
    /* add
     * Accept a new agent in the brotherhood of the agents (called the Swarm)  
     * @param  a A simple agent of class MACEAgent
     * 
     */
    public void addAgent(MACEAgent a){
	  String t=a.getId();
      this.Swarm.put(t,a);
      
      //Initialize the reward for this agent
      MACEReward.getRewardObject().set(a,0.0);
      
      //Get the algorithm and add the agent to the map of algorithms
      //TODO: Review if the following comments are reasonable
      //MACESelector al=(MACESelector)a.getAlgorithm();
      //al.add(a);
      return;
    }
    
     
    /* get
     * Get an agent given his Id
     * @param  id String identifying the agent
     * @return A handle to the agent
     */
    
    public MACEAgent getAgent(String id) {
	  return (MACEAgent)this.Swarm.get(id);    
    }    
    
    /* remove
     * Remove an agent given her Id
     * @param id of the agent
     * @return Agent if success, Null otherwise
     */
     
    public MACEAgent removeAgent(String id) {
	  return (MACEAgent) this.Swarm.remove(id); 
    }    

    /* setEpsilon
     * Set epsilon for all agents in the Swarm
     * @param e New Epsilon
     */
       
    public void setEpsilon(double e) {
      Collection cl=Swarm.values();
      Iterator   li=cl.iterator();
     
	  
      while (li.hasNext()) {
	      MACEAgent a=(MACEAgent)li.next();
	      AbstractMemorySelector ql=(AbstractMemorySelector)a.getAlgorithm();
	      ql.setEpsilon(e);
      }	  
	         
	    
    }    
       
    /* getAllAgent
     * Get a linked list with all agents in the swarm
     * @return LinkedList of all registered agents
     */ 
    
    public LinkedList getAllAgent() {
	  Collection cl=this.Swarm.values(); 
	  Iterator   li=cl.iterator();
      LinkedList l=new LinkedList();
      while (li.hasNext()) {
	    l.add(li.next());	  
      }	  
	         
	  return l;
    }    
      
    /* rendezvousAgent
     * Introduce all agents among thenselves
     */
    public void rendezvousAgent() {
	    
	// Setting the neighbours
	    LinkedList ll1=this.getAllAgent();
		Iterator   it1=ll1.iterator();
		while (it1.hasNext()) {
		  MACEAgent  a1=(MACEAgent)it1.next();
		  
		  LinkedList ll2=this.getAllAgent();
		  Iterator   it2=ll2.iterator();
		  
		  while (it2.hasNext()) {
			  MACEAgent a2=(MACEAgent)it2.next();
			  if (a1.getId()!=a2.getId()) {
			     a1.addNeighbour(a2);	  
			  }	  
		    	  
	      }
	      	  
		}
	        
    }
    
    /*============================================================================================================*/
    
    
    //********************************************************************************************************    
    /** Beginning from an initial state, play the game until : 
	<ul>
	<li> A final state</li>
	<li> The maximal length for an episode</li>
	</ul>
     	@return Length of episode
    */
    //********************************************************************************************************
    public int episode(IState s){
	   
       //Reset the episode	   
       episodeReset(s);	    

       //Start the episode, total reward is zero at this point, current State and old Staet are initial
	   this.totalReward=0.0; 
	   this.currentState=s; 
	   this.oldState=s; 
	   double reward=0.0;
	   
	   //Iterate the maximum allowed number of times
	   for(int i=0; i<this.maxIter;i++){
		  //Get a fresh composed action 
	      MACEComposedAction ca=new MACEComposedAction();
          ca=episodeChoose(ca);
          System.out.println("------------->"+ca); 
          System.exit(0); 
             
          //Make oldState be the current state
	      oldState=currentState.copy();
	      //Go to the environment to find which the new state is; see the environment MUST handle the composite action
	      //There is an assumption here and it's the environment is linear, therefore the order of applications of
	      //the actions is irrelevant. If this premise is true then actions will be applied in order and the new
	      //state obtained after that
	      currentState=this.universe.successorState(oldState,ca);
		  
	      //Find out the reward obtained in the transition between the old and new state
	      reward=this.universe.getReward(oldState,currentState,ca);
	      
	      //double intreward=MACEReward.getRewardObject().get();
	     
	      //Acum the reward for the episode
	      totalReward+=reward;
	      
	      // Action performed, state changed, reward given, time to learn !
	      // Walk again thru the agent swarm and distribute the information one by one
	      Collection cl=this.getAllAgent();
	      Iterator it=cl.iterator();
	     
	      while(it.hasNext()){
		     MACEAgent currentAgent=(MACEAgent) it.next();
		     
		     //Apply the current State, there is a mismatch in the arguments between what is needed
		     //here and what the architecture of PiQLe allows, so a shortcirtuit is made
		     //the currentState and Rewards are set thru ad-hoc methods at the problem implementation of Agent
		     //
		     double intreward=MACEReward.getRewardObject().get(currentAgent);
		     totalReward+=intreward;
		     currentAgent.setoldState(oldState);
		     currentAgent.setcurrentState(currentState);
		     currentAgent.setReward(reward);
		     
		     //Now apply the action itself
		     currentAgent.applyAction(ca);
		     
	      }
	      
	      //Evaluate the episode (only if the notification is set)
	      //if (this.notification==true) {
		  //    episodeEvaluation(reward);	 
	      //}
	      episodeEvaluation(reward);
	      //If the currentState is Final then complete the episode
	      if (this.universe.isFinal(this.currentState)) {
		      this.lastState=this.currentState;
		      return i+1;
	      }     
	      //Otherwise goback and repeat
	}this.lastState=this.currentState;
	return maxIter; //This means the episode has elapsed thru the maximum iterations without solving the problem
  }
//********************************************************************************************************

  /* episodeReset
   * @param s Initial state
   */
  protected void episodeReset(IState s) {
    //Get all agents in the swarm and initialize all to start the episode
    Collection cl=this.getAllAgent();;
    Iterator   it=cl.iterator();
    while (it.hasNext()) {
    	MACEAgent currentAgent=(MACEAgent) it.next();
    	currentAgent.setInitialState(s);	   
	   currentAgent.newEpisode(); 
    }

  }

  //********************************************************************************************************
  /* episodeEvaluation
   * This method allows the agent to evaluate actions if this is a two pass learning algorithm
   */

  protected void episodeEvaluation(double reward) {

  
	 MACEAgent currentAgent; 
	 
  	 // If notification is not completed see if this is the last state of the game and complete Q(s',a).
  	 if (!this.notification) {
	  	if (this.universe.isFinal(this.currentState)) {
		  	Collection cl=this.getAllAgent();;
	        Iterator it=cl.iterator();
	        while(it.hasNext()){
		       currentAgent=(MACEAgent) it.next();  	
		       currentAgent.finalize(reward);
		       
	        }   
	    }
	  	 
	    return; 	 
     }	 
     
     // Notification has been enabled
   
  	 //Be prepared to walkthru all agents
	 Collection cl=this.getAllAgent();;
	 Iterator it=cl.iterator();
	 
	 //Sum all contributions to see if are above the threshold
	 double  sum=0.0;
	 
	 while(it.hasNext()){
		//Get one agent of the swarm  
		currentAgent=(MACEAgent) it.next();
		double delta=currentAgent.evaluate();	
		sum += delta;
     }	
     
     //If sum is above then commit the changes to the Q(s,a)
     cl=this.getAllAgent();;
	 it=cl.iterator();
	 while(it.hasNext()){
	   //Get one agent of the swarm  
	   currentAgent=(MACEAgent) it.next();
	   
	   if (sum>0.0) {
	       currentAgent.commit();
	       if (this.universe.isFinal(this.currentState)) {
		       
		       currentAgent.finalize(reward);
	       }
       } else {
	       currentAgent.rollback();
       }
			 
     } 
      
  }
  
  //********************************************************************************************************
  /* episodeChoose
   * @param ca A ComposedAction object, assumed empty but containing all agents in the swarm
   * @return A ComposedAction with the action choosen by each agent
   */
   
  protected MACEComposedAction episodeChoose(MACEComposedAction ca) {
    
	 //Be prepared to walkthru all agents
	 Collection cl=this.getAllAgent();;
	 Iterator it=cl.iterator();
	 while(it.hasNext()){

		//Get one agent of the swarm  
		MACEAgent currentAgent=(MACEAgent) it.next();
		String id=currentAgent.getId();

		//Ask the agent to make her choice		    
		IAction a=(IAction)currentAgent.choose();
		//Add the choice to the Composed Action, see the Agent itself is the key to the ComposedAction entry
		ca.setAction(currentAgent,a);
     }
     return ca;	
  }
		    
		    
}
