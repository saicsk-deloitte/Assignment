package POST;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PostUser {
    //declaring request specification -to specify how request looks like
    RequestSpecification requestSpecification;
    //response specification - common response for multiple tests from body
    ResponseSpecification responseSpecification;
    ExtentTest test;
    ExtentReports extent;
    @BeforeTest
    void init() {
        RestAssured.useRelaxedHTTPSValidation();
    }
    @BeforeClass
    public ExtentHtmlReporter setUp() throws IOException {
        ArrayList al;
        ExcelTest et = new ExcelTest();
        al = et.readUser();
        System.out.println(al);
        HashMap hash = new HashMap();
        hash.put("name", al.get(0));
        hash.put("gender", al.get(1));
        hash.put("email", al.get(2));
        hash.put("status", al.get(3));
        String baseUrl = "https://gorest.co.in/public/v1";
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri(baseUrl).addHeader("Content-Type", "application/json;charset=utf-8");
        requestSpecification = RestAssured.with().spec(reqBuilder.build());
        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder().expectContentType(ContentType.JSON);
        responseSpecification = resBuilder.build();
        //preparing html report : using Extent Reports class
        ExtentHtmlReporter htmlReporter=new ExtentHtmlReporter("src\\test\\resources\\extentReports.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        test = extent.createTest("MyFirstTest", "Sample");
        return htmlReporter;
    }
    @Test(priority = 1)
    //posting the User Details using POST Method
    public void postUser() throws Exception {
        ArrayList al;
        ExcelTest et = new ExcelTest();
        al = et.readUser();
        System.out.println(al);
        HashMap hash = new HashMap();
        hash.put("name", al.get(0));
        hash.put("gender", al.get(1));
        hash.put("email", al.get(2));
        hash.put("status", al.get(3));
        Response response = requestSpecification.given().header("Authorization", "Bearer " + "1e3fbfc04d510423f16db6b1976fb8d49a339e4e486ef5006ebe118b552a2a32")
                .body(hash).
                when().
                post("/users").
                then().spec(responseSpecification).extract().response();
        try {
            Assert.assertEquals(response.statusCode(), 201);
            System.out.println("User Details Posted Successfully!!");
            test.pass("Success!! User Details Posted Successfully");
        }
        catch (Exception e){
            test.fail("Posting user details failed");
            System.out.println("Posting user details failed");

        }
        System.out.println("Success!! Status Code is " + response.getStatusCode());
        ResponseBody body = response.getBody();
        System.out.println("Response Body is: " + body.asString());
    }
    @Test(priority=2)
    //Validating the response after fetching data from Apache POI
    private void validateResponse() throws IOException {
        ArrayList al;
        ExcelTest et = new ExcelTest();
        al = et.readUser();
        HashMap hash = new HashMap();
        hash.put("name", al.get(0));
        hash.put("gender", al.get(1));
        hash.put("email", al.get(2));
        hash.put("status", al.get(3));
                 Response response=requestSpecification.given().header("Authorization", "Bearer " + "1e3fbfc04d510423f16db6b1976fb8d49a339e4e486ef5006ebe118b552a2a32")
                .body(hash).
                when().
                post("/users").
                then().spec(responseSpecification).extract().response();
        boolean validate_name = response.body().jsonPath().getString("data.name").equals(al.get(0));
        boolean validate_gender = response.body().jsonPath().getString("data.gender").equals(al.get(1));
        boolean validate_email = response.body().jsonPath().getString("data.email").equals(al.get(2));
        boolean validate_status= response.body().jsonPath().getString("data.status").equals(al.get(3));
        try {
            assertThat(validate_name, is(equals(al.get(0))));
            assertThat(validate_gender, is(equals(al.get(1))));
            assertThat(validate_email, is(equals(al.get(2))));
            assertThat(validate_status, is(equals(al.get(3))));

        }
        catch (AssertionError assertionError){
            assertThat(validate_name, is(equals(al.get(0))));
            assertThat(validate_gender, is(equals(al.get(1))));
            assertThat(validate_email, is(equals(al.get(2))));
            assertThat(validate_status, is(equals(al.get(3))));
        }
    }
    @Test(priority = 3)
    //Validating the error message when API is called with the existing data
    public void validateError() throws Exception {
        ArrayList al;
        ExcelTest et = new ExcelTest();
        al = et.readUser();
        System.out.println(al);
        HashMap hash = new HashMap();
        hash.put("name", al.get(0));
        hash.put("gender", al.get(1));
        hash.put("email", al.get(2));
        hash.put("status", al.get(3));
              Response response = requestSpecification.given().header("Authorization", "Bearer " + "1e3fbfc04d510423f16db6b1976fb8d49a339e4e486ef5006ebe118b552a2a32")
                .body(hash).
                when().
                post("/users").
                then().spec(responseSpecification).extract().response();
        System.out.println("Error Code is " + response.getStatusCode());
        //using Json Path for reading the "message" key/field in JSON Body
        JsonPath j = new JsonPath(response.asString());
        //"message" is in the Nested array (JSON Array) of data so using "data.message" to get Json Path & message
        String errorMsg = j.getString("data.message");
        System.out.println("Message: " + errorMsg);
    }
}