package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public record PlannedItemJson (

    @JsonProperty("type") String type,
    @JsonProperty("detail") JsonNode detail) {

}

