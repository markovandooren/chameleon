package chameleon.workspace;



/**
 * An abstract super class for creating projects.
 * 
 * @author Marko van Dooren
 */
public interface ProjectBuilder {

	/**
	 * Return the project built by this builder.
	 * 
	 * @throws ProjectException 
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Project project() throws ProjectException;
	
}
