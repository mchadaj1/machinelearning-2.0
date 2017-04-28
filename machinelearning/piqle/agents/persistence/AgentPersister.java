package agents.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;

import agents.IAgent;
import agents.LoneAgent;
import environment.IEnvironment;
import environment.IEnvironmentSingle;

import util.ExtensionFileFilter;

public class AgentPersister implements IAgentPersister {
	public void saveAgent(String fileName, Object agent) {
		File fileToSave; 
		ObjectOutputStream outStream;
		fileToSave=new File(fileName+".agt"); 
		try{
		    outStream=new ObjectOutputStream(new FileOutputStream(fileToSave)); 
		    outStream.writeObject(agent); 
		    outStream.close(); 
		    
		}
		catch(Exception e){ System.err.println("Problem when trying to save agent "+e.getMessage()+"***"+e); 
		}
		
		}
	public void saveAgent(Object agent) {
		JFileChooser chooser=new JFileChooser();
		chooser.setCurrentDirectory(new File(".")); 
		File sauvegarde; 
		ObjectOutputStream sortie;
		String ext[]={"agt"};
		ExtensionFileFilter filter = new ExtensionFileFilter(ext); 
		filter.setDescription("Agent file"); 
		chooser.setFileFilter(filter); 
		int returnVal = chooser.showSaveDialog(null); 
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		    { System.out.println("You chose to open this file: " 
					 + chooser.getSelectedFile().getName()); 
		    
		    sauvegarde=chooser.getSelectedFile(); 
		    }
		else 
		    {
			sauvegarde=new File("raymond.agt"); 
		
		    }
		try{
		sortie=new ObjectOutputStream(new FileOutputStream(sauvegarde)); 
		sortie.writeObject(this); 
		sortie.close(); 
		}
		catch(Exception e){ System.err.println("Problem when trying to save agent "+e.getMessage()); 
		}
		
		}
    /** Read an agent's description from a file. */
	public IAgent readAgent(String fichier, IEnvironment s) {
	File fichierALire=new File(fichier+".agt"); 
	ObjectInputStream entree;
	LoneAgent resultat=null; 
	try{
	    entree=new ObjectInputStream(new FileInputStream(fichierALire)); 
	    resultat=(LoneAgent)entree.readObject(); 
	    entree.close(); 
	}
	catch(Exception e){ System.err.println("Problem when reading agent file. "+e.getMessage()); }
	//	resultat.currentState=((IEnvironmentSingle) s).defaultInitialState(); 
	// TODO [francesco]: Is it correct to set the initial state here?
	resultat.setInitialState(((IEnvironmentSingle) s).defaultInitialState()); 
	return resultat; 
	}// readAgent
	
	
	
	/** Read an agent's description from a file, but find itself the universe the agent was into */
	public IAgent readAgent(String fichier) {
	File fichierALire=new File(fichier+".agt"); 
	ObjectInputStream entree;
	LoneAgent resultat=null; 
	try{
	    entree=new ObjectInputStream(new FileInputStream(fichierALire)); 
	    resultat=(LoneAgent)entree.readObject(); 
	    entree.close(); 
	}
	catch(Exception e){ System.err.println("Problem when reading agent file. "+e.getMessage()); }
	IEnvironmentSingle s=(IEnvironmentSingle) resultat.getEnvironment(); 
	//	resultat.currentState=s.defaultInitialState(); 
	// TODO [francesco]: Is it correct to set the initial state here?
	resultat.setInitialState(s.defaultInitialState()); 
	return resultat; 
	}// readAgent

	/** Same as above, but the file name is not given.*/
	public IAgent readAgent(IEnvironment s) {
	JFileChooser chooser=new JFileChooser();
	chooser.setCurrentDirectory(new File(".")); 
	String extension="agt";
	File fichierALire; 
	ObjectInputStream entree;
	ExtensionFileFilter filter = new ExtensionFileFilter(); 
	LoneAgent resultat=null; 
	filter.addExtension(extension);  
	filter.setDescription("Agent file"); 
	chooser.setFileFilter(filter); 
	int returnVal = chooser.showOpenDialog(null); 
	if(returnVal == JFileChooser.APPROVE_OPTION) 
	    { System.err.println("You choose to open this file: " 
				 + chooser.getSelectedFile().getName()); 
	    
	    fichierALire=chooser.getSelectedFile(); 
	    }
	else 
	    {
		return null; 
	
	    }
	try{
	entree=new ObjectInputStream(new FileInputStream(fichierALire)); 
	resultat=(LoneAgent)entree.readObject(); 
	entree.close(); 
	}
	catch(Exception e){ System.err.println("Problem when reading agent file. "+e.getMessage()); }
	//	resultat.currentState=((IEnvironmentSingle) s).defaultInitialState(); 
	// TODO [francesco]: Is it correct to set the initial state here?
	resultat.setInitialState(((IEnvironmentSingle) s).defaultInitialState()); 
	return resultat; 
	}


}
