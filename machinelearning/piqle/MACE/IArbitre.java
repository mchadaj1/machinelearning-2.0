
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

/*    IReward.java
 *    Apr 07
 * 	  
 *    Copyright (C) 2007 Francesco De Comité
 *    Copyright (C) 2007 Pedro Colla
 *
 *    This is a joint effort to implement an alternative Multi-Agent Cooperative framework (MAC)
 */

package MACE;

import agents.*;
import environment.*;
import java.io.Serializable;

/** IArbitre
    Interface to Arbitre objects which are used as part of the MACE Architectural adaptation
*/
public interface IArbitre extends Serializable{

    public int getMaxIter();
    public void setMaxIter(int t);
    // Reward of last episode
    public double getRewardForEpisode();
    public int episode(IState s);

}