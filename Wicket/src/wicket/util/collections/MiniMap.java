/*
 * $Id: MiniMap.java 6474 2006-07-17 15:07:53Z eelco12 $ $Revision: 6474 $
 * $Date: 2006-07-17 17:07:53 +0200 (Mon, 17 Jul 2006) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.util.collections;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A fixed size map implementation. Holds an array of keys and array of values
 * which correspond by index. Null key entries are available for use. This means
 * that null is not a valid key.
 * 
 * @author Jonathan Locke
 * @param <K>
 * @param <V>
 */
public final class MiniMap<K, V> implements Map<K, V>, Serializable
{
	private static final long serialVersionUID = 1L;

	/** The array of keys. Keys that are null are not used. */
	private final K[] keys;

	/** The array of values which correspond by index with the keys array. */
	private final V[] values;

	/** The number of valid entries */
	private int size;

	/** The last search index. This makes putting and getting more efficient. */
	private int lastSearchIndex;

	/**
	 * Constructor
	 * 
	 * @param maxEntries
	 *            The maximum number of entries this map can hold
	 */
	@SuppressWarnings("unchecked")
	public MiniMap(final int maxEntries)
	{
		this.keys = (K[])new Object[maxEntries];
		this.values = (V[])new Object[maxEntries];
	}

	/**
	 * Constructor
	 * 
	 * @param map
	 *            The map
	 * @param maxEntries
	 *            The maximum number of entries this map can hold
	 */
	public MiniMap(final Map map, final int maxEntries)
	{
		this(maxEntries);
		putAll(map);
	}

	/**
	 * @return True if this MicroMap is full
	 */
	public boolean isFull()
	{
		return size == keys.length;
	}

	/**
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return size;
	}

	/**
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		return size == 0;
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(final Object key)
	{
		return findKey(0, key) != -1;
	}

	/**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(final Object value)
	{
		return findValue(0, value) != -1;
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public V get(final Object key)
	{
		// Search for key
		final int index = findKey(key);

		if (index != -1)
		{
			// Return value
			return values[index];
		}

		// Failed to find key
		return null;
	}


	public V put(final K key, final V value)
	{
		// Search for key
		final int index = findKey(key);

		if (index != -1)
		{
			// Replace existing value
			final V oldValue = values[index];
			this.values[index] = value;
			return oldValue;
		}

		// Is there room for a new entry?
		if (size < keys.length)
		{
			// Store at first null index and continue searching after null index
			// next time
			final int nullIndex = nextNullKey(lastSearchIndex);
			lastSearchIndex = nextIndex(nullIndex);
			keys[nullIndex] = key;
			values[nullIndex] = value;
			size++;

			return null;
		}
		else
		{
			throw new IllegalStateException("Map full");
		}
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public V remove(final Object key)
	{
		// Search for key
		final int index = findKey(key);

		if (index != -1)
		{
			// Store value
			final V oldValue = values[index];

			keys[index] = null;
			values[index] = null;
			size--;

			return oldValue;
		}

		return null;
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void putAll(final Map map)
	{
		for (final Iterator<Entry<K, V>> iterator = map.entrySet().iterator(); iterator.hasNext();)
		{
			final Map.Entry<K, V> e = iterator.next();
			put(e.getKey(), e.getValue());
		}
	}

	/**
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		for (int i = 0; i < keys.length; i++)
		{
			keys[i] = null;
			values[i] = null;
		}

		size = 0;
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	public Set<K> keySet()
	{
		return new AbstractSet<K>()
		{
			@Override
			public Iterator<K> iterator()
			{
				return new Iterator<K>()
				{
					public boolean hasNext()
					{
						return i < size;
					}

					public K next()
					{
						// Find next key
						i = nextKey(nextIndex(i));

						// Get key
						return keys[i];
					}

					public void remove()
					{
						keys[i] = null;
						values[i] = null;
						size--;
					}

					int i = -1;
				};
			}

			@Override
			public int size()
			{
				return size;
			}
		};
	}

	/**
	 * @see java.util.Map#values()
	 */
	public Collection<V> values()
	{
		return new AbstractList<V>()
		{
			@Override
			public V get(final int index)
			{
				int keyIndex = nextKey(0);

				for (int i = 0; i < index; i++)
				{
					keyIndex = nextKey(keyIndex + 1);
				}

				return values[keyIndex];
			}

			@Override
			public int size()
			{
				return size;
			}
		};
	}

