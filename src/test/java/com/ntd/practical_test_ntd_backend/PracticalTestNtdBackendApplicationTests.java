package com.ntd.practical_test_ntd_backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.RestTemplate;

import com.ntd.practical_test_ntd_backend.dto.CalculatorDTO;
import com.ntd.practical_test_ntd_backend.dto.RecordDTO;
import com.ntd.practical_test_ntd_backend.dto.RecordRequestDTO;
import com.ntd.practical_test_ntd_backend.dto.TokenDTO;
import com.ntd.practical_test_ntd_backend.dto.UserDTO;
import com.ntd.practical_test_ntd_backend.entities.User;
import com.ntd.practical_test_ntd_backend.enums.OperationTypesEnum;
import com.ntd.practical_test_ntd_backend.entities.Record;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IOperationRepository;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IRecordRepository;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IUserRepository;
import com.ntd.practical_test_ntd_backend.services.JwtService;

@SpringBootTest
class PracticalTestNtdBackendApplicationTests {

	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IRecordRepository recordRepository;
	@Autowired
	private IOperationRepository operationRepository;
	@Autowired
	private JwtService jwtService;

	@Autowired
	private RestTemplate restTemplate;

	private User user;
	private UserDTO userDTO;

	private String token;

	private final String apiUrl = "http://localhost:8080/v1/";

	@BeforeEach
	void setup() {
		userDTO = new UserDTO("user@test.com", "password");
	}

	@AfterAll
	void teardown() {
		userRepository.delete(user);
	}

	@Test
	@Order(1)
	void createUserTest() {
		// Call Controller for createUser
		restTemplate.postForObject(apiUrl + "auth/signup", userDTO, null);
		// Check that the user was created successfully
		user = userRepository.findByUsername(userDTO.getUsername()).orElse(null);
		assertNotNull(user);
		// Check that the user has the default number of credits
		Record record = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId());
		assertNotNull(record);
		assertEquals(20.0, record.getUserBalance());
	}

	@Test
	@Order(2)
	void loginTest() {
		// Check if the login was successful
		token = restTemplate.postForObject(apiUrl + "auth/login", userDTO, TokenDTO.class).getToken();
		assertNotNull(token);
	}

	@Test
	void addTest() {
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.ADDITION.Value).getCost();
		// Make the addition
		CalculatorDTO request = new CalculatorDTO(BigDecimal.TEN, BigDecimal.TEN, OperationTypesEnum.ADDITION);
		BigDecimal result = restTemplate.postForObject(apiUrl + "calculator/add", request, BigDecimal.class);
		assertNotNull(result);
		assertEquals(BigDecimal.valueOf(20), result);
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}

	@Test
	void subtractTest() {
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.SUBTRACTION.Value).getCost();
		// Make the subtraction
		CalculatorDTO request = new CalculatorDTO(BigDecimal.TEN, BigDecimal.TEN, OperationTypesEnum.SUBTRACTION);
		BigDecimal result = restTemplate.postForObject(apiUrl + "calculator/subtract", request, BigDecimal.class);
		assertNotNull(result);
		assertEquals(BigDecimal.ZERO, result);
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}

	@Test
	void divideTest() {
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.DIVISION.Value).getCost();
		// Make the division
		CalculatorDTO request = new CalculatorDTO(BigDecimal.TEN, BigDecimal.TEN, OperationTypesEnum.DIVISION);
		BigDecimal result = restTemplate.postForObject(apiUrl + "calculator/divide", request, BigDecimal.class);
		assertNotNull(result);
		assertEquals(BigDecimal.ONE, result);
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}

	@Test
	void multiplyTest() {
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.MULTIPLICATION.Value).getCost();
		// Make the multiplication
		CalculatorDTO request = new CalculatorDTO(BigDecimal.TEN, BigDecimal.TEN, OperationTypesEnum.MULTIPLICATION);
		BigDecimal result = restTemplate.postForObject(apiUrl + "calculator/divide", request, BigDecimal.class);
		assertNotNull(result);
		assertEquals(BigDecimal.ONE, result);
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}

	@Test
	void generateStringTest() {
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.GENERATE_STRING.Value).getCost();
		// Generate the string and check if the length is correct
		int length = 5;
		String result = restTemplate.getForObject(apiUrl + "random/" + length, String.class);
		assertNotNull(result);
		assertEquals(length, result.length());
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}

	@Test
	void getRecordListTest() {
		// Request object
		RecordRequestDTO recordRequestDTO = new RecordRequestDTO();
		recordRequestDTO.setPageNumber(0);
		recordRequestDTO.setPageItemCount(10);
		// Get a Page of 10 records
		Page<RecordDTO> recordPage = restTemplate.postForObject(apiUrl + "record", recordRequestDTO, Page.class);
		// Check if not null and if there is at least one record, the added credits when
		// the user was created
		assertNotNull(recordPage);
		assertTrue(recordPage.getContent().size() > 0);
	}

	@Test
	void deleteRecordTest() {
		// Request object
		RecordRequestDTO recordRequestDTO = new RecordRequestDTO();
		recordRequestDTO.setPageNumber(0);
		recordRequestDTO.setPageItemCount(10);
		// Get a Page of 10 records
		Page<RecordDTO> recordPage = restTemplate.postForObject(apiUrl + "record", recordRequestDTO, Page.class);
	}

}
