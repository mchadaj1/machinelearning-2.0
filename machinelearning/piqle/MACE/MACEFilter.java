
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

/*    MACEFilter.java
 *    Apr 07
 * 	  
 *    Copyright (C) 2007 Francesco De Comité
 *    Copyright (C) 2007 Pedro Colla
 *
 *    This is a joint effort to implement an alternative Multi-Agent Cooperative framework (MAC)
 */

/** Two methods to change the view an elementary agent can have over 
 * a multi agent state, and perhaps the actions of its neighbour agents */


import java.io.Serializable;
import environment.*;
import agents.*;
import Log.*;


public class MACEFilter extends Filter{
    //Owner data
    public MACEAgent owner;
	
    
    public IState filterState(IState s,IEnvironment ct) {
	   if (Trace.verbosity==true) Trace.getTraceObject().printTrace();
	   return (IState)s.copy();
   }	   
}