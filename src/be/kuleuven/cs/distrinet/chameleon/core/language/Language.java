package be.kuleuven.cs.distrinet.chameleon.core.language;

import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContextFactory;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.property.PropertyRule;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationRule;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePlugin;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguageProcessor;
import be.kuleuven.cs.distrinet.chameleon.plugin.PluginContainer;
import be.kuleuven.cs.distrinet.chameleon.plugin.ProcessorContainer;
import be.kuleuven.cs.distrinet.rejuse.association.MultiAssociation;
import be.kuleuven.cs.distrinet.rejuse.junit.Revision;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyUniverse;

public interface Language extends PropertyUniverse<ChameleonProperty>, PluginContainer<LanguagePlugin>, ProcessorContainer<LanguageProcessor> {
	
	/**
	 * Clone this language.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
//	public Language clone();
	
	public Revision version();
	
	/**
	 * Return the name of this language.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public String name();
	
	/**
	 * Return the default properties of the given element.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ (* The properties of all rules are added to the result.*)
   @ post (\forall PropertyRule rule; propertyRules().contains(rule);
   @         \result.containsAll(rule.properties(element)));
   @ (* Only the properties given by the property rules are in the result *);
   @ post (\forall Property<Element> p; \result.contains(p);
   @        \exists(PropertyRule rule; propertyRules().contains(rule);
   @           rule.properties(element).contains(p)));
   @*/
	public PropertySet<Element,ChameleonProperty> defaultProperties(Element element, PropertySet<Element,ChameleonProperty> explicit);
	
	/**
	 * Return the list of rule that determine the default properties of an element.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<PropertyRule> propertyRules();
	
	/**
	 * Add a property rule to this language object.
	 * @param rule
	 */
 /*@
   @ public behavior
   @
   @ pre rule != null;
   @
   @ post propertyRules().contains(rule);
   @*/
	public void addPropertyRule(PropertyRule rule);
	
	/**
	 * Remove a property rule from this language object.
	 * @param rule
	 */
 /*@
   @ public behavior
   @
   @ pre rule != null;
   @
   @ post ! propertyRules().contains(rule);
   @*/
	public void removePropertyRule(PropertyRule rule);
	
	/**
	 * Set the name of this language.
	 * @param name
	 *        The new name of this language
	 */
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName() == name;
   @*/
	public void setName(String name);
	
	/**
	 * Return the default namespace attached to this language. A language is always attached to a default namespace because a language
	 * may need access to predefined elements, which are somewhere in the model.
	 * @return
	 */
//	public RootNamespace defaultNamespace();

	/**
	 * A property mutex for the scope property.
	 */
	public PropertyMutex<ChameleonProperty> SCOPE_MUTEX();


//  /**
//   * Return the connector corresponding to the given connector interface.
//   */
// /*@
//   @ public behavior
//   @
//   @ pre connectorInterface != null;
//   @*/
//  public <T extends LanguagePlugin> T plugin(Class<T> pluginInterface);

//  /**
//   * Remove the plugin corresponding to the given plugin interface. The
//   * bidirectional relation is kept in a consistent state.
//   * 
//   * @param <T>
//   * @param pluginInterface
//   */
// /*@
//   @ public behavior
//   @
//   @ pre pluginInterface != null;
//   @
//   @ post plugin(pluginInterface) == null;
//   @*/
//  public <T extends LanguagePlugin> void removePlugin(Class<T> pluginInterface);

//  /**
//   * Set the plugin corresponding to the given plugin interface. The bidirectional relation is 
//   * kept in a consistent state.
//   * 
//   * @param <T>
//   * @param pluginInterface
//   * @param plugin
//   */
// /*@
//   @ public behavior
//   @
//   @ pre pluginInterface != null;
//   @ pre plugin != null;
//   @
//   @ post plugin(pluginInterface) == plugin; 
//   @*/
//  public <T extends LanguagePlugin> void setPlugin(Class<T> pluginInterface, T plugin);
  
