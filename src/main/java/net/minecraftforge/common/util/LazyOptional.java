package net.minecraftforge.common.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A lazily-resolved optional, mirroring the subset of Forge's {@code LazyOptional} used by the
 * ported mod. Capability lookups on this shim typically return {@link #empty()}, so most call
 * sites simply fall through to their {@code orElse(...)} default.
 */
public final class LazyOptional<T> {
	private static final LazyOptional<Object> EMPTY = new LazyOptional<>(null);

	private final Supplier<T> supplier;
	private boolean resolved;
	private T value;
	private boolean valid = true;

	private LazyOptional(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public static <T> LazyOptional<T> of(Supplier<T> instanceSupplier) {
		return instanceSupplier == null ? empty() : new LazyOptional<>(instanceSupplier);
	}

	@SuppressWarnings("unchecked")
	public static <T> LazyOptional<T> empty() {
		return (LazyOptional<T>) EMPTY;
	}

	private T resolve() {
		if (this.supplier == null || !this.valid)
			return null;
		if (!this.resolved) {
			this.value = this.supplier.get();
			this.resolved = true;
		}
		return this.value;
	}

	public boolean isPresent() {
		return resolve() != null;
	}

	public void ifPresent(Consumer<? super T> consumer) {
		T resolved = resolve();
		if (resolved != null)
			consumer.accept(resolved);
	}

	public T orElse(T other) {
		T resolved = resolve();
		return resolved != null ? resolved : other;
	}

	public T orElseGet(Supplier<? extends T> other) {
		T resolved = resolve();
		return resolved != null ? resolved : other.get();
	}

	/** Reinterprets this optional as a different element type (Forge capability idiom). */
	@SuppressWarnings("unchecked")
	public <X> LazyOptional<X> cast() {
		return (LazyOptional<X>) this;
	}

	/** Marks this optional invalid, so it behaves as empty from now on (Forge idiom). */
	public void invalidate() {
		this.valid = false;
		this.resolved = true;
		this.value = null;
	}
}
