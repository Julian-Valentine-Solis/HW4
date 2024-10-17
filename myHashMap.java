/*
 * *** Julian Solis / 001 ***
 *
 * This hashMap object represents an over simplification of Java's implementation of HashMap within
 * Java's Collection Framework Library. You are to complete the following methods:
 *  - remove(K)
 *  - replace(K,V)
 *  - replace(K,V,V)
 *
 * In addition to the documentation below, you can read the online Java documentation for HashMap for
 * the expected behavior / return values of each method below. This object follows the same behavior
 * of those methods implemented in this Java library.
 *
 */

/**
 *
 *  This sample code is illustrating a hash table using separate chaining. To illustrate this,
 *  the code is building a Hash Map implementation that emulates Java's HashMap class. This class
 *  implements many of the java library's class's methods and emulates the behavior of the Map
 *  interface which is what the Java Library does.
 *
 *  This class is demonstrating the use of separate chaining hashing, which is also used by
 *  Java's library class. This class is not intended to be a full-blown Hash Map / Hash Table
 *  implementation, nor does it implement all methods in Java's HashMap class. But the ones that
 *  are implemented emulate how those methods work in Java's HashMap.
 *
 *  CAVEAT: as indicated, Java provides a HashMap class that is implemented on the Map Interface
 *  that is more robust, and is more expansive than this implementation. But what is implemented
 *  operates the same way. This coding example is illustrating sample coding for how hash tables
 *  using separate chaining (versus open addressing techniques). And the behavior emulates the Map
 *  interface that Java's HashMap follows.
 *
 *  PUBLIC METHODS:
 *  ---------------
 *
 *     void  clear()               - Removes all of the mappings from this map.
 *  boolean  containsValue(V)      - Returns true if this map maps one or more keys to the specified value
 *  boolean  containsKey(K)        - Returns true if this map contains a mapping for the specified key.
 *       V   get(K)                - Returns the value to which the specified key is mapped, or null
 *                                   if this map contains no mapping for the key
 *       V   put(K, V)             - Associates the specified value with the specified key in this map
 *       V   putIfAbsent(K, V)     - If the specified key is not already associated with a value (or
 *                                   is mapped to null) associates it with the given value and returns
 *                                   null, else returns the current value
 *       V   remove(K)             - Removes the entry for the specified key only if it is currently
 *                                   mapped to the specified value
 *  boolean  remove(K, V)          - Removes the entry for the specified key only if it is currently
 *                                   mapped to the specified value.
 *  boolean  replace(K, V)         - Replaces the entry for the specified key only if it is currently
 *                                   mapped to some value
 *        V  replace(K, V1, V2)    - Replaces the entry for the specified key only if currently mapped
 *                                   to the specified value.
 *  Set<K>   keySet()              - Returns a 'Set' view of the keys contained in the map.
 *  Set<Map.Entry<K,V>> entrySet() - Returns a 'Set' view of the mappings contains in the map.
 *      int  size()                - returns the number of <k,v> pairs in hashmap
 *      boolean isEmpty()          - returns true if this map contains no key-value mappings.
 *
 *
 *  Methods *NOT* implemented to fully emulate the behavior 
 *  of Java's HashMap Class
 *      - clone()
 *      - compute()
 *      - computeIfAbsent()
 *      - computeIfPresent()
 *      - foreach()
 *      - merge(()
 *      - putAll()
 *      - replaceAll()
 *      - values()
 *
 ****************************************/

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Class HashNode
 *
 * Node object representing a <Key, Value> pair stored in the Hash Map, elements
 * hashed to the same bucket slot will be chained through a singly linked-list.
 */

class HashNode<K, V> {
    K key;
    V value;
    HashNode<K, V> next;

    public HashNode() {
        this.key = key;
        this.value = value;
    }
}

/**
 * A simple implementation of a HashMap that is built to emulate the Map
 * Interface.
 * The <key, values> pairs are stored in a Map, where the key represents a hash
 * bucket slot number and the value represents a node which will form as
 * linked-list
 * for hash collisions on that bucket's slot.
 *
 * The array in this class represents the buckets, and each bucket has a pointer
 * to a node class for the linked-list of <k,v> pairs. The key for this bucket
 * array
 * is generated using a hash function that returns a number from 0 to n-1, where
 * n
 * is the number of buckets (array size).
 *
 * Note: Java provides a HashMap class which implements the HashMap on the Map
 * interface. Again, the intent is not to replace it and/or build out to the
 * same
 * level. We are illustrating 'separate chaining' using a singly linked-list.
 *
 * Last, the hashmap (array) is small, in practice, it will be much larger. But
 * this
 * will also illustrate the load factor being reached much faster and seeing the
 * hashmap
 * growth code be exercised.
 */

