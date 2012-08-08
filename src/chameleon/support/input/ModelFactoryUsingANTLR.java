package chameleon.support.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import org.antlr.runtime.RecognitionException;
import org.rejuse.association.Association;
import org.rejuse.io.DirectoryScanner;

import chameleon.core.document.Document;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.namespace.RootNamespace;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.input.InputProcessor;
import chameleon.input.ModelFactory;
import chameleon.input.NoLocationException;
import chameleon.input.ParseException;
import chameleon.input.SourceManager;
import chameleon.plugin.PluginImpl;
import chameleon.util.concurrent.CallableFactory;
import chameleon.util.concurrent.FixedThreadCallableExecutor;
import chameleon.util.concurrent.QueuePollingCallableFactory;
import chameleon.util.concurrent.UnsafeAction;

public abstract class ModelFactoryUsingANTLR extends PluginImpl implements ModelFactory {

  public abstract ModelFactoryUsingANTLR clone();

	private boolean _debug;
	
	public void setDebug(boolean value) {
		_debug = value;
	}
	
	public boolean debug() {
		return _debug;
	}

	
	
	public void parse(String source, Document cu) throws ParseException {
		InputStream inputStream = new StringBufferInputStream(source);
		try {
			parse(inputStream, cu);
		} catch (IOException e) {
			// cannot happen if we work with a String
			throw new ChameleonProgrammerException("IOException while parsing a String.", e);
		}
	}

	public void parse(InputStream inputStream, Document cu) throws IOException, ParseException {
		try {
			ChameleonParser parser = getParser(inputStream);
			cu.disconnectChildren();
			parser.setDocument(cu);
			parser.compilationUnit();
		} catch (RecognitionException e) {
			throw new ParseException(e,cu);
		}
	}
	
	public abstract ChameleonParser getParser(InputStream inputStream) throws IOException;

	/**
	 * @param pathList		The directories to from where to load the cs-files
	 * @param extension     Only files with this extension will be loaded
	 * @param recursive	    Wether or not to also load cs-files from all sub directories
	 * @return A set with all the cs-files in the given path
	 */
	public static Set<File> sourceFiles(List<String> pathList, String extension, boolean recursive) {
	    Set<File> result = new HashSet();
	    for(String path: pathList) {
	      result.addAll(new DirectoryScanner().scan(path, extension, recursive));
	    }
	    return result;
	}

	public void refresh(Element element) throws ParseException {
		Document compilationUnit = element.nearestAncestor(Document.class);
		Language lang = element.language();
		boolean done = false;
		while((element != null) && (! done)){
			try {
		    SourceManager manager = language().plugin(SourceManager.class);
		    String text = manager.text(element);
		    Element newElement = parse(element, text);
		    if(newElement != null) {
		      // Use raw type here, we can't really type check this.
		      Association childLink = element.parentLink().getOtherRelation();
		      childLink.replace(element.parentLink(), newElement.parentLink());
		      clearPositions(element,lang);
		      done = true;
		      break;
		    }
			} catch(ParseException exc) {
			} catch (NoLocationException e) {
			}
			if(! done) {
				Element old = element;
				element = element.parent();
				if(element == null) {
					throw new ParseException(old.nearestAncestor(Document.class));
				}
			}
		}
	}
	
	/**
	 * Remove all positional metadata from the given element using the
	 * input processors of the given language. 
	 * @param element
	 * @param lang
	 */
	protected void clearPositions(Element element, Language lang) {
   	for(InputProcessor processor: lang.processors(InputProcessor.class)) {
   		processor.removeLocations(element);
   	}
	}

	protected abstract <P extends Element> Element parse(Element element, String text) throws ParseException;


}
