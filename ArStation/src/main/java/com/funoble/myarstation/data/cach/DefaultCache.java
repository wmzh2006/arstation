package  com.funoble.myarstation.data.cach;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 缺省缓存实现。主要算法为：一个 HashMap 保持对缓存对象的快速查询，一个访问队列(LRU)按缓存对象最近访问顺序排序，一个年龄队列按缓存对象年龄排序。
 * 
 * 当一个对象加入到缓存空间时，首先将此包装为一个 CacheObject，其记录信息包括：
 * 
 * 1. 缓存对象本身
 * 
 * 2. 缓存对象在 LRU 队列中的节点引用
 * 
 * 3. 缓存对象在年龄队列中的节点引用
 * 
 * @author totobacoo
 */

public class DefaultCache<K, V> implements Cache<K, V>
{
	
	protected Map<K, DefaultCache.CacheObject<V>>	map;

	/**
	 * 最近访问维护
	 */
	protected LinkedList							lastAccessedList;

	/**
	 * 缓存对象的年龄维护
	 */
	protected LinkedList							ageList;

	/**
	 * 最大缓存对象数量
	 */
	private long									maxCacheSize;

	/**
	 * 缓存对象数量
	 */
	private int										cacheSize	= 0;

	/**
	 * 缓存对象最大生命周期
	 */
	protected long									maxLifetime;

	/**
	 * 缓存命中和丢失
	 */
	protected long									cacheHits, cacheMisses = 0L;

	/**
	 * 缓存名字
	 */
	private String									name;


	/**
	 * 创建一个缺省 cache
	 * 
	 * @param name cache名字
	 * @param maxSize 最大缓存对象数量，-1为无限制
	 * @param maxLifetime 最大缓存生命，-1为永不过期
	 */
	public DefaultCache(String name, long maxSize, long maxLifetime)
	{
		this.name = name;
		this.maxCacheSize = maxSize;
		this.maxLifetime = maxLifetime;

		// 本缓存主要容器是 HashMap，缺省容量为 11，大多数情况下不适用，因此设置成更大
		map = new HashMap<K, CacheObject<V>>(103);

		lastAccessedList = new LinkedList();
		ageList = new LinkedList();
	}


	public synchronized V put(K key, V value)
	{
		// 删除旧的 entryKey
		V answer = remove(key);

		cacheSize++;
		DefaultCache.CacheObject<V> cacheObject = new DefaultCache.CacheObject<V>(value);
		map.put(key, cacheObject);

		// 维护访问队列
		LinkedListNode lastAccessedNode = lastAccessedList.addFirst(key);

		// 在缓存对象中保存其在访问队列中的引用，以便随后的查找
		cacheObject.lastAccessedListNode = lastAccessedNode;

		// 维护年龄队列
		LinkedListNode ageNode = ageList.addFirst(key);
		ageNode.timestamp = System.currentTimeMillis();

		// 在缓存对象中保存其在年龄队列中的引用，以便随后的查找
		cacheObject.ageListNode = ageNode;

		// 如果缓存容量达上限，移除最近最少访问的缓存对象，直到腾出空间
		cullCache();

		return answer;
	}


	public synchronized V get(Object key)
	{
		// 每次查询缓存对象前，首先清除一次缓存空间内的生命超时对象
		deleteExpiredEntries();

		DefaultCache.CacheObject<V> cacheObject = map.get(key);
		if (cacheObject == null)
		{
			// 缓存查询失败，自增 miss 计数
			cacheMisses++;
			return null;
		}

		// 缓存查询成功，自增 hit 计数
		cacheHits++;

		// 缓存查询成功，自增访问计数
		cacheObject.readCount++;

		// 将访问记录从访问队列中删除，重新插入到队列的最前面
		cacheObject.lastAccessedListNode.remove();
		lastAccessedList.addFirst(cacheObject.lastAccessedListNode);

		return cacheObject.object;
	}


