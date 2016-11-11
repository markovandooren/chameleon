package org.aikodi.chameleon.core.element;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.aikodi.chameleon.util.association.Multi;
import org.junit.Test;

public class TestElement {

	private static class StubElement extends ElementImpl {
		@Override
		protected Element cloneSelf() {
			return new StubElement();
		}
	}
	
	private static class Container extends ElementImpl {

		private Multi<Element> _children = new Multi<>(this);
		
		@Override
		protected Element cloneSelf() {
			return new Container();
		}
		
		public void add(Element child) {
			add(_children, child);
		}
		
	}
	
	@Test
	public void testBlankElement() {
		Element e = new StubElement();
		assertNull(e.parent());
		assertEmpty(e.ancestors());
		assertEmpty(e.children());
		assertEmpty(e.descendants());
		assertNull(e.view());
		assertNull(e.language());
	}
	
	@Test
	public void testBlankClone() {
		StubElement e = new StubElement();
		StubElement clone = e.clone(e);
		assertTrue(clone != null);
		assertTrue(clone.getClass() == e.getClass());
	}
	
	@Test
	public void testCloneWithChildren() {
		StubElement e = new StubElement();
		Container c = new Container();
		c.add(e);
		Container clone = c.clone(c);
		assertTrue(c.children().size() == 1);
		assertTrue(c.children().contains(e));
		assertTrue(clone.children().size() == 1);
		assertTrue(! clone.children().contains(e));
		assertTrue(clone.children().get(0).getClass().equals(e.getClass()));
	}
	
	@Test
	public void testChildrenAndParent() {
		StubElement e = new StubElement();
		Container c = new Container();
		c.add(e);
		assertTrue(c.children().size() == 1);
		assertTrue(c.children().contains(e));
		assertTrue(e.parent().equals(c));
	}
	
	public void testUniParent() {
		
	}
	
	@Test
	public void testChildrenDisconnect() {
		StubElement e = new StubElement();
		Container c = new Container();
		c.add(e);
		e.disconnect();
		assertEmpty(c.children());
		assertNull(e.parent());
	}
	
	public void testLanguage() {
		StubElement e = new StubElement();
		// test language
		// test language of particular kind
	}
	
	public void testMetadata() {
		
	}
	
	private void assertEmpty(Collection<?> collection) {
		assertTrue(collection != null);
		assertTrue(collection.size() == 0);
	}
	
}
