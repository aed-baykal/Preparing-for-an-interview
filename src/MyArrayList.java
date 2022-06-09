import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;

public class MyArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

    public static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = {};
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    private Object[] elementData;
    private int size;

    public MyArrayList(int initialCapacity) {
        if (initialCapacity > 0) this.elementData = new Object[initialCapacity];
        else if (initialCapacity == 0) this.elementData = EMPTY_ELEMENTDATA;
        else throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
    }

    public MyArrayList(){
        this.size = 0;
        this.elementData = new Object[size];
    }

    public MyArrayList(Collection<? extends E>  collection) {
        size = collection.size();
        if (size != 0) {
            if (collection.getClass() == MyArrayList.class) this.elementData = collection.toArray();
            else {
                elementData = Arrays.copyOf(collection.toArray(), size, Object[].class);
            }
        } else elementData = EMPTY_ELEMENTDATA;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object[] toArray(Object[] a) {
        if (a.length < size)
            return Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean add(Object o) {
        if (size == elementData.length)
            elementData = grow();
        elementData[size] = o;
        size = size + 1;
        return true;
    }

    @Override
    public boolean addAll(Collection c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            return false;
        Object[] localElementData;
        int s;
        if (numNew > (localElementData = this.elementData).length - (s = size))
            localElementData = grow(s + numNew);
        System.arraycopy(a, 0, localElementData, s, numNew);
        size = s + numNew;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        rangeCheckForAdd(index);
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            return false;
        Object[] localElementData;
        int s;
        if (numNew > (localElementData = this.elementData).length - (s = size))
            localElementData = grow(s + numNew);

        int numMoved = s - index;
        if (numMoved > 0)
            System.arraycopy(localElementData, index,
                    localElementData, index + numNew,
                    numMoved);
        System.arraycopy(a, 0, localElementData, index, numNew);
        size = s + numNew;
        return true;
    }

    @Override
    public void clear() {
        final Object[] objects = elementData;
        for (int to = size, i = size = 0; i < to; i++)
            objects[i] = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        Objects.checkIndex(index, size);
        return (E) elementData[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, Object element) {
        Objects.checkIndex(index, size);
        Object oldValue = elementData[index];
        elementData[index] = element;
        return (E) oldValue;
    }

    @Override
    public void add(int index, Object element) {
        rangeCheckForAdd(index);
        int s;
        Object[] elementData;
        if ((s = size) == (elementData = this.elementData).length)
            elementData = grow();
        System.arraycopy(elementData, index,
                elementData, index + 1,
                s - index);
        elementData[index] = element;
        size = s + 1;
    }

    @Override
    public boolean remove(Object o) {
        Object[] objects = elementData;
        int size = this.size;
        int i = 0;
        found: {
            if (o == null) {
                for (; i < size; i++)
                    if (objects[i] == null)
                        break found;
            } else {
                for (; i < size; i++)
                    if (o.equals(objects[i]))
                        break found;
            }
            return false;
        }
        int newSize;
        if ((newSize = size - 1) > i)
            System.arraycopy(objects, i + 1, objects, i, newSize - i);
        objects[this.size = newSize] = null;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        Objects.checkIndex(index, size);
        Object[] objects = elementData;

        Object oldValue = objects[index];
        int newSize;
        if ((newSize = size - 1) > index)
            System.arraycopy(objects, index + 1, objects, index, newSize - index);
        objects[size = newSize] = null;
        return (E) oldValue;
    }

    @Override
    public int indexOf(Object o) {
        Object[] objects = elementData;
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (objects[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(objects[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Object[] objects = elementData;
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (objects[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(objects[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                    ") > toIndex(" + toIndex + ")");
        MyArrayList<E> subList = new MyArrayList<>();
        System.arraycopy(this.elementData, fromIndex, subList.elementData, 0, toIndex - fromIndex);
        return subList;
    }

    @Override
    public boolean retainAll(Collection c) {
        final Object[] objects = elementData;
        int r;
        // Optimize for initial run of survivors
        for (r = 0;; r++) {
            if (r == size)
                return false;
            if (!c.contains(objects[r]))
                break;
        }
        int w = r++;
        try {
            for (Object e; r < size; r++)
                if (c.contains(e = objects[r]))
                    objects[w++] = e;
        } catch (Throwable ex) {
            // Preserve behavioral compatibility with AbstractCollection,
            // even if c.contains() throws.
            System.arraycopy(objects, r, objects, w, size - r);
            w += size - r;
            throw ex;
        } finally {
            System.arraycopy(objects, size, objects, w, 0);
            for (int to = size, i = (size -= size - w); i < to; i++)
                objects[i] = null;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection c) {
        Objects.requireNonNull(c);
        final Object[] objects = elementData;
        int r;
        for (r = 0;; r++) {
            if (r == size)
                return false;
            if (c.contains(objects[r]))
                break;
        }
        int w = r++;
        try {
            for (Object e; r < size; r++)
                if (!c.contains(e = objects[r]))
                    objects[w++] = e;
        } catch (Throwable ex) {
            System.arraycopy(objects, r, objects, w, size - r);
            w += size - r;
            throw ex;
        } finally {
            System.arraycopy(objects, size, objects, w, 0);
            for (int to = size, i = (size -= size - w); i < to; i++)
                objects[i] = null;
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        rangeCheckForAdd(index);
        return new ListItr(index);
    }

    public int size() {
        return size;
    }

    private Object[] grow() {
        return grow(size + 1);
    }

    private Object[] grow(int minCapacity) {
        int oldCapacity = elementData.length;
        if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            int newCapacity = newLength(oldCapacity,
                    minCapacity - oldCapacity, /* minimum growth */
                    oldCapacity >> 1           /* preferred growth */);
            return elementData = Arrays.copyOf(elementData, newCapacity);
        } else {
            return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
    }

    public int newLength(int oldLength, int minGrowth, int prefGrowth){
        int prefLength = oldLength + Math.max(minGrowth, prefGrowth);
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
            return prefLength;
        } else {
            return hugeLength(oldLength, minGrowth);
        }
    }

    private int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError(
                    "Required array length " + oldLength + " + " + minGrowth + " is too large");
        } else if (minLength <= SOFT_MAX_ARRAY_LENGTH) {
            return SOFT_MAX_ARRAY_LENGTH;
        } else {
            return minLength;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    @Override
    public Object clone() {
        try {
            MyArrayList<?> v = (MyArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        Itr() {}

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
//            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = MyArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
//            checkForComodification();

            try {
                MyArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int size = MyArrayList.this.size;
            int i = cursor;
            if (i < size) {
                final Object[] es = elementData;
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size; i++)
                    action.accept(elementAt(es, i));
                cursor = i;
                lastRet = i - 1;
            }
        }

    }

    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
//            checkForComodification();
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = MyArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
//            checkForComodification();

            try {
                MyArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
//            checkForComodification();

            try {
                int i = cursor;
                MyArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
//                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @SuppressWarnings("unchecked")
    static <E> E elementAt(Object[] es, int index) {
        return (E) es[index];
    }

}
