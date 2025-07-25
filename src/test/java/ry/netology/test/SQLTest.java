package ry.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.page.LoginPage;
import ry.netology.helper.DataHelper;
import ry.netology.helper.SQLHelper;

import static com.codeborne.selenide.Selenide.open;
import static ry.netology.helper.SQLHelper.cleanAuthCodes;
import static ry.netology.helper.SQLHelper.cleanDatabase;

public class SQLTest {
    LoginPage loginPage;
    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfoWithTestData();

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    void successfulLoginToYourPersonalAccount() {
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void accountIsBlockedAfterThreeIncorrectAttempts() {
        DataHelper.AuthInfo randomUser = DataHelper.generateRandomUser();
        for(int i =0; i<3; i++) {
            loginPage.login(randomUser);
            loginPage.ErrorNotification("Ошибка! Неверно указан логин или пароль");

            loginPage.login(randomUser);
            loginPage.ErrorNotification("Ошибка! Неверно указан логин или пароль");
        }
    }
}
