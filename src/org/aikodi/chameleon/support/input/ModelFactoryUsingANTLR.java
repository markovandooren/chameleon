package org.aikodi.chameleon.support.input;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.input.ModelFactory;
import org.aikodi.chameleon.input.NoLocationException;
import org.aikodi.chameleon.input.ParseException;
import org.aikodi.chameleon.input.SourceManager;
import org.aikodi.chameleon.plugin.LanguagePluginImpl;
import org.aikodi.chameleon.workspace.View;
import org.antlr.runtime.RecognitionException;

import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.io.DirectoryScanner;

public abstract class ModelFactoryUsingANTLR extends LanguagePluginImpl implements ModelFactory {

	public ModelFactoryUsingANTLR() {
	}
	
  @Override
public abstract ModelFactoryUsingANTLR clone();

	private boolean _debug;
	
	public void setDebug(boolean value) {
		_debug = value;
	}
	
	public boolean debug() {
		return _debug;
	}

	
	
	@Override
   public void parse(String source, Document cu) throws ParseException {
		InputStream inputStream = new StringBufferInputStream(source);
		try {
			parse(inputStream, cu);
		} catch (IOException e) {
			// cannot happen if we work with a String
			throw new ChameleonProgrammerException("IOException while parsing a String.", e);
		}
	}

	@Override
   public void parse(InputStream inputStream, Document cu) throws IOException, ParseException {
		try {
			ChameleonANTLR3Parser<?> parser = getParser(inputStream, cu.view());
			cu.disconnectChildren();
			//FIXME: this is crap. I set the document, and later on set the view, 
			// while they are (and must be) connected anyway
			parser.setDocument(cu);
			parser.compilationUnit();
		} catch (RecognitionException e) {
			throw new ParseException(e,cu);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	protected abstract ChameleonANTLR3Parser getParser(InputStream inputStream, View view) throws IOException;

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

	@Override
   public void refresh(Element element) throws ParseException {
		Document compilationUnit = element.nearestAncestor(Document.class);
		View view = element.view();
		boolean done = false;
		if(view != null) {
			SourceManager manager = view.plugin(SourceManager.class);
			while((element != null) && (! done)){
				try {
					String text = manager.text(element);
					Element newElement = parse(element, text);
					if(newElement != null) {
						// Use raw type here, we can't really type check this.
						Association childLink = element.parentLink().getOtherRelation();
						childLink.replace(element.parentLink(), newElement.parentLink());
//						clearPositions(element,view);
						done = true;
						break;
					}
				} catch(ParseException exc) {
				} catch (NoLocationException e) {
				}
				Element old = element;
				if(! done) {
					element = element.parent();
					if(element == null) {
						throw new ParseException(old.nearestAncestor(Document.class));
					}
				}
				old.disconnect();
			}
		}
	}
	
//	/**
//	 * Remove all positional metadata from the given element using the
//	 * input processors of the given language. 
//	 * @param element
//	 * @param lang
//	 */
//	protected void clearPositions(Element element, View view) {
//   	for(InputProcessor processor: view.processors(InputProcessor.class)) {
//   		processor.removeLocations(element);
//   	}
//	}

	protected abstract <P extends Element> Element parse(Element element, String text) throws ParseException;


}