	public synchronized V remove(Object key)
	{
		DefaultCache.CacheObject<V> cacheObject = map.get(key);
		if (cacheObject == null)
		{
			return null;
		}

		// 删除缓存对象
		map.remove(key);

		// 维护访问队列和年龄队列
		cacheObject.lastAccessedListNode.remove();
		cacheObject.ageListNode.remove();

		// 置空缓存对象中的访问队列引用和年龄队列引用
		cacheObject.ageListNode = null;
		cacheObject.lastAccessedListNode = null;

		cacheSize--;

		return cacheObject.object;
	}


	public synchronized void clear()
	{
		Object[] keys = map.keySet().toArray();
		for (int i = 0; i < keys.length; i++)
		{
			remove(keys[i]);
		}

		// 清空重置所有容器
		map.clear();
		lastAccessedList.clear();
		lastAccessedList = new LinkedList();
		ageList.clear();
		ageList = new LinkedList();

		cacheSize = 0;
		cacheHits = 0;
		cacheMisses = 0;
	}


	public int size()
	{
		// 统计缓存数量前，先清除所有年龄超时的缓存对象
		deleteExpiredEntries();

		return map.size();
	}


	public boolean isEmpty()
	{
		// 统计缓存数量前，先清除所有年龄超时的缓存对象
		deleteExpiredEntries();

		return map.isEmpty();
	}


	public Collection<V> values()
	{
		// 返回value集合前，先清除所有年龄超时的缓存对象
		deleteExpiredEntries();
		return new DefaultCache.CacheObjectCollection(map.values());
	}


	/**
	 * Wraps a cached object collection to return a view of its inner objects
	 */
	@SuppressWarnings("hiding")
	private final class CacheObjectCollection<V> implements Collection<V>
	{
		private Collection<DefaultCache.CacheObject<V>>	cachedObjects;

		private CacheObjectCollection(Collection<DefaultCache.CacheObject<V>> cachedObjects)
		{
			this.cachedObjects = new ArrayList<CacheObject<V>>(cachedObjects);
		}

		public int size()
		{
			return cachedObjects.size();
		}

		public boolean isEmpty()
		{
			return size() == 0;
		}

		public boolean contains(Object o)
		{
			Iterator<V> it = iterator();
			while (it.hasNext())
			{
				if (it.next().equals(o))
				{
					return true;
				}
			}
			return false;
		}

		public Iterator<V> iterator()
		{
			return new Iterator<V>()
			{
				private final Iterator<DefaultCache.CacheObject<V>>	it	= cachedObjects.iterator();

				public boolean hasNext()
				{
					return it.hasNext();
				}

				public V next()
				{
					if (it.hasNext())
					{
						DefaultCache.CacheObject<V> object = it.next();
						if (object == null)
						{
							return null;
						}
						else
						{
							return object.object;
						}
					}
					else
					{
						throw new NoSuchElementException();
					}
				}

				public void remove()
				{
					throw new UnsupportedOperationException();
				}
			};
		}

		public Object[] toArray()
		{
			Object[] array = new Object[size()];
			Iterator it = iterator();
			int i = 0;
			while (it.hasNext())
			{
				array[i] = it.next();
			}
			return array;
		}

		public <V> V[] toArray(V[] a)
		{
			Iterator<V> it = (Iterator<V>) iterator();
			int i = 0;
			while (it.hasNext())
			{
				a[i++] = it.next();
			}
			return a;
		}

		public boolean containsAll(Collection<?> c)
		{
			Iterator it = c.iterator();
			while (it.hasNext())
			{
				if (!contains(it.next()))
				{
					return false;
				}
			}
			return true;
		}

		public boolean add(V o)
		{
			throw new UnsupportedOperationException();
		}

