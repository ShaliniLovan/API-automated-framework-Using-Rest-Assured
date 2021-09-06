import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;


public class Product extends extentReport {

    @Test
    public void validate_product_size() {

        ExtentTest test = er.startTest("validate_product_size()", "test started");
        given().baseUri("https://fakestoreapi.com").

                header("Content-Type", "application/json").

                when().get("/products").

                then().
                assertThat().body("size()", is(20)).log().all().statusCode(200);
        test.log(LogStatus.PASS, "Product size is 20");

        er.endTest(test);
        er.flush();
        er.close();
    }


    @Test
    public void validate_unique_id() {
        ExtentTest test = er.startTest("validate_unique_id()", "test started");
        Response response = given().
                baseUri("https://fakestoreapi.com").

                when().get("/products")

                .then().statusCode(200).extract().response();


        List<Integer> id_list = response.jsonPath().getList("id");

        int total_count = (int) id_list.stream().distinct().count();
        assertThat(20, is(equalTo(total_count)));

        test.log(LogStatus.PASS, "Each id in the response payload is unique");

        er.endTest(test);
        er.flush();
        er.close();

    }

    @Test
    public void post_call()
    {

        ExtentTest test1 = er.startTest("post_call()", "test started");


        String path = System.getProperty("user.dir");
        path = path + "\\src\\test\\resources\\Book.xlsx";

        try {
            File excelData = new File(path);
            FileInputStream fi = new FileInputStream(excelData);
            XSSFWorkbook workbook = new XSSFWorkbook(fi);
            XSSFSheet sheet = workbook.getSheetAt(0);
            JSONObject Jobj = new JSONObject();
            DataFormatter formatter = new DataFormatter();


            for (int i = 1; i <= 5; i++) {

                Cell c1 = sheet.getRow(i).getCell(0);
                int id = Integer.parseInt(formatter.formatCellValue(c1));


                Cell c2 = sheet.getRow(i).getCell(1);
                String title = formatter.formatCellValue(c2);

                Cell c3 = sheet.getRow(i).getCell(2);
                int price = Integer.parseInt(formatter.formatCellValue(c3));

                Cell c4 = sheet.getRow(i).getCell(3);
                String description = formatter.formatCellValue(c4);

                Cell c5 = sheet.getRow(i).getCell(4);
                String cdata = formatter.formatCellValue(c5);

                Cell c6 = sheet.getRow(i).getCell(5);
                String img = formatter.formatCellValue(c6);

                Jobj.put("id",id);
                Jobj.put("title",title);
                Jobj.put("price",price);
                Jobj.put("description",description);
                Jobj.put("category",cdata);
                Jobj.put("image",img);

            }
            test1.log(LogStatus.INFO, "TEST CASE PASSED");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
@Test
    public void product_Jsonschema_validation() {

        RequestSpecification req = with().baseUri("https://fakestoreapi.com").header("Content-Type", "application/json");
        Response response = req.get("/products").then().log().all().extract().response();
        response.then().body(matchesJsonSchemaInClasspath("schema_for_products.json"));

    }

}



