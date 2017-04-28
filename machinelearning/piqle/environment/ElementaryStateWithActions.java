
/*	This program is free software; you can redistribute it and/or modify
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
 *
 *
 *    Copyright (C) 2006 Francesco De Comit�
 *    ExtendedStateWithActions.java
 *    21 nov. 06
 *
 */
package environment;

import java.util.ArrayList;
import java.util.Iterator;




/**
 * For ElementaryCooperativeAgents : a local state is composed of : 
 * <ul>
 * <li> The limited vision (perception) of the global environment state.</li>
 * <li> The actions of the agent's neighbours</li>
 * </ul>
 * 
 * @author Francesco De Comit�
 * 21 nov. 06
 */
 abstract public class ElementaryStateWithActions<F extends IState>	
 	extends AbstractState {

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;

	/** memorize the neighbours 'actions */
	protected ArrayList<NullableAction> neighboursActions; 
	
	protected F perception=null; 
	
	protected int[] primes={2,3,5,7,11,13,17,19,23,29,31,37}; 
	protected int sizePrimes=this.primes.length; 
	/**
	 * @param ct
	 */
	public ElementaryStateWithActions(IEnvironment ct) {
		super(ct);
	}
	
	
	
	public ElementaryStateWithActions(IEnvironment ct,IState s,
			ArrayList<NullableAction> actions){
		this(ct);
		this.perception=(F)s;
		neighboursActions=new ArrayList<NullableAction>();
		Iterator<NullableAction> cur=actions.iterator();
		while(cur.hasNext()){
			neighboursActions.add((NullableAction)cur.next().copy()); 
		}
		
	}
	
	
	
	public int hashCode(){
		int sum=this.perception.hashCode();
		Iterator<NullableAction> cur=neighboursActions.iterator();
		int index=0;
		while(cur.hasNext()){
			sum+=cur.next().hashCode()%this.primes[index%this.sizePrimes];
			index++;
		}                               
		return sum%23;
	}//hashCode
	
	// This method can not be defined as static, otherwise it 
	// would not be overriden.
	protected ElementaryStateWithActions buildState(IEnvironment universe)
	{	System.out.println("You MUST override method ExtendedStateWithActions.buildState() !"); 
		System.out.println("Exiting");
		System.exit(0);
		return null;}
	
	public IState copy(){
	
		ElementaryStateWithActions  nouveau=buildState(this.myEnvironment);
		nouveau.perception=this.perception.copy();
		nouveau.neighboursActions=new ArrayList<NullableAction>(this.neighboursActions);
		return nouveau;
	}
	
	
	public boolean equals(Object o){
		if(!(o instanceof ElementaryStateWithActions)) return false;
		ElementaryStateWithActions eswa=(ElementaryStateWithActions)o;
		if(!eswa.perception.equals(this.perception)) return false;
		if(eswa.neighboursActions.size()!=this.neighboursActions.size()) return false;
		// TODO do we need such a complicated loop ?
		Iterator<NullableAction> cur1=this.neighboursActions.iterator();
		Iterator<NullableAction> cur2=eswa.neighboursActions.iterator();
		while(cur1.hasNext()){
			NullableAction a1=(NullableAction)cur1.next();
			NullableAction a2=(NullableAction)cur2.next();
			if(!a1.equals(a2)) return false;
		}
		return true;
		}
	
	public int nnCodingSize(){
		int size=this.perception.nnCodingSize(); 
		Iterator<NullableAction> cur=neighboursActions.iterator();
		while(cur.hasNext()){
			size+=cur.next().nnCodingSize();
		}
		return size; 
	}
 
	public double[] nnCoding(){
		double code[]=new double[this.nnCodingSize()];
		int indexInit=this.perception.nnCodingSize(); 
		System.arraycopy(this.perception.nnCoding(),0,
						 code,0,indexInit);
		Iterator<NullableAction> cur=neighboursActions.iterator();
		while(cur.hasNext()){
			NullableAction act=cur.next();
			int length=act.nnCodingSize(); 
			System.arraycopy(act.nnCoding(),0,code,indexInit,length); 
			indexInit+=length;
		}
		return code;
	}
	
	public String toString(){
		String s=""; 
		s+=this.perception+"\n"; 
		Iterator<NullableAction> cur=neighboursActions.iterator();
		int i=0; 
		while(cur.hasNext()){
			s+="\tNeighbour "+i+" action :"+cur.next()+"\n";
			i++;
		}
		return s; 
	}
 }
 //ElementaryStateWithActions



