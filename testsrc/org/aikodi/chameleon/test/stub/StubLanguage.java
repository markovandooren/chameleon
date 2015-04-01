package org.aikodi.chameleon.test.stub;

import org.aikodi.chameleon.core.language.LanguageImpl;

import be.kuleuven.cs.distrinet.rejuse.junit.BasicRevision;


public class StubLanguage extends LanguageImpl {

  public StubLanguage() {
    super("Chameleon testing stub language", new BasicRevision(1,0,0));
    
    
  }
  
}
