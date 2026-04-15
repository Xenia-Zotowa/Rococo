//package jupiter.extension;
//
//import jupiter.annotation.meta.User;
//
//import model.TestData;
//import model.UserJson;
//import org.junit.jupiter.api.extension.BeforeEachCallback;
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.api.extension.ParameterContext;
//import org.junit.jupiter.api.extension.ParameterResolutionException;
//import org.junit.jupiter.api.extension.ParameterResolver;
//import org.junit.platform.commons.support.AnnotationSupport;
//import service.AuthClient;
//import utils.RandomDataUtils;
//
//import javax.annotation.ParametersAreNonnullByDefault;
//import java.util.Optional;
//
//@ParametersAreNonnullByDefault
//public class UserExtension implements BeforeEachCallback, ParameterResolver {
//
//    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
//
//    private UserJson createdUser;
////    private final AuthClient authClient = AuthClient.getInstance();
//
//    @Override
//    public void beforeEach(ExtensionContext context) {
//        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
//                .ifPresent(userAnno -> {
//                    String username = userAnno.username();
//                    String password = userAnno.password();
//
//                    // Если username = "admin" - используем существующего
//                    if ("admin".equals(username)) {
//                        createdUser = new UserJson(username, password);
//                        TestData testData = new TestData(password);
//                        createdUser.setTestData(testData);
//                    }
//                    // Если username пустой - генерируем нового
//                    else if (username == null || username.isEmpty()) {
//                        String generatedUsername = RandomDataUtils.randomUsername();
//                        createdUser = authClient.registerUser(generatedUsername, password);
//                        TestData testData = new TestData(password);
//                        createdUser.setTestData(testData);
//                    }
//                    // Иначе создаём пользователя с указанным именем
//                    else {
//                        createdUser = authClient.registerUser(username, password);
//                        TestData testData = new TestData(password);
//                        createdUser.setTestData(testData);
//                    }
//
//                    // Сохраняем в контекст
//                    context.getStore(NAMESPACE).put(context.getUniqueId(), createdUser);
//                });
//    }
//
//    @Override
//    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
//        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
//    }
//
//    @Override
//    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
//        return extensionContext.getStore(NAMESPACE)
//                .get(extensionContext.getUniqueId(), UserJson.class);
//    }
//}