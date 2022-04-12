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
import java.util.ArrayList;
import java.util.HashMap;
public class Create {
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
        reqBuilder.setBaseUri(baseUrl).addHeader("Content-Type", "application/json");
        requestSpecification = RestAssured.with().spec(reqBuilder.build());
        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder().expectContentType(ContentType.JSON);
        responseSpecification = resBuilder.build();
    }
    @Test(priority = 1)
    public void register() throws Exception {
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
    @Test(priority = 2)
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
        String msg = j.getString("data.message");
        System.out.println("Message: " + msg);
    }
}