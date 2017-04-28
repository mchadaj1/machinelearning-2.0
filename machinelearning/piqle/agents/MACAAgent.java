
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
package agents;

import java.util.ArrayList;
import java.util.Iterator;

import algorithms.ISelector;
import environment.Filter;
import environment.IAction;
import environment.IEnvironment;
import environment.IdentityFilter;
import environment.IState; 
import environment.MACAComposedAction;

/**
 * 
 * @author Francesco de Comite
 * 3 Apr 2007
 *
 */
public class MACAAgent extends AbstractAgent {
	
	/** A filter extract information from a state, 
	 * 
	 * and create a restricted description of this state.*/
	protected Filter filter=new IdentityFilter();
	
	private int tokenId; 
	
	private ArrayList<MACAAgent> myNeighbours=new ArrayList<MACAAgent>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param s
	 * @param al
	 */
	public MACAAgent(IEnvironment s, ISelector al,Filter f) {
		super(s, al);
		this.filter=f; 
		// A MACAAgent is its own neighbour
		this.myNeighbours.add(0,this);
	}
	
	protected int nbNeighbours=1;
	
	public void addNeighbour(MACAAgent ag){
		this.myNeighbours.add(ag); 
		this.nbNeighbours++;
	}
	
	public void setInitialState(IState s) {
	this.oldState=this.filter.filterState(s,universe);	
	this.currentState=this.filter.filterState(s,universe);	
	}

	/**
	 * Using the IdentityFilter
	 * @param s
	 * @param al
	 */
	public MACAAgent(IEnvironment s, ISelector al) {
		super(s, al);
	}

	/**
	 * In the multi-agent case, action is performed inside 
	 * the global environment.
	 */
	public IAction act() {
		System.out.println("This method should not be called\n in a multi-agent context : exit");
		System.exit(0); 
		return null;
	}
	
	// a is in fact a composed action
	public void learn(IState oldState,IState newState,IAction a,double reward){
		// get the actions from its neighbours
		MACAComposedAction ca=(MACAComposedAction)a;
		Iterator<MACAAgent> cursor=this.myNeighbours.iterator();
		MACAComposedAction collect=new MACAComposedAction(this.nbNeighbours);
		int index=0;
		while(cursor.hasNext()){
			MACAAgent currentAgent=cursor.next();
			int id=currentAgent.getTokenId(); 
			collect.setAction(ca.getAction(id),index); 
			index++;
		}// while
		// As a MACAAgent is always its own neighbour, 
		// collect contains at least the last action of this 
		// MACAAgent
		this.algorithm.learn(this.filter.filterState(oldState,this.universe),
				this.filter.filterState(newState,this.universe)
				, collect, reward); 
	}

	/**
	 * @return the tokenId
	 */
	public int getTokenId() {
		return tokenId;
	}

	/**
	 * @param tokenId the tokenId to set
	 */
	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}
	

}
