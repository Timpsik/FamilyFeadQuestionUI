package ee.mikk.familyfeudquestionsui.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record Question (String question,
                        List<Answer> answers){}