	/**
	 * @see java.util.Map#entrySet()
	 */
	public Set<Entry<K, V>> entrySet()
	{
		return new AbstractSet<Entry<K, V>>()
		{
			@Override
			public Iterator<Entry<K, V>> iterator()
			{
				return new Iterator<Entry<K, V>>()
				{
					public boolean hasNext()
					{
						return index < size;
					}

					public Entry<K, V> next()
					{
						keyIndex = nextKey(nextIndex(keyIndex));
						index++;

						return new Map.Entry<K, V>()
						{
							public K getKey()
							{
								return keys[keyIndex];
							}

							public V getValue()
							{
								return values[keyIndex];
							}

							public V setValue(final V value)
							{
								final V oldValue = values[keyIndex];

								values[keyIndex] = value;

								return oldValue;
							}
						};
					}

					public void remove()
					{
						keys[keyIndex] = null;
						values[keyIndex] = null;
					}

					int keyIndex = -1;

					int index = 0;
				};
			}

			@Override
			public int size()
			{
				return size;
			}
		};
	}

	/**
	 * Computes the next index in the key or value array (both are the same
	 * length)
	 * 
	 * @param index
	 *            The index
	 * @return The next index, taking into account wraparound
	 */
	private int nextIndex(final int index)
	{
		return (index + 1) % keys.length;
	}

	/**
	 * Finds the index of the next non-null key. If the map is empty, -1 will be
	 * returned.
	 * 
	 * @param start
	 *            Index to start at
	 * @return Index of next non-null key
	 */
	private int nextKey(final int start)
	{
		int i = start;

		do
		{
			if (keys[i] != null)
			{
				return i;
			}

			i = nextIndex(i);
		}
		while (i != start);

		return -1;
	}

	/**
	 * Finds the index of the next null key. If no null key can be found, the
	 * map is full and -1 will be returned.
	 * 
	 * @param start
	 *            Index to start at
	 * @return Index of next null key
	 */
	private int nextNullKey(final int start)
	{
		int i = start;

		do
		{
			if (keys[i] == null)
			{
				return i;
			}

			i = nextIndex(i);
		}
		while (i != start);

		return -1;
	}

	/**
	 * Finds a key by starting at lastSearchIndex and searching from there. If
	 * the key is found, lastSearchIndex is advanced so the next key search can
	 * find the next key in the array, which is the most likely to be retrieved.
	 * 
	 * @param key
	 *            Key to find in map
	 * @return Index of matching key or -1 if not found
	 */
	private int findKey(final Object key)
	{
		// Find key starting at search index
		final int index = findKey(lastSearchIndex, key);

		// Found match?
		if (index != -1)
		{
			// Start search at the next index next time
			lastSearchIndex = nextIndex(index);

			// Return index of key
			return index;
		}

		return -1;
	}

	/**
	 * Searches for a key from a given starting index.
	 * 
	 * @param key
	 *            The key to find in this map
	 * @param start
	 *            Index to start at
	 * @return Index of matching key or -1 if not found
	 */
	private int findKey(final int start, final Object key)
	{
		int i = start;

		do
		{
			if (key.equals(keys[i]))
			{
				return i;
			}

			i = nextIndex(i);
		}
		while (i != start);

		return -1;
	}

	/**
	 * Searches for a value from a given starting index.
	 * 
	 * @param start
	 *            Index to start at
	 * @param value
	 *            The value to find in this map
	 * @return Index of matching value or -1 if not found
	 */
	private int findValue(final int start, final Object value)
	{
		int i = start;

		do
		{
			if (value.equals(values[i]))
			{
				return i;
			}

			i = nextIndex(i);
		}
		while (i != start);

		return -1;
	}
}
