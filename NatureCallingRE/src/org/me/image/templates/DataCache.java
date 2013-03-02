/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.image.templates;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Jastrzab
 */
public final class DataCache<Type> {

	private final ConcurrentHashMap<String, Type> mCache = new ConcurrentHashMap<String, Type>();
	private final ReadWriteLock mLock = new ReentrantReadWriteLock();

	public Type get(String object) {
		mLock.readLock().lock();
		try {
			return mCache.get(object);
		} finally {
			mLock.readLock().unlock();
		}
	}

	public void put(String key, Type value) {
		if (value != null) {
			mLock.writeLock().lock();
			try {
				mCache.put(key, value);
			} finally {
				mLock.writeLock().unlock();
			}
		}
	}

	public boolean containsKey(String key) {
		mLock.readLock().lock();
		try {
			return mCache.containsKey(key);
		} finally {
			mLock.readLock().unlock();
		}
	}

	public void clear() {
		mLock.writeLock().lock();
		try {
			mCache.clear();
		} finally {
			mLock.writeLock().unlock();
		}
	}

}