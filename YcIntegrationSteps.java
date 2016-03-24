package com.b.jbehavetest.spring;

import org.apache.log4j.Logger;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.Steps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;



import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.sql.DataSource;


@Component
public class YcIntegrationSteps{
	
	private final static Logger log = Logger.getLogger(YcIntegrationSteps.class);

//	@Autowired
//	YieldCurveIntegrationTest yieldCurveIntegrationTest;
	//===============================================================================
	@Autowired
	private DataSource dataSource;
	
	private BigDecimal dodThreshold;

	@Given("a Bond with a threshold setting for DoD $dodThreshold") // 0.1
	public void setThresold(@Named("dodThreshold") BigDecimal dodThreshold) {
		this.dodThreshold = dodThreshold;
		try {
//			if(dataSource!=null){
				dataSource.getConnection();
				System.out.println("===================>"+dataSource.toString());
//			}else{
//				System.out.println("===================>");
//			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//invoke original integration test
//		try {
//			
//			yieldCurveIntegrationTest.cleanUp();
//			yieldCurveIntegrationTest.testYieldCurve();
//		} catch (Exception e) {
//			log.error(e);
//			e.printStackTrace();
//		}
		
		System.out.println("setThresold ==> " + dodThreshold);
	}

	//===============================================================================
	
	private BigDecimal yesterdayPrice;
	private BigDecimal todayPrice;

	@When("1, BondPrice for yesterday is $yesterdayPrice and today is $todayPrice") 
	public void setPrice1(@Named("yesterdayPrice") BigDecimal yesterdayPrice,
			@Named("todayPrice") BigDecimal todayPrice) {
		this.yesterdayPrice = yesterdayPrice;
		this.todayPrice = todayPrice;
		System.out.println("setPrice ==> " + yesterdayPrice + " " + todayPrice);
	}

	@Then("1, alert should be $result") // 10
	public void calc1(@Named("result") BigDecimal  result) {
		assertThat(result, is(todayPrice.subtract(yesterdayPrice)));
	}

	//===============================================================================
	
	@When("2, BondPrice for yesterday is $yesterdayPrice and today is $todayPrice") 
	public void setPrice2(@Named("yesterdayPrice") BigDecimal yesterdayPrice,
			@Named("todayPrice") BigDecimal todayPrice) {
		this.yesterdayPrice = yesterdayPrice;
		this.todayPrice = todayPrice;
		System.out.println("setPrice ==> " + yesterdayPrice + " " + todayPrice);
	}

	@Then("2, alert should be '$result'")
	public void calc2(@Named("result") String  result) {
		assertThat(result, equalTo("There is no alert"));
	}

	//===============================================================================
	
	@When("3, BondPrice for yesterday is $yesterdayPrice and today is $todayPrice") 
	public void setPrice3(@Named("yesterdayPrice") BigDecimal yesterdayPrice,
			@Named("todayPrice") BigDecimal todayPrice) {
		this.yesterdayPrice = yesterdayPrice;
		this.todayPrice = todayPrice;
		System.out.println("setPrice ==> " + yesterdayPrice + " " + todayPrice);
	}

	@Then("3, alert should be '$result'")
	public void calc3(@Named("result") String  result) {
		assertThat(result, equalTo("there is no alert"));
	}

	
}
