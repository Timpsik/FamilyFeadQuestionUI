package ee.mikk.familyfeudquestionsui.dto;

import lombok.Builder;

@Builder
public record Answer(String value,
                     int points) {
}
