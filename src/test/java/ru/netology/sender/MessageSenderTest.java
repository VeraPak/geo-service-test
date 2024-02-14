package ru.netology.sender;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;

import static org.mockito.Mockito.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.HashMap;
import java.util.Map;

class MessageSenderTest {
    static LocalizationService localizationService;
    static GeoService geoService;
    static MessageSender messageSender;

    static final String RU = "^[А-Яа-я \\p{Punct}]+$";
    static final String EN = "^[A-Za-z \\p{Punct}]+$";

    @BeforeAll
    static void beforeAll() {

        localizationService = mock(LocalizationServiceImpl.class);
        when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать!");
        when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        geoService = mock(GeoServiceImpl.class);
        when(geoService.byIp("127.0.0.1")).thenReturn(new Location(null, null, null, 0));
        when(geoService.byIp("172.0.32.11")).thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        when(geoService.byIp("96.44.183.149")).thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));
        when(geoService.byIp(ArgumentMatchers.startsWith("172."))).thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
        when(geoService.byIp(ArgumentMatchers.startsWith("96."))).thenReturn(new Location("New York", Country.USA, null, 0));

        messageSender = new MessageSenderImpl(geoService, localizationService);
    }
    @ParameterizedTest
    @CsvSource(value = {
            "172.0.32.11, " + RU + ", " + EN,
            "96.44.183.149, " + EN + "," + RU,
            "172.0.12.32, " + RU + "," + EN,
            "96.23.44.32.234, " + EN + "," + RU,
    })
    void testParams(String ip, String patternPoss, String patternNeg){
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        String result = messageSender.send(headers);

        assertThat(result, matchesPattern(patternPoss));
        assertThat(result, not(matchesPattern(patternNeg)));
    }

}