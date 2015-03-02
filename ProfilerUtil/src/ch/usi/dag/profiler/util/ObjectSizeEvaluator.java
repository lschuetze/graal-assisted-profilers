package ch.usi.dag.profiler.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;

import sun.misc.Unsafe;

public class ObjectSizeEvaluator {

	private static Unsafe unsafe = null;

	static {
		try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			unsafe = (Unsafe) f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int NR_BITS = Integer.valueOf(System
			.getProperty("sun.arch.data.model"));
	public static final int BYTE = 8;
	public static final int WORD = NR_BITS / BYTE;

	// UseCompressedOops enabled on 64-bit machine
	public static final int MIN_SIZE = 12;

	public static final int INT_IN_BYTE = 4;
	public static final int LONG_IN_BYTE = 8;

	final static HashMap<Class<?>, Integer> sizes = new HashMap<>();

	public static int sizeof(Field f) {
		if (long.class.equals(f.getDeclaringClass())
				|| double.class.equals(f.getDeclaringClass())) {
			return LONG_IN_BYTE;
		} else {
			return INT_IN_BYTE;
		}
	}

	public static int sizeof(Class<?> klass) {
		Integer cache = sizes.get(klass);

		if (cache != null) {
			return cache;
		}

		// object shell size
		int size = MIN_SIZE;

		Class<?> current = klass;

		HashSet<Field> fields = new HashSet<Field>();

		while (Object.class != current) {
			for (Field f : current.getDeclaredFields()) {
				if ((f.getModifiers() & Modifier.STATIC) == 0) {
					fields.add(f);
				}
			}

			current = current.getSuperclass();
		}

		if (unsafe != null) {
			for (Field f : fields) {
				int offset = (int) unsafe.objectFieldOffset(f) + sizeof(f);
				if (offset > size) {
					size = offset;
				}
			}
		} else {
			for (Field f : fields) {
				size += sizeof(f);
			}
		}

		// alignment
		size = (((int) (size - INT_IN_BYTE) / WORD) + 1) * WORD;
		sizes.put(klass, size);
		return size;
	}

}
