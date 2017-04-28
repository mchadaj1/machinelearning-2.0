
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
 *    MACELearningSelector.java
 *    Copyright (C) 2004 Francesco De Comit�
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

public class MACELearningSelector extends MACESelector {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Memorizing or computing Q(s,a) */
	protected MACERewardMemorizer memory;
	
	/*
	Memory structure 
	
	hashMap() ---> [MACNQIAgent] [MACNQRewardMemorizer] <-- Conjeture of ith (this) agent about 1st agent
	                    ...               ...
	               [MACNQIAgent] [MACNQRewardMemorizer] <-- Conjeture of ith (this) agent about itself
	                    ...               ...
	               [MACNQIAgent] [MACNQRewardMemorizer] <-- Conjeture of ith (this) agent about nth agent
	
	*/
	protected HashMap memMap=new HashMap();

	public MACELearningSelector(){
	   
	   super();
	   //memory=new MACERewardMemorizer(); 
	   if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	   
    }
	
    public MACELearningSelector(IDefaultValueChooser dvc){
	   
	   super(dvc);
	   //this.myl=new QLearningSelector(dvc);
	   if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	   
	   
    }
     /**============================================================================================================ 
       * NashQ function as interpreted from 
       * "Nash-Q Learning for General Sum Stochastic Games" Hu, Journal of Machine Learning Research 1039-1069, 2003
       * @param  myself The state of the agent processing the NashQ
       * @param  s      State to compute equillibrium
       * @param  ca     Composed action vector (used to extract the list of agents to compute equillibria
       * @return NashQ function as destribed in Hu's paper
       *============================================================================================================
       */
     public double NashQ(IAgent myself,IState s,MACEComposedAction ca) {
	     if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	  
	     if (Trace.verbosity==true) Trace.getTraceObject().printLog(2,"Agent("+myself+") State("+s+") CA("+ca+")");	
	     
	     //MACEComposedAction nca=new MACEComposedAction();
	     LinkedList aglist=ca.getAgentList();
	     Iterator itaglist=aglist.iterator();
	     
	     while (itaglist.hasNext()) {
		    
		    //For each agent
		    MACEAgent ag=(MACEAgent)itaglist.next();
            if (Trace.verbosity==true) Trace.getTraceObject().printLog(2,"  Agent("+ag+") Retrieved");			    
		    
            //Get the memorizer assigned for this agent
		    MACERewardMemorizer mem=(MACERewardMemorizer)this.memMap.get((Object)ag);		
		    
		    //Retrieve the NashState for this agent
		    IAction act=(IAction)mem.getNashState(s,ag);
		    if (Trace.verbosity==true) Trace.getTraceObject().printLog(2,"  Agent("+ag+") Best Action("+act+")");	
		    
		    //Might be null at the begining (a NullAction might be of some help here...
		    if (act==null) {
			    if (Trace.verbosity==true) Trace.getTraceObject().printLog(2,"  Agent("+ag+") has no best Action, returning 0.0)");	
			    return 0.0;
		    }
		    
		    //Store the action in this continuum
		    //nca.setAction(ag,act);
		    ca.setAction(ag,act);
		    if (Trace.verbosity==true) Trace.getTraceObject().printLog(2,"  Agent("+myself+") Operating Agent("+ag+") Best Action("+act+")");	
		           
	     }
	     //Here we have a state and a CA with the best action for every agent
	     
        if (Trace.verbosity==true) Trace.getTraceObject().printLog(2,"  Composed Action for Nash Equillibria is("+ca+")");		          
	    
        //Get the memorizer for this agent
		MACERewardMemorizer mem=(MACERewardMemorizer)memMap.get((Object)myself);		
		
		//Get the QNash value for this Composed action and state
		//double qsa = mem.get(s,nca);
		double qsa = mem.get(s,ca);
	    if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Agent("+myself+") State("+s+") CA("+ca+") QNash(s)="+qsa);	
	    
	    return qsa;
     
     }
     
    
	
/**
	 * Learning from experience.
	 * 
	 * @param s1  the start state.
	 * @param s2  the arrival state.
	 * @param ca  the chosen action.
	 * @param r   Global Reward (perceived by all agents)
	 */
	//
	//                                                              +--- The reward object is not used but kept
	//                                                              V
	//                                                           =========
	public void learn(IState s1, IState s2, MACEComposedAction ca,double r) {
		if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	  
		if (Trace.verbosity==true) Trace.getTraceObject().printLog(2,"LEARN.X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-x");		   		  
		
		//Update the learning speed factor
		if (geometricDecay)
			alpha *= decayAlpha;
		else {
			alpha = 1 / Math.pow(count + 1.0, this.alphaDecayPower);
		}

		count++;

		if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"LEARN. Entry oldState("+s1+") newState("+s2+") GlobalReward("+r+") ComposedAction("+ca+")");
				
		//Is this clean enough? It's a pure singleton object but it's a by-pass of the architecture as well
		
		LinkedList ll=ca.getAgentList();
		Iterator   it=ll.iterator();
		
		while (it.hasNext()) {

          if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"LEARN.=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=");		   		  		  
		  
