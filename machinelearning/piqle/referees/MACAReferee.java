
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
/**
 * MACAReferee.java
 * 
 * Copyright (C) 2007 Francesco De Comité (decomite_at_lifl.fr)
 *
 */
package referees;

import java.util.ArrayList;
import java.util.Iterator;

import environment.IState;
import environment.MACAComposedAction;
import environment.MACAEnvironment;

import agents.MACAAgent;

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
 * @author Francesco De Comité
 *
 * 3 avr. 07
 */
public class MACAReferee {
	
	protected ArrayList<MACAAgent> listOfAgents=new ArrayList<MACAAgent>();
	
	protected MACAEnvironment universe; 
	
	 /** Reward from the current episode */
    private double totalReward; 
    
    private int tokenNum=0;
    
    
   

    /** Maximal length of an episode */
    private int maxIter=1000; 

    /** Read the maximal length of an episode */
    public int getMaxIter(){return this.maxIter;}

    /** Change the maximal length of an episode. */
    public void setMaxIter(int t){
	if(t>0) maxIter=t;}

     /** Verbosity */
    private boolean verbosity=false; 

    /** Verbose*/
    public void setVerbosity(){verbosity=true; }
    /** Mute*/
    public void unsetVerbosity(){verbosity=false;}

    public MACAReferee(MACAEnvironment u){
    		this.universe=u; 
    }
    /** Read the reward earned during the last episode */
    public double getRewardForEpisode(){
	return totalReward;
    }
    
    public int addAgent(MACAAgent ag){
    	ag.setTokenId(tokenNum);
    	this.listOfAgents.add(tokenNum,ag); 
    	tokenNum++; 
    	return tokenNum-1; 
    }
    
    /** Beginning from an initial state, play the game until : 
	<ul>
	<li> A final state</li>
	<li> The maximal length for an episode</li>
	</ul>

     	@return Length of episode
    */
    
    // TODO : this is the copy of the OnePlayerReferee method
    // A lot has to be done here (03/04/07)
    public int episode(IState initial){
    	Iterator<MACAAgent> cursor=this.listOfAgents.iterator();
    	while(cursor.hasNext()){
    		MACAAgent currentAgent=cursor.next();
    		currentAgent.setInitialState(initial);
    		currentAgent.newEpisode();
    	}
	totalReward=0.0; 
	if (verbosity) System.out.println("************\n"+"Starting State "+initial);
	IState currentState=initial; 
	IState oldState; 
	double reward=0;
	for(int i=0; i<this.maxIter;i++){
	    if(verbosity) System.out.println("************\nCurrent State  : "+currentState);
	    MACAComposedAction ca=new MACAComposedAction(tokenNum);
	   cursor=this.listOfAgents.iterator();
	   while(cursor.hasNext()){
		   MACAAgent currentAgent=cursor.next();
		   ca.setAction(currentAgent.choose(), currentAgent.getTokenId()); 
	   }// while
	   // All Agents have chosen their individual actions
	   // Referee can now inform the MACAEnvironment
	   oldState=currentState.copy(); 
	   currentState=this.universe.successorState(oldState,ca);
	   reward=this.universe.getReward(oldState,currentState,ca);
	   totalReward+=reward; 
	   // Action performed, state changed, reward given, time to learn !
	   cursor=this.listOfAgents.iterator(); 
	   while(cursor.hasNext()){
		   MACAAgent currentAgent=cursor.next();
		   currentAgent.learn(oldState, currentState,ca,reward);
	   }// while
	   if (universe.isFinal(currentState)) {
		   return i+1; 
	   }
	}// for i
	return maxIter; 
    }
    
   
}
