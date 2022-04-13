package POST;
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
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    @BeforeTest
    void init() {
        RestAssured.useRelaxedHTTPSValidation();
    }
    @BeforeClass
    public void setUp() {
        String baseUrl = "https://gorest.co.in/public/v1";
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri(baseUrl).addHeader("Content-Type", "application/json;charset=utf-8");
        requestSpecification = RestAssured.with().spec(reqBuilder.build());
        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder().expectContentType(ContentType.JSON);
        responseSpecification = resBuilder.build();
    }
    @Test(priority = 1)
    public void postUser() throws Exception {
        ArrayList al;
        ExcelTest et = new ExcelTest();
        al = et.readCells();
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
         Assert.assertEquals(response.statusCode(), 201);
        System.out.println("Success!! Status Code is " + response.getStatusCode());
        ResponseBody body = response.getBody();
        System.out.println("Response Body is: " + body.asString());
    }
    @Test(priority=2)
    private void validateResponse() throws IOException {
        ArrayList al;
        ExcelTest et = new ExcelTest();
        al = et.readCells();
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
        boolean name_validation = response.body().jsonPath().getString("data.name").equals(al.get(0));
        boolean email_validation = response.body().jsonPath().getString("data.gender").equals(al.get(1));
        boolean gender_validation = response.body().jsonPath().getString("data.email").equals(al.get(2));
        boolean status_validation = response.body().jsonPath().getString("data.status").equals(al.get(3));
        try {

            assertThat(name_validation, is(equals(al.get(0))));
            assertThat(email_validation, is(equals(al.get(1))));
            assertThat(gender_validation, is(equals(al.get(2))));
            assertThat(status_validation, is(equals(al.get(3))));

        }catch (AssertionError assertionError){
            assertThat(name_validation, is(equals(al.get(0))));
            assertThat(email_validation, is(equals(al.get(1))));
            assertThat(gender_validation, is(equals(al.get(2))));
            assertThat(status_validation, is(equals(al.get(3))));
        }
    }
    @Test(priority = 3)
    public void validateError() throws Exception {
        ArrayList al;
        ExcelTest et = new ExcelTest();
        al = et.readCells();
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
        JsonPath j = new JsonPath(response.asString());
        String errormsg = j.getString("data.message");
        System.out.println("Message: " + errormsg);
    }
}