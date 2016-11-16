package org.aikodi.chameleon.test.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.aikodi.chameleon.builder.ProjectBuilder;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.Change;
import org.aikodi.chameleon.core.event.association.ChildAdded;
import org.aikodi.chameleon.core.event.association.ChildRemoved;
import org.aikodi.chameleon.core.event.association.ParentAdded;
import org.aikodi.chameleon.core.event.association.ParentRemoved;
import org.aikodi.chameleon.core.event.association.Removed;
import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.factory.Factory;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.test.stub.StubLanguage;
import org.aikodi.chameleon.test.stub.builder.StubDocumentBuilder;
import org.aikodi.chameleon.test.stub.declaration.StubDeclaration;
import org.aikodi.chameleon.workspace.ProjectException;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.rejuse.association.Association;
import org.junit.Before;
import org.junit.Test;

/**
 * A class to test the event mechanism.
 * 
 * @author Marko van Dooren
 */
public class TestEvents {

  @Before
  public void createProject() throws ProjectException {
    _language = new StubLanguage();
    _language.setPlugin(Factory.class, new Factory());
    ProjectBuilder projectBuilder = new ProjectBuilder("test");
    projectBuilder.withLanguage(_language, v -> {_view = v;}).project();
    _declarations = new ArrayList<>();
    Consumer<Declaration> f = d -> _declarations.add(d);
    _document = new StubDocumentBuilder<ProjectBuilder>(_view, projectBuilder, d -> {})
       .builder()
       .declaration("a",f)
       .container("b",f)
         .declaration("a",f)
         .declaration("b",f)
       .done().done().document();
  }

  @Test
  public void testAdd() {
    Declaration a = new StubDeclaration("q");
    
    EventChecker<Change, Element> documentAnyChecker = new EventChecker<Change, Element>();
    _document.when().any().call(documentAnyChecker);
    EventChecker<Change, Element> aChecker = new EventChecker<Change, Element>();
    a.when().self().call(aChecker);
    
    
    EventChecker<ChildAdded,Element> documentChildAdded = new EventChecker<>();
    _document.when().any().about(ChildAdded.class).call(documentChildAdded);
    EventChecker<ChildRemoved,Element> documentRemovedAdded = new EventChecker<>();
    _document.when().any().about(ChildRemoved.class).call(documentRemovedAdded);
    aChecker.expect(ParentAdded.class, a);
    documentAnyChecker.expect(ChildAdded.class,namespaceDeclaration());
    documentChildAdded.expect(ChildAdded.class,namespaceDeclaration());
    namespaceDeclaration().add(a);
    documentAnyChecker.check();
    aChecker.check();
    documentChildAdded.check();
    documentRemovedAdded.check();
  }
  
  @Test
  public void testAddAndRename() {
    Declaration a = new StubDeclaration("q");
    
    EventChecker<Change, Element> documentAnyChecker = new EventChecker<Change, Element>();
    _document.when().any().call(documentAnyChecker);
    EventChecker<Change, Element> aChecker = new EventChecker<Change, Element>();
    a.when().self().call(aChecker);
    
    
    EventChecker<ChildAdded,Element> documentChildAdded = new EventChecker<>();
    _document.when().any().about(ChildAdded.class).call(documentChildAdded);
    EventChecker<ChildRemoved,Element> documentRemovedAdded = new EventChecker<>();
    _document.when().any().about(ChildRemoved.class).call(documentRemovedAdded);
    aChecker.expect(ParentAdded.class, a);
    documentAnyChecker.expect(ChildAdded.class,namespaceDeclaration());
    documentChildAdded.expect(ChildAdded.class,namespaceDeclaration());
    namespaceDeclaration().add(a);
    documentAnyChecker.check();
    aChecker.check();
    documentChildAdded.check();
    documentRemovedAdded.check();
    
    // Check if the events of the newly added element are also propagated to
    // the document
    documentAnyChecker.expect(NameChanged.class, a.signature());
    // The declaration itself hasn't changed, only its signature
    EventChecker<NameChanged,Element> signatureChecker = new EventChecker<>();
    a.signature().when().self().about(NameChanged.class).call(signatureChecker);
    signatureChecker.expect(NameChanged.class, a.signature());
    a.setName("f");
    documentAnyChecker.check();
    aChecker.check();
    signatureChecker.check();
  }
  
