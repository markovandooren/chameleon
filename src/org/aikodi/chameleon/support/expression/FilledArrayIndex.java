package org.aikodi.chameleon.support.expression;

import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.util.association.Multi;

import java.util.List;

/**
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public class FilledArrayIndex extends ArrayIndex {

	public FilledArrayIndex(){

	}

	public FilledArrayIndex(Expression expr){
		addIndex(expr);
	}

	private Multi<Expression> _expressions = new Multi<Expression>(this);

	public void addIndex(Expression expr){
		add(_expressions,expr);
	}

	public void removeIndex(Expression expr){
		remove(_expressions,expr);
	}
	
	public List<Expression> getIndices() {
        return _expressions.getOtherEnds();
	}
	
	@Override protected FilledArrayIndex cloneSelf() {
        return new FilledArrayIndex();

	}

	@Override
	public Verification verifySelf() {
		//FIXME: create CLikeLanguage with intType()?
		return Valid.create();
	}

}
