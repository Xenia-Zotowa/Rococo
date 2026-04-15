package model;

public class UserJson {
    private String username;
    private String password;
    private TestData testData;

    public UserJson(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TestData getTestData() {
        return testData;
    }

    public void setTestData(TestData testData) {
        this.testData = testData;
    }

    public UserJson addTestData(TestData testData) {
        this.testData = testData;
        return this;
    }
}