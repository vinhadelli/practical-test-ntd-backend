package com.ntd.practical_test_ntd_backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import com.ntd.practical_test_ntd_backend.services.RecordService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PracticalTestNtdBackendApplicationTests {

	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IRecordRepository recordRepository;
	@Autowired
	private IOperationRepository operationRepository;
	@Autowired
	private RecordService recordService;

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port = 8080;

	private UserDTO userDTO;

	private final String apiUrl = "http://localhost:"+port+"/v1/";

	@BeforeEach
	void setup() {
		this.userDTO = new UserDTO("user@test.com", "password");
	}

    private String login()
	{
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json");
		HttpEntity<UserDTO> entity = new HttpEntity<>(this.userDTO, httpHeaders);
		ResponseEntity<TokenDTO> response = restTemplate.exchange(this.apiUrl + "auth/login", HttpMethod.POST,entity, TokenDTO.class);
		TokenDTO result = response.getBody();
		if(result != null)
			return result.getToken();
		else
			return "";
	}
    private User getUser()
	{
		return userRepository.findByUsername(this.userDTO.getUsername()).orElse(null);
	}

	// Was an after all, but the @AfterAll notation has to be static, and the repository is not.
	@Test
	@Order(12)
	@Transactional
    void deleteUser() {
		User user = getUser();
		assertNotNull(user);
		recordRepository.deleteByUser(user.getId());
		userRepository.delete(user);
		User checkIfDeleted = userRepository.findById(user.getId()).orElse(null);
		assertNull(checkIfDeleted);
	}

	@Test
	@Order(1)
	void createUserTest() {
		// Call Controller for createUser
		restTemplate.postForObject(this.apiUrl + "auth/signup", this.userDTO, String.class);
		// Check that the user was created successfully
		User user = userRepository.findByUsername(this.userDTO.getUsername()).orElse(null);
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
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json");
		HttpEntity<UserDTO> entity = new HttpEntity<>(this.userDTO, httpHeaders);
		ResponseEntity<TokenDTO> response = restTemplate.exchange(this.apiUrl + "auth/login", HttpMethod.POST,entity, TokenDTO.class);
		TokenDTO result = response.getBody();
		assertNotNull(result);
		String token = result.getToken();
		assertNotNull(token);
	}

	@Test
	@Order(3)
	void getUserBalanceTest()
	{
		User user = getUser();
		String token = login();
		// Check if logged in
		assertNotNull(user);
		assertNotNull(token);
		// Get user balance from the repository
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		// Make the request
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + token);
		httpHeaders.set("Content-Type", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<Double> response = restTemplate.exchange(this.apiUrl + "user/balance", HttpMethod.GET,entity, Double.class);
		// Check if the response completed successfully and get the result
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Double result = response.getBody();
		// Compare the result with the balance from the repository
		assertNotNull(result);
		assertEquals(userBalance, result);
	}

	@Test
	@Order(4)
	void addTest() {
		User user = getUser();
		String token = login();
		// Check if logged in
		assertNotNull(user);
		assertNotNull(token);
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.ADDITION.Value).getCost();
		// Make the request
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json");
		httpHeaders.set("Authorization", "Bearer " + token);
		CalculatorDTO request = new CalculatorDTO(BigDecimal.TEN, BigDecimal.TEN, OperationTypesEnum.ADDITION);
		HttpEntity<CalculatorDTO> entity = new HttpEntity<>(request, httpHeaders);
		ResponseEntity<BigDecimal> response = restTemplate.exchange(this.apiUrl + "calculator/add", HttpMethod.POST,entity, BigDecimal.class);
		// Check if the response completed successfully and get the result
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		BigDecimal result = response.getBody();
		// Check the result
		assertNotNull(result);
		assertEquals(BigDecimal.valueOf(20), result);
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}

	@Test
	@Order(5)
	void subtractTest() {
		User user = getUser();
		String token = login();
		// Check if logged in
		assertNotNull(user);
		assertNotNull(token);
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.SUBTRACTION.Value).getCost();
		// Make the request
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + token);
		httpHeaders.set("Content-Type", "application/json");
		CalculatorDTO request = new CalculatorDTO(BigDecimal.TEN, BigDecimal.TEN, OperationTypesEnum.SUBTRACTION);
		HttpEntity<CalculatorDTO> entity = new HttpEntity<>(request, httpHeaders);
		ResponseEntity<BigDecimal> response = restTemplate.exchange(this.apiUrl + "calculator/subtract", HttpMethod.POST,entity, BigDecimal.class);
		// Check if the response completed successfully and get the result
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		BigDecimal result = response.getBody();
		// Check the result
		assertNotNull(result);
		assertEquals(BigDecimal.ZERO, result);
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}

	@Test
	@Order(6)
	void divideTest() {
		User user = getUser();
		String token = login();
		// Check if logged in
		assertNotNull(user);
		assertNotNull(token);
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.DIVISION.Value).getCost();
		// Make the request
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + token);
		httpHeaders.set("Content-Type", "application/json");
		CalculatorDTO request = new CalculatorDTO(BigDecimal.TEN, BigDecimal.TEN, OperationTypesEnum.DIVISION);
		HttpEntity<CalculatorDTO> entity = new HttpEntity<>(request, httpHeaders);
		ResponseEntity<BigDecimal> response = restTemplate.exchange(this.apiUrl + "calculator/divide", HttpMethod.POST,entity, BigDecimal.class);
		// Check if the response completed successfully and get the result
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		BigDecimal result = response.getBody();
		// Check the result
		assertNotNull(result);
		assertEquals(BigDecimal.ONE, result);
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}

	@Test
	@Order(7)
	void multiplyTest() {
		User user = getUser();
		String token = login();
		// Check if logged in
		assertNotNull(user);
		assertNotNull(token);
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.MULTIPLICATION.Value).getCost();
		// Make the request
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + token);
		httpHeaders.set("Content-Type", "application/json");
		CalculatorDTO request = new CalculatorDTO(BigDecimal.TEN, BigDecimal.TEN, OperationTypesEnum.MULTIPLICATION);
		HttpEntity<CalculatorDTO> entity = new HttpEntity<>(request, httpHeaders);
		ResponseEntity<BigDecimal> response = restTemplate.exchange(this.apiUrl + "calculator/multiply", HttpMethod.POST,entity, BigDecimal.class);
		// Check if the response completed successfully and get the result
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		BigDecimal result = response.getBody();
		// Check the result
		assertNotNull(result);
		assertEquals(BigDecimal.valueOf(100), result);
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}
	@Test
	@Order(8)
	void squareRootTest() {
		User user = getUser();
		String token = login();
		// Check if logged in
		assertNotNull(user);
		assertNotNull(token);
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.SQUARE_ROOT.Value).getCost();
		// Make the request
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + token);
		httpHeaders.set("Content-Type", "application/json");
		CalculatorDTO request = new CalculatorDTO(BigDecimal.valueOf(25), null, OperationTypesEnum.SQUARE_ROOT);
		HttpEntity<CalculatorDTO> entity = new HttpEntity<>(request, httpHeaders);
		ResponseEntity<BigDecimal> response = restTemplate.exchange(this.apiUrl + "calculator/squareroot", HttpMethod.POST,entity, BigDecimal.class);
		// Check if the response completed successfully and get the result
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		BigDecimal result = response.getBody();
		// Check the result
		assertNotNull(result);
		assertEquals(BigDecimal.valueOf(5), result);
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}

	@Test
	@Order(9)
	void generateStringTest() {
		User user = getUser();
		String token = login();
		// Check if logged in
		assertNotNull(user);
		assertNotNull(token);
		// Get user balance and cost of Operation
		Double userBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		Double operationCost = operationRepository.findByType(OperationTypesEnum.GENERATE_STRING.Value).getCost();
		// Make the request
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + token);
		httpHeaders.set("Content-Type", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
		int length = 5;
		ResponseEntity<String> response = restTemplate.exchange(this.apiUrl + "random/"+length, HttpMethod.GET,entity, String.class);
		// Check if the response completed successfully and get the result
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		String result = response.getBody();
		// Check the result
		assertNotNull(result);
		assertEquals(length, result.length());
		// Check if user balance was debited
		Double newUserBalance = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId()).getUserBalance();
		assertEquals(userBalance - operationCost, newUserBalance);
	}

	@Test
	@Order(10)
	@Transactional
	// Testing the Service because the TestRestTemplate was having issues with the Page<RecordDTO> Serialization
	void getRecordListTest() {
		User user = getUser();
		// Check if logged in
		assertNotNull(user);
		// Request object
		RecordRequestDTO recordRequestDTO = new RecordRequestDTO();
		recordRequestDTO.setPageNumber(0);
		recordRequestDTO.setPageItemCount(10);

		// Calling the service just like the controller does
		Page<RecordDTO> recordPage = recordService.getUserRecords(user.getId(), recordRequestDTO.getPageNumber(), recordRequestDTO.getPageItemCount(), recordRequestDTO.getSortBy(), recordRequestDTO.getSortDirection(), recordRequestDTO.getSearchResult(), recordRequestDTO.getOperationType());

		// Check if not null and if there is at least one record, the added credits when
		// the user was created
		assertNotNull(recordPage);
        assertFalse(recordPage.getContent().isEmpty());
	}

	@Test
	@Order(11)
	void deleteRecordTest() {
		User user = getUser();
		String token = login();
		// Check if logged in
		assertNotNull(user);
		assertNotNull(token);
		// Get Last Record From the User
		Record record = recordRepository.findTopByUserIdOrderByCreationDateDesc(user.getId());
		// Make the request
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + token);
		httpHeaders.set("Content-Type", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
		ResponseEntity<String> response = restTemplate.exchange(this.apiUrl + "record/"+record.getId(), HttpMethod.DELETE,entity, String.class);
		// Check if the response completed successfully
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		// Check if the record was deleted
		Record deletedRecord = recordRepository.findById(record.getId()).orElse(null);
		assertNotNull(deletedRecord);
		assertNotNull(deletedRecord.getDeletionDate());
	}
}