		public boolean remove(Object o)
		{
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection<? extends V> coll)
		{
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(Collection<?> coll)
		{
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(Collection<?> coll)
		{
			throw new UnsupportedOperationException();
		}

		public void clear()
		{
			throw new UnsupportedOperationException();
		}
	}

	public boolean containsKey(Object key)
	{
		// First, clear all entries that have been in cache longer than the
		// maximum defined age.
		deleteExpiredEntries();

		return map.containsKey(key);
	}

	public void putAll(Map<? extends K, ? extends V> map)
	{
		for (Iterator<? extends K> i = map.keySet().iterator(); i.hasNext();)
		{
			K key = i.next();
			V value = map.get(key);
			put(key, value);
		}
	}

	public boolean containsValue(Object value)
	{
		// First, clear all entries that have been in cache longer than the
		// maximum defined age.
		deleteExpiredEntries();

		if (value == null)
		{
			return containsNullValue();
		}

		Iterator it = values().iterator();
		while (it.hasNext())
		{
			if (value.equals(it.next()))
			{
				return true;
			}
		}
		return false;
	}

	private boolean containsNullValue()
	{
		Iterator it = values().iterator();
		while (it.hasNext())
		{
			if (it.next() == null)
			{
				return true;
			}
		}
		return false;
	}

	public Set<Entry<K, V>> entrySet()
	{
		// First, clear all entries that have been in cache longer than the
		// maximum defined age.
		deleteExpiredEntries();
		// TODO Make this work right

		synchronized (this)
		{
			final Map<K, V> result = new HashMap<K, V>();
			for (final Entry<K, DefaultCache.CacheObject<V>> entry : map.entrySet())
			{
				result.put(entry.getKey(), entry.getValue().object);
			}
			return result.entrySet();
		}
	}

	public Set<K> keySet()
	{
		// First, clear all entries that have been in cache longer than the
		// maximum defined age.
		deleteExpiredEntries();
		synchronized (this)
		{
			return new HashSet<K>(map.keySet());
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getCacheHits()
	{
		return cacheHits;
	}

	public long getCacheMisses()
	{
		return cacheMisses;
	}

	public int getCacheSize()
	{
		return cacheSize;
	}

	public long getMaxCacheSize()
	{
		return maxCacheSize;
	}

	public void setMaxCacheSize(int maxCacheSize)
	{
		this.maxCacheSize = maxCacheSize;
		cullCache();
	}

	public long getMaxLifetime()
	{
		return maxLifetime;
	}

	public void setMaxLifetime(long maxLifetime)
	{
		this.maxLifetime = maxLifetime;
	}


	/**
	 * 清除所有年龄超时对象
	 */
	protected void deleteExpiredEntries()
	{
		if (maxLifetime <= 0)
		{
			return;
		}

		// 从年龄队列最后面开始清除，可以减少开销
		LinkedListNode node = ageList.getLast();
		if (node == null)
		{
			return;
		}

		long expireTime = System.currentTimeMillis() - maxLifetime;
		while (expireTime > node.timestamp)
		{
			remove(node.object);

			node = ageList.getLast();
			if (node == null)
			{
				return;
			}
		}
	}


	/**
	 * 当缓存空间内缓存对象数量快要爆满时，开始LRU 清理 缓存空间容量使用 97% 以上，视为快要爆满 空间清理后，将腾出 10% 的空闲空间
	 */
	protected final void cullCache()
	{
		if (maxCacheSize < 0)
		{
			return;
		}

		if (cacheSize >= maxCacheSize * .97)
		{
			deleteExpiredEntries();
			int desiredSize = (int) (maxCacheSize * .90);
			while (cacheSize > desiredSize)
			{
				remove(lastAccessedList.getLast().object);
			}
		}
	}


	/**
	 * 缓存对象封装。其主要功能为保持对访问队列和年龄队列的引用。
	 */
	private static class CacheObject<V>
	{
		/**
		 * 实际缓存对象
		 */
		public V				object;

		/**
		 * 访问队列引用
		 */
		public LinkedListNode	lastAccessedListNode;

		/**
		 * 年龄队列引用
		 */
		public LinkedListNode	ageListNode;

		/**
		 * 访问次数
		 */
		public int				readCount	= 0;


		public CacheObject(V object)
		{
			this.object = object;
		}
	}
}
