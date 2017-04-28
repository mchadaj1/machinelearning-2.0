
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
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA.
 */

/*
 *    MACERewardMemorizer.java
 *    Copyright (C) 2004 Francesco De Comit�
 *
 */

import java.util.Iterator;
import java.util.Set;

import environment.*;
import qlearning.*;

import agents.*;



/** Memorizing  Q(s,a) in HashMap. The key is the pair (state, value). */

public class MACERewardMemorizer extends RewardMemorizer{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;	
    private IDefaultValueChooser valueChooser; 
    public MACERewardMemorizer(){
    	this.valueChooser=new NullValueChooser();
    }

    /** get
     * @param s State
     * @param a ComposedAction CA(a1,....,an)
     * @return Q(s,CA)
     */
     
    public double get(IState s,IAction a){
	if((a==null)||(s==null)) return 0; 
	//Transform the action-state into an unique token
	ActionStatePair us=new ActionStatePair(a,s); 
	
	//Get the Q value
	Double db=(Double)(map.get(us));
	if (db==null){
	    double u=this.valueChooser.getValue(); 
	    MACEState st=(MACEState)s;
	    MACEComposedAction ac=(MACEComposedAction)a;
	    map.put(new ActionStatePair(ac,st),new Double(u)); 
	    return u;
	}
	return db.doubleValue(); 
    }
    
     
    /** put
     *  @param s   State
     *  @param a   Composed Action to store
     *  @param sp  New State
     *  @param qsa Q(s,a) function
     */
    
    public void put(IState s,IAction a,IState sp,double qsa){
	MACEState st=(MACEState)s;
	MACEComposedAction ac=(MACEComposedAction)a;
	if(sp!=null){
		
		
	    ActionStatePair as=new ActionStatePair(ac,st);
        this.map.put(as,new Double(qsa));
	   	    
	    	
	    
	} else {
		
	    ActionStatePair as=new ActionStatePair(ac,st);
	    this.map.put(as, new Double(qsa));	    
	}
    }

    /** getNashState
     *  Get the Policy driven action for this agent to compute the Nash Equilibria
     *  @param s  State for which the equilibria is computed
     *  @param ag Agent
     *  @return Action according to policy
     *  This is the for the k-th Agent the PIk(s') function mentioned in [8] of Hu's paper.
     */
        
    public IAction getNashState(IState s,IAgent ag) {	    
	    //Get the entire space of Action-State entries 
    	//(this isn't anywhere near efficiente).
	     
	    Set kmem=this.map.keySet();
	    Iterator ikmem=kmem.iterator();
	    
	    //Best Action so far, a Null action would be useful here
	    IAction bestAction=null;
	    
	    
	    //Start walking the space
	    boolean ffirst=true;
	    Double  Qmax=0.0;
	    while (ikmem.hasNext()) {
		    
		    //Derive the State-ComposedAction Entry
		    ActionStatePair as=(ActionStatePair) ikmem.next();
		    
		    //Get the individual State
		    IState keyState=as.getState();
		    
		    //No point on continuing if this is not for the inteneded state
		    if (keyState.equals(s)){
			    
			    //Retrieve the action, which in this case is a ComposedAction
		        
			    MACEComposedAction mca=(MACEComposedAction)as.getAction();
			    
			    //Get the individual action for this agent
			    
		        IAction act=(IAction)mca.getAction(ag);
		        
		        //Get the Q(s,CA) value for this particular State-Action
		        
		        Double Q=(Double)this.map.get((Object)as);
		        //If the first or the biggest so far store it.
		        
		        if ((ffirst==true) || (Qmax>Q)) {
			        ffirst=false;
			        bestAction=act;
			        Qmax=Q;
		        }
		        
		   }
		 
	    }
	    
	    //Had any value being selected?
	    if (ffirst==true) {  return null;
	    }   
	    //Yes, return the action associated with the biggest Q(s,CA) for this state 
         return bestAction;    
	    
    }   
    /**   getBestAction
     *    @param  ag  Agent
     *    @param  st  State
     *    @param  act Target action
     *    @return The best value for this state where the agent's action is the one given
     */
     
    
    public double getBestAction(IAgent ag,IState st,IAction act) {   	  
	    //TODO: add filters for agent 
	    boolean ffirst=true;
	    double  Qmax=-1E6;
	    
	    //Get all Action pairs 
	    Set kmem=this.map.keySet();
	    Iterator ikmem=kmem.iterator();
	    while (ikmem.hasNext()) {
		    
		    ActionStatePair as=(ActionStatePair) ikmem.next();
		    
		    //Get the individual Actions-State
		    IState keyState=as.getState();
		    MACEComposedAction mca=(MACEComposedAction)as.getAction();
		    
		    //Get out of the ComposedAction CA={a1,..,ai,..,an} the action of agent i which is ai
		    IAction aa=(IAction)mca.getAction((IAgent)ag);
		    //If the state and the actions are the same		    
		    if( (keyState.equals(st)) && (aa.equals(act)) ) {
			     //Preserve the biggest value
			     double Q=(double)this.map.get((Object)as);
		         if (ffirst==true) {
			         ffirst=false;
			         Qmax=Q;
		         }
		         
		         if(Q>Qmax) {
			        Qmax=Q;
		         }
	        }
		 
	    }
         return Qmax;    
	    
    } 
    
       
    /** getQmax
     *  @param  s State
     *  @return Best Q(s,a) value for this state 
     */
    public double getQmax (IState s) {
	    boolean ffirst=true;
	    double  Qmax=0.0;
	    
	    //Scan call entries of the Q Map
	    Set kmem=this.map.keySet();
	    Iterator ikmem=kmem.iterator();
	    while (ikmem.hasNext()) {
		    
		    //Extract individual states
		    ActionStatePair as=(ActionStatePair) ikmem.next();
		    IState keyState=as.getState();

		    //If State is the same then compare   		    
		    if(keyState.equals(s)==true) {
			  
		      double Q=(double)this.map.get((Object)as);
              if (ffirst==true) {
			      ffirst=false;
			      Qmax=Q;
			      
		      }
		      
		      
		      if(Q>Qmax) {
			     
			     Qmax=Q;
		      }
	        }
		 
	    }
	            	  	    
	    return Qmax;    
    }    
   
    /** to String
        @return String with the Q function for ALL agentes
	*/
	
	/*public String toString(){
	HashMap<IState,IAction> prov=new HashMap<IState, IAction>();
	ActionStatePair courante=null; 
	Set keys=map.keySet(); 
	String s="\nMAC NashQ Reward Store\n"+keys.size()+" state/action pairs \nListing of ALL  Q(s,a)\n";  
	
	Iterator enu=keys.iterator(); 
	
	while(enu.hasNext()){
	    ActionStatePair as=(ActionStatePair)enu.next(); 
	    double Qsa=(double)this.map.get((Object)as);
	    s+=as+" "+Qsa+"\n";
	    
	    // Looking for the best (state,action) pair
	   
	    	    
	}
	s+="Best actions :\n";
		
	return s;
    } // toString
*/
    
    
}

