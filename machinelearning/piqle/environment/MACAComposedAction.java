

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
 * MACAComposedAction.java
 * 
 * Copyright (C) 2007 Francesco De Comité (decomite_at_lifl.fr)
 *
 */
package environment;

/**
 * @author Francesco De Comité
 *
 * 4 avr. 07
 */
public class MACAComposedAction implements IAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IAction[] actionsArray; 
	
	public MACAComposedAction(int size){
		actionsArray=new IAction[size]; 
	}

	public int getSize(){return this.actionsArray.length;}
	
	public IAction getAction(int i){
			return this.actionsArray[i];
	}
	
	public void setAction(IAction a,int i){
		this.actionsArray[i]=a; 
	}
	
	/* (non-Javadoc)
	 * @see environment.IAction#copy()
	 */
	public Object copy() {
		MACAComposedAction nouvelle=new MACAComposedAction(this.getSize()); 
		for(int i=0;i<this.getSize();i++)
			nouvelle.actionsArray[i]=(IAction)this.actionsArray[i].copy();
		return nouvelle;
	}

	/* (non-Javadoc)
	 * @see environment.IAction#nnCoding()
	 */
	public double[] nnCoding() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see environment.IAction#nnCodingSize()
	 */
	public int nnCodingSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		final int P2=27;
		int result = 1;
		for(int i=0;i<this.actionsArray.length;i++){
		result = (PRIME * result + this.actionsArray[i].hashCode())%P2;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MACAComposedAction other=(MACAComposedAction) obj; 
		for(int i=0;i<this.actionsArray.length;i++){
			if(!this.actionsArray[i].equals(other.actionsArray[i])) 
				return false; 
		}
		return true;
	}

}
