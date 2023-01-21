package ee.mikk.familyfeudquestionsui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.mikk.familyfeudquestionsui.dto.FamilyFeud;
import ee.mikk.familyfeudquestionsui.dto.FamilyFeudGamePhase;
import ee.mikk.familyfeudquestionsui.dto.Question;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FamilyFeudJsonWriter {

    private static final String FILE_NAME = "newFormatQuestions.json";

    private final List<Question> baseGameQuestions;
    private final List<Question> fastMoneyQuestions;

    public FamilyFeudJsonWriter(List<Question> baseGameQuestions, List<Question> fastMoneyQuestions) {
        this.baseGameQuestions = baseGameQuestions;
        this.fastMoneyQuestions = fastMoneyQuestions;
    }

    public void writeToFile() throws IOException {
        String gameJson = getGameJson();
        FileWriter myWriter = new FileWriter(FILE_NAME);
        myWriter.write(gameJson);
        myWriter.close();
    }

    private String getGameJson() throws JsonProcessingException {
        FamilyFeud familyFeud = new FamilyFeud(new FamilyFeudGamePhase(baseGameQuestions), new FamilyFeudGamePhase(fastMoneyQuestions));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(familyFeud);
    }
}
