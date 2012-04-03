package chameleon.eclipse;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import chameleon.core.language.Language;
import chameleon.eclipse.connector.EclipseBootstrapper;
import chameleon.eclipse.presentation.PresentationModel;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.plugin.build.Builder;
import chameleon.plugin.output.Syntax;

/**
 * @author Marko van Dooren
 * @author Manuel Van Wesemael
 * @author Joeri Hendrickx
 *
 * A class that manages the languages of the chameleonEditor. This class is
 * implemented with the singleton pattern
 */
public class LanguageMgt {

	  public final static String LANGUAGE_EXTENSION_ID = "chameleon.eclipse.language";
	
    /*
      * The instance of this language management
      */
    private static LanguageMgt instance;

    // keeps track of the presentation model for each of the languages
    private Map<String, PresentationModel> presentationModels;

    // the languages names
    private Map<String, EclipseBootstrapper> languages;

    /**
     * creates a new mapping for the languages & presenation models These are
     * instantiated from the XML file
     *
     */
    private LanguageMgt() {
        presentationModels = new HashMap<String, PresentationModel>();
        languages = new HashMap<String, EclipseBootstrapper>();
        try {
            //loadJars();
            loadPlugins();
        }
//        catch (IOException e) {
//            System.err.println("Couldn't load languages : "+e.getMessage());
//        }
        catch (CoreException e) {
        	  System.err.println("Couldn't load languages : "+e.getMessage());
        	  e.printStackTrace();
        }
    }
    
    /**
     * Load the Chameleon language plugins. If the plugin extends the Chameleon language extensions point, it is
     * automatically loaded. For each language, the corresponding bootstrapper object is added as a value to the map of languages, 
     * with the language name as the key.
     */
    private void loadPlugins() throws CoreException {
    	IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(LANGUAGE_EXTENSION_ID);
    	for (IConfigurationElement e : config) {
				EclipseBootstrapper bootstrapper = (EclipseBootstrapper) e.createExecutableExtension("class");
				addLanguage(bootstrapper);
    	}
    }

//    private void loadJars() throws IOException {
//    	URL url = pluginURL(ChameleonEditorPlugin.PLUGIN_ID, "languages/");
//    	FilenameFilter filter = fileNameFilter(".jar");
//    	File[] files = (new File(url.getFile())).listFiles(filter);
//    	URL[] urls = new URL[files.length];
//
//    		for (int i = 0; i < urls.length; i++) {
//    			try {
//    				String s = "jar:"+files[i].toURL().toExternalForm()+"!/";
//    				urls[i] = new URL(s);
//    			}
//    			catch (MalformedURLException e) {
//    				System.err.println("Incorrect language ["+i+"] "+files[i].toString());
//    			}
//    		}
//
//    		languageloader = new URLClassLoader(urls, Element.class.getClassLoader());
//
//    		System.out.println("\nLoading Languages ===========");
//    		System.out.println("Filename   \tpackage      \tLanguage          \tModel");
//
//    		for (int i = 0; i < urls.length; i++) {
//    			try {
//    				String[] filenameParts = urls[i].getFile().split("/");
//    				String filename = filenameParts[filenameParts.length-1].replace("!","");
//    				String packagename = filename.substring(0, filename.length()-4);
//
//    				try {
//    					EclipseBootstrapper id = (EclipseBootstrapper) languageloader.loadClass(packagename+".LanguageModelID").newInstance();
//    					addLanguage(id);
//    					//                    ChameleonEditorExtension editorExt = (ChameleonEditorExtension)languageloader.loadClass(packagename+".tool."+id.getLanguageName()+"EditorExtension").newInstance();
//    					//                    tools.put(id.getLanguageName(),editorExt);
//    					System.out.println(filename+"\t"+packagename+"\t"+id.getLanguageName()+" "+id.getLanguageVersion()+"\t"+id.getDescription());
//    				}
//    				catch(NoClassDefFoundError throwable) {
//    					throwable.printStackTrace();
//    					System.err.println("Error while loading language : "+packagename+"\nNo LanguageModelID is supplied.");
//    				}
//    				catch (InstantiationException e) {
//    					System.err.println("Error while loading language : "+packagename+"\n"+e.getMessage());
//    				}
//    				catch (ClassCastException e) {
//    					System.err.println("Error while loading language : "+packagename+"\nLanguageModelID is not of the correct type.");
//    				}
//    				catch (IllegalAccessException e) {
//    					System.err.println("Error while loading language : "+packagename+"\n"+e.getMessage());
//    				}
//    				catch (ClassNotFoundException e) {
//    					e.printStackTrace();
//    					System.err.println("Error while loading language : "+packagename+"\nNo LanguageModelID is supplied.");
//    				}
//    			} catch (Exception e){
//    				System.err.println("Couldnt load language from "+urls[i]+" : "+e.getMessage());
//    			}
//    		}
//    		System.out.println("Done.\n");
//    }
    
