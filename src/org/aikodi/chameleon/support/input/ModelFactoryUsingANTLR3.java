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

public abstract class ModelFactoryUsingANTLR3 extends LanguagePluginImpl implements ModelFactory {

	public ModelFactoryUsingANTLR3() {
	}
	
  @Override
public abstract ModelFactoryUsingANTLR3 clone();

	private boolean _debug;
	
	public void setDebug(boolean value) {
		_debug = value;
	}
	
	public boolean debug() {
		return _debug;
	}

	
	
	@Override
   public void parse(InputStream inputStream, Document cu) throws IOException, ParseException {
		try {
			ChameleonANTLR3Parser<?> parser = getParser(inputStream, cu.view());
//			cu.disconnectChildren();
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
	
	protected abstract ChameleonANTLR3Parser<?> getParser(InputStream inputStream, View view) throws IOException;

}
