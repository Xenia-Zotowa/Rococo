package utils;

import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepLogger {
    private static final Logger logger = LoggerFactory.getLogger(StepLogger.class);

    @Step("{description}")
    public static void logStep(String description, Object... args) {
        String formattedMessage = String.format(description, args);
        logger.info("STEP: {}", formattedMessage);
    }

    @Step("Выполнение действия: {action}")
    public static void logAction(String action) {
        logger.info("ACTION: {}", action);
    }

    @Step("Проверка: {check}")
    public static void logCheck(String check) {
        logger.info("CHECK: {}", check);
    }

    @Step("Получение данных: {data}")
    public static void logData(String data) {
        logger.info("DATA: {}", data);
    }
}