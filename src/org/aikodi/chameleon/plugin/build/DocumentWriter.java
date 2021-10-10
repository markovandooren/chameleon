package org.aikodi.chameleon.plugin.build;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.plugin.LanguagePluginImpl;
import org.aikodi.chameleon.plugin.output.Syntax;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class for writing the contents of a Document to a file.
 * 
 * @author Marko van Dooren
 */
public abstract class DocumentWriter extends LanguagePluginImpl {

  /**
   * The file extension to be used for the output files.
   * 
   * FIXME Get rid of this. A writer may writer files with different
   * extensions based on the contents of a document.
   */
	private String _extension;

	/**
	 * Create a new document writer that writes files with the
	 * given extension.
	 * 
	 * @param extension The extension of the output files.
	 */
  public DocumentWriter(String extension) {
    _extension = extension;
  }

	/**
	 * @return The file extension of the output files.
	 */
	public String extension() {
		return _extension;
	}
	
	/**
   * Write the given document to a file. The contents of the output file is
   * determined by {@link #toString(Document)}. The name of the output file
   * is determined by {@link #fileName(Document)}, and the output directory
   * is determined by {@link #directoryName(Document)}.
   * 
   * @param document The document to be written to a file.
   * 
   * @return The file to which the document was written.
   * 
   * @throws ModelException An exception occurred while converting the document
   * to a string.
   * @throws IOException An exception occurred while writing the output file.
   */
	public File write(Document document, File outputDir) throws ModelException, IOException {
	  String fileName = fileName(document);
	  if(fileName != null) {
	    String directoryName = directoryName(document);
	    String relDirName = directoryName.replace('.', File.separatorChar);
	    File out = new File(outputDir.getAbsolutePath()+File.separatorChar + relDirName + File.separatorChar + fileName);
	    File parent = out.getParentFile();
	    boolean unused = parent.mkdirs();
	    boolean ignored = out.createNewFile();
	    FileWriter fw = new FileWriter(out);
	    fw.write(toString(document));
	    fw.close();
	    System.out.println("Wrote: "+out.getAbsolutePath());
	    return out;
	  }
	  return null;
	}

	/**
   * Convert the given document to a string. By default, the {@link Syntax}
   * plugin is used to translate the document to a string, but subclasses
   * can choose to do this in a different manner.
   * 
   * @param document The document to be converted to a string. The document
   * cannot be null.
   * 
   * @return A string that represents the given document.
   * 
   * @throws ModelException An exception occurred while converting the
   * given document to a string.
   */
	protected String toString(Document document) throws ModelException {
    Syntax writer = document.language().plugin(Syntax.class);
		return writer.toCode(document);
	}
	
	/**
	 * Calculate the file name that must be used for the given document. This
	 * does not include the file name extension.
	 * 
	 * @param document The document for which the output file name is requested.
	 * 
	 * @return The name of the output file. The name cannot be null.
	 * 
	 * @throws ModelException An exception occurred while determining the name
	 * of the output file.
	 */
	public abstract String fileName(Document document) throws ModelException;

	/**
	 * Return the fully qualified name of the namespace of the given document.
	 * @param document
	 * @return
	 * @throws LookupException
	 * @throws ModelException
	 */
	public abstract String directoryName(Document document) throws LookupException, ModelException;
	
}