class myHashMap<K, V> {

    private static final float DEFAULT_LOAD_FACTOR = 0.7f;
    private static final int INITIAL_NUM_BUCKETS = 10;

    ArrayList<HashNode<K, V>> bucket = new ArrayList<>();
    int numBuckets = INITIAL_NUM_BUCKETS;
    int size = 0;

    public myHashMap() {
        for (int i = 0; i < numBuckets; i++) {
            bucket.add(null);
        }
    }

    public int Size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Method clear()
     *
     * Reinitialize the hash to INITIAL_NUM_BUCKETS. For each bucket, it resets
     * the bucket slots (in the array) to a null Node.
     */

    public void clear() {
        size = 0;
        numBuckets = INITIAL_NUM_BUCKETS;
        bucket = new ArrayList<>();
        for (int i = 0; i < numBuckets; i++) {
            bucket.add(null);
        }
    }

    /**
     * method getBucketindex()
     *
     * Performs two parts.
     * 1) First invokes a very simple hash code generator which generates a 32-bit
     * integer. The mask (bit operation) masks off the sign bit )turns the
     * 32-bit integer into a 31-bit non-negative integer).
     * 2) Second, it invokes a compressor expression (in this case, performing a
     * MOD operation). This compresses the hash number to between 0 and
     * (numBuckets-1) which will be an index into our hash bucket slot (aka,
     * key for Map);
     *
     * @param key - key value to locate hash map bucket for
     *
     * @return bucketIndex - bucket index number for key value
     */

    private int getBucketIndex(K key) {
        return (key.hashCode() & 0x7fffffff) % numBuckets;
    }

    /**
     * method: V get(K)
     *
     * Returns the value to which the specified key is mapped, or null if this map
     * contains
     * no mapping for the key. This method will probe to the correct bucket, then
     * loop
     * through the bucket's chained nodes (linked-list) until the key is found. If
     * not found,
     * the key is not in the hash map and the method will return null.
     *
     * @param key - key value for identifying the <k,v> pair
     *
     * @return val - value for the provided key value, else null
     */

