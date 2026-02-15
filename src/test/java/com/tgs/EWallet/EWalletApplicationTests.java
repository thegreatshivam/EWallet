package com.tgs.EWallet; // Ensure this matches your package name!

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EWalletApplicationTests {

	@Autowired
	private MockMvc mockMvc;


	@Test
	void testDepositSuccess() throws Exception {
		// Note: Use the UUID you seeded in Liquibase/DB
		String validJson = "{"
				+ "\"valletId\": \"550e8400-e29b-41d4-a716-446655440000\","
				+ "\"operationType\": \"DEPOSIT\","
				+ "\"amount\": 1000"
				+ "}";

		mockMvc.perform(post("/api/v1/wallet")
						.contentType(MediaType.APPLICATION_JSON)
						.content(validJson))
				.andExpect(status().isOk());
	}

	@Test
	void testGetBalanceSuccess() throws Exception {
		mockMvc.perform(get("/api/v1/wallets/550e8400-e29b-41d4-a716-446655440000"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void testGetWalletNotFound() throws Exception {
		// Checks that a random UUID returns 404
		mockMvc.perform(get("/api/v1/wallets/550e8400-e29b-41d4-a716-446655440005"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testInvalidJsonFormat() throws Exception {
		// Checks that sending "garbage" text returns 400
		mockMvc.perform(post("/api/v1/wallet")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"invalid\": \"json\" }"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testMissingFields() throws Exception {
		// Checks that missing valletId returns 400 (if you added @Valid)
		mockMvc.perform(post("/api/v1/wallet")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"operationType\": \"DEPOSIT\", \"amount\": 1000 }"))
				.andExpect(status().isBadRequest());
	}
}