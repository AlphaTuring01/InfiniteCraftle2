package lab.prog.infinitecraftle;

import androidx.annotation.NonNull;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class GPT {
    private URL url;
    private final String apiKey;
    private final int maxTokens;
    private final String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \" %s \"}], \"temperature\": %f, \"max_tokens\": %d}";
    private final float temperature;
    private String fewShotLearningTemplate;
    private int counter = 0;

    /**
     * Constructor for GPT
     * @param apiKey API key for the OpenAI API
     * @param maxTokens Maximum number of tokens to generate
     * @param temperature Temperature for the model
     */
    public GPT(String apiKey, int maxTokens, float temperature) {
        try {
            this.url = new URI("https://api.openai.com/v1/chat/completions").toURL();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.apiKey = apiKey;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
    }

    /**
     * Get the prompt template from the txt file
     * @return The prompt template
     */
    private String getPromptTemplate(){
        StringBuilder sb = new StringBuilder();
        try {
            String filePath = "prompt.txt";
            String absolutePath = GPT.class.getClassLoader().getResource(filePath).getFile();
            File myObj = new File(absolutePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                sb.append(myReader.nextLine());
                sb.append("  ");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Format the question for the prompt
     * @param op1 First operand
     * @param op2 Second operand
     * @return The prompt with the question added
     */
    public String addQuestion(String op1, String op2){
        String prompt = getPromptTemplate();
        return prompt.replace("<input>", String.format("%s, %s", op1, op2));
    }

    /**
     * Retrieve the answer to a prompt
     * @param op1 First operand
     * @param op2 Second operand
     * @return The answer to the question
     */
    public String retrieveAnswer(String op1, String op2){
        String question = addQuestion(op1, op2);
        String answer = retrieveFullAnswer(question);
        return answer.substring(answer.indexOf("content") + 11, answer.indexOf("\"", answer.indexOf("content") + 11));
    }

    /**
     * Get the new craft from the model. This is a helper function that calls retrieveAnswer
     * @param parent1 First parent
     * @param parent2 Second parent
     * @param emoji1 Emoji for the first parent
     * @param emoji2 Emoji for the second parent
     * @return The new craft
     * If the model returns "None", return null
     */
    public String[] getNewCraft(String parent1, String parent2, String emoji1, String emoji2){
        String ret = retrieveAnswer(emoji1 + parent1, emoji2 + parent2);
        if(ret.equals("None")){
            String[] str = new String[2];
            str[0] = str[1] = null;
            return str;
        }
        return ret.split(" ");
    }

    /**
     * Formats the message to be sent to the OpenAI API
     * @param msg Message to be sent
     * @return Formatted message
     */
    private String formatSendMessage(String msg){
        return String.format(requestBody, msg, temperature, maxTokens);
    }

    /**
     * Retrieves the full answer from the OpenAI API
     * @param msg Message to be sent
     * @return The full answer, a JSON in String format
     */
    public String retrieveFullAnswer(String msg) {
        HttpURLConnection conn = getHttpURLConnection();
        System.out.println("Request: " + msg);
        System.out.println(apiKey);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = formatSendMessage(msg).getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        conn.disconnect();

        return response.toString();
    }

    @NonNull
    private HttpURLConnection getHttpURLConnection() {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setDoOutput(true);
        return conn;
    }

}
