package cn.ideaswork.ideacoder.domain.vms.ai;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.web.bind.annotation.GetMapping;

// @RestController
// @RequestMapping("ai")
public class TestController {

    @GetMapping
    public String test() {
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);

        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .user("testing")
                .n(3)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

//        System.out.println("\nCreating Image...");
//        CreateImageRequest request = CreateImageRequest.builder()
//                .prompt("A cow breakdancing with a turtle")
//                .build();
//
//        System.out.println("\nImage is located at:");
//        System.out.println(service.createImage(request).getData().get(0).getUrl());
        return "hello";
    }
}