package jupiter.extension;

import jupiter.annotation.User;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String DEFAULT_PASSWORD = "secret";

    private final UserdataApiClient userdataApiClient = new UserdataApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnnotation -> {
                    UserJson createdUser = createUser(userAnnotation);
                    setUser(createdUser);
                });
    }

    private UserJson createUser(User annotation) {
        String username = annotation.username();
        if (username.isEmpty()) {
            username = "test_user_" + UUID.randomUUID().toString().substring(0, 8);
        }

        TestData testData = new TestData(DEFAULT_PASSWORD);
        UserJson user = new UserJson(username, testData);

        // Регистрация пользователя через API
        userdataApiClient.registerUser(user);

        return user;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return createdUser().orElseThrow(() ->
                new ParameterResolutionException("No user found in context"));
    }

    public static void setUser(UserJson user) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("user", user);
    }

    public static Optional<UserJson> createdUser() {
        return Optional.ofNullable(
                TestMethodContextExtension.context().getStore(NAMESPACE).get("user", UserJson.class)
        );
    }
}
