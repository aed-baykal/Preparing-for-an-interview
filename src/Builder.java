public class Builder {
    private final Person person = new Person();

    public Builder firstName(String firstName) {
        person.setFirstName(firstName);
        return this;
    }

    public Builder lastName(String lastName) {
        person.setLastName(lastName);
        return this;
    }

    public Builder middleName(String middleName) {
        person.setMiddleName(middleName);
        return this;
    }

    public Builder country(String country) {
        person.setCountry(country);
        return this;
    }

    public Builder address(String address) {
        person.setAddress(address);
        return this;
    }

    public Builder phone(String phone) {
        person.setPhone(phone);
        return this;
    }

    public Builder age(int age) {
        person.setAge(age);
        return this;
    }

    public Builder gender(String gender) {
        person.setGender(gender);
        return this;
    }

    public Person build() {
        return person;
    }

}
