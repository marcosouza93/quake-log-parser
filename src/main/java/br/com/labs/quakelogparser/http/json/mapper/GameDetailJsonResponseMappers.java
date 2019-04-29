package br.com.labs.quakelogparser.http.json.mapper;

import br.com.labs.quakelogparser.domain.Game;
import br.com.labs.quakelogparser.http.json.GameDetailJsonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@FunctionalInterface
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GameDetailJsonResponseMappers {

  List<GameDetailJsonResponse> gameToGameDetailJsonResponse(List<Game> games);

  enum GameDetailJsonResponseMapper {
    ;

    public static final GameDetailJsonResponseMappers MAPPER =
        Mappers.getMapper(GameDetailJsonResponseMappers.class);
  }
}
