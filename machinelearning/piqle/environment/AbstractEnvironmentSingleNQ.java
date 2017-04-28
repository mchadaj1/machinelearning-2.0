
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
 * AbstractEnvironmentSingleNQ.java
 * 
 * Copyright (C) 2007 Francesco De Comit� (decomite_at_lifl.fr)
 *
 */
package environment;

/**
 * 
 * For implementation of Nash Q-learning like algorithms, 
 * we need to collect the reward of all agents before learning
 * 
 * @author Francesco De Comit�
 *
 * 14 juin 07
 */
abstract public class AbstractEnvironmentSingleNQ extends AbstractEnvironmentSingle {

	abstract public double[] getRewardVector(IState s1,
											 IState s2, 
											 AbstractComposedAction ca);

}
