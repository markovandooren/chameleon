package org.aikodi.chameleon.test;

import java.io.IOException;

import junit.framework.TestSuite;

import org.aikodi.chameleon.input.ParseException;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.ProjectException;
import org.aikodi.chameleon.workspace.View;
import org.junit.After;
import org.junit.Before;

/**
 * The top level test class for Chameleon tests. This class provides the infrastructure
 * that allows tests to be reused for different languages and different test configurations.
 * 
 * @author Marko van Dooren
 */
public abstract class ModelTest {
	
	 /**
	  * Create a new test that uses the given provider to create models
	  * for testing.
	 * @throws IOException 
	 * @throws ParseException 
	  */
	/*@
	  @ public behavior
	  @
	  @ post modelProvider() == provider;
    @ post baseRecursive();
    @ post customRecursive();
	  @*/
	 public ModelTest(Project project) throws ProjectException {
     _project = project;
     setUp();
	 }
	 
   /**
    * This method is invoked during setup to set the levels of the loggers.
    * It allows subclasses to easily changes those levels if tests fail, without
    * having to change this class.
    * 
    * The default behavior is to leave log levels untouched. They are DEBUG by default.
    */
   public void setLogLevels() {
    	// do nothing by default
   }

   /**
    * Use the model provider to create a model, and store its language object
    * in this test.
    * 
    * This method also invokes setLogLevels() to set the log levels.
   * @throws IOException 
   * @throws ParseException 
    * @throws Exception
    */
  /*@
    @ public behavior
    @
    @ // not quite correct
    @ post language() == provider().model();
    @*/
   @Before
   public void setUp() throws ProjectException {
    	setLogLevels();
    }
    
   @After
   public void tearDown() {
  	 _project = null;
   }
   
   /**
    * Return the language object of the model being tested.
    */
  /*@
    @ public behavior
    @
    @ post \result != null;
    @*/
   public Project project() {
   	 return _project;
   }
   
   public View view() {
  	 return project().views().get(0);
   }
  
   private Project _project;
    
}
