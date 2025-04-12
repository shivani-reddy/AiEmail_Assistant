package com.email.AIEmail_writer.Emailservice;

import com.email.AIEmail_writer.model.Emailrequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class emailService {
    private final WebClient webClient;
    @Value("${gemini.api.url}")
    private String geminiAPiUrl;

    @Value("${gemini.api.key}")
    private String getGeminiAPikey;

    public emailService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateEmailReply(Emailrequest emailContent) {
        //Build the prompt
        String prompt=buildPrompt(emailContent);
        //craft a request
        Map<String,Object> requestBody= Map.of("contents",new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                });

        //Do request and get response
        //web cilent is used to make the request , async which is webflux
         String response=webClient.post()
                .uri(geminiAPiUrl+getGeminiAPikey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //return response
        return extractResponse(response);
    }

    private String extractResponse(String response) {
        try{
            ObjectMapper mapper=new ObjectMapper();
            JsonNode rootNode=mapper.readTree(response);
            return rootNode.get("candidates").get(0).path("content").
                    path("parts").get(0).get("text").asText();
        }
        catch(Exception e){
            return "error processing request:"+e.getMessage();
        }
    }

    private String buildPrompt(Emailrequest emailContent) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("generate a professional email response to the following email content.Please dont not generate the subject line \n");
        if(emailContent.getTone()!=null&&!emailContent.getTone().isEmpty()){
            prompt.append("use a ").append(emailContent.getTone()).append(" tone");
        }
        prompt.append("\nOriginal email:\n").append(emailContent.getEmailContent());
        return prompt.toString();
    }
}