  @Test
  public void testRemove() {
    Declaration a = _declarations.get(0);
    EventChecker<Change, Element> documentAnyChecker = new EventChecker<Change, Element>();
    _document.when().any().call(documentAnyChecker);
    EventChecker<Change, Element> aChecker = new EventChecker<Change, Element>();
    a.when().self().call(aChecker);
    
    documentAnyChecker.expect(Removed.class, namespaceDeclaration());
    aChecker.expect(ParentRemoved.class,a);
    // The signature will be disconnected as well.
    aChecker.expect(ChildRemoved.class,a);
    a.disconnect();
    documentAnyChecker.check();
    aChecker.check();
  }
  
  @Test
  public void testRemoveAndRename() {
    Declaration a = _declarations.get(0);
    EventChecker<Change, Element> documentAnyChecker = new EventChecker<Change, Element>();
    _document.when().any().call(documentAnyChecker);
    EventChecker<Change, Element> aChecker = new EventChecker<Change, Element>();
    a.when().self().call(aChecker);
    
    documentAnyChecker.expect(Removed.class, namespaceDeclaration());
    // The signature will not be disconnected, so we expect only the parent removed event.
    aChecker.expect(ParentRemoved.class,a);
    a.parentLink().connectTo(null);
    documentAnyChecker.check();
    aChecker.check();
    
    
    EventChecker<NameChanged,Element> signatureChecker = new EventChecker<>();
    a.signature().when().self().about(NameChanged.class).call(signatureChecker);
    signatureChecker.expect(NameChanged.class, a.signature());
    a.setName("x");
    //No event should have been sent to the document.
    documentAnyChecker.check();
    signatureChecker.check();
  }
  
  @Test
  public void testChangeDeclarationName() {
    Declaration a = _declarations.get(0);
    EventChecker<Change, Element> documentAnyChecker = new EventChecker<Change, Element>();
    _document.when().any().call(documentAnyChecker);
    EventChecker<Change, Element> aChecker = new EventChecker<Change, Element>();
    a.when().self().call(aChecker);
    
    
    documentAnyChecker.expect(NameChanged.class, a.signature());
    a.setName("x");
    documentAnyChecker.check();
    aChecker.check();

  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  public void testReplace() {
    Declaration a = new StubDeclaration("q");
    Declaration declaration = _declarations.get(0);
    EventChecker<Change, Element> declarationChecker = new EventChecker<Change, Element>();
    declaration.when().self().about(ParentRemoved.class).call(declarationChecker);
    EventChecker<Change, Element> aChecker = new EventChecker<Change, Element>();
    a.when().self().about(ParentAdded.class).call(aChecker);
    
    declarationChecker.expect(ParentRemoved.class, declaration);
    aChecker.expect(ParentAdded.class, a);
    declaration.parentLink().getOtherRelation().replace((Association)declaration.parentLink(), (Association)a.parentLink());
    aChecker.check();
    declarationChecker.check();
    
  }
  
//  @Test
//  public void testNamespace() {
//    Declaration declaration = _declarations.get(0);
//    declaration.namespace().when().descendant().call(e -> {
//      System.out.println(e.change().getClass());
//    });
//    declaration.setName("Bla");
//  }

  protected NamespaceDeclaration namespaceDeclaration() {
    return _document.namespaceDeclaration(0);
  }
  
  private View _view;
  
  private Language _language;

  private List<Declaration> _declarations;

  private Document _document;
}
