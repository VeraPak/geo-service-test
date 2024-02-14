package ru.netology.geo;

import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import static org.hamcrest.MatcherAssert.*;

class GeoServiceImplTest {
    GeoService geoService = new GeoServiceImpl();


    @ParameterizedTest
    @CsvSource(value = {
            "127.0.0.1, , , , 0",
            "172.0.32.11, Moscow, RUSSIA, Lenina, 15",
            "96.44.183.149, New York, USA, 10th Avenue, 32",
            "172.1.12.44, Moscow, RUSSIA, , 0",
            "96.11.134.129, New York, USA, , 0",
    })
    void byIpTest(String ip, String city, Country country, String street, int builing) {

        Location location = geoService.byIp(ip);

        assertThat(location.getCity(), Matchers.equalTo(city));
        assertThat(location.getCountry(), Matchers.equalTo(country));
        assertThat(location.getStreet(), Matchers.equalTo(street));
        assertThat(location.getBuiling(), Matchers.equalTo(builing));
    }
}