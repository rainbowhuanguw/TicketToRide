package city;

public class City {
    private String cityName;

    public City(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public boolean equals(City other) {
        return (cityName.equals(other.getCityName()));
    }
}
