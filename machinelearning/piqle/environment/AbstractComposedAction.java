
package environment;
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

/*
*    AbstractComposedAction.java
*    Copyright (C) 2006 Francesco De Comité
*
*/



import java.util.ArrayList;

import agents.ElementaryAgent;

/** Composed actions are used in the multi-agent case : an abstract composed
 * action stores elementary actions, together with the elementary agent applying
 * this action : the goal is to be able to find what action a given agent
 * performed.
 * This will let others agents able to be kept inform of their neighbours's 
 * actions.
 * 
 * 
 * @author decomite
 *
 */
public abstract class AbstractComposedAction implements IAction{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
    abstract public int getSize(); 
    protected  ArrayList<ElementaryAgent>listOfAgents; 
	public abstract void addElementaryAction(ElementaryAgent agent, IAction action);

	public abstract IAction getAction(ElementaryAgent a);

	public abstract IAction getAction(int i);
	

	public AbstractComposedAction() {
		super();
		this.listOfAgents=new ArrayList<ElementaryAgent>(); 
	}
	
	public ElementaryAgent getAgent(int i){
		return this.listOfAgents.get(i);
	}


	/** Clone an Action. */
	public IAction copy() {
	//Should not be used : defined in all subclasses
		System.err.println("copy() in AbstractComposedAction should not be used");
		System.exit(0);
		return null;
	}

	/** Size of an Action's coding (for NN). */
	public int nnCodingSize() {
		System.err.println("nnCodingSize in AbstractComposedAction : why using this method ?" ); 
		System.exit(-1); 
		return 0;}

	/** Action's coding (for NN). */
	public double[] nnCoding() {
		System.err.println("coding in AbstractComposedAction : why using this method ?" ); 
		System.exit(-1); 
		return null;}

	/** No action */
	public boolean isNullAction() {return false;}

	/** Q-Learning memorizing techniques use hashcoding : it is necessary to redefine it for each problem/game */
	public int hashCode() {//return list.hashCode();}
		System.err.println("hashCode in AbstractComposedAction : why using this method ?" ); 
		System.exit(-1); 
	return 0;}

	/** Q-Learning memorizing techniques use equality: it is necessary to redefine it for each problem/game */
	public boolean equals(Object o) {//return list.equals(o); }
		System.err.println("equals in AbstractComposedAction : why using this method ?" ); 
		System.exit(-1); 
	return false;}
	
	public String toString(){
		String s=""; 
		for(int i=0;i<this.getSize();i++) s+="\n Action Agent "+i+":"+getAction(i); 
			return s; 
	}

}