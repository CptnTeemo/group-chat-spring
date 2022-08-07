package main.utils;

import main.dto.DtoMessage;
import main.dto.DtoUser;
import main.model.Message;
import main.model.User;

public class MapperUtils {

    public static DtoMessage messageToDtoMessage(Message message){
        DtoMessage dtoMessage = new DtoMessage();
        dtoMessage.setDatetime(message.getDateTime().toString());
        dtoMessage.setUsername(message.getUser().getName());
        dtoMessage.setText(message.getMessage());
        return dtoMessage;
    }

    public static DtoUser userToDtoUser(User user) {
        DtoUser dtoUser = new DtoUser();
        dtoUser.setId(user.getId());
        dtoUser.setUsername(user.getName());
        return dtoUser;
    }
}
