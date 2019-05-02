package br.com.labs.quakelogparser.http;

import br.com.labs.quakelogparser.usecase.GameReportGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {GameReportController.class})
public class GameReportControllerUnitTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private GameReportGenerator gameReportGenerator;

  @Test
  public void getGameReportSuccessfully() throws Exception {
    final MockHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.get("/api/report")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(builder).andExpect(status().isOk());

    verify(this.gameReportGenerator, VerificationModeFactory.times(1)).generate();
  }
}
