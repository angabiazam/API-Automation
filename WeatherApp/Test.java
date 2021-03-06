package restAssuredTest;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HourlyWeatherForecastTest 
{
	@Test
	public void getdetails()
	{
		Response res =
				
		given()

		.when()
			.get("https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22")
			
		.then()
			.statusCode(200)
			
			.statusLine("HTTP/1.1 200 OK")
			
			
			.extract().response();
		
		Set<String> set_days = new HashSet<String>();
		Set<String> set_hours = new HashSet<String>();
		List<String> jsonString =  res.jsonPath().getList("list.dt_txt");
		
		//No of days response contains - Question 1
		for(String i : jsonString)
		{
			set_hours.add(i.substring(11,13));
			set_days.add(i.substring(8, 10));
  		
		}
		System.out.println("Days : " +set_days);
		if(set_days.size() == 4)
		{
			System.out.println("Response contains data of 4 days");
		}
		else
		{
			System.out.println("Response contains data of "+set_days.size()+" days");
		}
		
		//forecast in hourly interval - Question 2 
		System.out.println("Hours : "+set_hours);
		int count = 0;
		for(String j : set_hours)
		{
			if(Integer.parseInt(j) >=0 && Integer.parseInt(j) <24)
			{
				count += 1 ;
			}
		}
		if(count == 24 )
		{
			System.out.println("All forecast are in hourly interval !!");
		}
		else
		{
			System.out.println("All forecast are not in hourly interval !!");
		}
		
//		List<Object> temp =  res.jsonPath().getList("list.main.temp");
	
		//Min & Max temperature - Question 3
		for(int k = 0 ; k < jsonString.size(); k++)
		{
			Float temp = res.jsonPath().getFloat("list["+k+"].main.temp");
			Float min_temp = res.jsonPath().getFloat("list["+k+"].main.temp_min");
			Float max_temp = res.jsonPath().getFloat("list["+k+"].main.temp_max");
			if(temp >= min_temp && temp <= max_temp)
			{
				System.out.println("Temperature is not less than min temp & not more than max temp. ");
			}
			else
			{
				System.out.println("Temperature is less than min temp or  more than max temp. ");
			}
		}
		
		// weather id - Question 4 & 5
		for(int k = 0 ; k < jsonString.size(); k++)
		{
			Integer weather_id = res.jsonPath().getInt("list["+k+"].weather[0].id");
			if(weather_id == 500)
			{
				String description = res.jsonPath().getString("list["+k+"].weather[0].description");
				if(description.equalsIgnoreCase("light rain"))
				{
					System.out.println("Correct Weather details :" +description);
				}
				else
				{
					System.out.println("Incorrect Weather details :"+description);
				}
			}
			else if(weather_id == 800)
			{
				String description = res.jsonPath().getString("list["+k+"].weather[0].description");
				if(description.equalsIgnoreCase("clear sky"))
				{
					System.out.println("Correct Weather details :" +description);
				}
				else
				{
					System.out.println("Incorrect Weather details :"+description);
				}
			}
		
		}
	}
}
