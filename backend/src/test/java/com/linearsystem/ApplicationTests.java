package com.linearsystem;

import com.linearsystem.service.LinearSystemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//@SpringBootTest
//@RunWith
@RunWith(JUnit4.class)
class ApplicationTests {

//	@Autowired
//	private  LinearSystemService linearSystemService;
//	@Test
//	void contextLoads() {
//
//		linearSystemService.teste();
//	}
	@Test
	void contextLoads() {

		LinearSystemServiceImpl linearSystemService = new LinearSystemServiceImpl();
//		linearSystemService.calculate(matriz);
	}

}
