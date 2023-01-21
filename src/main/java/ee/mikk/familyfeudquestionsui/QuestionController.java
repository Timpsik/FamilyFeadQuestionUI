package ee.mikk.familyfeudquestionsui;

import ee.mikk.familyfeudquestionsui.dto.Answer;
import ee.mikk.familyfeudquestionsui.dto.Question;
import ee.mikk.familyfeudquestionsui.exception.InvalidQuestionException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class QuestionController {
    @FXML
    private Label questionCounterLabel;

    @FXML
    private TextArea question;

    @FXML
    private Button saveQuestionButton;

    @FXML
    private Button startFastMoneyButton;

    @FXML
    private TextField answer1;

    @FXML
    private TextField points1;

    @FXML
    private TextField answer2;

    @FXML
    private TextField points2;

    @FXML
    private TextField answer3;

    @FXML
    private TextField points3;

    @FXML
    private TextField answer4;

    @FXML
    private TextField points4;

    @FXML
    private TextField answer5;

    @FXML
    private TextField points5;

    @FXML
    private TextField answer6;

    @FXML
    private TextField points6;

    @FXML
    private TextField answer7;

    @FXML
    private TextField points7;

    @FXML
    private TextField answer8;

    @FXML
    private TextField points8;

    private int questionCounter = 1;

    private boolean isFastMoneyQuestions = false;

    private final List<Question> baseGameQuestions = new ArrayList<>();

    private final List<Question> fastMoneyQuestions = new ArrayList<>();

    private final List<AnswerAndPointsInput> answerAndPointsInputs = new ArrayList<>();

    public void initialize() {
        setQuestionCounterLabelText();

        answerAndPointsInputs.add(createAnswerAndPointsInput(answer1, points1));
        answerAndPointsInputs.add(createAnswerAndPointsInput(answer2, points2));
        answerAndPointsInputs.add(createAnswerAndPointsInput(answer3, points3));
        answerAndPointsInputs.add(createAnswerAndPointsInput(answer4, points4));
        answerAndPointsInputs.add(createAnswerAndPointsInput(answer5, points5));
        answerAndPointsInputs.add(createAnswerAndPointsInput(answer6, points6));
        answerAndPointsInputs.add(createAnswerAndPointsInput(answer7, points7));
        answerAndPointsInputs.add(createAnswerAndPointsInput(answer8, points8));
    }

    private AnswerAndPointsInput createAnswerAndPointsInput(TextField answer, TextField points) {
        return AnswerAndPointsInput.builder()
                .answer(answer)
                .points(points)
                .build();
    }

    @FXML
    protected void onSaveQuestionClick() {
        try {
            saveQuestion();

            questionCounter++;
            setQuestionCounterLabelText();

            clearAnswersAndPoints();
            question.clear();

            if (isFastMoneyQuestions && questionCounter == 5) {
                saveQuestionButton.setVisible(false);
                startFastMoneyButton.setDisable(false);
            }
        } catch (InvalidQuestionException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Vigane küsimus: %s".formatted(e.getMessage()), ButtonType.OK);
            alert.setTitle("Vigane küsimus");
            alert.showAndWait();
        }

    }

    private void saveQuestion() throws InvalidQuestionException {
        Question createdQuestion = validateAndCreateQuestion();
        if (isFastMoneyQuestions) {
            fastMoneyQuestions.add(createdQuestion);
        } else {
            baseGameQuestions.add(createdQuestion);
        }
    }

    private Question validateAndCreateQuestion() throws InvalidQuestionException {
        if (isNotValidQuestion()) {
            throw new InvalidQuestionException("Not valid question");
        }

        return Question.builder()
                .question(question.getText())
                .answers(getAnswers())
                .build();
    }

    private boolean isNotValidQuestion() throws InvalidQuestionException {
        if (isBlank(question.getText())) {
            throw new InvalidQuestionException("Küsimuse tekst ei tohi olla tühi");
        }

        int indexOfFirstEmptyInput = 10;
        int previousPoints = Integer.MAX_VALUE;
        for (int i = 0; i < 8; i++) {
            AnswerAndPointsInput answerAndPointsInput = answerAndPointsInputs.get(i);
            if (isEmptyInput(answerAndPointsInput) && i < 4) {
                throw new InvalidQuestionException("Vähemalt 4 vastust peab olema küsimusel");
            }

            if (!isEmptyInput(answerAndPointsInput) && isNotValidPoints(answerAndPointsInput.points)) {
                throw new InvalidQuestionException("Vastuse %d punktid on tühi või vigane number".formatted(i+1));
            }

            if (!isEmptyInput(answerAndPointsInput) && isMorePointsThanPreviousAnswer(answerAndPointsInput.points, previousPoints)) {
                throw new InvalidQuestionException("Vastus %d annab rohkem punkte kui Vastus %d".formatted(i+1, i));
            }

            if (!isEmptyInput(answerAndPointsInput)) {
                previousPoints = Integer.parseInt(answerAndPointsInput.points.getText());
            }

            if (isEmptyInput(answerAndPointsInput)) {
                indexOfFirstEmptyInput = i;
            }

            if (!isEmptyInput(answerAndPointsInput) && i > indexOfFirstEmptyInput) {
                throw new InvalidQuestionException("Vastus %d oli tühi, kuid pärast seda on veel vastuseid sisestatud. Täida puuduv vastus enne."
                        .formatted(indexOfFirstEmptyInput + 1));
            }
        }

        return false;
    }

    private boolean isMorePointsThanPreviousAnswer(TextField points, int previousPoints) {
        return Integer.parseInt(points.getText()) > previousPoints;
    }

    private boolean isEmptyInput(AnswerAndPointsInput answerAndPointsInput) {
        return isBlank(answerAndPointsInput.answer.getText());
    }

    private boolean isNotValidPoints(TextField points) {
        return isBlank(points.getText()) || !Util.isNumeric(points.getText());
    }

    private List<Answer> getAnswers() {
        List<Answer> answers = new ArrayList<>();
        for (AnswerAndPointsInput input : answerAndPointsInputs) {
            if (isBlank(input.answer.getText())) {
                break;
            }

            answers.add(Answer.builder()
                    .value(input.answer.getText())
                    .points(Integer.valueOf(input.points.getText()))
                    .build());
        }
        return answers;
    }

    @FXML
    protected void onEndGamePhaseClick() {
        if (!isFastMoneyQuestions) {
            questionCounter = 1;
            isFastMoneyQuestions = true;
            setQuestionCounterLabelText();

            clearAnswersAndPoints();
            question.clear();
            startFastMoneyButton.setText("Salvesta ja sulge");
            startFastMoneyButton.setDisable(true);
        } else {
            try {
                saveQuestion();
            } catch (InvalidQuestionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Vigane küsimus: %s".formatted(e.getMessage()), ButtonType.OK);
                alert.setTitle("Vigane küsimus");
                alert.showAndWait();
            }

            try {
                saveQuestionsToJson();
                ((Stage) startFastMoneyButton.getScene().getWindow()).close();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ei õnnestunud faili kirjutada mängu: ", ButtonType.OK);
                alert.setTitle("Unknown Exception");
                alert.showAndWait();
            }
        }
    }

    private void saveQuestionsToJson() throws IOException {
        FamilyFeudJsonWriter jsonWriter = new FamilyFeudJsonWriter(baseGameQuestions, fastMoneyQuestions);
        jsonWriter.writeToFile();
    }

    private void setQuestionCounterLabelText() {
        questionCounterLabel.setText(getQuestionLabel().formatted(questionCounter));
    }

    private String getQuestionLabel() {
        return isFastMoneyQuestions ? "FastMoney Küsimus %d/5" :"Küsimus %d";
    }

    private void clearAnswersAndPoints() {
        answer1.clear();
        answer2.clear();
        answer3.clear();
        answer4.clear();
        answer5.clear();
        answer6.clear();
        answer7.clear();
        answer8.clear();

        points1.clear();
        points2.clear();
        points3.clear();
        points4.clear();
        points5.clear();
        points6.clear();
        points7.clear();
        points8.clear();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class AnswerAndPointsInput {
        private TextField answer;

        private TextField points;
    }
}