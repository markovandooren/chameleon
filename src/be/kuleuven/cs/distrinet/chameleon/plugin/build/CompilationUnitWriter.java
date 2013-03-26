package be.kuleuven.cs.distrinet.chameleon.plugin.build;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePluginImpl;
import be.kuleuven.cs.distrinet.chameleon.plugin.output.Syntax;

/**
 * A default class for writing the contents of a Document to a file.
 * @author Marko van Dooren
 */
public abstract class CompilationUnitWriter extends LanguagePluginImpl {

	String _extension;

	public String extension() {
		return _extension;
	}
	
	public CompilationUnitWriter(String extension) {
		_extension = extension;
	}

	public String outputDirName(File file) {
		return file.getAbsolutePath();
	}
	
	/**
	 * Write the given document to a file. The Syntax plugin is used to translate the document
	 * to a string. The fully qualified name of the namespace declaration is used to determine
	 * the directory of the output file.
	 * 
	 * @param doc
	 * @return
	 * @throws LookupException
	 * @throws ModelException
	 * @throws IOException
	 */
	public File write(Document doc, File outputDir) throws LookupException, ModelException, IOException {
		Syntax writer = doc.language().plugin(Syntax.class);
		if(writer != null) {
			String fileName = fileName(doc);
			if(fileName != null) {
				String packageFQN = packageFQN(doc);
				String relDirName = packageFQN.replace('.', File.separatorChar);
				File out = new File(outputDirName(outputDir)+File.separatorChar + relDirName + File.separatorChar + fileName);
				File parent = out.getParentFile();
				parent.mkdirs();
				out.createNewFile();
				FileWriter fw = new FileWriter(out);
				fw.write(toString(doc, writer));
				fw.close();
				System.out.println("Wrote: "+out.getAbsolutePath());
				return out;
			}
		} 
		return null;
	}

	protected String toString(Document cu, Syntax writer) throws ModelException {
		return writer.toCode(cu);
	}
	
	public abstract String fileName(Document compilationUnit) throws LookupException, ModelException;

	public abstract String packageFQN(Document compilationUnit) throws LookupException, ModelException;
	
}