  /**************
   * PROCESSORS *
   **************/
  
//  /**
//   * Return the processors corresponding to the given processor interface.
//   */
// /*@
//   @ public behavior
//   @
//   @ post \result.equals(processorMap().get(connectorInterface));
//   @*/
//  public <T extends LanguageProcessor> List<T> processors(Class<T> connectorInterface);
//
//  /**
//   * Remove the given processor. The
//   * bidirection relation is kept in a consistent state.
//   * 
//   * @param <T>
//   * @param connectorInterface
//   */
// /*@
//   @ public behavior
//   @
//   @ pre connectorInterface != null;
//   @ pre processor != null;
//   @
//   @ post !processor(connectorInterface).contains(processor); 
//   @*/
//  public <T extends LanguageProcessor> void removeProcessor(Class<T> connectorInterface, T processor);
//

//  /**
//   * Add the given processor to the list of processors correponding to the given connector interface. 
//   * The bidirectional relation is kept in a consistent state.
//   * 
//   * @param <T>
//   * @param connectorInterface
//   * @param connector
//   */
// /*@
//   @ public behavior
//   @
//   @ pre connectorInterface != null;
//   @ pre processor != null;
//   @
//   @ post processor(connectorInterface).contains(processor); 
//   @*/
//  public <T extends LanguageProcessor> void addProcessor(Class<T> connectorInterface, T processor);
//
//  /**
//   * Copy the processor mapping from the given language to this language.
//   */
// /*@
//   @ public behavior
//   @
//   @ post (\forall Class<? extends Processor> cls; from.processorMap().containsKey(cls);
//   @         processors(cls).containsAll(from.processorMap().valueSet());
//   @*/
//	public <S extends Processor> void cloneProcessorsFrom(Language from);
	
	/**
	 * Return the mapping of classes/interfaces to the processors of that kind.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	@Override
   public Map<Class<? extends LanguageProcessor>, List<? extends LanguageProcessor>> processorMap();
	
  /**************************************************************************
   *                                 PROPERTIES                             *
   **************************************************************************/
  
  /**
   * Return the properties that can be used for elements in this model.
   * 
   * For every class of properties, one object is in the set.
   * @return
   */
  @Override
public Set<ChameleonProperty> properties();

  /**
   * Return the object representing the association between this language and the
   * properties to which it is attached.
   * 
   * DO NOT MODIFY THE RESULTING OBJECT. IT IS ACCESSIBLE ONLY BECAUSE OF THE 
   * VERY DUMB ACCESS CONTROL IN JAVA.
   */
  @Override
public MultiAssociation<Language,ChameleonProperty> propertyLink();
  
  /**
   * 
   * @param name
   * @return
   * @throws ChameleonProgrammerException
   *         There is no property with the given name.
   */
  public ChameleonProperty property(String name) throws ChameleonProgrammerException;
  
  
  /**************************************************************************
   *                           DEFAULT NAMESPACE                            *
   **************************************************************************/
  
  /**
   * Set the default namespace.
   */
//  public void setDefaultNamespace(RootNamespace defaultNamespace);

  /**
   * Return the association object that represents that association with the
   * default (root) namespace.
   */
//  public Association<Language, View> viewLink();
  
//  public View view();
  
//  public Project project();
  /**
   * Return the factory for creating lookup strategies.
   */
  public LookupContextFactory lookupFactory();
  
  /**
	 * Returns true if the given character is a valid character
	 * for an identifier.
	 */
	public boolean isValidIdentifierCharacter(char character);
	
	/**
	 * Return the list of rule that determine the language specific validity conditions of an element.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<VerificationRule> validityRules();
	
	/**
	 * Add a property rule to this language object.
	 * @param rule
	 */
 /*@
   @ public behavior
   @
   @ pre rule != null;
   @
   @ post propertyRules().contains(rule);
   @*/
	public void addValidityRule(VerificationRule rule);
	
	/**
	 * Remove a property rule from this language object.
	 * @param rule
	 */
 /*@
   @ public behavior
   @
   @ pre rule != null;
   @
   @ post ! propertyRules().contains(rule);
   @*/
	public void removeValidityRule(VerificationRule rule);
	
	/**
	 * Verify the given element. 
	 * 
	 * This method verifies constraints on the element that are specific for the language.
	 * One example is the validity of the name of an element. Different languages may have different
	 * rules with respect to the validity of a name.
	 * 
	 * @param element
	 * @return
	 */
	public Verification verify(Element element);
	
	/**
	 * Flush the caches kept by this language. Caches of model elements are flushed separately. 
	 * The default behavior is to do nothing.
	 */
	@Override
   public void flushCache();

}
