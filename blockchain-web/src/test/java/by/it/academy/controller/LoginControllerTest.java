package by.it.academy.controller;

import by.it.academy.ApplicationConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.junit.Assert.fail;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
public class LoginControllerTest {

    @Autowired
    HomeController homeController;
    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loginPage() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/login-distributor");
        request.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE,
                new FlashMap().addTargetRequestParam("userId", "testUserId"));

        MockHttpServletResponse response = new MockHttpServletResponse();

        Object handler;

        try {
            handler = handlerMapping.getHandler(request).getHandler();

            ModelAndView modelAndView = handlerAdapter.handle(request,
                    response, handler);

            assertViewName(modelAndView, "redirect:/{userId}/wallet-all");
        } catch (Exception e) {
            String err = "Error executing controller : " + e.toString();
            fail(err);
        }
    }
}