
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

/*    MACEComposedAction.java
 *    Apr 07
 * 	  
 *    Copyright (C) 2007 Francesco De Comité
 *    Copyright (C) 2007 Pedro Colla
 *
 *    This is a joint effort to implement an alternative Multi-Agent Cooperative framework (MAC)
 */
package MACE;

/**
 * MACEComposedAction.java
 * 
 * Copyright (C) 2007 Francesco De Comité (decomite_at_lifl.fr)
 *           (C) 2007 Pedro Colla
 *
 */


import environment.*;
import agents.*;
import algorithms.*;
import referees.*;
import Log.*;
import java.util.*;


public class MACEComposedAction extends HashMap implements IAction  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Constructor
	 */
	 
	public MACEComposedAction(){
		super(); 
		if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
	}

	/** getAction
	 *  @param id the Agent
	 *  @return her action
	 */
	public IAction getAction(IAgent id) {
		if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
		return (IAction) this.get((Object)id);
    }
    	
    /** setAction
     *  @param id The agent generating the action (to be stored and used as a key
     *  @param a  The atomic action
     */
    public void setAction(MACEAgent id,IAction a) {
	    if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
	    this.put((Object)id,(Object)a);
    }
    
    /** getList
     *  @return A Linked List with all Actions (the object could be accessed directly with .get(key,object) if needed
     */
    public LinkedList getList() {
	    
	    if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
	    if (Trace.verbosity==true) Trace.getTraceObject().printLog(3,"getList","Current Size of HashMap("+this.size()+")");
	    
	    LinkedList ll=new LinkedList();
	    
	    Set kca=this.keySet();
	    Iterator ikca=kca.iterator();
	    while (ikca.hasNext()) {
		    ll.add(get(ikca.next()));
		 
	    }    
	    
	    return ll;
    }
    /** getList
     *  @return A Linked List with all Actions (the object could be accessed directly with .get(key,object) if needed
     */
    public LinkedList getAgentList() {
	    
	    if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
	    if (Trace.verbosity==true) Trace.getTraceObject().printLog(3,"getList","Current Size of HashMap("+this.size()+")");
	    
	    LinkedList ll=new LinkedList();
	    
	    Set kca=this.keySet();
	    Iterator ikca=kca.iterator();
	    while (ikca.hasNext()) {
		    ll.add(ikca.next());
		 
	    }    
	    
	    return ll;
    }
    
    public IAgent getAgent(String aid) {
	    
	    if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
	    if (Trace.verbosity==true) Trace.getTraceObject().printLog(3,"Getting Pointer for Agent("+aid+")");
	    	    
	    Set kca=this.keySet();
	    Iterator ikca=kca.iterator();
	    while (ikca.hasNext()) {
		    MACEAgent ag=(MACEAgent)ikca.next();
		    if (Trace.verbosity==true) Trace.getTraceObject().printLog(3,"  Retrieving Agent("+ag+") Id("+ag.getId()+")");
		    if (ag.getId().trim().equalsIgnoreCase(aid.trim())) {
			    if (Trace.verbosity==true) Trace.getTraceObject().printLog(3,"  Hit!");
			    return ag;
		    }
		    	 
	    }    
	    if (Trace.verbosity==true) Trace.getTraceObject().printLog(3,"  No Match!!");
	    return null;
    }
    
    
    /** Clone an Action. */

	public IAction copy() {
        if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
		MACEComposedAction mca=new MACEComposedAction();
		Set ks=this.keySet();
		Iterator it=ks.iterator();
		while (it.hasNext()) {
		  Object key=it.next();
		  mca.put(key,this.get(key));
	    }	
		return mca;
    }	
	
    /** toString
      * @return A stringsfied version of the Composed action, actually a list of all Actions
      */
      
    public String toString() {
	    if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
	    String st="";
	    
		
		Set ks=this.keySet();
		Iterator it=ks.iterator();
		while (it.hasNext()) {
		  IAgent key=(IAgent)it.next();
		  IAction mia=(IAction)this.get((Object)key);
		  st=st+key+" "+mia+" -- ";
	    }	
		return st;
	    
    }    
	
	/** Q-Learning requires the creation of a hashCode unique for the object
	 *  @return An integer resulting from the hashCode of the hashCode of all actions stored
	 */
	  
	public int hashCode() {
	    int result=17;
        LinkedList ll=getList();
        Iterator it=ll.iterator();
        while (it.hasNext()) {
	       MACEAction a=(MACEAction)it.next(); 
	       result=37*result+a.hashCode(); 
        }       
	    
	    return result; 
   }

    /** Q-Learning requires the definition of equality
      * @return 
        <ul>
        <li>true, if all actions are the same in the same order</li>
        <li>false, otherwise.</li>
        </ul>	
    */
    
	public boolean equals(Object o) {
      
	  //If it is not even of the same clase cann't be equal	
	  if (!(o instanceof MACEComposedAction)) return false;
	  	
	  MACEComposedAction m=(MACEComposedAction)o;
	  
	  //If not of the same size can't possibly be equal
	  if (m.size()!=this.size()) {	return false; }
	  
	  //Take the guest object and got all keys on it
	  Set sm=m.keySet();
	  Iterator im=sm.iterator();
	  
	  while (im.hasNext()) {
		  
	    Object id=im.next();
	    //Get the same object, which is an action, for the same ids.
	    MACEAction a1=(MACEAction)this.get(id);
	    MACEAction a2=(MACEAction)m.get(id);
	    
	    //If actions aren't the same then the objects aren't equal.
	    if (!a1.equals(a2)) { return false; }
	  }	  
	  return true;
      
    }	
	
    /** Size of an Action's coding (for NN). NOT IMPLEMENTED WILL ABORT THE EXECUTION IF CALLED*/
	public int nnCodingSize() {
		throw new MACEException ("ERROR! This method can not be instantiated in this class");
		
    }		

	/** Action's coding (for NN). NOT IMPLEMENTED WILL ABORT THE EXECUTION IF CALLED*/
	public double[] nnCoding() {
		throw new MACEException ("ERROR! This method can not be instantiated in this class");
	  
	
    }
     
}
