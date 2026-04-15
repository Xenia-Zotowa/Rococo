package jupiter.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class TestMethodContextExtension implements TestWatcher, TestExecutionExceptionHandler {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(TestMethodContextExtension.class);
    private static final ThreadLocal<ExtensionContext> CONTEXT_HOLDER = new ThreadLocal<>();

    public static ExtensionContext context() {
        return CONTEXT_HOLDER.get();
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        CONTEXT_HOLDER.set(context);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        CONTEXT_HOLDER.set(context);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        CONTEXT_HOLDER.set(context);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        CONTEXT_HOLDER.set(context);
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        CONTEXT_HOLDER.set(context);
        throw throwable;
    }
}