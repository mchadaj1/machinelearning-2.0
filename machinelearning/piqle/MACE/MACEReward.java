
package MACE; 
/*
UTN-FRSF
emISI -- Doctorado en Ingeniería (Sistemas de Información)
RIIA07

Pedro E. Colla (2007)
*/


import java.io.*; 
import java.util.*;

import java.lang.reflect.*;
import java.lang.*;
import agents.*;
import Log.*;

/*===
	   	   This class is used for tracing and logging using a Singleton design pattern
	   	   in order to have just one instance of it.
	   	   printLog   == Print the entry thru std output
	   	   printErr   == Print the entry thru err output
	   	   printTrace == Print a trace on where the execution path is
	   	   
====*/   
		
public class MACEReward implements IReward
{
   	
  private MACEReward ()
  {
	if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	  
    rewards=new HashMap();      
	  
  }

  
  public static synchronized MACEReward getRewardObject() {
    if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	
	//if this is the first instance then call it  
    if (lpReward == null) {
       lpReward = new MACEReward();		
    }   
    return lpReward;
  }
  
  public Object clone()
	throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException(); 
    
  }

  private static MACEReward  lpReward;
  public  static HashMap     rewards;

  public  double get(IAgent aa) {
	  if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	  
	  return (Double) (rewards.get((Object)aa));
  }
  public void set(IAgent aa,double r) {
	  if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	
	  rewards.put((Object)aa,(Object)r);
  }
  
  public void add(IAgent aa,double r) {
	  if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	
	  Double nr=(Double) (rewards.get((Object)aa));
	  nr+=r;
	  rewards.put((Object)aa,(Object)nr);
	  return;
  }
  public void reset() {
      if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	
	  Set ks=this.rewards.keySet();
	  Iterator it=ks.iterator();
	  while (it.hasNext()) {
		IAgent key=(IAgent)it.next();
		this.set(key,0.0);
		  
	  }		  
	  
  }	  	  

  public double sum() {
      if (Trace.verbosity==true) Trace.getTraceObject().printTrace();    	
	  double s=0.0;
	  
      Set ks=this.rewards.keySet();
	  Iterator it=ks.iterator();
	  while (it.hasNext()) {
		IAgent key=(IAgent)it.next();
		double r=(Double) (rewards.get((Object)key));
		s+=r;
		  
	  }		  
	  
	  return s;
  }	  	  
	 
	   
}