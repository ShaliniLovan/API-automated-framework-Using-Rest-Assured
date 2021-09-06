import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Carts extends extentReport {


    @Test
    public void get_schema_validation() {

        ExtentTest test = er.startTest("get_schema_validation() ", "schema validation starts");

        given().
                baseUri("https://fakestoreapi.com").

                header("Content-Type", "application/json").

                when().
                get("/carts").

                then().

                body(matchesJsonSchemaInClasspath("schema_for_cart.json")).statusCode(200);

        test.log(LogStatus.PASS, "Response code" + expect().statusCode(200));

        er.endTest(test);
        er.flush();
        er.close();

    }

    @Test
    public void product_qnty() {
        ExtentTest test = er.startTest("product_qnty() ", "test  starts");

        Response response = given().
                baseUri("https://fakestoreapi.com").
                header("Content-Type", "application/json").


                when().get("/carts").then().statusCode(200).extract().response();


        JSONArray arr = new JSONArray(response.asString());
        for (int i = 0; i < arr.length(); i++) {
            JSONArray OBJ = arr.getJSONObject(i).getJSONArray("products");

            for (int k = 0; k < OBJ.length(); i++) {
                JSONObject obj = OBJ.getJSONObject(k);

                assertThat(obj, is(notNullValue()));
                assertThat(response.statusCode(), is(equalTo(200)));
                test.log(LogStatus.PASS, "test case passed");
            }

        }
    }
}