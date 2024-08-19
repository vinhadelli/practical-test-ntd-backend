package com.ntd.practical_test_ntd_backend.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import com.ntd.practical_test_ntd_backend.services.interfaces.IRandomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ntd.practical_test_ntd_backend.entities.Operation;
import com.ntd.practical_test_ntd_backend.entities.Record;
import com.ntd.practical_test_ntd_backend.entities.User;
import com.ntd.practical_test_ntd_backend.enums.OperationTypesEnum;
import com.ntd.practical_test_ntd_backend.exception.InsufficientBalance;
import com.ntd.practical_test_ntd_backend.exception.NetworkException;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IOperationRepository;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IRecordRepository;
import com.ntd.practical_test_ntd_backend.services.interfaces.IUserService;

@Service
public class RandomService implements IRandomService {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRecordRepository recordRepository;
    @Autowired
    private IOperationRepository operationRepository;
    @Value("${api.random.url}")
    private String apiUrl;
    @Value("${api.random.key}")
    private String apiKey;

    private int defaultTimeout = 120 * 1000; // 2 minutes
    private String possibleCharacters = "abcdefghijklmnopqrstuvwxyz";

    
    // Function to get a random generated string from Random.org.
    // Parameter: {length} - Must be within the [1, 32] range. All strings will be of the same length.
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Returns: String - The generated Random String.
    public String generateRandomString(int length, Long userId) throws NetworkException
    {
        JsonObject request = prepareRequest(length);
        
        // Send the request
        JsonObject response = new JsonObject();
        try {
            response = this.SendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch(NetworkException e)
        {
            throw e;
        }
        String result = null;
        result = getResultingString(response);
        if(result != null)
        {
            // Process the successful operation
            ProcessOperation(OperationTypesEnum.GENERATE_STRING, userId, String.format("Generated String: %s", result));
        }
        return result;
    }

    // Function to parse the response and get the returned random string.
    // Parameter: {response} - The response received from the API
    // Returns: String - The generated Random String.
    private String getResultingString(JsonObject response) {
        JsonArray data = response.get("result").getAsJsonObject().get("random").getAsJsonObject().get("data").getAsJsonArray();
		
		return data.get(0).getAsString();
    }

    // Prepare the request with the predefined parameters of the random string
    // Parameter: {length} - Must be within the [1, 32] range. All strings will be of the same length.
    // Returns: JsonObject - The request with all the necessary parameters.
    private JsonObject prepareRequest(int length)
    {
        JsonObject request = new JsonObject();
        
        // The body of the request.
        JsonObject body = new JsonObject();
		body.addProperty("n", 1);
		body.addProperty("length", length);
		body.addProperty("characters", this.possibleCharacters);
        body.addProperty("apiKey", this.apiKey);
        // The parameters of the request
		request.addProperty("jsonrpc", "2.0");
		request.addProperty("method", "generateStrings");
		request.add("params", body);
		request.addProperty("id", UUID.randomUUID().toString());
		
		return request;
    }

    // Function to send the request to de Random.org API. It creates the connection, send the request and parse the result.
    // Parameter: {request} - The request the will be sent to the Random.org API.
    // Returns: JsonObject - The JsonObject of the response from the API.
    // Throws: NetworkException - A custom exception containing the Status Code and the Error Message returned from the API.
    // Throws: MalformedURLException
    // Throws: IOException
    private JsonObject SendRequest(JsonObject request) throws NetworkException, MalformedURLException, IOException
    {
        // Prepare connection
        HttpsURLConnection connection = (HttpsURLConnection) new URL(this.apiUrl).openConnection();
        connection.setConnectTimeout(this.defaultTimeout);
        connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");

        // Send the request
        connection.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
		dos.writeBytes(request.toString());
		dos.flush();
		dos.close();

        // Gets the response code
        int responseCode = connection.getResponseCode();

        // If successful, get the resulting Json Object 
        if (responseCode == HttpsURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return JsonParser.parseString(response.toString()).getAsJsonObject();
		} 
        // If not, throw the error
        else {
			throw new NetworkException("Error " + responseCode + ": " 
						+ connection.getResponseMessage());
		}
    }

    // This function processes the operation received. It goes to the database to retrieve the correct type of operation
    // and user. It also gets the remaining balance of the user and subtracts for the cost of the operation.
    // Parameter: {type} - Type of operation.
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Parameter: {message} - Message to be stored in the record. Contains the result and the parameters.
    private void ProcessOperation(OperationTypesEnum type, Long userId, String message)
    {
        Operation operation = operationRepository.findByType(type.Value);
        User user = userService.getUser(userId);
        Double remainingBalance = userService.getUserBalance(userId) - operation.getCost();
        if(remainingBalance < 0)
            throw new InsufficientBalance();
        recordRepository.save(new Record(operation, user, operation.getCost(), remainingBalance, message));
    }
}
