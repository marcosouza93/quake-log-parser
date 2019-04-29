package br.com.labs.quakelogparser.http;

import br.com.labs.quakelogparser.domain.Game;
import br.com.labs.quakelogparser.http.json.GameDetailJsonResponse;
import br.com.labs.quakelogparser.http.json.mapper.GameDetailJsonResponseMappers.GameDetailJsonResponseMapper;
import br.com.labs.quakelogparser.usecase.GameReportGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/report")
@Api(tags = "GameDetailJsonResponse Report", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameReportController {

  @Autowired private GameReportGenerator gameReportGenerator;

  @ApiOperation(value = "Resource to generate a report through Quake game logs")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 422, message = "Unprocessable Entity"),
        @ApiResponse(code = 500, message = "Internal Server Error")
      })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<GameDetailJsonResponse>> getGameReport() throws Exception {

    List<Game> games = gameReportGenerator.generate();

    return new ResponseEntity<>(buildListOrderJson(games), HttpStatus.OK);
  }

  private List<GameDetailJsonResponse> buildListOrderJson(final List<Game> games) {
    return GameDetailJsonResponseMapper.MAPPER.gameToGameDetailJsonResponse(games);
  }
}
