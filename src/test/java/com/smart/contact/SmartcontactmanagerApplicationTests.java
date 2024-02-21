package com.smart.contact;

import org.assertj.core.api.AbstractBigDecimalAssert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


//@SpringBootTest
class SmartcontactmanagerApplicationTests {
private Calculator calculator=new Calculator();
	@Test
	void contextLoads() {
	}

	@Test
	//@Disabled
	void testSum(){

		int expectedResult=6;

		int actualResult=calculator.doSum(1,2,3);

		assertThat(actualResult).isEqualTo(expectedResult);

	}
@Test
	void testProduct(){
		int expectedResult=6;

		int actualResult=calculator.doMul(2,3);

		assertThat(actualResult).isEqualTo(expectedResult);
	}

@Test
	void compareNum(){

		boolean actualResult=calculator.isSame(5,5);
		assertThat(actualResult).isTrue();
}




}
