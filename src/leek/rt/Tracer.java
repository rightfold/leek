package leek.rt;

/**
 * A tracer records when procedures are entered (called) and when they leave
 * (return). This is intended to be enabled in production and aids in debugging
 * unforeseen problems.
 *
 * Tracers should not throw exceptions; aborting the program because tracing
 * fails is not desired.
 */
public interface Tracer
{
    public void enter(String callee);
    public void leave();
}
