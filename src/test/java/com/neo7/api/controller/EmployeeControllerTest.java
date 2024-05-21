package com.neo7.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

  private String authToken;

  @Getter
  @Setter
  private class TestEmployee {

    private String firstName;

    private String lastName;

    private String mail;

    private String password;

  }

  private TestEmployee testEmployee;

  public EmployeeControllerTest() {
    this.testEmployee = new TestEmployee();
    this.testEmployee.setFirstName("Anita");
    this.testEmployee.setLastName("Anasova");
    this.testEmployee.setMail("anita.a@mail.com");
    this.testEmployee.setPassword("password");
  }

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testGetEmployeesWithoutLogin() throws Exception {
    mockMvc.perform(get("/employee/all"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testLogin() throws Exception {
    // Log in as dbuser/user
    MvcResult loginResult = mockMvc.perform(post("/login").header("Authorization", "Basic ZGJ1c2VyOnVzZXI="))
        .andExpect(status().isOk())
        .andReturn();
    assert (loginResult.getResponse().getContentAsString().length() > 0);
    this.authToken = loginResult.getResponse().getContentAsString();
  }

  @Test
  public void testBadLogin() throws Exception {
    mockMvc.perform(post("/login").header("Authorization", "Basic ZXXXXXXXXXX="))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testGetEmployees() throws Exception {
    if (this.authToken != null) {
      mockMvc.perform(get("/employee/all").header("Authorization", "Bearer " + this.authToken))
          .andExpect(status().isOk())
          // Requires this option in application.properties (if not data will not be
          // loaded): spring.jpa.defer-datasource-initialization: true
          .andExpect(jsonPath("$[0].firstName", is("Laurent")));
    }
  }

  @Test
  public void testEmployeeNotFound() throws Exception {
    if (this.authToken != null) {
      mockMvc.perform(get("/employee/999").header("Authorization", "Bearer " + this.authToken))
          .andExpect(status().isNotFound());
    }
  }

  @Test
  public void testCreateAndGetOneEmployee() throws Exception {
    if (this.authToken != null) {

      // Create an employee
      String newEmployeeJson = mockMvc
          .perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
              .header("Authorization", "Bearer " + this.authToken).content(toJsonString(this.testEmployee)))
          .andExpect(status().isCreated())
          .andExpect(header().exists("Location"))
          .andReturn().getResponse().getContentAsString();
      JSONObject jsonObject = new JSONObject(newEmployeeJson);
      int employeeId = jsonObject.getInt("id");

      // Get the created employee
      mockMvc.perform(get("/employee/" + employeeId).header("Authorization", "Bearer " + this.authToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.firstName", is("Anita")));
    }
  }

  /**
   * Converts an object to a JSON string representation.
   *
   * @param obj the object to be converted to JSON
   * @return the JSON string representation of the object
   * @throws RuntimeException if an error occurs during the conversion
   */
  public static String toJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}