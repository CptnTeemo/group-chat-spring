package main;

import main.dto.DtoMessage;
import main.dto.DtoUser;
import main.utils.MapperUtils;
import main.model.Message;
import main.model.MessageRepository;
import main.model.User;
import main.model.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ChatController
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    private MapperUtils mapperUtils;


    @GetMapping("/init")
    public HashMap<String, Boolean> init()
    {
        HashMap<String, Boolean> response = new HashMap<>();

        Map<String, Boolean> result = new HashMap<>();
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<User> userOpt = userRepository.findBySessionId(sessionId);

        response.put("result", userOpt.isPresent());
        return response;
    }

    @PostMapping("/auth")
    public HashMap<String, Boolean> auth(@RequestParam String name)
    {
        HashMap<String, Boolean> response = new HashMap<>();
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        User user = new User();
        user.setName(name);
        user.setSessionId(sessionId);
        userRepository.save(user);
        response.put("result", true);
        return response;
    }

    @PostMapping("/message")
    public Map<String, Boolean> sendMessage(@RequestParam String message)
    {
        if (Strings.isEmpty(message)) {
            return Map.of("result", false);
        }
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        User user = userRepository.findBySessionId(sessionId).get();

        Message msg = new Message();
        msg.setDateTime(LocalDateTime.now());
        msg.setMessage(message);
        msg.setUser(user);
        messageRepository.saveAndFlush(msg);
        return Map.of("result", true);
    }

    @GetMapping("/message")
    public List<DtoMessage> getMessagesList()
    {
        return messageRepository
                .findAll(Sort.by(Sort.Direction.ASC, "dateTime"))
                .stream()
                .map(MapperUtils::messageToDtoMessage)
                .collect(Collectors.toList());
    }

    @GetMapping("/user")
    public List<DtoUser> getUsersList()
    {
//        List<String> response = new ArrayList<>();
        List<DtoUser> dtoUserList = userRepository.findAll()
                .stream()
                .map(MapperUtils::userToDtoUser)
                .collect(Collectors.toList());
//        dtoUserList.forEach(e -> response.add(e.getUsername()));
        return dtoUserList;
    }

//    @GetMapping("/user")
//    public HashMap<Integer, String> getUsersList()
//    {
//        HashMap<Integer, String> response = new HashMap<>();
//        List<DtoUser> dtoUserList = userRepository.findAll()
//                .stream()
//                .map(MessageMapper::userToDtoUser)
//                .collect(Collectors.toList());
//        dtoUserList.forEach(e -> response.put(e.getId(), e.getUsername()));
//        return response;
//    }
}
