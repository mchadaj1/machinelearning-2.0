

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
 * MACAEnvironment.java
 * 
 * Copyright (C) 2007 Francesco De Comit� (decomite_at_lifl.fr)
 *
 */
package environment;

/**
 * @author Francesco De Comit�
 *
 * 3 avr. 07
 */
public class MACAEnvironment implements IEnvironment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see environment.IEnvironment#getActionList(environment.IState)
	 */
	public ActionList getActionList(IState s) {
		System.out.println("Method getActionList from MACAEnvironment"); 
		System.out.println("This method should not be used"); 
		System.exit(0);
		return null;
	}

	/* (non-Javadoc)
	 * @see environment.IEnvironment#getReward(environment.IState, environment.IState, environment.IAction)
	 */
	public double getReward(IState s1, IState s2, IAction a) {
		System.out.println("You should have instantiated method");
		System.out.println("getReward from class MACAEnvironment");
		System.exit(0);
		return 0;
	}

	/* (non-Javadoc)
	 * @see environment.IEnvironment#isFinal(environment.IState)
	 */
	public boolean isFinal(IState s) {
		System.out.println("You should have instantiated method");
		System.out.println("isFinal from class MACAEnvironment");
		System.exit(0);
		return false;
	}

	/* (non-Javadoc)
	 * @see environment.IEnvironment#successorState(environment.IState, environment.IAction)
	 */
	public IState successorState(IState s, IAction a) {
		// TODO Auto-generated method stub
		System.out.println("You should have instantiated method");
		System.out.println("successorState from class MACAEnvironment");
		System.exit(0);
		return null;
	}

	/* (non-Javadoc)
	 * @see environment.IEnvironment#whoWins(environment.IState)
	 */
	public int whoWins(IState s) {
		// TODO Auto-generated method stub
		System.out.println("You should have instantiated method");
		System.out.println("whoWins from class MACAEnvironment");
		System.exit(0);
		return 0;
	}

}