          //Get the agent being processed	
		  MACEAgent ag=(MACEAgent)it.next();
          if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"  Iterating Agent("+ag+")");
		  		  		  
		  //Get the memorizer for it
		  MACERewardMemorizer mem=(MACERewardMemorizer)memMap.get((Object)ag);
		  
		  //Get the reward given to it
		  double rew=r+MACEReward.getRewardObject().get(ag);
		  if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"  Iterating Reward("+rew+")");
		  
		  //Get the action for this agent
		  MACEAction act=(MACEAction)ca.getAction(ag);
		  if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"  Iterating Action("+act+")");

		  if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"LEARN.Agent("+ag+") Update Q oldState("+s1+") newState("+s2+") GlobalReward("+r+") SelfReward("+MACEReward.getRewardObject().get(ag)+") ComposedAction("+ca+")");
          		  
          //Get the maximum Q for this agent, state and must be an action
		  double qsa = mem.get(s1,ca);
		  
		  if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"LEARN. Previous Agent("+ag+") GlobalReward("+r+") InternalReward("+MACEReward.getRewardObject().get(ag)+") old Q(s,CA)="+qsa);
		  //qsa = (1-alpha)*qsa + (alpha*rew) + (gamma*alpha*NashQ(ag,s2,ca));
		  
		  qsa = (1-alpha)*qsa + (alpha*rew);
		  
		  if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"LEARN. Storing Agent("+ag+") oldState("+s1+") newState("+s2+") QSA("+qsa+") ComposedAction("+ca+")");		  
		  mem.put(s1, ca, s2, qsa);	

		  
		  if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"LEARN. Agent("+ag+") New QSA "+mem.toString());		   
		  
		  if (Trace.verbosity==true) Trace.getTraceObject().printLog(2,"LEARN.=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=");		   		  
        }
        if (Trace.verbosity==true) Trace.getTraceObject().printLog(2,"LEARN.X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-x");		   		          
		
	}
	
	/*  add
	 *  Adds an agent to the memory structure
	 *  @param ag Agent
	 */
	public void add(MACEAgent ag) {
		if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    
			  
		if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Adding a IRewardStore object for Agent("+ag+")");    
		MACERewardMemorizer mem=new MACERewardMemorizer();
		memMap.put((Object)ag,(Object)mem);
		return;
    }
	
	/** getChoice
	    @param  ag Agent asking for the choice 
	    @param  l  List of possible actions to choose from
	    @return Choosen action
	    Only Epsilon-greedy choice for MAC Nash-Q
	    TODO: expand it to other choosing methods
	  */  
	//                             +------------- This defines the agent for which the choice is made  
	//                             V
	//                       =========  
	public IAction getChoice(IAgent ag,ActionList l){
		if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	  
		
		//If there are no action lists return no action
		if(l.size()==0) return null;
		
		//Select the Reward Store where the actions will be taken from
		MACERewardMemorizer mem=(MACERewardMemorizer)memMap.get((Object)ag);
	
		//Start with the first action from the list
		//Beware the ActionList arrangement has individual actions (!!) and not Composed Actions
		//The arbitre will ensemble all answers into the CA for this episode	
		IState s=l.getState(); 	
		IAction best=l.get(0); 
	    boolean ffirst=true;
	    
		//See if a random choosing will be made, if so make it first
		if(generator.nextDouble()<=this.epsilon) {
		   
		   best=l.get(generator.nextInt(l.size()));
		   if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Random Choice   Agent("+ag+") ActionList("+l+") BestAction("+best+")");
		   return best; 
	    }
		
        if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Non Random  Choice Q(s,a) for Agent("+ag+") \n"+mem.toString());    	  
	    	    	
		//Returns the Q(s,a) value for this state for the action of this agent
		double maxqsap=mem.getBestAction(ag,s,best);
        if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Initial maxqsap is("+maxqsap+")");    	  
				
		// TODO : might use an iterator
		for(int i=1;i<l.size();i++){
			
		    IAction a=l.get(i); 
		    double qsap=mem.getBestAction(ag,s,a);
		    if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"  {"+i+"} Non Random Agent("+ag+") State("+s+") Action("+a+") Qmax(agent,s,action)="+qsap+" old Qmax="+maxqsap);    	  
		    
		    if(qsap>maxqsap) {
			   ffirst=false;
			   maxqsap=qsap;  
			   best=a;
		    }
		}
        if (Trace.verbosity==true) Trace.getTraceObject().printLog(1,"Non Random Agent("+ag+") State("+s+") Action("+best+") Qmax(agent,s,action)="+maxqsap);    	  		    
		return best;
			    
	}
	
	
	/** extractDataset
	  * Required by Interface but not implemented, will throw an exception if used
	  */		
	public Dataset extractDataset() {
	    throw new MACEException ("ERROR! This method can not be instantiated in this class");
		
	}
	
	/** toString()
     *  @param ag Agent 
     *  @return Q Function for this agent
     */
         
     public String toString(IAgent ag) {
	    String   s="State/ComposedAction pairs Agent("+ag+") \n";  
	    MACERewardMemorizer m=(MACERewardMemorizer)memMap.get((Object)ag);
		s+="---------Agent["+ag+"]------------- State/ComposedAction pairs\n";
		s+="   "+m.toString();  
	    return s; 
	     
     }
     /** toString()
      *  @return Q Function for all Agents
     */
      
     public String toString() {
	     
	    Set kmem=memMap.keySet(); 
	    Iterator kit=kmem.iterator();
	    String s="";
	    while (kit.hasNext()) {
		  MACEAgent ag=(MACEAgent)kit.next();
		  
		  s+=this.toString(ag);
	    }      
	    return s;
	}
}    
