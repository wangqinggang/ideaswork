package cn.ideaswork.ideacoder;

import com.tencentcloudapi.aai.v20180522.models.ChatRequest;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.edit.EditRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TestAi {
    String openaikey = "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";
    @Test
    void testCompletion(){
//        String token = System.getenv(openaikey);
        System.out.println(openaikey);
        OpenAiService service = new OpenAiService(openaikey);

        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("davinci")
                .prompt("please do brain storm and generate 5 ideas in Chinese for Git 版本控制课程")
                .echo(true)
                .user("testing")
                .n(3)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);



    }

    @Test
    void test1(){
        OpenAiService service = new OpenAiService(openaikey);

        ChatMessageRole chatMessageRole = ChatMessageRole.SYSTEM;
        ChatMessage sysMessage = new ChatMessage();
        sysMessage.setRole(chatMessageRole.value());
        sysMessage.setContent("你是大师级别的 youtube 科技区网红博主");

        ChatMessageRole chatMessageRoleUser = ChatMessageRole.USER;
        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole(chatMessageRoleUser.value());
        userMessage.setContent("使用中文为如下youtube视频大纲头脑风暴出可能需要的拍摄画面,返回内容为Json数据格式\n" +
                "```\n" +
                "[ {\n" +
                "    \"title\": \"\",\n" +
                "    \"content\": \"\"\n" +
                "  }]\n" +
                "```\n" +
                "视频大纲" +
                "一、介绍视频拍摄管理软件的重要性\n" +
                "- 视频拍摄管理软件可以帮助用户更好地管理和组织视频素材，提高工作效率和质量。\n" +
                "\n" +
                "二、介绍常见的视频拍摄管理软件\n" +
                "- 简述几款常见的视频拍摄管理软件，如Adobe Premiere Pro、Final Cut Pro、DaVinci Resolve等。\n" +
                "\n" +
                "三、讲解视频拍摄管理软件的功能和特点\n" +
                "- 介绍视频拍摄管理软件的主要功能和特点，如剪辑、调色、字幕、音频处理等。\n" +
                "\n" +
                "四、分享使用视频拍摄管理软件的经验和技巧\n" +
                "- 分享使用视频拍摄管理软件的经验和技巧，如如何快速剪辑、如何调整色彩、如何添加字幕等。\n" +
                "\n" +
                "五、总结视频拍摄管理软件的优点和不足\n" +
                "- 总结视频拍摄管理软件的优点和不足，如提高工作效率、提高视频质量，但也存在学习成本高、硬件要求高等问题。\n" +
                "\n" +
                "六、推荐适合不同用户的视频拍摄管理软件\n" +
                "- 根据不同用户的需求和水平，推荐适合他们的视频拍摄管理软件，如初学者可以选择易于上手的软件，专业人士可以选择功能更强大的软件。\n" +
                "\n" +
                "七、总结\n" +
                "- 总结视频拍摄管理软件的重要性和使用建议，鼓励用户尝试使用视频拍摄管理软件提高工作效率和质量。");


        List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
        chatMessages.add(userMessage);

        ChatCompletionRequest chatRequest =
                ChatCompletionRequest.builder()
                        .model("gpt-3.5-turbo")
                        .n(1)
                        .maxTokens(2048)
                        .topP(1.0)
                        .frequencyPenalty(0.0)
                        .presencePenalty(0.0)
                        .temperature(0.0)
                        .messages(chatMessages).build();

        service.createChatCompletion(chatRequest).getChoices().forEach(System.out::println);

    }

    @Test
    void testImage() throws Exception {
//        String token = System.getenv(openaikey);
        OpenAiService service = new OpenAiService(openaikey);

        System.out.println("\nCreating Image...");
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt("A cow breakdancing with a turtle")
                .build();

        System.out.println("\nImage is located at:");
        System.out.println(service.createImage(request).getData().get(0).getUrl());
    }

}
