package org.aikodi.chameleon.oo.member;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.declaration.Name;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

public abstract class SimpleNameMember extends MemberImpl {

  private String _name;

  @Override
  public String name() {
    return _name;
  }

  private Name _signature;

  @Override
  public void setName(String name) {
    _name = name;
    if (_signature != null) {
      _signature.setName(name);
    }
  }

  @Override
  public void setSignature(Signature signature) {
    if (signature instanceof Name) {
      setName(signature.name());
    } else {
      throw new ChameleonProgrammerException();
    }
  }

  @Override
  public Name signature() {
    if (_signature == null) {
      synchronized (this) {
        if (_signature == null) {
          _signature = new Name(_name) {
            @Override
            public void setName(String name) {
              super.setName(name);
              SimpleNameMember.this._name = name;
            }
          };
        }
        _signature.setUniParent(this);
      }
    }
    return _signature;
  }

  @Override
  public boolean sameSignatureAs(Declaration declaration) throws LookupException {
    if (declaration instanceof SimpleNameMember) {
      return _name.equals(((SimpleNameMember) declaration)._name);
    } else {
      return declaration.name().equals(_name) && declaration.signature() instanceof Name;
    }
  }

}
