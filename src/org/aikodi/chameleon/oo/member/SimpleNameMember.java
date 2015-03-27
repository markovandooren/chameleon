package org.aikodi.chameleon.oo.member;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

public abstract class SimpleNameMember extends MemberImpl {

  private String _name;

  @Override
  public String name() {
    return _name;
  }

  private SimpleNameSignature _signature;

  @Override
  public void setName(String name) {
    if (_signature != null) {
      _name = name;
      _signature.setName(name);
    } else {
      String old = _name;
      _name = name;
      if(changeNotificationEnabled()) {
        notify(new NameChanged(old, name));
      }
    }
  }

  @Override
  public void setSignature(Signature signature) {
    if (signature instanceof SimpleNameSignature) {
      setName(signature.name());
    } else {
      throw new ChameleonProgrammerException();
    }
  }

  @Override
  public SimpleNameSignature signature() {
    if (_signature == null) {
      synchronized (this) {
        if (_signature == null) {
          _signature = new SimpleNameSignature(_name) {
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
      return declaration.name().equals(_name) && declaration.signature() instanceof SimpleNameSignature;
    }
  }

}
