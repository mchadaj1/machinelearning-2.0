
package Log; 

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
UTN-FRSF
emISI -- Doctorado en Ingeniería (Sistemas de Información)
Pedro E. Colla (pcolla@frsf.utn.edu.ar) (2007)
*/

import java.lang.Math;
import java.io.*; 
import java.util.*;
import java.util.Random; 
import java.lang.reflect.*;
import java.lang.*;

/*===
	   	   This class is a collection of methods used for tracing and logging using a Singleton design pattern
	   	   in order to have just one instance of it.
	   	   printLog        == Print the entry thru std output
	   	   printErr        == Print the entry thru err output
	   	   printTrace      == Print a trace on where the execution path is
	   	   printClassInfo  == Print information about a class
	   	   printSystemInfo == Print System Information
	   	   printArray      == Print Array Information
====*/   
		
public class Trace
{
  /* Constructor
     Assumed trace level 1 (minimum) by default, no verbosity and no method tracing enabled
   */
  private Trace (int l)
  {
    this.TRACELEVEL = l;
    
    this.verbosity=false;
    this.traceEnabled=false;
      
  }

            
  /* getTraceObject
   * @param level Is the level (1..5) of tracing, only calls at this level or above will be activated
   * @return Returns and object of the class Trace
   */

  public static synchronized Trace getTraceObject(int level)
  {
	//if this is the first instance then call it  
    if (lpTrace == null) {
       lpTrace = new Trace(level);	
    } 
    return lpTrace;
  }
  
  /* getTraceObject
   * @return Returns and object of the class Trace assuming no trace activated
   */

    
  public static synchronized Trace getTraceObject() {
  
	//if this is the first instance then call it  
    if (lpTrace == null) {
       lpTrace = new Trace(0);		
    }   
    return lpTrace;
  }

  /* printTrace
   * @param  TRACE Is the trace level of this call, the message is printed if TRACELEVEL is at or below this level
   */
  
  public void printTrace(int TRACE) {
	  if ((this.verbosity==false) || (this.traceEnabled==false)) {
		return;    
    }
	  this.printInternalTrace();
  }
  
  /* printTrace
   * Prints only if the verbosity and trace are enabled
   */
  
  public void printTrace() {
	  if ((this.verbosity==false) || (this.traceEnabled==false)) {
	   	return;    
    }
	  this.printInternalTrace(); 
  }
	  
  /* printInternalTrace
   * Internal trace produces information about the caller and current stack of the processing
   */
  private void printInternalTrace() {
	 
    Throwable th = new Throwable();  
	StackTraceElement[] es = th.getStackTrace();
    int i=es.length;
    int j=2;
    String Caller="";
      if (i>j) {
	      StackTraceElement s=es[j+1];
	      Caller=s.getClassName()+"."+s.getMethodName();
      }   
    StackTraceElement e = es[j];
    System.out.println("Trace Nest["+j+"]["+TRACELEVEL+"] class("+e.getClassName()+":"+e.getMethodName()+") Parent("+Caller+")");
  }

  /*
   * If clone() is called then an exception will occur
   */
    
  public Object clone()
	throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException(); 
    
  }

  /*
   * Static variables of the Singleton Instance
   */
   
  private static Trace   lpTrace;
  public  static int     TRACELEVEL;
  public  static boolean traceEnabled;
  public  static boolean verbosity;	
  
  /*
   * printLog (Print thru STDOUT)
   * @param m Message to Print at all trace levels
   */
   
  public void printLog(String m) {
    Throwable th = new Throwable();  
	StackTraceElement[] es = th.getStackTrace();
    int i=es.length-1;
    int j=1;
      
    StackTraceElement e = es[j];
    String t="["+j+"]["+TRACELEVEL+"] "+e.getClassName()+"."+e.getMethodName();
    this.printLog(1,t,m);
    
    return;
  }    
  
  /*
   * printLog
   * @param TRACE Tracelevel, message is print if TRACE>=TRACELEVEL
   * @param m     Message to Print at all trace levels
   */
  public void printLog(int TRACE,String m) {
    if ((TRACELEVEL==0) || (TRACE>TRACELEVEL)) { return; }
    
    
    Throwable th = new Throwable();  
	StackTraceElement[] es = th.getStackTrace();
    int i=es.length;
    int j=1;
    
	 
    StackTraceElement e = es[j];
    String t=e.getClassName()+"."+e.getMethodName();
    this.printLog(TRACE,t,m);
    return;  
	
  }	  
 
  //Print a Log entry
  public void printLog(int TRACE,String t, String m) {
	
   if (TRACE<=TRACELEVEL) {
     Date d = new Date();
     String nowDate=d.toString();  
     System.out.println("[{L} "+nowDate+"--"+t+"]:"+m);
   }    
  }
     
  /*
   * printErr (Print thru STDERR)
   * @param m     Message to Print at all trace levels
   */ 
  public void printErr(String m) {
    Throwable th = new Throwable();  
	StackTraceElement[] es = th.getStackTrace();
    int i=es.length-1;
    int j=1;
      
    StackTraceElement e = es[j];
    String t="["+j+"]["+TRACELEVEL+"] "+e.getClassName()+"."+e.getMethodName();
    this.printErr(1,t,m);
    
    return;
  }
  public void printErr(int TRACE,String m) {
    if ((TRACELEVEL==0) || (TRACE>TRACELEVEL)) { return; }
    
    
    Throwable th = new Throwable();  
	StackTraceElement[] es = th.getStackTrace();
    int i=es.length;
    int j=1;
    
	 
    StackTraceElement e = es[j];
    String t=e.getClassName()+"."+e.getMethodName();
    this.printErr(TRACE,t,m);
    return;  
	
  }	
  
  
  //Print a Trace entry thru STDERR
  public void printErr(int TRACE,String t, String m) {
	
    if (TRACE<=TRACELEVEL) {
     Date d = new Date();
     String nowDate=d.toString();
     System.err.println("[{E} "+nowDate+"--"+t+"]:"+m);
    }    
  }
  
  /* printClassInfo
   * @param TRACE Trace Level (print if TRACE<=TRACELEVEL)
   * @param o     Object to trace information about
   */
   
  public void printClassInfo(int TRACE,Object o) {
     if (TRACE>TRACELEVEL) { return; }
     printClassInfo(o);	  
	 return; 
	  
  }	 
  
  /* printClassInfo
   * @param o     Object to trace information about
   */ 
  public void printClassInfo(Object o) {
	  
	 if (TRACELEVEL==0) { return; } 

     Class c = o.getClass();
     System.out.println("Name: " + c.getName());

     Class superclass = c.getSuperclass();
     if (superclass != null) {
        System.out.println("Superclass: " + superclass.getName());
     }

     System.out.println("Interfaces:");
     Class[] interfaces = c.getInterfaces();
     for (int i = 0; i < interfaces.length; ++i) {
         System.out.println("\t" + interfaces[i].getName());
     }

     System.out.println("Fields:");
     Field[] fields = c.getDeclaredFields();
     for (int i = 0; i < fields.length; ++i) {
         System.out.print("\t");
         printModifiers(fields[i].getModifiers());
         System.out.println((fields[i].getType()).getName()+ " " + fields[i].getName() + ";");
     }

     System.out.println("Methods:");
     Method[] methods = c.getDeclaredMethods();
     
     for (int i = 0; i < methods.length; ++i) {

         System.out.println("\t");
         printModifiers(methods[i].getModifiers());

         System.out.print((methods[i].getReturnType()).getName()+ " " + methods[i].getName() + "(");

         Class[] params = methods[i].getParameterTypes();
         for (int j = 0; j < params.length; ++j) {
              System.out.print(params[j].getName());
              if (j != 0 && j != params.length - 1) {
                  System.out.print(", ");
              }
         }

         System.out.println(");");
     }

}
  
