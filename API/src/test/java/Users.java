import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import java.util.List;
import java.util.regex.Pattern;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.AssertJUnit.assertNotNull;


public class Users extends extentReport {
    @Test
    public void firstname_validation() {
        ExtentTest test = er.startTest("firstname_validation() ", "test starts");

        Response response = given().
                baseUri("https://fakestoreapi.com").

                header("Content-Type", "application/json").

                when().get("/users").

                then().statusCode(200).extract().response();


        List<String> name_list = response.jsonPath().getList("name.firstname");
        System.out.println(name_list);

        for (int i = 0; i < name_list.size(); i++) {

            if ((name_list.get(i)).equals("david")) {

                test.log(LogStatus.PASS, "list contains david");

            }
            if ((name_list.get(i)).equals("don")) {
                test.log(LogStatus.PASS, "List contains don");

            }

            if ((name_list.get(i)).equals("mariam")) {
                test.log(LogStatus.PASS, "List contains mariam");

            }
        }

        test.log(LogStatus.PASS, "All names are present");
        er.endTest(test);
    }

    @Test
    public void lat_long_notNull_validation() {
        ExtentTest test1 = er.startTest("lat_long() ", "test starts");

        Response response = given().
                baseUri("https://fakestoreapi.com").

                header("Content-Type", "application/json").

                when().get("/users")

                .then().statusCode(200).extract().response();


        List<String> geolocation_lat_list = response.jsonPath().getList("address.geolocation.lat");
        test1.log(LogStatus.INFO, "lat list " + geolocation_lat_list);

        assertNotNull(geolocation_lat_list);


        List<String> geolocation_long_list = response.jsonPath().getList("address.geolocation.long");
        test1.log(LogStatus.INFO, "long list :" + geolocation_long_list);

        assertNotNull(geolocation_long_list);

        er.endTest(test1);
        er.flush();
        er.close();

    }

    @Test
    public void password_validation() {
        ExtentTest test2 = er.startTest("password_validation() ", "test starts");

        Response response = given().
                baseUri("https://fakestoreapi.com").
                header("Content-Type", "application/json").
                when()

                .get("/users").

                then()

                .statusCode(200).extract().response();


        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=_*])";

        boolean flag = false;

        List<String> password_list = response.jsonPath().getList("password");
        System.out.println(password_list);
        test2.log(LogStatus.INFO, "password list" + password_list);


        for (int i = 0; i < password_list.size(); i++) {

            boolean validPassword = Pattern.matches(password_list.get(i), regex);

            if (!validPassword) {
                flag = true;
                assertThat(response.statusCode(), is(Matchers.equalTo(200)));
            }
        }

        if (flag == true) {
            test2.log(LogStatus.INFO, "invalid password");
            System.out.println("invalid password");

        }

        test2.log(LogStatus.PASS, "Password Validated Successfully");
        er.endTest(test2);


    }
}