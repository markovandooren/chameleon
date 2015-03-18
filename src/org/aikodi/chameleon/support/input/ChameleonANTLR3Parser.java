package org.aikodi.chameleon.support.input;

import java.util.List;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespace.RootNamespace;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.input.InputProcessor;
import static org.aikodi.chameleon.input.PositionMetadata.*;
import org.aikodi.chameleon.workspace.View;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;

public abstract class ChameleonANTLR3Parser<L extends Language> extends Parser {
	
	 public ChameleonANTLR3Parser(TokenStream input, RecognizerSharedState state) {
		super(input,state);
	}

		public Object compilationUnit() throws RecognitionException {
			throw new ChameleonProgrammerException();
		}

	   public void check_null(Object o) {
	     if(o == null) {
	       throw new RuntimeException("Object returned by parsing rule is null.");
	     }
	   }
	   
	   public void check_stack(Object s) {
	    if(s == null) {
	      throw new RuntimeException("The stack element is null.");
	    }
	   }

	   public void setLocation(Element element, Token start, Token stop) {
	  	 if(element != null) {
	  		 CommonToken begin = (CommonToken)start;
	  		 CommonToken end = (CommonToken)stop;
	  		 if(begin != null && end != null) {
	  			 int offset = begin.getStartIndex();
	  			 int length = end.getStopIndex() - offset;
	  			 for(InputProcessor processor: inputProcessors()) {
	  				 setLocation(element, offset, length, processor);
	  			 }
	  		 }
	  	 }
	   }

	   /**
	    * FIXME it seems like this should be done by a separate object that
	    * maps source elements to meta data tags.
	    * 
	    * @param element
	    * @param offset
	    * @param length
	    * @param processor
	    */
		private void setLocation(Element element, int offset, int length, InputProcessor processor) {
			Document document = getDocument();
			processor.setLocation(element, offset, length, document,ALL);
			if(element instanceof CrossReference && (! element.hasMetadata(CROSSREFERENCE))) {
				processor.setLocation(element, offset, length, document, CROSSREFERENCE);
			}
			if(element instanceof Modifier && (! element.hasMetadata(MODIFIER))) {
				processor.setLocation(element, offset, length, document, MODIFIER);
			}
		}
	   
		public List<InputProcessor> inputProcessors() {
			View view = view();
			if(view != null) {
				return view.processors(InputProcessor.class);
			} else {
				throw new IllegalStateException();
			}
		}

	   public void setLocation(Element element, Token start, Token stop, String tagType) {
	     List<InputProcessor> processors = inputProcessors();
	     if(processors.size() > 0) {
	    	 CommonToken begin = (CommonToken)start;
	    	 CommonToken end = (CommonToken)stop;
	    	 if(begin != null && end != null) {
	    		 int offset = offset(begin);
	    		 int length = length(begin,end);
	    		 for(InputProcessor processor: processors) {
	    			 //processor.setLocation(element, new Position2D(begin.getLine(), begin.getCharPositionInLine()), new Position2D(end.getLine(), end.getCharPositionInLine()));
	    			 processor.setLocation(element, offset, length, getDocument(), tagType);
	    		 }
	    	 }
	     }
	   }
	   
	   public int offset(CommonToken token) {
	  	 return token.getStartIndex();
	   }
	   
	   public int length(CommonToken start, CommonToken stop) {
	  	 return stop.getStopIndex() - offset(start);
	   }
	   
	   /**
	    * Add locations to the given element
	    * @param element
	    * @param first
	    * @param second
	    */
	   public void setLocation(Element element, ParserRuleReturnScope first, ParserRuleReturnScope second) {
	     Token end = first.stop;
	     if(second != null) {
	       end = second.stop;
	     }
	     setLocation(element, first.start, end);
	   }
	   
	   
	   public void setLocation(Element element, Token token, String tagType) {
	     if(token != null) {
	       setLocation(element, token, token, tagType);
	     }
	   }
	   
	   public void setName(Element element, Token token) {
	  	 if(token != null) {
	  	   setLocation(element, token ,NAME);
	  	 }
	   }
	   
	   public void setName(Element element, Token start, Token stop) {
	  	 if(start != null && stop != null) {
	  	   setLocation(element, start , stop, NAME);
	  	 }
	   }
	   	   
	   public void setKeyword(Element element, Token token) {
	     if(token != null) {
	       setLocation(element, token, token, KEYWORD);
	     }
	   }
	   
	   public void setCrossReference(Element element, Token start, Token stop) {
	     if(start != null && stop != null) {
	       setLocation(element, start, stop, CROSSREFERENCE);
	     }
	   }
	   public void setAllLocation(Element element, Token token) {
	     if(token != null) {
	       setLocation(element, token, token, ALL);
	     }
	   }
	   
	   Language _lang;
	   
	   public Language language() {
	     return _lang;
	   }
	   
	   public void setView(View view) {
	  	 _view = view;
	     _lang = view.language();
	     _root = view.namespace();
	   }
	   
	   protected View _view;
	   
	   public View view() {
	  	 return _view;
	   }
	   
	   RootNamespace _root;

	   public Namespace getDefaultNamespace() {
	     return _root;
	   }

	   Document _document = new Document();
	   
	   public Document getDocument() {
	     return _document;
	   }
	   
	   public void setDocument(Document document) {
	    if(document == null) {
	      throw new IllegalArgumentException("The compilation unit cannot be null.");
	    }
	     _document = document;
	   }
	   
	   @Override
	   public void displayRecognitionError(String[] tokenNames, RecognitionException exc) {
	   	 CommonToken token = (CommonToken) exc.token;
	   	 int offset = offset(token);
	   	 int length = length(token,token);
	   	 // Message construction copied from the super method. It is not available as a separate inspector.
	   	 String message = getErrorMessage(exc, tokenNames);
       for(InputProcessor processor: inputProcessors()) {
      	 processor.markParseError(offset, length, message, getDocument());
       }
	   }
}
