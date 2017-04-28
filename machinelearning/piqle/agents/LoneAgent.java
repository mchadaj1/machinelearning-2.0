
package agents; 
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
 *    LoneAgent.java
 *    Copyright (C) 2004 Francesco De Comit�
 *
 */

import algorithms.ISelector;
import environment.IEnvironmentSingle;

/** The basic behavior of an Agent is : 
 <ul>
<li> According to the current state of the environment, choose the action</li>
<li> Apply this action, get the reward</li>
</ul>

Every Agent can call its underlying <i>algorithm</i>, and ask it to choose the action. 

 @author Francesco De Comite (decomite at lifl.fr)
 @version $Revision: 1.0 $ 
*/


public class LoneAgent extends AbstractAgent{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Place the agent  in the environment.*/
    public LoneAgent(IEnvironmentSingle s, ISelector al){ 
    super(s,al);
    // For the multi-agent case (ICML Octopus)
    if(s!=null)
    	this.currentState=s.defaultInitialState();  
    }

	
	
}