    public V get(K key) {
        int index = getBucketIndex(key);

        HashNode<K, V> head = bucket.get(index);
        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    /**
     * method: V remove(K)
     *
     * Removes the entry for the specified key only if it is currently mapped to the
     * specified value. The method will probe into the bucket lists. If the bucket
     * has a chained list, then it will be traversed to identify the key to
     * remove. If no chained list and/or no node is found the chained list
     * representing
     * the key, then it is not in the hash map.
     *
     * If the key is found, it is removed from the chained list and the hashmap size
     * is adjusted.
     *
     * @param key - key value for the <key,value> pair to remove
     *
     * @return value - return the node for the <key,value>
     *         removed, else null if not found
     */

    public V remove(K key) {

        /*
         * ADD YOUR CODE HERE
         *
         * Review the code in the whole object to understand teh data structures layout.
         * Additionally, review the method put() for inserting a new Key / Value pair
         * into
         * the HashMap. This method will do the opposite by removing an element. Do see
         * the return value discussion in this method's prologue to make sure the
         * correct
         * return value is returned the invoking function based on the remove outcome.
         */

        // We need to get the key's hash code in order to identify whether the key is
        // present in the hashMap buckets
        int indexOfKeyThatWeAreLookingFor = getBucketIndex(key);// Locate the bucket index
        HashNode<K, V> currentNode = bucket.get(indexOfKeyThatWeAreLookingFor);// Get the bucket associated with it and
                                                                               // pull the head of the linked located in
                                                                               // that bucket
        HashNode<K, V> nodeBeforCurrent = null;// Temp node used to conduct removal

        if (currentNode == null) {// if that buckect contains no key, value pairs, then that tells us this key
                                  // does not exist in our HashMap and that bucket is null
            return null;// The bucket is empty
        } else {// if the bucket holds a linked, we will need to traverse that list in an effort
                // to locate that key
            while (currentNode != null && !currentNode.key.equals(key)) {// Traverse the list, checking to see if each
                                                                         // of the node's in the list are not
                                                                         // null(meaning we haven't reached the end of
                                                                         // the list) and do not contain the key we are
                                                                         // looking for.
                nodeBeforCurrent = currentNode;// Tracking the node prior for easy removal process
                currentNode = currentNode.next;// Move onto the next node for analysis
            }
        } // at this point the while loop would have stopped either because we have
          // located a node that is null or located the key that we are looking for, let's
          // go ahead and handle the case in whihc we searched the entirety of the linked
          // list and we found no matching key
        if (currentNode == null) {
            return null;// we have traversed and found no matching key
        }

        V removedValue = currentNode.value;

        if (nodeBeforCurrent != null) {
            // If the node before the current node is not null, this indicates that they key
            // that we are removing is not at the start of the linked list, so we can simply
            // just bypass the node that we are removing.
            nodeBeforCurrent.next = currentNode.next;// connecting node before removal node to node after removal node
        } else {
            // If the node to remove is the head, use the set
            bucket.set(indexOfKeyThatWeAreLookingFor, currentNode.next);// If we are removing the first node in the
                                                                        // linked list, you need to make the next node
                                                                        // the new head of the linked list.
        }

        size--;// adjusting the size of the map
        return removedValue;// returning the node removed
    }

    /**
     * Method: boolean remove(K, V)
     *
     * Removes the entry for the specified key only if it is currently mapped to
     * some value
     *
     * @param: key - key for identifying <k,v>
     * @param: val - will remove <k,v> only if existing value
     *             equals val
     *
     * @return: true if deleted, else false
     */

    public boolean remove(K key, V val) {

        V originalValue = get(key);

        if (originalValue == null ||
                (!originalValue.equals(val))) {
            return false;
        }

        // Key was found and its value equals the passed
        // parameter 'val'
        remove(key);

        return true;
    }

    /**
     * method: V put(K, V)
     *
     * Associates the specified value with the specified key in this map. The method
     * will check if the key is already in the hash map, if so, it updates the value
     * and returns the old value. If the key is not found, then it will insert the
     * <k,v> pair. Last if inserting the <k,v>, the load factor is checked. If it is
     * greater than the DEFAULT_LOAD_FACTOR %, the method will double the bucket
     * map and rehash the whole hash map.
     *
     * @param key   - Key to the <k,v> pair operate on
     * @param value - if key found, value is updated to this
     *              param, else routine inserts <k,v>
     *
     * @return value - if key exists, returns old value before
     *         replacing with provided value, else null.
     */

    public V put(K key, V value) {

        /*
         * If the <key,value> already exists in the hash map,
         * then replace the value, else insert the <key,value>
         */
        V oldValue = get(key);
        if (oldValue != null) {
            replace(key, value);
            return oldValue;
        }

        int index = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(index);
        HashNode<K, V> toAdd = new HashNode<>();
        toAdd.key = key;
        toAdd.value = value;
        if (head == null) {
            bucket.set(index, toAdd);
            size++;

        } else {
            while (head != null) {
                if (head.key.equals(key)) {
                    head.value = value;
                    size++;
                    break;
                }
                head = head.next;
            }
            if (head == null) {
                head = bucket.get(index);
                toAdd.next = head;
                bucket.set(index, toAdd);
                size++;
            }
        }

        /*
         * Check the load factor of the hashmap, if greater
         * than DEFAULT_LOAD_FACTOR, we will double the number
         * of buckets of our hashmap.
         */

        if ((1.0 * size) / numBuckets > DEFAULT_LOAD_FACTOR) {
            // do something
            ArrayList<HashNode<K, V>> tmp = bucket;
            bucket = new ArrayList<>();
            numBuckets = 2 * numBuckets;
            size = 0;

            for (int i = 0; i < numBuckets; i++) {
                bucket.add(null);
            }

            /*
             * Traverse the original buckets, and for each bucket
             * traverse the nodes stored there (via linked-list).
             * For each node (<key, value> pair), add to the new
             * (grown) bucket list. The re-add process will
             * rehash the keys to the new bucket size.
             */
            for (HashNode<K, V> headNode : tmp) {
                while (headNode != null) {
                    put(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }

        return null;
    }

    /**
     * method: V putIfAbsent(K, V)
     *
     * If the specified key is not already associated with a value (or is mapped to
     * null)
     * associates it with the given value and returns null, else returns the current
     * value.
     *
     * @param: key - The key to check if exists in the hashmap
     * @parem: value - The value to place in as a <k, v> pair if
     *         key does not exist
     *
     * @return: V - returns the existing value if the key is
     *          found, else null
     */

    public V putIfAbsent(K key, V value) {
        V originalValue = get(key);

        if (originalValue == null) {
            put(key, value);
            return null;
        }

        return originalValue;
    }

    /**
     * method: V replace(K, V)
     *
     * Replaces the entry for the specified key only if it is currently mapped to
     * some value (aka, the
     * key already exist with some value).
     *
     * @param key - Key for the <k, v> pair to replace its
     *            value
     * @param val - The new value to replace the old one if
     *            found.
     *
     * @return V - returns the old value for the <k,v> pair,
     *         else null if not found.
     */

    public V replace(K key, V val) {

        /*
         * ADD YOUR CODE HERE - DO NOT FORGET TO ADD YOUR NAME AT TOP OF FILE
         *
         * Make sure you return the proper value based on the outcome of this method's
         * replace (see method's prologue above).
         */
        // So we need to locate the key given, so once again we need to utilize the hash
        // function to figure out which bucket this key is in, if it's in one at all.
        int hashFunctionCode = getBucketIndex(key);// get the hash code
        HashNode<K, V> node = bucket.get(hashFunctionCode);// use the hash code to find the correct bucket and linked
                                                           // list

        while (node != null && !node.key.equals(key)) {// Searching for the key while we have not reached the end of the
                                                       // linked list and we haven't located the key
            node = node.next;
        }

        // if we have made it to this point either we haven't found the key or we have
        // found the key and we'll need to perform the replacement
        if (node == null) {// no key located, return null
            return null;
        } else {// perform replacement
            V oldValue = node.value;// store old value for return
            node.value = val;// param val replaces the node's old value
            return oldValue;// return the old value
        }
    }

    /**
     * method: boolean replace(K, V, V)
     *
     * Replaces the entry for the specified key only if currently mapped to the
     * specified value.
     *
     * @param key    - Key for the <k, v> pair to replace its
     *               value
     * @param oldVal - Replace only if current <k,v>'s value
     *               is same as oldVal
     * @param newVal - the new value to use.
     *
     * @return V - returns the old value for the <k,v> pair,
     *         else null if not found.
     */
    // Changed boolean to V in order to follow the following instruction: returns
    // the old value for the <k,v> pair, else null if not found, then realized that
    // the main class is expecting a boolean, so I changed it back
    public boolean replace(K key, V oldVal, V newVal) {

        /*
         * ADD YOUR CODE HERE
         *
         * This method should apply the precondition (aka, the Key already exists with
         * the
         * value 'oldval', and is so, it SHOULD call replace(K, V) for code reuse.
         */
        // Finding the bucket, use hash function to locate index of bucket
        int bucketID = getBucketIndex(key);// We have our bucket index
        HashNode<K, V> node = bucket.get(bucketID);// Grabbing the node for the link list in the bucket

        while (node != null) {// while we haven't reached the end of the list, keeping traversing...
            if (node.key.equals(key) && node.value.equals(oldVal)) {// if we find a node that contains the key we are
                                                                    // looking for and the value of oldVal matches the
                                                                    // value in the node, replace that key's value with
                                                                    // the newVal
                // V oldValue = replace(key, newVal);
                replace(key, newVal);// use our previous method that will handle replacing the old value w/ newVal
                // return oldValue;
                return true;// We have completed the replacement
            }
            node = node.next;// Go to next node.
        }

        return false;
    }

    /**
     * Method: boolean contains(V)
     *
     * Returns true if this map maps one or more keys to the specified value
     *
     * @param val: Value to search for in hashmap to determine
     *             if it is contained there.
     *
     * @return: true if found, else false.
     */

    public boolean containsValue(V val) {

        for (HashNode<K, V> headNode : bucket) {
            while (headNode != null) {
                if (headNode.value.equals(val))
                    return true;
                headNode = headNode.next;
            }
        }

        return false;
    }

    /**
     * Method: boolean containsKey(K)
     *
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key: The key to search for to determine of hash
     *             map contains it
     *
     * @return: true if found, else false.
     */

    public boolean containsKey(K key) {
        return (get(key) == null ? false : true);
    }

    /**
     * Method: Set<Map.Entry<K,V>> entrySet()
     *
     * Returns a 'Set' view of the mappings contained in the map.
     *
     * @return Set<Map.Entry<K,V></K,V>> - set of all K/V pairs in map
     */
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> returnSet = new HashSet<>();

        for (HashNode<K, V> headNode : bucket) {
            while (headNode != null) {
                returnSet.add(Map.entry(headNode.key, headNode.value));
                headNode = headNode.next;
            }
        }

        return returnSet;
    }

    /**
     * Method: Set<K> keySet()
     *
     * Returns a 'Set' view of the keys contained in the map.
     *
     * @return Set<K> - set of all keys in map
     */

    public Set<K> keySet() {
        Set<K> returnSet = new HashSet<>();
        for (HashNode<K, V> headNode : bucket) {
            while (headNode != null) {
                returnSet.add(headNode.key);
                headNode = headNode.next;
            }
        }

        return returnSet;
    }

} /* end class myHashMap */