package by.it.academy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
public class MiningController {


    private static final Logger log = LoggerFactory.getLogger(MiningController.class);

    @RequestMapping(value = "/{userId}/wallet/{walletId}/mining", method = RequestMethod.GET)
    public ModelAndView createWallet(ModelAndView modelAndView,
                                     @PathVariable String userId,
                                     @PathVariable String walletId) throws IOException, InterruptedException {


        String body = createPostBody(walletId);
        modelAndView.setViewName("mining");
        final HttpResponse<String> httpResponse = post(body, userId, walletId);
        log.info("Mining session start response:" +  httpResponse.body());
        return modelAndView;
    }

    private String createPostBody(String walletId) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n" +
                "  \"blockId\": \"none\",\n" +
                "  \"minerId\": \"none\",\n" +
                "  \"minerReward\": \"none\",\n" +
                "  \"miningSessionStatus\": \"none\",\n" +
                "  \"sessionEnd\": \"none\",\n" +
                "  \"sessionStart\": \"none\",\n" +
                "  \"walletId\": \"");
        sb.append(walletId);
        sb.append("\"\n" +
                "}");
        return sb.toString();
    }

    private HttpResponse<String> post(String body, String userId, String walletId) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create("http://localhost:8081/blockchain-mining/new-session"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return httpResponse;
    }

}