private void printModifiers(int modifiers) {

	   
	    
        if (Modifier.isPrivate(modifiers)) {
            System.out.print("private ");
        }
        else if (Modifier.isProtected(modifiers)) {
            System.out.print("protected ");
        }
        else if (Modifier.isPublic(modifiers)) {
            System.out.print("public ");
        }

        if (Modifier.isAbstract(modifiers)) {
            System.out.print("abstract ");
        }
        if (Modifier.isStatic(modifiers)) {
            System.out.print("static ");
        }
        if (Modifier.isFinal(modifiers)) {
            System.out.print("final ");
        }
        if (Modifier.isTransient(modifiers)) {
            System.out.print("transient ");
        }
        if (Modifier.isVolatile(modifiers)) {
            System.out.print("volatile ");
        }
        if (Modifier.isSynchronized(modifiers)) {
            System.out.print("synchronized ");
        }
        if (Modifier.isNative(modifiers)) {
            System.out.print("native ");
        }
    }
  /* printSystemInfo
   * Prints some information about the system this classes are running at
   */ 

   public void printSystemInfo() {
	   
	    if (TRACELEVEL==0) { return; } 
	   
		double t = (double)Runtime.getRuntime().totalMemory();
		double f = (double)Runtime.getRuntime().freeMemory();
		
		System.out.println("Operating System: "+System.getProperty("os.name")+ " ver " + System.getProperty("os.version")+ " " + System.getProperty("os.arch"));
		System.out.println("Java Vendor: " +System.getProperty("java.vendor")+ " ver " + System.getProperty("java.version"));
		System.out.println("Java Memory Total : " + Math.round(t/1048576D) + " Mb");
		System.out.println("            Free  : " + Math.round(f/1048576D)  + " Mb");
        System.out.println("            Used  : " + Math.round((t-f)/1048576D) + " Mb");
        System.out.println("Java Class Path   : " +System.getProperty("java.class.path"));
        System.out.println("Java Library Path : " +System.getProperty("java.library.path"));
        System.out.println("Java Home Path    : " +System.getProperty("java.home"));
        System.out.println("User:               " +System.getProperty("user.name"));
        
	}

  /* printAbort
   * Prints a string passed and the terminates the program
   * @param m String to print
   */ 
	   
    
   public void printAbort(String m) {
    Throwable th = new Throwable();  
	StackTraceElement[] es = th.getStackTrace();
    int i=es.length-1;
    int j=1;
      
    StackTraceElement e = es[j];
    
    System.out.println("ERROR: "+m);
    System.out.println("At Method:"+e.getClassName()+"."+e.getMethodName());
    System.exit(0);

    return;
  } 
  /* printArray
   * Prints an array passed as parameter
   * @param array Array of Integers to print
   */ 

    public void printArray(int[] array) {
	    
	    if (TRACELEVEL==0) { return; } 
	    
        String string = "{";
        for(int i = 0; i < array.length ; i++)
        {
            string += array[i] + (i != array.length - 1 ? ", " : "");
        }
        string += "}";
        
        this.printLog(string);
    }

  /* printClass
   * Prints a reduced set of information about an object
   * @param o Object to display information about
   */ 

    public void printClass(Object o) {
        this.printLog("Class Information for Object("+o+")");
        this.printLog("     ClassName("+o.getClass().getName()+")");
        this.printLog("   LoaderClass("+ o.getClass().getClassLoader()+")");
        this.printLog("    SuperClass("+ o.getClass().getSuperclass()+") Name("+o.getClass().getSuperclass().getName()+")");
     }

}