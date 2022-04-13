package GET;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static java.lang.System.getProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
public class GetMethod {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    JSONArray jsonArray;
    ExtentTest test;
    ExtentReports extent;
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
        ExtentHtmlReporter htmlReporter=new ExtentHtmlReporter("src\\test\\resources\\extentReports.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        test = extent.createTest("MyFirstTest", "Sample");
    }
    @Test(priority = 1)
    //validating the gender : male or female
    public void validateGender() throws IOException {
        Response response =
                given().
                        spec(requestSpecification).
                        when().
                        get("/users").
                        then().
                        spec(responseSpecification).statusCode(200).
                        extract().response();
        JSONObject obj = new JSONObject();
        for (int i = 0; i < obj.length(); i++) {
            assert obj.get("gender") == "male" || (obj.get("gender") == "female");
        }
    }
    @Test(priority = 2)
    //validating the domain : checking whether at least 2 mail id contains ".biz" extension
    public void domainValidation() {
        int DomainExtensionCount = 0;
        String domain_check = getProperties().getProperty(".biz");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object obj = jsonArray.getJSONObject(i).get("email");
            if (obj.toString().contains(".biz")) {
                DomainExtensionCount++;
                assertThat(DomainExtensionCount, greaterThan(2));
            }
        }
    }
@Test(priority = 3)
//validating the id values: checking whether id values are unique or not
    public void idValidation() {
        ArrayList<Integer> id_list = new ArrayList<>();
        JSONArray jsonArray=new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object obj = jsonArray.getJSONObject(i).get("id");
            int id = Integer.parseInt(obj.toString());
            int count = 0;
            if (id_list.contains(id)) {
                break;
            } else {
                id_list.add(id);
            }
            assertThat(count, equalTo(1));System.out.println("ID values are unique");
            test.pass("ID is unique");
        }
        test.fail("ID values are not unique");
        }
    @Test(priority = 4)
    //validating JSON schema
    public void validateSchema(){
        Response response = given().
                baseUri("https://gorest.co.in/public/v1").
                header("Content-Type","application/json").
                when().
                get("/users").
                then().assertThat().body(JsonSchemaValidator.
                        matchesJsonSchema(
                                new File("src/main/resources/user_schema.json")))
                .statusCode(200).contentType("application/json").extract().response();
        jsonArray = new JSONArray(response.body().jsonPath().getList("data"));System.out.println("JSON Schema is validated");
        test.pass("JSON Schema is validated");

    }

}