    public static List<File> allFiles(URL directory, FilenameFilter filter) {
    	File file = new File(directory.getFile());
    	return allFiles(file, filter);
    }
    
    /**
     * Return all files directly or indirectly in the given directory that satisfy the
     * given file name filter.
     */
    public static List<File> allFiles(File dir, FilenameFilter filter) {
    	List<File> files = new ArrayList<File>();
    	File[] local = dir.listFiles(filter);
    	for(int i=0; i<local.length;i++) {
    		files.add(local[i]);
    	}
    	local = dir.listFiles(new FileFilter(){
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});
    	for(int i=0; i<local.length;i++) {
    		files.addAll(allFiles(local[i],filter));
    	}
    	return files;
    }

		public static FilenameFilter fileNameFilter(final String extension) {
			FilenameFilter filter = new FilenameFilter(){
    		public boolean accept(File dir, String name) {
    			return name.endsWith(extension);
    		}};
			return filter;
		}

		public static URL pluginURL(String pluginID, String directory) throws IOException {
			URL url = FileLocator.toFileURL(FileLocator.find(
    			Platform.getBundle(pluginID),
    			new Path(directory), null));
			return url;
		}

		private void addLanguage(EclipseBootstrapper bootstrapper) {
			languages.put(bootstrapper.getLanguageName(), bootstrapper);
		}

    /**
     * @return The one and only instance of this language management
     */
    public static LanguageMgt getInstance() {
        if (instance == null) instance = new LanguageMgt();
        return instance;
    }

    /**
     * @return an array containing string representations of all the supported
     *         languages of the chameleonEditor
     */
    public synchronized String[] getLanguageStrings() {
        return languages.keySet().toArray(new String[0]);
    }

    public synchronized Language createLanguage(String name) {
    	try {
				return languages.get(name).createLanguage();
			} catch (Exception e) {
				// FIXME this should not be able to happen. Does splitting Language into Language and Model help? Don't think so.
				e.printStackTrace();
				throw new ChameleonProgrammerException(e);
			}
    }
    
//    /**
//     *
//     * @param languageString
//     *            The string representation of the language to be used
//     * @return gives the correct metaModel for the given language string. It is
//     *         advised to call getLanguageStrings so the correct string
//     *         representation can be used
//     */
//    public ModelFactory getModelFactory(String languageString) {
//        try {
//            ModelFactory fact = models.get(languageString).modelFactory();
//            fact.addToolExtension(ChameleonEditorExtension.class,tools.get(languageString));
//            return fact;
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public Syntax getCodeWriter(String language) {
        try {
            return languages.get(language).getCodeWriter();

        } catch (NullPointerException e){ return null; }
    }

    /**
     * @return the xml document in the directory of the chameleonEditor
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Document getXMLDocument(String filename) throws IOException, ParserConfigurationException, SAXException {
        URL url = ChameleonEditorPlugin.getDefault().getBundle().getEntry(
                "xml/" + filename);
    		String pathStr = FileLocator.toFileURL(url).getPath();
        IPath path = new Path(pathStr);
        path.removeTrailingSeparator();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(path.toOSString());
        return document;
    }

    /**
     * creates a new presentation model and returns it for the given language.
     * The model is unique and is loaded when needed. when the language is not
     * supported, an empty model is loaded.
     */
    public PresentationModel getPresentationModel(String languageString) {
    	PresentationModel r = presentationModels.get(languageString);
    	if (r == null) {
    		EclipseBootstrapper bootstrapper = languages.get(languageString);
    		String filename = "/xml/presentation.xml";
    		InputStream stream = bootstrapper.getClass().getClassLoader().getResourceAsStream(filename);
    		r = new PresentationModel(languageString, stream);
    		presentationModels.put(languageString, r);
    	}
    	return r;
    }

		public List<String> extensions(Language language) {
			String name = language.name();
			EclipseBootstrapper eclipseBootstrapper = languages.get(name);
			return eclipseBootstrapper.fileExtensions();
		}

		//FIXME BUILDER SHOULD BE CONNECTOR!!!
		public Builder createBuilder(Language language, File projectDir) {
			return languages.get(language.name()).createBuilder(language, projectDir);
		}
}
