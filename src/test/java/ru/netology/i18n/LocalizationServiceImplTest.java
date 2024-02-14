package ru.netology.i18n;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.entity.Country;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class LocalizationServiceImplTest {

    static LocalizationService localizationService;

    @BeforeAll
    static void beforeAll(){
        localizationService = new LocalizationServiceImpl();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "RUSSIA, Добро пожаловать",
            "USA, Welcome",
            "BRAZIL, Welcome",
            "GERMANY, Welcome",
    })
    void localeTest(Country country, String greetingExpected) {
        String greetingRes = localizationService.locale(country);
        assertThat(greetingRes, equalTo(greetingExpected));
    }
}