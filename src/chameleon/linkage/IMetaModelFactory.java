package chameleon.linkage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.method.Method;
import chameleon.core.namespace.Namespace;
import chameleon.input.ParseException;
import chameleon.tool.ToolExtension;

/**
 * @author Jef Geerinckx, Joeri Hendrickx
 *
 * Interface for a MetaModelFactory, a class creating metamodels based
 * on Chameleon
 */
public interface IMetaModelFactory {

    /**
     * Create a metamodel from the sources supplied by a ISourceSupplier class
     */
    public Namespace getMetaModel(ISourceSupplier supplier) throws Exception;

    /**
     * Create a metamodel from a set of files. The supplied ILinkageFactory
     * will be used to create ILinkage objects for each file.
     */
    public Namespace getMetaModel(ILinkageFactory fact, Set<File> files) throws Exception;

    public void parseTypeMembers(ILinkage linkage, String code, int line, int column, Element typeMember) throws Exception;

    public void parseNameSpaceMembers(ILinkage linkage, String code, int line, int column, Element nameSpaceMember) throws Exception;

    /**
     * Add a source to an existing MetModel.
     * @param linkage
     * 	The Linkage object for this source
     * @param source
     * 	The source itself
     * @param DefaultPackage
     * 	The root element of the MetaModel
     */
    public void addSource(ILinkage linkage, String source, Element DefaultPackage) throws MalformedURLException, ParseException, IOException, MetamodelException;

    public void replaceMethod(ILinkage linkage, String methodCode, Method method)throws IOException, ParseException, MetamodelException;

    void addToolExtension(Class<? extends ToolExtension> extClass, ToolExtension ext);

    void removeToolExtension(Class<? extends ToolExtension> extClass);

}
