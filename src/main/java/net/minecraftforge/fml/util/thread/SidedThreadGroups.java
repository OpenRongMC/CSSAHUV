package net.minecraftforge.fml.util.thread;

/**
 * Stand-in for Forge's {@code SidedThreadGroups}. Forge places server threads into the
 * {@link #SERVER} group so that {@code Thread.currentThread().getThreadGroup() == SERVER} can be
 * used as a cheap logical-side test.
 *
 * <p>On Fabric we cannot reparent Minecraft's threads, so instead of relying on identity of the
 * group we approximate the check: {@link #isServerThread()} returns {@code true} whenever the
 * current thread is not a known client/render thread. {@link #SERVER} itself is also exposed (and
 * used as the parent group of any threads this shim spawns) so that legacy
 * {@code == SidedThreadGroups.SERVER} comparisons still resolve against a real group.</p>
 */
public final class SidedThreadGroups {
	public static final ThreadGroup SERVER = new ThreadGroup("SERVER");
	public static final ThreadGroup CLIENT = new ThreadGroup("CLIENT");

	private SidedThreadGroups() {
	}

	/**
	 * Approximate logical-side test: everything that is not obviously a client thread is treated
	 * as server. This matches the task directive "always treat as server if not client".
	 */
	public static boolean isServerThread() {
		Thread current = Thread.currentThread();
		ThreadGroup group = current.getThreadGroup();
		if (group == CLIENT)
			return false;
		String name = current.getName();
		if (name == null)
			return true;
		String lower = name.toLowerCase();
		boolean looksClient = lower.contains("render") || lower.contains("client") || lower.contains("sound")
				|| lower.equals("main");
		return !looksClient;
	}
}
