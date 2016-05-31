package org.aikodi.chameleon.plugin.build;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.plugin.ViewPluginImpl;
import org.aikodi.chameleon.plugin.build.BuildException;
import org.aikodi.chameleon.plugin.build.BuildProgressHelper;
import org.aikodi.chameleon.plugin.build.Builder;
import org.aikodi.chameleon.plugin.build.DocumentWriter;
import org.aikodi.chameleon.plugin.output.Syntax;
import org.aikodi.chameleon.support.translate.IncrementalTranslator;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.View;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class LanguageBuilder<S extends Language, T extends Language> extends ViewPluginImpl implements Builder {

    private DocumentWriterFactory dwf;
    protected IncrementalTranslator<S, T> translator;


    protected Syntax syntax = null;

    public LanguageBuilder(View view, DocumentWriterFactory dwf) {
        setContainer(view, Builder.class);
        this.dwf = dwf;
    }

    protected LanguageBuilder() {
    }

    @Override
    public LanguageBuilder<S, T> clone() {
        return new LanguageBuilder<>();
    }

    @Override
    public void build(List<Document> compilationUnits, File outputDir, BuildProgressHelper buildProgressHelper) throws BuildException {
        for (Document cu : compilationUnits) {
            if (buildProgressHelper != null) {
                buildProgressHelper.checkForCancellation();
            }

            build(cu, outputDir, buildProgressHelper);

            if (buildProgressHelper != null) {
                buildProgressHelper.addWorked(1);
            }
        }
    }

    @Override
    public int totalAmountOfWork(List<Document> documents, List<Document> allDocuments) {
        return documents.size();
    }

    public View target() {
        return translator.target();
    }

    public void buildAll(File outputDir, BuildProgressHelper buildProgressHelper) throws BuildException {
        try {
            build(view().sourceDocuments(), outputDir, buildProgressHelper);
        } catch (InputException e) {
            throw new BuildException(e);
        }
    }

    public void build(Document compilationUnit, File outputDir) throws BuildException {
        build(compilationUnit, outputDir, null);
    }

    public void build(Document compilationUnit, File outputDir, BuildProgressHelper buildProgressHelper) throws BuildException {
        try {
            DocumentWriter writer = dwf.writer();
            String fileName = writer.fileName(compilationUnit);
            System.out.println("Building " + fileName);
            Collection<Document> compilationUnits = translator.build(compilationUnit, buildProgressHelper);
            for (Document translated : compilationUnits) {
                translated.flushCache();
                writer.write(translated, outputDir);
            }
        } catch (Error | RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (ModelException | IOException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Set a syntax to view the intermediate steps for debugging purposes
     */
    public void setSyntax(Syntax syntax) {
        this.syntax = syntax;
    }
}
