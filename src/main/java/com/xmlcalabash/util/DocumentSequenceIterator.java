package com.xmlcalabash.util;

import net.sf.saxon.expr.LastPositionFinder;
import net.sf.saxon.om.FocusIterator;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;

/**
 * Created by IntelliJ IDEA.
 * User: ndw
 * Date: Oct 20, 2008
 * Time: 4:35:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentSequenceIterator<T extends Item<?>> implements FocusIterator<T>, LastPositionFinder {
    int position = 0;
    int last = 0;
    T item = null;

    public void setPosition(int position) {
        this.position = position;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public T next() throws XPathException {
        throw new UnsupportedOperationException("Don't know what to do for next() on DocumentSequenceIterator");
    }

    public T current() {
        return item;
    }

    public int position() {
        return position;
    }

    public void close() {
        // ???
    }

    public FocusIterator getAnother() throws XPathException {
        throw new UnsupportedOperationException("Don't know what to do for getAnother() on DocumentSequenceIterator");
    }

    public int getProperties() {
        return SequenceIterator.LAST_POSITION_FINDER;
    }

    @Override
    public int getLength() throws XPathException {
        return last;
    }
}
