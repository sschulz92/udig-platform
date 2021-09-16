/**
 * uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2004, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.core;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A set backed onto a WeakHashMap.  Will not allow nulls
 * 
 * @author Jesse
 * @since 1.1.0
 */
public class WeakHashSet<T> extends AbstractSet<T> implements Set<T> {
    WeakHashMap<T, T> set=new WeakHashMap<T,T>();
    
    public int size() {
        return set.keySet().size();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public boolean contains( Object o ) {
        return set.containsKey(o);
    }

    public Iterator<T> iterator() {
        return set.keySet().iterator();
    }

    public Object[] toArray() {
        return set.keySet().toArray();
    }

    public <M> M[] toArray( M[] a ) {
        return set.keySet().toArray(a);
    }

    public boolean add( T o ) {
        if( o==null )
            throw new NullPointerException("Argument is null"); //$NON-NLS-1$
        if( set.containsKey(o) )
            return false;
        set.put(o, o);
        return true;
    }

    public boolean remove( Object o ) {
        return set.remove(o)!=null;
    }

    public boolean containsAll( Collection< ? > c ) {
        return set.keySet().containsAll(c);
    }

    public boolean addAll( Collection< ? extends T> c ) {
        boolean added=false;
        for( T t : c ) {
            if( add(t) )
                added=true;
        }
        return added;
    }

    public boolean removeAll( Collection< ? > c ) {
        boolean removed=false;
        for( Object object : c ) {
                if( remove(object) )
                    removed=true;
        }
        return removed;
    }

    public void clear() {
        set.clear();
    }

}
