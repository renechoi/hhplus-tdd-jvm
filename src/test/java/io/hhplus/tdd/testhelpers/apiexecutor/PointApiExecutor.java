package io.hhplus.tdd.testhelpers.apiexecutor;

import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.hhplus.tdd.point.util.YmlLoader;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class PointApiExecutor extends AbstractRequestExecutor{
	private static final String CONTEXT_PATH = YmlLoader.ymlLoader().getContextPath();

	private static final String URL_PATH = CONTEXT_PATH + "/point";

	private static RequestSpecification getRequestSpecification(int port){
		return given().log().all().port(port).contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> chargePoint(long id, long amount){
		return doPatch(getRequestSpecification(DynamicPortHolder.getPort()), URL_PATH + "/" + id + "/charge", amount);
	}

	public static ExtractableResponse<Response> chargePointWithOk(long id, long amount){
		return doPatchWithOk(getRequestSpecification(DynamicPortHolder.getPort()), URL_PATH + "/" + id + "/charge", amount);
	}


}
