package ru.netology.test;

import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.*;
import ru.netology.page.LoginPage;
import ru.netology.helper.DataHelper;
import ru.netology.helper.SQLHelper;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.helper.SQLHelper.cleanAuthCodes;
import static ru.netology.helper.SQLHelper.cleanDatabase;

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
    void accountIsBlockedAfterThreeIncorrectAttempts() throws SQLException {
        SQLHelper.cleanAuthCodes();

        for (int i = 0; i < 3; i++) {

            loginPage.login(new DataHelper.AuthInfo(authInfo.getLogin(), DataHelper.generateRandomPassword()));
        }
            String actualStatus = SQLHelper.getUserStatus(authInfo.getLogin());
            assertEquals("blocked", actualStatus);
        }




    }






