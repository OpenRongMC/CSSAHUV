package net.minecraftforge.eventbus.api;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * A minimal event bus. Despite the {@code I}-prefixed name (kept for source compatibility with
 * Forge), this is a concrete implementation: listeners are discovered by scanning
 * {@link SubscribeEvent}-annotated methods on {@link #register(Object)}, and dispatched by
 * {@link #post(Event)} to every listener whose declared parameter type is assignable from the
 * posted event.
 */
public final class IEventBus {
	private record Listener(Class<?> eventType, Consumer<Event> invoker) {
	}

	private final List<Listener> listeners = new CopyOnWriteArrayList<>();

	public IEventBus() {
	}

	/**
	 * Registers a listener. The argument may be either:
	 * <ul>
	 *     <li>a {@link Class}, in which case its {@code static} {@link SubscribeEvent} methods are registered, or</li>
	 *     <li>an object instance, in which case both its instance and static annotated methods are registered.</li>
	 * </ul>
	 */
	public void register(Object target) {
		if (target == null)
			return;
		boolean isClass = target instanceof Class<?>;
		Class<?> owner = isClass ? (Class<?>) target : target.getClass();
		Object instance = isClass ? null : target;
		for (Method method : owner.getDeclaredMethods()) {
			if (!method.isAnnotationPresent(SubscribeEvent.class))
				continue;
			Class<?>[] params = method.getParameterTypes();
			if (params.length != 1)
				continue;
			boolean isStatic = Modifier.isStatic(method.getModifiers());
			if (isClass && !isStatic)
				continue;
			final Object boundInstance = isStatic ? null : instance;
			method.setAccessible(true);
			final Class<?> eventType = params[0];
			listeners.add(new Listener(eventType, event -> {
				try {
					method.invoke(boundInstance, event);
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException("Failed to dispatch " + eventType.getName() + " to " + method, e);
				}
			}));
		}
	}

	/**
	 * Registers a single typed listener. Mirrors Forge's {@code addListener}.
	 */
	public <T extends Event> void addListener(Class<T> eventType, Consumer<T> consumer) {
		@SuppressWarnings("unchecked")
		Consumer<Event> raw = (Consumer<Event>) consumer;
		listeners.add(new Listener(eventType, raw));
	}

	/**
	 * Posts an event to all applicable listeners.
	 *
	 * @return {@code true} if the event was cancelled.
	 */
	public boolean post(Event event) {
		List<Listener> snapshot = new ArrayList<>(listeners);
		for (Listener listener : snapshot) {
			if (listener.eventType().isInstance(event))
				listener.invoker().accept(event);
		}
		return event.isCanceled();
	}
}
