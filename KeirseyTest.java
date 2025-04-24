import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

public class KeirseyTest extends Application {
    private ArrayList<String> questions;
    private int currentQuestion = 0;
    private StringBuilder answers = new StringBuilder();
    private String userName = "";

    // GUI elements
    private Label questionLabel;
    private ToggleGroup options;
    private Button nextButton;
    private Label questionNumberLabel;

    @Override
    public void start(Stage primaryStage) {
        loadQuestions("questions.txt");
        
        // Create main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        // Welcome screen
        VBox welcomeScreen = createWelcomeScreen(mainLayout, primaryStage);
        
        // Set up the scene
        Scene scene = new Scene(welcomeScreen, 600, 400);
        primaryStage.setTitle("Keirsey Temperament Sorter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createWelcomeScreen(VBox mainLayout, Stage primaryStage) {
        VBox welcomeScreen = new VBox(20);
        welcomeScreen.setAlignment(Pos.CENTER);
        welcomeScreen.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome to Keirsey Temperament Sorter!");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setMaxWidth(300);

        Button startButton = new Button("Start Test");
        startButton.setOnAction(e -> {
            if (!nameField.getText().trim().isEmpty()) {
                userName = nameField.getText().trim();
                primaryStage.getScene().setRoot(createQuestionScreen(primaryStage));
            } else {
                showAlert("Please enter your name to continue.");
            }
        });

        welcomeScreen.getChildren().addAll(welcomeLabel, nameField, startButton);
        return welcomeScreen;
    }

    private VBox createQuestionScreen(Stage primaryStage) {
        VBox questionScreen = new VBox(20);
        questionScreen.setAlignment(Pos.CENTER);
        questionScreen.setPadding(new Insets(20));

        questionNumberLabel = new Label("Question " + (currentQuestion/3 + 1) + " of " + (questions.size()/3));
        questionLabel = new Label();
        options = new ToggleGroup();

        RadioButton optionA = new RadioButton();
        RadioButton optionB = new RadioButton();
        optionA.setToggleGroup(options);
        optionB.setToggleGroup(options);

        VBox optionsBox = new VBox(10, optionA, optionB);
        optionsBox.setAlignment(Pos.CENTER_LEFT);

        nextButton = new Button("Next");
        nextButton.setOnAction(e -> handleNextQuestion(primaryStage));

        questionScreen.getChildren().addAll(questionNumberLabel, questionLabel, optionsBox, nextButton);
        updateQuestion(optionA, optionB);

        return questionScreen;
    }

    private void updateQuestion(RadioButton optionA, RadioButton optionB) {
        if (currentQuestion < questions.size()) {
            questionLabel.setText(questions.get(currentQuestion));
            optionA.setText(questions.get(currentQuestion + 1));
            optionB.setText(questions.get(currentQuestion + 2));
            questionNumberLabel.setText("Question " + (currentQuestion/3 + 1) + " of " + (questions.size()/3));
            
            if (currentQuestion/3 + 1 == questions.size()/3) {
                nextButton.setText("Finish");
            }
        }
    }

    private void handleNextQuestion(Stage primaryStage) {
        if (options.getSelectedToggle() == null) {
            showAlert("Please select an answer to continue.");
            return;
        }

        RadioButton selected = (RadioButton) options.getSelectedToggle();
        answers.append(selected.getText().startsWith("(A)") ? "A" : "B");
        
        currentQuestion += 3;
        
        if (currentQuestion >= questions.size()) {
            finishTest(primaryStage);
        } else {
            options.getSelectedToggle().setSelected(false);
            updateQuestion((RadioButton)options.getToggles().get(0), 
                         (RadioButton)options.getToggles().get(1));
        }
    }

    private void finishTest(Stage primaryStage) {
        try {
            saveToFile(userName, answers.toString());
            
            VBox resultScreen = new VBox(20);
            resultScreen.setAlignment(Pos.CENTER);
            resultScreen.setPadding(new Insets(20));

            Label thankYouLabel = new Label("Thank you for completing the test!");
            Label processingLabel = new Label("Processing your results...");
            
            resultScreen.getChildren().addAll(thankYouLabel, processingLabel);
            primaryStage.getScene().setRoot(resultScreen);

            // Run the personality analysis
            Personality.main(new String[0]);
            
            // Show results from personality.out
            showResults(resultScreen);
            
        } catch (IOException ex) {
            showAlert("Error saving results: " + ex.getMessage());
        }
    }

    private void showResults(VBox resultScreen) {
        try {
            Scanner scanner = new Scanner(new File("personality.out"));
            while (scanner.hasNextLine()) {
                String result = scanner.nextLine();
                resultScreen.getChildren().add(new Label(result));
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            showAlert("Error reading results: " + ex.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadQuestions(String filename) {
        questions = new ArrayList<>();
        try {
            Scanner file = new Scanner(new File(filename));
            while (file.hasNextLine()) {
                String line = file.nextLine().trim();
                if (!line.isEmpty()) {
                    questions.add(line);
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            showAlert("Error loading questions: " + e.getMessage());
        }
    }

    private void saveToFile(String name, String answers) throws IOException {
        PrintWriter output = new PrintWriter(new FileWriter("personality"));
        output.println(name);
        output.println(answers);
        output.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}