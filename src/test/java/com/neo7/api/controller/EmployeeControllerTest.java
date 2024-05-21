package com.neo7.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo7.api.model.Employee;
import com.neo7.api.model.Ward;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class EmployeeControllerTest {

  private static String authToken;

  private Employee testEmployee;

  public EmployeeControllerTest() {
    this.testEmployee = new Employee();
    this.testEmployee.setFirstName("Anita");
    this.testEmployee.setLastName("Anasova");
    this.testEmployee.setMail("anita.a@mail.com");
    this.testEmployee.setPassword("password");
    this.testEmployee.addWard(new Ward("Ward 1"));
  }

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Order(1)
  public void testGetEmployeesWithoutLogin() throws Exception {
    mockMvc.perform(get("/employee/all"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(1)
  public void testBadLogin() throws Exception {
    mockMvc.perform(post("/login").header("Authorization", "Basic ZXXXXXXXXXX="))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(2)
  public void testLogin() throws Exception {
    // Log in as dbuser/user
    MvcResult loginResult = mockMvc.perform(post("/login").header("Authorization", "Basic ZGJ1c2VyOnVzZXI="))
        .andExpect(status().isOk())
        .andReturn();
    assert (loginResult.getResponse().getContentAsString().length() > 0);
    authToken = loginResult.getResponse().getContentAsString();
  }

  @Test
  @Order(3)
  public void testGetEmployees() throws Exception {
    if (authToken == null) {
      fail("This test requires login and a valid authentication token");
    }
    mockMvc.perform(get("/employee/all").header("Authorization", "Bearer " + authToken))
        .andExpect(status().isOk())
        // Requires this option in application.properties (if not data will not be
        // loaded): spring.jpa.defer-datasource-initialization: true
        .andExpect(jsonPath("$[0].firstName", is("Laurent")));

  }

  @Test
  @Order(3)
  public void testEmployeeNotFound() throws Exception {
    if (authToken == null) {
      fail("This test requires login and a valid authentication token");
    }
    mockMvc.perform(get("/employee/999").header("Authorization", "Bearer " + authToken))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(3)
  public void testCreateAndGetOneEmployee() throws Exception {
    if (authToken == null) {
      fail("This test requires login and a valid authentication token");
    }

    // Create an employee
    String newEmployeeURIasString = mockMvc
        .perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + authToken).content(toJsonString(this.testEmployee)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andReturn().getResponse().getHeader("Location");
    int employeeId = Integer.parseInt(newEmployeeURIasString.substring(newEmployeeURIasString.lastIndexOf("/") + 1));

    // Get the created employee
    mockMvc.perform(get("/employee/" + employeeId).header("Authorization", "Bearer " + authToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName", is("Anita")));
  }

  @Test
  @Order(3)
  public void testUpdateEmployee() throws Exception {
    // if (authToken == null) {
    // fail("This test requires login and a valid authentication token");
    // }

    // // Update an employee
    // this.testEmployee.setFirstName("Johnny");
    // mockMvc
    // .perform(put("/employee/4").contentType(MediaType.APPLICATION_JSON)
    // .header("Authorization", "Bearer " +
    // authToken).content(toJsonString(this.testEmployee)))
    // .andExpect(status().isOk());

    // // Get the created employee
    // mockMvc.perform(get("/employee/4").header("Authorization", "Bearer " +
    // authToken))
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$.firstName", is("Johnny")));
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