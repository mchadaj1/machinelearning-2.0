package agents.persistence;

import agents.IAgent;
import environment.IEnvironment;

public interface IAgentPersister<TAgent> {
	/**
	 * Save an Agent into a file : by reading it again, you can continue the
	 * learning.
	 */
	public void saveAgent(TAgent Agent);

	/** Same as above, but the file name is given. */
	public void saveAgent(String fileName, TAgent Agent);

	/** Read an agent's description from a file. */
	public IAgent readAgent(String fichier, IEnvironment s);

	/**
	 * Read an agent's description from a file, but find itself the universe the
	 * agent was into
	 */
	public IAgent readAgent(String fichier);

	/** Same as above, but the file name is not given. */
	public IAgent readAgent(IEnvironment s);

}