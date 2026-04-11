package jupiter.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.jupiter.api.extension.TestWatcher;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TestMethodContextExtension implements TestWatcher, TestExecutionExceptionHandler, TestInstancePostProcessor {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(TestMethodContextExtension.class);
    private static final ThreadLocal<ExtensionContext> CONTEXT_HOLDER = new ThreadLocal<>();

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        CONTEXT_HOLDER.set(context);
    }

    public static ExtensionContext context() {
        return CONTEXT_HOLDER.get();
    }
}